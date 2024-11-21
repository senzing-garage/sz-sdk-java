package com.senzing.sdk.core;

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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonNumber;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzRecordKeys;
import com.senzing.sdk.SzEntityIds;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzNotFoundException;

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

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreEngineGraphTest extends AbstractTest {
    private static final String EMPLOYEES_DATA_SOURCE   = "EMPLOYEES";
    private static final String PASSENGERS_DATA_SOURCE  = "PASSENGERS";
    private static final String VIPS_DATA_SOURCE        = "VIPS";
    private static final String UNKNOWN_DATA_SOURCE     = "UNKNOWN";

    private static final List<Set<SzFlag>> FIND_PATH_FLAG_SET;
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
        list.add(Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
        FIND_PATH_FLAG_SET = Collections.unmodifiableList(list);
    }

    private static final List<Set<SzFlag>> FIND_NETWORK_FLAG_SET;
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

    private static final SzRecordKey PASSENGER_ABC123
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "ABC123");
    
    private static final SzRecordKey PASSENGER_DEF456
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "DEF456");

    private static final SzRecordKey PASSENGER_GHI789 
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "GHI789");

    private static final SzRecordKey PASSENGER_JKL012
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "JKL012");

    private static final SzRecordKey EMPLOYEE_MNO345
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "MNO345");

    private static final SzRecordKey EMPLOYEE_PQR678
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "PQR678");

    private static final SzRecordKey EMPLOYEE_ABC567
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "ABC567");
        
    private static final SzRecordKey EMPLOYEE_DEF890
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "DEF890");

    private static final SzRecordKey VIP_STU901
        = SzRecordKey.of(VIPS_DATA_SOURCE, "STU901");

    private static final SzRecordKey VIP_XYZ234
        = SzRecordKey.of(VIPS_DATA_SOURCE, "XYZ234");

    private static final SzRecordKey VIP_GHI123
        = SzRecordKey.of(VIPS_DATA_SOURCE, "GHI123");

    private static final SzRecordKey VIP_JKL456
        = SzRecordKey.of(VIPS_DATA_SOURCE, "JKL456");
    

    private static final List<SzRecordKey> GRAPH_RECORD_KEYS
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

    private SzCoreEnvironment env = null;

    private Map<SzRecordKey, Long> loadedRecordMap
        = new LinkedHashMap<>();
    
    private Map<Long, Set<SzRecordKey>> loadedEntityMap
        = new LinkedHashMap<>();

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
            for (SzRecordKey key : GRAPH_RECORD_KEYS) {
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
                this.loadedRecordMap.put(key, entityId);
                Set<SzRecordKey> recordKeySet = this.loadedEntityMap.get(entityId);
                if (recordKeySet == null) {
                    recordKeySet = new LinkedHashSet<>();
                    this.loadedEntityMap.put(entityId, recordKeySet);
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
    dataSources.add(PASSENGERS_DATA_SOURCE);
    dataSources.add(EMPLOYEES_DATA_SOURCE);
    dataSources.add(VIPS_DATA_SOURCE);
    
    File passengerFile = this.preparePassengerFile();
    File employeeFile = this.prepareEmployeeFile();
    File vipFile = this.prepareVipFile();

    employeeFile.deleteOnExit();
    passengerFile.deleteOnExit();
    vipFile.deleteOnExit();

    RepositoryManager.configSources(repoDirectory,
                                    dataSources,
                                    true);

    RepositoryManager.loadFile(repoDirectory,
                               passengerFile,
                               PASSENGERS_DATA_SOURCE,
                               true);

    RepositoryManager.loadFile(repoDirectory,
                               employeeFile,
                               EMPLOYEES_DATA_SOURCE,
                               true);

    RepositoryManager.loadFile(repoDirectory,
                               vipFile,
                               VIPS_DATA_SOURCE,
                               true);
  }

  private File preparePassengerFile() {
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
    return this.prepareCSVFile("test-passengers-", headers, passengers);
  }

  private File prepareEmployeeFile() {
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

    return this.prepareJsonArrayFile("test-employees-", headers, employees);
  }

  private File prepareVipFile() {
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

    return this.prepareJsonFile("test-vips-", headers, vips);
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

    public Long getEntityId(SzRecordKey recordKey) {
        return this.loadedRecordMap.get(recordKey);
    }

    public List<Long> getEntityIds(Collection<SzRecordKey> recordKeys) {
        if (recordKeys == null) return null;
        List<Long> result = new ArrayList<>(recordKeys.size());
        for (SzRecordKey recordKey : recordKeys) {
            result.add(this.getEntityId(recordKey));
        }
        return result;
    }

    public Set<Long> entityIdSet(Set<SzRecordKey> recordKeys) {
        Set<Long> result = new LinkedHashSet<>();
        for (SzRecordKey key: recordKeys) {
            result.add(this.getEntityId(key));
        }
        return result;
    }

    public void validatePath(String             pathJson,
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

            if (avoidances != null && avoidances.size() > 0
                && ((flags != null) && flags.contains(SZ_FIND_PATH_STRICT_AVOID))) 
            {
                assertFalse(avoidedIds.contains(entityId), 
                            "Strictly avoided entity ID (" + entityId + ") found "
                            + "in path: entityIds=[ " + entityIds 
                            + " ], recordKeys=[ " +  this.loadedEntityMap.get(entityId)
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

        // validate the required data sources
        if (requiredSources != null && requiredSources.size() > 0) {
            boolean sourcesSatisified = false;
            for (Long entityId : actualPathIds) {
                if (entityId.equals(startEntityId)) continue;
                if (entityId.equals(endEntityId)) continue;
                Set<SzRecordKey> keys = this.loadedEntityMap.get(entityId);
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

    public static Set<SzFlag> flagsWith(Set<SzFlag> baseFlags, SzFlag... otherFlags) {
        EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);
        if (baseFlags != null) set.addAll(baseFlags);
        for (SzFlag flag : otherFlags) {
            set.add(flag);
        }
        if (set.size() == 0 && baseFlags == null) return null;
        return set;
    }

    public static Set<SzFlag> flagsWithout(Set<SzFlag> baseFlags, SzFlag... otherFlags) {
        if (baseFlags == null) return null;
        EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);
        set.addAll(baseFlags);
        for (SzFlag flag : otherFlags) {
            set.remove(flag);
        }
        return set;
    }

    public static Set<SzFlag> flagsWithStrictAvoid(Set<SzFlag> baseFlags) {
        return flagsWith(baseFlags, SZ_FIND_PATH_STRICT_AVOID);
    }

    public static Set<SzFlag> flagsWithDefaultAvoid(Set<SzFlag> baseFlags) {
        return flagsWithout(baseFlags, SZ_FIND_PATH_STRICT_AVOID);
    }

    public List<Arguments> getEntityPathParameters() {
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
            set(EMPLOYEES_DATA_SOURCE), flagsWithDefaultAvoid(flagSetIter.next()),
            null, null, 6, list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890,
                                VIP_JKL456, VIP_XYZ234, PASSENGER_JKL012)));
        
        result.add(Arguments.of(
            "Diverted path at 5 degrees due to required VIP source",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012), 
            10, null, null, set(VIPS_DATA_SOURCE),
            flagsWithDefaultAvoid(flagSetIter.next()), null, null, 6,
            list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890,
                    VIP_JKL456, VIP_XYZ234, PASSENGER_JKL012)));
        
        result.add(Arguments.of(
            "Diverted path at 5 degrees due to 2 required sources",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012), 
            10, null, null, set(EMPLOYEES_DATA_SOURCE, VIPS_DATA_SOURCE),
            flagsWithDefaultAvoid(flagSetIter.next()), null, null, 6,
            list(PASSENGER_ABC123, EMPLOYEE_MNO345, EMPLOYEE_DEF890,
                    VIP_JKL456, VIP_XYZ234, PASSENGER_JKL012)));

        result.add(Arguments.of(
            "Diverted path with required sources and avoidance",
            PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
            PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012), 
            10, set(VIP_STU901), set(this.getEntityId(VIP_STU901)),
            set(EMPLOYEES_DATA_SOURCE, VIPS_DATA_SOURCE),
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
            set(SzRecordKey.of(PASSENGERS_DATA_SOURCE, "XXX000")),
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
                SzRecordKey.of(PASSENGERS_DATA_SOURCE, "XXX000"),
                100000000L, PASSENGER_JKL012, this.getEntityId(PASSENGER_JKL012),
                10, null, null, null, flagSetIter.next(), 
                NOT_FOUND, NOT_FOUND, 0, EMPTY_PATH));

        result.add(Arguments.of(
                "Unknown end record ID in find path via key",
                PASSENGER_ABC123, this.getEntityId(PASSENGER_ABC123),
                SzRecordKey.of(PASSENGERS_DATA_SOURCE, "XXX000"),
                200000000L, 10, null, null, null, flagSetIter.next(), 
                NOT_FOUND, NOT_FOUND, 0, EMPTY_PATH));
        
        return result;
    }

    @ParameterizedTest
    @MethodSource("getEntityPathParameters")
    void testFindPathByRecordId(String              testDescription,
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

        String prefix = "";
        for (SzRecordKey key : expectedPath) {
            sb.append(prefix).append(key).append(" (");
            sb.append(this.getEntityId(key)).append(")");
            prefix = ", ";
        }
        sb.append(" ], recordMap=[ ").append(this.loadedRecordMap).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getEntityPathParameters")
    void testFindPathByEntityId(String              testDescription,
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

        String prefix = "";
        for (SzRecordKey key : expectedPath) {
            sb.append(prefix).append(key).append(" (");
            sb.append(this.getEntityId(key)).append(")");
            prefix = ", ";
        }
        sb.append(" ], recordMap=[ ").append(this.loadedRecordMap).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

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

    @SuppressWarnings("unchecked")
    public List<Arguments> getEntityNetworkParameters() {
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
            set(VIP_XYZ234, SzRecordKey.of(PASSENGERS_DATA_SOURCE,"XXX000")),
            set(this.getEntityId(VIP_XYZ234), -100L),
            3, 0, 10, flagSetIter.next(),
            NOT_FOUND, NOT_FOUND, 0, NO_PATHS, NO_ENTITIES));
    
        return result;
    }

    public void validateNetwork(String                   networkJson,
                                String                   testData,
                                Set<SzRecordKey>         recordKeys,
                                int                      maxDegrees,
                                int                      buildOutDegrees,
                                int                      buildOutMaxEntities,
                                Set<SzFlag>              flags,
                                int                      expectedPathCount,
                                List<List<SzRecordKey>>  expectedPaths)
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
    
    @ParameterizedTest
    @MethodSource("getEntityNetworkParameters")
    void testFindNetworkByEntityId(String                   testDescription,
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
        sb.append(" ], recordMap=[ ").append(this.loadedRecordMap).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

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
    void testFindNetworkByRecordId(String                   testDescription,
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
        sb.append(" ], recordMap=[ ").append(this.loadedRecordMap).append(" ]");
        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

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
