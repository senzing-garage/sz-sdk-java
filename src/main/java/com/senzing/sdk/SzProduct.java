package com.senzing.sdk;

/**
 * <p>
 * Defines the Java interface to the Senzing product functions.  The Senzing
 * product functions provide information regarding the Senzing product
 * installation and user license.
 * </p>
 *
 * <p>
 * An {@link SzProduct} instance is typically obtained from an {@link SzEnvironment}
 * instance via the {@link SzEnvironment#getProduct()} method as follows:
 *
 * {@snippet class="com.senzing.sdk.SzProductDemo" region="getProduct"}
 * </p>
 */
public interface SzProduct {
    /**
     * Returns the currently configured license details.
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzProductDemo" region="getLicense"}
     * </p>
     * 
     * @return The JSON document describing the license details.
     * 
     * @throws SzException If a failure occurs.
     */
    String getLicense() throws SzException;

    /**
     * Returns the currently installed version details.
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzProductDemo" region="getVersion"}
     * </p>
     *
     * @return The JSON document of version details.
     *
     * @throws SzException If a failure occurs.
     */
    String getVersion() throws SzException;
}
