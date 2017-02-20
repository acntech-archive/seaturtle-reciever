package no.acntech.seaturtle.receiver.kafka;

import no.acntech.seaturtle.SeaturtleException;

public class KafkaException extends SeaturtleException {

    public KafkaException() {
    }

    public KafkaException(String message) {
        super(message);
    }

    public KafkaException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaException(Throwable cause) {
        super(cause);
    }
}
