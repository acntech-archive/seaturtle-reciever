package no.acntech.seaturtle.receiver.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.stream.IntStream;

public class KafkaSingleThreadedMessageProducer extends KafkaClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    private static final String PRODUCER_PROPERTIES_FILE = "producer.properties";
    private static final String TOPIC = "heartbeat";
    private static final int RECORD_COUNT = 1000000;

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedMessageProducer().consumeRecords();
    }

    private void consumeRecords() {
        Properties properties = readProperties(PRODUCER_PROPERTIES_FILE);

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            LOGGER.info("Starting to produce {} records...", RECORD_COUNT);
            long startMillis = System.currentTimeMillis();
            IntStream.range(0, RECORD_COUNT).forEach(i -> producer.send(createRecord(TOPIC, i)));
            long usedMillis = System.currentTimeMillis() - startMillis;
            LOGGER.info("Finished production of {} records {}ms", RECORD_COUNT, usedMillis);
        } catch (Throwable t) {
            throw new KafkaException("Unable to send messages", t);
        }
    }

    private ProducerRecord<String, String> createRecord(String topic, int i) {
        return new ProducerRecord<>(topic, "Key " + i, "Value " + i);
    }
}
