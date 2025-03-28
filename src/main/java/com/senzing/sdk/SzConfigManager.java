package com.senzing.sdk;

/**
 * <p>
 * Defines the Java interface to the Senzing configuration management functions.
 * </p>
 * 
 * <p>
 * An {@link SzConfigManager} instance is typically obtained from an
 * {@link SzEnvironment} instance via the {@link SzEnvironment#getConfigManager()}
 * method as follows:
 *
 * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getConfigManager"}
 * </p>
 */
public interface SzConfigManager {
    /**
     * Adds the configuration described by the specified JSON to the repository
     * with the specified comment and returns the identifier for referencing the
     * the config in the entity repository.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="addConfig"}
     * </p>
     *
     * @param configDefinition The JSON text describing the configuration.
     * @param configComment The comments for the configuration.
     * @return The identifier for referncing the config in the entity repository.
     * 
     * @throws SzException If a failure occurs.
     */
    long addConfig(String configDefinition, String configComment)
        throws SzException;

    /**
     * Gets the configuration with the specified config ID and returns the 
     * configuration defintion as a {@link String}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getConfig"}
     * </p>
     *
     * @param configId The configuration ID of the configuration to retrieve.
     * 
     * @return The configuration definition as a {@link String}.
     * 
     * @throws SzException If a failure occurs.
     */
    String getConfig(long configId) throws SzException;

    /**
     * Gets the list of saved configuration ID's with their comments and
     * timestamps and return the JSON {@link String} describing them.  An
     * example format for the response is:
     * <pre>
     * {
     *   "CONFIGS": [
     *     {
     *        "CONFIG_ID": 12345678912345,
     *        "SYS_CREATE_DT": "2021-03-25 18:35:00.743",
     *        "CONFIG_COMMENTS": "Added EMPLOYEES data source."
     *     },
     *     {
     *        "CONFIG_ID": 23456789123456,
     *        "SYS_CREATE_DT": "2021-02-08 23:27:09.876",
     *        "CONFIG_COMMENTS": "Added CUSTOMERS data source."
     *     },
     *     {
     *        "CONFIG_ID": 34567891234567,
     *        "SYS_CREATE_DT": "2021-02-08 23:27:05.212",
     *        "CONFIG_COMMENTS": "Initial Config"
     *     },
     *     . . .
     *   ]
     * }
     * </pre>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getConfigs"}
     * </p>
     *
     * @return The JSON {@link String} describing the configurations registered
     *         in the entity repository with their identifiers, timestamps and 
     *         comments.
     * 
     * @throws SzException If a failure occurs.
     */
    String getConfigs() throws SzException;

    /**
     * Gets the configuration ID of the default configuration for the repository
     * and returns it.  If the entity repository is in the initial state and the
     * default configuration ID has not yet been set, then zero (0) is returned.
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getDefaultConfigId"}
     * </p>
     * 
     * @return The current default cofiguration ID in the repository, or zero (0)
     *         if the entity repository is in the initial state with no default
     *         configuration ID having yet been set.
     * 
     * @throws SzException If a failure occurs.
     */
    long getDefaultConfigId() throws SzException;

    /**
     * Replaces the current configuration ID of the repository with the specified
     * new configuration ID providing the current configuration ID of the
     * repository is equal to the specified old configuration ID.  If the current
     * configuration ID is not the same as the specified old configuration ID then
     * this method fails to replace the default configuration ID with the new
     * value and an {@link SzReplaceConflictException} is thrown.
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="replaceDefaultConfigId"}
     * </p>
     *
     * @param currentDefaultConfigId The configuration ID that is believed to be the
     *                               current default configuration ID.
     * 
     * @param newDefaultConfigId The new configuration ID for the repository.
     * 
     * @throws SzReplaceConflictException If the default configuration ID was not updated
     *                                    to the specified new value because the current
     *                                    default configuration ID found in the repository
     *                                    was not equal to the specified expected current
     *                                    default configuration ID value.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     */
    void replaceDefaultConfigId(long currentDefaultConfigId, long newDefaultConfigId)
        throws SzReplaceConflictException, SzException;

    /**
     * Sets the default configuration for the repository to the specified
     * configuration ID.
     * 
     * <p>
     * <b>NOTE:</b> This is best used when initializing the Senzing repository with
     * a registered default config ID the first time (i.e.: when there is no existing
     * default config ID registered).  When there is already a default config ID 
     * registered, you should consider using {@link #replaceDefaultConfigId(long, long)}
     * especially if you want to handle race conditions in setting the default config ID.
     * </p>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="setDefaultConfigId"}
     * </p>
     *
     * @param configId The configuration ID to set as the default configuration.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/InitDefaultConfig.java">Code Snippet: Initialize Config</a>
     */
    void setDefaultConfigId(long configId) throws SzException;
}
