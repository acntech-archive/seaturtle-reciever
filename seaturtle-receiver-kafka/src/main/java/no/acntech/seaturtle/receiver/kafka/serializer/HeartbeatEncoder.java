package no.acntech.seaturtle.receiver.kafka.serializer;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;
import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.KafkaException;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HeartbeatEncoder implements Encoder<Heartbeat> {

    public HeartbeatEncoder(VerifiableProperties props) {
    }

    @Override
    public byte[] toBytes(Heartbeat heartbeat) {
        if (heartbeat == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            SpecificDatumWriter<Heartbeat> writer = new SpecificDatumWriter<>(Heartbeat.getClassSchema());
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            writer.write(heartbeat, encoder);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new KafkaException("Unable to write to encoder", e);
        }
    }
}
