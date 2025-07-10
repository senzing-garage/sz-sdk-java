/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Implements the {@link NativeProduct} interface to call the native
 * implementations of each function.
 */
class NativeProductJni implements NativeProduct
{
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
    public native int destroy();

    /**
     * {@inheritDoc}
     */
    @Override
    public native String getLicense();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public native String getVersion();

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
