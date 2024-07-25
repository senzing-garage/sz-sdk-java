package com.senzing.sdk.core;

import java.util.Set;
import java.util.TreeSet;
import javax.json.JsonObject;
import javax.json.JsonArray;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzReplaceConflictException;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreConfigManagerTest extends AbstractTest {
    private static final String CUSTOMERS_DATA_SOURCE = "CUSTOMERS";

    private SzCoreEnvironment env = null;

    private String defaultConfig = null;

    private String modifiedConfig = null;

    private long defaultConfigId = 0L;

    private long modifiedConfigId = 0L;

    private static final String DEFAULT_COMMENT = "Default";

    private static final String MODIFIED_COMMENT = "Modified";

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment(true);
        String settings = this.getRepoSettings();
        
        String instanceName = this.getClass().getSimpleName();

        NativeConfig nativeConfig = new NativeConfigJni();
        long configHandle = 0L;
        try {
            // initialize the native config
            int returnCode = nativeConfig.init(instanceName, settings, false);
            if (returnCode != 0) {
                throw new RuntimeException(nativeConfig.getLastException());
            }

            // create the default config
            Result<Long> result = new Result<>();
            returnCode = nativeConfig.create(result);
            if (returnCode != 0) {
                throw new RuntimeException(nativeConfig.getLastException());
            }

            // get the config handle
            configHandle = result.getValue();

            // export the default config JSON
            StringBuffer sb = new StringBuffer();
            returnCode = nativeConfig.save(configHandle, sb);
            if (returnCode != 0) {
                throw new RuntimeException(nativeConfig.getLastException());
            }

            // set the default config
            this.defaultConfig = sb.toString();

            // add the data source
            sb.delete(0, sb.length());
            returnCode = nativeConfig.addDataSource(configHandle, 
                "{\"DSRC_CODE\": \"" + CUSTOMERS_DATA_SOURCE + "\"}", sb);
            if (returnCode != 0) {
                throw new RuntimeException(nativeConfig.getLastException());
            }

            // export the modified config JSON
            sb.delete(0, sb.length());
            returnCode = nativeConfig.save(configHandle, sb);
            if (returnCode != 0) {
                throw new RuntimeException(nativeConfig.getLastException());
            }

            // set the modified config
            this.modifiedConfig = sb.toString();

            // close the config handle
            returnCode = nativeConfig.close(configHandle);
            configHandle = 0L;
            if (returnCode != 0) {
                throw new RuntimeException(nativeConfig.getLastException());
            }

        } finally {
            if (configHandle != 0L) nativeConfig.close(configHandle);
            configHandle = 0L;
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
    @Order(5)
    void testGetNativeApi() {
        this.performTest(() -> {
            try {
                SzCoreConfigManager configMgr = (SzCoreConfigManager) this.env.getConfigManager();

                assertNotNull(configMgr.getNativeApi(),
                      "Underlying native API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }

    @Test
    @Order(10)
    void testAddConfigDefault() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                this.defaultConfigId = configMgr.addConfig(this.defaultConfig, DEFAULT_COMMENT);
                
                assertNotEquals(0L, this.defaultConfigId, "Config ID is zero (0)");

            } catch (Exception e) {
                fail("Failed testAddConfigDefault test with exception", e);
            }
        });
    }

    @Test
    @Order(20)
    void testAddConfigModified() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                this.modifiedConfigId = configMgr.addConfig(this.modifiedConfig, MODIFIED_COMMENT);
                
                assertNotEquals(0L, this.modifiedConfigId, "Config ID is zero (0)");

            } catch (Exception e) {
                fail("Failed testAddConfigModified test with exception", e);
            }
        });
    }

    @Test
    @Order(30)
    void testGetConfigDefault() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();
                    
                String configDefinition = configMgr.getConfig(this.defaultConfigId);

                assertEquals(this.defaultConfig, configDefinition, 
                             "Configuration retrieved is not as expected");

            } catch (Exception e) {
                fail("Failed testGetConfigDefault test with exception", e);
            }
        });
    }

    @Test
    @Order(40)
    void testGetConfigModified() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();
                    
                String configDefinition = configMgr.getConfig(this.modifiedConfigId);

                assertEquals(this.modifiedConfig, configDefinition, 
                             "Configuration retrieved is not as expected");

            } catch (Exception e) {
                fail("Failed testGetConfigModified test with exception", e);
            }
        });
    }

    @Test
    @Order(50)
    void testGetConfigs() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();
                    
                String result = configMgr.getConfigs();
                
                JsonObject jsonObj = parseJsonObject(result);

                assertTrue(jsonObj.containsKey("CONFIGS"), "Did not find CONFIGS in result");

                JsonArray configs = jsonObj.getJsonArray("CONFIGS");
                
                assertEquals(2, configs.size(), "CONFIGS array not of expected size");

                Object normConfigs = normalizeJsonValue(configs);
                
                validateJsonDataMapArray(normConfigs, true, 
                    "CONFIG_ID", "SYS_CREATE_DT", "CONFIG_COMMENTS");

                long    configId1 = getLong(configs.getJsonObject(0), "CONFIG_ID");
                String  comments1 = getString(configs.getJsonObject(0), "CONFIG_COMMENTS");

                long    configId2 = getLong(configs.getJsonObject(1), "CONFIG_ID");
                String  comments2 = getString(configs.getJsonObject(1), "CONFIG_COMMENTS");

                Set<Long> configIds = new TreeSet<>();
                configIds.add(defaultConfigId);
                configIds.add(modifiedConfigId);
                Set<String> comments = new TreeSet<>();
                comments.add(DEFAULT_COMMENT);
                comments.add(MODIFIED_COMMENT);

                assertTrue(configIds.contains(configId1), "First config ID not as expected");
                assertTrue(comments.contains(comments1), "First config comment not as expected");
                
                assertTrue(configIds.contains(configId2), "Second config ID not as expected");
                assertTrue(comments.contains(comments2), "Second config comment not as expected");

            } catch (Exception e) {
                fail("Failed testGetConfigs test with exception", e);
            }
        });
    }

    @Test
    @Order(60)
    void testGetDefaultConfigIdInitial() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                long configId = configMgr.getDefaultConfigId();
                
                assertEquals(0L, configId, 
                "Initial default config ID is not zero (0)");

            } catch (Exception e) {
                fail("Failed testGetDefaultConfigIdInitial test with exception", e);
            }
        });
    }

    @Test
    @Order(70)
    void testSetDefaultConfigId() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                configMgr.setDefaultConfigId(this.defaultConfigId);
                
                long configId = configMgr.getDefaultConfigId();

                assertEquals(this.defaultConfigId, configId, 
                "Set default config ID is not as expected");

            } catch (Exception e) {
                fail("Failed testSetDefaultConfigId test with exception", e);
            }
        });
    }

    @Test
    @Order(80)
    void testReplaceDefaultConfigId() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                configMgr.replaceDefaultConfigId(this.defaultConfigId, this.modifiedConfigId);
                
                long configId = configMgr.getDefaultConfigId();

                assertEquals(this.modifiedConfigId, configId, 
                "Replaced default config ID is not as expected");

            } catch (Exception e) {
                fail("Failed testReplaceDefaultConfigId test with exception", e);
            }
        });

    }

    @Test
    @Order(90)
    void testNotReplaceDefaultConfigId() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                configMgr.replaceDefaultConfigId(this.defaultConfigId, this.modifiedConfigId);
                
                fail("Replaced default config ID when it should not have been possible");

            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testNotReplaceDefaultConfigId test with exception", e);
            }
        });

    }
}
