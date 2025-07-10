package com.senzing.sdk.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzException;
import com.senzing.sdk.test.SzConfigTest;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
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
    void testNullDefinitionConstruct() {
        this.performTest(() -> {
            try {
                new SzCoreConfig(this.env, null);

                fail("Unexpectedly constructed with null config definition");
            } catch (NullPointerException expected) {
                // expected

            } catch (Exception e) {
                fail("Failed testNullDefinitionConstruct test with exception", e);
            }
        });
    }

    @Test
    void testNullEnvironmentConstruct() {
        this.performTest(() -> {
            try {                
                String templateConfig = this.getTestData().getTemplateConfig();

                new SzCoreConfig(null, templateConfig);

                fail("Unexpectedly constructed with null environment");

            } catch (NullPointerException expected) {
                // expected

            } catch (Exception e) {
                fail("Failed testNullEnvironmentConstruct test with exception", e);
            }
        });
    }

    @Test
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
}
