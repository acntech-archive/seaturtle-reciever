package no.acntech.seaturtle.receiver.kafka.serializer;

import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;
import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.KafkaException;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HeartbeatDecoder implements Decoder<Heartbeat> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatDecoder.class);
    private static final byte MAGIC_BYTE = 0x0;
    private static final int ID_SIZE = 4;

    public HeartbeatDecoder(VerifiableProperties props) {
    }

    @Override
    public Heartbeat fromBytes(byte[] payload) {
        if (payload == null) {
            return null;
        }
        LOGGER.info("--- Payload: {}", new String(payload));
        int id = -1;
        try {
            ByteBuffer buffer = getByteBuffer(payload);
            id = buffer.getInt();
            int length = buffer.limit() - 1 - ID_SIZE;
            int start = buffer.position() + buffer.arrayOffset();
            //SpecificDatumReader<Heartbeat> reader = new SpecificDatumReader<>(Heartbeat.getClassSchema());
            GenericDatumReader<Heartbeat> reader = new GenericDatumReader<>(Heartbeat.getClassSchema());
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(buffer.array(), start, length, null);
            return reader.read(null, decoder);
        } catch (IOException e) {
            throw new KafkaException("Unable deserialize Avro message for id" + id, e);
        }
    }

    private ByteBuffer getByteBuffer(byte[] payload) {
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        /*if (buffer.get() != MAGIC_BYTE) {
            throw new SerializationException("Unknown magic byte!");
        }*/
        return buffer;
    }
}
