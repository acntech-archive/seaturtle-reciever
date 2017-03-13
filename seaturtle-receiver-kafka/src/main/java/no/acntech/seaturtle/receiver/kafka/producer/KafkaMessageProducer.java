package no.acntech.seaturtle.receiver.kafka.producer;

import no.acntech.seaturtle.receiver.kafka.KafkaClient;
import no.acntech.seaturtle.receiver.kafka.KafkaException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Properties;
import java.util.stream.IntStream;

public abstract class KafkaMessageProducer<K, V> extends KafkaClient {

    private static final String PRODUCER_PROPERTIES_FILE = "producer.properties";

    protected void produceRecords(String topic, int messageCount) {
        Properties properties = readProperties(PRODUCER_PROPERTIES_FILE);

        try (KafkaProducer<K, V> producer = new KafkaProducer<>(properties, createKeySerializer(), createValueSerializer())) {
            logger.debug("Starting to produce {} records...", messageCount);
            long startMillis = System.currentTimeMillis();
            IntStream.range(0, messageCount).forEach(i -> producer.send(produceRecord(topic, i)));
            long usedMillis = System.currentTimeMillis() - startMillis;
            logger.debug("Finished production of {} records {}ms", messageCount, usedMillis);
        } catch (Throwable t) {
            throw new KafkaException("Unable to send messages", t);
        }
    }

    protected abstract ProducerRecord<K, V> produceRecord(String topic, int i);

    protected abstract Serializer<K> createKeySerializer();

    protected abstract Serializer<V> createValueSerializer();
}
