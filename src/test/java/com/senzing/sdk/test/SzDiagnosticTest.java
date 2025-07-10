package com.senzing.sdk.test;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;
import javax.json.JsonArray;

import com.senzing.sdk.SzException;
import com.senzing.sdk.SzDiagnostic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlags.*;
import static org.junit.jupiter.params.provider.Arguments.*;

/**
 * Unit tests for {@link SzDiagnostic} implementations.
 */
 public interface SzDiagnosticTest extends SdkTest {
    /**
     * Provides the test data for the {@link SzDiagnosticTest}
     * interface.
     */
    public static class TestData {
        /**
         * The data source code for the test record.
         */
        private static final String TEST_DATA_SOURCE = "TEST";

        /**
         * The record ID for the test record.
         */
        private static final String TEST_RECORD_ID = "ABC123";

        /**
         * The flags to use for retrieving the entity for the test record.
         */
        private static final long FLAGS 
            = SZ_ENTITY_INCLUDE_ALL_FEATURES
            | SZ_ENTITY_INCLUDE_ENTITY_NAME
            | SZ_ENTITY_INCLUDE_RECORD_SUMMARY
            | SZ_ENTITY_INCLUDE_RECORD_TYPES
            | SZ_ENTITY_INCLUDE_RECORD_DATA
            | SZ_ENTITY_INCLUDE_RECORD_JSON_DATA
            | SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO
            | SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA
            | SZ_ENTITY_INCLUDE_RECORD_FEATURES
            | SZ_ENTITY_INCLUDE_INTERNAL_FEATURES
            | SZ_INCLUDE_MATCH_KEY_DETAILS
            | SZ_INCLUDE_FEATURE_SCORES;

        /**
         * The feature map to use for the feature testing.
         */
        private Map<Long, String> featureMap = new LinkedHashMap<>();

        /**
         * Default constructor.
         */
        public TestData() {
            // do nothing
        }

        /**
         * Does the setup for the test data using the specified
         * {@link TestDataLoader}.
         * 
         * @param dataLoader The {@link TestDataLoader} to use for setting
         *                   up the test data.
         */
        public void setup(TestDataLoader dataLoader) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("DATA_SOURCE", "TEST");
            job.add("RECORD_ID", "ABC123");
            job.add("NAME_FULL", "Joe Schmoe");
            job.add("EMAIL_ADDRESS", "joeschmoe@nowhere.com");
            job.add("PHONE_NUMBER", "702-555-1212");
            JsonObject jsonObj = job.build();
            String recordDefinition = jsonObj.toString();

            String entityJson = dataLoader.loadAndGetEntity(
                TEST_DATA_SOURCE, TEST_RECORD_ID, recordDefinition, FLAGS);

            JsonObject entity = parseJsonObject(entityJson);
            entity = entity.getJsonObject("RESOLVED_ENTITY");
            JsonObject features = entity.getJsonObject("FEATURES");
            for (String featureName : features.keySet()) {
                JsonArray featureArr = features.getJsonArray(featureName);
                for (JsonObject feature : featureArr.getValuesAs(JsonObject.class)) {
                    this.featureMap.put(getLong(feature, "LIB_FEAT_ID"),
                                        toJsonText(feature));
                }
            }
            this.featureMap = Collections.unmodifiableMap(this.featureMap);
        }

        /**
         * Gets the <b>unmodifiable</b> {@link Map} of {@link Long} feature ID's
         * to JSON-formatted text describing the feature as a JSON object.
         * 
         * @return The <b>unmodifiable</b> {@link Map} of {@link Long} feature ID's
         *         to JSON-formatted text describing the feature as a JSON object.
         * 
         */
        public Map<Long, String> getTestFeatureMap() {
            return this.featureMap;
        }
    }

    /**
     * Gets the {@link SzDiagnostic} to use with the tests.
     * 
     * @return The {@link SzDiagnostic} to use with the tests.
     * 
     * @throws SzException If a failure occurs.
     */
    SzDiagnostic getDiagnostic() throws SzException;

    /**
     * Gets the {@link TestData} to use with the tests.
     * 
     * @return The {@link TestData} to use with the tests.
     */
    TestData getTestData();

    /**
     * Tests the {@link SzDiagnostic#getRepositoryInfo()} functionality.
     * 
     */
    @Test
    @Order(100)
    default void testGetDatastoreInfo() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.getDiagnostic();

                String result = diagnostic.getRepositoryInfo();
                
                // parse the result as JSON and check that it parses
                parseJsonObject(result);

            } catch (Exception e) {
                fail("Failed testGetDatastoreInfo test with exception", e);
            }
        });        
    }

    /**
     * Tests the {@link SzDiagnostic#checkRepositoryPerformance(int)} functionality.
     */
    @Test
    @Order(200)
    default void testCheckDatastorePerformance() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.getDiagnostic();

                String result = diagnostic.checkRepositoryPerformance(5);
                
                // parse the result as JSON and check that it parses
                parseJsonObject(result);

            } catch (Exception e) {
                fail("Failed testCheckDatastorePerformance test with exception", e);
            }
        });
    }

    /**
     * Provides arguments for testing the {@link SzDiagnostic#getFeature(long)} 
     * functionality as a {@link List} of {@link Arguments} instances, each
     * including the following:
     * <ol>
     *      <li>A {@link Long} feature ID to use as the parameter.
     *      <li>The expected feature JSON as a result.
     * </ol>
     * 
     * @return The {@link List} of {@link Arguments} instances describing
     *         the test parameters.
     */
    default List<Arguments> getFeatureIdArguments() {
        Map<Long, String> featureMap = this.getTestData().getTestFeatureMap();

        List<Arguments> argumentsList = new LinkedList<>();
        featureMap.forEach((featureId, feature) -> {
            argumentsList.add(arguments(featureId, feature));
        });
        return argumentsList;
    }

    /**
     * Tests the {@link SzDiagnostic#getFeature(long)} functionality.
     * 
     * @param featureId The feature ID to test with.
     * 
     * @param expected The expected JSON result describing the feature.
     */
    @ParameterizedTest
    @MethodSource("getFeatureIdArguments")
    @Order(300)
    default void testGetFeature(long featureId, String expected) {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.getDiagnostic();

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

    /**
     * Tests the {@link SzDiagnostic#getFeature(long)} functionality when 
     * passing an invalid feature ID.
     */
    @Test
    @Order(400)
    default void testGetBadFeature()
    {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.getDiagnostic();

                diagnostic.getFeature(100000000L);

                fail("GetFeature() unexpected succeeded with bad "
                     + "feature ID");

            } catch (SzException e) {
                /// expected
            }
        });
    }

    /**
     * Tests the {@link SzDiagnostic#purgeRepository()} functionality.
     */
    @Test
    @Order(10000)
    default void testPurgeRepository() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.getDiagnostic();

                diagnostic.purgeRepository();

                // ensure the features are all gone
                Map<Long,String> featureMap = this.getTestData().getTestFeatureMap();
                featureMap.keySet().forEach(featureId -> {
                    try {
                        String feature = diagnostic.getFeature(featureId);

                        fail("Found feature for feature ID (" + featureId
                             + ") after purging: " + feature);

                    } catch (SzException expected) {
                        // expect to fail finding the feature
                    }
                });

            } catch (Exception e) {
                fail("Failed testPurgeRepository test with exception", e);
            }
        });

    }

}
