package com.senzing.sdk.core;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzEngine;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreEngineBasicsTest extends AbstractTest {

    private SzCoreEnvironment env = null;

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
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                assertNotNull(engine.getNativeApi(),
                      "Underlying native API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }

    private List<Arguments> getEncodeDataSourcesArguments() {
        List<Arguments> result = new LinkedList<>();

        result.add(Arguments.of(null, "{\"DATA_SOURCES\":[]}"));

        result.add(Arguments.of(Set.of(), "{\"DATA_SOURCES\":[]}"));

        result.add(Arguments.of(Set.of("CUSTOMERS"), "{\"DATA_SOURCES\":[\"CUSTOMERS\"]}"));

        result.add(Arguments.of(
            (new LinkedHashSet<String>(List.of("CUSTOMERS","EMPLOYEES"))),
            "{\"DATA_SOURCES\":[\"CUSTOMERS\",\"EMPLOYEES\"]}"));

        result.add(Arguments.of(
            (new LinkedHashSet<String>(List.of("CUSTOMERS","EMPLOYEES","WATCHLIST"))),
            "{\"DATA_SOURCES\":[\"CUSTOMERS\",\"EMPLOYEES\",\"WATCHLIST\"]}"));
    
        return result;
    }

    @ParameterizedTest
    @MethodSource("getEncodeDataSourcesArguments")
    void testEncodeDataSources(Set<String> sources, String expectedJson) {
        this.performTest(() -> {
            String actualJson = SzCoreEngine.encodeDataSources(sources);
                
            assertEquals(expectedJson, actualJson, "Data sources not properly encoded");
        });
    }

    private List<Arguments> getEncodeEntityIdsArguments() {
        List<Arguments> result = new LinkedList<>();

        result.add(Arguments.of(null, "{\"ENTITIES\":[]}"));

        result.add(Arguments.of(Set.of(), "{\"ENTITIES\":[]}"));

        result.add(Arguments.of(Set.of(10L), "{\"ENTITIES\":[{\"ENTITY_ID\":10}]}"));

        result.add(Arguments.of(
            (new LinkedHashSet<Long>(List.of(10L, 20L))),
            "{\"ENTITIES\":[{\"ENTITY_ID\":10},{\"ENTITY_ID\":20}]}"));

        result.add(Arguments.of(
            (new LinkedHashSet<Long>(List.of(10L, 20L, 15L))),
            "{\"ENTITIES\":[{\"ENTITY_ID\":10},{\"ENTITY_ID\":20},{\"ENTITY_ID\":15}]}"));
    
        return result;
    }

    @ParameterizedTest
    @MethodSource("getEncodeEntityIdsArguments")
    void testEncodeEntityIds(Set<Long> entityIds, String expectedJson) {
        this.performTest(() -> {
            String actualJson = SzCoreEngine.encodeEntityIds(entityIds);
                
            assertEquals(expectedJson, actualJson, "Entity ID's not properly encoded");
        });
    }

    private List<Arguments> getEncodeRecordKeysArguments() {
        List<Arguments> result = new LinkedList<>();

        result.add(Arguments.of(null, "{\"RECORDS\":[]}"));

        result.add(Arguments.of(Set.of(), "{\"RECORDS\":[]}"));

        result.add(Arguments.of(
            Set.of(SzRecordKey.of("CUSTOMERS","ABC123")),
            "{\"RECORDS\":[{\"DATA_SOURCE\":\"CUSTOMERS\",\"RECORD_ID\":\"ABC123\"}]}"));

        result.add(Arguments.of(
            (new LinkedHashSet<SzRecordKey>(
                List.of(SzRecordKey.of("CUSTOMERS","ABC123"),
                        SzRecordKey.of("EMPLOYEES","DEF456")))),
            "{\"RECORDS\":[{\"DATA_SOURCE\":\"CUSTOMERS\",\"RECORD_ID\":\"ABC123\"},"
            + "{\"DATA_SOURCE\":\"EMPLOYEES\",\"RECORD_ID\":\"DEF456\"}]}"));

            result.add(Arguments.of(
                (new LinkedHashSet<SzRecordKey>(
                    List.of(SzRecordKey.of("CUSTOMERS","ABC123"),
                            SzRecordKey.of("EMPLOYEES","DEF456"),
                            SzRecordKey.of("WATCHLIST","GHI789")))),
                "{\"RECORDS\":[{\"DATA_SOURCE\":\"CUSTOMERS\",\"RECORD_ID\":\"ABC123\"},"
                + "{\"DATA_SOURCE\":\"EMPLOYEES\",\"RECORD_ID\":\"DEF456\"},"
                + "{\"DATA_SOURCE\":\"WATCHLIST\",\"RECORD_ID\":\"GHI789\"}]}"));
        
        return result;
    }

    @ParameterizedTest
    @MethodSource("getEncodeRecordKeysArguments")
    void testEncodeRecordKeys(Set<SzRecordKey> recordKeys, String expectedJson) {
        this.performTest(() -> {
            String actualJson = SzCoreEngine.encodeRecordKeys(recordKeys);
                
            assertEquals(expectedJson, actualJson, "Record Keys not properly encoded");
        });
    }


    @Test
    void testPrimeEngine() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                engine.primeEngine();
            } catch (Exception e) {
                fail("Priming engine failed with an exception", e);
            }
        });
    }
}
