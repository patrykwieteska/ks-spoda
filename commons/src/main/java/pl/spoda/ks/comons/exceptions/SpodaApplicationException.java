package pl.spoda.ks.comons.exceptions;

public class SpodaApplicationException extends RuntimeException {

    public SpodaApplicationException(String errorMessage) {
        super(errorMessage);
    }

    public SpodaApplicationException(String errorMessage, Throwable throwable) {
        super(errorMessage,throwable);
    }
}
