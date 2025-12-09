package es.esimarket.backend.exceptions;

public class CannotCompleteActionError extends RuntimeException {
    public CannotCompleteActionError() {
    }

    public CannotCompleteActionError(String message) {
        super(message);
    }

    public CannotCompleteActionError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCompleteActionError(Throwable cause) {
        super(cause);
    }

    public CannotCompleteActionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
