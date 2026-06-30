package app.exception;

public class StableServiceOperationException extends RuntimeException {
    public StableServiceOperationException(String message) {
        super(message);
    }

    public StableServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
