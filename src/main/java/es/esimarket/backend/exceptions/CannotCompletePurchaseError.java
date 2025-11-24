package es.esimarket.backend.exceptions;

public class CannotCompletePurchaseError extends RuntimeException {
    public CannotCompletePurchaseError() {
    }

    public CannotCompletePurchaseError(String message) {
        super(message);
    }

    public CannotCompletePurchaseError(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCompletePurchaseError(Throwable cause) {
        super(cause);
    }

    public CannotCompletePurchaseError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
