package org.zeus.dal;

public class DbException extends Exception {
    public DbException(String messsage) {
        super(messsage);
    }

    public DbException(Throwable cause) {
        super(cause);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}
