package com.senzing.sdk;

/**
 * Extends {@link SzUnrecoverableException} to define an exceptional
 * condition caused by an otherwise unhandled and unexpected failure
 * in the Senzing SDK.
 */
public class SzUnhandledException extends SzUnrecoverableException {
    /**
     * Default constructor.
     */
    public SzUnhandledException() {
        super();
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     *
     * @param message The message explaining the reason for the exception.
     */
    public SzUnhandledException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzUnhandledException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzUnhandledException(Throwable cause) {
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
    public SzUnhandledException(String message, Throwable cause) {
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
    public SzUnhandledException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
