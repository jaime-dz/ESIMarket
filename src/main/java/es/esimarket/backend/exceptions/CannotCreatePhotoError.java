package es.esimarket.backend.exceptions;

public class CannotCreatePhotoError extends RuntimeException {
    public CannotCreatePhotoError() {
    }

    public CannotCreatePhotoError(String message) {
        super(message);
    }

    public CannotCreatePhotoError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCreatePhotoError(Throwable cause) {
        super(cause);
    }

    public CannotCreatePhotoError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
