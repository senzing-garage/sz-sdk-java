package com.senzing.sdk.core;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzException;
import com.senzing.sdk.test.SzDiagnosticTest;
import com.senzing.sdk.test.TestDataLoader;
import com.senzing.sdk.SzDiagnostic;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
 @TestInstance(Lifecycle.PER_CLASS)
 @Execution(ExecutionMode.SAME_THREAD)
 @TestMethodOrder(OrderAnnotation.class)
 public class SzCoreDiagnosticTest 
    extends AbstractCoreTest 
    implements SzDiagnosticTest
{
    private SzCoreEnvironment env = null;

    private TestData testData = new TestData();

    @Override
    public SzDiagnostic getDiagnostic() throws SzException {
        return this.env.getDiagnostic();
    }

    @Override
    public TestData getTestData() {
        return this.testData;
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
    
    /**
     * Overridden to load test data and extract features.
     */
    protected void prepareRepository() {
        File repoDirectory = this.getRepositoryDirectory();
        
        TestDataLoader loader = new RepoMgrTestDataLoader(repoDirectory);

        this.testData.setup(loader);
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
    @Order(10)
    void testGetNativeApi() {
        this.performTest(() -> {
            try {
                SzCoreDiagnostic diagnostic 
                    = (SzCoreDiagnostic) this.env.getDiagnostic();

                assertNotNull(diagnostic.getNativeApi(),
                      "Underlying native API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }
}
