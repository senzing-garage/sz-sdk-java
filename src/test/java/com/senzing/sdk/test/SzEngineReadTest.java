package com.senzing.sdk.test;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.BufferedReader;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.json.JsonObject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzNotFoundException;
import com.senzing.sdk.SzBadInputException;
import com.senzing.util.JsonUtilities;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.sdk.test.SdkTest.*;
import static com.senzing.io.RecordReader.Format;
import static com.senzing.io.RecordReader.Format.*;
import static com.senzing.sdk.test.SzEngineReadTest.TestData.*;

/**
 * Unit tests for {@link SzEngine} read operations.
 */
public interface SzEngineReadTest extends SdkTest {
    /**
     * The test data class for the {@link SzEngineReadTest}
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
         * The data source code for the marriages data source.
         */
        public static final String MARRIAGES = "MARRIAGES";

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
         * with record ID <code>"XYZ234"</code>.
         */
        public static final SzRecordKey XYZ234
            = SzRecordKey.of(VIPS, "XYZ234");

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
            = SzRecordKey.of(MARRIAGES, "BCD123");
        
        /**
         * The {@link SzRecordKey} for the marriages record 
         * with record ID <code>"CDE456"</code>.
         */
        public static final SzRecordKey CDE456
            = SzRecordKey.of(MARRIAGES, "CDE456");

        /**
         * The {@link SzRecordKey} for the marriages record 
         * with record ID <code>"EFG789"</code>.
         */
        public static final SzRecordKey EFG789
            = SzRecordKey.of(MARRIAGES, "EFG789");

