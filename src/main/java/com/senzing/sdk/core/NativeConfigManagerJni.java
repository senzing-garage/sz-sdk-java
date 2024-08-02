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
class NativeConfigManagerJni implements NativeConfigManager {
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
    public native int addConfig(String          configStr,
                                String          configComments,
                                Result<Long>    configID);

    /**
     * {@inheritDoc}
     */
    @Override
    public native int getConfig(long configID, StringBuffer response);

    /**
     * {@inheritDoc}
     */
    @Override
    public native int getConfigList(StringBuffer response);

    /**
     * {@inheritDoc}
     */
    @Override
    public native int setDefaultConfigID(long configID);

    /**
     * {@inheritDoc}
     */
    @Override
    public native int getDefaultConfigID(Result<Long> configID);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public native int replaceDefaultConfigID(long oldConfigID, long newConfigID);

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
