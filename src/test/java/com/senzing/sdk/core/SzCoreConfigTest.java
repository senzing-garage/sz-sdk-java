package com.senzing.sdk.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzException;
import com.senzing.sdk.test.SzConfigTest;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.sdk.core.SzCoreUtilities.DESTROYED_MESSAGE;
import static com.senzing.sdk.core.SzCoreUtilities.FAILURE_PREFIX;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreConfigTest 
    extends AbstractCoreTest 
    implements SzConfigTest
{
    private SzCoreEnvironment env = null;

    private TestData testData = new TestData();

    @Override
    public SzConfigManager getConfigManager() throws SzException {
        return this.env.getConfigManager();
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

        NativeConfig nativeConfig = new NativeConfigJni();

        int returnCode = nativeConfig.init(instanceName, settings, false);
        
        if (returnCode != 0) {
            throw new RuntimeException(nativeConfig.getLastException());
        }

        try {
            NativeTestConfigurator configurator
                = new NativeTestConfigurator(nativeConfig);

            this.testData.setup(configurator);
            
        } finally {
            nativeConfig.destroy();
        }

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
    @Order(1000)
    void testNullDefinitionConstruct() {
        this.performTest(() -> {
            try {
                SzCoreConfigManager configMgr 
                    = (SzCoreConfigManager) this.env.getConfigManager();
                
                new SzCoreConfig(this.env, configMgr.getConfigApi(), null);

                fail("Unexpectedly constructed with null config definition");
            } catch (NullPointerException expected) {
                // expected

            } catch (Exception e) {
                fail("Failed testNullDefinitionConstruct test with exception", e);
            }
        });
    }

    @Test
    @Order(1010)
    void testNullEnvironmentConstruct() {
        this.performTest(() -> {
            try {                
                String templateConfig = this.getTestData().getTemplateConfig();

                SzCoreConfigManager configMgr 
                    = (SzCoreConfigManager) this.env.getConfigManager();

                new SzCoreConfig(null, configMgr.getConfigApi(), templateConfig);

                fail("Unexpectedly constructed with null environment");

            } catch (NullPointerException expected) {
                // expected

            } catch (Exception e) {
                fail("Failed testNullEnvironmentConstruct test with exception", e);
            }
        });
    }

    @Test
    @Order(1020)
    void testNullNativeApiConstruct() {
        this.performTest(() -> {
            try {                
                String templateConfig = this.getTestData().getTemplateConfig();

                new SzCoreConfig(this.env, null, templateConfig);

                fail("Unexpectedly constructed with null native API");

            } catch (NullPointerException expected) {
                // expected

            } catch (Exception e) {
                fail("Failed testNullEnvironmentConstruct test with exception", e);
            }
        });
    }

    @Test
    @Order(1030)
    void testGetNativeApi() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();
                SzCoreConfig config = (SzCoreConfig) configMgr.createConfig();

                assertNotNull(config.getNativeApi(),
                      "Underlying native API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfig#export()} functionality.
     */
    @Test
    @Order(10000)
    public void testDestroyedExportStringConfig() {
        this.performTest(() -> {
            try { 
                SzConfigManager configMgr = this.getConfigManager();

                SzConfig config = configMgr.createConfig();

                // destroy the environment
                this.env.destroy();
                this.env = null;

                // export the config
                String destroyedMessage = config.toString();

                assertEquals(DESTROYED_MESSAGE, destroyedMessage, 
                             "Unexpected destroyed result from toString()");
                
            } catch (Exception e) {
                fail("Failed testDestroyedExportConfig test with exception", e);
            }
        });
    }

    private static class MockEnvironment extends SzCoreEnvironment {
        private static ThreadLocal<SzException> MOCK_FAILURE
            = new ThreadLocal<>();

        public MockEnvironment(String instanceName, String settings) {
            super(SzCoreEnvironment.newBuilder()
                    .settings(settings).instanceName(instanceName));
        }

        public void mock(SzException e) {
            MOCK_FAILURE.set(e);
        }

        @Override
        protected <T> T doExecute(Callable<T> task) throws Exception
        {
            SzException mockFailure = MOCK_FAILURE.get();
            MOCK_FAILURE.set(null);
            if (mockFailure != null) {
                throw mockFailure;
            }

            return super.doExecute(task);
        }
    }

    /**
     * Tests the {@link SzConfig#export()} functionality.
     */
    @Test
    @Order(10010)
    public void testFailedExportStringConfig() {
        this.performTest(() -> {
            String instanceName = this.getInstanceName();
            String settings = this.getRepoSettings();

            MockEnvironment mockEnv = new MockEnvironment(instanceName, settings);

            try { 
                SzConfigManager configMgr = mockEnv.getConfigManager();

                SzConfig config = configMgr.createConfig();

                // export the config
                mockEnv.mock(new SzException("mock"));
                String failureMessage = config.toString();

                assertEquals(FAILURE_PREFIX + "mock", failureMessage, 
                             "Unexpected failure result from toString()");
                
            } catch (Exception e) {
                fail("Failed testFailedExportConfig test with exception", e);
            } finally {
                mockEnv.destroy();
            }
        });
    }

}
