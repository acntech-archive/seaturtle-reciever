package no.acntech.seaturtle.receiver.kafka.serializer;

import no.acntech.seaturtle.receiver.kafka.KafkaException;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class SpecificRecordSerializer<T extends SpecificRecordBase> implements Serializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (DataFileWriter<T> dataFileWriter = new DataFileWriter<>(new SpecificDatumWriter<>())) {
                dataFileWriter.create(data.getSchema(), outputStream);
                dataFileWriter.append(data);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            throw new KafkaException("Unable to open stream", e);
        }
    }

    @Override
    public void close() {
    }
}
