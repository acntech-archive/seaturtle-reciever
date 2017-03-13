package no.acntech.seaturtle.receiver.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class KafkaMultiThreadedTextMessageConsumer {

    private static final String TOPIC = "heartbeat";
    private static final int THREAD_COUNT = 10;
    private final String[] topicNames;

    private KafkaMultiThreadedTextMessageConsumer(String... topicNames) {
        this.topicNames = topicNames;
    }

    private void consumeRecords() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        IntStream.rangeClosed(1, THREAD_COUNT).forEach(threadId -> executorService.execute(new ConsumerTask(threadId, topicNames)));
    }

    class ConsumerTask extends KafkaMessageConsumer<String, String, String> implements Runnable {

        private final int threadId;

        ConsumerTask(int threadId, String... topicNames) {
            super(topicNames);
            this.threadId = threadId;
        }

        @Override
        public void run() {
            consumeRecords();
        }

        @Override
        protected String consumeRecord(ConsumerRecord<String, String> record) {
            logger.info("Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
            return record.value();
        }

        @Override
        protected Deserializer<String> createKeyDeserializer() {
            return new StringDeserializer();
        }

        @Override
        protected Deserializer<String> createValueDeserializer() {
            return new StringDeserializer();
        }
    }

    public static void main(String[] args) throws Exception {
        new KafkaMultiThreadedTextMessageConsumer(TOPIC).consumeRecords();
    }
}