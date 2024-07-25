package com.senzing.sdk.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzProduct;

import static com.senzing.util.JsonUtilities.normalizeJsonText;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreProductTest extends AbstractTest {
    private SzCoreEnvironment env = null;

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();
        String settings = this.getRepoSettings();
        
        String instanceName = this.getClass().getSimpleName();

        this.env = SzCoreEnvironment.newBuilder()
                                      .instanceName(instanceName)
                                      .settings(settings)
                                      .verboseLogging(false)
                                      .build();
    }

    @AfterAll
    public void teardownEnvironment() {
        try {
            if (this.env != null) {
                this.env.destroy();
                this.env = null;
            }
            this.teardownTestEnvironment();
        } finally {
            this.endTests();
        }
    }

    @Test
    void testGetNativeApi() {
        this.performTest(() -> {
            try {
                SzCoreProduct product = (SzCoreProduct) this.env.getProduct();

                assertNotNull(product.getNativeApi(),
                      "Underlying native API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }
    @Test
    void testGetLicense() {
        this.performTest(() -> {
            try {
            SzProduct product = this.env.getProduct();

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
    void testGetVersion() {
        this.performTest(() -> {
            try {
            SzProduct product = this.env.getProduct();

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
