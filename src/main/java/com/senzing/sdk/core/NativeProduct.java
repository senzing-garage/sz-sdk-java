/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Defines the Java interface to the G2 product functions.  The G2 product
 * functions provide information regarding the Senzing product installation
 * and user license.
 *
 */
interface NativeProduct extends NativeApi
{
  /**
   * Initializes the G2 product API with the specified module name,
   * init parameters and flag indicating verbose logging.  If the
   * <code>G2CONFIGFILE</code> init parameter is absent then the default
   * configuration from the repository is used.
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
   * Uninitializes the G2 product API.
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
   * Determines whether a specified license file is valid.
   *
   * @param licenseFile The path
   * @param errorResponse The {@link StringBuffer} to write any error response
   *                      to (if an error occurs).
   * @return Zero (0) for valid license, one (1) for invalid, and a negative
   *         number for errors.
   */
  int validateLicenseFile(String licenseFile, StringBuffer errorResponse);

  /**
   * Determines whether a specified license Base-64 string is valid.
   *
   * @param licenseData The license data as a encoded Base-64 {@link String}.
   * @param errorResponse The {@link StringBuffer} to write any error response
   *                      to (if an error occurs).
   * @return Zero (0) for valid license, one (1) for invalid, and a negative
   *         number for errors.
   */
  int validateLicenseStringBase64(String licenseData, StringBuffer errorResponse);

  /**
   * Returns the currently installed version details.
   *
   * @return JSON document of version details.
   */
  String version();
}
