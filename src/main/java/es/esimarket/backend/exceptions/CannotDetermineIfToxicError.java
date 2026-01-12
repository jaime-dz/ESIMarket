package es.esimarket.backend.exceptions;

public class CannotDetermineIfToxicError extends RuntimeException {
    public CannotDetermineIfToxicError() {
    }

    public CannotDetermineIfToxicError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotDetermineIfToxicError(String message) {
        super(message);
    }

    public CannotDetermineIfToxicError(Throwable cause) {
        super(cause);
    }

    public CannotDetermineIfToxicError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
