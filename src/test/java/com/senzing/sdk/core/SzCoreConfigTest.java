package com.senzing.sdk.core;

import javax.json.JsonObject;
import javax.json.JsonArray;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzConfigManager;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreConfigTest extends AbstractTest {

    private static final String CUSTOMERS_DATA_SOURCE = "CUSTOMERS";

    private static final String EMPLOYEES_DATA_SOURCE = "EMPLOYEES";

    private SzCoreEnvironment env = null;

    private String defaultConfig = null;

    private String modifiedConfig = null;

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();
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
    void testNullDefinitionConstruct() {
        this.performTest(() -> {
            try {
                new SzCoreConfig(this.env, null);

                fail("Unexpectedly constructed with null config definition");
            } catch (NullPointerException expected) {
                // expected

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }

    @Test
    void testNullEnvironmentConstruct() {
        this.performTest(() -> {
            try {                
                new SzCoreConfig(null, this.defaultConfig);

                fail("Unexpectedly constructed with null environment");

            } catch (NullPointerException expected) {
                // expected

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
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

    @Test
    void testCreateConfigFromTemplate() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                SzConfig config = configMgr.createConfig();

                assertNotNull(config, "SzConfig should not be null");
                
                String configJson = config.export();
                
                assertEquals(this.defaultConfig, configJson, "Unexpected configuration definition.");
 
            } catch (Exception e) {
                fail("Failed testCreateConfig test with exception", e);
            }
        });
    }

    @Test
    void testCreateConfigFromDefinition() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                SzConfig config = configMgr.createConfig(this.modifiedConfig);

                assertNotNull(config, "SzConfig should not be null");

                String configJson = config.export();
                
                assertEquals(this.modifiedConfig, configJson, "Unexpected configuration definition.");
                    
            } catch (Exception e) {
                fail("Failed testImportConfig test with exception", e);
            }
        });
    }

    @Test
    void testExportConfig() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                SzConfig config = configMgr.createConfig(this.modifiedConfig);

                // export the config
                String configJson = config.export();
                String string = config.toString();

                assertEquals(this.modifiedConfig, configJson, "Unexpected configuration definition.");
                assertEquals(this.modifiedConfig, string, "Unexpected toString() result");

                JsonObject jsonObj = parseJsonObject(configJson);
                
                assertTrue(jsonObj.containsKey("G2_CONFIG"), 
                            "Config JSON is missing G2_CONFIG property: " + configJson);

                JsonObject g2Config = jsonObj.getJsonObject("G2_CONFIG");

                assertTrue(g2Config.containsKey("CFG_DSRC"), 
                            "Config JSON is missing CFG_DSRC property: " + configJson);

                JsonArray cfgDsrc = g2Config.getJsonArray("CFG_DSRC");

                assertEquals(3, cfgDsrc.size(), "Data source array is wrong size");
                
                JsonObject customerDataSource = cfgDsrc.getJsonObject(2);
                String dsrcCode = customerDataSource.getString("DSRC_CODE");

                assertEquals(CUSTOMERS_DATA_SOURCE, dsrcCode, "Third data source is not as expected");
                
            } catch (Exception e) {
                fail("Failed testExportConfig test with exception", e);
            }
        });
    }

    @Test
    void testAddDataSource() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                SzConfig config = configMgr.createConfig();

                String result = config.addDataSource(EMPLOYEES_DATA_SOURCE);

                JsonObject resultObj = null;
                try {
                    resultObj = parseJsonObject(result);
                } catch (Exception e) {
                    fail("The addDataSource() result did not parse as JSON: " + result);
                }

                String resultId = getString(resultObj, "DSRC_ID");

                assertNotNull(resultId, "The DSRC_ID was missing or null in the result: " + result);
                    
                String configJson = config.export();

                JsonObject jsonObj = parseJsonObject(configJson);
                
                assertTrue(jsonObj.containsKey("G2_CONFIG"), 
                            "Config JSON is missing G2_CONFIG property: " + configJson);

                JsonObject g2Config = jsonObj.getJsonObject("G2_CONFIG");

                assertTrue(g2Config.containsKey("CFG_DSRC"), 
                            "Config JSON is missing CFG_DSRC property: " + configJson);

                JsonArray cfgDsrc = g2Config.getJsonArray("CFG_DSRC");

                assertEquals(3, cfgDsrc.size(), "Data source array is wrong size");
                
                JsonObject customerDataSource = cfgDsrc.getJsonObject(2);
                String dsrcCode = customerDataSource.getString("DSRC_CODE");

                assertEquals(EMPLOYEES_DATA_SOURCE, dsrcCode, "Third data source is not as expected");
                    
            } catch (Exception e) {
                fail("Failed testAddDataSource test with exception", e);
            }
        });
    }

    @Test
    void testDeleteDataSource() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                SzConfig config = configMgr.createConfig(this.modifiedConfig);

                config.deleteDataSource("CUSTOMERS");

                String configJson = config.export();

                JsonObject jsonObj = parseJsonObject(configJson);
                
                assertTrue(jsonObj.containsKey("G2_CONFIG"), 
                            "Config JSON is missing G2_CONFIG property: " + configJson);

                JsonObject g2Config = jsonObj.getJsonObject("G2_CONFIG");

                assertTrue(g2Config.containsKey("CFG_DSRC"), 
                            "Config JSON is missing CFG_DSRC property: " + configJson);

                JsonArray cfgDsrc = g2Config.getJsonArray("CFG_DSRC");

                assertEquals(2, cfgDsrc.size(), "Data source array is wrong size");
                
                JsonObject dataSource1 = cfgDsrc.getJsonObject(0);
                String dsrcCode1 = dataSource1.getString("DSRC_CODE");

                assertEquals("TEST", dsrcCode1, "First data source is not as expected");

                JsonObject dataSource2 = cfgDsrc.getJsonObject(1);
                String dsrcCode2 = dataSource2.getString("DSRC_CODE");

                assertEquals("SEARCH", dsrcCode2, "Second data source is not as expected");

            } catch (Exception e) {
                fail("Failed testDeleteDataSource test with exception", e);
            }
        });

    }

    @Test
    void testGetDataSources() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                SzConfig config = configMgr.createConfig(this.modifiedConfig);

                String dataSources = config.getDataSources();
                
                assertNotNull(dataSources, "Data sources result was null");

                JsonObject jsonObj = parseJsonObject(dataSources);
                
                assertTrue(jsonObj.containsKey("DATA_SOURCES"), 
                            "JSON is missing DATA_SOURCES property: " + dataSources);

                JsonArray jsonArray = jsonObj.getJsonArray("DATA_SOURCES");

                assertEquals(3, jsonArray.size(), "Data sources JSON array is wrong size.");
                            
                JsonObject dataSource1 = jsonArray.getJsonObject(0);
                String dsrcCode1 = dataSource1.getString("DSRC_CODE");

                assertEquals("TEST", dsrcCode1, "First data source is not as expected");

                JsonObject dataSource2 = jsonArray.getJsonObject(1);
                String dsrcCode2 = dataSource2.getString("DSRC_CODE");

                assertEquals("SEARCH", dsrcCode2, "Second data source is not as expected");

                JsonObject dataSource3 = jsonArray.getJsonObject(2);
                String dsrcCode3 = dataSource3.getString("DSRC_CODE");

                assertEquals(CUSTOMERS_DATA_SOURCE, dsrcCode3, "Third data source is not as expected");

            } catch (Exception e) {
                fail("Failed getDataSources test with exception", e);
            }
        });
    }
}
