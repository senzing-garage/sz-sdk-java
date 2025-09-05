package com.senzing.sdk;

/**
 * Defines an exceptional condition where the failure is not
 * recoverable and all operations should be stopped until the
 * system can be modified to resolve the condition causing the
 * failure.
 * 
 * @since 4.0.0
 */
public class SzUnrecoverableException extends SzException {
    /**
     * Default constructor.
     */
    public SzUnrecoverableException() {
        super();
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzUnrecoverableException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzUnrecoverableException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzUnrecoverableException(Throwable cause) {
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
    public SzUnrecoverableException(String message, Throwable cause) {
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
    public SzUnrecoverableException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
