package com.senzing.sdk;

/**
 * Extends {@link SzRetryableException} to define an exceptional condition
 * where an operation failed because a database condition that is transient
 * and would like be resolved on a repeated attempt.  Retrying the operation
 * may result in it completing successfully.
 */
public class SzDatabaseTransientException extends SzRetryableException {
    /**
     * Default constructor.
     */
    public SzDatabaseTransientException() {
        super();
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     *
     * @param message The message explaining the reason for the exception.
     */
    public SzDatabaseTransientException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzDatabaseTransientException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzDatabaseTransientException(Throwable cause) {
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
    public SzDatabaseTransientException(String message, Throwable cause) {
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
    public SzDatabaseTransientException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
