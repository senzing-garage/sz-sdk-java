package com.senzing.sdk;

/**
 * Extends {@link SzUnrecoverableException} to define an exceptional
 * condition triggered by Senzing not being initialized.
 */
public class SzNotInitializedException extends SzUnrecoverableException {
    /**
     * Default constructor.
     */
    public SzNotInitializedException() {
        super();
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     *
     * @param message The message explaining the reason for the exception.
     */
    public SzNotInitializedException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzNotInitializedException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzNotInitializedException(Throwable cause) {
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
    public SzNotInitializedException(String message, Throwable cause) {
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
    public SzNotInitializedException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
