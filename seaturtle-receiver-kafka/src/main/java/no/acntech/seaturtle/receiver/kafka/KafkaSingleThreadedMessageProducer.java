package no.acntech.seaturtle.receiver.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaSingleThreadedMessageProducer extends KafkaMessageProducer<String, String> {

    private static final String TOPIC = "heartbeat";
    private static final int RECORD_COUNT = 1000000;

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedMessageProducer().produceRecords(TOPIC, RECORD_COUNT);
    }

    @Override
    protected ProducerRecord<String, String> produceRecord(String topic, int i) {
        return new ProducerRecord<>(topic, "Key " + i, "Value " + i);
    }

    @Override
    protected Serializer<String> createKeySerializer() {
        return new StringSerializer();
    }

    @Override
    protected Serializer<String> createValueSerializer() {
        return new StringSerializer();
    }
}
