package no.acntech.seaturtle.receiver.kafka.serializer;

import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;
import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.KafkaException;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.IOException;

public class HeartbeatDecoder implements Decoder<Heartbeat> {

    public HeartbeatDecoder(VerifiableProperties props) {
    }

    @Override
    public Heartbeat fromBytes(byte[] bytes) {
        try {
            SpecificDatumReader<Heartbeat> reader = new SpecificDatumReader<>(Heartbeat.getClassSchema());
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
            return reader.read(null, decoder);
        } catch (IOException e) {
            throw new KafkaException("Unable to read from decoder", e);
        }
    }
}
