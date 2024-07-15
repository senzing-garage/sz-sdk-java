package com.senzing.sdk.core;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.sdk.core.SzRecord.SzAddressByParts;
import com.senzing.sdk.core.SzRecord.SzDateOfBirth;
import com.senzing.sdk.core.SzRecord.SzFullAddress;
import com.senzing.sdk.core.SzRecord.SzFullName;
import com.senzing.sdk.core.SzRecord.SzNameByParts;
import com.senzing.sdk.core.SzRecord.SzPhoneNumber;
import com.senzing.sdk.core.SzRecord.SzSocialSecurity;
import com.senzing.util.JsonUtilities;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import javax.json.JsonObject;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzBadInputException;
import com.senzing.sdk.SzUnknownDataSourceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreEngineWriteTest extends AbstractTest {
    private static final String CUSTOMERS_DATA_SOURCE   = "CUSTOMERS";
    private static final String WATCHLIST_DATA_SOURCE   = "WATCHLIST";
    private static final String EMPLOYEES_DATA_SOURCE   = "EMPLOYEES";
    private static final String PASSENGERS_DATA_SOURCE  = "PASSENGERS";
    private static final String VIPS_DATA_SOURCE        = "VIPS";
    private static final String UNKNOWN_DATA_SOURCE     = "UNKNOWN";
    
    private static final SzRecord RECORD_JOE_SCHMOE
        = new SzRecord(
            SzFullName.of("Joe Schmoe"),
            SzPhoneNumber.of("725-555-1313"),
            SzFullAddress.of("101 Main Street, Las Vegas, NV 89101"));
    
    private static final SzRecord RECORD_JANE_SMITH
        = new SzRecord(
            SzFullName.of("Jane Smith"),
            SzPhoneNumber.of("725-555-1414"),
            SzFullAddress.of("440 N Rancho Blvd, Las Vegas, NV 89101"));

    private static final SzRecord RECORD_JOHN_DOE
        = new SzRecord(
            SzFullName.of("John Doe"),
            SzPhoneNumber.of("725-555-1717"),
            SzFullAddress.of("777 W Sahara Blvd, Las Vegas, NV 89107"));

    private static final SzRecord RECORD_JAMES_MORIARTY
        = new SzRecord(
            SzNameByParts.of("James", "Moriarty"),
            SzPhoneNumber.of("44-163-555-1313"),
            SzFullAddress.of("16A Upper Montagu St, London, W1H 2PB, England"));

    private static final SzRecord RECORD_SHERLOCK_HOLMES
        = new SzRecord(
            SzFullName.of("Sherlock Holmes"),
            SzPhoneNumber.of("44-163-555-1212"),
            SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));

    private static final SzRecord RECORD_JOHN_WATSON
        = new SzRecord(
            SzFullName.of("Dr. John H. Watson"),
            SzPhoneNumber.of("44-163-555-1414"),
            SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));
    
    private static final SzRecord RECORD_JOANN_SMITTH
        = new SzRecord(
            SzFullName.of("Joann Smith"),
            SzPhoneNumber.of("725-888-3939"),
            SzFullAddress.of("101 Fifth Ave, Las Vegas, NV 89118"),
            SzDateOfBirth.of("15-MAY-1983"));
        
    private static final SzRecord RECORD_BILL_WRIGHT
        = new SzRecord(
            SzNameByParts.of("Bill", "Wright", "AKA"),
            SzNameByParts.of("William", "Wright", "PRIMARY"),
            SzPhoneNumber.of("725-444-2121"),
            SzAddressByParts.of("101 Main StreetFifth Ave", "Las Vegas", "NV", "89118"),
            SzDateOfBirth.of("15-MAY-1983"));

    private static final SzRecord RECORD_CRAIG_SMITH
        = new SzRecord(
            SzNameByParts.of("Craig", "Smith"),
            SzPhoneNumber.of("725-888-3940"),
            SzFullAddress.of("101 Fifth Ave, Las Vegas, NV 89118"),
            SzDateOfBirth.of("12-JUN-1981"));
                
    private static final SzRecord RECORD_KIM_LONG
        = new SzRecord(
            SzFullName.of("Kim Long"),
            SzPhoneNumber.of("725-135-1913"),
            SzFullAddress.of("451 Dover St., Las Vegas, NV 89108"),
            SzDateOfBirth.of("24-OCT-1976"));

    private static final SzRecord RECORD_KATHY_OSBOURNE
        = new SzRecord(
            SzFullName.of("Kathy Osbourne"),
            SzPhoneNumber.of("725-111-2222"),
            SzFullAddress.of("707 Seventh Ave, Las Vegas, NV 89143"),
            SzDateOfBirth.of("24-OCT-1976"));
    
    private static final List<SzRecord> NEW_RECORDS
        = List.of(RECORD_BILL_WRIGHT,
                  RECORD_CRAIG_SMITH,
                  RECORD_JAMES_MORIARTY,
                  RECORD_JANE_SMITH,
                  RECORD_JOANN_SMITTH,
                  RECORD_JOE_SCHMOE,
                  RECORD_JOHN_DOE,
                  RECORD_JOHN_WATSON,
                  RECORD_KATHY_OSBOURNE,
                  RECORD_KIM_LONG,
                  RECORD_SHERLOCK_HOLMES);

    private static final List<SzRecordKey> NEW_RECORD_KEYS
        = List.of(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "ABC123"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "DEF456"),
            SzRecordKey.of(UNKNOWN_DATA_SOURCE, "GHI789"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "JKL012"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "MNO345"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "PQR678"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "STU901"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "VWX234"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "YZA567"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "BCD890"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "EFG123")
        );

    private static final Set<SzRecord> COUNT_REDO_TRIGGER_RECORDS = Set.of(
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-01"),
            SzFullName.of("Scott Summers"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-02"),
            SzFullName.of("Jean Gray"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-03"),
            SzFullName.of("Charles Xavier"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-04"),
            SzFullName.of("Henry McCoy"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-05"),
            SzFullName.of("James Howlett"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-06"),
            SzFullName.of("Ororo Munroe"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-07"),
            SzFullName.of("Robert Drake"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-08"),
            SzFullName.of("Anna LeBeau"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-09"),
            SzFullName.of("Lucas Bishop"),
            SzSocialSecurity.of("999-99-9999")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-10"),
            SzFullName.of("Erik Lehnsherr"),
            SzSocialSecurity.of("999-99-9999")));

    private static final Set<SzRecord> PROCESS_REDO_TRIGGER_RECORDS = Set.of(
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-11"),
            SzFullName.of("Anthony Stark"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-12"),
            SzFullName.of("Janet Van Dyne"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-13"),
            SzFullName.of("Henry Pym"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-14"),
            SzFullName.of("Bruce Banner"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-15"),
            SzFullName.of("Steven Rogers"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-16"),
            SzFullName.of("Clinton Barton"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-17"),
            SzFullName.of("Wanda Maximoff"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-18"),
            SzFullName.of("Victor Shade"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-19"),
            SzFullName.of("Natasha Romanoff"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "SAME-SSN-20"),
            SzFullName.of("James Rhodes"),
            SzSocialSecurity.of("888-88-8888")));

    private static final List<Set<SzFlag>> WRITE_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new ArrayList<>(4);
        list.add(null);
        list.add(SZ_NO_FLAGS);
        list.add(SZ_WITH_INFO_FLAGS);
        WRITE_FLAG_SETS = Collections.unmodifiableList(list);
    }
    
    private static final Map<SzRecordKey, Long> LOADED_RECORD_MAP
        = Collections.synchronizedMap(new LinkedHashMap<>());

    private static final Map<Long, Set<SzRecordKey>> LOADED_ENTITY_MAP
        = Collections.synchronizedMap(new LinkedHashMap<>());

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
    

    private static final List<SzRecordKey> EXISTING_RECORD_KEYS
        = List.of(PASSENGER_ABC123,
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
            for (SzRecordKey key : EXISTING_RECORD_KEYS) {
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
        dataSources.add(CUSTOMERS_DATA_SOURCE);
        dataSources.add(WATCHLIST_DATA_SOURCE);
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
            {PASSENGER_ABC123.recordId(), "Joseph", "Schmidt", "818-555-1212", "818-777-2424",
                "101 Main Street, Los Angeles, CA 90011", "12-JAN-1981"},
            {PASSENGER_DEF456.recordId(), "Joann", "Smith", "818-555-1212", "818-888-3939",
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

    public List<Arguments> getAddRecordArguments() {
        List<Arguments>     result      = new LinkedList<>();
        int count = Math.min(NEW_RECORD_KEYS.size(), NEW_RECORDS.size());
        Iterator<SzRecordKey>   keyIter     = NEW_RECORD_KEYS.iterator();
        Iterator<SzRecord>      recordIter  = NEW_RECORDS.iterator();

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

        int errorCase = 0;
        for (int index = 0; index < count; index++) {
            SzRecordKey key             = keyIter.next();
            SzRecord    record          = recordIter.next();
            Class<?>    exceptionType   = null;
            Set<SzFlag> flagSet         = flagSetIter.next();

            switch (errorCase) {
                case 1:
                {
                    String dataSource 
                        = (CUSTOMERS_DATA_SOURCE.equals(key.dataSourceCode()))
                        ? WATCHLIST_DATA_SOURCE
                        : CUSTOMERS_DATA_SOURCE;

                    SzRecordKey wrongKey 
                        = SzRecordKey.of(dataSource, key.recordId());

                    record = new SzRecord(wrongKey, record);

                    exceptionType = SzBadInputException.class;
                    errorCase++;
                }
                break;
                case 2:
                {
                    SzRecordKey wrongKey 
                        = SzRecordKey.of(key.dataSourceCode(), "WRONG_ID");

                    record = new SzRecord(wrongKey, record);

                    exceptionType = SzBadInputException.class;

                    errorCase++;
                }
                break;
                default:
                {
                    record = new SzRecord(key, record);
                    if (key.dataSourceCode().equals(UNKNOWN_DATA_SOURCE)) {
                        errorCase++;
                        exceptionType = SzUnknownDataSourceException.class;
                    }        
                }
            }
            
            result.add(Arguments.of(key, record, flagSet, exceptionType));
        }

        return result;
    }

    @ParameterizedTest
    @MethodSource("getAddRecordArguments")
    @Order(10)
    void testAddRecord(SzRecordKey  recordKey, 
                       SzRecord     record,
                       Set<SzFlag>  flags,
                       Class<?>     expectedExceptionType)
    {
        String testData = "recordKey=[ " + recordKey 
            + " ], record=[ " + record + " ], withFlags=[ " 
            + SzFlag.toString(flags) + " ], expectedException=[ "
            + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.addRecord(recordKey,
                                                 record.toString(),
                                                 flags);

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in adding record: " + testData);
                }

                // check if we are expecting info
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    // parse the result as JSON and check that it parses
                    JsonObject jsonObject = parseJsonObject(result);

                    assertTrue(jsonObject.containsKey("DATA_SOURCE"),
                               "Info message lacking DATA_SOURCE key: "
                               + testData);
                    assertTrue(jsonObject.containsKey("RECORD_ID"),
                                "Info message lacking RECORD_ID key: " 
                                + testData);
                    assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                "Info message lacking AFFECTED_ENTITIES key: "
                                + testData);
                } else {
                    assertEquals(SzCoreEngine.NO_INFO, result,
                                "No INFO requested, but non-empty response received: "
                                + testData);
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

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed adding a record: "
                         + testData + ", " + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "addRecord() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }

    @Test
    @Order(20)
    void testGetStats() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String stats = engine.getStats();

                assertNotNull(stats, "Stats was unexpectedly null");
                
                try {
                    JsonUtilities.parseJsonObject(stats);
                } catch (Exception e) {
                    fail("Stats were not parseable as a JSON: " + stats);
                }

            } catch (SzException e) {
                fail("Getting stats failed with an exception", e);
            }

        });
    }


    public List<Arguments> getReevaluateRecordArguments() {
        List<Arguments> result = new LinkedList<>();
        int errorCase = 0;

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

        for (SzRecordKey key : LOADED_RECORD_MAP.keySet()) {
            Class<?>    exceptionType   = null;
            Set<SzFlag> flagSet         = flagSetIter.next();
            
            switch (errorCase) {
                case 0:
                // do a bad data source
                key = SzRecordKey.of(UNKNOWN_DATA_SOURCE, key.recordId());
                exceptionType = SzUnknownDataSourceException.class;

                break;
                case 1:
                // do a good data source with a bad record ID
                key = SzRecordKey.of(key.dataSourceCode(), key.recordId() + "-UNKNOWN");

                // Per decision, reevaluate record silently does nothing if record ID not found
                // exceptionType = SzNotFoundException.class;

                break;
                default:
                // do nothing
            }
            errorCase++;
            result.add(Arguments.of(key, flagSet, exceptionType));
        };

        result.add(Arguments.of(
            SzRecordKey.of(PASSENGERS_DATA_SOURCE, "XXX000"), 
            SZ_NO_FLAGS,
            null));

        result.add(Arguments.of(
            SzRecordKey.of(PASSENGERS_DATA_SOURCE, "XXX000"), 
            SZ_WITH_INFO_FLAGS,
            null));
                
        return result;
    }

    @ParameterizedTest
    @MethodSource("getReevaluateRecordArguments")
    @Order(40)
    void testReevaluateRecord(SzRecordKey   recordKey, 
                              Set<SzFlag>   flags,
                              Class<?>      expectedExceptionType)
    {
        String testData = "recordKey=[ " + recordKey 
            + " ], withFlags=[ " + SzFlag.toString(flags) 
            + " ], expectedException=[ " + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.reevaluateRecord(recordKey, flags);

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in reevaluating record: "
                         + testData);
                }

                // check if we are expecting info
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    // parse the result as JSON and check that it parses
                    JsonObject jsonObject = parseJsonObject(result);

                    if (jsonObject.size() > 0) {
                        assertTrue(jsonObject.containsKey("DATA_SOURCE"),
                                "Info message lacking DATA_SOURCE key: " + testData);
                        assertTrue(jsonObject.containsKey("RECORD_ID"),
                                    "Info message lacking RECORD_ID key: " + testData);
                        assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                    "Info message lacking AFFECTED_ENTITIES key: " + testData);
                    }
                } else {
                    assertEquals(SzCoreEngine.NO_INFO, result,
                                "No INFO requested, but non-empty response received");
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

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed reevaluating a record: "
                         + testData + ", " + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "Reevaluating record failed with an "
                        + "unexpected exception type: " + testData
                        + ", " + description);
                }
            }
        });
    }

    public List<Arguments> getReevaluateEntityArguments() {
        List<Arguments> result = new LinkedList<>();
        int errorCase = 0;

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

        for (Long entityId : LOADED_RECORD_MAP.values()) {
            Class<?>    exceptionType   = null;
            Set<SzFlag> flagSet         = flagSetIter.next();
            
            switch (errorCase) {
                case 0:
                // do a negative entity ID
                entityId = -1 * entityId;
                // Per decision, reevaluate entity silently does nothing if enitty ID not found
                // exceptionType = SzNotFoundException.class;

                break;
                case 1:
                // do a large entity that does not exist
                entityId = 1000000000L;
                // Per decision, reevaluate entity silently does nothing if enitty ID not found
                // exceptionType = SzNotFoundException.class;

                break;
                default:
                // do nothing
            }
            errorCase++;
            result.add(Arguments.of(entityId, flagSet, exceptionType));
        };

        result.add(Arguments.of(100000000L, SZ_NO_FLAGS, null));
        result.add(Arguments.of(100000000L, SZ_WITH_INFO_FLAGS, null));
        
        return result;
    }

    @ParameterizedTest
    @MethodSource("getReevaluateEntityArguments")
    @Order(50)
    void testReevaluateEntity(Long          entityId,
                              Set<SzFlag>   flags,
                              Class<?>      expectedExceptionType) 
    {
        String testData = "entityId=[ " + entityId + " ], havingRecords=[ "
            + LOADED_ENTITY_MAP.get(entityId) + " ], withFlags=[ " 
            + SzFlag.toString(flags) + " ], expectedException=[ "
            + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.reevaluateEntity(entityId, flags);

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in reevaluating entity: " + testData);
                }

                // check if we are expecting info
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    // parse the result as JSON and check that it parses
                    JsonObject jsonObject = parseJsonObject(result);

                    if (jsonObject.size() > 0) {
                        assertTrue(jsonObject.containsKey("DATA_SOURCE"),
                                "Info message lacking DATA_SOURCE key: " + testData);
                        assertTrue(jsonObject.containsKey("RECORD_ID"),
                                    "Info message lacking RECORD_ID key: " + testData);
                        assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                    "Info message lacking AFFECTED_ENTITIES key: " + testData);
                    }
                } else {
                    assertEquals(SzCoreEngine.NO_INFO, result,
                                 "No INFO requested, but non-empty response received: "
                                 + testData);
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

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed reevaluating an entity: "
                         + testData + ", " + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "Reevaluating entity failed with an "
                        + "unexpected exception type: " + testData
                        + ", " + description);
                }
            }
        });
    }


    public List<Arguments> getDeleteRecordArguments() {
        List<Arguments> result = new LinkedList<>();
        int errorCase = 0;

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

        for (SzRecordKey key : LOADED_RECORD_MAP.keySet()) {
            Class<?>    exceptionType   = null;
            Set<SzFlag> flagSet         = flagSetIter.next();
            
            switch (errorCase) {
                case 0:
                // do a bad data source
                key = SzRecordKey.of(UNKNOWN_DATA_SOURCE, key.recordId());
                exceptionType = SzUnknownDataSourceException.class;

                break;
                case 1:
                // do a good data source with a bad record ID
                key = SzRecordKey.of(key.dataSourceCode(), key.recordId() + "-UNKNOWN");
                
                // NOTE: we expect no exception on deleting non-existent record

                break;
                default:
                // do nothing
            }
            errorCase++;
            result.add(Arguments.of(key, flagSet, exceptionType));
        };
        
        return result;
    }

    @ParameterizedTest
    @MethodSource("getDeleteRecordArguments")
    @Order(100)
    void testDeleteRecord(SzRecordKey  recordKey, 
                          Set<SzFlag>  flags,
                          Class<?>     expectedExceptionType)
    {
        String testData = "recordKey=[ " + recordKey 
            + " ], withFlags=[ " + SzFlag.toString(flags) 
            + " ], expectedException=[ " + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.deleteRecord(recordKey, flags);

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in deleting record: "
                         + testData);                    
                }
                
                // check if we are expecting info
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    // parse the result as JSON and check that it parses
                    JsonObject jsonObject = parseJsonObject(result);

                    assertTrue(jsonObject.containsKey("DATA_SOURCE"),
                               "Info message lacking DATA_SOURCE key: "
                               + testData);
                    assertTrue(jsonObject.containsKey("RECORD_ID"),
                                "Info message lacking RECORD_ID key: "
                                + testData);
                    assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                "Info message lacking AFFECTED_ENTITIES key: "
                                + testData);
                } else {
                    assertEquals(SzCoreEngine.NO_INFO, result,
                                "No INFO requested, but non-empty response received: "
                                + testData);
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

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed deleting a record: "
                         + testData + ", " + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "deleteRecord() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }

    @Test
    @Order(200)
    void testCountRedoRecordsZero() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                long count = engine.countRedoRecords();

                assertEquals(0, count, "Unexpected redo record count");

            } catch (SzException e) {
                fail("Failed to count records when none present", e);
            }
        });
    }

    @Test
    @Order(210)
    void testGetRedoRecordZero() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String redoRecord = engine.getRedoRecord();

                assertNull(redoRecord, "Unexpected non-null redo record: " + redoRecord);
                
            } catch (SzException e) {
                fail("Failed to get redo record when none present", e);
            }
        });
    }

    @Test
    @Order(220)
    void testCountRedoRecordsNonZero() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                for (SzRecord record : COUNT_REDO_TRIGGER_RECORDS) {
                    engine.addRecord(record.getRecordKey(), record.toString(), null); 
                }

                long count = engine.countRedoRecords();

                assertNotEquals(0, count, "Redo record count unexpectedly zero");
                
            } catch (SzException e) {
                fail("Failed to get redo record when none present", e);
            }
        });
    }

    @Test
    @Order(230)
    void testProcessRedoRecords() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                for (SzRecord record : PROCESS_REDO_TRIGGER_RECORDS) {
                    engine.addRecord(record.getRecordKey(), record.toString(), null); 
                }

                Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

                long redoCount = engine.countRedoRecords();

                assertNotEquals(0, redoCount, "Redo record count is zero (0)");
                assertTrue((redoCount > 0), "Redo record count is negative: " + redoCount);
                long actualCount = 0;
                for (String redoRecord = engine.getRedoRecord();
                     (redoRecord != null);
                     redoRecord = engine.getRedoRecord()) 
                {
                    actualCount++;
                    Set<SzFlag> flags = flagSetIter.next();

                    String result = engine.processRedoRecord(redoRecord, flags);

                    // check if we are expecting info
                    if (flags != null && flags.contains(SZ_WITH_INFO)) {
                        // parse the result as JSON and check that it parses
                        JsonObject jsonObject = parseJsonObject(result);

                        assertTrue(jsonObject.containsKey("DATA_SOURCE"),
                                   "Info message lacking DATA_SOURCE key for redo: "
                                   + redoRecord);

                        assertTrue(jsonObject.containsKey("RECORD_ID"),
                                   "Info message lacking RECORD_ID key for redo: "
                                   + redoRecord);

                        assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                   "Info message lacking AFFECTED_ENTITIES key for redo: "
                                   + redoRecord);
                    } else {
                        assertEquals(SzCoreEngine.NO_INFO, result,
                                     "No INFO requested, but non-empty response "
                                     + "received for redo: " + redoRecord);
                    }
                }

                assertEquals(redoCount, actualCount, "Not all redo records were processed");
                
            } catch (SzException e) {
                fail("Failed to get redo record when none present", e);
            }
        });
    }
}
