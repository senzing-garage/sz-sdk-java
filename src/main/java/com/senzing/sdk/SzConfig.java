package com.senzing.sdk;

/**
 * <p>
 * Defines the Java interface that encapsulates and represents a
 * Senzing configuration and provides functions to operate on that
 * configuration.
 * </p>
 * 
 * <p>
 * An {@link SzConfig} instance is typically obtained from an
 * {@link SzConfigManager} instance via one of the following methods:
 * <ul>
 *   <li>{@link SzConfigManager#createConfig()}
 *   <li>{@link SzConfigManager#createConfig(String)}
 *   <li>{@link SzConfigManager#createConfig(long)}
 * </ul>
 *
 * <p><b>Create from template configuration:</b>
 * {@snippet class="com.senzing.sdk.SzConfigDemo" region="createConfigFromTemplate"}
 * 
 * <p><b>Create from configuration definition:</b>
 * {@snippet class="com.senzing.sdk.SzConfigDemo" region="createConfigFromDefinition"}
 * 
 * <p><b>Create from registered configuration ID:</b>
 * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="createConfigFromConfigId"}
 */
public interface SzConfig {
    /**
     * Retrieves the definition for this configuration.
     * 
     * <p><b>Note:</b>
     * Typically, an implementation's {@link Object#toString()} function
     * will be implemented to return the result from this function.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" region="exportConfig"}
     * </p>
     *
     * <p><b>Example Result:</b><br>
     * The example result is rather large, but can viewed 
     * <a target="_blank" href="doc-files/SzConfigDemo-exportConfig.txt">here</a> 
     * (formatted for readability).
     * </p>
     *
     * @return The configuration definition formatted as a JSON object.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/InitDefaultConfig.java">Code Snippet: Initialize Config</a>
     */
    String export() throws SzException;

    /**
     * Gets the data source registry for this configuration.
     *  
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" region="getDataSourceRegistry"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzConfigDemo-getDataSourceRegistry.txt"}
     * </p>
     * 
     * @return The data source registry describing the data sources for this 
     *         configuration formatted as a JSON object.
     *
     * @throws SzException If a failure occurs.
     */
    String getDataSourceRegistry() throws SzException;

    /**
     * Adds a data source to this configuration.
     * 
     * <p>
     * Because {@link SzConfig} is an in-memory representation, the repository is not
     * changed unless the configuration is {@linkplain #export() exported} and then
     * {@linkplain SzConfigManager#registerConfig(String,String) registered} via
     * {@link SzConfigManager}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" region="registerDataSource"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzConfigDemo-registerDataSource.txt"}
     * </p>
     * 
     * @param dataSourceCode The data source code for the new data source.
     *
     * @return The JSON {@link String} describing the data source was
     *         added to the configuration.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     */
    String registerDataSource(String dataSourceCode)
        throws SzException;

    /**
     * Removes a data source from this configuration.
     * 
     * <p>
     * Because {@link SzConfig} is an in-memory representation, the repository is not
     * changed unless the configuration is {@linkplain #export() exported} and then
     * {@linkplain SzConfigManager#registerConfig(String,String) registered} via
     * {@link SzConfigManager}.
     * </p>
     * 
     * <p><b>NOTE:</b> This method is idempotent in that it succeeds with no changes
     * being made when specifying a data source code that is not found in the registry.</p>
     * 
     * <p><b>WARNING:</b> If records in the repository refer to the unregistered data 
     * source, the configuration cannot be used as the active configuration.</p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" region="unregisterDataSource"}
     * </p>
     *
     * @param dataSourceCode The data source code that identifies the data
     *                       source to delete from the configuration.
     *
     * @throws SzException If a failure occurs.
     */
    void unregisterDataSource(String dataSourceCode)
        throws SzException;
}
