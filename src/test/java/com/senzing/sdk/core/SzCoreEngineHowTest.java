package com.senzing.sdk.core;

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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.JsonObject;
import javax.json.JsonArray;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzNotFoundException;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.CollectionUtilities.list;
import static com.senzing.util.CollectionUtilities.set;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;

/**
 * Unit tests for {@link SzCoreEngine} how & virtual entity operations.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreEngineHowTest extends AbstractTest {
    private static final String UNKNOWN_DATA_SOURCE = "UNKNOWN";
    private static final String PASSENGERS          = "PASSENGERS";
    private static final String EMPLOYEES           = "EMPLOYEES";
    private static final String VIPS                = "VIPS";
    private static final String DUAL_IDENTITIES     = "DUAL_IDENTITIES";
  
    private static final SzRecordKey ABC123
        = SzRecordKey.of(PASSENGERS,"ABC123");
    private static final SzRecordKey DEF456
        = SzRecordKey.of(PASSENGERS, "DEF456");
    private static final SzRecordKey GHI789
        = SzRecordKey.of(PASSENGERS, "GHI789");
    private static final SzRecordKey JKL012
        = SzRecordKey.of(PASSENGERS, "JKL012");
    private static final SzRecordKey MNO345
        = SzRecordKey.of(EMPLOYEES, "MNO345");
    private static final SzRecordKey PQR678
        = SzRecordKey.of(EMPLOYEES, "PQR678");
    private static final SzRecordKey STU901
        = SzRecordKey.of(VIPS, "STU901");
    private static final SzRecordKey XYZ234
        = SzRecordKey.of(VIPS, "XYZ234");
    private static final SzRecordKey STU234
        = SzRecordKey.of(VIPS, "STU234");
    private static final SzRecordKey XYZ456
        = SzRecordKey.of(VIPS, "XYZ456");
    private static final SzRecordKey ZYX321
        = SzRecordKey.of(EMPLOYEES, "ZYX321");
    private static final SzRecordKey CBA654
        = SzRecordKey.of(EMPLOYEES, "CBA654");
  
    private static final SzRecordKey BCD123
        = SzRecordKey.of(DUAL_IDENTITIES, "BCD123");
    private static final SzRecordKey CDE456
        = SzRecordKey.of(DUAL_IDENTITIES, "CDE456");
    private static final SzRecordKey EFG789
        = SzRecordKey.of(DUAL_IDENTITIES, "EFG789");
    private static final SzRecordKey FGH012
        = SzRecordKey.of(DUAL_IDENTITIES, "FGH012");
  
    private static final Set<SzFlag> FEATURE_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_ALL_FEATURES,
            SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES,
            SZ_ENTITY_INCLUDE_INTERNAL_FEATURES));
    
    private static final Set<SzFlag> RECORD_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_RECORD_DATA,
            SZ_ENTITY_INCLUDE_RECORD_FEATURES));
    
    private static final List<Set<SzFlag>> VIRTUAL_ENTITY_FLAG_SETS;
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

    private static final List<Set<SzFlag>> HOW_FLAG_SETS;
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

    private static final Map<SzRecordKey, Long> LOADED_RECORD_MAP
        = Collections.synchronizedMap(new LinkedHashMap<>());

    private static final Map<Long, Set<SzRecordKey>> LOADED_ENTITY_MAP
        = Collections.synchronizedMap(new LinkedHashMap<>());

    public static Long getEntityId(SzRecordKey recordKey) {
        return LOADED_RECORD_MAP.get(recordKey);
    }

    private static final List<SzRecordKey> RECORD_KEYS
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

    private SzCoreEnvironment env = null;

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();

        String settings = this.getRepoSettings();
        
        String instanceName = this.getClass().getSimpleName();
        
        // now we just need the entity ID's for the loaded records to use later
        NativeEngine nativeEngine = new NativeEngineJni();
        try {
            int returnCode = nativeEngine.init(instanceName, settings, false);
            if (returnCode != 0) {
                throw new RuntimeException(nativeEngine.getLastException());
            }

            // get the loaded records and entity ID's
            StringBuffer sb = new StringBuffer();
            for (SzRecordKey key : RECORD_KEYS) {
                // clear the buffer
                sb.delete(0, sb.length());
                returnCode = nativeEngine.getEntityByRecordID(
                    key.dataSourceCode(), key.recordId(), sb);
                if (returnCode != 0) {
                    throw new RuntimeException(nativeEngine.getLastException());
                }
                // parse the JSON 
                JsonObject  jsonObj     = parseJsonObject(sb.toString());
                JsonObject  entity      = getJsonObject(jsonObj, "RESOLVED_ENTITY");
                Long        entityId    = getLong(entity, "ENTITY_ID");
                LOADED_RECORD_MAP.put(key, entityId);
                Set<SzRecordKey> recordKeySet = LOADED_ENTITY_MAP.get(entityId);
                if (recordKeySet == null) {
                    recordKeySet = new LinkedHashSet<>();
                    LOADED_ENTITY_MAP.put(entityId, recordKeySet);
                }
                recordKeySet.add(key);
            };

        } finally {
            nativeEngine.destroy();
        }

        this.env = SzCoreEnvironment.newBuilder()
                                      .instanceName(instanceName)
                                      .settings(settings)
                                      .verboseLogging(false)
                                      .build();
    }

    /**
     * Overridden to configure some data sources.
     */
    protected void prepareRepository() {
        File repoDirectory = this.getRepositoryDirectory();

        Set<String> dataSources = new LinkedHashSet<>();
        dataSources.add(PASSENGERS);
        dataSources.add(EMPLOYEES);
        dataSources.add(VIPS);
        dataSources.add(DUAL_IDENTITIES);

        File passengerFile      = this.preparePassengerFile();
        File employeeFile       = this.prepareEmployeeFile();
        File vipFile            = this.prepareVipFile();
        File dualIdentitiesFile = this.prepareDualIdentitiesFile();

        employeeFile.deleteOnExit();
        passengerFile.deleteOnExit();
        vipFile.deleteOnExit();
        dualIdentitiesFile.deleteOnExit();

        RepositoryManager.configSources(repoDirectory,
                                        dataSources,
                                        true);

        RepositoryManager.loadFile(repoDirectory,
                                    passengerFile,
                                    PASSENGERS,
                                    true);

        RepositoryManager.loadFile(repoDirectory,
                                    employeeFile,
                                    EMPLOYEES,
                                    true);

        RepositoryManager.loadFile(repoDirectory,
                                    vipFile,
                                    VIPS,
                                    true);

        RepositoryManager.loadFile(repoDirectory,
                                    dualIdentitiesFile,
                                    DUAL_IDENTITIES,
                                    true);
    }

    private File preparePassengerFile() {
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
        return this.prepareCSVFile("test-passengers-", headers, passengers);
    }
    
    private File prepareEmployeeFile() {
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
    
        return this.prepareJsonArrayFile("test-employees-", headers, employees);
    }
    
    private File prepareVipFile() {
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
    
        return this.prepareJsonFile("test-vips-", headers, vips);
    }
    
    private File prepareDualIdentitiesFile() {
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
    
        return this.prepareJsonFile("test-marriages-", headers, spouses);
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

    private List<Arguments> getVirtualEntityParameters() {
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

    @ParameterizedTest
    @MethodSource("getVirtualEntityParameters")
    void testGetVirtualEntity(Set<SzRecordKey>          recordKeys,
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
                SzEngine engine = this.env.getEngine();

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
                    fail("Unexpectedly failed whyEntities(): "
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


    public static void validateVirtualEntity(String                     result,
                                             String                     testData,
                                             Set<SzRecordKey>           recordKeys,
                                             Set<SzFlag>                flags,
                                             Integer                    expectedRecordCount,
                                             Map<String,Integer>        expectedFeatureCounts,
                                             Map<String,Set<String>>    primaryFeatureValues)
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
            assertNotNull(features, "The RECORDS property is missing or null: "
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

    private List<Arguments> getHowEntityParameters() {
        List<Arguments> result = new LinkedList<>();

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
                                        getEntityId(recordKey),
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

    @ParameterizedTest
    @MethodSource("getHowEntityParameters")
    void testHowEntity(SzRecordKey      recordKey,
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
                SzEngine engine = this.env.getEngine();

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

    public static void validateHowEntityResult(
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
