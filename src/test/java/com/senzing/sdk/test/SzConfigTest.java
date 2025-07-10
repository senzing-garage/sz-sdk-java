package com.senzing.sdk.test;

import javax.json.JsonObject;
import javax.json.JsonArray;

import org.junit.jupiter.api.Test;

import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.test.SzConfigTest.TestData.*;

/**
 * Provides the default test functionality for {@link SzConfig}.
 */
public interface SzConfigTest extends SdkTest {
    /**
     * The test data class for the {@link SzConfigTest} interface.
     */
    class TestData {
        /**
         * The data source code for the passengers data source.
         */
        public static final String CUSTOMERS = "CUSTOMERS";

        /**
         * The data source code for the employees data source.
         */
        public static final String EMPLOYEES = "EMPLOYEES";

        private String templateConfig = null;

        private String customersConfig = null;

        /**
         * Default constructor
         */
        public TestData() {

        }

        /**
         * Provides setup for the test data for the test.
         * 
         * @param configurator The {@link TestConfigurator} to use.
         */
        public void setup(TestConfigurator configurator) {
            this.templateConfig = configurator.createConfig();
            this.customersConfig = configurator.createConfig(CUSTOMERS);
        }

        /**
         * Gets the template config definition.
         * 
         * @return The template config definition.
         */
        public String getTemplateConfig() {
            return this.templateConfig;
        }

        /**
         * Gets the config definition with the {@link #CUSTOMERS}
         * data source.
         * 
         * @return The config definition with the {@link #CUSTOMERS}
         *         data source.
         */
        public String getCustomersConfig() {
            return this.customersConfig;
        }
    }

    /**
     * Gets the {@link SzConfigManager} to use.
     * 
     * @return The {@link SzConfigManager} to use.
     */
    SzConfigManager getConfigManager() throws SzException;

    /**
     * Gets the {@link TestData} for this instance.
     * 
     * @return The {@link TestData} for this instance.
     */
    TestData getTestData();

    /**
     * Tests the {@link SzConfigManager#createConfig()} functionality.
     */
    @Test
    default void testCreateConfigFromTemplate() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                SzConfig config = configMgr.createConfig();

                assertNotNull(config, "SzConfig should not be null");
                
                String configJson = config.export();
                
                assertEquals(this.getTestData().getTemplateConfig(),
                             configJson, "Unexpected configuration definition.");
 
            } catch (Exception e) {
                fail("Failed testCreateConfig test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfigManager#createConfig(String)} functionality.
     */
    @Test
    default void testCreateConfigFromDefinition() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                String customersConfig = this.getTestData().getCustomersConfig();

                SzConfig config = configMgr.createConfig(customersConfig);

                assertNotNull(config, "SzConfig should not be null");

                String configJson = config.export();
                
                assertEquals(customersConfig, configJson,
                             "Unexpected configuration definition.");
                    
            } catch (Exception e) {
                fail("Failed testImportConfig test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfig#export()} functionality.
     */
    @Test
    default void testExportConfig() {
        this.performTest(() -> {
            try {
                String customersConfig = this.getTestData().getCustomersConfig();

                SzConfigManager configMgr = this.getConfigManager();

                SzConfig config = configMgr.createConfig(customersConfig);

                // export the config
                String configJson = config.export();
                String string = config.toString();

                assertEquals(customersConfig, configJson, "Unexpected configuration definition.");
                assertEquals(customersConfig, string, "Unexpected toString() result");

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

                assertEquals(CUSTOMERS, dsrcCode, 
                             "Third data source is not as expected");
                
            } catch (Exception e) {
                fail("Failed testExportConfig test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfig#registerDataSource(String)} functionality.
     */
    @Test
    default void testRegisterDataSource() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                SzConfig config = configMgr.createConfig();

                String result = config.registerDataSource(EMPLOYEES);

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

                assertEquals(EMPLOYEES, dsrcCode, 
                             "Third data source is not as expected");
                    
            } catch (Exception e) {
                fail("Failed testAddDataSource test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfig#unregisterDataSource(String)} functionality.
     */
    @Test
    default void testUnregisterDataSource() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                String customersConfig = this.getTestData().getCustomersConfig();

                SzConfig config = configMgr.createConfig(customersConfig);

                config.unregisterDataSource(CUSTOMERS);

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

    /**
     * Tests the {@link SzConfig#getDataSourceRegistry()} functionality.
     */
    @Test
    default void testGetDataSourceRegistry() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                String customersConfig = this.getTestData().getCustomersConfig();

                SzConfig config = configMgr.createConfig(customersConfig);

                String dataSources = config.getDataSourceRegistry();
                
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

                assertEquals(CUSTOMERS, dsrcCode3, "Third data source is not as expected");

            } catch (Exception e) {
                fail("Failed getDataSources test with exception", e);
            }
        });
    }
}
