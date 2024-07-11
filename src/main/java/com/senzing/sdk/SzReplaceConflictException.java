package com.senzing.sdk;

/**
 * Describes an exceptional condition when an attempt is made to replace
 * a Senzing value with a new value providing it has not not already been
 * changed, however, the current value is no longer the expected value and
 * has therefore already been changed.
 */
public class SzReplaceConflictException extends SzException {
    /**
     * Default constructor.
     */
    public SzReplaceConflictException() {
        super();
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzReplaceConflictException(String message) {
        super(message);
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzReplaceConflictException(int errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzReplaceConflictException(Throwable cause) {
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
    public SzReplaceConflictException(String message, Throwable cause) {
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
    public SzReplaceConflictException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
