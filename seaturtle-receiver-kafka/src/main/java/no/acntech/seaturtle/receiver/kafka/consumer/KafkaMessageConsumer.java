package no.acntech.seaturtle.receiver.kafka.consumer;

import no.acntech.seaturtle.SeaturtleException;
import no.acntech.seaturtle.receiver.kafka.KafkaClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class KafkaMessageConsumer<K, V, R> extends KafkaClient {

    private static final String CONSUMER_PROPERTIES_FILE = "consumer.properties";
    private static final int THREAD_COUNT = 10;
    private static final int MAX_SLEEP_MILLIS = 1000;
    private static final int SLEEP_DELTA_MILLIS = MAX_SLEEP_MILLIS / THREAD_COUNT;
    private static final int POLL_TIMEOUT = 200;
    private int timeouts = 0;
    private int sleepMillis = 0;
    private final String[] topicNames;

    protected KafkaMessageConsumer(String... topicNames) {
        this.topicNames = topicNames;
    }

    protected void consumeRecords() {
        Properties properties = readProperties(CONSUMER_PROPERTIES_FILE);
        logger.info("Connecting to Kafka servers...", properties.getProperty("bootstrap.servers"));
        try (KafkaConsumer<K, V> consumer = createKafkaConsumer(properties, topicNames)) {
            logger.info("Connection successful! Starting consumption from topics {}", Arrays.toString(topicNames));
            while (keepRunning(consumer)) {
                ConsumerRecords<K, V> records = consumer.poll(POLL_TIMEOUT);
                if (records.count() == 0) {
                    timeouts++;
                    logger.trace("No messages received");
                    continueOrSleep();
                } else {
                    timeouts = 0;
                    sleepMillis = 0;
                    logger.debug("Started consumption of {} messages", records.count());
                    records.forEach(this::consumeRecord);
                    logger.debug("Completed consumption of {} messages", records.count());
                }
            }
        }
    }

    private boolean keepRunning(KafkaConsumer<K, V> consumer) {
        return consumer != null;
    }

    private void continueOrSleep() {
        if (timeouts > 0) {
            int maxSleepMillis = sleepMillis + SLEEP_DELTA_MILLIS;
            maxSleepMillis = maxSleepMillis < MAX_SLEEP_MILLIS ? maxSleepMillis : MAX_SLEEP_MILLIS;
            sleepMillis = ThreadLocalRandom.current().nextInt(sleepMillis, maxSleepMillis);
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                throw new SeaturtleException("Unable to sleep", e);
            }
        }
    }

    private KafkaConsumer<K, V> createKafkaConsumer(Properties properties, String... topicNames) {
        Set<String> topics = Arrays.stream(topicNames).collect(Collectors.toSet());
        logger.info("Staring consuming messages from topics {}", topics.toString());
        KafkaConsumer<K, V> consumer = new KafkaConsumer<>(properties, createKeyDeserializer(), createValueDeserializer());
        consumer.subscribe(topics);
        return consumer;
    }

    protected abstract R consumeRecord(ConsumerRecord<K, V> record);

    protected abstract Deserializer<K> createKeyDeserializer();

    protected abstract Deserializer<V> createValueDeserializer();
}