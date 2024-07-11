/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Provides a base interface for Senzing native SDK's can have failures occur.
 */
interface NativeApi {
  /**
   * Returns a string about the last error the system received.
   * This is most commonly called after an API function returns an error code (non-zero or NULL)
   *
   * @return An error message
   */
  String getLastException();

  /**
   * Returns the exception code of the last error the system received.
   * This is most commonly called after an API function returns an error code (non-zero or NULL)
   *
   * @return An error code
   */
  int getLastExceptionCode();

  /**
   * Clears the information about the last error the system received.
   */
  void clearLastException();
}
