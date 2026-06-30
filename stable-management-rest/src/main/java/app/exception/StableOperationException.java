package app.exception;

public class StableOperationException extends RuntimeException {
    public StableOperationException(String message) {
        super(message);
    }

    public StableOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
