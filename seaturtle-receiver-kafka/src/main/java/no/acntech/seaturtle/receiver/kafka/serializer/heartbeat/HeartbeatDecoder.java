package no.acntech.seaturtle.receiver.kafka.serializer.heartbeat;

import kafka.utils.VerifiableProperties;
import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.serializer.AbstractAvroDecoder;
import org.apache.avro.Schema;

public class HeartbeatDecoder extends AbstractAvroDecoder<Heartbeat> {

    public HeartbeatDecoder() {
    }

    public HeartbeatDecoder(VerifiableProperties verifiableProperties) {
        super(verifiableProperties);
    }

    @Override
    protected Schema schema() {
        return Heartbeat.getClassSchema();
    }
}
