package es.esimarket.backend.exceptions;

public class BadInputError extends RuntimeException {
    public BadInputError() {
    }

    public BadInputError(String message) {
        super(message);
    }

    public BadInputError(String message, Throwable cause) {
        super(message, cause);
    }

    public BadInputError(Throwable cause) {
        super(cause);
    }

    public BadInputError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
