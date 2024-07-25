package com.senzing.sdk;

/**
 * Defines the Java interface to the Senzing product functions.  The Senzing
 * product functions provide information regarding the Senzing product
 * installation and user license.
 */
public interface SzProduct {
    /**
     * Returns the currently configured license details.
     *
     * @return The JSON document describing the license details.
     * 
     * @throws SzException If a failure occurs.
     */
    String getLicense() throws SzException;

    /**
     * Returns the currently installed version details.
     *
     * @return The JSON document of version details.
     *
     * @throws SzException If a failure occurs.
     */
    String getVersion() throws SzException;
}
