package no.acntech.seaturtle.receiver.kafka.consumer.spark;

import kafka.serializer.Decoder;
import no.acntech.seaturtle.receiver.kafka.KafkaClient;
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

public abstract class KafkaSparkMessageConsumer<K, V, KD extends Decoder<K>, VD extends Decoder<V>> extends KafkaClient implements Serializable {

    private static final String CONSUMER_PROPERTIES_FILE = "consumer.properties";
    private static final String SPARK_MASTER = "local[*]";
    private static final long SPARK_BATCH_DURATION_MILLIS = 2000;
    private final String[] topicNames;

    protected KafkaSparkMessageConsumer(String... topicNames) {
        this.topicNames = topicNames;
    }

    protected void consumeRecords() {
        Set<String> topics = Arrays.stream(topicNames).collect(Collectors.toSet());
        Map<String, String> config = readConfig(CONSUMER_PROPERTIES_FILE);
        SparkConf conf = new SparkConf().setAppName(this.getClass().getSimpleName()).setMaster(SPARK_MASTER);
        try (JavaStreamingContext ssc = new JavaStreamingContext(new JavaSparkContext(conf), new Duration(SPARK_BATCH_DURATION_MILLIS))) {
            JavaPairInputDStream<K, V> directKafkaStream = KafkaUtils.createDirectStream(ssc, keyClass(), valueClass(), keyDecoderClass(), valueDecoderClass(), config, topics);
            directKafkaStream.foreachRDD(this::consumeRecord);
            ssc.start();
            ssc.awaitTermination();
        }
    }

    protected abstract void consumeRecord(JavaPairRDD<K, V> rdd);

    protected abstract Class<K> keyClass();

    protected abstract Class<V> valueClass();

    protected abstract Class<KD> keyDecoderClass();

    protected abstract Class<VD> valueDecoderClass();
}
