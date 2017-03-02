package no.acntech.seaturtle.receiver.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaSingleThreadedMessageConsumer extends KafkaMessageConsumer<String, String> {

    private static final String TOPIC = "heartbeat";

    public KafkaSingleThreadedMessageConsumer(String... topicNames) {
        super(topicNames);
    }

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedMessageConsumer(TOPIC).consumeRecords();
    }

    @Override
    protected Deserializer<String> createKeyDeserializer() {
        return new StringDeserializer();
    }

    @Override
    protected Deserializer<String> createValueDeserializer() {
        return new StringDeserializer();
    }
}