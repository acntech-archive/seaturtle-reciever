package no.acntech.seaturtle.receiver.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaSingleThreadedTextMessageProducer extends KafkaMessageProducer<String, String> {

    private static final String TOPIC = "heartbeat";
    private static final int RECORD_COUNT = 1000000;

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

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedTextMessageProducer().produceRecords(TOPIC, RECORD_COUNT);
    }
}
