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
 * 
 * @since 4.0.0
 */
public interface SzConfigManager {
    /**
     * Creates a new {@link SzConfig} instance from the template
     * configuration definition.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo" 
     *           region="createConfigFromTemplate"}
     * </p>
     *
     * @return A newly created {@link SzConfig} instance representing the
     *         template configuration definition.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/InitDefaultConfig.java">Code Snippet: Initialize Config</a>
     * 
     * @since 4.0.0
     */
    SzConfig createConfig() throws SzException;

    /**
     * Creates a new {@link SzConfig} instance from a configuration
     * definition.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigDemo"
     *           region="createConfigFromDefinition"}
     * </p>
     * 
     * @param configDefinition The definition for the Senzing configuration.
     * 
     * @return A newly created {@link SzConfig} representing the specified
     *         configuration definition.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     * 
     * @since 4.0.0
     */
    SzConfig createConfig(String configDefinition) throws SzException;

    /**
     * Creates a new {@link SzConfig} instance for a configuration ID.
     * 
     * <p>
     * If the configuration ID is not found then an exception is thrown.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo"
     *           region="createConfigFromConfigId"}
     * </p>
     *
     * @param configId The configuration ID of the configuration definition 
     *                 to retrieve.
     * 
     * @return A newly created {@link SzConfig} instance representing the
     *         configuration definition that is registered with the
     *         specified config ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @since 4.0.0
     */
    SzConfig createConfig(long configId) throws SzException;

    /**
     * Registers a configuration definition in the repository.
     * 
     * <p>
     * <b>NOTE:</b>: Registered configurations do not become immediately
     * active nor do they become the default.  Further, registered
     * configurations cannot be unregistered.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo"
     *           region="registerConfigWithComment"}
     * </p>
     *
     * @param configDefinition The configuration definition to register.
     * @param configComment The comments for the configuration.
     * @return The identifier for referencing the config in the entity repository.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     * 
     * @since 4.0.0
     */
    long registerConfig(String configDefinition, String configComment)
        throws SzException;

    /**
     * Registers a configuration definition in the repository with an
     * auto-generated comment.
     * 
     * <p>
     * <b>NOTE:</b>: Registered configurations do not become immediately
     * active nor do they become the default.  Further, registered
     * configurations cannot be unregistered.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="registerConfig"}
     * </p>
     *
     * @param configDefinition The configuration definition to register.
     * @return The identifier for referencing the config in the entity repository.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     * 
     * @since 4.0.0
     */
    long registerConfig(String configDefinition)
        throws SzException;

    /**
     * Gets the configuration registry.
     * 
     * <p>
     * The registry contains the original timestamp, original comment and 
     * configuration ID of all configurations ever registered with the repository.
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> Registered configurations cannot be unregistered.
     * </p>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getConfigRegistry"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzConfigManagerDemo-getConfigRegistry.txt"}
     * </p>
     * 
     * @return The JSON object {@link String} describing the configurations
     *         registered in the repository with their identifiers, timestamps
     *         and comments.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @since 4.0.0
     */
    String getConfigRegistry() throws SzException;

    /**
     * Gets the default configuration ID for the repository.
     * 
     * <p>
     * Unless an explicit configuration ID is specified at initialization, 
     * the default configuration ID is used.
     * </p>
     * 
     * <p><b>NOTE:</b> The default configuration ID may not be the same as 
     * the active configuration ID.</p>
     * 
     * Gets the configuration ID of the default configuration for the repository
     * and returns it.  If the entity repository is in the initial state and the
     * default configuration ID has not yet been set, then zero (0) is returned.
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="getDefaultConfigId"}
     * </p>
     * 
     * @return The current default configuration ID, or zero (0) if the default
     *         configuration has not been set.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/AddDataSources.java">Code Snippet: Add Data Sources</a>
     * 
     * @since 4.0.0
     */
    long getDefaultConfigId() throws SzException;

    /**
     * Replaces the existing default configuration ID with a new configuration ID.
     * 
     * <p>
     * The change is prevented (with an {@link SzReplaceConflictException} being thrown)
     * if the current default configuration ID value is not as expected.  Use this in
     * place of {@link #setDefaultConfigId(long)} to handle race conditions.
     * </p>
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
     * 
     * @since 4.0.0
     */
    void replaceDefaultConfigId(long currentDefaultConfigId, long newDefaultConfigId)
        throws SzReplaceConflictException, SzException;

    /**
     * Sets the default configuration ID.
     * 
     * <p>
     * Usually this method is sufficient for setting the default configuration ID.
     * However, in concurrent environments that could encounter race conditions, 
     * consider using {@link #replaceDefaultConfigId(long,long)} instead.
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
     * 
     * @since 4.0.0
     */
    void setDefaultConfigId(long configId) throws SzException;

    /**
     * Registers a configuration in the repository and then sets its ID as
     * the default for the repository.
     * 
     * <p>
     * This is a convenience method for {@link #registerConfig(String,String)} followed
     * by {@link #setDefaultConfigId(long)}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" 
     *           region="setDefaultConfigWithComment"}
     * </p>
     *
     * @param configDefinition The configuration definition to register as the default.
     * @param configComment The comments for the configuration.
     * 
     * @return The configuration ID under which the configuration was registered.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/InitDefaultConfig.java">Code Snippet: Initialize Config</a>
     * 
     * @since 4.0.0
     */
    long setDefaultConfig(String configDefinition, String configComment) throws SzException;

    /**
     * Registers a configuration in the repository and then sets its ID as
     * the default for the repository with an auto-generated comment.
     * 
     * <p>
     * This is a convenience method for {@link #registerConfig(String)} followed
     * by {@link #setDefaultConfigId(long)}.
     * </p>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzConfigManagerDemo" region="setDefaultConfig"}
     * </p>
     *
     * @param configDefinition The configuration definition to register as the default.
     * 
     * @return The configuration ID under which the configuration was registered.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/configuration/InitDefaultConfig.java">Code Snippet: Initialize Config</a>
     * 
     * @since 4.0.0
     */
    long setDefaultConfig(String configDefinition) throws SzException;
}
