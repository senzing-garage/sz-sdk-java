package com.senzing.sdk;

/**
 * Defines an exceptional condition when a failure has occurred
 * pertaining to the Senzing configuration.
 */
public class SzConfigurationException extends SzException {
    /**
     * Default constructor.
     */
    public SzConfigurationException() {
        super();
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzConfigurationException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzConfigurationException(Throwable cause) {
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
    public SzConfigurationException(String message, Throwable cause) {
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
    public SzConfigurationException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
