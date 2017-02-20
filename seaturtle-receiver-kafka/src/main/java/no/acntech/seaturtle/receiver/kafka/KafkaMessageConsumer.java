package no.acntech.seaturtle.receiver.kafka;

import no.acntech.seaturtle.SeaturtleException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class KafkaMessageConsumer<K, V> extends KafkaClient {

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

    public void consumeRecords() {
        Properties properties = readProperties(CONSUMER_PROPERTIES_FILE);
        try (KafkaConsumer<K, V> consumer = createKafkaConsumer(properties, topicNames)) {
            while (keepRunning(consumer)) {
                ConsumerRecords<K, V> records = consumer.poll(POLL_TIMEOUT);
                if (records.count() == 0) {
                    timeouts++;
                    continueOrSleep();
                } else {
                    timeouts = 0;
                    sleepMillis = 0;
                    records.forEach(this::interpretRecord);
                }
            }
        }
    }

    private V interpretRecord(ConsumerRecord<K, V> record) {
        logger.info("Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
        return record.value();
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
        KafkaConsumer<K, V> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(topics);
        return consumer;
    }
}