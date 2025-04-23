package com.senzing.sdk;

/**
 * Extends {@link SzRetryableException} to define an exceptional condition
 * where a database connection was lost causing a Senzing operation to fail.
 * Retrying the operation would likely result in the connection being
 * reestablished and the operation succeeding.
 */
public class SzDatabaseConnectionLostException extends SzRetryableException {
    /**
     * Default constructor.
     */
    public SzDatabaseConnectionLostException() {
        super();
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     *
     * @param message The message explaining the reason for the exception.
     */
    public SzDatabaseConnectionLostException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzDatabaseConnectionLostException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzDatabaseConnectionLostException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs with a message explaining the reason for the exception
     * and the {@link Throwable} that is the underlying cause for the 
     * exception.
     * 
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzDatabaseConnectionLostException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs with the Senzing error code, the message explaining
     * the reason for the exception and the {@link Throwable} that
     * is the underlying cause for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     *
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzDatabaseConnectionLostException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
