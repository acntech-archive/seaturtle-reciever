package no.acntech.seaturtle.receiver.storage;

import no.acntech.seaturtle.SeaturtleException;

public class StorageException extends SeaturtleException {

    public StorageException() {
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}
