package com.senzing.sdk.core;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;

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

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;
import javax.json.JsonArray;

import com.senzing.sdk.SzDiagnostic;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.core.NativeEngine.*;
import static org.junit.jupiter.params.provider.Arguments.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
 @TestInstance(Lifecycle.PER_CLASS)
 @Execution(ExecutionMode.SAME_THREAD)
 @TestMethodOrder(OrderAnnotation.class)
 public class SzCoreDiagnosticTest extends AbstractTest {
    private static final String TEST_DATA_SOURCE = "TEST";
    private static final String TEST_RECORD_ID = "ABC123";

    private static final long FLAGS 
        = SZ_ENTITY_INCLUDE_ALL_FEATURES
        | SZ_ENTITY_INCLUDE_ENTITY_NAME
        | SZ_ENTITY_INCLUDE_RECORD_SUMMARY
        | SZ_ENTITY_INCLUDE_RECORD_TYPES
        | SZ_ENTITY_INCLUDE_RECORD_DATA
        | SZ_ENTITY_INCLUDE_RECORD_JSON_DATA
        | SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO
        | SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA
        | SZ_ENTITY_INCLUDE_RECORD_FEATURE_IDS
        | SZ_ENTITY_INCLUDE_INTERNAL_FEATURES
        | SZ_ENTITY_INCLUDE_FEATURE_ELEMENTS
        | SZ_INCLUDE_MATCH_KEY_DETAILS
        | SZ_INCLUDE_FEATURE_SCORES;

    private SzCoreEnvironment env = null;

    private Map<Long, String> featureMaps = new LinkedHashMap<>();

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();
        String settings = this.getRepoSettings();
        
        String instanceName = this.getClass().getSimpleName();

        NativeEngine nativeEngine = new NativeEngineJni();
        try {
            // initialize the native engine
            int returnCode = nativeEngine.init(instanceName, settings, false);
            if (returnCode != 0) {
                throw new RuntimeException(nativeEngine.getLastException());
            }

            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("DATA_SOURCE", "TEST");
            job.add("RECORD_ID", "ABC123");
            job.add("NAME_FULL", "Joe Schmoe");
            job.add("EMAIL_ADDRESS", "joeschmoe@nowhere.com");
            job.add("PHONE_NUMBER", "702-555-1212");
            JsonObject jsonObj = job.build();
            String recordDefinition = jsonObj.toString();

            // add a record
            returnCode = nativeEngine.addRecord(
                TEST_DATA_SOURCE, TEST_RECORD_ID, recordDefinition);

            if (returnCode != 0) {
                throw new RuntimeException(nativeEngine.getLastException());
            }
                
             // get the entity 
             StringBuffer sb = new StringBuffer();
             returnCode = nativeEngine.getEntityByRecordID(
                TEST_DATA_SOURCE, TEST_RECORD_ID, FLAGS, sb);

            // parse the entity and get the feature ID's
            JsonObject entity = parseJsonObject(sb.toString());
            entity = entity.getJsonObject("RESOLVED_ENTITY");
            JsonObject features = entity.getJsonObject("FEATURES");
            for (String featureName : features.keySet()) {
                JsonArray featureArr = features.getJsonArray(featureName);
                for (JsonObject feature : featureArr.getValuesAs(JsonObject.class)) {
                    this.featureMaps.put(getLong(feature, "LIB_FEAT_ID"),
                    toJsonText(feature));
                }
            }

        } finally {
            nativeEngine.destroy();
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
    @Order(10)
    void testGetNativeApi() {
        this.performTest(() -> {
            try {
                SzCoreDiagnostic diagnostic = (SzCoreDiagnostic) this.env.getDiagnostic();

                assertNotNull(diagnostic.getNativeApi(),
                      "Underlying native API is unexpectedly null");

            } catch (Exception e) {
                fail("Failed testGetNativeApi test with exception", e);
            }
        });
    }

    @Test
    @Order(10)
    void testGetDatastoreInfo() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.env.getDiagnostic();

                String result = diagnostic.getDatastoreInfo();
                
                // parse the result as JSON and check that it parses
                parseJsonObject(result);

            } catch (Exception e) {
                fail("Failed testGetDatastoreInfo test with exception", e);
            }
        });        
    }

    @Test
    @Order(20)
    void testCheckDatastorePerformance() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.env.getDiagnostic();

                String result = diagnostic.checkDatastorePerformance(5);
                
                // parse the result as JSON and check that it parses
                parseJsonObject(result);

            } catch (Exception e) {
                fail("Failed testCheckDatastorePerformance test with exception", e);
            }
        });
    }

    protected List<Arguments> getFeatureIdArguments() {
        List<Arguments> argumentsList = new LinkedList<>();
        this.featureMaps.forEach((featureId, feature) -> {
            argumentsList.add(arguments(featureId, feature));
        });
        return argumentsList;
    }

    @ParameterizedTest
    @MethodSource("getFeatureIdArguments")
    @Order(30)
    void testGetFeature(long featureId, String expected) {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.env.getDiagnostic();

                String actual = diagnostic.getFeature(featureId);

                JsonObject actualObj    = parseJsonObject(actual);
                JsonObject expectedObj  = parseJsonObject(expected);
                
                assertEquals(getLong(expectedObj, "LIB_FEAT_ID"),
                             getLong(actualObj, "LIB_FEAT_ID"),
                             "Feature ID does not match what is expected");
                
            } catch (Exception e) {
                fail("Failed testPurgeRepository test with exception", e);
            }
        });
    }

    @Test
    @Order(100)
    void testPurgeRepository() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.env.getDiagnostic();

                diagnostic.purgeRepository();

            } catch (Exception e) {
                fail("Failed testPurgeRepository test with exception", e);
            }
        });

    }

}
