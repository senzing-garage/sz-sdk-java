package com.senzing.sdk;

/**
 * Provides a factory interface for obtaining the references to the Senzing SDK 
 * singleton instances that have been initialized.
 * 
 * <p><b>Usage:</b> 
 * {@snippet class="com.senzing.sdk.SzProductDemo" region="SzEnvironment"}
 * </p>
 * 
 * @since 4.0.0
 */
public interface SzEnvironment {
    /**
     * Provides a reference to the {@link SzProduct} instance associated with
     * this {@link SzEnvironment}.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzProductDemo" region="getProduct"}
     * </p>
     * 
     * @return The {@link SzProduct} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzProduct} instance. 
     * 
     * @since 4.0.0
     */
    SzProduct getProduct() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzEngine} instance associated with
     * this {@link SzEnvironment}.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getEngine"}
     * </p>
     * 
     * @return The {@link SzEngine} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzEngine} instance. 
     * 
     * @since 4.0.0
     */
    SzEngine getEngine() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzConfigManager} instance associated with
     * this {@link SzEnvironment}.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getConfigManager"}
     * </p>
     * 
     * @return The {@link SzConfigManager} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzConfigManager} instance. 
     * 
     * @since 4.0.0
     */
    SzConfigManager getConfigManager() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzDiagnostic} instance associated with
     * this {@link SzEnvironment}.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="getDiagnostic"}
     * </p>
     * 
     * @return The {@link SzDiagnostic} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzDiagnostic} instance. 
     * 
     * @since 4.0.0
     */
    SzDiagnostic getDiagnostic() throws IllegalStateException, SzException;

    /** 
     * Gets the currently active configuration ID for the {@link
     * SzEnvironment}.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getActiveConfigId"}
     * </p>
     * 
     * @return The currently active configuration ID.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining the active config ID.
     * 
     * @since 4.0.0
     */
    long getActiveConfigId() throws IllegalStateException, SzException;

    /**
     * Reinitializes the {@link SzEnvironment} with the specified
     * configuration ID.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getActiveConfigId"}
     * </p>
     * 
     * @param configId The configuration ID with which to initialize.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure reinitializing.
     * 
     * @since 4.0.0
     */
    void reinitialize(long configId)
        throws IllegalStateException, SzException;

    /**
     * Destroys this {@link SzEnvironment} and invalidates any SDK singleton
     * references that has previously provided.  If this instance has already
     * been destroyed then this method has no effect.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzProductDemo" region="destroyEnvironment"}
     * </p>
     * 
     * @since 4.0.0
    */
    void destroy();

    /**
     * Checks if this instance has had its {@link #destroy()} method called.
     * 
     * <p><b>Usage:</b> 
     * {@snippet class="com.senzing.sdk.SzProductDemo" region="destroyEnvironment"}
     * </p>
     *
     * @return <code>true</code> if this instance has had its {@link 
     *         #destroy()} method called, otherwise <code>false</code>.
     * 
     * @since 4.0.0
     */
    boolean isDestroyed();
}
