package es.esimarket.backend.exceptions;

public class CannotMakeDonationError extends RuntimeException {
    public CannotMakeDonationError() {
    }

    public CannotMakeDonationError(String message) {
        super(message);
    }

    public CannotMakeDonationError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotMakeDonationError(Throwable cause) {
        super(cause);
    }

    public CannotMakeDonationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
