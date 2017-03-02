package no.acntech.seaturtle.receiver.kafka;

import kafka.serializer.StringDecoder;
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

public class KafkaSparkMessageConsumer extends KafkaClient implements Serializable {

    private static final String CONSUMER_PROPERTIES_FILE = "consumer.properties";
    private static final String TOPIC = "heartbeat";
    private final String[] topicNames;

    private KafkaSparkMessageConsumer(String... topicNames) {
        this.topicNames = topicNames;
    }

    public static void main(String[] args) throws Exception {
        new KafkaSparkMessageConsumer(TOPIC).consumeRecords();
    }

    private void consumeRecords() {
        Set<String> topics = Arrays.stream(topicNames).collect(Collectors.toSet());
        Map<String, String> config = readConfig(CONSUMER_PROPERTIES_FILE);
        SparkConf conf = new SparkConf().setAppName(this.getClass().getSimpleName()).setMaster("local[*]");
        try (JavaStreamingContext ssc = new JavaStreamingContext(new JavaSparkContext(conf), new Duration(2000))) {
            JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(ssc, String.class, String.class, StringDecoder.class, StringDecoder.class, config, topics);
            directKafkaStream.foreachRDD(this::consumeRecord);
            ssc.start();
            ssc.awaitTermination();
        }
    }

    private void consumeRecord(JavaPairRDD<String, String> rdd) {
        logger.info("--- New RDD with {} partitions and {} records", rdd.partitions().size(), rdd.count());
        rdd.foreach(record -> {
            logger.info("--- Key: {}, Value: {}", record._1, record._2);
        });
    }
}
