package com.senzing.sdk;

/**
 * Provides a factory interface for obtaining the references to the Senzing SDK 
 * singleton instances that have been initialized.
 * 
 */
public interface SzEnvironment {
    /**
     * Provides a reference to the {@link SzProduct} instance associated with
     * this {@link SzEnvironment}.
     * 
     * @return The {@link SzProduct} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzProduct} instance. 
     */
    SzProduct getProduct() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzConfig} instance associated with
     * this {@link SzEnvironment}.
     * 
     * @return The {@link SzProduct} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzConfig} instance. 
     */
    SzConfig getConfig() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzEngine} instance associated with
     * this {@link SzEnvironment}.
     * 
     * @return The {@link SzEngine} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzEngine} instance. 
     */
    SzEngine getEngine() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzConfigManager} instance associated with
     * this {@link SzEnvironment}.
     * 
     * @return The {@link SzConfigManager} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzConfigManager} instance. 
     */
    SzConfigManager getConfigManager() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzDiagnostic} instance associated with
     * this {@link SzEnvironment}.
     * 
     * @return The {@link SzDiagnostic} instance associated with this 
     *         {@link SzEnvironment}.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzDiagnostic} instance. 
     */
    SzDiagnostic getDiagnostic() throws IllegalStateException, SzException;

    /** 
     * Gets the currently active configuration ID for the {@link
     * SzEnvironment}.
     * 
     * @return The currently active configuration ID.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzDiagnostic} instance. 
     */
    long getActiveConfigId() throws IllegalStateException, SzException;

    /**
     * Reinitializes the {@link SzEnvironment} with the specified
     * configuration ID.
     * 
     * @param configId The configuraiton ID with which to initialize.
     * 
     * @throws IllegalStateException If this {@link SzEnvironment} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzDiagnostic} instance. 
     */
    void reinitialize(long configId)
        throws IllegalStateException, SzException;

    /**
    * Destroys this {@link SzEnvironment} and invalidates any SDK singleton
    * references that has previously provided.  If this instance has already
    * been destroyed then this method has no effect.
    */
    void destroy();

    /**
    * Checks if this instance has had its {@link #destroy()} method called.
    *
    * @return <code>true</code> if this instance has had its {@link 
    *         #destroy()} method called, otherwise <code>false</code>.
    */
    boolean isDestroyed();
}
