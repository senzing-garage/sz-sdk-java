package com.senzing.sdk;

/**
 * Extends {@link SzUnrecoverableException} to define an exceptional
 * condition triggered by an invalid, expired or exhausted Senzing
 * license.
 * 
 * @since 4.0.0
 */
public class SzLicenseException extends SzUnrecoverableException {
    /**
     * Default constructor.
     */
    public SzLicenseException() {
        super();
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     *
     * @param message The message explaining the reason for the exception.
     */
    public SzLicenseException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaining the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzLicenseException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzLicenseException(Throwable cause) {
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
    public SzLicenseException(String message, Throwable cause) {
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
    public SzLicenseException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
