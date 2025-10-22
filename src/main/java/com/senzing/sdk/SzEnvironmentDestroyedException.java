package com.senzing.sdk;

/**
 * Extends {@link IllegalStateException} so the exceptional 
 * condition of the {@link SzEnvironment} already being destroyed
 * can be differentiated from other {@link IllegalStateException}
 * instances that might be thrown.
 * 
 * <p>
 * This is a {@link RuntimeException} that can be thrown from almost
 * all Senzing SDK functions if the associated {@link SzEnvironment}
 * has been destroyed.
 * </p>
 * 
 * <p>
 * <b>NOTE:</b> This class does <b>not</b> extend {@link SzException}, 
 * but rather extends {@link IllegalStateException}.
 * </p>
 * 
 * @since 4.1.0
 */
public class SzEnvironmentDestroyedException extends IllegalStateException {
    /**
     * Default constructor.
     *
     */
    public SzEnvironmentDestroyedException() {
        super();
    }
    
    /**
     * Constructs with the specified message.
     * 
     * @param message The message describing what occurred.
     */
    public SzEnvironmentDestroyedException(String message) {
        super(message);
    }

    /**
     * Constructs with the specified message and {@link Throwable} cause.
     * 
     * @param message The message describing what occurred.
     * @param cause The {@link Throwable} cause.
     */
    public SzEnvironmentDestroyedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs with the specified {@link Throwable} cause.
     * 
     * @param cause The {@link Throwable} cause.
     */
    public SzEnvironmentDestroyedException(Throwable cause) {
        super(cause);
    }
}
