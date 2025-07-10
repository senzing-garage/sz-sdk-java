package com.senzing.sdk.test;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.JsonObject;
import javax.json.JsonArray;

import com.senzing.io.RecordReader.Format;
import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.CollectionUtilities.list;
import static com.senzing.util.CollectionUtilities.set;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.sdk.test.SdkTest.*;
import static com.senzing.io.RecordReader.Format;
import static com.senzing.io.RecordReader.Format.*;
import static com.senzing.sdk.test.SzEngineHowTest.TestData.*;

/**
 * Provides unit tests for {@link SzEngine} how & virtual
 * entity operations.
 */
public interface SzEngineHowTest extends SdkTest {
    /**
     * The test data for the {@link SzEngineHowTest} interface.
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
         * The data source for superhero records who have secret identities.
         */
        public static final String DUAL_IDENTITIES = "DUAL_IDENTITIES";
  
        /**
         * The fake data source code for an unknown data source.
         */
        public static final String UNKNOWN_DATA_SOURCE = "UNKNOWN";

        /**
         * The {@link SzRecordKey} for the passenger record 
         * with record ID <code>"ABC123"</code>.
         */
        public static final SzRecordKey ABC123
            = SzRecordKey.of(PASSENGERS,"ABC123");

        /**
         * The {@link SzRecordKey} for the passenger record 
         * with record ID <code>"DEF456"</code>.
         */
        public static final SzRecordKey DEF456
            = SzRecordKey.of(PASSENGERS, "DEF456");

        /**
         * The {@link SzRecordKey} for the passenger record 
         * with record ID <code>"GHI798"</code>.
         */
        public static final SzRecordKey GHI789
            = SzRecordKey.of(PASSENGERS, "GHI789");

        /**
         * The {@link SzRecordKey} for the passenger record 
         * with record ID <code>"JKL012"</code>.
         */
        public static final SzRecordKey JKL012
            = SzRecordKey.of(PASSENGERS, "JKL012");

        /**
         * The {@link SzRecordKey} for the employee record 
         * with record ID <code>"MNO345"</code>.
         */
        public static final SzRecordKey MNO345
            = SzRecordKey.of(EMPLOYEES, "MNO345");

        /**
         * The {@link SzRecordKey} for the employee record 
         * with record ID <code>"PQR678"</code>.
         */
        public static final SzRecordKey PQR678
            = SzRecordKey.of(EMPLOYEES, "PQR678");
        
        /**
         * The {@link SzRecordKey} for the VIP record 
         * with record ID <code>"STU901"</code>.
         */
        public static final SzRecordKey STU901
            = SzRecordKey.of(VIPS, "STU901");

        /**
         * The {@link SzRecordKey} for the VIP record 
         * with record ID <code>"STU901"</code>.
         */
        public static final SzRecordKey STU234
            = SzRecordKey.of(VIPS, "STU234");

        /**
         * The {@link SzRecordKey} for the VIP record 
         * with record ID <code>"XYZ234"</code>.
         */
        public static final SzRecordKey XYZ234
            = SzRecordKey.of(VIPS, "XYZ234");

        /**
         * The {@link SzRecordKey} for the VIP record 
         * with record ID <code>"XYZ234"</code>.
         */
        public static final SzRecordKey XYZ456
            = SzRecordKey.of(VIPS, "XYZ456");

        /**
         * The {@link SzRecordKey} for the employee record 
         * with record ID <code>"ZYX321"</code>.
         */
        public static final SzRecordKey ZYX321
            = SzRecordKey.of(EMPLOYEES, "ZYX321");

        /**
         * The {@link SzRecordKey} for the employee record 
         * with record ID <code>"CBA654"</code>.
         */
        public static final SzRecordKey CBA654
            = SzRecordKey.of(EMPLOYEES, "CBA654");
    
        /**
         * The {@link SzRecordKey} for the marriages record 
         * with record ID <code>"BCD123"</code>.
         */
        public static final SzRecordKey BCD123
            = SzRecordKey.of(DUAL_IDENTITIES, "BCD123");
        
        /**
         * The {@link SzRecordKey} for the marriages record 
         * with record ID <code>"CDE456"</code>.
         */
        public static final SzRecordKey CDE456
            = SzRecordKey.of(DUAL_IDENTITIES, "CDE456");

        /**
         * The {@link SzRecordKey} for the marriages record 
         * with record ID <code>"EFG789"</code>.
         */
        public static final SzRecordKey EFG789
            = SzRecordKey.of(DUAL_IDENTITIES, "EFG789");

