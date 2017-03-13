package no.acntech.seaturtle.receiver.kafka.producer;

import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.serializer.GenericAvroSerializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class KafkaMultiThreadedAvroMessageProducer {

    private static final String TOPIC = "heartbeat";
    private static final int THREAD_COUNT = 10;
    private static final int RECORD_COUNT = 10000;

    private void produceRecords(String topicName, int recordCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        IntStream.rangeClosed(1, THREAD_COUNT).forEach(threadId -> executorService.execute(new ProducerTask(threadId, topicName, recordCount)));
    }

    class ProducerTask extends KafkaMessageProducer<String, Heartbeat> implements Runnable {

        private final int threadId;
        private final String topicName;
        private final int recordCount;

        ProducerTask(int threadId, String topicName, int recordCount) {
            this.threadId = threadId;
            this.topicName = topicName;
            this.recordCount = recordCount;
        }

        @Override
        public void run() {
            produceRecords(topicName, recordCount);
        }

        @Override
        protected ProducerRecord<String, Heartbeat> produceRecord(String topic, int i) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            ZonedDateTime dateTime = ZonedDateTime.now();
            Heartbeat heartbeat = Heartbeat.newBuilder()
                    .setTimestamp(dateTime.format(dateTimeFormatter))
                    .setEvent("Thread " + threadId + " Event " + i)
                    .setRemote("Thread " + threadId + " Remote " + i)
                    .build();
            return new ProducerRecord<>(topic, "Thread " + threadId + " Key " + i, heartbeat);
        }

        @Override
        protected Serializer<String> createKeySerializer() {
            return new StringSerializer();
        }

        @Override
        protected Serializer<Heartbeat> createValueSerializer() {
            return new GenericAvroSerializer<>();
        }
    }

    public static void main(String[] args) throws Exception {
        new KafkaMultiThreadedAvroMessageProducer().produceRecords(TOPIC, RECORD_COUNT);
    }
}
