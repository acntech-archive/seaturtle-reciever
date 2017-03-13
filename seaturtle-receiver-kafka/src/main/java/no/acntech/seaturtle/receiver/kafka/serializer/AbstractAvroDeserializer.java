package no.acntech.seaturtle.receiver.kafka.serializer;

import org.apache.avro.generic.GenericContainer;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public abstract class AbstractAvroDeserializer<T extends GenericContainer> implements Deserializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public T deserialize(String topic, byte[] payload) {
        return decoder().fromBytes(payload);
    }

    @Override
    public void close() {
    }

    protected abstract AbstractAvroDecoder<T> decoder();
}
