package com.senzing.sdk;

/**
 * Extends {@link SzBadInputException} to define an exceptional condition
 * where the provided bad input to a Senzing operation is an identifier
 * that could not be used to successfully locate required data for that
 * operation.
 */
public class SzNotFoundException extends SzBadInputException {
    /**
     * Default constructor.
     */
    public SzNotFoundException() {
        super();
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     *
     * @param message The message explaining the reason for the exception.
     */
    public SzNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzNotFoundException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzNotFoundException(Throwable cause) {
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
    public SzNotFoundException(String message, Throwable cause) {
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
    public SzNotFoundException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
