package com.senzing.sdk.core;

import com.senzing.sdk.SzException;
import com.senzing.sdk.SzConfigManager;

/**
 * The package-private core implementation of {@link SzConfigManager}
 * that works with the {@link SzCoreEnvironment} class.
 */
class SzCoreConfigManager implements SzConfigManager {    
    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link NativeConfigManagerJni} instance.
     */
    private NativeConfigManagerJni nativeApi = null;

    /**
     * Internal object for instance-wide synchronized locking.
     */
    private final Object monitor = new Object();

    /**
     * Constructs with the specified {@link SzCoreEnvironment}.
     * 
     * @param environment The {@link SzCoreEnvironment} with which to 
     *                    construct.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreEnvironment} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreConfigManager(SzCoreEnvironment environment) 
        throws IllegalStateException, SzException 
    {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new NativeConfigManagerJni();

            int returnCode = this.nativeApi.init(this.env.getInstanceName(),
                                                 this.env.getSettings(),
                                                 this.env.isVerboseLogging());

            this.env.handleReturnCode(returnCode, this.nativeApi);

            // no return value, so return null
            return null;
        });
    }

    /**
     * Gets the associated {@link NativeConfigManagerJni} instance.
     * 
     * @return The associated {@link NativeConfigManagerJni} instance.
     */
    NativeConfigManagerJni getNativeApi() {
        return this.nativeApi;
    }

    /**
     * The package-protected function to destroy the Senzing Config Manager SDK.
     */
    void destroy() {
        synchronized (this.monitor) {
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
        synchronized (this.monitor) {
            return (this.nativeApi == null);
        }
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public long addConfig(String configDefinition, String configComment)
        throws SzException 
    {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.addConfig(
                configDefinition, configComment, result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public String getConfig(long configId) throws SzException 
    {
        return this.env.execute(() -> {
            // create the result object
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.getConfig(configId, sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public String getConfigs() throws SzException {
        return this.env.execute(() -> {
            // create the result object
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.getConfigList(sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public long getDefaultConfigId() throws SzException {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.getDefaultConfigID(result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public void replaceDefaultConfigId(long currentDefaultConfigId, long newDefaultConfigId)
        throws SzException 
    {
        this.env.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.replaceDefaultConfigID(
                currentDefaultConfigId, newDefaultConfigId);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public void setDefaultConfigId(long configId) throws SzException {
        this.env.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.setDefaultConfigID(configId);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });
    }

}
