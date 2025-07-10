package com.senzing.sdk.test;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import javax.json.JsonObject;
import javax.json.JsonArray;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzReplaceConflictException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.test.SdkTest.*;
import static com.senzing.sdk.test.SzConfigManagerTest.TestData.*;

/**
 * Provides unit tests for {@link SzConfigManager} functionality.
 */
public interface SzConfigManagerTest extends SdkTest {
    /**
     * The test data class for the {@link SzConfigManagerTest}
     * interface.
     */
    class TestData {
        /**
         * The data source code for the passengers data source.
         */
        public static final String PASSENGERS = "PASSENGERS";

        /**
         * The data source code for the employees data source.
         */
        public static final String EMPLOYEES = "EMPLOYEES";

        /**
         * The data source code for the VIP's data source.
         */
        public static final String VIPS = "VIPS";

        /**
         * The data source code for the customers data source.
         */
        public static final String CUSTOMERS = "CUSTOMERS";

        /**
         * The data source code for the watchlist data source.
         */
        public static final String WATCHLIST = "WATCHLIST";

        /**
         * The maximum number of data sources in the test 
         * configuration definitions.
         */
        public static final int MAX_DATA_SOURCE_COUNT = 4;

        /**
         * The comment to use when registering the config with a 
         * single data source.
         */
        public static final String COMMENT_FOR_1_SOURCE
            = "Modified: CUSTOMERS";

        /**
         * The comment to use when registering the config with 
         * three data sources.
         */
        public static final String COMMENT_FOR_3_SOURCES 
            = "Modified: CUSTOMERS, EMPLOYEES, WATCHLIST";

        private Map<String, Integer> sourceCountByConfig = new LinkedHashMap<>();

        private Map<Integer, String> configsBySourceCount = new LinkedHashMap<>();

        private Map<Integer, Long> configIdsBySourceCount = new LinkedHashMap<>();

        private Map<String, Long> configIdByConfig = new LinkedHashMap<>();

        private Map<Long, String> configByConfigId = new LinkedHashMap<>();

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
            String config0 = configurator.createConfig();
            String config1 = configurator.createConfig(CUSTOMERS);
            String config2 = configurator.createConfig(CUSTOMERS, EMPLOYEES);
            String config3 = configurator.createConfig(
                CUSTOMERS, EMPLOYEES, WATCHLIST);
            String config4 = configurator.createConfig(
                CUSTOMERS, EMPLOYEES, WATCHLIST, PASSENGERS);

            this.configsBySourceCount.put(0, config0);
            this.configsBySourceCount.put(1, config1);
            this.configsBySourceCount.put(2, config2);
            this.configsBySourceCount.put(3, config3);
            this.configsBySourceCount.put(4, config4);

            this.configsBySourceCount.forEach((sourceCount, config) -> {
                this.sourceCountByConfig.put(config, sourceCount);
            });            
        }

        /**
         * Gets the the {@link String} configuration definition
         * having the specified number of data sources.
         * 
         * @param dataSourceCount The number of data sources.
         * 
         * @return The config definition having the specified 
         *         number of data sources, or <code>null</code> 
         *         if none.
         */
        public String getConfig(int dataSourceCount) {
            return this.configsBySourceCount.get(dataSourceCount);
        }

        /**
         * Gets the the {@link Long} configuration ID for the
         * configuration having the specified number of data 
         * sources.
         * 
         * @param dataSourceCount The number of data sources.
         * 
         * @return The config ID for the configuration having the
         *         specified number of data sources, or
         *         <code>null</code> if not known.
         */
        public Long getConfigId(int dataSourceCount) {
            return this.configIdsBySourceCount.get(dataSourceCount);
        }

        /**
         * Records the config ID for the specified config definition.
         * If the specified configuration definition is recognized 
         * then the configuration ID is also recorded for the data
         * source count.
         * 
         * @param config The configuration definition.
         * @param configId The config ID obtained from registering the
         *                 configuration definition.
         */
        public void recordConfigId(String config, long configId) {
            Integer sourceCount = this.sourceCountByConfig.get(config);
            if (sourceCount != null) {
                this.configIdsBySourceCount.put(sourceCount, configId);
            }
            this.configIdByConfig.put(config, configId);
            this.configByConfigId.put(configId, config);
        }

