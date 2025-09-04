package com.senzing.sdk;

/**
 * Defines an exceptional condition when an invalid input value is
 * provided to a Senzing operation preventing the successful
 * completion of that operation.
 * @since 4.0.0
 */
public class SzBadInputException extends SzException {
    /**
     * Default constructor.
     */
    public SzBadInputException() {
        super();
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzBadInputException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzBadInputException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzBadInputException(Throwable cause) {
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
    public SzBadInputException(String message, Throwable cause) {
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
    public SzBadInputException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
