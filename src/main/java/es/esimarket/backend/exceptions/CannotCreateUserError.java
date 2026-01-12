package es.esimarket.backend.exceptions;

public class CannotCreateUserError extends RuntimeException {
    public CannotCreateUserError() {
    }

    public CannotCreateUserError(String message) {
        super(message);
    }

    public CannotCreateUserError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCreateUserError(Throwable cause) {
        super(cause);
    }

    public CannotCreateUserError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
