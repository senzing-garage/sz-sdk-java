package com.senzing.sdk;

/**
 * Extends {@link SzRetryableException} to define an exceptional condition
 * where an operation failed because a timeout was exceeded.  Retrying the 
 * operation (possibly with a longer timeout) may result in it completing
 * successfully.
 */
public class SzRetryTimeoutExceededException extends SzRetryableException {
    /**
     * Default constructor.
     */
    public SzRetryTimeoutExceededException() {
        super();
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     *
     * @param message The message explaining the reason for the exception.
     */
    public SzRetryTimeoutExceededException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzRetryTimeoutExceededException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzRetryTimeoutExceededException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs with a message explaing the reason for the exception
     * and the {@link Throwable} that is the underlying cause for the 
     * exception.
     * 
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzRetryTimeoutExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs with the Senzing error code, the message explaing
     * the reason for the exception and the {@link Throwable} that
     * is the underlying cause for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     *
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzRetryTimeoutExceededException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
