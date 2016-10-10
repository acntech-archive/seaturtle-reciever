package no.acntech.seaturtle.receiver.util;

public class ResourceFactoryException extends RuntimeException {

    public ResourceFactoryException() {
    }

    public ResourceFactoryException(String message) {
        super(message);
    }

    public ResourceFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceFactoryException(Throwable cause) {
        super(cause);
    }
}
