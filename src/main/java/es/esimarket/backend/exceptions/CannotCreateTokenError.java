package es.esimarket.backend.exceptions;

public class CannotCreateTokenError extends RuntimeException {
    public CannotCreateTokenError() {
    }

    public CannotCreateTokenError(String message) {
        super(message);
    }

    public CannotCreateTokenError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCreateTokenError(Throwable cause) {
        super(cause);
    }

    public CannotCreateTokenError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
