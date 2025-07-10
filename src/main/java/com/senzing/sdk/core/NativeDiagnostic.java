/**********************************************************************************
 © Copyright Senzing, Inc. 2021-2023
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Defines the Java interface to the G2 diagnostic functions.  The G2 diagnostic
 * functions provide diagnostics and statistics pertaining to the host system
 * and the Senzing repository.
 */
interface NativeDiagnostic extends NativeApi
{
  /**
   * Initializes the G2 Diagnostic object with the specified module name,
   * init parameters and flag indicating verbose logging.  If the
   * <code>G2CONFIGFILE</code> init parameter is absent then the default
   * configuration from the repository is used.
   *
   * @param moduleName A short name given to this instance of the diagnostic
   *                   object.
   * @param iniParams A JSON string containing configuration parameters.
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout.
   *
   * @return Zero (0) on success, non-zero on failure.
   */
  int init(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Initializes the G2 Diagnostic object with the module name, initialization
   * parameters, verbose logging flag and a specific configuration ID
   * identifying the configuration to use.
   *
   * @param moduleName The module name with which to initialize.
   * @param iniParams The JSON initialization parameters.
   * @param initConfigID The specific configuration ID to initialize with.
   * @param verboseLogging Whether or not to initialize with verbose logging.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int initWithConfigID(String   moduleName,
                       String   iniParams,
                       long     initConfigID,
                       boolean  verboseLogging);

  /**
   * Reinitializes with the specified configuration ID.
   *
   * @param initConfigID The configuration ID with which to reinitialize.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int reinit(long initConfigID);

  /**
   * Uninitializes the G2 diagnostic object.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int destroy();

  /**
   * Gathers database information and writes detail of the result
   * as JSON in the specified {@link StringBuffer}.
   *
   * @param response The {@link StringBuffer} in which to write the JSON text
   *                 that details the database information.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRepositoryInfo(StringBuffer response);

  /**
   * Runs non-destruction DB performance tests and writes detail of the result
   * as JSON in the specified {@link StringBuffer}.
   *
   * @param secondsToRun How long to run the database performance test.
   * @param response The {@link StringBuffer} in which to write the JSON text
   *                 that details the result of the performance test.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int checkRepositoryPerformance(int secondsToRun, StringBuffer response);

  /**
   * Purges all data in the configured repository
   * WARNING: There is no undoing from this.  Make sure your repository is
   * regularly backed up.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int purgeRepository();

  /**
   * Experimental/internal method for obtaining diagnostic feature information.
   *
   * @param libFeatID The <code>LIB_FEAT_ID</code> identifying the feature.
   * @param response The {@link StringBuffer} to write the JSON result
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeature(long libFeatID, StringBuffer response);

}
