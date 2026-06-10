package exception;

public class HorseOperationException extends RuntimeException {
    public HorseOperationException(String message) {
        super(message);
    }

    public HorseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
