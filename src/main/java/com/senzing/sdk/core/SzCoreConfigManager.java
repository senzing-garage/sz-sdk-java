package com.senzing.sdk.core;

import java.util.Set;
import java.util.TreeSet;

import com.senzing.sdk.SzException;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzConfig;

/**
 * The package-private core implementation of {@link SzConfigManager}
 * that works with the {@link SzCoreEnvironment} class.
 */
class SzCoreConfigManager implements SzConfigManager {    
    /**
     * The <b>unmodifiable</b> {@link Set} of default data sources.
     */
    private static final Set<String> DEFAULT_SOURCES 
        = Set.of("TEST", "SEARCH");

    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link NativeConfigJni} instance.
     */
    private NativeConfigJni configApi = null;

    /**
     * The underlying {@link NativeConfigManagerJni} instance.
     */
    private NativeConfigManagerJni configMgrApi = null;

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
            this.configApi = new NativeConfigJni();

            int returnCode = this.configApi.init(this.env.getInstanceName(),
                                                 this.env.getSettings(),
                                                 this.env.isVerboseLogging());

            this.env.handleReturnCode(returnCode, this.configApi);

            // no return value, so return null
            return null;
        });
    }

    /**
     * Gets the associated {@link NativeConfigJni} instance.
     * 
     * @return The associated {@link NativeConfigJni} instance.
     */
    NativeConfigJni getConfigApi() {
        return this.configApi;
    }

    /**
     * Gets the associated {@link NativeConfigManagerJni} instance.
     * 
     * @return The associated {@link NativeConfigManagerJni} instance.
     * 
     * @throws SzException If a failure occurs.
     */
    NativeConfigManagerJni getConfigManagerApi() 
        throws SzException
    {
        synchronized (this.monitor) {
            // check if the config manager API has not been initialized
            if (this.configMgrApi == null) {
                this.env.execute(() -> {
                    this.configMgrApi = new NativeConfigManagerJni();
        
                    int returnCode = this.configMgrApi.init(
                        this.env.getInstanceName(),
                        this.env.getSettings(),
                        this.env.isVerboseLogging());
        
                    this.env.handleReturnCode(returnCode, this.configMgrApi);
        
                    // no return value, so return null
                    return null;
                });        
            }

            // return the config manager API
            return this.configMgrApi;
        }
    }

    /**
     * The package-protected function to destroy the Senzing Config Manager SDK.
     */
    void destroy() {
        synchronized (this.monitor) {
            // destroy the config manager API
            if (this.configMgrApi != null) {
                this.configMgrApi.destroy();
                this.configMgrApi = null;
            }

            // destroy the config API
            if (this.configApi != null) {
                this.configApi.destroy();
                this.configApi = null;
            }
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
            return (this.configApi == null);
        }
    }
    
    @Override
    public SzConfig createConfig() throws SzException {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.configApi.create(result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

            // get the config handle
            long configHandle = result.getValue();

            try {
                // export the new config
                StringBuffer sb = new StringBuffer();
                returnCode = this.configApi.save(configHandle, sb);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.configApi);

                // store the new config definition
                return new SzCoreConfig(this.env, sb.toString());
                
            } finally {
                // close the config handle
                returnCode = this.configApi.close(configHandle);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.configApi);
            }
        });
    }

    @Override
    public SzConfig createConfig(String configDefinition) throws SzException {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function to load the config
            // and thus validate it before just assuming it is good
            int returnCode = this.configApi.load(configDefinition, result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

            // get the config handle
            long configHandle = result.getValue();

            try {
                // export the new config
                StringBuffer sb = new StringBuffer();
                returnCode = this.configApi.save(configHandle, sb);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.configApi);

                // store the new config definition
                return new SzCoreConfig(this.env, sb.toString());
                
            } finally {
                // close the config handle
                returnCode = this.configApi.close(configHandle);

                // handle any error code if there is one
                this.env.handleReturnCode(returnCode, this.configApi);
            }
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public SzConfig createConfig(long configId) throws SzException 
    {
        return this.env.execute(() -> {
            // get the config manager API
            NativeConfigManager nativeApi = this.getConfigManagerApi();
            
            // create the result object
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = nativeApi.getConfig(configId, sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

            // return the config instance
            return new SzCoreConfig(this.env, sb.toString());
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public long registerConfig(String configDefinition, String configComment)
        throws SzException 
    {
        return this.env.execute(() -> {
            // get the config manager API
            NativeConfigManager nativeApi = this.getConfigManagerApi();

            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = nativeApi.addConfig(
                configDefinition, configComment, result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

            // return the config handle
            return result.getValue();
        });
    }

    /**
     * Finds the index of the first non-whitespace character after the
     * specified index from the specified character array.
     * 
     * @param charArray The character array.
     * @param fromIndex The starting index.
     * 
     * @return The index of the first non-whitespace character or the 
     *         length of the character array if no non-whitespace 
     *         character is found.
     */
    private static int eatWhiteSpace(char[] charArray, int fromIndex) {
        int index = fromIndex;
        
        // advance past any whitespace
        while (index < charArray.length && Character.isWhitespace(charArray[index])) {
            index++;
        }

        // return the index
        return index;
    }

    /**
     * Produce an auto-generated configuration comment for the 
     * configuration maanger registry.  This does a psuedo-JSON 
     * parse to avoid a third-party JSON parser dependency.
     * 
     * @param configDefinition The {@link String} configuration
     *                         definition.
     * 
     * @return The auto-generated comment, which may be empty-string
     *         if an auto-generated comment could not otherwise be produced.
     */
    protected String createConfigComment(String configDefinition) 
    {
        int index = configDefinition.indexOf("\"CFG_DSRC\"");
        if (index < 0) {
            return "";
        }
        char[] charArray = configDefinition.toCharArray();

        // advance past any whitespace
        index = eatWhiteSpace(charArray, index + "\"CFG_DSRC\"".length());
        
        // check for the colon
        if (index >= charArray.length || charArray[index++] != ':') {
            return "";
        }

        // advance past any whitespace
        index = eatWhiteSpace(charArray, index);
        
        // check for the open bracket
        if (index >= charArray.length || charArray[index++] != '[') {
            return "";
        }

        // find the end index
        int endIndex = configDefinition.indexOf("]", index);
        if (endIndex < 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Data Sources: ");
        String prefix = "";
        int dataSourceCount = 0;
        Set<String> defaultSources = new TreeSet<>();
        while (index > 0 && index < endIndex) {
            index = configDefinition.indexOf("\"DSRC_CODE\"", index);
            if (index < 0 || index >= endIndex) {
                continue;
            }
            index = eatWhiteSpace(charArray, index + "\"DSRC_CODE\"".length());
            
            // check for the colon
            if (index >= endIndex || charArray[index++] != ':') {
                return "";
            }

            index = eatWhiteSpace(charArray, index);

            // check for the quote
            if (index >= endIndex || charArray[index++] != '"') {
                return "";
            }
            int start = index;

            // find the ending quote
            while (index < endIndex && charArray[index] != '"') {
                index++;
            }
            if (index >= endIndex) {
                return "";
            }
            
            // get the data source code
            String dataSource = configDefinition.substring(start, index);
            if (DEFAULT_SOURCES.contains(dataSource)) {
                defaultSources.add(dataSource);
                continue;
            }
            sb.append(prefix);
            sb.append(dataSource);
            dataSourceCount++;
            prefix = ", ";
        }

        // check if only the default data sources
        if (dataSourceCount == 0 && defaultSources.size() == 0) {
            sb.append("[ NONE ]");
        } else if (dataSourceCount == 0 
                   && defaultSources.size() == DEFAULT_SOURCES.size()) 
        {
            sb.append("[ ONLY DEFAULT ]");

        } else if (dataSourceCount == 0) {

            sb.append("[ SOME DEFAULT (");
            prefix = "";
            for (String source : defaultSources) {
                sb.append(prefix);
                sb.append(source);
                prefix = ", ";
            }
            sb.append(") ]");
        }

        // return the constructed string
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the {@link #registerConfig(String, String)} function
     * with an auto-generated comment.
     */
    @Override
    public long registerConfig(String configDefinition)
        throws SzException 
    {
        // generate a configuration comment
        String configComment = this.createConfigComment(configDefinition);

        // return the result from the base method
        return this.registerConfig(configDefinition, configComment);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native function.
     */
    @Override
    public String getConfigs() throws SzException {
        return this.env.execute(() -> {
            // get the config manager API
            NativeConfigManager nativeApi = this.getConfigManagerApi();

            // create the result object
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = nativeApi.getConfigList(sb);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

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
            // get the config manager API
            NativeConfigManager nativeApi = this.getConfigManagerApi();

            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = nativeApi.getDefaultConfigID(result);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

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
            // get the config manager API
            NativeConfigManager nativeApi = this.getConfigManagerApi();

            // call the underlying C function
            int returnCode = nativeApi.replaceDefaultConfigID(
                currentDefaultConfigId, newDefaultConfigId);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

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
            // get the config manager API
            NativeConfigManager nativeApi = this.getConfigManagerApi();

            // call the underlying C function
            int returnCode = nativeApi.setDefaultConfigID(configId);

            // handle any error code if there is one
            this.env.handleReturnCode(returnCode, this.configMgrApi);

            // return null
            return null;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the {@link #registerConfig(String, String)} and
     * {@link #setDefaultConfigId(long)} functions.
     */
    @Override
    public long setDefaultConfig(String configDefinition, String configComment) 
        throws SzException 
    {
        // register the configuration
        long configId = this.registerConfig(configDefinition, configComment);

        // set it as the default config ID
        this.setDefaultConfigId(configId);
        
        // return the config ID
        return configId;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the {@link #registerConfig(String)} and
     * {@link #setDefaultConfigId(long)} functions.
     */
    @Override
    public long setDefaultConfig(String configDefinition) 
        throws SzException 
    {
        // register the configuration
        long configId = this.registerConfig(configDefinition);

        // set it as the default config ID
        this.setDefaultConfigId(configId);
        
        // return the config ID
        return configId;
    }
}
