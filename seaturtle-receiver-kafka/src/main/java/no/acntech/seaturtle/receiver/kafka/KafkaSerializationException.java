package no.acntech.seaturtle.receiver.kafka;

public class KafkaSerializationException extends KafkaException {

    public KafkaSerializationException() {
    }

    public KafkaSerializationException(String message) {
        super(message);
    }

    public KafkaSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaSerializationException(Throwable cause) {
        super(cause);
    }
}
