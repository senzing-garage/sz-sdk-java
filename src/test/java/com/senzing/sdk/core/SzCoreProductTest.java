package com.senzing.sdk.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzProduct;
import com.senzing.sdk.SzException;
import com.senzing.sdk.test.SzProductTest;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreProductTest extends AbstractCoreTest implements SzProductTest {
    private SzCoreEnvironment env = null;

    /**
     * @inheritDoc
     */
    public SzProduct getProduct() throws SzException {
        return this.env.getProduct();
    }

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
                SzCoreProduct product = (SzCoreProduct) this.getProduct();

                assertNotNull(product.getNativeApi(),
                      "Underlying native API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }
}