        /**
         * Gets the config ID for the specified configuration definition.
         * 
         * @param config The configuration definition for which the 
         *               config ID is desired.
         * 
         * @return The {@link Long} config ID for the configuration 
         *         definition, or <code>null</code> if the config is 
         *         not previously recorded.
         */
        public Long getConfigId(String config) {
            return this.configIdByConfig.get(config);
        }

        /**
         * Gets the config definition for the specified config ID.
         * 
         * @param configId The config ID for which the configuration 
         *                 definition is being requested.
         * 
         * @return The {@link String} config definition for the specified
         *         config ID, or <code>null</code> if the config ID is not
         *         recognized.
         */
        public String getConfigById(long configId) {
            return this.configByConfigId.get(configId);
        }
    }

    /**
     * Gets the {@link TestData} for this instance.
     * 
     * @return The {@link TestData} for this instance.
     */
    TestData getTestData();

    /**
     * Gets the {@link SzConfigManager} to use for the tests.
     * 
     * @return The {@link SzConfigManager} to use for the tests.
     * 
     * @throws SzException If a failure occurs.
     */
    SzConfigManager getConfigManager() throws SzException;

    /**
     * Tests registering the template config
     * with no comment specified.
     */
    @Test
    @Order(100)
    default void testRegisterTemplateConfig() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                TestData    testData    = this.getTestData();
                String      config      = testData.getConfig(0);
                
                long configId = configMgr.registerConfig(config);

                assertNotEquals(0L, configId, "Config ID is zero (0)");

                testData.recordConfigId(config, configId);

            } catch (Exception e) {
                fail("Failed testRegisterConfigDefault test with exception", e);
            }
        });
    }

    /**
     * Tests registering a single-data-source config with a
     * specified comment.
     */
    @Test
    @Order(200)
    default void testRegisterSingleSourceConfigWithComment() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                TestData    testData    = this.getTestData();
                String      config      = testData.getConfig(1);
                String      comment     = TestData.COMMENT_FOR_1_SOURCE;

                long configId = configMgr.registerConfig(config, comment);
                
                assertNotEquals(0L, configId, "Config ID is zero (0)");

                testData.recordConfigId(config, configId);

            } catch (Exception e) {
                fail("Failed testRegisterConfigDefaultWithComment test with exception", e);
            }
        });
    }

    /**
     * Test registering a two-data-source config
     * with no comment specified.
     */
    @Test
    @Order(300)
    default void testRegisterTwoSourceConfig() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                TestData    testData    = this.getTestData();
                String      config      = testData.getConfig(2);

                long configId = configMgr.registerConfig(config);

                assertNotEquals(0L, configId, "Config ID is zero (0)");

                testData.recordConfigId(config, configId);

            } catch (Exception e) {
                fail("Failed testRegisterConfigModified test with exception", e);
            }
        });
    }

    /**
     * Test registering a three-data-source config with
     * a specified comment.
     */
    @Test
    @Order(400)
    default void testRegisterConfigModifiedWithComment() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                TestData    testData    = this.getTestData();
                String      config      = testData.getConfig(3);
                String      comment     = TestData.COMMENT_FOR_3_SOURCES;

                long configId = configMgr.registerConfig(config, comment);
                
                assertNotEquals(0L, configId, "Config ID is zero (0)");

                testData.recordConfigId(config, configId);

            } catch (Exception e) {
                fail("Failed testRegisterConfigModifiedWithComment test with exception", e);
            }
        });
    }

    /**
     * Tests registering a four-data-source config with
     * <code>null<code> specified as the comment.
     */
    @Test
    @Order(500)
    default void testRegisterFourSourceConfigWithNullComment() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                TestData    testData    = this.getTestData();
                String      config      = testData.getConfig(4);

                long configId = configMgr.registerConfig(config, null);
                
                assertNotEquals(0L, configId, "Config ID is zero (0)");

                testData.recordConfigId(config, configId);

            } catch (Exception e) {
                fail("Failed testRegisterConfigModifiedWithNullComment test with exception", e);
            }
        });
    }

    /**
     * Creates the parameters for testing the funtionality for
     * creating an {@link SzConfig} from a configuration ID and
     * returns a {@link List} of {@link Arguments}.
     * 
     * <o>
     * The {@link Arguments} are as follows:
     * <ol>
     *  <li>A {@link String} configuration definition.
     * </ol>
     * 
     * @return The {@link List} of {@link Arguments} instances.
     */
    default List<Arguments> createConfigFromConfigIdParameters() {
        TestData testData = this.getTestData();

        List<Arguments> result = new ArrayList<>(5);

        int max = MAX_DATA_SOURCE_COUNT;

        for (int dataSourceCount = 0; dataSourceCount <= max; dataSourceCount++) {
            result.add(Arguments.of(testData.getConfig(dataSourceCount)));
        }
        return result;
    }

    /**
     * Tests the {@link SzConfigManager#createConfig(long)} functionality.
     * 
     * @param configDefinition The expected configuration definition.
     */
    @ParameterizedTest
    @MethodSource("createConfigFromConfigIdParameters")
    @Order(600)
    default void testCreateConfigFromConfigId(String configDefinition) {
        this.performTest(() -> {
            try {
                TestData testData = this.getTestData();

                long configId = testData.getConfigId(configDefinition);

                SzConfigManager configMgr = this.getConfigManager();

                SzConfig config = configMgr.createConfig(configId);

                assertNotNull(config, "SzConfig should not be null");
                
                String exportResult = config.export();

                assertEquals(configDefinition, exportResult, 
                             "Configuration retrieved is not as expected: configId=[ " 
                             + configId + " ]");

            } catch (Exception e) {
                fail("Failed testGetConfigDefault test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfigManager#getConfigRegistry()} functionality.
     * 
     */
    @Test
    @Order(700)
    default void testGetConfigRegistry() {
        this.performTest(() -> {
            try {
                TestData testData = this.getTestData();

                SzConfigManager configMgr = this.getConfigManager();
                    
                String result = configMgr.getConfigRegistry();
                
                JsonObject jsonObj = parseJsonObject(result);

                assertTrue(jsonObj.containsKey("CONFIGS"), "Did not find CONFIGS in result");

                JsonArray configs = jsonObj.getJsonArray("CONFIGS");
                
                assertEquals(5, configs.size(), "CONFIGS array not of expected size");

                Object normConfigs = normalizeJsonValue(configs);
                
                validateJsonDataMapArray(normConfigs, true, 
                    "CONFIG_ID", "SYS_CREATE_DT", "CONFIG_COMMENTS");

                Map<Long,String> expectedMap = new TreeMap<>();
                expectedMap.put(testData.getConfigId(0), "Data Sources: [ ONLY DEFAULT ]");
                expectedMap.put(testData.getConfigId(1), COMMENT_FOR_1_SOURCE);
                expectedMap.put(testData.getConfigId(2), "Data Sources: CUSTOMERS, EMPLOYEES");
                expectedMap.put(testData.getConfigId(3), COMMENT_FOR_3_SOURCES);
                expectedMap.put(testData.getConfigId(4), "");
                
                for (int index = 0; index < 4; index++) {
                    long actualConfigId = getLong(
                        configs.getJsonObject(index), "CONFIG_ID");
                    String actualComment = getString(
                        configs.getJsonObject(index), "CONFIG_COMMENTS");
                    
                    assertTrue(expectedMap.containsKey(actualConfigId), 
                        "Config ID (" + index + ") not as expected: actual=[ "
                        + actualConfigId + " ], expected=[ " + expectedMap.keySet()
                        + " ]");

                    String expectedComment = expectedMap.get(actualConfigId);

                    assertEquals(expectedComment, actualComment, 
                                 "Comments (" + index + ") not as expected.");
                }
                
            } catch (Exception e) {
                fail("Failed testGetConfigs test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfigManager#getDefaultConfigId()} functionality.
     */
    @Test
    @Order(800)
    default void testGetDefaultConfigIdInitial() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.getConfigManager();

                long configId = configMgr.getDefaultConfigId();
                
                assertEquals(0L, configId, 
                "Initial default config ID is not zero (0)");

            } catch (Exception e) {
                fail("Failed testGetDefaultConfigIdInitial test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfigManager#setDefaultConfigId(long)} functionality.
     */
    @Test
    @Order(900)
    default void testSetDefaultConfigId() {
        this.performTest(() -> {
            try {
                TestData testData = this.getTestData();

                SzConfigManager configMgr = this.getConfigManager();

                long newConfigId = testData.getConfigId(0);

                configMgr.setDefaultConfigId(newConfigId);
                
                long configId = configMgr.getDefaultConfigId();

                assertEquals(newConfigId, configId, 
                "Set default config ID is not as expected");

            } catch (Exception e) {
                fail("Failed testSetDefaultConfigId test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfigManager#replaceDefaultConfigId(long, long)}
     * functionality.
     */
    @Test
    @Order(1000)
    default void testReplaceDefaultConfigId() {
        this.performTest(() -> {
            try {
                TestData testData = this.getTestData();

                SzConfigManager configMgr = this.getConfigManager();

                long currentConfigId = testData.getConfigId(0);
                long newConfigId = testData.getConfigId(1);

                configMgr.replaceDefaultConfigId(currentConfigId, newConfigId);
                
                long configId = configMgr.getDefaultConfigId();

                assertEquals(newConfigId, configId, 
                    "Replaced default config ID is not as expected");

            } catch (Exception e) {
                fail("Failed testReplaceDefaultConfigId test with exception", e);
            }
        });

    }

    /**
     * Tests the {@link SzConfigManager#replaceDefaultConfigId(long, long)}
     * when the first argument is <b>not</b> the correct current default
     * config ID and ensures the proper exception is thrown.
     */
    @Test
    @Order(1100)
    default void testNotReplaceDefaultConfigId() {
        this.performTest(() -> {
            try {
                TestData testData = this.getTestData();

                SzConfigManager configMgr = this.getConfigManager();

                long currentConfigId = testData.getConfigId(0);
                long newConfigId = testData.getConfigId(1);

                configMgr.replaceDefaultConfigId(currentConfigId, newConfigId);
                
                fail("Replaced default config ID when it should not have been possible");

            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testNotReplaceDefaultConfigId test with exception", e);
            }
        });
    }

    /**
     * Tests the {@link SzConfigManager#setDefaultConfig(String)} functionality.
     */
    @Test
    @Order(1200)
    default void testSetDefaultConfig() {
        this.performTest(() -> {
            try {
                TestData testData = this.getTestData();

                SzConfigManager configMgr = this.getConfigManager();

                String config = testData.getConfig(1);

                long configId = configMgr.setDefaultConfig(config);
                
                assertEquals(testData.getConfigId(1), configId,
                             "Default config ID is not as expected");

            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testSetDefaultConfig test with exception", e);
            }
        });

    }

    /**
     * Tests the {@link SzConfigManager#setDefaultConfig(String, String)}
     * functionality.
     */
    @Test
    @Order(1300)
    default void testSetDefaultConfigWithComment() {
        this.performTest(() -> {
            try {
                TestData testData = this.getTestData();

                SzConfigManager configMgr = this.getConfigManager();

                String config = testData.getConfig(3);

                long configId = configMgr.setDefaultConfig(config, COMMENT_FOR_3_SOURCES);
                
                assertEquals(testData.getConfigId(3), configId,
                             "Default config ID is not as expected");
                
            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testSetDefaultConfigWithComment test with exception", e);
            }
        });
    }
}
