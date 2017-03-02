package no.acntech.seaturtle.receiver.kafka.serializer;

import no.acntech.seaturtle.receiver.kafka.KafkaException;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class SpecificRecordDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {
            /*try (DataFileReader<T> dataFileReader = new DataFileReader<T>(inputStream, new SpecificDatumReader<>())) {
                dataFileReader.create(data.getSchema(), outputStream);
                dataFileWriter.append(data);
                return outputStream.toByteArray();
            }*/
            return null;
        } catch (IOException e) {
            throw new KafkaException("Unable to open stream", e);
        }
    }

    @Override
    public void close() {
    }
}
