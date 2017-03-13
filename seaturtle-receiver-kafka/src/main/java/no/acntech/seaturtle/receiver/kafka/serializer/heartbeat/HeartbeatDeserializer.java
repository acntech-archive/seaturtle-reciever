package no.acntech.seaturtle.receiver.kafka.serializer.heartbeat;

import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.serializer.AbstractAvroDecoder;
import no.acntech.seaturtle.receiver.kafka.serializer.AbstractAvroDeserializer;

public class HeartbeatDeserializer extends AbstractAvroDeserializer<Heartbeat> {

    @Override
    protected AbstractAvroDecoder<Heartbeat> decoder() {
        return new HeartbeatDecoder();
    }
}
