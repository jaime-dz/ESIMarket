package es.esimarket.backend.exceptions;

public class CannotCreateChatError extends RuntimeException {
    public CannotCreateChatError() {
    }

    public CannotCreateChatError(String message) {
        super(message);
    }

    public CannotCreateChatError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCreateChatError(Throwable cause) {
        super(cause);
    }

    public CannotCreateChatError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
