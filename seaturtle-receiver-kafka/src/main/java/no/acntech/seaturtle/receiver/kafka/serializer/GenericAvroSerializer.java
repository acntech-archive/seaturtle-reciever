package no.acntech.seaturtle.receiver.kafka.serializer;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class GenericAvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T message) {
        return encoder().toBytes(message);
    }

    @Override
    public void close() {
    }

    protected GenericAvroEncoder<T> encoder() {
        return new GenericAvroEncoder<>();
    }
}