        /**
         * The {@link SzRecordKey} for the marriages record 
         * with record ID <code>"FGH012"</code>.
         */
        public static final SzRecordKey FGH012
            = SzRecordKey.of(DUAL_IDENTITIES, "FGH012");
    
        /**
         * The set of {@link SzFlag} instances that deal with 
         * obtaining features so the test flags can be tested to 
         * see if they contain feature flags.
         */
        public static final Set<SzFlag> FEATURE_FLAGS
            = Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_ALL_FEATURES,
                SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES,
                SZ_ENTITY_INCLUDE_INTERNAL_FEATURES));
        
        /**
         * The set of {@link SzFlag} instances that deal with 
         * obtaining record data so the test flags can be tested
         * to see if they contain record flags.
         */
        public static final Set<SzFlag> RECORD_FLAGS
            = Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_FEATURES,
                SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS,
                SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS));
        
        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * virtual entity test functions.
         */
        public static final List<Set<SzFlag>> VIRTUAL_ENTITY_FLAG_SETS;
        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS);
            list.add(SZ_VIRTUAL_ENTITY_ALL_FLAGS);
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_FEATURES,
                SZ_ENTITY_INCLUDE_ALL_FEATURES,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_INTERNAL_FEATURES,
                SZ_INCLUDE_MATCH_KEY_DETAILS,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_FEATURES,
                SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES,
                SZ_ENTITY_INCLUDE_INTERNAL_FEATURES,
                SZ_INCLUDE_MATCH_KEY_DETAILS,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
            VIRTUAL_ENTITY_FLAG_SETS = Collections.unmodifiableList(list);
        }

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * how-entity function.
         */
        public static final List<Set<SzFlag>> HOW_FLAG_SETS;
        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_INCLUDE_MATCH_KEY_DETAILS)));
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_INCLUDE_FEATURE_SCORES)));
            list.add(SZ_HOW_ENTITY_DEFAULT_FLAGS);
            list.add(SZ_HOW_ALL_FLAGS);
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_INCLUDE_MATCH_KEY_DETAILS,
                SZ_INCLUDE_FEATURE_SCORES)));
            HOW_FLAG_SETS = Collections.unmodifiableList(list);
        }

        /**
         * The {@link List} of all declared {@link SzRecordKey} instances
         * for the test data.
         */        
        public static final List<SzRecordKey> RECORD_KEYS
            = List.of(ABC123,
                    DEF456,
                    GHI789,
                    JKL012,
                    MNO345,
                    PQR678,
                    STU901,
                    XYZ234,
                    STU234,
                    XYZ456,
                    ZYX321,
                    CBA654,
                    BCD123,
                    CDE456,
                    EFG789,
                    FGH012);

        /**
         * Prepares a {@link File} of passenger data for the {@link #PASSENGERS}
         * data source. 
         * 
         * @param format The {@link Format} for the file.
         * 
         * @return The {@link File} with the data to be loaded.
         */
        public static File preparePassengerFile(Format format) {
            String[] headers = {
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "PHONE_NUMBER", "ADDR_FULL",
                "DATE_OF_BIRTH"};
        
            String[][] passengers = {
                {ABC123.recordId(), "Joe", "Schmoe", "702-555-1212",
                    "101 Main Street, Las Vegas, NV 89101", "1981-01-12"},
                {DEF456.recordId(), "Joanne", "Smith", "212-555-1212",
                    "101 Fifth Ave, Las Vegas, NV 10018", "1983-05-15"},
                {GHI789.recordId(), "John", "Doe", "818-555-1313",
                    "100 Main Street, Los Angeles, CA 90012", "1978-10-17"},
                {JKL012.recordId(), "Jane", "Doe", "818-555-1212",
                    "100 Main Street, Los Angeles, CA 90012", "1979-02-05"}
            };
            return prepareDataFile(format, "test-passengers-", headers, passengers);
        }
        
        /**
         * Prepares a {@link File} of employee data for the {@link #EMPLOYEES}
         * data source. 
         * 
         * @param format The {@link Format} for the file.
         * 
         * @return The {@link File} with the data to be loaded.
         */
        public static File prepareEmployeeFile(Format format) {
            String[] headers = {
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "PHONE_NUMBER", "ADDR_FULL",
                "DATE_OF_BIRTH","MOTHERS_MAIDEN_NAME", "SSN_NUMBER"};
        
            String[][] employees = {
                {MNO345.recordId(), "Joseph", "Schmoe", "702-555-1212",
                    "101 Main Street, Las Vegas, NV 89101", "1981-01-12", "WILSON",
                    "145-45-9866"},
                {PQR678.recordId(), "Jo Anne", "Smith", "212-555-1212",
                    "101 Fifth Ave, Las Vegas, NV 10018", "1983-05-15", "JACOBS",
                    "213-98-9374"},
                {ZYX321.recordId(), "Mark", "Hightower", "563-927-2833",
                    "1882 Meadows Lane, Las Vegas, NV 89125", "1981-06-22", "JENKINS",
                    "873-22-4213"},
                {CBA654.recordId(), "Mark", "Hightower", "781-332-2824",
                    "2121 Roscoe Blvd, Los Angeles, CA 90232", "1980-09-09", "BROOKS",
                    "827-27-4829"}
            };
        
            return prepareDataFile(format, "test-employees-", headers, employees);
        }
        
        /**
         * Prepares a {@link File} of VIP data for the {@link #VIPS}
         * data source. 
         * 
         * @param format The {@link Format} for the file.
         * 
         * @return The {@link File} with the data to be loaded.
         */
        public static File prepareVipFile(Format format) {
            String[] headers = {
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "PHONE_NUMBER", "ADDR_FULL",
                "DATE_OF_BIRTH","MOTHERS_MAIDEN_NAME"};
        
            String[][] vips = {
                {STU901.recordId(), "Joe", "Schmoe", "702-555-1212",
                    "101 Main Street, Las Vegas, NV 89101", "1981-12-01", "WILSON"},
                {XYZ234.recordId(), "Joanne", "Smith", "212-555-1212",
                    "101 5th Avenue, Las Vegas, NV 10018", "1983-05-15", "JACOBS"},
                {STU234.recordId(), "John", "Doe", "818-555-1313",
                    "100 Main Street Ste. A, Los Angeles, CA 90012", "1978-10-17",
                    "WILLIAMS" },
                {XYZ456.recordId(), "Jane", "Doe", "818-555-1212",
                    "100 Main Street Suite A, Los Angeles, CA 90012", "1979-02-05",
                    "JENKINS" }
            };
        
            return prepareDataFile(format, "test-vips-", headers, vips);
        }
        
        /**
         * Prepares a {@link File} of marriage data for the
         * {@link #DUAL_IDENTITIES} data source. 
         * 
         * @param format The {@link Format} for the file.
         * 
         * @return The {@link File} with the data to be loaded.
         */
        private File prepareDualIdentitiesFile(Format format) {
            String[] headers = {
                "RECORD_ID", "NAME_FULL", "PHONE_NUMBER", "ADDR_FULL",
                "DATE_OF_BIRTH", "GENDER" };
        
            String[][] spouses = {
                {BCD123.recordId(), "Bruce Wayne", "201-765-3451",
                    "101 Wayne Court; Gotham City, NJ 07017", "1974-06-05", "M" },
                {CDE456.recordId(), "Jack Napier", "201-875-2314",
                    "101 Falconi Boulevard; Gotham City, NJ 07017", "1965-05-14", "M" },
                {EFG789.recordId(), "Batman", "201-782-3214",
                    "Batcave; Gotham City, NJ 07020", "", "M" },
                {FGH012.recordId(), "Joker", "201-832-2321",
                    "101 Arkham Road; Gotham City, NJ 07018", "1965-05-14", "M" }
            };
        
            return prepareDataFile(format, "test-marriages-", headers, spouses);
        }

        private SzEntityLookup entityLookup = null;

        /**
         * Default constructor.
         */
        public TestData() {
            // do nothing
        }

        /**
         * Loads the data with the specified {@link TestDataLoader}.
         * 
         * @param loader The {@link TestDataLoader} to use.
         */
        public void loadData(TestDataLoader loader) {
            loader.configureDataSources(PASSENGERS, EMPLOYEES, VIPS, DUAL_IDENTITIES);
            Map<String, File> fileMap = new LinkedHashMap<>();
            
            fileMap.put(PASSENGERS, preparePassengerFile(CSV));
            fileMap.put(EMPLOYEES, prepareEmployeeFile(JSON));
            fileMap.put(VIPS, prepareVipFile(JSON_LINES));
            fileMap.put(DUAL_IDENTITIES, prepareDualIdentitiesFile(JSON_LINES));

            fileMap.values().forEach(f -> {
                f.deleteOnExit();
            });

            Map<SzRecordKey,String> recordMap = new LinkedHashMap<>();

            fileMap.forEach((dataSource, file) -> {
                recordMap.putAll(loader.loadRecords(dataSource, file));
            });

            // get the mapping of keys to entity ID's
            this.entityLookup = loader.getEntityLookup(recordMap.keySet());
        }

        /**
         * Returns the {@link SzEntityLookup} for looking up entity ID's
         * by {@link SzRecordKey} and/or {@link SzRecordKey}'s by entity ID.
         * 
         * @return The {@link SzEntityLookup} for looking up entity ID's
         *         by {@link SzRecordKey} and/or {@link SzRecordKey}'s by
         *         entity ID.
         */
        public SzEntityLookup getEntityLookup() {
            return this.entityLookup;
        }
    }

    /**
     * Gets the {@link TestData} for this instance.
     * 
     * @return The {@link TestData} for this instance.
     */
    TestData getTestData();

    /**
     * Gets the {@link SzEngine} to use for this instance.
     * 
     * @return The {@link SzEngine} to use for this instance.
     * 
     * @throws SzException If a failure occurs.
     */
    SzEngine getEngine() throws SzException;
    
    /**
     * Helper function for building parameters that merge Bruce Wayne and
     * Batman into a virtual entity.  The returned {@Link List} contains
     * the following:
     * <ol>
     *  <li>The {@link Set} of {@link SzRecordKey} instances for the virtual entity.
     *  <li>A <code>null</code> placeholder for the flags.
     *  <li>The expected record count.
     *  <li>The {@link Map} of expected feature counts by feature type.
     *  <li>The {@link Map} of primary feature values to be validated.
     *  <li>The placeholder for any expected exception.
     * </ol>
     * 
     * @return The {@link List} of templated parameters.
     */
    private List<?> batmanVirtualEntityArgs() {
        Map<String, Set<String>> primaryFeatureValues = new LinkedHashMap<>();
        primaryFeatureValues.put("NAME", set("Bruce Wayne", "Batman"));

        final int expectedRecordCount = 2;

        Map<String, Integer> expectedFeatureCounts = new LinkedHashMap<>();
        expectedFeatureCounts.put("NAME", 2);
        expectedFeatureCounts.put("DOB", 1);
        expectedFeatureCounts.put("ADDRESS", 2);
        expectedFeatureCounts.put("PHONE", 2);

        return list(set(BCD123, EFG789),
                    null,                   // flags
                    expectedRecordCount,
                    expectedFeatureCounts,
                    primaryFeatureValues,
                    null);                  // expected exception
    }

    /**
     * Helper function for building parameters that merge Jack Napier and
     * Joker into a virtual entity.  The returned {@Link List} contains
     * the following:
     * <ol>
     *  <li>The {@link Set} of {@link SzRecordKey} instances for the virtual entity.
     *  <li>A <code>null</code> placeholder for the flags.
     *  <li>The expected record count.
     *  <li>The {@link Map} of expected feature counts by feature type.
     *  <li>The {@link Map} of primary feature values to be validated.
     *  <li>The placeholder for any expected exception.
     * </ol>
     * 
     * @return The {@link List} of templated parameters.
     */
    private List<?> jokerVirtualEntityArgs() {
        Map<String, Set<String>> primaryFeatureValues = new LinkedHashMap<>();
        primaryFeatureValues.put("NAME", set("Jack Napier", "Joker"));

        final int expectedRecordCount = 2;

        Map<String, Integer> expectedFeatureCounts = new LinkedHashMap<>();
        expectedFeatureCounts.put("NAME", 2);
        expectedFeatureCounts.put("DOB", 1);
        expectedFeatureCounts.put("ADDRESS", 2);
        expectedFeatureCounts.put("PHONE", 2);

        return list(set(CDE456, FGH012),
                    null,
                    expectedRecordCount,
                    expectedFeatureCounts,
                    primaryFeatureValues,
                    null);
    }

    /**
     * Gets a {@link List} of {@link Arguments} that are the parameters for
     * testing the {@link SzEngine#getVirtualEntity(Set, Set)} functionality.
     * The returned {@link Arguments} contain the following:
     * <ol>
     *  <li>The {@link Set} of {@link SzRecordKey} instances for the virtual entity.
     *  <li>The {@link Set} of {@link SzFlag} instances to use as flags.
     *  <li>The expected record count.
     *  <li>The {@link Map} of expected feature counts by feature type.
     *  <li>The {@link Map} of primary feature values to be validated.
     *  <li>The {@link Class} for the expected exception type, or <code>null</code>
     *      if no exception is expected.
     * </ol>
     * 
     * @return The {@link List} of {@link Arguments} for the function test
     *         parameters.
     */
    default List<Arguments> getVirtualEntityParameters() {
        List<Arguments> result = new LinkedList<>();

        List<List<?>> templateArgs = list(
            batmanVirtualEntityArgs(), jokerVirtualEntityArgs());

        for (List<?> args : templateArgs) {
            for (Set<SzFlag> flags : VIRTUAL_ENTITY_FLAG_SETS) {
                Object[] argsArray = args.toArray();
                argsArray[1] = flags;
                result.add(Arguments.of(argsArray));
            }
        }

        result.add(Arguments.of(
            set(SzRecordKey.of(UNKNOWN_DATA_SOURCE, "ABC123"), CDE456),
                SZ_HOW_ENTITY_DEFAULT_FLAGS,
                0,
                null,
                null,
                SzUnknownDataSourceException.class));

        result.add(Arguments.of(
            set(SzRecordKey.of(DUAL_IDENTITIES, "XXX000"), CDE456),
                SZ_HOW_ENTITY_DEFAULT_FLAGS,
                0,
                null,
                null,
                SzNotFoundException.class));
            
        return result;
    }

    /**
     * Tests the {@link SzEngine#getVirtualEntity(Set, Set)} functionality
     * using the specified parameters.
     * 
     * @param recordKeys The {@link Set} of {@link SzRecordKey} instances
     *                   for getting the virtual entity.
     * @param flags The {@link Set} of {@link SzFlag} instances to use in
     *              getting the virtual entity.
     * @param expectedRecordCount The expected number of records in the 
     *                            resultant virtual entity.
     * @param expectedFeatureCounts The {@link Map} of {@link String} feature
     *                              type keys to {@link Integer} feature counts
     *                              that are expected, or <code>null</code> if
     *                              feature counts are not being validated.
     * @param primaryFeatureValues The {@link Map} of {@link String} feature type
     *                             keys to {@link Set} values containing
     *                             {@link String} primary feature values to validate,
     *                             or <code>null</code> if not validating these.
     * @param exceptionType The {@link Class} of the expected exception type, or
     *                      <code>null</code> if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getVirtualEntityParameters")
    default void testGetVirtualEntity(Set<SzRecordKey>          recordKeys,
                                      Set<SzFlag>               flags,
                                      Integer                   expectedRecordCount,
                                      Map<String,Integer>       expectedFeatureCounts,
                                      Map<String,Set<String>>   primaryFeatureValues,
                                      Class<?>                  exceptionType)
    {
        String testData = "recordKeys=[ " + recordKeys + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedRecordCount=[ "
            + expectedRecordCount + " ], expectedFeatureCounts=[ "
            + expectedFeatureCounts + " ], primaryFeatureValues=[ "
            + primaryFeatureValues + " ], expectedException=[ "
            + exceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.getVirtualEntity(recordKeys, flags);

                if (exceptionType != null) {
                    fail("Unexpectedly succeeded getVirtualEntity() call: "
                         + testData);
                }

                validateVirtualEntity(result,
                                      testData,
                                      recordKeys,
                                      flags,
                                      expectedRecordCount,
                                      expectedFeatureCounts,
                                      primaryFeatureValues);

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (exceptionType == null) {
                    fail("Unexpectedly failed getVirtualEntity(): "
                         + testData + ", " + description, e);

                } else if (exceptionType != e.getClass()) {
                    assertInstanceOf(
                        exceptionType, e, 
                        "whyEntities() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }


    /**
     * Validates a virtual entity.
     * 
     * @param result The virtual entity JSON result.
     * @param testData The description of the test data to include in any
     *                 generated failure message.
     * @param recordKeys The {@link Set} of {@link SzRecordKey} instances
     *                   to use for the virtual entity.
     * @param flags The {@link Set} of {@link SzFlag} instanes to use to get
     *              the virtual entity.
     * @param expectedRecordCount The expected record count for the virtual 
     *                            entity.
     * @param expectedFeatureCounts The {@link Map} of {@link String} feature
     *                              type keys to {@link Integer} feature counts
     *                              that are expected, or <code>null</code> if
     *                              feature counts are not being validated.
     * @param primaryFeatureValues The {@link Map} of {@link String} feature type
     *                             keys to {@link Set} values containing
     *                             {@link String} primary feature values to validate,
     *                             or <code>null</code> if not validating these.
     */
    default void validateVirtualEntity(String                   result,
                                       String                   testData,
                                       Set<SzRecordKey>         recordKeys,
                                       Set<SzFlag>              flags,
                                       Integer                  expectedRecordCount,
                                       Map<String,Integer>      expectedFeatureCounts,
                                       Map<String,Set<String>>  primaryFeatureValues)
    {
        JsonObject jsonObject = null;
        try {
            jsonObject = parseJsonObject(result);
        } catch (Exception e) {
            fail("Virtual entity result did not parse as JSON: "
                 + testData, e);
        }

        JsonObject entity = getJsonObject(jsonObject, "RESOLVED_ENTITY");
        assertNotNull(entity, "The RESOLVED_ENTITY property is missing or null: " 
                      + testData + ", result=[ " + result + " ]");
        
        Long entityId = getLong(entity, "ENTITY_ID");
        assertNotNull(entityId, "The ENTITY_ID property is missing or null: " 
                      + testData + ", result=[ " + result + " ]");

        String name = getString(entity, "ENTITY_NAME");
        if (flags != null && flags.contains(SZ_ENTITY_INCLUDE_ENTITY_NAME)) {
            assertNotNull(name, "The ENTITY_NAME property is missing or null: "
                          + testData + ", entity=[ " + entity + " ]");
        } else {
            assertNull(name, 
                       "The ENTITY_NAME property was provided despite flags: "
                       + testData + " ], name=[ " + name + " ]");
        }

        JsonObject features = getJsonObject(entity, "FEATURES");
        if (SzFlag.intersects(flags, FEATURE_FLAGS)) {
            assertNotNull(features, "The FEATURES property is missing or null: "
                          + testData + ", entity=[ " + entity + " ]");

            Map<String,Set<String>> featuresMap = new LinkedHashMap<>();
            features.forEach((key,values) -> {
                JsonArray valuesArray = values.asJsonArray();
                Set<String> valueSet = featuresMap.get(key);
                if (valueSet == null) {
                    valueSet = new LinkedHashSet<>();
                    featuresMap.put(key, valueSet);
                }
                for (JsonObject feature : valuesArray.getValuesAs(JsonObject.class)) {
                    String primaryValue = getString(feature, "FEAT_DESC");
                    valueSet.add(primaryValue);
                }
            });

            // verify the feature counts
            if (expectedFeatureCounts != null) {
                expectedFeatureCounts.forEach((key, count) -> {
                    Set<String> actuals = featuresMap.get(key);
                    assertNotNull(actuals,
                        "No features found for expected type (" + key + "): "
                        + testData);
                    assertEquals(count, actuals.size(),
                        "Unexpected number of values for feature (" + key 
                        + "): values=[ " + actuals + " ], " + testData);
                });
            }
            // verify the features
            if (primaryFeatureValues != null) {
                primaryFeatureValues.forEach((key, values) -> {
                    Set<String> actuals = featuresMap.get(key);
                    assertNotNull(actuals,
                        "No primary feature values found for expected "
                        + "type (" + key + "): " + testData);
                    assertEquals(values, actuals,
                        "Unexpected primary values for feature (" + key 
                        + "): " + testData);
                });
            }
        } else {
            assertNull(features, 
                       "The FEATURES property was provided despite flags: "
                       + testData + " ], features=[ " + features + " ]");
        }
        
        JsonArray records = getJsonArray(entity, "RECORDS");
        if (SzFlag.intersects(flags, RECORD_FLAGS)) {
            assertNotNull(records, "The RECORDS property is missing or null: "
                          + testData + ", entity=[ " + entity + " ]");

            assertEquals(expectedRecordCount, records.size(),
                "Unexpected number of records: " + testData
                + " ], records=[ " + records + " ]");
        
        } else {
            assertNull(records, 
                        "The RECORDS property was provided despite flags: "
                        + testData + " ], records=[ " + records + " ]");
        }
        
    }

    /**
     * Provides {@link List} of {@link Arguments} that are the parameters for
     * testing the {@link SzEngine#howEntity(long, Set)} funtionality.  The 
     * {@link Arguments} contain the following:
     * <ol>
     *  <li>The {@link SzRecordKey} identifying the record for the entity.
     *  <li>The {@link Long} entity ID identifying the entity.
     *  <li>The {@link Set} of {@link SzFlag} instances to use.
     *  <li>The {@link Integer} expected record count.
     *  <li>The {@link Set} of {@link SzRecordKey} instances identifying the
     *      expected records.
     *  <li>The {@link Class} for the expected exception type, or
     *      <code>null</code> if no exception is expected.
     * </ol>
     * @return
     */
    default List<Arguments> getHowEntityParameters() {
        List<Arguments> result = new LinkedList<>();

        SzEntityLookup lookup = this.getTestData().getEntityLookup();

        @SuppressWarnings("unchecked")
        List<Set<SzRecordKey>> recordSets = list(
            set(ABC123, MNO345, STU901),  // Joe Schmoe
            set(DEF456, PQR678, XYZ234),  // Joanne Smith
            set(GHI789, STU234),          // John Doe
            set(JKL012, XYZ456));         // Jane Doe
    
        Iterator<Set<SzFlag>> flagSetIter 
            = circularIterator(HOW_FLAG_SETS);

        for (Set<SzRecordKey> set : recordSets) {
            for (SzRecordKey recordKey : set) {
                result.add(Arguments.of(recordKey,
                                        lookup.getMapByRecordKey().get(recordKey),
                                        flagSetIter.next(),
                                        set.size(),
                                        set,
                                        null));
            }
        }

        result.add(Arguments.of(
            SzRecordKey.of(UNKNOWN_DATA_SOURCE, "ABC123"),
            -200L,
            flagSetIter.next(),
            0,
            Collections.emptySet(),
            SzNotFoundException.class));

        result.add(Arguments.of(
            SzRecordKey.of(PASSENGERS, "XXX000"),
            200000000L,
            flagSetIter.next(),
            0,
            Collections.emptySet(),
            SzNotFoundException.class));
    
        return result;
    }

    /**
     * Tests the {@link SzEngine#howEntity(long, Set)} functionality.
     * 
     * @param recordKey The {@link SzRecordKey} identifying the record for the
     *                  entity.
     * @param entityId The {@link Long} entity ID identifying the entity.
     * @param flags The {@link Set} of {@link SzFlag} instances to use.
     * @param expectedRecordCount The expected record count, or <code>null</code>
     *                            if not validating the record count.
     * @param expectedRecordKeys The {@link Set} of {@link SzRecordKey} instances
     *                           for the expected records, or <code>null</code>
     *                           if not validating.
     * @param exceptionType The {@link Class} for the expected exception type,
     *                      or <code>null</code> if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getHowEntityParameters")
    default void testHowEntity(SzRecordKey      recordKey,
                               long             entityId,
                               Set<SzFlag>      flags,
                               Integer          expectedRecordCount,
                               Set<SzRecordKey> expectedRecordKeys,
                               Class<?>         exceptionType)
    {
        String testData = "recordKey=[ " + recordKey 
            + " ], entityId=[ " + entityId + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedRecordCount=[ "
            + expectedRecordCount + " ], expectedRecordKeys=[ "
            + expectedRecordKeys + " ], expectedException=[ "
            + exceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.howEntity(entityId, flags);

                if (exceptionType != null) {
                    fail("Unexpectedly succeeded howEntity() call: "
                         + testData);
                }

                validateHowEntityResult(result,
                                        testData,
                                        recordKey,
                                        entityId,
                                        flags,
                                        expectedRecordCount,
                                        expectedRecordKeys);

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (exceptionType == null) {
                    fail("Unexpectedly failed howEntity(): "
                         + testData + ", " + description, e);

                } else if (exceptionType != e.getClass()) {
                    assertInstanceOf(
                        exceptionType, e, 
                        "howEntity() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }

    /**
     * Validates the result from {@link SzEngine#howEntity(long, Set)}.
     * 
     * @param result The result from {@link SzEngine#howEntity(long, Set)}.
     * @param testData The test data describing the test parameters so it can be
     *                 included in any failure message that is generated.
     * @param recordKey The {@link SzRecordKey} identifying the record for the
     *                  entity.
     * @param entityId The {@link Long} entity ID identifying the entity.
     * @param flags The {@link Set} of {@link SzFlag} instances to use.
     * @param expectedRecordCount The expected record count, or <code>null</code>
     *                            if not validating the record count.
     * @param expectedRecordKeys The {@link Set} of {@link SzRecordKey} instances
     *                           for the expected records, or <code>null</code>
     *                           if not validating.
     */
    default void validateHowEntityResult(
        String              result,
        String              testData,
        SzRecordKey         recordKey,
        long                entityId,
        Set<SzFlag>         flags,
        Integer             expectedRecordCount,
        Set<SzRecordKey>    expectedRecordKeys)
    {
        JsonObject jsonObject = null;
        try {
            jsonObject = parseJsonObject(result);
        } catch (Exception e) {
            fail("How entity result did not parse as JSON: "
                 + testData, e);
        }

        JsonObject how = getJsonObject(jsonObject, "HOW_RESULTS");
        assertNotNull(how, "The HOW_RESULTS property is missing: " + testData
                      + ", result=[ " + result + " ]");

        JsonArray steps = getJsonArray(how, "RESOLUTION_STEPS");
        assertNotNull(steps, "The RESOLUTION_STEPS property is missing: "
                      + testData + ", result=[ " + result + " ]");

        Set<SzFlag> stepFlags = EnumSet.of(SZ_INCLUDE_FEATURE_SCORES, 
                                           SZ_INCLUDE_MATCH_KEY_DETAILS);

        if (SzFlag.intersects(flags, stepFlags)) {
            steps.getValuesAs(JsonObject.class).forEach(step -> {
                JsonObject matchInfo = getJsonObject(step, "MATCH_INFO");
                assertNotNull(matchInfo, "The MATCH_INFO property is missing: "
                              + testData + ", step=[ " + step + " ]");

                JsonObject scores = getJsonObject(matchInfo, "FEATURE_SCORES");
                if (flags.contains(SZ_INCLUDE_FEATURE_SCORES)) {
                    assertNotNull(scores, "The FEATURE_SCORES property is missing: "
                                  + testData + ", matchInfo=[ " + matchInfo + " ]");
                } else {
                    assertNull(scores, "The FEATURE_SCORES are present, despite flags: "
                        + testData + ", matchInfo=[ " + matchInfo + " ]");
                }

                JsonObject details = getJsonObject(matchInfo, "MATCH_KEY_DETAILS");
                if (flags.contains(SZ_INCLUDE_MATCH_KEY_DETAILS)) {
                    assertNotNull(details, "The MATCH_KEY_DETAILS property is missing: "
                                  + testData + ", matchInfo=[ " + matchInfo + " ]");
                } else {
                    assertNull(details, "The MATCH_KEY_DETAILS are present, despite flags: "
                               + testData + ", matchInfo=[ " + matchInfo + " ]");
                }
            });
        }
        JsonObject finalState = getJsonObject(how, "FINAL_STATE");
        assertNotNull(finalState, "The FINAL_STATE property is missing: "
                      + testData + ", result=[ " + result + " ]");

        JsonArray virtualEntities = getJsonArray(finalState, "VIRTUAL_ENTITIES");
        assertNotNull(virtualEntities, "The VIRTUAL_ENTITIES property is missing: "
                      + testData + ", finalState=[ " + finalState + " ]");

        Set<SzRecordKey> actualRecords = new LinkedHashSet<>();
        virtualEntities.getValuesAs(JsonObject.class).forEach(virtualEntity -> {
            JsonArray memberRecords = getJsonArray(virtualEntity, "MEMBER_RECORDS");
            assertNotNull(memberRecords, "The MEMBER_RECORDS property is missing: "
                        + testData + ", virtualEntity=[ " + virtualEntity + " ]");
            
            memberRecords.getValuesAs(JsonObject.class).forEach(memberRecord -> {
                JsonArray records = getJsonArray(memberRecord, "RECORDS");
                assertNotNull(records, "The RECORDS property is missing: "
                              + testData + ", memberRecord=[ " + memberRecord + " ]");
                
                records.getValuesAs(JsonObject.class).forEach(record -> {
                    String dataSourceCode = getString(record, "DATA_SOURCE");
                    assertNotNull(dataSourceCode, "The DATA_SOURCE property is missing: "
                                  + testData + ", record=[ " + record + " ]");

                    String recordId = getString(record, "RECORD_ID");
                    assertNotNull(recordId, "The RECORD_ID property is missing: "
                                  + testData + ", record=[ " + record + " ]");

                    actualRecords.add(SzRecordKey.of(dataSourceCode, recordId));
                });
            });
        });

        if (expectedRecordKeys != null) {
            assertEquals(expectedRecordKeys, actualRecords,
                "The records were not as expected: " + testData);
        }

        if (expectedRecordCount != null) {
            assertEquals(expectedRecordCount, actualRecords.size(),
                "The number of records were not as expected: " + testData);
        }
    }
}
