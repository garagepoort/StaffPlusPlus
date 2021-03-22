package net.shortninja.staffplus.core.common.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(String error, Throwable e) {
        super(error, e);
    }
}
