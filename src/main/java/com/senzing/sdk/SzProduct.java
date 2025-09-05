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
 * 
 * @since 4.0.0
 */
public interface SzProduct {
    /**
     * Gets the details and entitlements of the applied product license.
     * 
     * <p>
     * <b>NOTE:</b> The details do not include the license key.
     * </p>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzProductDemo" region="getLicense"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzProductDemo-getLicense.txt"}
     * </p>
     * 
     * @return The JSON document describing the license details.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @since 4.0.0
     */
    String getLicense() throws SzException;

    /**
     * Gets the product version details.
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzProductDemo" region="getVersion"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzProductDemo-getVersion.txt"}
     * </p>
     * 
     * @return The JSON document of version details.
     *
     * @throws SzException If a failure occurs.
     * 
     * @since 4.0.0
     */
    String getVersion() throws SzException;
}
