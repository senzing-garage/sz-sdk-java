/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Implements the {@link NativeConfig} interface to call the native implementations
 * of the functions.
 */
class NativeConfigJni implements NativeConfig {
  static {
    System.loadLibrary("Sz");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native int init(String  instanceName,
                         String  settings,
                         boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int create(Result<Long> configHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int load(String jsonConfig, Result<Long> configHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int export(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int close(long configHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getDataSourceRegistry(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int registerDataSource(long         configHandle,
                                       String       inputJson,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int unregisterDataSource(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  public native String getLastException();

  /**
   * {@inheritDoc}
   */
  public native int getLastExceptionCode();

  /**
   * {@inheritDoc}
   */
  public native void clearLastException();
}

