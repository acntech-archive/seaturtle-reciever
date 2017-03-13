package no.acntech.seaturtle.receiver.kafka.serializer;

import kafka.serializer.Encoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GenericAvroEncoder<T extends SpecificRecordBase> implements Encoder<T> {

    @Override
    public byte[] toBytes(T message) {
        if (message == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            DatumWriter<T> writer = new SpecificDatumWriter<>(message.getSchema());
            writer.write(message, encoder);
            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new KafkaSerializationException("Unable to encoder message", e);
        }
    }
}
