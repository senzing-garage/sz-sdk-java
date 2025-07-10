package com.senzing.sdk.test;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashSet;
import java.util.IdentityHashMap;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonNumber;

import com.senzing.io.RecordReader.Format;
import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzRecordKeys;
import com.senzing.sdk.SzEntityIds;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzNotFoundException;

import static com.senzing.io.RecordReader.Format.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.util.CollectionUtilities.*;
import static com.senzing.sdk.test.SdkTest.*;
import static com.senzing.sdk.test.SzEngineGraphTest.TestData.*;

/**
 * Unit tests for {@link SzEngine} graph functionality.
 */
public interface SzEngineGraphTest extends SdkTest {
    /**
     * The test data class for the {@link SzEngineGraphTest}
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
         * The fake data source code for an unknown data source.
         */
        public static final String UNKNOWN_DATA_SOURCE = "UNKNOWN";

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * find-path test functions.
         */
        public static final List<Set<SzFlag>> FIND_PATH_FLAG_SET;

        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_FIND_PATH_DEFAULT_FLAGS);
            list.add(SZ_FIND_PATH_ALL_FLAGS);
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_FIND_PATH_INCLUDE_MATCHING_INFO,
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
            list.add(Collections.unmodifiableSet(
                EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
            FIND_PATH_FLAG_SET = Collections.unmodifiableList(list);
        }

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * find-network test functions.
         */
        public static final List<Set<SzFlag>> FIND_NETWORK_FLAG_SET;
        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_FIND_NETWORK_DEFAULT_FLAGS);
            list.add(SZ_FIND_NETWORK_ALL_FLAGS);
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO,
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
            list.add(Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
            FIND_NETWORK_FLAG_SET = Collections.unmodifiableList(list);
        }

        public static final SzRecordKey PASSENGER_ABC123
            = SzRecordKey.of(PASSENGERS, "ABC123");
        
        public static final SzRecordKey PASSENGER_DEF456
            = SzRecordKey.of(PASSENGERS, "DEF456");

        public static final SzRecordKey PASSENGER_GHI789 
            = SzRecordKey.of(PASSENGERS, "GHI789");

        public static final SzRecordKey PASSENGER_JKL012
            = SzRecordKey.of(PASSENGERS, "JKL012");

        public static final SzRecordKey EMPLOYEE_MNO345
            = SzRecordKey.of(EMPLOYEES, "MNO345");

        public static final SzRecordKey EMPLOYEE_PQR678
            = SzRecordKey.of(EMPLOYEES, "PQR678");

        public static final SzRecordKey EMPLOYEE_ABC567
            = SzRecordKey.of(EMPLOYEES, "ABC567");
            
        public static final SzRecordKey EMPLOYEE_DEF890
            = SzRecordKey.of(EMPLOYEES, "DEF890");

        public static final SzRecordKey VIP_STU901
            = SzRecordKey.of(VIPS, "STU901");

        public static final SzRecordKey VIP_XYZ234
            = SzRecordKey.of(VIPS, "XYZ234");

        public static final SzRecordKey VIP_GHI123
            = SzRecordKey.of(VIPS, "GHI123");

        public static final SzRecordKey VIP_JKL456
            = SzRecordKey.of(VIPS, "JKL456");

        public static final List<SzRecordKey> RECORD_KEYS
            = list(PASSENGER_ABC123,
                   PASSENGER_DEF456,
                   PASSENGER_GHI789,
                   PASSENGER_JKL012,
                   EMPLOYEE_MNO345,
                   EMPLOYEE_PQR678,
                   EMPLOYEE_ABC567,
                   EMPLOYEE_DEF890,
                   VIP_STU901,
                   VIP_XYZ234,
                   VIP_GHI123,
                   VIP_JKL456);

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
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
                "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH"};

            String[][] passengers = {
                {PASSENGER_ABC123.recordId(), "Joseph", "Schmidt", "213-555-1212", "818-777-2424",
                    "101 Main Street, Los Angeles, CA 90011", "12-JAN-1981"},
                {PASSENGER_DEF456.recordId(), "Joann", "Smith", "213-555-1212", "818-888-3939",
                    "101 Fifth Ave, Los Angeles, CA 90018", "15-MAR-1982"},
                {PASSENGER_GHI789.recordId(), "John", "Parker", "818-555-1313", "818-999-2121",
                    "101 Fifth Ave, Los Angeles, CA 90018", "17-DEC-1977"},
                {PASSENGER_JKL012.recordId(), "Jane", "Donaldson", "818-555-1313", "818-222-3131",
                    "400 River Street, Pasadena, CA 90034", "23-MAY-1973"}
            };
            return prepareDataFile(
                format, "test-passengers-", headers, passengers);
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
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
                "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH"};

            String[][] employees = {
                {EMPLOYEE_MNO345.recordId(), "Bill", "Bandley", "818-444-2121", "818-123-4567",
                    "101 Main Street, Los Angeles, CA 90011", "22-AUG-1981"},
                {EMPLOYEE_PQR678.recordId(), "Craig", "Smith", "818-555-1212", "818-888-3939",
                    "451 Dover Street, Los Angeles, CA 90018", "17-OCT-1983"},
                {EMPLOYEE_ABC567.recordId(), "Kim", "Long", "818-246-8024", "818-135-7913",
                    "451 Dover Street, Los Angeles, CA 90018", "24-NOV-1975"},
                {EMPLOYEE_DEF890.recordId(), "Katrina", "Osmond", "818-444-2121", "818-111-2222",
                    "707 Seventh Ave, Los Angeles, CA 90043", "27-JUN-1980"}
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
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
                "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH"};

            String[][] vips = {
                {VIP_STU901.recordId(), "Martha", "Wayne", "818-891-9292", "818-987-1234",
                    "888 Sepulveda Blvd, Los Angeles, CA 90034", "27-NOV-1973"},
                {VIP_XYZ234.recordId(), "Jane", "Johnson", "818-333-7171", "818-123-9876",
                    "400 River Street, Pasadena, CA 90034", "6-SEP-1975"},
                {VIP_GHI123.recordId(), "Martha", "Kent", "818-333-5757", "818-123-9876",
                    "888 Sepulveda Blvd, Los Angeles, CA 90034", "17-AUG-1978"},
                {VIP_JKL456.recordId(), "Kelly", "Rogers", "818-333-7171", "818-789-6543",
                    "707 Seventh Ave, Los Angeles, CA 90043", "15-JAN-1979"}
            };

            return prepareDataFile(format, "test-vips-", headers, vips);
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
            loader.configureDataSources(PASSENGERS, EMPLOYEES, VIPS);
            Map<String, File> fileMap = new LinkedHashMap<>();
            
            fileMap.put(PASSENGERS, preparePassengerFile(CSV));
            fileMap.put(EMPLOYEES, prepareEmployeeFile(JSON));
            fileMap.put(VIPS, prepareVipFile(JSON_LINES));

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
     * Gets the {@link List} of entity ID's for the specified
     * {@link Collection} of {@link SzRecordKey} instances with
     * the respective entity ID's for each {@link SzRecordKey}
     * in the same order as the specified {@link Collection}.
     * 
     * @param recordKeys The {@link Collection} of {@link SzRecordKey}
     *                   instances identifying the records.
     * 
     * @return The {@link List} of entity ID's for the specified
     *         {@link Collection} of {@link SzRecordKey}'s.
     */
    default List<Long> getEntityIds(Collection<SzRecordKey> recordKeys) {
        if (recordKeys == null) return null;
        List<Long> result = new ArrayList<>(recordKeys.size());
        for (SzRecordKey recordKey : recordKeys) {
            result.add(this.getEntityId(recordKey));
        }
        return result;
    }

    /**
     * Gets the {@link Set} of entity ID's for the specified
     * {@link Set} of {@link SzRecordKey} instances.
     * 
     * @param recordKeys The {@link Set} of {@link SzRecordKey}
     *                   instances identifying the records.
     * 
     * @return The {@link Set} of entity ID's for the specified
     *         {@link Set} of {@link SzRecordKey}'s.
     */
    default Set<Long> entityIdSet(Set<SzRecordKey> recordKeys) {
        Set<Long> result = new LinkedHashSet<>();
        for (SzRecordKey key: recordKeys) {
            result.add(this.getEntityId(key));
        }
        return result;
    }

    /**
     * Validates an entity path versus expected result.
     * 
     * @param pathJson The JSON string describing the entity path.
     * @param testData The test data describing the test in case of failure.
     * @param startRecordKey The expected starting {@link SzRecordKey}.
     * @param endRecordKey The expected ending {@link SzRecordKey}.
     * @param maxDegrees The maximum number of degrees allowed for the path.
     * @param avoidances The {@link Set} of {@link SzRecordKey} instances 
     *                   that must be avoided, or <code>null</code> if none.
     * @param requiredSources The {@link Set} of {@link String} data source
     *                        codes required, or <code>null</code> if none.
     * @param flags The specified flags for the test.
     * @param expectedPathLength The expected path length of the path, or
     *                           <code>null</code> if none.
     * @param expectedPath The {@link List} of {@link SzRecordKey} instances
     *                     describing the expected path in order.
     */
    default void validatePath(String             pathJson,
                              String             testData,
                              SzRecordKey        startRecordKey,
                              SzRecordKey        endRecordKey,
                              int                maxDegrees,
                              Set<SzRecordKey>   avoidances,
                              Set<String>        requiredSources,
                              Set<SzFlag>        flags,
                              Integer            expectedPathLength,
                              List<SzRecordKey>  expectedPath)
    {
        JsonObject jsonObject = null;
        try {
            jsonObject = parseJsonObject(pathJson);
        } catch (Exception e) {
            fail("Unable to parse find-path result as JSON: " + pathJson);
        }

        JsonArray entityPaths = getJsonArray(jsonObject, "ENTITY_PATHS");
                
        assertNotNull(entityPaths, "Entity path is missing: " + testData);

        assertEquals(1, entityPaths.size(),
                     "Paths array has unexpected length: paths=[ " + entityPaths 
                     + " ], " + testData);
        
        JsonObject path0 = getJsonObject(entityPaths, 0);

        assertNotNull(path0, "Entity path was null: paths=[ " + entityPaths
                      + " ], " + testData);
        
        JsonArray entityIds = getJsonArray(path0, "ENTITIES");

        JsonArray pathLinks = getJsonArray(jsonObject, "ENTITY_PATH_LINKS");
        if (flags != null && flags.contains(SZ_FIND_PATH_INCLUDE_MATCHING_INFO)) {
            assertNotNull(pathLinks, "Entity path links missing or null: "
                          + "pathLinks=[ " + pathLinks + " ], " + testData);
        } else {
            assertNull(pathLinks, "Entity path links present when not requested.  "
                       + "pathLinks=[ " + pathLinks + " ], " + testData);
        }

        JsonArray entities = getJsonArray(jsonObject, "ENTITIES");

        assertNotNull(entities,
                      "Entity details for path are missing: " + testData);

        // validate the path length
        assertEquals(expectedPathLength, entityIds.size(),
                     "Path is not of expected length: path=[ "
                     + entityIds + "], " + testData);
        
        Long        startEntityId   = this.getEntityId(startRecordKey);
        Long        endEntityId     = this.getEntityId(endRecordKey);
        List<Long>  avoidedIds      = this.getEntityIds(avoidances);
        List<Long>  expectedIds     = this.getEntityIds(expectedPath);
        List<Long>  actualPathIds   = new ArrayList<>(entityIds.size());

        JsonNumber prevId = null;
        for (JsonNumber jsonNum: entityIds.getValuesAs(JsonNumber.class)) {
            long entityId = jsonNum.longValue();
            
            if (prevId == null) {
                assertEquals(startEntityId, entityId,
                             "The starting entity ID in the path is not as expected: "
                            + "entityIds=[ " + entityIds + " ], " + testData);             

            }

            SzEntityLookup lookup = this.getTestData().getEntityLookup();
            Map<Long,Set<SzRecordKey>> loadedEntityMap = lookup.getMapByEntityId();

            if (avoidances != null && avoidances.size() > 0
                && ((flags != null) && flags.contains(SZ_FIND_PATH_STRICT_AVOID))) 
            {
                assertFalse(avoidedIds.contains(entityId), 
                            "Strictly avoided entity ID (" + entityId + ") found "
                            + "in path: entityIds=[ " + entityIds 
                            + " ], recordKeys=[ " +  loadedEntityMap.get(entityId)
                            + " ], " + testData);
            }

            // create the path list
            actualPathIds.add(entityId);
            prevId = jsonNum;
        }

        // assert the end of the path is as expected
        if (prevId != null) {
            assertEquals(endEntityId, prevId.longValue(),
                         "The ending entity ID (" + prevId.longValue() 
                         + ") in the path is not as expected: " + testData);
        }

        // now check the path is as expected
        assertEquals(expectedIds, actualPathIds,
                     "Entity path is not as expected: path=[ " + entityPaths
                     + " ], " + testData);

        // add the start and end entity ID to the ID set
        actualPathIds.add(startEntityId);
        actualPathIds.add(endEntityId);
        
        // check that the entities we found are on the path
        Set<Long> detailEntityIds = new HashSet<>();
        for (JsonObject entity : entities.getValuesAs(JsonObject.class)) {
            assertNotNull(entity, "Entity from path was null: "
                          + entities + ", " + testData);

            entity = getJsonObject(entity, "RESOLVED_ENTITY");
            assertNotNull(entity, "Resolved entity from path was null: "
                          + entities + ", " + testData);
            
            // get the entity ID
            Long id = getLong(entity, "ENTITY_ID");
            assertNotNull(
                id, "The entity detail from path was missing or "
                + "null ENTITY_ID: " + entity + ", " + testData);

            // ensure the entity ID is expected
            assertTrue(actualPathIds.contains(id),
                       "Entity (" + id + ") returned that is not in "
                       + "the path: entity=[ " + entity + " ], pathIds=[ "
                       + actualPathIds + " ], " + testData);
            
            // add to the ID set
            detailEntityIds.add(id);
        }

        // check that all the path ID's have details
        for (Long id : actualPathIds) {
            assertTrue(
                detailEntityIds.contains(id),
                 "A path entity (" + id + ") was missing from entity "
                 + "details: entities=[ " + entities + " ], " 
                 + testData);
        }

        SzEntityLookup lookup = this.getTestData().getEntityLookup();
        // validate the required data sources
        if (requiredSources != null && requiredSources.size() > 0) {
            boolean sourcesSatisified = false;
            for (Long entityId : actualPathIds) {
                if (entityId.equals(startEntityId)) continue;
                if (entityId.equals(endEntityId)) continue;
                Set<SzRecordKey> keys = lookup.getMapByEntityId().get(entityId);
                for (SzRecordKey key: keys) {
                    if (requiredSources.contains(key.dataSourceCode())) {
                        sourcesSatisified = true;
                        break;
                    }
                }
                if (sourcesSatisified) break;
            }
            if (!sourcesSatisified) {
                fail("Entity path does not contain required data sources: "
                     + "entityPath=[ " + actualPathIds + " ], " + testData);
            }
        }
    }

    /**
     * Convenience method for creating a new {@link Set} of {@link SzFlag}
     * from an existing {@link Set} of {@link SzFlag} instances and zero
     * or more additional {@link SzFlag} instances.
     * 
     * @param baseFlags The initial {@link Set} {@link SzFlag} instances to copy.
     * @param otherFlags The zero or additional {@link SzFlag} instances to
     *                   be appended to the initial {@link Set}.
     * @return The new {@link Set} of {@link SzFlag} instances.
     */
    public static Set<SzFlag> flagsWith(Set<SzFlag> baseFlags,
                                        SzFlag...   otherFlags) 
    {
        EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);
        if (baseFlags != null) set.addAll(baseFlags);
        for (SzFlag flag : otherFlags) {
            set.add(flag);
        }
        if (set.size() == 0 && baseFlags == null) return null;
        return set;
    }

    /**
     * Convenience method for creating a new {@link Set} of {@link SzFlag}
     * from an existing {@link Set} of {@link SzFlag} instances 
     * <b>excluding</b> zero or more other {@link SzFlag} instances.
     * 
     * @param baseFlags The initial {@link Set} {@link SzFlag} instances to copy.
     * @param otherFlags The zero or additional {@link SzFlag} instances to
     *                   be excluded from the initial {@link Set}.
     * @return The new {@link Set} of {@link SzFlag} instances.
     */
    public static Set<SzFlag> flagsWithout(Set<SzFlag>  baseFlags, 
                                           SzFlag...    otherFlags) 
    {
        if (baseFlags == null) return null;
        EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);
        set.addAll(baseFlags);
        for (SzFlag flag : otherFlags) {
            set.remove(flag);
        }
        return set;
    }

    /**
     * Convenience method for creating a new {@link Set} of {@link SzFlag}
     * from an existing {@link Set} of {@link SzFlag} instances and adding
     * the {@link SzFlag#SZ_FIND_PATH_STRICT_AVOID} flag.
     * 
     * @param baseFlags The initial {@link Set} {@link SzFlag} instances.
     * @return The new {@link Set} of {@link SzFlag} instances.
     */
    public static Set<SzFlag> flagsWithStrictAvoid(Set<SzFlag> baseFlags) {
        return flagsWith(baseFlags, SZ_FIND_PATH_STRICT_AVOID);
    }

    /**
     * Convenience method for creating a new {@link Set} of {@link SzFlag}
     * from an existing {@link Set} of {@link SzFlag} instances and excluding
     * the {@link SzFlag#SZ_FIND_PATH_STRICT_AVOID} flag.
     * 
     * @param baseFlags The initial {@link Set} {@link SzFlag} instances.
     * @return The new {@link Set} of {@link SzFlag} instances.
     */
    public static Set<SzFlag> flagsWithDefaultAvoid(Set<SzFlag> baseFlags) {
        return flagsWithout(baseFlags, SZ_FIND_PATH_STRICT_AVOID);
    }

    /**
     * Generates the parameters for the "find path" tests.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>A {@link String} description of the test.
     *   <li>The {@link SzRecordKey} for a record in the
     *       starting entity for the path.
     *   <li>The {@link Long} entity ID for the starting
     *       entity for the path.
     *   <li>The {@link SzRecordKey} for a record in the
     *       ending entity for the path.
     *   <li>The {@link Long} entity ID for the ending
     *       entity for the path.
     *   <li>The {@link Integer} for the maximum degrees of separation.
     *   <li>The {@link Set} of {@link SzRecordKey}'s to avoid for 
     *       the path, or <code>null</code> if none.
     *   <li>The {@link Set} of {@link Long} entity ID's to avoid
     *        for the path, or <code>null</code> if none.
     *   <li>The {@link Set} of {@link String} required data sources
     *       for the path.
     *   <li>The {@link Set} of {@link SzFlag} instances for the test.
     *   <li>The {@link Class} of the expected exception if finding the
     *       path by record key, or <code>null</code> if no exception
     *       is expected. 
     *   <li>The {@link Class} of the expected exception if finding the
     *       path by entity ID, or <code>null</code> if no exception
     *       is expected. 
     *   <li>The {@link Integer} for the expected length of the resulting
     *       entity path, or <code>null</code> if this is not being 
     *       validated.
     *   <li>The {@link List} of {@link SzRecordKey} instances describing
     *       the expected entity path in order, or <code>null</code> if
     *       this is not being validated.
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances.
     */
    default List<Arguments> getEntityPathParameters() {
        Iterator<Set<SzFlag>>   flagSetIter = circularIterator(FIND_PATH_FLAG_SET);

        List<Arguments> result = new LinkedList<>();
        
        final List<SzRecordKey> EMPTY_PATH = Collections.emptyList();

        final Class<?> UNKNOWN_SOURCE = SzUnknownDataSourceException.class;
        final Class<?> NOT_FOUND = SzNotFoundException.class;

        result.add(Arguments.of(
            "Basic path find at 2 degrees",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            EMPLOYEE_DEF890, this.getEntityId(EMPLOYEE_DEF890), 2, null, 
            null, null, flagSetIter.next(), null, null, 3,
            list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890)));
        
        result.add(Arguments.of(
            "Basic path found at 3 degrees",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            VIP_JKL456, this.getEntityId(VIP_JKL456), 3, null, null, null,
            flagSetIter.next(), null, null, 4,
            list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890, VIP_JKL456)));

        result.add(Arguments.of(
            "Path not found due to max degrees",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            VIP_JKL456, this.getEntityId(VIP_JKL456), 2, null, null, null,
            flagSetIter.next(), null, null, 0, EMPTY_PATH));
        
        result.add(Arguments.of(
            "Diverted path found with avoidance",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            VIP_JKL456, this.getEntityId(VIP_JKL456), 4, set(EMPLOYEE_DEF890),
            set(this.getEntityId(EMPLOYEE_DEF890)), null,
            flagsWithDefaultAvoid(flagSetIter.next()), null, null,
            4, list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890, VIP_JKL456)));

        result.add(Arguments.of(
            "No path found due to strict avoidance and max degrees",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            VIP_JKL456, this.getEntityId(VIP_JKL456), 3, set(EMPLOYEE_DEF890),
            set(this.getEntityId(EMPLOYEE_DEF890)), null,
            flagsWithStrictAvoid(flagSetIter.next()), null, null, 0,
            EMPTY_PATH));
        
        result.add(Arguments.of(
            "Diverted path at 5 degrees with strict avoidance",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            VIP_JKL456, this.getEntityId(VIP_JKL456), 10, set(EMPLOYEE_DEF890),
            set(this.getEntityId(EMPLOYEE_DEF890)), null,
            flagsWithStrictAvoid(flagSetIter.next()),  null, null, 6,
            list(PASSENGER_ABC123, PASSENGER_DEF456, PASSENGER_GHI789,
                    PASSENGER_JKL012, VIP_XYZ234, VIP_JKL456)));
        
        result.add(Arguments.of(
            "Diverted path at 5 degrees due to required EMPLOYEES source",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012), 10, null, null,
            set(EMPLOYEES), flagsWithDefaultAvoid(flagSetIter.next()),
            null, null, 6, list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890,
                                VIP_JKL456, VIP_XYZ234, PASSENGER_JKL012)));
        
        result.add(Arguments.of(
            "Diverted path at 5 degrees due to required VIP source",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012), 
            10, null, null, set(VIPS),
            flagsWithDefaultAvoid(flagSetIter.next()), null, null, 6,
            list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890,
                    VIP_JKL456, VIP_XYZ234, PASSENGER_JKL012)));
        
        result.add(Arguments.of(
            "Diverted path at 5 degrees due to 2 required sources",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012), 
            10, null, null, set(EMPLOYEES, VIPS),
            flagsWithDefaultAvoid(flagSetIter.next()), null, null, 6,
            list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890,
                    VIP_JKL456, VIP_XYZ234, PASSENGER_JKL012)));

        result.add(Arguments.of(
            "Diverted path with required sources and avoidance",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012), 
            10, set(VIP_STU901), set(this.getEntityId(VIP_STU901)),
            set(EMPLOYEES, VIPS),
            flagsWithDefaultAvoid(flagSetIter.next()), null, null, 6,
            list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890,
                    VIP_JKL456, VIP_XYZ234, PASSENGER_JKL012)));

        result.add(Arguments.of(
            "Unknown required data source",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            EMPLOYEE_DEF890, this.getEntityId(EMPLOYEE_DEF890), 10, null, 
            null, set(UNKNOWN_DATA_SOURCE), flagSetIter.next(), 
            UNKNOWN_SOURCE, UNKNOWN_SOURCE, 0, EMPTY_PATH));
        
        result.add(Arguments.of(
            "Unknown source for avoidance record",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            VIP_JKL456, this.getEntityId(VIP_JKL456), 4,
            set(SzRecordKey.of(UNKNOWN_DATA_SOURCE, "DEF890")),
            set(-300L), null, flagsWithDefaultAvoid(flagSetIter.next()),
            UNKNOWN_SOURCE, null, 4, list(PASSENGER_ABC123, EMPLOYEE_MNO345,
                                          EMPLOYEE_DEF890, VIP_JKL456)));

        result.add(Arguments.of(
            "Not found record ID for avoidance record",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            VIP_JKL456, this.getEntityId(VIP_JKL456), 4,
            set(SzRecordKey.of(PASSENGERS, "XXX000")),
            set(300000000L), null, flagsWithDefaultAvoid(flagSetIter.next()),
            null, null, 4, list(PASSENGER_ABC123, EMPLOYEE_MNO345,
                                EMPLOYEE_DEF890, VIP_JKL456)));
                
        result.add(Arguments.of(
                "Unknown start data source in find path via key",
                SzRecordKey.of(UNKNOWN_DATA_SOURCE, "ABC123"), -100L,
                PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012),
                10, null, null, null, flagSetIter.next(), 
                UNKNOWN_SOURCE, NOT_FOUND, 0, EMPTY_PATH));
                
        result.add(Arguments.of(
                "Unknown end data source in find path via key",
                PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
                SzRecordKey.of(UNKNOWN_DATA_SOURCE, "JKL012"), -200L,
                10, null, null, null, flagSetIter.next(), 
                UNKNOWN_SOURCE, NOT_FOUND, 0, EMPTY_PATH));
        
        result.add(Arguments.of(
                "Unknown start record ID in find path via key",
                SzRecordKey.of(PASSENGERS, "XXX000"),
                100000000L, PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012),
                10, null, null, null, flagSetIter.next(), 
                NOT_FOUND, NOT_FOUND, 0, EMPTY_PATH));

        result.add(Arguments.of(
                "Unknown end record ID in find path via key",
                PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
                SzRecordKey.of(PASSENGERS, "XXX000"),
                200000000L, 10, null, null, null, flagSetIter.next(), 
                NOT_FOUND, NOT_FOUND, 0, EMPTY_PATH));
        
        return result;
    }

    /**
     * Tests finding an entity path by record ID.
     * 
     * @param testDescription The description of the test.
     * @param startRecordKey The {@link SzRecordKey} identifying the
     *                       starting entity for the path.
     * @param startEntityId The entity ID identifying the starting
     *                      entity for the path.
     * @param endRecordKey The {@link SzRecordKey} identifying the
     *                     ending entity for the path.
     * @param endEntityId The entity ID identifying the ending entity
     *                    for the path.
     * @param maxDegrees The maximum number of degrees of separation
     *                   for the path.
     * @param avoidances The {@link Set} of {@link SzRecordKey} instances
     *                   identifying the entities to be avoided, or 
     *                   <code>null</code> if none are to be avoided.
     * @param avoidanceIds The {@link Set} of {@link Long} entity ID's
     *                     identifying the entities to be avoided, or
     *                     <code>null</code> if none are to be avoided.
     * @param requiredSources The {@link Set} of {@link String} data source
     *                        codes that are required to be included for the
     *                        path, or <code>null</code> if no such requirement.
     * @param flags The {@link Set} of {@link SzFlag} instances for the test.
     * @param recordExceptionType The {@link Class} of the exception type that
     *                            is expected when getting the path by record ID,
     *                            or <code>null</code> if no exception is expected.
     * @param entityExceptionType The {@link Class} of the exception type that
     *                            is expected when getting the path by entity ID,
     *                            or <code>null</code> if no exception is expected.
     * @param expectedPathLength The expected length of the path, or <code>null</code>
     *                           if not validating the length of the path.
     * @param expectedPath The {@link List} of in-order {@link SzRecordKey} instances
     *                     identifying the entities for the path, or <code>null</code>
     *                     if this is not being validated.
     */
    @ParameterizedTest
    @MethodSource("getEntityPathParameters")
    default void testFindPathByRecordId(
        String              testDescription,
        SzRecordKey         startRecordKey,
        long                startEntityId,
        SzRecordKey         endRecordKey,
        long                endEntityId,
        int                 maxDegrees,
        Set<SzRecordKey>    avoidances,
        Set<Long>           avoidanceIds,
        Set<String>         requiredSources,
        Set<SzFlag>         flags,
        Class<?>            recordExceptionType,
        Class<?>            entityExceptionType,
        Integer             expectedPathLength,
        List<SzRecordKey>   expectedPath) 
    {
        StringBuilder sb = new StringBuilder(
            "description=[ " + testDescription + " ], startRecordKey=[ "
            + startRecordKey + " ], startRecordId=[ " + startEntityId 
            + " ] endRecordKey=[ " + endRecordKey + " ], endRecordId=[ "
            + endEntityId + " ], maxDegrees=[ " + maxDegrees + " ], avoidances=[ "
            + avoidances + " ], avoidanceIds=[ " + avoidanceIds 
            + " ] requiredSources=[ " + requiredSources + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedException=[ "
            + recordExceptionType + " ], expectedPathLength=[ "
            + expectedPathLength + " ], expectedPath=[ ");

        SzEntityLookup lookup = this.getTestData().getEntityLookup();
        
        String prefix = "";
        for (SzRecordKey key : expectedPath) {
            sb.append(prefix).append(key).append(" (");
            sb.append(this.getEntityId(key)).append(")");
            prefix = ", ";
        }
        sb.append(" ], recordMap=[ ").append(lookup.getMapByRecordKey()).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.findPath(
                        startRecordKey, 
                        endRecordKey, 
                        maxDegrees,
                        SzRecordKeys.of(avoidances),
                        requiredSources,
                        flags);

                if (recordExceptionType != null) {
                    fail("Unexpectedly succeeded in finding a path: " + testData);
                }

                this.validatePath(result, 
                                  testData,
                                  startRecordKey,
                                  endRecordKey,
                                  maxDegrees,
                                  avoidances,
                                  requiredSources,
                                  flags,
                                  expectedPathLength,
                                  expectedPath);


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
                    fail("Unexpectedly failed finding an entity path: "
                         + testData + ", " + description, e);

                } else if (recordExceptionType != e.getClass()) {
                    assertInstanceOf(
                        recordExceptionType, e, 
                        "findPath() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });

    }

    /**
     * Tests finding an entity path by entity ID.
     * 
     * @param testDescription The description of the test.
     * @param startRecordKey The {@link SzRecordKey} identifying the
     *                       starting entity for the path.
     * @param startEntityId The entity ID identifying the starting
     *                      entity for the path.
     * @param endRecordKey The {@link SzRecordKey} identifying the
     *                     ending entity for the path.
     * @param endEntityId The entity ID identifying the ending entity
     *                    for the path.
     * @param maxDegrees The maximum number of degrees of separation
     *                   for the path.
     * @param avoidances The {@link Set} of {@link SzRecordKey} instances
     *                   identifying the entities to be avoided, or 
     *                   <code>null</code> if none are to be avoided.
     * @param avoidanceIds The {@link Set} of {@link Long} entity ID's
     *                     identifying the entities to be avoided, or
     *                     <code>null</code> if none are to be avoided.
     * @param requiredSources The {@link Set} of {@link String} data source
     *                        codes that are required to be included for the
     *                        path, or <code>null</code> if no such requirement.
     * @param flags The {@link Set} of {@link SzFlag} instances for the test.
     * @param recordExceptionType The {@link Class} of the exception type that
     *                            is expected when getting the path by record ID,
     *                            or <code>null</code> if no exception is expected.
     * @param entityExceptionType The {@link Class} of the exception type that
     *                            is expected when getting the path by entity ID,
     *                            or <code>null</code> if no exception is expected.
     * @param expectedPathLength The expected length of the path, or <code>null</code>
     *                           if not validating the length of the path.
     * @param expectedPath The {@link List} of in-order {@link SzRecordKey} instances
     *                     identifying the entities for the path, or <code>null</code>
     *                     if this is not being validated.
     */
    @ParameterizedTest
    @MethodSource("getEntityPathParameters")
    default void testFindPathByEntityId(
        String              testDescription,
        SzRecordKey         startRecordKey,
        long                startEntityId,
        SzRecordKey         endRecordKey,
        long                endEntityId,
        int                 maxDegrees,
        Set<SzRecordKey>    avoidances,
        Set<Long>           avoidanceIds,
        Set<String>         requiredSources,
        Set<SzFlag>         flags,
        Class<?>            recordExceptionType,
        Class<?>            entityExceptionType,
        int                 expectedPathLength,
        List<SzRecordKey>   expectedPath) 
    {
        StringBuilder sb = new StringBuilder(
            "description=[ " + testDescription + " ], startRecordKey=[ "
            + startRecordKey + " ], startRecordId=[ " + startEntityId 
            + " ] endRecordKey=[ " + endRecordKey + " ], endRecordId=[ "
            + endEntityId + " ], maxDegrees=[ " + maxDegrees + " ], avoidances=[ "
            + avoidances + " ], avoidanceIds=[ " + avoidanceIds 
            + " ] requiredSources=[ " + requiredSources + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedException=[ "
            + entityExceptionType + " ], expectedPathLength=[ "
            + expectedPathLength + " ], expectedPath=[ ");

        SzEntityLookup lookup = this.getTestData().getEntityLookup();

        String prefix = "";
        for (SzRecordKey key : expectedPath) {
            sb.append(prefix).append(key).append(" (");
            sb.append(this.getEntityId(key)).append(")");
            prefix = ", ";
        }
        sb.append(" ], recordMap=[ ").append(lookup.getMapByRecordKey()).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.findPath(
                        startEntityId, 
                        endEntityId, 
                        maxDegrees,
                        SzEntityIds.of(avoidanceIds),
                        requiredSources,
                        flags);

                if (entityExceptionType != null) {
                    fail("Unexpectedly succeeded in finding a path: " + testData);
                }

                this.validatePath(result, 
                                  testData,
                                  startRecordKey,
                                  endRecordKey,
                                  maxDegrees,
                                  avoidances,
                                  requiredSources,
                                  flags,
                                  expectedPathLength,
                                  expectedPath);


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
                    fail("Unexpectedly failed finding an entity path: "
                         + testData + ", " + description, e);

                } else if (recordExceptionType != e.getClass()) {
                    assertInstanceOf(
                        entityExceptionType, e, 
                        "findPath() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }

    /**
     * Generates the parameters for the "find network" tests.
     * 
     * <p>
     * The parameter values are:
     * <ol>
     *   <li>A {@link String} description of the test.
     *   <li>The {@link Set} of {@link SzRecordKey} instances for
     *       the network.
     *   <li>The {@link Set} of {@link Long} entity ID's for the
     *       network.
     *   <li>The {@link Integer} for the max degrees of separation
     *       for each entity path.
     *   <li>The {@link Integer} for the number of degrees to 
     *       build-out the network.
     *   <li>The {@link Integer} that is the maximum number of 
     *       entities to include in the network build-out step.
     *   <li>The {@link Set} of {@link SzFlag} instances for the test.
     *   <li>The {@link Class} of the expected exception if finding the
     *       network by record key, or <code>null</code> if no exception
     *       is expected. 
     *   <li>The {@link Class} of the expected exception if finding the
     *       network by entity ID, or <code>null</code> if no exception
     *       is expected. 
     *   <li>The non-null {@link Integer} that is the expected number
     *       of entity paths for the network.
     *   <li>The {@link List} of {@link List}'s of {@link SzRecordKey}
     *       instances describing the expected entity paths, or 
     *       <code>null</code> if this is not being validated.
     *   <li>The {@link Set} of {@link SzRecordKey} instances for those
     *       entities that must exist in the network, or <code>null</code>
     *       if none are being validated.
     * </ol>
     * 
     * @return The {@link List} of {@link Argument} instances.
     */
    @SuppressWarnings("unchecked")
    default List<Arguments> getEntityNetworkParameters() {
        Iterator<Set<SzFlag>> flagSetIter = circularIterator(FIND_NETWORK_FLAG_SET);

        List<Arguments> result = new LinkedList<>();
        
        final List<List<SzRecordKey>>   NO_PATHS    = Collections.emptyList();
        final Set<SzRecordKey>          NO_ENTITIES = Collections.emptySet();

        final Class<?> UNKNOWN_SOURCE = SzUnknownDataSourceException.class;
        final Class<?> NOT_FOUND = SzNotFoundException.class;

        result.add(Arguments.of(
            "Single entity network",
            set(PASSENGER_ABC123), set(this.getEntityId(PASSENGER_ABC123)),
            1, 0, 10, flagSetIter.next(), null, null, 0,
            NO_PATHS, set(PASSENGER_ABC123)));

        result.add(Arguments.of(
            "Single entity with one-degree build-out",
            set(PASSENGER_ABC123), entityIdSet(set(PASSENGER_ABC123)),
            1, 1, 1000, flagSetIter.next(), null, null, 0, 
            NO_PATHS, set(PASSENGER_ABC123, PASSENGER_DEF456, EMPLOYEE_MNO345)));

        result.add(Arguments.of(
            "Two entities with no path",
            set(PASSENGER_ABC123,VIP_JKL456), entityIdSet(set(PASSENGER_ABC123,VIP_JKL456)),
  
              1, 0, 10, flagSetIter.next(), null, null, 1, 
            list(list(null, PASSENGER_ABC123, VIP_JKL456)), 
            set(PASSENGER_ABC123, VIP_JKL456)));
            
        result.add(Arguments.of(
            "Two entities at three degrees",
            set(PASSENGER_ABC123,VIP_JKL456), entityIdSet(set(PASSENGER_ABC123,VIP_JKL456)),
            3, 0, 10, flagSetIter.next(), null, null, 1, 
            list(list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890, VIP_JKL456)), 
            set(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890, VIP_JKL456)));

        result.add(Arguments.of(
            "Three entities at three degrees with no build-out",
            set(PASSENGER_ABC123,EMPLOYEE_ABC567,VIP_JKL456),
            entityIdSet(set(PASSENGER_ABC123,EMPLOYEE_ABC567,VIP_JKL456)),
            3, 0, 10, flagSetIter.next(), null, null, 3,
            list(list(PASSENGER_ABC123,EMPLOYEE_MNO345,EMPLOYEE_DEF890,VIP_JKL456),
                 list(PASSENGER_ABC123,PASSENGER_DEF456,EMPLOYEE_PQR678,EMPLOYEE_ABC567),
                 list(null, EMPLOYEE_ABC567, VIP_JKL456)),
            set(PASSENGER_ABC123,EMPLOYEE_MNO345,EMPLOYEE_DEF890,VIP_JKL456,
                PASSENGER_DEF456,EMPLOYEE_PQR678,EMPLOYEE_ABC567)));
        
        result.add(Arguments.of(
            "Three entities at zero degrees with single buid-out",
            set(EMPLOYEE_ABC567,VIP_GHI123,EMPLOYEE_MNO345),
            entityIdSet(set(EMPLOYEE_ABC567,VIP_GHI123,EMPLOYEE_MNO345)),
            0, 1, 10, flagSetIter.next(), null, null, 3,
            list(list(null, EMPLOYEE_ABC567, VIP_GHI123),
                 list(null, EMPLOYEE_ABC567, EMPLOYEE_MNO345),
                 list(null, VIP_GHI123, EMPLOYEE_MNO345)),
            set(EMPLOYEE_ABC567,VIP_GHI123,EMPLOYEE_MNO345,EMPLOYEE_PQR678,
                VIP_XYZ234,VIP_STU901,PASSENGER_ABC123,EMPLOYEE_DEF890)));
        
        result.add(Arguments.of(
            "Two entities at zero degrees with single build-out",
            set(PASSENGER_ABC123, PASSENGER_DEF456),
            entityIdSet(set(PASSENGER_ABC123, PASSENGER_DEF456)), 0, 1, 10,
            flagSetIter.next(), null, null, 1,
            list(list(null, PASSENGER_ABC123, PASSENGER_DEF456)),
            set(PASSENGER_ABC123,PASSENGER_DEF456,EMPLOYEE_MNO345,
                PASSENGER_GHI789,EMPLOYEE_PQR678)));
        
        result.add(Arguments.of(
            "Unknown data source for network entity",
            set(SzRecordKey.of(UNKNOWN_DATA_SOURCE,"ABC123"), VIP_XYZ234),
            set(100000000L, this.getEntityId(VIP_XYZ234)),
            3, 0, 10, flagSetIter.next(),
            UNKNOWN_SOURCE, NOT_FOUND, 0, NO_PATHS, NO_ENTITIES));

        result.add(Arguments.of(
            "Not-found record ID for network entity",
            set(VIP_XYZ234, SzRecordKey.of(PASSENGERS,"XXX000")),
            set(this.getEntityId(VIP_XYZ234), -100L),
            3, 0, 10, flagSetIter.next(),
            NOT_FOUND, NOT_FOUND, 0, NO_PATHS, NO_ENTITIES));
    
        return result;
    }

    /**
     * Validates an entity network versus expected result.
     * 
     * @param networkJson The JSON string describing the network.
     * @param testData The test data describing the test in case of failure.
     * @param recordKeys The {@link Set} of {@link SzRecordKey} instances 
     *                   for the entities in the network.
     * @param maxDegrees The maximum number of degrees between the paths of
     *                   the network.
     * @param buildOutDegrees The number of degrees to build out the network.
     * @param buildOutMaxEntities The maximum number of entities to build out.
     * @param flags The flags that were used for the test.
     * @param expectedPathCount The expected number of paths for the network.
     * @param expectedPaths The {@link List} of expected paths as in-order
     *                      {@link List} instances of {@link SzRecordKey} values.
     */
    default void validateNetwork(
        String                  networkJson,
        String                  testData,
        Set<SzRecordKey>        recordKeys,
        int                     maxDegrees,
        int                     buildOutDegrees,
        int                     buildOutMaxEntities,
        Set<SzFlag>             flags,
        int                     expectedPathCount,
        List<List<SzRecordKey>> expectedPaths)
    {
        JsonObject jsonObject = null;
        try {
            jsonObject = parseJsonObject(networkJson);
        } catch (Exception e) {
            fail("Unable to parse find-network result as JSON: " + networkJson);
        }
        
        JsonArray entityPaths = getJsonArray(jsonObject, "ENTITY_PATHS");
                
        assertNotNull(entityPaths, "Entity paths are missing: " + testData);

        assertEquals(expectedPathCount, entityPaths.size(),
                     "Paths array has unexpected length: paths=[ " + entityPaths 
                     + " ], " + testData);
        
        Set<Long> allEntityIds = new LinkedHashSet<>();
        Map<String, List<Long>> actualPaths = new LinkedHashMap<>();
        for (JsonObject path : entityPaths.getValuesAs(JsonObject.class)) {
            assertNotNull(path, "Entity path was null: paths=[ " + entityPaths
                          + " ], " + testData);
        
            Long startEntityId  = getLong(path, "START_ENTITY_ID");
            Long endEntityId    = getLong(path, "END_ENTITY_ID");

            assertNotNull(startEntityId, 
                "Starting entity ID on path should not be null.  path=[ "
                + path + " ], " + testData);
            
            assertNotNull(endEntityId,
                "Ending entity ID on path should not be null.  path=[ "
                + path + " ], " + testData);
            
            JsonArray entityIds = getJsonArray(path, "ENTITIES");

            List<Long> pathIds = new ArrayList<>(entityIds.size());
            for (JsonNumber num : entityIds.getValuesAs(JsonNumber.class)) {
                pathIds.add(num.longValue());
                allEntityIds.add(num.longValue());
            }

            long minEntityId = Math.min(startEntityId, endEntityId);
            long maxEntityId = Math.max(startEntityId, endEntityId);
            if (minEntityId != startEntityId) {
                Collections.reverse(pathIds);
            }
            String key = minEntityId + ":" + maxEntityId;
            actualPaths.put(key, pathIds);
        }

        JsonArray pathLinks = getJsonArray(jsonObject, "ENTITY_NETWORK_LINKS");
        if (flags != null && flags.contains(SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO)) {
            assertNotNull(pathLinks, "Entity path links missing or null: "
                          + "pathLinks=[ " + pathLinks + " ], " + testData);
        } else {
            assertNull(pathLinks, "Entity path links present when not requested.  "
                       + "pathLinks=[ " + pathLinks + " ], " + testData);
        }

        JsonArray entities = getJsonArray(jsonObject, "ENTITIES");

        assertNotNull(entities,
                      "Entity details for network are missing: " + testData);

        // validate the entity count
        int minEntityCount = allEntityIds.size();
        int maxEntityCount = minEntityCount + buildOutMaxEntities;
        assertTrue(entities.size() >= minEntityCount,
                   "Too few entity details provided -- expected at least "
                   + minEntityCount + ", but got only " + entities.size()
                   + ": " + testData);
        assertTrue(entities.size() <= maxEntityCount,
                   "Too many entity details provided -- expected at most "
                   + maxEntityCount + ", but got " + entities.size()
                   + ": " + testData);
                

        // handle the expected paths
        Map<String, List<Long>> expectedPathMap = new LinkedHashMap<>();
        Map<List<SzRecordKey>, String> expectedLookup = new IdentityHashMap<>();
        Set<Long> expectedEntityIds = new LinkedHashSet<>();
        for (List<SzRecordKey> expectedPath: expectedPaths) {
            if (expectedPath.get(0) == null) {
                SzRecordKey startKey    = expectedPath.get(1);
                SzRecordKey endKey      = expectedPath.get(2);
                long        startId     = this.getEntityId(startKey);
                long        endId       = this.getEntityId(endKey);

                long minId = Math.min(startId, endId);
                long maxId = Math.max(startId, endId);

                String key = minId + ":" + maxId;
                expectedPathMap.put(key, list());
                expectedLookup.put(expectedPath, key);

                expectedEntityIds.add(startId);
                expectedEntityIds.add(endId);

            } else {
                List<Long>  entityIds   = this.getEntityIds(expectedPath);
                long        startId     = entityIds.get(0);
                long        endId       = entityIds.get(entityIds.size() - 1);
                long        minId       = Math.min(startId, endId);
                long        maxId       = Math.max(startId, endId);

                if (minId != startId) {
                    Collections.reverse(entityIds);
                }

                String key = minId + ":" + maxId;
                expectedPathMap.put(key, entityIds);
                expectedLookup.put(expectedPath, key);
                expectedEntityIds.addAll(entityIds);
            }
        }

        expectedLookup.forEach((expectedPath, key) -> {
            List<Long> expectedIds  = expectedPathMap.get(key);
            List<Long> actualIds    = actualPaths.get(key);

            assertNotNull(actualIds, 
                "Did not find actual path for expected path.  expected=[ "
                + expectedPath + " ], key=[ " + key + " ], expectedEntityIds=[ "
                + expectedIds + " ]");
            
            assertEquals(expectedIds, actualIds,
                "Path between entities is not as expected.  expected=[ "
                + expectedPath + " ], key=[ " + key + " ], actualPaths=[ "
                + actualPaths + " ], " + testData);
        });

        actualPaths.forEach((key, actualIds) -> {
            assertTrue(expectedPathMap.containsKey(key),
                "Actual path provided that was not expected.  path=[ "
                + actualIds + " ], key=[ " + key + " ]");
        });

        // check that the entities we found are on the path
        Set<Long> detailEntityIds = new HashSet<>();
        for (JsonObject entity : entities.getValuesAs(JsonObject.class)) {
            assertNotNull(entity, "Entity detail from network was null: "
                          + entities + ", " + testData);

            entity = getJsonObject(entity, "RESOLVED_ENTITY");
            assertNotNull(entity, "Resolved entity from network was null: "
                          + entities + ", " + testData);
            
            // get the entity ID
            Long id = getLong(entity, "ENTITY_ID");
            assertNotNull(
                id, "The entity detail from path was missing or "
                + "null ENTITY_ID: " + entity + ", " + testData);
            
            // add to the ID set
            detailEntityIds.add(id);
        }

        for (long entityId : expectedEntityIds) {
            assertTrue(detailEntityIds.contains(entityId),
                "Entity ID from network not found in entity details.  "
                + "entityId=[ " + entityId + " ], " + testData);
        }
    }
    
    /**
     * Tests finding an entity network by entity ID.
     * 
     * @param testDescription The description of the test being run.
     * @param recordKeys The {@link Set} of {@link SzRecordKey} instances
     *                   identifying the entities for the network.
     * @param entityIds The {@link Set} of {@link Long} entity ID's
     *                  identifying the entities for the network
     * @param maxDegrees The maximum number of degrees for the paths in
     *                   the network.
     * @param buildOutDegrees The number of degrees to build out the 
     *                        network.
     * @param buildOutMaxEntities The maximum number of entities to 
     *                            build out the network.
     * @param flags The {@link Set} of {@link SzFlag} instances to use
     *              for the test.
     * @param recordExceptionType The {@link Class} of the exception type that
     *                            is expected when getting the path by record ID,
     *                            or <code>null</code> if no exception is expected.
     * @param entityExceptionType The {@link Class} of the exception type that
     *                            is expected when getting the path by entity ID,
     *                            or <code>null</code> if no exception is expected.
     * @param expectedPathCount The expected number of paths for the network.
     * @param expectedPaths The {@link List} of in-order {@link SzRecordKey}
     *                      {@link List}'s for the expected paths in the network,
     *                      or <code>null</code> if not validating this.
     * @param expectedEntities The {@link Set} of {@link SzRecordKey} instances 
     *                         identifying the entities to be validated for
     *                         inclusion in the network, or <code>null</code< if
     *                         not validating.
     */
    @ParameterizedTest
    @MethodSource("getEntityNetworkParameters")
    default void testFindNetworkByEntityId(
        String                   testDescription,
        Set<SzRecordKey>         recordKeys,
        Set<Long>                entityIds,
        int                      maxDegrees,
        int                      buildOutDegrees,
        int                      buildOutMaxEntities,
        Set<SzFlag>              flags,
        Class<?>                 recordExceptionType,
        Class<?>                 entityExceptionType,
        int                      expectedPathCount,
        List<List<SzRecordKey>>  expectedPaths,
        Set<SzRecordKey>         expectedEntities)
    {
        StringBuilder sb = new StringBuilder(
            "description=[ " + testDescription + " ], recordKeys=[ "
            + recordKeys + " ], entityIds=[ " + entityIds 
            + " ], maxDegrees=[ " + maxDegrees + " ], buildOutDegrees=[ "
            + buildOutDegrees + " ], buildOutMaxEntities=[ " 
            + buildOutMaxEntities + " ], flags=[ " + SzFlag.toString(flags)
            + " ], expectedException=[ " + entityExceptionType
            + " ], expectedPathCount=[ " + expectedPathCount
            + " ], expectedPaths=[ ");

        String prefix1 = "";
        for(List<SzRecordKey> expectedPath: expectedPaths) {
            sb.append(prefix1);
            if (expectedPath.get(0) == null) {
                SzRecordKey startKey = expectedPath.get(1);
                SzRecordKey endKey = expectedPath.get(2);
                sb.append("{ NO PATH BETWEEN ").append(startKey).append(" (");
                sb.append(this.getEntityId(startKey)).append(") AND ");
                sb.append(endKey).append(" (").append(this.getEntityId(endKey)).append(") }");
            } else {
                String prefix2 = "";
                sb.append("{ ");
                for (SzRecordKey key : expectedPath) {
                    sb.append(prefix2).append(key).append(" (");
                    sb.append(this.getEntityId(key)).append(")");
                    prefix2 = ", ";
                }
                sb.append("}");
            }
            prefix1 = ", ";
        }
        SzEntityLookup lookup = this.getTestData().getEntityLookup();
        sb.append(" ], recordMap=[ ").append(lookup.getMapByRecordKey()).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.findNetwork(
                    SzEntityIds.of(entityIds), 
                    maxDegrees,
                    buildOutDegrees,
                    buildOutMaxEntities,
                    flags);

                if (entityExceptionType != null) {
                    fail("Unexpectedly succeeded in finding a path: " + testData);
                }

                this.validateNetwork(result,
                                     testData,
                                     recordKeys,
                                     maxDegrees,
                                     buildOutDegrees,
                                     buildOutMaxEntities,
                                     flags,
                                     expectedPathCount,
                                     expectedPaths);

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
                    fail("Unexpectedly failed finding an entity network: "
                         + testData + ", " + description, e);

                } else if (recordExceptionType != e.getClass()) {
                    assertInstanceOf(
                        entityExceptionType, e, 
                        "findNetwork() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getEntityNetworkParameters")
    default void testFindNetworkByRecordId(
        String                  testDescription,
        Set<SzRecordKey>        recordKeys,
        Set<Long>               entityIds,
        int                     maxDegrees,
        int                     buildOutDegrees,
        int                     buildOutMaxEntities,
        Set<SzFlag>             flags,
        Class<?>                recordExceptionType,
        Class<?>                entityExceptionType,
        int                     expectedPathCount,
        List<List<SzRecordKey>> expectedPaths,
        Set<SzRecordKey>        expectedEntities)
    {
        StringBuilder sb = new StringBuilder(
            "description=[ " + testDescription + " ], recordKeys=[ "
            + recordKeys + " ], entityIds=[ " + entityIds 
            + " ], maxDegrees=[ " + maxDegrees + " ], buildOutDegrees=[ "
            + buildOutDegrees + " ], buildOutMaxEntities=[ " 
            + buildOutMaxEntities + " ], flags=[ " + SzFlag.toString(flags)
            + " ], expectedException=[ " + recordExceptionType
            + " ], expectedPathCount=[ " + expectedPathCount
            + " ], expectedPaths=[ ");

        String prefix1 = "";
        for(List<SzRecordKey> expectedPath: expectedPaths) {
            sb.append(prefix1);
            if (expectedPath.get(0) == null) {
                SzRecordKey startKey = expectedPath.get(1);
                SzRecordKey endKey = expectedPath.get(2);
                sb.append("{ NO PATH BETWEEN ").append(startKey).append(" (");
                sb.append(this.getEntityId(startKey)).append(") AND ");
                sb.append(endKey).append(" (").append(this.getEntityId(endKey)).append(") }");
            } else {
                String prefix2 = "";
                sb.append("{ ");
                for (SzRecordKey key : expectedPath) {
                    sb.append(prefix2).append(key).append(" (");
                    sb.append(this.getEntityId(key)).append(")");
                    prefix2 = ", ";
                }
                sb.append("}");
            }
            prefix1 = ", ";
        }
        SzEntityLookup lookup = this.getTestData().getEntityLookup();
        sb.append(" ], recordMap=[ ").append(lookup.getMapByRecordKey()).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.findNetwork(
                    SzRecordKeys.of(recordKeys), 
                    maxDegrees,
                    buildOutDegrees,
                    buildOutMaxEntities,
                    flags);

                if (recordExceptionType != null) {
                    fail("Unexpectedly succeeded in finding a path: " + testData);
                }

                this.validateNetwork(result,
                                     testData,
                                     recordKeys,
                                     maxDegrees,
                                     buildOutDegrees,
                                     buildOutMaxEntities,
                                     flags,
                                     expectedPathCount,
                                     expectedPaths);

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
                    fail("Unexpectedly failed finding an entity network: "
                         + testData + ", " + description, e);

                } else if (recordExceptionType != e.getClass()) {
                    assertInstanceOf(
                        entityExceptionType, e, 
                        "findNetwork() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }
}
