package no.acntech.seaturtle.receiver.kafka;

import no.acntech.seaturtle.receiver.domain.avro.Heartbeat;
import no.acntech.seaturtle.receiver.kafka.serializer.SpecificRecordSerializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class KafkaSingleThreadedAvroMessageProducer extends KafkaMessageProducer<String, Heartbeat> {

    private static final String TOPIC = "heartbeat";
    private static final int RECORD_COUNT = 100;

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedAvroMessageProducer().produceRecords(TOPIC, RECORD_COUNT);
    }

    @Override
    protected ProducerRecord<String, Heartbeat> produceRecord(String topic, int i) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        ZonedDateTime dateTime = ZonedDateTime.now();
        Heartbeat heartbeat = Heartbeat.newBuilder()
                .setTimestamp(dateTime.format(dateTimeFormatter))
                .setEvent("Event " + i)
                .setRemote("Remote " + i)
                .build();
        return new ProducerRecord<>(topic, "Key " + i, heartbeat);
    }

    @Override
    protected Serializer<String> createKeySerializer() {
        return new StringSerializer();
    }

    @Override
    protected Serializer<Heartbeat> createValueSerializer() {
        return new SpecificRecordSerializer<>();
    }
}
