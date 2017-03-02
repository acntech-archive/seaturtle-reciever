package no.acntech.seaturtle.receiver.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class KafkaMultiThreadedMessageConsumer {

    private static final String TOPIC = "heartbeat";
    private static final int THREAD_COUNT = 10;

    private KafkaMultiThreadedMessageConsumer(String... topicNames) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        IntStream.rangeClosed(1, THREAD_COUNT).forEach(threadId -> executorService.execute(new ConsumerTask(threadId, topicNames)));
    }

    public static void main(String[] args) throws Exception {
        new KafkaMultiThreadedMessageConsumer(TOPIC);
    }

    class ConsumerTask extends KafkaMessageConsumer<String, String> implements Runnable {

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
        protected Deserializer<String> createKeyDeserializer() {
            return new StringDeserializer();
        }

        @Override
        protected Deserializer<String> createValueDeserializer() {
            return new StringDeserializer();
        }
    }
}