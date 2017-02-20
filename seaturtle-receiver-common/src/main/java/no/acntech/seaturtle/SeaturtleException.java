package no.acntech.seaturtle;

public class SeaturtleException extends RuntimeException {

    public SeaturtleException() {
    }

    public SeaturtleException(String message) {
        super(message);
    }

    public SeaturtleException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeaturtleException(Throwable cause) {
        super(cause);
    }
}
