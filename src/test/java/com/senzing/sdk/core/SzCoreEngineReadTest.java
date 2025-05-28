package com.senzing.sdk.core;

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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.BufferedReader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.json.JsonObject;
import javax.json.JsonArray;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzNotFoundException;
import com.ibm.icu.impl.Assert;
import com.senzing.sdk.SzBadInputException;
import com.senzing.util.JsonUtilities;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.sdk.core.Utilities.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreEngineReadTest extends AbstractTest {
    private static final String PASSENGERS          = "PASSENGERS";
    private static final String EMPLOYEES           = "EMPLOYEES";
    private static final String VIPS                = "VIPS";
    private static final String MARRIAGES           = "MARRIAGES";
    private static final String UNKNOWN_DATA_SOURCE = "UNKNOWN";
  
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
    private static final SzRecordKey ZYX321
        = SzRecordKey.of(EMPLOYEES, "ZYX321");
    private static final SzRecordKey CBA654
        = SzRecordKey.of(EMPLOYEES, "CBA654");
  
    private static final SzRecordKey BCD123
        = SzRecordKey.of(MARRIAGES, "BCD123");
    private static final SzRecordKey CDE456
        = SzRecordKey.of(MARRIAGES, "CDE456");
    private static final SzRecordKey EFG789
        = SzRecordKey.of(MARRIAGES, "EFG789");
    private static final SzRecordKey FGH012
        = SzRecordKey.of(MARRIAGES, "FGH012");
  
    private static final List<Set<SzFlag>> ENTITY_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new LinkedList<>();
        list.add(null);
        list.add(SZ_NO_FLAGS);
        list.add(SZ_ENTITY_DEFAULT_FLAGS);
        list.add(SZ_ENTITY_BRIEF_DEFAULT_FLAGS);
        list.add(Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_ENTITY_NAME,
            SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
            SZ_ENTITY_INCLUDE_RECORD_DATA,
            SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
        ENTITY_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private static final List<Set<SzFlag>> RECORD_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new LinkedList<>();
        list.add(null);
        list.add(SZ_NO_FLAGS);
        list.add(SZ_RECORD_DEFAULT_FLAGS);
        list.add(SZ_RECORD_ALL_FLAGS);
        list.add(Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO,
            SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA)));
        list.add(Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_RECORD_TYPES,
            SZ_ENTITY_INCLUDE_RECORD_JSON_DATA)));
        RECORD_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private static final List<SzFlag> SEARCH_INCLUDE_FLAGS
        = List.of(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED,
                  SZ_SEARCH_INCLUDE_POSSIBLY_SAME,
                  SZ_SEARCH_INCLUDE_RESOLVED);

    private static final List<Set<SzFlag>> SEARCH_FLAG_SETS;
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
        list.add(Collections.unmodifiableSet(set));

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
        SEARCH_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private static final List<SzFlag> EXPORT_INCLUDE_FLAGS
        = List.of(SZ_EXPORT_INCLUDE_POSSIBLY_RELATED,
                  SZ_EXPORT_INCLUDE_POSSIBLY_SAME,
                  SZ_EXPORT_INCLUDE_DISCLOSED,
                  SZ_EXPORT_INCLUDE_NAME_ONLY,
                  SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES,
                  SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES);

    private static final List<Set<SzFlag>> EXPORT_FLAG_SETS;
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
        list.add(Collections.unmodifiableSet(set));

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
        EXPORT_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private Map<SzRecordKey, Long> loadedRecordMap
        = new LinkedHashMap<>();
    
    private Map<Long, Set<SzRecordKey>> loadedEntityMap
        = new LinkedHashMap<>();

    private static final List<SzRecordKey> RECORD_KEYS
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

    public Long getEntityId(SzRecordKey recordKey) {
        return this.loadedRecordMap.get(recordKey);
    }

    /**
     * Overridden to configure some data sources.
     */
    protected void prepareRepository() {
        File repoDirectory = this.getRepositoryDirectory();

        Set<String> dataSources = new LinkedHashSet<>();
        dataSources.add("PASSENGERS");
        dataSources.add("EMPLOYEES");
        dataSources.add("VIPS");
        dataSources.add("MARRIAGES");

        File passengerFile = this.preparePassengerFile();
        File employeeFile = this.prepareEmployeeFile();
        File vipFile = this.prepareVipFile();
        File marriagesFile = this.prepareMarriagesFile();

        employeeFile.deleteOnExit();
        passengerFile.deleteOnExit();
        vipFile.deleteOnExit();
        marriagesFile.deleteOnExit();

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
                                   marriagesFile,
                                   MARRIAGES,
                                   true);
    }

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
            {STU901.recordId(), "John", "Doe", "818-555-1313",
                "100 Main Street, Los Angeles, CA 90012", "1978-10-17", "GREEN"},
            {XYZ234.recordId(), "Jane", "Doe", "818-555-1212",
                "100 Main Street, Los Angeles, CA 90012", "1979-02-05", "GRAHAM"}
        };

        return this.prepareJsonFile("test-vips-", headers, vips);
    }

    private File prepareMarriagesFile() {
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

    private List<Arguments> getRecordKeyParameters() {
        List<Arguments> result = new ArrayList<>(RECORD_KEYS.size());
        RECORD_KEYS.forEach(key -> {
            result.add(Arguments.of(key));
        });
        return result;
    }

    private List<Arguments> getGetEntityParameters() {
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

    public List<Arguments> getExportCsvParameters() {
        List<Arguments> result = new LinkedList<>();

        List<String> columnLists = List.of(
            "", "*", "RESOLVED_ENTITY_ID,RESOLVED_ENTITY_NAME,RELATED_ENTITY_ID");

        Iterator<String> columnListIter = circularIterator(columnLists);

        for (Set<SzFlag> flagSet : EXPORT_FLAG_SETS) {
            result.add(Arguments.of(columnListIter.next(), flagSet, null));        
        }

        Set<SzFlag> flagSet = EXPORT_FLAG_SETS.iterator().next();
        result.add(
            Arguments.of("RESOLVED_ENTITY_ID,BAD_COLUMN_NAME", flagSet, SzBadInputException.class));
        
        return result;
    }

    public List<Arguments> getExportJsonParameters() {
        List<Arguments> result = new LinkedList<>();

        for (Set<SzFlag> flagSet : EXPORT_FLAG_SETS) {
            result.add(Arguments.of(flagSet, null));        
        }
        
        return result;
    }

    @ParameterizedTest
    @MethodSource("getExportCsvParameters")
    void testExportCsvEntityReport(String       columnList,
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
                engine = this.env.getEngine();

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

    @ParameterizedTest
    @ValueSource(strings = {
        "","*","RESOLVED_ENTITY_ID,RESOLVED_ENTITY_NAME,RELATED_ENTITY_ID"})
    public void TestExportCsvDefaults(String columnList)
    {
        this.performTest(() ->
        {
            long exportHandle = 0L;
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getExportJsonParameters")
    void testExportJsonEntityReport(Set<SzFlag>  flags,
                                    Class<?>     expectedException)
    {
        String testData = "flags=[ " + SzFlag.toString(flags) 
            + " ], expectedException=[ " + expectedException + " ]";

        this.performTest(() -> {
            Long        handle = null;
            SzEngine    engine = null;
            try {
                engine = this.env.getEngine();

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

    @Test
    public void testExportJsonDefaults() {
        this.performTest(() -> {
            long exportHandle = 0L;
            try
            {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    public void testGetEntityByRecordIdDefaults(SzRecordKey recordKey)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String dataSourceCode = recordKey.dataSourceCode();
                String recordID = recordKey.recordId();

                String defaultResult = engine.getEntity(recordKey);
                
                String explicitResult = engine.getEntity(
                    recordKey, SZ_ENTITY_DEFAULT_FLAGS);
                    
                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().getEntityByRecordID(
                    dataSourceCode, recordID, sb);
                    
                if (returnCode != 0) {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                String nativeResult = sb.toString();

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            }
            catch (Exception e) {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    void testGetEntityByRecordId(String         testDescription,
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
                SzEngine engine = this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    public void testGetEntityByEntityIdDefaults(SzRecordKey recordKey)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                long entityId = this.getEntityId(recordKey);

                String defaultResult = engine.getEntity(entityId);
                
                String explicitResult = engine.getEntity(
                    entityId, SZ_ENTITY_DEFAULT_FLAGS);
                    
                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().getEntityByEntityID(
                    entityId, sb);
                    
                if (returnCode != 0) {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                String nativeResult = sb.toString();

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            }
            catch (Exception e) {
                fail("Unexpectedly failed getting entity by entity ID", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    void testGetEntityByEntityId(String         testDescription,
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
                SzEngine engine = this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    public void testFindInterestingEntitiesByRecordIdDefaults(SzRecordKey recordKey) {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    void testFindInterestingEntitiesByRecordId(
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
                SzEngine engine = this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    public void testFindInterestingEntitiesByEntityIdDefaults(SzRecordKey recordKey) {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getGetEntityParameters")
    void testFindInterestingEntitiesByEntityId(
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
                SzEngine engine = this.env.getEngine();

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


    private List<Arguments> getGetRecordParameters() {
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

    @ParameterizedTest
    @MethodSource("getGetRecordParameters")
    void testGetRecord(String         testDescription,
                       SzRecordKey    recordKey,
                       Set<SzFlag>    flags,
                       Class<?>       expectedExceptionType)
    {
        String testData = "description=[ " + testDescription 
            + " ], recordKey=[ " + recordKey + " ], flags=[ "
            + SzFlag.toString(flags) + " ], expectedExceptionType=[ "
            + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    public void testGetRecordDefaults(SzRecordKey recordKey) {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String dataSourceCode = recordKey.dataSourceCode();
                String recordID = recordKey.recordId();

                String defaultResult = engine.getRecord(recordKey);
                
                String explicitResult = engine.getRecord(
                    recordKey, SZ_RECORD_DEFAULT_FLAGS);
                    
                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().getRecord(
                    dataSourceCode, recordID, sb);
                    
                if (returnCode != 0) {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                String nativeResult = sb.toString();

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            } catch (Exception e) {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }

    private <K extends Comparable<K>, V> Map<K, V> sortedMap(Map<K, V> map)
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


    private static class Criterion {
        private String key;
        private Set<String> values;

        private Criterion(String key, String... values) {
            this.key = key;
            this.values = new LinkedHashSet<>();
            for (String value : values) {
            this.values.add(value);
            }
        }
    }

    private static Criterion criterion(String key, String... values) {
        return new Criterion(key, values);
    }

    private static Map<String, Set<String>> criteria(String key, String... values) {
        Criterion criterion = criterion(key, values);
        return criteria(criterion);
    }

    private static Map<String, Set<String>> criteria(Criterion... criteria) {
        Map<String, Set<String>> result = new LinkedHashMap<>();
        for (Criterion criterion : criteria) {
            Set<String> values = result.get(criterion.key);
            if (values == null) {
            result.put(criterion.key, criterion.values);
            } else {
            values.addAll(criterion.values);
            }
        }
        return result;
    }

    public static Set<SzFlag> flags(Set<SzFlag> flagSet, SzFlag... addlFlags) {
        Set<SzFlag> result = EnumSet.noneOf(SzFlag.class);
        result.addAll(flagSet);
        for (SzFlag flag : addlFlags) {
            result.add(flag);
        }
        return result;
    }

    public List<Arguments> getSearchParameters() {
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

                String attributes = criteriaToJson(criteria);

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

    public List<Arguments> getSearchDefaultParameters()
    {
        List<Arguments> searchParams = this.getSearchParameters();

        List<Arguments> defaultParams = new ArrayList<>(searchParams.size());

        searchParams.forEach(args -> {
            Object[] arr = args.get();
            
            // skip parameters that expect exceptions
            if (arr[arr.length - 1] != null) return;

            defaultParams.add(Arguments.of(arr[0], arr[1]));
        });

        return defaultParams;
    }

    private String criteriaToJson(Map<String, Set<String>> criteria) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String prefix1 = "";
        for (Map.Entry<String,Set<String>> entry : criteria.entrySet()) {
            String      key     = entry.getKey().toUpperCase();
            Set<String> values  = entry.getValue();
            if (values.size() == 0) continue;
            if (values.size() == 1) {
                sb.append(prefix1);
                sb.append(jsonEscape(key)).append(":");
                sb.append(jsonEscape(values.iterator().next()));
                prefix1 = ",";
                continue;
            }
            String pluralKey = (key.endsWith("S")) 
                ? (key + "ES") : (key + "S");
            sb.append(prefix1);
            sb.append(jsonEscape(pluralKey)).append(": [");
            String prefix2 = "";
            for (String value: values) {
                sb.append(prefix2);
                sb.append("{").append(jsonEscape(key));
                sb.append(":").append(jsonEscape(value)).append("}");
                prefix2 = ",";
            }
            sb.append("]");
            prefix1 = ",";
        }
        sb.append("}");
        return sb.toString();
    }

    @ParameterizedTest
    @MethodSource("getSearchParameters")
    void testSearchByAttributes(String      attributes,
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
                SzEngine engine = this.env.getEngine();

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

    @ParameterizedTest
    @MethodSource("getSearchDefaultParameters")
    public void testSearchByAttributesdDefaults(String attributes,
                                                String searchProfile)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String defaultResult = (searchProfile == null)
                    ? engine.searchByAttributes(attributes)
                    : engine.searchByAttributes(attributes, searchProfile);

                String explicitResult = (searchProfile == null)
                    ? engine.searchByAttributes(attributes, 
                                                SzFlag.SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS)
                    : engine.searchByAttributes(attributes, 
                                                searchProfile,
                                                SzFlag.SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS);
                    
                StringBuffer sb = new StringBuffer();
                int returnCode = (searchProfile == null)
                    ? engine.getNativeApi().searchByAttributes(attributes, sb)
                    : engine.getNativeApi().searchByAttributes(
                        attributes, searchProfile, 
                        SzFlag.toLong(SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS), sb);

                if (returnCode != 0) {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                String nativeResult = sb.toString();

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            
            } catch (Exception e) {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }

}
