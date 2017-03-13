package no.acntech.seaturtle.receiver.kafka.serializer;

import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;
import no.acntech.seaturtle.receiver.kafka.KafkaException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.IOException;

public abstract class AbstractAvroDecoder<T extends GenericContainer> implements Decoder<T> {

    private VerifiableProperties verifiableProperties;

    public AbstractAvroDecoder() {
    }

    public AbstractAvroDecoder(VerifiableProperties verifiableProperties) {
        this.verifiableProperties = verifiableProperties;
    }

    @Override
    public T fromBytes(byte[] payload) {
        if (payload == null) {
            return null;
        }
        try {
            DatumReader<T> reader = new SpecificDatumReader<>(schema());
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(payload, null);
            return reader.read(null, decoder);
        } catch (IOException e) {
            throw new KafkaException("Unable to decode message", e);
        }
    }

    protected abstract Schema schema();
}
