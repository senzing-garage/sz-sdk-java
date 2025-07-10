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
     * Obtains the configuration definition (typically formatted as JSON)
     * for this configuration.
     * 
     * <p><b>Note:</b>
     * Typically, an implementation's {@link Object#toString()} function
     * will be implemented to return the result from this function.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" region="exportConfig"}
     * </p>
     *
     * @return The configuration definition (typically formatted as JSON).
     *
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/InitDefaultConfig.java">Code Snippet: Initialize Config</a>
     */
    String export() throws SzException;

    /**
     * Extracts the data sources from this configuration and returns the
     * JSON text describing the data sources from the configuration.
     * 
     * <p>
     * The format of the JSON response is as follows:
     * 
     * <pre>
     * {
     *   "DATA_SOURCES": [
     *     {
     *       "DSRC_ID": 1,
     *       "DSRC_CODE": "TEST"
     *     },
     *     {
     *       "DSRC_ID": 2,
     *       "DSRC_CODE": "SEARCH"
     *     }
     *   ]
     * }
     * </pre>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" region="getDataSourceRegistry"}
     * </p>
     * 
     * @return The JSON {@link String} describing the data sources found in
     *         the configuration.
     *
     * @throws SzException If a failure occurs.
     */
    String getDataSourceRegistry() throws SzException;

    /**
     * Adds a new data source that is identified by the specified data source
     * code to this configuration.  An exception is thrown if the data source
     * already exists in the configuration.
     * <p>
     * The response JSON provides the data source ID of the created data source
     * and has the following format:
     * <pre>
     *   {
     *     "DSRC_ID": 410
     *   }
     * </pre>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" region="registerDataSource"}
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
     * Deletes the data source identified by the specified data source code
     * from this configuration.
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
