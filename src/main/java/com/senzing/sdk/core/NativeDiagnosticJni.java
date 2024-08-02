/**********************************************************************************
 © Copyright Senzing, Inc. 2021-2023
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Implements the {@link NativeDiagnostic} to call the native implementations of
 * each function.
 */
class NativeDiagnosticJni implements NativeDiagnostic {
  static {
    System.loadLibrary("Sz");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native int init(String   moduleName,
                         String   iniParams,
                         boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initWithConfigID(String   moduleName,
                                     String   iniParams,
                                     long     initConfigID,
                                     boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reinit(long initConfigID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int destroy();


  /**
   * {@inheritDoc}
   */
  @Override
  public native int purgeRepository();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getDatastoreInfo(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int checkDatastorePerformance(int secondsToRun, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeature(long libFeatID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native String getLastException();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getLastExceptionCode();

  /**
   * {@inheritDoc}
   */
  @Override
  public native void clearLastException();
}
