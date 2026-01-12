package es.esimarket.backend.exceptions;

public class CannotCreateProductError extends RuntimeException {
    public CannotCreateProductError() {
    }

    public CannotCreateProductError(String message) {
        super(message);
    }

    public CannotCreateProductError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCreateProductError(Throwable cause) {
        super(cause);
    }

    public CannotCreateProductError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
