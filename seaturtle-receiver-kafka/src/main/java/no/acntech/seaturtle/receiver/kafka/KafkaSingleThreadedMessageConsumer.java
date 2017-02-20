package no.acntech.seaturtle.receiver.kafka;

public class KafkaSingleThreadedMessageConsumer extends KafkaMessageConsumer<String, String> {

    private static final String TOPIC = "heartbeat";

    public KafkaSingleThreadedMessageConsumer(String... topicNames) {
        super(topicNames);
        consumeRecords();
    }

    public static void main(String[] args) throws Exception {
        new KafkaSingleThreadedMessageConsumer(TOPIC);
    }
}