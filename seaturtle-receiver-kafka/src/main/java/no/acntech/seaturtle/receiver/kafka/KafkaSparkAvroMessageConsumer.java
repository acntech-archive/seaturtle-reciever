package no.acntech.seaturtle.receiver.kafka;

import kafka.serializer.StringDecoder;
import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.serializer.heartbeat.HeartbeatDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KafkaSparkAvroMessageConsumer extends KafkaClient implements Serializable {

    private static final String CONSUMER_PROPERTIES_FILE = "consumer.properties";
    private static final String TOPIC = "heartbeat";

    private KafkaSparkAvroMessageConsumer(String... topicNames) {
        Set<String> topics = Arrays.stream(topicNames).collect(Collectors.toSet());
        consumeRecords(topics);
    }

    public static void main(String[] args) throws Exception {
        new KafkaSparkAvroMessageConsumer(TOPIC);
    }

    private void consumeRecords(Set<String> topics) {
        Map<String, String> config = readConfig(CONSUMER_PROPERTIES_FILE);
        SparkConf conf = new SparkConf().setAppName(this.getClass().getSimpleName()).setMaster("local[*]");
        try (JavaStreamingContext ssc = new JavaStreamingContext(new JavaSparkContext(conf), new Duration(2000))) {
            JavaPairInputDStream<String, Heartbeat> directKafkaStream = KafkaUtils.createDirectStream(ssc, String.class, Heartbeat.class, StringDecoder.class, HeartbeatDecoder.class, config, topics);
            directKafkaStream.foreachRDD(this::consumeRecord);
            ssc.start();
            ssc.awaitTermination();
        }
    }

    private void consumeRecord(JavaPairRDD<String, Heartbeat> rdd) {
        logger.info("--- New RDD with {} partitions and {} records", rdd.partitions().size(), rdd.count());
        rdd.foreach(record -> {
            logger.info("--- Key: {}, Value: timestamp={} event={} remote={}", record._1, record._2.getTimestamp(), record._2.getEvent(), record._2.getRemote());
        });
    }
}
