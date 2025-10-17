package com.senzing.sdk.core;

import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzException;

import static com.senzing.sdk.core.Utilities.jsonEscape;

import java.util.Objects;

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
     * The underlying {@link NativeConfig} instance.
     */
    private NativeConfig nativeApi = null;

    /**
     * The backing config definition.
     */
    private String configDefinition;

    /**
     * Constructs with the specified {@link SzCoreEnvironment}.
     * 
     * @param environment The {@link SzCoreEnvironment} with which to 
     *                    construct.
     * 
     * @param nativeConfig The {@link NativeConfig} to use.
     * 
     * @param configDefinition The {@link String} config definition describing the
     *                         configuration represented by this instance.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreEnvironment} instance 
     *                               has already been destroyed.
     * 
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreConfig(SzCoreEnvironment  environment, 
                 NativeConfig       nativeConfig,
                 String             configDefinition)
        throws IllegalStateException, SzException 
    {
        Objects.requireNonNull(
            environment, "The specified SzEnvironment cannot be null");
        Objects.requireNonNull(
            nativeConfig, "The specified NativeConfig API cannot be null");
        Objects.requireNonNull(
            configDefinition, "The specified config definition cannot be null");
        
        this.env                = environment;
        this.configDefinition   = configDefinition;
        this.nativeApi          = nativeConfig;
    }

    /**
     * Gets the associated {@link NativeConfig} instance.
     * 
     * @return The associated {@link NativeConfig} instance.
     */
    NativeConfig getNativeApi() {
        return this.nativeApi;
    }

    @Override
    public String export() throws SzException {
        return this.env.execute(() -> {
            return this.configDefinition;
        });
    }

    @Override
    public String toString() {
        return SzCoreUtilities.configToString(this);
    }

    @Override
    public String getDataSourceRegistry() throws SzException {
        return this.env.execute(() -> {
            // load the configuration
            Result<Long> result = new Result<>();

            int returnCode = this.nativeApi.load(this.configDefinition, 
                                                 result);
 
            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);
            
            // get the config handle
            long configHandle = result.getValue();

            // create the response buffer
            StringBuffer sb = new StringBuffer();

            try {
                // call the underlying C function
                returnCode = this.nativeApi.getDataSourceRegistry(configHandle, sb);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);

                // return the contents of the buffer
                return sb.toString();

            } finally {
                // close the config handle
                returnCode = this.nativeApi.close(configHandle);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);
            }
        });
    }

    @Override
    public String registerDataSource(String dataSourceCode) 
        throws SzException 
    {
        return this.env.execute(() -> {
            // load the configuration
            Result<Long> result = new Result<>();

            int returnCode = this.nativeApi.load(this.configDefinition, 
                                                 result);
 
            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);
            
            // get the config handle
            long configHandle = result.getValue();

            try {
                // format the JSON for the native call
                String inputJson = "{\"DSRC_CODE\":" + jsonEscape(dataSourceCode) + "}";

                // create a StringBuffer for calling the native call
                StringBuffer sb = new StringBuffer();

                // call the underlying C function
                returnCode = this.nativeApi.registerDataSource(
                    configHandle, inputJson, sb);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);

                // store the JSON result
                String jsonResult = sb.toString();

                // export the new config
                sb.delete(0, sb.length());
                returnCode = this.nativeApi.export(configHandle, sb);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);

                // store the new config definition
                this.configDefinition = sb.toString();

                // return the JSON result
                return jsonResult;

            } finally {
                // close the config handle
                returnCode = this.nativeApi.close(configHandle);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);
            }
        });              
    }

    @Override
    public void unregisterDataSource(String dataSourceCode) 
        throws SzException 
    {
        this.env.execute(() -> {
            // load the configuration
            Result<Long> result = new Result<>();

            int returnCode = this.nativeApi.load(this.configDefinition, 
                                                 result);
 
            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.nativeApi);
            
            // get the config handle
            long configHandle = result.getValue();

            try {
                // format the JSON for the JNI call
                String inputJson = "{\"DSRC_CODE\":" + jsonEscape(dataSourceCode) + "}";

                // call the underlying C function
                returnCode = this.nativeApi.unregisterDataSource(
                    configHandle, inputJson);
                
                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);

                // export the new config
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.export(configHandle, sb);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);

                // store the new config definition
                this.configDefinition = sb.toString();

                // return null
                return null;

            } finally {
                // close the config handle
                returnCode = this.nativeApi.close(configHandle);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.nativeApi);
            }
        });
    }
}
