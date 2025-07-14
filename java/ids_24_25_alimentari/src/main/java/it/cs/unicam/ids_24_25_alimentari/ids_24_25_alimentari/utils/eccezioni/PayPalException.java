package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.eccezioni;

public class PayPalException extends RuntimeException {

    private final String errorCode;

    public PayPalException(String message) {
        super(message);
        this.errorCode = "PAYPAL_ERROR";
    }

    public PayPalException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PayPalException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PAYPAL_ERROR";
    }

    public PayPalException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
