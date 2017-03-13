package no.acntech.seaturtle.receiver.kafka.consumer.spark;

import kafka.serializer.StringDecoder;
import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.serializer.heartbeat.HeartbeatDecoder;
import org.apache.spark.api.java.JavaPairRDD;

import java.io.Serializable;

public class KafkaSparkAvroMessageConsumer extends KafkaSparkMessageConsumer<String, Heartbeat, StringDecoder, HeartbeatDecoder> implements Serializable {

    private static final String TOPIC = "heartbeat";

    private KafkaSparkAvroMessageConsumer(String... topicNames) {
        super(topicNames);
    }

    @Override
    protected void consumeRecord(JavaPairRDD<String, Heartbeat> rdd) {
        if (rdd.count() > 0) {
            logger.debug("--- Starting consumption of {} RDDs from {} partitions", rdd.count(), rdd.partitions().size());
        } else {
            logger.trace("--- No RDDs from {} partitions", rdd.partitions().size());
        }
        rdd.foreach(record -> logger.trace("--- Key: {}, Value: timestamp={} event={} remote={}", record._1, record._2.getTimestamp(), record._2.getEvent(), record._2.getRemote()));
        if (rdd.count() > 0) {
            logger.debug("--- Completed consumption of {} RDDs from {} partitions", rdd.count(), rdd.partitions().size());
        }
    }

    @Override
    protected Class<String> keyClass() {
        return String.class;
    }

    @Override
    protected Class<Heartbeat> valueClass() {
        return Heartbeat.class;
    }

    @Override
    protected Class<StringDecoder> keyDecoderClass() {
        return StringDecoder.class;
    }

    @Override
    protected Class<HeartbeatDecoder> valueDecoderClass() {
        return HeartbeatDecoder.class;
    }

    public static void main(String[] args) throws Exception {
        new KafkaSparkAvroMessageConsumer(TOPIC).consumeRecords();
    }
}
