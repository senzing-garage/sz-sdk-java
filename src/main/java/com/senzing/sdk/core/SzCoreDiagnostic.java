package com.senzing.sdk.core;

import com.senzing.sdk.SzDiagnostic;
import com.senzing.sdk.SzException;

/**
 * The package-protected implementation of {@link SzDiagnostic} that works
 * with the {@link SzCoreEnvironment} class.
 */
public class SzCoreDiagnostic implements SzDiagnostic {
    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link NativeDiagnosticJni} instance.
     */
    private NativeDiagnosticJni nativeApi = null;

    /**
     * Constructs with the specified {@link SzCoreEnvironment}.
     * 
     * @param environment The {@link SzCoreEnvironment} with which to 
     *                    construct.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreEnvironment} instance 
     *                               has already been destroyed.
     * 
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreDiagnostic(SzCoreEnvironment environment) 
        throws IllegalStateException, SzException 
    {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new NativeDiagnosticJni();

            // check if we are initializing with a config ID
            if (this.env.getConfigId() == null) {
                // if not then call the basic init
                int returnCode = this.nativeApi.init(
                    this.env.getInstanceName(),
                    this.env.getSettings(),
                    this.env.isVerboseLogging());
 
                // handle any failure
                this.env.handleReturnCode(returnCode, this.nativeApi);

            } else {
                // if so then call init with config ID
                int returnCode = this.nativeApi.initWithConfigID(
                    this.env.getInstanceName(),
                    this.env.getSettings(),
                    this.env.getConfigId(),
                    this.env.isVerboseLogging());

                // handle any failure
                this.env.handleReturnCode(returnCode, this.nativeApi);
            }

            // return null
            return null;
        });
    }

    /**
     * Gets the associated {@link NativeDiagnosticJni} instance.
     * 
     * @return The associated {@link NativeDiagnosticJni} instance.
     */
    NativeDiagnosticJni getNativeApi() {
        return this.nativeApi;
    }

    /**
     * The package-protected function to destroy the Senzing Diagnostic SDK.
     */
    void destroy() {
        synchronized (this) {
            if (this.nativeApi == null) {
                return;
            }
            this.nativeApi.destroy();
            this.nativeApi = null;
        }
    }

    /**
     * Checks if this instance has been destroyed by the associated
     * {@link SzCoreEnvironment}.
     * 
     * @return <code>true</code> if this instance has been destroyed,
     *         otherwise <code>false</code>.
     */
    protected boolean isDestroyed() {
        synchronized (this) {
            return (this.nativeApi == null);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getDatastoreInfo() throws SzException {
        return this.env.execute(() -> {
            // declare the buffer for the result
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.getDatastoreInfo(sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the JSON from the string buffer
            return sb.toString();
        });
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String checkDatastorePerformance(int secondsToRun) throws SzException {
        return this.env.execute(() -> {
            // declare the buffer for the result
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.checkDatastorePerformance(secondsToRun, sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the JSON from the string buffer
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getFeature(long featureId) throws SzException {
        return this.env.execute(() -> {
            // declare the buffer for the result
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.getFeature(featureId, sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the JSON from the string buffer
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public void purgeRepository() throws SzException {
        this.env.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.purgeRepository();
            
            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });
        
    }
}
