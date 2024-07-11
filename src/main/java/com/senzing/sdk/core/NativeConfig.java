/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Defines the Java interface to the G2 configuration functions.
 */
interface NativeConfig extends NativeApi
{
  /**
   * Initializes the G2 config API with the specified instance name,
   * settings and <code>boolean</code> flag indicating verbose logging.
   *
   * @param instanceName A short name given to this instantiation of Senzing that 
   *                     will be used in
   * @param settings A JSON string containing configuration parameters.
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int init(String instanceName, String settings, boolean verboseLogging);

  /**
   * Uninitializes the G2 config API and cleans up system resources.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int destroy();

  /**
   * Creates a new in-memory configuration using the default template and
   * sets the specified {@link Result} parameter with the value of the
   * configuration handle for working with it.
   *
   * @param configHandle The {@link Result} object on which to set the value of
   *                     the configuration handle.
   * @return Zero (0) on success and non-zero on failure.
   */
  int create(Result<Long> configHandle);

  /**
   * Creates a new in-memory configuration using the specified JSON text and
   * sets the specified {@link Result} parameter with the value of the
   * configuration handle for working with it.
   *
   * @param jsonConfig The JSON text for the config.
   * @param configHandle The {@link Result} object on which to set the value of
   *                     the configuration handle.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int load(String jsonConfig, Result<Long> configHandle);

  /**
   * Writes the JSON text for the configuration associated with the specified
   * configuration handle to the specified {@link StringBuffer}.
   *
   * @param configHandle The configuration handle to export the JSON text from.
   * @param response The {@link StringBuffer} to write the JSON text to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int save(long configHandle, StringBuffer response );

  /**
   * Closes the in-memory configuration associated with the specified config
   * handle and cleans up system resources.  After calling this method, the
   * configuration handle can no longer be used and becomes invalid.
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @return Zero (0) on success and non-zero on failure.
   */
  int close(long configHandle);

  /**
   * Extracts the data sources from the in-memory configuration associated with
   * the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the data sources from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"DATA_SOURCES": [
   *    {
   * 			"DSRC_ID": 1,
   * 			"DSRC_CODE": "TEST"
   *    },
   *    {
   * 			"DSRC_ID": 2,
   * 			"DSRC_CODE": "SEARCH"
   *    }
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listDataSources(long configHandle, StringBuffer response);

  /**
   * Adds a data source described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "DSRC_CODE": "CUSTOMERS"
   *   }
   * </pre>
   * Optionally, you can specify the data source ID:
   * <pre>
   *   {
   *     "DSRC_CODE": "CUSTOMERS",
   *     "DSRC_ID": 410
   *   }
   * </pre>
   * <p>
   * The response JSON provides the data source ID of the created data source,
   * which is especially useful if the data source ID was not specified in the
   * input:
   * <pre>
   *   {
   *     "DSRC_ID": 410
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the data source to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addDataSource(long          configHandle,
                    String        inputJson,
                    StringBuffer  response);

  /**
   * Deletes the data source described by the specified JSON from the in-memory
   * configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "DSRC_CODE": "CUSTOMERS"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the data source to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteDataSource(long configHandle, String inputJson);
}

