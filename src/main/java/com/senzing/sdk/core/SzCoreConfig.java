package com.senzing.sdk.core;

import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzException;

import static com.senzing.sdk.core.Utilities.jsonEscape;

/**
 * The package-private core implementation of {@link SzConfig}
 * that works with the {@link SzCoreEnvironment} class.
 */
class SzCoreConfig implements SzConfig {
    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link NativeConfigJni} instance.
     */
    private NativeConfigJni nativeApi = null;

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
    SzCoreConfig(SzCoreEnvironment environment)
        throws IllegalStateException, SzException 
    {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new NativeConfigJni();

            int returnCode = this.nativeApi.init(this.env.getInstanceName(),
                                                 this.env.getSettings(),
                                                 this.env.isVerboseLogging());

            this.env.handleReturnCode(returnCode, this.nativeApi);

            // no return value, so return null
            return null;
        });
    }

    /**
     * Gets the associated {@link NativeConfigJni} instance.
     * 
     * @return The associated {@link NativeConfigJni} instance.
     */
    NativeConfigJni getNativeApi() {
        return this.nativeApi;
    }

    /**
     * The package-protected function to destroy the Senzing Config SDK.
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

    @Override
    public long createConfig() throws SzException {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.create(result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    @Override
    public long importConfig(String configDefinition) throws SzException {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.load(configDefinition, result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    @Override
    public String exportConfig(long configHandle) throws SzException {
        return this.env.execute(() -> {
            // create the response buffer
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.save(configHandle, sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the contents of the buffer
            return sb.toString();
        });
    }

    @Override
    public void closeConfig(long configHandle) throws SzException {
        this.env.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.close(configHandle);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);
            
            // return null
            return null;
        });
    }

    @Override
    public String getDataSources(long configHandle) throws SzException {
        return this.env.execute(() -> {
            // create the response buffer
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.listDataSources(configHandle, sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the contents of the buffer
            return sb.toString();
        });
    }

    @Override
    public String addDataSource(long configHandle, String dataSourceCode) 
        throws SzException 
    {
        return this.env.execute(() -> {
            // format the JSON for the native call
            String inputJson = "{\"DSRC_CODE\":" + jsonEscape(dataSourceCode) + "}";

            // create a StringBuffer for calling the native call
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.addDataSource(
                configHandle, inputJson, sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return sb.toString();
        });              
    }

    @Override
    public void deleteDataSource(long configHandle, String dataSourceCode) 
        throws SzException 
    {
        this.env.execute(() -> {
            // format the JSON for the JNI call
            String inputJson = "{\"DSRC_CODE\":" + jsonEscape(dataSourceCode) + "}";

            // call the underlying C function
            int returnCode = this.nativeApi.deleteDataSource(
                configHandle, inputJson);
            
            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });
    }
}
