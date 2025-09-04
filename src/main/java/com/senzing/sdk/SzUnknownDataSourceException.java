package com.senzing.sdk;

/**
 * Extends {@link SzBadInputException} to define an exceptional condition
 * where a specified data source code is not configured in the current
 * active configuration and therefore the data source could not be found.
 * 
 * @since 4.0.0
 */
public class SzUnknownDataSourceException extends SzBadInputException {
    /**
     * Default constructor.
     */
    public SzUnknownDataSourceException() {
        super();
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzUnknownDataSourceException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzUnknownDataSourceException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzUnknownDataSourceException(Throwable cause) {
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
    public SzUnknownDataSourceException(String message, Throwable cause) {
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
    public SzUnknownDataSourceException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
