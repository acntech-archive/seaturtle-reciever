package no.acntech.seaturtle.receiver.kafka.consumer;

import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.serializer.KafkaSerializationException;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;

public class KafkaSingleThreadedAvroMessageConsumer extends KafkaMessageConsumer<String, byte[], Heartbeat> {

    private static final String TOPIC = "heartbeat";

    private KafkaSingleThreadedAvroMessageConsumer(String... topicNames) {
        super(topicNames);
    }

    @Override
    protected Heartbeat consumeRecord(ConsumerRecord<String, byte[]> record) {
        logger.trace("Topic: {}, Partition: {}, Offset: {}, Key: {}, Value Length: {}", record.topic(), record.partition(), record.offset(), record.key(), record.value().length);
        try {
            SpecificDatumReader<Heartbeat> reader = new SpecificDatumReader<>(Heartbeat.getClassSchema());
            Decoder decoder = DecoderFactory.get().binaryDecoder(record.value(), null);
            Heartbeat message = reader.read(null, decoder);
            logger.trace("Key: {}, Value: timestamp={} event={} remote={}", record.key(), message.getTimestamp(), message.getEvent(), message.getRemote());
            return message;
        } catch (IOException e) {
            throw new KafkaSerializationException("Could not decode Avro message", e);
        }
    }

    @Override
    protected Deserializer<String> createKeyDeserializer() {
        return new StringDeserializer();
    }

    @Override
    protected Deserializer<byte[]> createValueDeserializer() {
        return new ByteArrayDeserializer();
    }

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedAvroMessageConsumer(TOPIC).consumeRecords();
    }
}