package no.acntech.seaturtle.receiver.kafka.consumer.spark;

import kafka.serializer.StringDecoder;
import org.apache.spark.api.java.JavaPairRDD;

import java.io.Serializable;

public class KafkaSparkTextMessageConsumer extends KafkaSparkMessageConsumer<String, String, StringDecoder, StringDecoder> implements Serializable {

    private static final String TOPIC = "heartbeat";

    private KafkaSparkTextMessageConsumer(String... topicNames) {
        super(topicNames);
    }

    @Override
    protected void consumeRecord(JavaPairRDD<String, String> rdd) {
        logger.trace("--- New RDD with {} partitions and {} records", rdd.partitions().size(), rdd.count());
        rdd.foreach(record -> {
            logger.info("--- Key: {}, Value: {}", record._1, record._2);
        });
    }

    @Override
    protected Class<String> keyClass() {
        return String.class;
    }

    @Override
    protected Class<String> valueClass() {
        return String.class;
    }

    @Override
    protected Class<StringDecoder> keyDecoderClass() {
        return StringDecoder.class;
    }

    @Override
    protected Class<StringDecoder> valueDecoderClass() {
        return StringDecoder.class;
    }

    public static void main(String[] args) throws Exception {
        new KafkaSparkTextMessageConsumer(TOPIC).consumeRecords();
    }
}
