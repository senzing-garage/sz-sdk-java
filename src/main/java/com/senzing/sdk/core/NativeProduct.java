/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Defines the Java interface to the Senzing product functions.  The Senzing
 * product functions provide information regarding the Senzing product
 * installation and user license.
 */
interface NativeProduct extends NativeApi
{
  /**
   * Initializes the Senzing product API with the specified module name,
   * init parameters and flag indicating verbose logging.
   *
   * @param moduleName A short name given to this instance of the product API.
   * @param iniParams A JSON string containing configuration parameters.
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int init(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Uninitializes the Senzing product API.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int destroy();

  /**
   * Returns the currently configured license details.
   *
   * @return The JSON document describing the license details.
   */
  String license();

  /**
   * Returns the currently installed version details.
   *
   * @return A JSON document describing the version details.
   */
  String version();
}
