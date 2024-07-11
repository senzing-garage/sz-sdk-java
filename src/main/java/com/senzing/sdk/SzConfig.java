package com.senzing.sdk;

/**
 * Defines the Java interface to the Senzing configuration functions.
 */
public interface SzConfig {
  /**
   * Creates a new in-memory configuration using the default configuraiton
   * template and returns the configuration handle for working with it.
   *
   * @return The configuraton handle for working with the configuration
   *         that was created.
   * 
   * @throws SzException If a failure occurs.
   */
  long createConfig() throws SzException;

  /**
   * Creates a new in-memory configuration using the specified configuration
   * definition and returns the configuration handle for working with it.
   * Depending upon implementation of this interface, the specified definition
   * may allow other forms, but it is typically a JSON-formatted Senzing
   * configuration (an example template JSON configuration ships with the 
   * Senzing product).
   * 
   * @param configDefinition The definition for the Senzing configuration.
   * 
   * @return The configuraton handle for working with the configuration
   *         that was created and populated with the specified definition.
   *
   * @throws SzException If a failure occurs.
   */
  long importConfig(String configDefinition) throws SzException;
  
  /**
   * Obtains the configuration definition formatted as JSON for the in-memory
   * configuration associated with the specified configuration handle.
   *
   * @param configHandle The configuration handle associated with the 
   *                     in-memory configuration to be formatted as JSON.
   * 
   * @return The configuration defininition formatted as JSON. 
   *
   * @throws SzException If a failure occurs.
   */
  String exportConfig(long configHandle) throws SzException;

  /**
   * Closes the in-memory configuration associated with the specified config
   * handle and cleans up system resources.  After calling this method, the
   * configuration handle can no longer be used and becomes invalid.
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   *
   * @throws SzException If a failure occurs.
   */
  void closeConfig(long configHandle) throws SzException;

  /**
   * Extracts the data sources from the in-memory configuration associated
   * with the specified config handle returns the JSON text describing the
   * data sources from the configuration.
   * 
   * <p>
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
   * @param configHandle The config handle associated witht he in-memory
   *                     configuration from which to obtain the data sources.
   * 
   * @return The JSON {@link String} describing the data sources found in 
   *         the configuration.
   *
   * @throws SzException If a failure occurs.
   */
  String getDataSources(long configHandle) throws SzException;

  /**
   * Adds a new data source that is identified by the specified data source
   * code to the in-memory configuration associated with the specified 
   * configuraiton handle.
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param dataSourceCode The data source code for the new data source.
   *
   * @return The JSON {@link String} describing the data source was 
   *         added to the configuration.
   * 
   * @throws SzException If a failure occurs.
   */
  String addDataSource(long configHandle, String dataSourceCode)
    throws SzException;

  /**
   * Deletes the data source identified by the specified data source code
   * from the in-memory configuration associated with the specified config
   * handle.
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param dataSourceCode The data source code that identifies the data
   *                       source to delete from the configuration.
   *
   * @throws SzException If a failure occurs.
   */
  void deleteDataSource(long configHandle, String dataSourceCode)
    throws SzException;
    
}
