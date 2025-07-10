package com.senzing.sdk.core;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import javax.json.JsonObject;
import javax.json.JsonArray;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
import com.senzing.sdk.test.SzConfigManagerTest;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.test.SdkTest.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreConfigManagerTest 
    extends AbstractCoreTest 
    implements SzConfigManagerTest
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
        this.initializeTestEnvironment(true);

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
    @Order(50)
    void testGetConfigApi() {
        this.performTest(() -> {
            try {
                SzCoreConfigManager configMgr 
                    = (SzCoreConfigManager) this.env.getConfigManager();

                assertNotNull(configMgr.getConfigApi(),
                      "Underlying native config API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetConfigApi test with exception", e);
            }
        });
    }

    @Test
    @Order(250)
    void testGetConfigManagerApi() {
        this.performTest(() -> {
            try {
                SzCoreConfigManager configMgr 
                    = (SzCoreConfigManager) this.env.getConfigManager();

                assertNotNull(configMgr.getConfigManagerApi(),
                      "Underlying native config manager API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetConfigManagerApi test with exception", e);
            }
        });
    }

    public List<Arguments> configCommentParameters() {
        List<Arguments> result = new LinkedList<>();

        TestData testData = this.getTestData();

        result.add(Arguments.of(
            testData.getConfig(0),
            "Data Sources: [ ONLY DEFAULT ]"));

        result.add(Arguments.of(
            testData.getConfig(1), 
            "Data Sources: CUSTOMERS"));
        
        result.add(Arguments.of(
            testData.getConfig(2),
            "Data Sources: CUSTOMERS, EMPLOYEES"));
        
        result.add(Arguments.of(
            testData.getConfig(3),
            "Data Sources: CUSTOMERS, EMPLOYEES, WATCHLIST"));

        try {
            SzConfigManager configMgr = this.getConfigManager();
            SzConfig config = configMgr.createConfig();
            config.unregisterDataSource("TEST");
            result.add(Arguments.of(config.export(), "Data Sources: [ SOME DEFAULT (SEARCH) ]"));

            config.unregisterDataSource("SEARCH");
            result.add(Arguments.of(config.export(), "Data Sources: [ NONE ]"));

            config = configMgr.createConfig();

            config.unregisterDataSource("SEARCH");
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
    @Order(1350)
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
