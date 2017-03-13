package no.acntech.seaturtle.receiver.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaSingleThreadedTextMessageConsumer extends KafkaMessageConsumer<String, String, String> {

    private static final String TOPIC = "heartbeat";

    private KafkaSingleThreadedTextMessageConsumer(String... topicNames) {
        super(topicNames);
    }

    @Override
    protected String consumeRecord(ConsumerRecord<String, String> record) {
        logger.info("--- Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
        return record.value();
    }

    @Override
    protected Deserializer<String> createKeyDeserializer() {
        return new StringDeserializer();
    }

    @Override
    protected Deserializer<String> createValueDeserializer() {
        return new StringDeserializer();
    }

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedTextMessageConsumer(TOPIC).consumeRecords();
    }
}