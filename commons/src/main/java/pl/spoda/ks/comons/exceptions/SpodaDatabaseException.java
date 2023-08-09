package pl.spoda.ks.comons.exceptions;

public class SpodaDatabaseException extends RuntimeException {

    public SpodaDatabaseException(String errorMessage) {
        super(errorMessage);
    }
}
