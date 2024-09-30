package com.senzing.sdk;

/**
 * Defines the base exception for Senzing errors.  This adds a property
 * for the numeric Senzing error code which can optionally be set.
 */
public class SzException extends Exception {
    /**
     * The underlying Senzing error code.
     */
    private Integer errorCode = null;
    
    /**
     * Default constructor.
     */
    public SzException() {
        super();
        this.errorCode = null;
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzException(String message) {
        super(message);
        this.errorCode = null;
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param errorCode The underlying Senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The underlying cause for the exception.
     */
    public SzException(Throwable cause) {
        super(cause);
        this.errorCode = null;
    }

    /**
     * Constructs with a message explaing the reason for the exception
     * and the {@link Throwable} that is the underlying cause for the 
     * exception.
     * 
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The underlying cause for the exception.
     */
    public SzException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    /**
     * Constructs with the Senzing error code, the message explaing
     * the reason for the exception and the {@link Throwable} that
     * is the underlying cause for the exception.
     * 
     * @param errorCode The underlying Senzing error code.
     *
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The underlying cause for the exception.
     */
    public SzException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Gets the underlying Senzing error code associated with the
     * exception.  This returns <code>null</code> if no error code was 
     * associated with the exception.
     * 
     * @return The underlying Senzing error code associated with the
     *         exception, or <code>null</code> if none was associated.
     */
    public Integer getErrorCode() {
        return this.errorCode;
    }
}