        /**
         * The {@link SzRecordKey} for the marriages record 
         * with record ID <code>"FGH012"</code>.
         */
        public static final SzRecordKey FGH012
            = SzRecordKey.of(MARRIAGES, "FGH012");
    

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * entity test functions.
         */
        public static final List<Set<SzFlag>> ENTITY_FLAG_SETS;

        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_ENTITY_DEFAULT_FLAGS);
            list.add(SZ_ENTITY_BRIEF_DEFAULT_FLAGS);
            list.add(unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
            list.add(unmodifiableSet(
                EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
            ENTITY_FLAG_SETS = unmodifiableList(list);
        }

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * record test functions.
         */
        public static final List<Set<SzFlag>> RECORD_FLAG_SETS;
        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_RECORD_DEFAULT_FLAGS);
            list.add(SZ_RECORD_ALL_FLAGS);
            list.add(unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO,
                SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA)));
            list.add(unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_TYPES,
                SZ_ENTITY_INCLUDE_RECORD_JSON_DATA)));
            RECORD_FLAG_SETS = unmodifiableList(list);
        }

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * search test functions for including specific kinds
         * of search results.
         */
        public static final List<SzFlag> SEARCH_INCLUDE_FLAGS
            = List.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED,
                      SZ_SEARCH_INCLUDE_POSSIBLY_SAME,
                      SZ_SEARCH_INCLUDE_RESOLVED);

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * search test functions.
         */
        public static final List<Set<SzFlag>> SEARCH_FLAG_SETS;
        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS);
            list.add(SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL);
            list.add(SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG);
            list.add(SZ_SEARCH_BY_ATTRIBUTES_STRONG);
            Set<SzFlag> set = EnumSet.of(
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA);
            set.addAll(SZ_SEARCH_INCLUDE_ALL_ENTITIES);
            list.add(unmodifiableSet(set));

            Set<Set<SzFlag>> includeSets    = new LinkedHashSet<>();
            Set<Set<SzFlag>> prevSets       = Set.of(SZ_ENTITY_DEFAULT_FLAGS);
            for (int index = 0; index < SEARCH_INCLUDE_FLAGS.size(); index++) {
                Set<Set<SzFlag>> currentSets = new LinkedHashSet<>();
                for (Set<SzFlag> prevSet: prevSets) {
                    for (SzFlag flag : SEARCH_INCLUDE_FLAGS) {
                        Set<SzFlag> currentSet = EnumSet.noneOf(SzFlag.class);
                        currentSet.addAll(prevSet);
                        currentSet.add(flag);
                        currentSets.add(currentSet);
                    }
                }
                prevSets = currentSets;
                includeSets.addAll(currentSets);
                currentSets = new LinkedHashSet<>();
            }
            list.addAll(includeSets);
            SEARCH_FLAG_SETS = unmodifiableList(list);
        }

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * export test functions for including specific kinds
         * of entities in the export.
         */
        public static final List<SzFlag> EXPORT_INCLUDE_FLAGS
            = List.of(SZ_EXPORT_INCLUDE_POSSIBLY_RELATED,
                      SZ_EXPORT_INCLUDE_POSSIBLY_SAME,
                      SZ_EXPORT_INCLUDE_DISCLOSED,
                      SZ_EXPORT_INCLUDE_NAME_ONLY,
                      SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES,
                      SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES);

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * export test functions.
         */
        public static final List<Set<SzFlag>> EXPORT_FLAG_SETS;
        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_EXPORT_DEFAULT_FLAGS);
            list.add(SZ_EXPORT_INCLUDE_ALL_ENTITIES);
            list.add(SZ_EXPORT_INCLUDE_ALL_HAVING_RELATIONSHIPS);
            list.add(SZ_EXPORT_ALL_FLAGS);
            Set<SzFlag> set = EnumSet.of(
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA);
            set.addAll(SZ_EXPORT_INCLUDE_ALL_ENTITIES);
            list.add(unmodifiableSet(set));

            Set<Set<SzFlag>> includeSets    = new LinkedHashSet<>();
            Set<Set<SzFlag>> prevSets       = Set.of(SZ_ENTITY_DEFAULT_FLAGS);
            for (int index = 0; index < EXPORT_INCLUDE_FLAGS.size(); index++) {
                Set<Set<SzFlag>> currentSets = new LinkedHashSet<>();
                for (Set<SzFlag> prevSet: prevSets) {
                    for (SzFlag flag : EXPORT_INCLUDE_FLAGS) {
                        Set<SzFlag> currentSet = EnumSet.noneOf(SzFlag.class);
                        currentSet.addAll(prevSet);
                        currentSet.add(flag);
                        currentSets.add(currentSet);
                    }
                }
                prevSets = currentSets;
                includeSets.addAll(currentSets);
                currentSets = new LinkedHashSet<>();
            }
            list.addAll(includeSets);
            EXPORT_FLAG_SETS = unmodifiableList(list);
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
                {STU901.recordId(), "John", "Doe", "818-555-1313",
                    "100 Main Street, Los Angeles, CA 90012", "1978-10-17", "GREEN"},
                {XYZ234.recordId(), "Jane", "Doe", "818-555-1212",
                    "100 Main Street, Los Angeles, CA 90012", "1979-02-05", "GRAHAM"}
            };

            return prepareDataFile(format, "test-vips-", headers, vips);
        }

        /**
         * Prepares a {@link File} of marriage data for the {@link #MARRIAGES}
         * data source. 
         * 
         * @param format The {@link Format} for the file.
         * 
         * @return The {@link File} with the data to be loaded.
         */
        public static File prepareMarriagesFile(Format format) {
            String[] headers = {
                "RECORD_ID", "NAME_FULL", "AKA_NAME_FULL", "PHONE_NUMBER", "ADDR_FULL",
                "MARRIAGE_DATE", "DATE_OF_BIRTH", "GENDER", "RELATIONSHIP_TYPE",
                "RELATIONSHIP_ROLE", "RELATIONSHIP_KEY" };

            String[][] spouses = {
                {BCD123.recordId(), "Bruce Wayne", "Batman", "201-765-3451",
                    "101 Wayne Manor Rd; Gotham City, NJ 07017", "2008-06-05",
                    "1971-09-08", "M", "SPOUSE", "HUSBAND",
                    relationshipKey(BCD123, CDE456)},
                {CDE456.recordId(), "Selina Kyle", "Catwoman", "201-875-2314",
                    "101 Wayne Manor Rd; Gotham City, NJ 07017", "2008-06-05",
                    "1981-12-05", "F", "SPOUSE", "WIFE",
                    relationshipKey(BCD123, CDE456)},
                {EFG789.recordId(), "Barry Allen", "The Flash", "330-982-2133",
                    "1201 Main Street; Star City, OH 44308", "2014-11-07",
                    "1986-03-04", "M", "SPOUSE", "HUSBAND",
                    relationshipKey(EFG789, FGH012)},
                {FGH012.recordId(), "Iris West-Allen", "", "330-675-1231",
                    "1201 Main Street; Star City, OH 44308", "2014-11-07",
                    "1986-05-14", "F", "SPOUSE", "WIFE",
                    relationshipKey(EFG789, FGH012)}
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
            loader.configureDataSources(PASSENGERS, EMPLOYEES, VIPS, MARRIAGES);
            Map<String, File> fileMap = new LinkedHashMap<>();
            
            fileMap.put(PASSENGERS, preparePassengerFile(CSV));
            fileMap.put(EMPLOYEES, prepareEmployeeFile(JSON));
            fileMap.put(VIPS, prepareVipFile(JSON_LINES));
            fileMap.put(MARRIAGES, prepareMarriagesFile(JSON_LINES));

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
     * Gets the entity ID for the specified {@link SzRecordKey}.
     * 
     * @param recordKey The {@link SzRecordKey} for which the entity ID
     *                  is being requested.
     * 
     * @return The entity ID for the specified {@link SzRecordKey}, or
     *         <code>null</code> if not found.
     */
    default Long getEntityId(SzRecordKey recordKey) {
        SzEntityLookup lookup = this.getTestData().getEntityLookup();
        return lookup.getMapByRecordKey().get(recordKey);
    }

    /**
     * Encodes a "relationship key" for the specified record keys.
     * This will sort the record keys so regardless of the order of
     * the parameters, the formated results will be the same.
     * 
     * @param recordKey1 The first {@link SzRecordKey}.
     * @param recordKey2 The second {@link SzRecordKey}.
     * 
     * @return The formatted "relationship key".
     */
    private static String relationshipKey(SzRecordKey recordKey1,
                                          SzRecordKey recordKey2) 
    {
        String rec1 = recordKey1.toString();
        String rec2 = recordKey2.toString();
        if (rec1.compareTo(rec2) <= 0) {
            return rec1 + "|" + rec2;
        } else {
            return rec2 + "|" + rec1;
        }
    }

    /**
     * Gets the record-key parameters for running a test with each
     * record key from the {@link TestData}.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>{@link SzRecordKey}
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances as described.
     */
    default List<Arguments> getRecordKeyParameters() {
        List<Arguments> result = new ArrayList<>(RECORD_KEYS.size());
        RECORD_KEYS.forEach(key -> {
            result.add(Arguments.of(key));
        });
        return result;
    }

    /**
     * Gets the parameters for testing the "get entity" functions.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>Test info {@link String}
     *   <li>The {@link SzRecordKey}
     *   <li>The respective {@link Long} entity ID.
     *   <li>The {@link Set} of {@link SzFlag} instances for entity flags.
     *   <li>The expected exception {@link Class} if performing the operation
     *       by {@link SzRecordKey}, or <code>null</code> if no exception expected.
     *   <li>The expected exception {@link Class} if performing the operation
     *       by entity ID, or <code>null</code> if no exception expected.
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances as described.
     */
    default List<Arguments> getGetEntityParameters() {
        List<Arguments>         result      = new LinkedList<>();
        Iterator<Set<SzFlag>>   flagSetIter = circularIterator(ENTITY_FLAG_SETS);

        final Class<?> UNKNOWN_SOURCE = SzUnknownDataSourceException.class;
        final Class<?> NOT_FOUND = SzNotFoundException.class;

        for (SzRecordKey recordKey : RECORD_KEYS) {
            long entityId = getEntityId(recordKey);
            result.add(Arguments.of(
                "Get entity for " + recordKey + "(" + entityId + ") test",
                recordKey,
                entityId,
                flagSetIter.next(), 
                null,
                null));
        }

        result.add(Arguments.of(
            "Get Entity with bad data source code / entity ID test",
            SzRecordKey.of(UNKNOWN_DATA_SOURCE, "ABC123"),
            -200L,
            flagSetIter.next(),
            UNKNOWN_SOURCE,
            NOT_FOUND));

        result.add(Arguments.of(
            "Get Entity with not-found record ID / entity ID test",
            SzRecordKey.of(PASSENGERS, "XXX000"),
            200000000L,
            flagSetIter.next(),
            NOT_FOUND,
            NOT_FOUND));

        return result;
    }

    /**
     * Tests the {@link SzEntity#getEntity(SzRecordKey)} function.
     * 
     * @param testDescription The parameter description to log in case of failure.
     * @param recordKey The {@link SzRecordKey} for the record.
     * @param entityId The entity ID for the record to lookup (must be the entity).
     * @param flags The {@link Set} of {@link SzFlag} instances.
     * @param recordExceptionType The expected exception or <code>null</code> if none.
     * @param entityExceptionType The by-entity-id exception (ignored).
     */
    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    default void testGetEntityByRecordId(String         testDescription,
                                         SzRecordKey    recordKey,
                                         long           entityId,
                                         Set<SzFlag>    flags,
                                         Class<?>       recordExceptionType,
                                         Class<?>       entityExceptionType)
    {
        String testData = "description=[ " + testDescription
            + " ], recordKey=[ " + recordKey + " ], entityId=[ "
            + entityId + " ], flags=[ " + SzFlag.toString(flags)
            + " ], expectedExceptionType=[ " + recordExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.getEntity(recordKey, flags);

                if (recordExceptionType != null) {
                    fail("Unexpectedly succeeded in getting an entity: " + testData);
                }
                
                // parse the result
                JsonObject  jsonObject = null;
                try {
                    jsonObject = parseJsonObject(result);

                } catch (Exception e) {
                    fail("Failed to parse entity result JSON: " + testData 
                         + ", result=[ " + result + " ]", e);
                }

                // get the entity
                JsonObject  entity = getJsonObject(jsonObject, "RESOLVED_ENTITY");

                assertNotNull(entity, "No RESOLVED_ENTITY property in entity JSON: " 
                              + testData + ", result=[ " + result + " ]");

                // get the entity ID
                Long actualEntityId = getLong(entity, "ENTITY_ID");

                assertNotNull(entity, "No ENTITY_ID property in entity JSON: "
                              + testData + ", result=[ " + result + " ]");

                assertEquals(entityId, actualEntityId, 
                             "Unexpected entity ID: " + testData);

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (recordExceptionType == null) {
                    fail("Unexpectedly failed getting entity by record: "
                         + description, e);

                } else {
                    assertInstanceOf(
                        recordExceptionType, e, 
                        "get-entity-by-record failed with an unexpected exception type: "
                        + description);
                }
            }
        });
    }

    /**
     * Tests the {@link SzEntity#getEntity(long)} function.
     * 
     * @param testDescription The parameter description to log in case of failure.
     * @param recordKey The {@link SzRecordKey} for the record (must be contained).
     * @param entityId The entity ID for the record to lookup.
     * @param flags The {@link Set} of {@link SzFlag} instances.
     * @param recordExceptionType The by-record-key exception (ignored).
     * @param entityExceptionType The expected exception or <code>null</code> if none.
     */
    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    default void testGetEntityByEntityId(String         testDescription,
                                         SzRecordKey    recordKey,
                                         long           entityId,
                                         Set<SzFlag>    flags,
                                         Class<?>       recordExceptionType,
                                         Class<?>       entityExceptionType)
    {
        String testData = "description=[ " + testDescription
            + " ], recordKey=[ " + recordKey + " ], entityId=[ "
            + entityId + " ], flags=[ " + SzFlag.toString(flags)
            + " ], expectedExceptionType=[ " + entityExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.getEntity(entityId, flags);

                if (entityExceptionType != null) {
                    fail("Unexpectedly succeeded in getting an entity: " + testData);
                }
                
                // parse the result
                JsonObject jsonObject = null;
                try {
                    jsonObject = parseJsonObject(result);

                } catch (Exception e) {
                    fail("Failed to parse entity result JSON: " + testData 
                         + ", result=[ " + result + " ]", e);
                }

                // get the entity
                JsonObject  entity = getJsonObject(jsonObject, "RESOLVED_ENTITY");

                assertNotNull(entity, "No RESOLVED_ENTITY property in entity JSON: " 
                              + testData + ", result=[ " + result + " ]");

                // get the entity ID
                Long actualEntityId = getLong(entity, "ENTITY_ID");

                assertNotNull(entity, "No ENTITY_ID property in entity JSON: "
                              + testData + ", result=[ " + result + " ]");

                assertEquals(entityId, actualEntityId, 
                             "Unexpected entity ID: " + testData);

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (entityExceptionType == null) {
                    fail("Unexpectedly failed getting entity by record: "
                         + description, e);

                } else {
                    assertInstanceOf(
                        entityExceptionType, e, 
                        "get-entity-by-id failed with an unexpected exception type: "
                        + description);
                }
            }
        });
    }

    /**
     * Gets the CSV export test parameters.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>The CSV headers {@link String}
     *   <li>The {@link Set} of {@link SzFlag} instances for export flags.
     *   <li>The expected exception {@link Class}, or <code>null</code> if
     *       no exception expected.
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances as described.
     */
    default List<Arguments> getExportCsvParameters() {
        List<Arguments> result = new LinkedList<>();

        List<String> columnLists = List.of(
            "", "*", 
            "RESOLVED_ENTITY_ID,RESOLVED_ENTITY_NAME,RELATED_ENTITY_ID");

        Iterator<String> columnListIter = circularIterator(columnLists);

        for (Set<SzFlag> flagSet : EXPORT_FLAG_SETS) {
            result.add(Arguments.of(columnListIter.next(), flagSet, null));        
        }

        Set<SzFlag> flagSet = EXPORT_FLAG_SETS.iterator().next();
        result.add(
            Arguments.of("RESOLVED_ENTITY_ID,BAD_COLUMN_NAME",
                         flagSet,
                         SzBadInputException.class));
        
        return result;
    }

    /**
     * Gets the JSON export test parameters.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>The {@link Set} of {@link SzFlag} instances for export flags.
     *   <li>The expected exception {@link Class}, or <code>null</code> if
     *       no exception expected.
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances as described.
     */
    default List<Arguments> getExportJsonParameters() {
        List<Arguments> result = new LinkedList<>();

        for (Set<SzFlag> flagSet : EXPORT_FLAG_SETS) {
            result.add(Arguments.of(flagSet, null));        
        }
        
        return result;
    }

    /**
     * Tests the CSV export functionality.
     * 
     * @param columnList The list of columns to export.
     * @param flags The flags for the export.
     * @param expectedException The expected exception, or <code>null</code>
     *                          if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getExportCsvParameters")
    default void testExportCsvEntityReport(String       columnList,
                                           Set<SzFlag>  flags,
                                           Class<?>     expectedException)
    {
        String testData = "columnList=[ " + columnList + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedException=[ "
            + expectedException + " ]";

        this.performTest(() -> {
            Long        handle = null;
            SzEngine    engine = null;
            try {
                engine = this.getEngine();

                handle = engine.exportCsvEntityReport(columnList, flags);
                
                if (expectedException != null) {
                    fail("Export unexpectedly succeeded when exception expected: " 
                        + testData);
                }

                StringWriter    sw = new StringWriter();
                PrintWriter     pw = new PrintWriter(sw);
                for (String data = engine.fetchNext(handle); 
                     data != null; 
                     data = engine.fetchNext(handle)) 
                {
                    pw.println(data);
                }

                long invalidHandle = handle;
                try {
                    engine.closeExport(handle);
                } finally {
                    handle = null;
                }
                try {
                    // try fetching with an invalid handle
                    engine.fetchNext(invalidHandle);
                    fail("Succeeded in fetching with an invalid export handle");
                    
                } catch (SzException e) { // TODO(bcaceres): change this to SzBadInputException??
                    // expected
                } catch (Exception e) {
                    fail("Fetching with invalid handle failed with unexpected exception.", e);
                }
                try {
                    // try closing the handle twice (should succeed)
                    engine.closeExport(invalidHandle);

                    // should not be able to close an invalid handle
                    fail("Unexpectedly succeeded in closing an invalid export handle.");

                } catch (Exception e) {
                    // expected
                }

                String fullExport = sw.toString();

                CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setHeader().setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .setIgnoreSurroundingSpaces(true).build();

                // ensure we can parse the CSV
                StringReader reader = new StringReader(fullExport);
                try (CSVParser parser = new CSVParser(reader, csvFormat)) {
                    Map<String, Integer> headerMap = parser.getHeaderMap();
                    Set<String> headers = new LinkedHashSet<>();
                    headerMap.keySet().forEach((key) -> {
                        headers.add(key.trim().toUpperCase());
                    });
                    
                    if (columnList.equals("*") || columnList.length() == 0) {
                        assertTrue(headers.contains("RESOLVED_ENTITY_ID"),
                            "Default columns exported, but RESOLVED_ENTITY_ID not found "
                            + "in headers (" + headers + "): " + testData);
                    
                    } else {
                        String[] columns = columnList.split(",");
                        for (String column : columns) {
                            column = column.trim().toUpperCase();
                            assertTrue(headers.contains(column), 
                                "Specified column (" + column + ") was not found in columns ("
                                + headers + "): " + testData);
                        }
                    }

                    Iterator<CSVRecord> recordIter = parser.iterator();
                    while (recordIter.hasNext()) {
                        CSVRecord record = recordIter.next();
                        assertNotNull(record, "The CSV record was null");
                    }
                }

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (expectedException == null) {
                    fail("Unexpectedly failed exporting CSV entities: "
                         + description, e);

                } else {
                    assertInstanceOf(
                        expectedException, e, 
                        "Export CSV failed with an unexpected exception type: "
                        + description);
                }


            } finally {
                if (handle != null && engine != null) {
                    try {
                        engine.closeExport(handle);
                    } catch (SzException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }   

    /**
     * Tests CSV export default flags to see if they are as expected.
     * 
     * @param columnList The list of columns to export.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "","*","RESOLVED_ENTITY_ID,RESOLVED_ENTITY_NAME,RELATED_ENTITY_ID"})
    default void testExportCsvDefaults(String columnList)
    {
        this.performTest(() ->
        {
            long exportHandle = 0L;
            try {
                SzEngine engine = this.getEngine();

                exportHandle = engine.exportCsvEntityReport(columnList);
                String defaultResult = readExportFullyAndClose(engine, exportHandle);
                exportHandle = 0L;

                exportHandle = engine.exportCsvEntityReport(columnList, 
                                                            SZ_EXPORT_DEFAULT_FLAGS);
                String explicitResult = readExportFullyAndClose(engine, exportHandle);
                exportHandle = 0L;
                
                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");
            }
            catch (Exception e) {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }

    /**
     * Utility function to read an export report fully and then close the
     * export report.
     * 
     * @param engine The {@link SzEngine} to use.
     * 
     * @param exportHandle The export handle for the export report.
     * 
     * @return The text that was read.
     * 
     * @throws SzException If a failure occurs.
     */
    private static String readExportFullyAndClose(SzEngine engine, long exportHandle)
        throws SzException 
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        for (String data = engine.fetchNext(exportHandle);
             data != null;
             data = engine.fetchNext(exportHandle))
        {
            pw.println(data);
        }
        engine.closeExport(exportHandle);
        pw.flush();
        return sw.toString();
    }

    /**
     * Tests JSON export functionality.
     * 
     * @param flags The flags to use for the export.
     * 
     * @param expectedException The expected exception, or <code>null</code>
     *                          if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getExportJsonParameters")
    default void testExportJsonEntityReport(Set<SzFlag>  flags,
                                            Class<?>     expectedException)
    {
        String testData = "flags=[ " + SzFlag.toString(flags) 
            + " ], expectedException=[ " + expectedException + " ]";

        this.performTest(() -> {
            Long        handle = null;
            SzEngine    engine = null;
            try {
                engine = this.getEngine();

                handle = engine.exportJsonEntityReport(flags);
                
                if (expectedException != null) {
                    fail("Export unexpectedly succeeded when exception expected: " 
                        + testData);
                }

                StringWriter    sw = new StringWriter();
                PrintWriter     pw = new PrintWriter(sw);
                for (String data = engine.fetchNext(handle); 
                     data != null; 
                     data = engine.fetchNext(handle)) 
                {
                    pw.println(data);
                }

                long invalidHandle = handle;
                try {
                    engine.closeExport(handle);
                } finally {
                    handle = null;
                }
                try {
                    // try fetching with an invalid handle
                    engine.fetchNext(invalidHandle);
                    fail("Succeeded in fetching with an invalid export handle");

                } catch (SzException e) { // TODO(bcaceres): change this to SzBadInputException??
                    // expected
                } catch (Exception e) {
                    fail("Fetching with invalid handle failed with unexpected exception.", e);
                }
                try {
                    // try closing the handle twice (should succeed)
                    engine.closeExport(invalidHandle);

                } catch (Exception e) {
                    // TODO(bcaceres): This should have succeeded but currently fails
                    //fail("Failed to close an export handle more than once.", e);
                }

                String fullExport = sw.toString();

                // ensure we can parse the JSON
                StringReader reader = new StringReader(fullExport);
                BufferedReader br = new BufferedReader(reader);
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    if (line.trim().length() == 0) {
                        continue;
                    }
                    JsonUtilities.parseJsonObject(line);
                }

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (expectedException == null) {
                    fail("Unexpectedly failed exporting JSON entities: "
                         + description, e);

                } else {
                    assertInstanceOf(
                        expectedException, e, 
                        "Export JSON failed with an unexpected exception type: "
                        + description);
                }


            } finally {
                if (handle != null && engine != null) {
                    try {
                        engine.closeExport(handle);
                    } catch (SzException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Tests the JSON export default flags.
     */
    @Test
    default void testExportJsonDefaults() {
        this.performTest(() -> {
            long exportHandle = 0L;
            try
            {
                SzEngine engine = this.getEngine();
                
                exportHandle = engine.exportJsonEntityReport();
                String defaultResult = readExportFullyAndClose(engine, exportHandle);
                exportHandle = 0L;
                
                exportHandle = engine.exportJsonEntityReport(SZ_EXPORT_DEFAULT_FLAGS);
                String explicitResult = readExportFullyAndClose(engine, exportHandle);
                exportHandle = 0L;
                
                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");
            }
            catch (Exception e)
            {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }

    /**
     * Tests {@link SzEngine#findInterestingEntities(SzRecordKey)}
     * with the default flags.
     * 
     * @param recordKey The record key to test with.
     */
    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    default void testFindInterestingEntitiesByRecordIdDefaults(SzRecordKey recordKey) {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String defaultResult = engine.findInterestingEntities(recordKey);

                String explicitResult = engine.findInterestingEntities(
                    recordKey, SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS);
                    
                String nullResult = engine.findInterestingEntities(
                    recordKey, null);

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nullResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than setting the flags parameter to null for the SDK function.");

            } catch (Exception e) {
                fail("Unexpectedly failed getting interesting entities by record", e);
            }
        });
    }

    /**
     * Tests {@link SzEngine#findInterestingEntities(SzRecordKey, Set)} funtionality.
     * 
     * @param testDescription The description of the test.
     * @param recordKey The record key to test with.
     * @param entityId The respective entity ID for the specified record key.
     * @param flags The flags to test with.
     * @param recordExceptionType The expected exeption, or <code>null</code> if none.
     * @param entityExceptionType This is ignored by this test.
     */
    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    default void testFindInterestingEntitiesByRecordId(
        String         testDescription,
        SzRecordKey    recordKey,
        long           entityId,
        Set<SzFlag>    flags,
        Class<?>       recordExceptionType,
        Class<?>       entityExceptionType)
    {
        String testData = "description=[ " + testDescription
            + " ], recordKey=[ " + recordKey + " ], entityId=[ "
            + entityId + " ], flags=[ " + SzFlag.toString(flags)
            + " ], expectedExceptionType=[ " + recordExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.findInterestingEntities(recordKey, flags);

                if (recordExceptionType != null) {
                    fail("Unexpectedly succeeded in getting an entity: " + testData);
                }
                
                // parse the result
                try {
                    parseJsonObject(result);

                } catch (Exception e) {
                    fail("Failed to parse entity result JSON: " + testData 
                         + ", result=[ " + result + " ]", e);
                }

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (recordExceptionType == null) {
                    fail("Unexpectedly failed finding interesting entities by record: "
                         + description, e);

                } else {
                    assertInstanceOf(
                        recordExceptionType, e, 
                        "find-interesting-entities-by-record failed with an unexpected "
                        + "exception type: " + description);
                }
            }
        });
    }

    /**
     * Tests {@link SzEngine#findInterestingEntities(long)} with the default flags.
     * 
     * @param recordKey The {@link SzRecordKey} for obtaining the entity ID to use.
     */
    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    default void testFindInterestingEntitiesByEntityIdDefaults(SzRecordKey recordKey) {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                long entityId = this.getEntityId(recordKey);

                String defaultResult = engine.findInterestingEntities(entityId);

                String explicitResult = engine.findInterestingEntities(
                    entityId, SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS);
                    
                String nullResult = engine.findInterestingEntities(
                    entityId, null);

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nullResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than setting the flags parameter to null for the SDK function.");

            } catch (Exception e) {
                fail("Unexpectedly failed getting interesting entities by record", e);
            }
        });
    }

    /**
     * Tests {@link SzEngine#findInterestingEntities(SzRecordKey,Set)} funtionality.
     * 
     * @param testDescription The description of the test.
     * @param recordKey The record key to test with.
     * @param entityId The respective entity ID for the specified record key.
     * @param flags The flags to test with.
     * @param recordExceptionType This is ignored by this test.
     * @param entityExceptionType The expected exeption, or <code>null</code> if none.
     */
    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    default void testFindInterestingEntitiesByEntityId(
        String         testDescription,
        SzRecordKey    recordKey,
        long           entityId,
        Set<SzFlag>    flags,
        Class<?>       recordExceptionType,
        Class<?>       entityExceptionType)
    {
        String testData = "description=[ " + testDescription
            + " ], recordKey=[ " + recordKey + " ], entityId=[ "
            + entityId + " ], flags=[ " + SzFlag.toString(flags)
            + " ], expectedExceptionType=[ " + entityExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.findInterestingEntities(entityId, flags);

                if (entityExceptionType != null) {
                    fail("Unexpectedly succeeded in getting an entity: " + testData);
                }
                
                // parse the result
                try {
                    parseJsonObject(result);

                } catch (Exception e) {
                    fail("Failed to parse entity result JSON: " + testData 
                         + ", result=[ " + result + " ]", e);
                }

            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (entityExceptionType == null) {
                    fail("Unexpectedly failed finding interesting enntities by ID: "
                         + description, e);

                } else {
                    assertInstanceOf(
                        entityExceptionType, e, 
                        "find-interesting-entities-by-id failed with an unexpected "
                        + "exception type: " + description);
                }
            }
        });
    }

    /**
     * Provides the parameters for testing the "get record" functionality.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>The description of the test.
     *   <li>The {@link SzRecordKey} for the test.
     *   <li>The {@link Set} of {@link SzFlag} instances for the test.
     *   <li>The expected exception {@link Class}, or <code>null</code> if
     *       no exception expected.
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances.
     */
    default List<Arguments> getGetRecordParameters() {
        List<Arguments>         result      = new LinkedList<>();
        Iterator<Set<SzFlag>>   flagSetIter = circularIterator(RECORD_FLAG_SETS);

        final Class<?> UNKNOWN_SOURCE = SzUnknownDataSourceException.class;
        final Class<?> NOT_FOUND = SzNotFoundException.class;

        for (SzRecordKey recordKey : RECORD_KEYS) {
            result.add(Arguments.of(
                "Get record for " + recordKey + " test",
                recordKey,
                flagSetIter.next(), 
                null));
        }

        result.add(Arguments.of(
            "Get record with bad data source code test",
            SzRecordKey.of(UNKNOWN_DATA_SOURCE, "ABC123"),
            flagSetIter.next(),
            UNKNOWN_SOURCE));

        result.add(Arguments.of(
            "Get record with not-found record ID test",
            SzRecordKey.of(PASSENGERS, "XXX000"),
            flagSetIter.next(),
            NOT_FOUND));

        return result;
    }

    /**
     * Tests the "get recoord" funtionality.
     * 
     * @param testDescription The desription of the test.
     * @param recordKey The {@link SzRecordKey} for the test.
     * @param flags The {@link Set} of {@link SzFlag} instances for the test.
     * @param expectedExceptionType The expected exeption, or <code>null</code> if none.
     */
    @ParameterizedTest
    @MethodSource("getGetRecordParameters")
    default void testGetRecord(String       testDescription,
                               SzRecordKey  recordKey,
                               Set<SzFlag>  flags,
                               Class<?>     expectedExceptionType)
    {
        String testData = "description=[ " + testDescription 
            + " ], recordKey=[ " + recordKey + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedExceptionType=[ "
            + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.getRecord(recordKey, flags);

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in getting a record: " + testData);
                }
                
                // parse the result
                JsonObject jsonObject = null;
                try {
                    jsonObject = parseJsonObject(result);

                } catch (Exception e) {
                    fail("Failed to parse record result JSON: " + testData 
                         + ", result=[ " + result + " ]", e);
                }

                // get the data source code
                String dataSourceCode = getString(jsonObject, "DATA_SOURCE");

                assertNotNull(dataSourceCode, "No DATA_SOURCE property in record JSON: " 
                              + testData + ", result=[ " + result + " ]");

                // get the record ID
                String recordId = getString(jsonObject, "RECORD_ID");

                assertNotNull(recordId, "No RECORD_ID property in record JSON: " 
                              + testData + ", result=[ " + result + " ]");

                SzRecordKey actualRecordKey = SzRecordKey.of(dataSourceCode, recordId);

                assertEquals(recordKey, actualRecordKey, 
                    "The data source code and/or record ID are not as expected: "
                        + testData + ", result=[ " + result + " ]");
                
            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed getting entity by record: "
                         + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "get-record failed with an unexpected exception type: "
                        + description);
                }
            }
        });
    }

    /**
     * Utility method for creating a {@link SortedMap} from a {@link Map}.
     * 
     * @param <K> The type for the map key.
     * @param <V> The type for the map value.
     * @param map The {@link Map} from which to copy the entries.
     * @return The {@link SortedMap} with the same entries.
     */
    private static <K extends Comparable<K>, V> Map<K, V> sortedMap(Map<K, V> map)
    {
        List<K> list = new ArrayList<>(map.size());
        list.addAll(map.keySet());
        Collections.sort(list);
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (K key: list) {
            sortedMap.put(key, map.get(key));
        }
        return sortedMap;
    }


    /**
     * Encapsualtes a search criterion for testing the search 
     * functions.
     */
    public static class Criterion {
        private String key;
        private Set<String> values;

        /**
         * Constructs with the specified key and the one or more values.
         *
         * @param key The key for the criterion.
         * @param value The value for the criterion.
         * @param otherValues The additional values for the criterion.
         * 
         * @throws NullPointerException If the specified key or any of the
         *                              values is <code>null</code>.
         */
        public Criterion(String key, String value, String... otherValues) {
            Objects.requireNonNull(key, "The key cannot be null");
            Objects.requireNonNull(value, "The value cannot be null");
            this.key = key;
            this.values = new LinkedHashSet<>();
            this.values.add(value);
            if (otherValues != null) {
                for (String val : otherValues) {
                    Objects.requireNonNull(
                        val, "None of the additional values can be null");
                    this.values.add(val);
                }
            }
            this.values = unmodifiableSet(this.values);
        }

        /**
         * Gets the key for the criterion.
         * @return The key for the criterion.
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Gets the <b>unmodifiable</b> {@link Set} of one or more
         * values for the criterion.
         * 
         * @return The <b>unmodifiable</b> {@link Set} of one or more
         *         values for the criterion.
         */
        public Set<String> getValues() {
            return this.values;
        }

        /**
         * Implemented to return <code>true</code> if and only if the
         * specified parameter is a non-null reference to an object 
         * of the same class with an equivalent {@linkplain #getKey() key}
         * and {@link #getValues() values}.
         * 
         * {@inheritDoc}.
         * 
         * @param obj The object to compare with.
         * @return <code>true</code> if the objects are equal, 
         *         otherwise <code>false</code>
         */
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (this == obj) return true;
            if (this.getClass() != obj.getClass()) return false;
            Criterion that = (Criterion) obj;
            if (!Objects.equals(this.key, that.key)) return false;
            return (!Objects.equals(this.values, that.values));
        }

        /**
         * Implemented to make the hash code consistent with the 
         * {@link #equals(Object)} implementation.
         * 
         * {@inheritDoc}
         * 
         * @return The hash code for this instance.
         */
        public int hashCode() {
            return Objects.hash(this.key, this.values);
        }

        /**
         * Adds the property and value (or values) to the specified
         * {@link JsonObjectBuilder}.  If multiple values are present
         * then a JSON array is used, otherwise a single JSON string
         * value is used to represent the value.
         * 
         * @param job The {@link JsonObjectBuilder} to which to add 
         *            this criterion.
         * 
         * @return The specified {@link JsonObjectBuilder} for method
         *         call chaining.
         *            
         */
        public JsonObjectBuilder toJson(JsonObjectBuilder job) {
            String key = this.getKey().toUpperCase();

            if (this.values.size() == 1) {
                job.add(key, this.values.iterator().next());

            } else {
                String pluralKey = (key.endsWith("S")) 
                    ? (key + "ES") : (key + "S");

                JsonArrayBuilder jab = Json.createArrayBuilder();
                for (String val : this.values) {
                    JsonObjectBuilder job2 = Json.createObjectBuilder();
                    job2.add(key, val);
                    jab.add(job2);
                }
                job.add(pluralKey, jab);
            }
            return job;
        }

        /**
         * Creates a JSON object representation of this instance and
         * returns it as a {@link String}.
         * 
         * @return The JSON-formatted {@link String} describing this
         *         instance.
         */
        public String toJson() {
            JsonObjectBuilder job = Json.createObjectBuilder();
            this.toJson(job);
            return toJsonText(job);
        }

        /**
         * Implemented to call {@link #toJson()}.
         * 
         * {@inheritDoc}
         * 
         * @return The JSON-formatted {@link String} describing this
         *         instance.
         */
        public String toString() {
            return this.toJson();
        }

        /**
         * Converts the specified criteria {@link Map} to a JSON.
         * 
         * @param criteria The {@link Map} of {@link String} keys to 
         *                 {@link Set}'s of {@link String} values.
         * @return The JSON representation of the criteria.
         */
        public static String toJson(Map<String, Set<String>> criteria) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            for (Map.Entry<String,Set<String>> entry : criteria.entrySet()) {
                String      key     = entry.getKey().toUpperCase();
                Set<String> values  = entry.getValue();
                if (values.size() == 0) continue;
                if (values.size() == 1) {
                    job.add(key, values.iterator().next());
                    continue;
                }
                String pluralKey = (key.endsWith("S")) 
                    ? (key + "ES") : (key + "S");
                JsonArrayBuilder jab = Json.createArrayBuilder();
                for (String value: values) {
                    JsonObjectBuilder job2 = Json.createObjectBuilder();
                    job2.add(key, value);
                    jab.add(job2);
                }
                job.add(pluralKey, jab);
            }
            return toJsonText(job);
        }
    }

    /**
     * Constructs a {@link Criterion} instance with the specified key
     * and the one or more values.
     *
     * @param key The key for the criterion.
     * @param value The value for the criterion.
     * @param otherValues The additional values for the criterion.
     * 
     * @throws NullPointerException If the specified key or any of the
     *                              values is <code>null</code>.
     */
    public static Criterion criterion(String    key, 
                                      String    value, 
                                      String... otherValues)
    {
        return new Criterion(key, value, otherValues);
    }

    /**
     * Constructs a criteria {@link Map} containing the specified
     * {@link String} key and the {@link Set} of one or more 
     * specified values.
     *
     * @param key The key for the criteria.
     * @param value The value for the criteria.
     * @param otherValues The additional values for the criteria.
     * 
     * @throws NullPointerException If the specified key or any of the
     *                              values is <code>null</code>.
     */
    static Map<String, Set<String>> criteria(String      key, 
                                             String      value,
                                             String...   otherValues) 
    {
        Criterion criterion = criterion(key, value, otherValues);
        return criteria(criterion);
    }

    /**
     * Constructs a criteria {@link Map} containing the keys from the
     * specified {@link Criterion} instances mapped to the cumulative
     * {@link Set} of values for those respective keys (combining values
     * from {@link Criterion} instanes that have the same key).
     *
     * @param criterion The first {@link Criterion}.
     * @param otherCriteria The additional {@link Criterion} instances.
     * 
     * @throws NullPointerException If any of the specified {@link Criterion}
     *                              instances is <code>null</cocde>.
     */
    static Map<String, Set<String>> criteria(Criterion      criterion, 
                                             Criterion...   otherCriteria) 
    {
        Map<String, Set<String>> result = new LinkedHashMap<>();
        result.put(criterion.getKey(), new LinkedHashSet<>(criterion.getValues()));
        if (otherCriteria != null) {
            for (Criterion c : otherCriteria) {
                String key = c.getKey();
                Set<String> values = result.get(key);
                if (values == null) {
                    result.put(key, new LinkedHashSet<>(c.getValues()));
                } else {
                    values.addAll(c.getValues());
                }
            }
        }
        return result;
    }

    /**
     * Convenience method for creating a new {@link Set} of {@link SzFlag}
     * from an existing {@link Set} of {@link SzFlag} instances and zero
     * or more additional {@link SzFlag} instances.
     * 
     * @param flagSet The initial {@link Set} {@link SzFlag} instances to copy.
     * @param addlFlags The zero or additional {@link SzFlag} instances to
     *                  be appended to the initial {@link Set}.
     * @return The new {@link Set} of {@link SzFlag} instances.
     */
    static Set<SzFlag> flags(Set<SzFlag> flagSet, SzFlag... addlFlags) {
        // start with an empty set of flags
        Set<SzFlag> result = EnumSet.noneOf(SzFlag.class);

        // check if the initial set is non-null
        if (flagSet != null) {
            // iterate through the initial flags
            for (SzFlag flag: flagSet) {
                // for each non-null flag, add to the result
                if (flag != null) {
                    result.add(flag);
                }
            }
        }

        // check if we have additional flags
        if (addlFlags != null) {
            // iterate over the flags
            for (SzFlag flag : addlFlags) {
                // for each non-null flag, add to the result
                if (flag != null) {
                    result.add(flag);
                }
            }
        }

        // return the result
        return result;
    }

    /**
     * Generates the parameters for the "search by attribtues" test.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>The search attributes JSON text as a {@link String}.
     *   <li>A {@link String} that is the search profile.
     *   <li>The {@link Set} of {@link SzFlag} instances for the test.
     *   <li>The expected number of search results.
     *   <li>The expected exception {@link Class}, or <code>null</code> if
     *       no exception expected.
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances.
     */
    default List<Arguments> getSearchParameters() {
        List<Arguments> result = new LinkedList<>();

        Map<Map<String, Set<String>>, Map<SzFlag, Integer>> searchCountMap
            = new LinkedHashMap<>();

        searchCountMap.put(criteria("PHONE_NUMBER", "702-555-1212"),
                           sortedMap(Map.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, 1)));

        searchCountMap.put(criteria("PHONE_NUMBER", "212-555-1212"),
                           sortedMap(Map.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, 1)));

        searchCountMap.put(criteria("PHONE_NUMBER", "818-555-1313"),
                           sortedMap(Map.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, 1)));

        searchCountMap.put(criteria("PHONE_NUMBER", "818-555-1212"),
                           sortedMap(Map.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, 1)));

        searchCountMap.put(
            criteria("PHONE_NUMBER", "818-555-1212", "818-555-1313"),
            sortedMap(Map.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, 2)));

        searchCountMap.put(
            criteria(criterion("ADDR_LINE1", "100 MAIN STREET"),
                     criterion("ADDR_CITY", "LOS ANGELES"),
                     criterion("ADDR_STATE", "CALIFORNIA"),
                     criterion("ADDR_POSTAL_CODE", "90012")),
            sortedMap(Map.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, 2)));

        searchCountMap.put(
            criteria(criterion("NAME_FULL", "JOHN DOE", "JANE DOE"),
                     criterion("ADDR_LINE1", "100 MAIN STREET"),
                     criterion("ADDR_CITY", "LOS ANGELES"),
                     criterion("ADDR_STATE", "CALIFORNIA"),
                     criterion("ADDR_POSTAL_CODE", "90012")),
            sortedMap(Map.of(SZ_SEARCH_INCLUDE_RESOLVED, 2)));

        searchCountMap.put(
            criteria(criterion("NAME_FULL", "JOHN DOE"),
                     criterion("ADDR_LINE1", "100 MAIN STREET"),
                     criterion("ADDR_CITY", "LOS ANGELES"),
                     criterion("ADDR_STATE", "CALIFORNIA"),
                     criterion("ADDR_POSTAL_CODE", "90012")),
            sortedMap(Map.of(SZ_SEARCH_INCLUDE_RESOLVED, 1,
                             SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, 1)));

        searchCountMap.put(
            criteria(criterion("NAME_FULL", "Mark Hightower"),
                     criterion("PHONE_NUMBER", "563-927-2833")),
            sortedMap(Map.of(SZ_SEARCH_INCLUDE_RESOLVED, 1,
                             SZ_SEARCH_INCLUDE_POSSIBLY_SAME, 1)));

        searchCountMap.put(
            criteria(criterion("NAME_FULL", "Mark Hightower"),
                     criterion("DATE_OF_BIRTH", "1981-03-22")),
            sortedMap(Map.of(SZ_SEARCH_INCLUDE_POSSIBLY_SAME, 1)));

        searchCountMap.put(
            criteria(criterion("NAME_FULL", "Mark Hightower"),
                     criterion("PHONE_NUMBER", "563-927-2833"),
                     criterion("PHONE_NUMBER", "781-332-2824"),
                     criterion("DATE_OF_BIRTH", "1981-06-22")),
            sortedMap(Map.of(SZ_SEARCH_INCLUDE_RESOLVED, 1,
                             SZ_SEARCH_INCLUDE_POSSIBLY_SAME, 1)));

        for (Set<SzFlag> flagSet : SEARCH_FLAG_SETS) {
            searchCountMap.forEach((criteria, countsMap) -> {
                int expectedCount   = 0;
                int totalCount      = 0;
                int flagCount       = 0;
                for (SzFlag flag : SEARCH_INCLUDE_FLAGS) {
                    if (countsMap.containsKey(flag)) {
                        totalCount += countsMap.get(flag);
                    }
                    if (flagSet == null || !flagSet.contains(flag)) continue;
                    flagCount++;
                    if (!countsMap.containsKey(flag)) continue;
                    expectedCount += countsMap.get(flag);
                }
                if (flagCount == 0) expectedCount = totalCount;

                String attributes = Criterion.toJson(criteria);

                if (result.size() == 0) {
                    result.add(Arguments.of(
                        attributes,
                        "BAD_SEARCH_PROFILE",
                        flagSet,
                        0,
                        SzBadInputException.class));
                }

                result.add(Arguments.of(
                    attributes,
                    null,
                    flagSet,
                    expectedCount,
                    null));

                result.add(Arguments.of(
                    attributes,
                    "SEARCH",
                    flagSet,
                    expectedCount,
                    null));

                result.add(Arguments.of(
                    attributes,
                    "INGEST",
                    flagSet,
                    expectedCount,
                    null));

            });
        }
        return result;
    }

    /**
     * Tests the "search-by-attributes" functionality.
     * 
     * @param attributes The attributes to use for the test.
     * @param searchProfile The search profile to use or <code>null</code>.
     * @param flags The {@link Set} of {@link SzFlag} instances to use.
     * @param expectedCount The expected search result count.
     * @param expectedExceptionType The expected exception type, or <code>null</code>
     *                              if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getSearchParameters")
    default void testSearchByAttributes(String      attributes,
                                        String      searchProfile,
                                        Set<SzFlag> flags,
                                        Integer     expectedCount,
                                        Class<?>    expectedExceptionType)
    {
        String testData = "description=[ " + attributes
            + " ], searchProfile=[ " + searchProfile + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedCount=[ "
            + expectedCount + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = null;
                if (searchProfile == null) {
                    result = engine.searchByAttributes(attributes, flags);
                } else {
                    result = engine.searchByAttributes(attributes,
                                                       searchProfile,
                                                       flags);
                }

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in searching: " + testData);
                }

                // parse the result
                JsonObject jsonObject = null;
                try {
                    jsonObject = parseJsonObject(result);

                } catch (Exception e) {
                    fail("Failed to parse search result JSON: " + testData 
                         + ", result=[ " + result + " ]", e);
                }

                JsonArray entities = getJsonArray(jsonObject, "RESOLVED_ENTITIES");
                assertNotNull(entities, "The RESOLVED_ENTITIES property was not found "
                    + "in the result.  " + testData + ", result=[ " + result + " ]");
                
                int actualCount = entities.size();
                assertEquals(expectedCount, actualCount,
                    "Unexpected number of search results.  " + testData
                    + ", entities=[ " + entities + " ]");
                
            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = "exception=[ " + e.toString() + " ]";
                }

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed search: " + testData + ", " + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "search failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });

    }
}
