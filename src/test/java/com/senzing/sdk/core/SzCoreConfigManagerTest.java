package com.senzing.sdk.core;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.TreeMap;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzException;
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

    private static final String EMPLOYEES_DATA_SOURCE = "EMPLOYEES";

    private static final String WATCHLIST_DATA_SOURCE = "WATCHLIST";

    private static final String PASSENGERS_DATA_SOURCE = "PASSENGERS";

    private SzCoreEnvironment env = null;

    private String defaultConfig = null;

    private String modifiedConfig1 = null;

    private String modifiedConfig2 = null;

    private String modifiedConfig3 = null;

    private String modifiedConfig4 = null;

    private long defaultConfigId = 0L;

    private long modifiedConfigId1 = 0L;

    private long modifiedConfigId2 = 0L;
    
    private long modifiedConfigId3 = 0L;

    private long modifiedConfigId4 = 0L;

    private static final String MODIFIED_COMMENT_1 = "Modified: CUSTOMERS";

    private static final String MODIFIED_COMMENT_3 = "Modified: CUSTOMERS, EMPLOYEES, WATCHLIST";

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

            // add the CUSTOMERS data source
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
            this.modifiedConfig1 = sb.toString();

            // add the EMPLOYEES data source
            sb.delete(0, sb.length());
            returnCode = nativeConfig.addDataSource(configHandle, 
                "{\"DSRC_CODE\": \"" + EMPLOYEES_DATA_SOURCE + "\"}", sb);
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
            this.modifiedConfig2 = sb.toString();

            // add the WATCHLIST data source
            sb.delete(0, sb.length());
            returnCode = nativeConfig.addDataSource(configHandle, 
                "{\"DSRC_CODE\": \"" + WATCHLIST_DATA_SOURCE + "\"}", sb);
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
            this.modifiedConfig3 = sb.toString();

            // add the PASSENGERS data source
            sb.delete(0, sb.length());
            returnCode = nativeConfig.addDataSource(configHandle, 
                "{\"DSRC_CODE\": \"" + PASSENGERS_DATA_SOURCE + "\"}", sb);
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
            this.modifiedConfig4 = sb.toString();

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
    void testGetConfigApi() {
        this.performTest(() -> {
            try {
                SzCoreConfigManager configMgr = (SzCoreConfigManager) this.env.getConfigManager();

                assertNotNull(configMgr.getConfigApi(),
                      "Underlying native config API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetConfigApi test with exception", e);
            }
        });
    }

    @Test
    @Order(10)
    void testRegisterConfigDefault() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                this.defaultConfigId = configMgr.registerConfig(this.defaultConfig);
                
                assertNotEquals(0L, this.defaultConfigId, "Config ID is zero (0)");

            } catch (Exception e) {
                fail("Failed testRegisterConfigDefault test with exception", e);
            }
        });
    }

    @Test
    @Order(20)
    void testRegisterConfigDefaultWithComment() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                this.modifiedConfigId1 = configMgr.registerConfig(this.modifiedConfig1, MODIFIED_COMMENT_1);
                
                assertNotEquals(0L, this.modifiedConfigId1, "Config ID is zero (0)");

            } catch (Exception e) {
                fail("Failed testRegisterConfigDefaultWithComment test with exception", e);
            }
        });
    }

    @Test
    @Order(25)
    void testGetConfigManagerApi() {
        this.performTest(() -> {
            try {
                SzCoreConfigManager configMgr = (SzCoreConfigManager) this.env.getConfigManager();

                assertNotNull(configMgr.getConfigManagerApi(),
                      "Underlying native config manager API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetConfigManagerApi test with exception", e);
            }
        });
    }

    @Test
    @Order(30)
    void testRegisterConfigModified() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                this.modifiedConfigId2 = configMgr.registerConfig(this.modifiedConfig2);
                
                assertNotEquals(0L, this.modifiedConfigId2, "Config ID is zero (0)");

            } catch (Exception e) {
                fail("Failed testRegisterConfigModified test with exception", e);
            }
        });
    }

    @Test
    @Order(40)
    void testRegisterConfigModifiedWithComment() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                this.modifiedConfigId3 = configMgr.registerConfig(this.modifiedConfig3, MODIFIED_COMMENT_3);
                
                assertNotEquals(0L, this.modifiedConfigId3, "Config ID is zero (0)");

            } catch (Exception e) {
                fail("Failed testRegisterConfigModifiedWithComment test with exception", e);
            }
        });
    }

    @Test
    @Order(45)
    void testRegisterConfigModifiedWithNullComment() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                this.modifiedConfigId4 = configMgr.registerConfig(this.modifiedConfig4, null);
                
                assertNotEquals(0L, this.modifiedConfigId4, "Config ID is zero (0)");

            } catch (Exception e) {
                fail("Failed testRegisterConfigModifiedWithNullComment test with exception", e);
            }
        });
    }

    private List<Arguments> createConfigFromConfigIdParameters() {
        List<Arguments> result = new ArrayList<>(5);
        result.add(Arguments.of(this.defaultConfigId, this.defaultConfig));
        result.add(Arguments.of(this.modifiedConfigId1, this.modifiedConfig1));
        result.add(Arguments.of(this.modifiedConfigId2, this.modifiedConfig2));
        result.add(Arguments.of(this.modifiedConfigId3, this.modifiedConfig3));
        result.add(Arguments.of(this.modifiedConfigId4, this.modifiedConfig4));
        return result;
    }

    @ParameterizedTest
    @MethodSource("createConfigFromConfigIdParameters")
    @Order(50)
    void testCreateConfigFromConfigId(long configId, String expectedDefinition) {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();
                    
                SzConfig config = configMgr.createConfig(configId);

                assertNotNull(config, "SzConfig should not be null");
                
                assertNotNull(((SzCoreConfig) config).getNativeApi(),
                      "Underlying native API is unexpectedly null");
                
                String configDefinition = config.export();

                assertEquals(expectedDefinition, configDefinition, 
                             "Configuration retrieved is not as expected: configId=[ " 
                             + configId + " ]");

            } catch (Exception e) {
                fail("Failed testGetConfigDefault test with exception", e);
            }
        });
    }

    @Test
    @Order(60)
    void testGetConfigs() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();
                    
                String result = configMgr.getConfigRegistry();
                
                JsonObject jsonObj = parseJsonObject(result);

                assertTrue(jsonObj.containsKey("CONFIGS"), "Did not find CONFIGS in result");

                JsonArray configs = jsonObj.getJsonArray("CONFIGS");
                
                assertEquals(5, configs.size(), "CONFIGS array not of expected size");

                Object normConfigs = normalizeJsonValue(configs);
                
                validateJsonDataMapArray(normConfigs, true, 
                    "CONFIG_ID", "SYS_CREATE_DT", "CONFIG_COMMENTS");

                Map<Long,String> expectedMap = new TreeMap<>();
                expectedMap.put(defaultConfigId, "Data Sources: [ ONLY DEFAULT ]");
                expectedMap.put(this.modifiedConfigId1, MODIFIED_COMMENT_1);
                expectedMap.put(this.modifiedConfigId2, "Data Sources: CUSTOMERS, EMPLOYEES");
                expectedMap.put(this.modifiedConfigId3, MODIFIED_COMMENT_3);
                expectedMap.put(this.modifiedConfigId4, "");
                
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

    @Test
    @Order(70)
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
    @Order(80)
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
    @Order(90)
    void testReplaceDefaultConfigId() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                configMgr.replaceDefaultConfigId(this.defaultConfigId, this.modifiedConfigId1);
                
                long configId = configMgr.getDefaultConfigId();

                assertEquals(this.modifiedConfigId1, configId, 
                "Replaced default config ID is not as expected");

            } catch (Exception e) {
                fail("Failed testReplaceDefaultConfigId test with exception", e);
            }
        });

    }

    @Test
    @Order(100)
    void testNotReplaceDefaultConfigId() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                configMgr.replaceDefaultConfigId(this.defaultConfigId, this.modifiedConfigId1);
                
                fail("Replaced default config ID when it should not have been possible");

            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testNotReplaceDefaultConfigId test with exception", e);
            }
        });
    }

    @Test
    @Order(110)
    void testSetDefaultConfig() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                long configId = configMgr.setDefaultConfig(this.modifiedConfig1);
                
                assertEquals(this.modifiedConfigId1, configId, "Default config ID is not as expected");

            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testSetDefaultConfig test with exception", e);
            }
        });

    }

    @Test
    @Order(120)
    void testSetDefaultConfigWithComment() {
        this.performTest(() -> {
            try {
                SzConfigManager configMgr = this.env.getConfigManager();

                long configId = configMgr.setDefaultConfig(this.modifiedConfig3, MODIFIED_COMMENT_3);
                
                assertEquals(this.modifiedConfigId3, configId, "Default config ID is not as expected");
                
            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testSetDefaultConfigWithComment test with exception", e);
            }
        });
    }

    List<Arguments> configCommentParameters() {
        List<Arguments> result = new LinkedList<>();

        result.add(Arguments.of(this.defaultConfig, "Data Sources: [ ONLY DEFAULT ]"));
        result.add(Arguments.of(this.modifiedConfig1, "Data Sources: CUSTOMERS"));
        result.add(Arguments.of(this.modifiedConfig2, "Data Sources: CUSTOMERS, EMPLOYEES"));
        result.add(Arguments.of(this.modifiedConfig3, "Data Sources: CUSTOMERS, EMPLOYEES, WATCHLIST"));

        try {
            SzConfigManager configMgr = this.env.getConfigManager();
            SzConfig config = configMgr.createConfig();
            config.deleteDataSource("TEST");
            result.add(Arguments.of(config.export(), "Data Sources: [ SOME DEFAULT (SEARCH) ]"));

            config.deleteDataSource("SEARCH");
            result.add(Arguments.of(config.export(), "Data Sources: [ NONE ]"));

            config = configMgr.createConfig();

            config.deleteDataSource("SEARCH");
            result.add(Arguments.of(config.export(), "Data Sources: [ SOME DEFAULT (TEST) ]"));
            
            String missingDataSources = """
                    {
                        "G2_CONFIG": {
                            "CFG_ATTR": [ ]
                        }
                    }
                    """;
            result.add(Arguments.of(missingDataSources, ""));

            String missingColon = """
                    {
                        "G2_CONFIG": {
                            "CFG_DSRC" [ 

                            ]
                        }
                    }
                    """;

            result.add(Arguments.of(missingColon, ""));

            String missingColonEnd = """
                    {
                        "G2_CONFIG": {
                            "CFG_DSRC"
                    """;

            result.add(Arguments.of(missingColonEnd, ""));

            String missingBracket = """
                    {
                        "G2_CONFIG": {
                            "CFG_DSRC"  :  
                                { }
                            ]
                        }
                    }
                    """;
                    
            result.add(Arguments.of(missingBracket, ""));

            String missingEndBracket = """
                    {
                        "G2_CONFIG": {
                            "CFG_DSRC"  :  [
                                { }
                        }
                    }
                    """;
                    
            result.add(Arguments.of(missingEndBracket, ""));

            String missingBracketEnd = """
                    {
                        "G2_CONFIG": {
                            "CFG_DSRC"  :
                    """;
                    
            result.add(Arguments.of(missingBracketEnd, ""));

            String missingSubcolon = """
                {
                    "G2_CONFIG": {
                        "CFG_DSRC" : [ 
                            {
                                "DSRC_CODE"  "TEST"
                            }
                        ]
                    }
                }
                """;

            result.add(Arguments.of(missingSubcolon, ""));

            String missingSubcolonEnd = """
                {
                    "G2_CONFIG": {
                        "CFG_DSRC" : [ 
                            {
                                "DSRC_CODE"
                        ]
                    }
                """;

            result.add(Arguments.of(missingSubcolonEnd, ""));

            String missingQuote = """
                {
                    "G2_CONFIG": {
                        "CFG_DSRC" : [ 
                            {
                                "DSRC_CODE" : TEST"
                            }
                        ]
                    }
                }
                """;

            result.add(Arguments.of(missingQuote, ""));

            String missingQuoteEnd = """
                {
                    "G2_CONFIG": {
                        "CFG_DSRC" : [ 
                            {
                                "DSRC_CODE" : 
                        ]
                    }
                """;

            result.add(Arguments.of(missingQuoteEnd, ""));

            String missingEndQuote = """
                {
                    "G2_CONFIG": {
                        "CFG_DSRC" : [ 
                            {
                                "DSRC_CODE" : "TEST
                            }
                        ]
                    }
                }
                """;

            result.add(Arguments.of(missingEndQuote, ""));

            String missingEndQuoteEnd = """
                {
                    "G2_CONFIG": {
                        "CFG_DSRC" : [ 
                            {
                                "DSRC_CODE" : "TEST
                        ]
                    }
                """;

            result.add(Arguments.of(missingEndQuoteEnd, ""));

            String minimalConfig = """
                {
                    "G2_CONFIG" : {
                        "CFG_DSRC" : [ 
                            {
                                "DSRC_CODE" : "TEST"
                            },
                            {
                                "DSRC_CODE" : "SEARCH"
                            },
                            {
                                "DSRC_CODE" : "CUSTOMERS"
                            }
                        ]
                    }
                }
                """;

            result.add(Arguments.of(minimalConfig, "Data Sources: CUSTOMERS"));


        } catch (SzException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @ParameterizedTest
    @MethodSource("configCommentParameters")
    void testCreateConfigComment(String configDefinition, String expected) {
        this.performTest(() -> {
            try {
                SzCoreConfigManager configMgr
                    = (SzCoreConfigManager) this.env.getConfigManager();

                String comment = configMgr.createConfigComment(configDefinition);

                assertEquals(expected, comment, "Comment not as expected: " + configDefinition);
                
            } catch (SzReplaceConflictException e) {
                // expected exception

            } catch (Exception e) {
                fail("Failed testCreateConfigComment test with exception", e);
            }
        });
    }
}
