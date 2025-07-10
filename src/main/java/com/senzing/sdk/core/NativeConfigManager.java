/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Defines the Java interface to the G2 configuration management functions.
 */
interface NativeConfigManager extends NativeApi 
{
    /**
     * Initializes the G2 config manager API with the specified module name,
     * init parameters and flag indicating verbose logging.
     *
     * @param moduleName A short name given to this instance of the config
     *                   manager API.
     * @param iniParams A JSON string containing configuration parameters.
     * @param verboseLogging Enable diagnostic logging which will print a massive
     *                       amount of information to stdout.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int init(String moduleName, String iniParams, boolean verboseLogging);

    /**
     * Uninitializes the G2 config manager API.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int destroy();

    /**
     * Adds the configuration described by the specified JSON to the repository
     * with the specified comments and returns the ID of the config in the
     * specified {@link Result} object.
     *
     * @param configStr The JSON text describing the configuration.
     * @param configComments The comments for the configuration.
     * @param configID The configuration ID for the registered config.
     * @return Zero (0) on success and non-zero on failure.
     */
    int registerConfig(String configStr, String configComments, Result<Long> configID);

    /**
     * Gets the configuration with the specified config ID and writes the JSON
     * text of configuration to the specified {@link StringBuffer}.
     *
     * @param configID The configuration ID of the configuration to retrieve.
     * @param response The {@link StringBuffer} to write the JSON text of the
     *                 configuration to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int getConfig(long configID, StringBuffer response);

    /**
     * Gets the list of saved configuration IDs with their comments and
     * timestamps and writes the JSON text describing the configurations to
     * the specified {@link StringBuffer}.  The format of the response is:
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
     * @param response The {@link StringBuffer} to write the JSON text of the
     *                 configuration list to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int getConfigRegistry(StringBuffer response);

    /**
     * Sets the default configuration for the repository to the specified
     * configuration ID.
     *
     * @param configID The configuration ID to set as the default configuration.
     * @return Zero (0) on success and non-zero on failure.
     */
    int setDefaultConfigID(long configID);

    /**
     * Gets the configuration ID of the default configuration for the repository
     * and sets the value in the specified {@link Result} object.
     *
     * @param configID The {@link Result} object on which to set the
     *                 default configuration ID.
     * @return Zero (0) on success and non-zero on failure.
     */
    int getDefaultConfigID(Result<Long> configID);

    /**
     * Replaces the current configuration ID of the repository with the specified
     * new configuration ID providing the current configuration ID of the
     * repository is equal to the specified old configuration ID.  If the current
     * configuration ID is not the same as the specified old configuration ID then
     * this method fails to replace the default configuration ID with the new
     * value.
     *
     * @param oldConfigID The configuration ID that is believed to be the
     *                    current default configuration ID.
     * @param newConfigID The new configuration ID for the repository.
     * @return Zero (0) on success and non-zero on failure
     */
    int replaceDefaultConfigID(long oldConfigID, long newConfigID);
}
