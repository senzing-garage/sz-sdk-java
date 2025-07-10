package com.senzing.sdk.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.senzing.sdk.SzProduct;
import com.senzing.sdk.SzException;

import static com.senzing.util.JsonUtilities.normalizeJsonText;
import static com.senzing.sdk.test.SdkTest.*;

/**
 * Provides unit tests for the {@link SzProduct} functionality.
 */
public interface SzProductTest extends SdkTest {
    /**
     * Gets the {@link SzProduct} to use for the tests.
     * 
     * @return The {@link SzProduct} to use for the tests.
     * 
     * @throws SzException If a failure occurs.
     */
    SzProduct getProduct() throws SzException;

    @Test
    default void testGetLicense() {
        this.performTest(() -> {
            try {
            SzProduct product = this.getProduct();

                String license = product.getLicense();

                Object jsonData = normalizeJsonText(license);

                validateJsonDataMap(
                    jsonData,
                    "customer", "contract", "issueDate", "licenseType",
                    "licenseLevel", "billing", "expireDate", "recordLimit");

            } catch (Exception e) {
                fail("Failed testGetLicense test with exception", e);
            }
        });
    }

    @Test
    default void testGetVersion() {
        this.performTest(() -> {
            try {
            SzProduct product = this.getProduct();

                String version = product.getVersion();

                Object jsonData = normalizeJsonText(version);

                validateJsonDataMap(
                    jsonData,
                    false,
                    "VERSION", "BUILD_NUMBER", "BUILD_DATE", "COMPATIBILITY_VERSION");
          
            } catch (Exception e) {
                fail("Failed testGetVersion test with exception", e);
            }
        });
    }
}
