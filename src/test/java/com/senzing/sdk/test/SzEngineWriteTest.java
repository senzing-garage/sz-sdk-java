package com.senzing.sdk.test;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.util.JsonUtilities;

import javax.json.JsonObject;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.io.RecordReader.Format;
import com.senzing.sdk.SzBadInputException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.test.SzRecord.SzAddressByParts;
import com.senzing.sdk.test.SzRecord.SzDateOfBirth;
import com.senzing.sdk.test.SzRecord.SzFullAddress;
import com.senzing.sdk.test.SzRecord.SzFullName;
import com.senzing.sdk.test.SzRecord.SzNameByParts;
import com.senzing.sdk.test.SzRecord.SzPhoneNumber;
import com.senzing.sdk.test.SzRecord.SzSocialSecurity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.sdk.test.SdkTest.*;
import static com.senzing.io.RecordReader.Format;
import static com.senzing.io.RecordReader.Format.*;
import static com.senzing.sdk.test.SzEngineWriteTest.TestData.*;

/**
 * Unit tests for {@link SzEngine} write operations.
 */
public interface SzEngineWriteTest extends SdkTest {
    /**
     * The test data for the {@link SzEngineWriteTest} interface.
     */
    class TestData {
        /**
         * The data source code for the customers data source.
         */
        public static final String CUSTOMERS = "CUSTOMERS";

        /**
         * The data source code for the watchlist data source.
         */
        public static final String WATCHLIST = "WATCHLIST";

        /**
         * The data source code for the employees data source.
         */
        public static final String EMPLOYEES = "EMPLOYEES";
        
        /**
         * The data source code for the passengers data source.
         */
        public static final String PASSENGERS = "PASSENGERS";
        /**
         * The data source code for the VIP's data source.
         */
        public static final String VIPS = "VIPS";

        /**
         * The fake data source code for an unknown data source.
         */
        public static final String UNKNOWN_DATA_SOURCE = "UNKNOWN";
        
        /**
         * The test {@link SzRecord} for "Joe Schmoe".
         */
        public static final SzRecord RECORD_JOE_SCHMOE
            = new SzRecord(
                SzFullName.of("Joe Schmoe"),
                SzPhoneNumber.of("725-555-1313"),
                SzFullAddress.of("101 Main Street, Las Vegas, NV 89101"));
        
        /**
         * The test {@link SzRecord} for "Jane Smith".
         */
        public static final SzRecord RECORD_JANE_SMITH
            = new SzRecord(
                SzFullName.of("Jane Smith"),
                SzPhoneNumber.of("725-555-1414"),
                SzFullAddress.of("440 N Rancho Blvd, Las Vegas, NV 89101"));

        /**
         * The test {@link SzRecord} for "John Doe".
         */
        public static final SzRecord RECORD_JOHN_DOE
            = new SzRecord(
                SzFullName.of("John Doe"),
                SzPhoneNumber.of("725-555-1717"),
                SzFullAddress.of("777 W Sahara Blvd, Las Vegas, NV 89107"));

        /**
         * The test {@link SzRecord} for "James Moriarty".
         */
        public static final SzRecord RECORD_JAMES_MORIARTY
            = new SzRecord(
                SzNameByParts.of("James", "Moriarty"),
                SzPhoneNumber.of("44-163-555-1313"),
                SzFullAddress.of("16A Upper Montagu St, London, W1H 2PB, England"));

        /**
         * The test {@link SzRecord} for "Sherlock Holmes".
         */
        public static final SzRecord RECORD_SHERLOCK_HOLMES
            = new SzRecord(
                SzFullName.of("Sherlock Holmes"),
                SzPhoneNumber.of("44-163-555-1212"),
                SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));

        /**
         * The test {@link SzRecord} for "John Watson".
         */
        public static final SzRecord RECORD_JOHN_WATSON
            = new SzRecord(
                SzFullName.of("Dr. John H. Watson"),
                SzPhoneNumber.of("44-163-555-1414"),
                SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));
        
        /**
         * The test {@link SzRecord} for "Joann Smith".
         */
        public static final SzRecord RECORD_JOANN_SMITTH
            = new SzRecord(
                SzFullName.of("Joann Smith"),
                SzPhoneNumber.of("725-888-3939"),
                SzFullAddress.of("101 Fifth Ave, Las Vegas, NV 89118"),
                SzDateOfBirth.of("15-MAY-1983"));
            
        /**
         * The test {@link SzRecord} for "Bill Wright".
         */
        public static final SzRecord RECORD_BILL_WRIGHT
            = new SzRecord(
                SzNameByParts.of("Bill", "Wright", "AKA"),
                SzNameByParts.of("William", "Wright", "PRIMARY"),
                SzPhoneNumber.of("725-444-2121"),
                SzAddressByParts.of("101 Main StreetFifth Ave", "Las Vegas", "NV", "89118"),
                SzDateOfBirth.of("15-MAY-1983"));

        /**
         * The test {@link SzRecord} for "Craig Smith".
         */
        public static final SzRecord RECORD_CRAIG_SMITH
            = new SzRecord(
                SzNameByParts.of("Craig", "Smith"),
                SzPhoneNumber.of("725-888-3940"),
                SzFullAddress.of("101 Fifth Ave, Las Vegas, NV 89118"),
                SzDateOfBirth.of("12-JUN-1981"));
        
        /**
         * The test {@link SzRecord} for "Kim Long".
         */
        public static final SzRecord RECORD_KIM_LONG
            = new SzRecord(
                SzFullName.of("Kim Long"),
                SzPhoneNumber.of("725-135-1913"),
                SzFullAddress.of("451 Dover St., Las Vegas, NV 89108"),
                SzDateOfBirth.of("24-OCT-1976"));

        /**
         * The test {@link SzRecord} for "Kathy Osbourne".
         */
        public static final SzRecord RECORD_KATHY_OSBOURNE
            = new SzRecord(
                SzFullName.of("Kathy Osbourne"),
                SzPhoneNumber.of("725-111-2222"),
                SzFullAddress.of("707 Seventh Ave, Las Vegas, NV 89143"),
                SzDateOfBirth.of("24-OCT-1976"));
        
        /**
         * The {@link List} of {@link SzRecord} instances
         * to add as new records.  This includes:
         * <ol>
         *   <li>{@link #RECORD_BILL_WRIGHT}
         *   <li>{@link #RECORD_CRAIG_SMITH}
         *   <li>{@link #RECORD_JAMES_MORIARTY}
         *   <li>{@link #RECORD_JANE_SMITH}
         *   <li>{@link #RECORD_JOANN_SMITTH}
         *   <li>{@link #RECORD_JOE_SCHMOE}
         *   <li>{@link #RECORD_JOHN_DOE}
         *   <li>{@link #RECORD_JOHN_WATSON}
         *   <li>{@link #RECORD_KATHY_OSBOURNE}
         *   <li>{@link #RECORD_KIM_LONG}
         *   <li>{@link #RECORD_SHERLOCK_HOLMES}
         * </ol>
         */
        public static final List<SzRecord> NEW_RECORDS
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

        /**
         * The {@link List} of {@link SzRecordKey} instances for the
         * new records.
         */
        public static final List<SzRecordKey> NEW_RECORD_KEYS
            = List.of(
                SzRecordKey.of(CUSTOMERS, "ABC123"),
                SzRecordKey.of(WATCHLIST, "DEF456"),
                SzRecordKey.of(UNKNOWN_DATA_SOURCE, "GHI789"),
                SzRecordKey.of(CUSTOMERS, "JKL012"),
                SzRecordKey.of(WATCHLIST, "MNO345"),
                SzRecordKey.of(CUSTOMERS, "PQR678"),
                SzRecordKey.of(WATCHLIST, "STU901"),
                SzRecordKey.of(CUSTOMERS, "VWX234"),
                SzRecordKey.of(WATCHLIST, "YZA567"),
                SzRecordKey.of(CUSTOMERS, "BCD890"),
                SzRecordKey.of(WATCHLIST, "EFG123")
            );

        /**
         * The {@link Set} of {@link SzRecord} instances to trigger
         * a redo so {@link SzEngine#countRedoRecords()} can be tested.
         */
        public static final Set<SzRecord> COUNT_REDO_TRIGGER_RECORDS = Set.of(
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-01"),
                SzFullName.of("Scott Summers"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-02"),
                SzFullName.of("Jean Gray"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-03"),
                SzFullName.of("Charles Xavier"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-04"),
                SzFullName.of("Henry McCoy"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-05"),
                SzFullName.of("James Howlett"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-06"),
                SzFullName.of("Ororo Munroe"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-07"),
                SzFullName.of("Robert Drake"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-08"),
                SzFullName.of("Anna LeBeau"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-09"),
                SzFullName.of("Lucas Bishop"),
                SzSocialSecurity.of("999-99-9999")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-10"),
                SzFullName.of("Erik Lehnsherr"),
                SzSocialSecurity.of("999-99-9999")));

        /**
         * The {@link Set} of {@link SzRecord} instances to trigger
         * a redo so {@link SzEngine#processRedoRecord(String)} can be tested.
         */
        public static final Set<SzRecord> PROCESS_REDO_TRIGGER_RECORDS = Set.of(
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-11"),
                SzFullName.of("Anthony Stark"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-12"),
                SzFullName.of("Janet Van Dyne"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-13"),
                SzFullName.of("Henry Pym"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-14"),
                SzFullName.of("Bruce Banner"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-15"),
                SzFullName.of("Steven Rogers"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-16"),
                SzFullName.of("Clinton Barton"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-17"),
                SzFullName.of("Wanda Maximoff"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-18"),
                SzFullName.of("Victor Shade"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-19"),
                SzFullName.of("Natasha Romanoff"),
                SzSocialSecurity.of("888-88-8888")),
            new SzRecord(
                SzRecordKey.of(CUSTOMERS, "SAME-SSN-20"),
                SzFullName.of("James Rhodes"),
                SzSocialSecurity.of("888-88-8888")));

        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * write functions.
         */
        public static final List<Set<SzFlag>> WRITE_FLAG_SETS;
        static {
            List<Set<SzFlag>> list = new ArrayList<>(4);
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_WITH_INFO_FLAGS);
            WRITE_FLAG_SETS = Collections.unmodifiableList(list);
        }
        
        /**
         * The {@link List} of {@link Set} values containing 
         * the {@link SzFlag} instances to apply to the 
         * preprocess functions.
         */
        public static final List<Set<SzFlag>> PREPROCESS_FLAG_SETS;
        static {
            List<Set<SzFlag>> list = new LinkedList<>();
            list.add(null);
            list.add(SZ_NO_FLAGS);
            list.add(SZ_PREPROCESS_RECORD_DEFAULT_FLAGS);
            list.add(SZ_PREPROCESS_RECORD_ALL_FLAGS);
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_FEATURES,
                SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA)));
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_INTERNAL_FEATURES,
                SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS,
                SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS)));
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS,
                SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS)));
            list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA,
                SZ_ENTITY_INCLUDE_RECORD_JSON_DATA)));
            PREPROCESS_FLAG_SETS = Collections.unmodifiableList(list);
        }

        /**
         * The {@link SzRecordKey} for the passenger record
         * with record ID <code>"ABC123"</code>.
         */
        public static final SzRecordKey PASSENGER_ABC123
            = SzRecordKey.of(PASSENGERS, "ABC123");
        
        /**
         * The {@link SzRecordKey} for the passenger record
         * with record ID <code>"DEF456"</code>.
         */
        public static final SzRecordKey PASSENGER_DEF456
            = SzRecordKey.of(PASSENGERS, "DEF456");

        /**
         * The {@link SzRecordKey} for the passenger record
         * with record ID <code>"GHI789"</code>.
         */
        public static final SzRecordKey PASSENGER_GHI789 
            = SzRecordKey.of(PASSENGERS, "GHI789");

        /**
         * The {@link SzRecordKey} for the passenger record
         * with record ID <code>"JKL012"</code>.
         */
        public static final SzRecordKey PASSENGER_JKL012
            = SzRecordKey.of(PASSENGERS, "JKL012");

        /**
         * The {@link SzRecordKey} for the employee record
         * with record ID <code>"MNO345"</code>.
         */
        public static final SzRecordKey EMPLOYEE_MNO345
            = SzRecordKey.of(EMPLOYEES, "MNO345");

        /**
         * The {@link SzRecordKey} for the employee record
         * with record ID <code>"PQR678"</code>.
         */
        public static final SzRecordKey EMPLOYEE_PQR678
            = SzRecordKey.of(EMPLOYEES, "PQR678");

        /**
         * The {@link SzRecordKey} for the employee record
         * with record ID <code>"ABC567"</code>.
         */
        public static final SzRecordKey EMPLOYEE_ABC567
            = SzRecordKey.of(EMPLOYEES, "ABC567");
            
        /**
         * The {@link SzRecordKey} for the employee record
         * with record ID <code>"DEF890"</code>.
         */
        public static final SzRecordKey EMPLOYEE_DEF890
            = SzRecordKey.of(EMPLOYEES, "DEF890");

        /**
         * The {@link SzRecordKey} for the VIP record
         * with record ID <code>"STU901"</code>.
         */
        public static final SzRecordKey VIP_STU901
            = SzRecordKey.of(VIPS, "STU901");

        /**
         * The {@link SzRecordKey} for the VIP record
         * with record ID <code>"XYZ234"</code>.
         */
        public static final SzRecordKey VIP_XYZ234
            = SzRecordKey.of(VIPS, "XYZ234");

        /**
         * The {@link SzRecordKey} for the VIP record
         * with record ID <code>"GHI123"</code>.
         */
        public static final SzRecordKey VIP_GHI123
            = SzRecordKey.of(VIPS, "GHI123");

        /**
         * The {@link SzRecordKey} for the VIP record
         * with record ID <code>"JKL456"</code>.
         */
        public static final SzRecordKey VIP_JKL456
            = SzRecordKey.of(VIPS, "JKL456");
        
        /**
         * The {@link List} of {@link SzRecordKey} instances
         * identifying records that already exist at the start
         * of the test run.
         */
        public static final List<SzRecordKey> EXISTING_RECORD_KEYS
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

        /**
         * Prepares a {@link File} of passenger data for the
         * {@link #PASSENGERS} data source. 
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
                {PASSENGER_ABC123.recordId(), "Joseph", "Schmidt", "818-555-1212", "818-777-2424",
                    "101 Main Street, Los Angeles, CA 90011", "12-JAN-1981"},
                {PASSENGER_DEF456.recordId(), "Joann", "Smith", "818-555-1212", "818-888-3939",
                    "101 Fifth Ave, Los Angeles, CA 90018", "15-MAR-1982"},
                {PASSENGER_GHI789.recordId(), "John", "Parker", "818-555-1313", "818-999-2121",
                    "101 Fifth Ave, Los Angeles, CA 90018", "17-DEC-1977"},
                {PASSENGER_JKL012.recordId(), "Jane", "Donaldson", "818-555-1313", "818-222-3131",
                    "400 River Street, Pasadena, CA 90034", "23-MAY-1973"}
            };
            return prepareDataFile(format, "test-passengers-", headers, passengers);
        }

        /**
         * Prepares a {@link File} of employee data for the
         * {@link #EMPLOYEES} data source. 
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
         * Prepares a {@link File} of VIP data for the
         * {@link #VIPS} data source. 
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
            loader.configureDataSources(
                CUSTOMERS, WATCHLIST, PASSENGERS, EMPLOYEES, VIPS);
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
     * Gets the {@link List} of {@link Arguments} for testing the
     * {@link SzEngine#preprocessRecord(String, Set)} function.
     * The {@link Arguments} include the following:
     * <ol>
     *  <li>The {@link SzRecord} to preprocess.
     *  <li>The {@link Set} of {@link SzFlag} instances to use, or
     *      <code>null</code> if no flags.
     *  <li>The {@link Class} for the expected exception type, or
     *      <code>null</code> if no exception is expected.
     * </ol>
     * @return The {@link List} of {@link Arguments} for the parameters.
     */
    default List<Arguments> getPreprocessRecordArguments() {
        List<Arguments> result = new LinkedList<>();
        int count = Math.min(NEW_RECORD_KEYS.size(), NEW_RECORDS.size());
        Iterator<SzRecordKey>   keyIter     = NEW_RECORD_KEYS.iterator();
        Iterator<SzRecord>      recordIter  = NEW_RECORDS.iterator();

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(PREPROCESS_FLAG_SETS);

        for (int index = 0; index < count; index++) {
            SzRecordKey key             = keyIter.next();
            SzRecord    record          = recordIter.next();
            Class<?>    exceptionType   = null;
            Set<SzFlag> flagSet         = flagSetIter.next();

            record = new SzRecord(key, record);
            
            result.add(Arguments.of(record, flagSet, exceptionType));
        }

        return result;
    }


    /**
     * Tests the {@link SzEngine#preprocessRecord(String, Set)}
     * functionality.
     * 
     * @param record The {@link SzRecord} to preprocess.
     * @param flags The {@link Set} of {@link SzFlag} instances to 
     *              use or <code>null</code> if no flags.
     * @param expectedExceptionType The {@link Class} for the expected
     *                              exception type, or <code>null</code>
     *                              if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getPreprocessRecordArguments")
    @Order(100)
    default void testPreprocessRecord(SzRecord      record,
                                      Set<SzFlag>   flags,
                                      Class<?>      expectedExceptionType)
    {
        String testData = "record=[ " + record + " ], withFlags=[ " 
            + SzFlag.toString(flags) + " ], expectedException=[ "
            + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.preprocessRecord(record.toString(),
                                                        flags);

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in adding record: " + testData);
                }

                // parse the result as JSON and check that it parses
                JsonObject jsonObject = parseJsonObject(result);

                if (flags == null || flags.size() == 0) {
                    assertEquals(0, jsonObject.size(),
                                "Unexpected return properties on preprocess: "
                                + testData + ", " + result);
                } else {
                    if (flags.contains(SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA)) {
                        assertTrue(jsonObject.containsKey("UNMAPPED_DATA"), 
                            "Did not get UNMAPPED_DATA property with "
                            + "SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA: " 
                            + testData + ", " + result);
                    }
                    if (flags.contains(SZ_ENTITY_INCLUDE_RECORD_JSON_DATA)) {
                        assertTrue(jsonObject.containsKey("JSON_DATA"), 
                            "Did not get JSON_DATA property with "
                            + "SZ_ENTITY_INCLUDE_RECORD_JSON_DATA: " 
                            + testData + ", " + result);
                    }
                    if (flags.contains(SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS)) {
                        assertTrue(jsonObject.containsKey("FEATURES"), 
                            "Did not get FEATURES property with "
                            + "SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS: " 
                            + testData + ", " + result);
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

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed preprocessing a record: "
                         + testData + ", " + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "preprocessRecord() failed with an unexpected exception type: "
                        + testData + ", " + description);
                }
            }
        });
    }

    /**
     * Gets the {@link List} of {@link Arguments} for the parmeters for
     * testing the {@link SzEngine#addRecord(SzRecordKey, String, Set)}.
     * The {@link Arguments} included are as follows:
     * <ol>
     *  <li>The {@link SzRecordKey} for the record.
     *  <li>The {@link SzRecord} to be added.
     *  <li>The {@link Set} of {@link SzFlag} instances for the flags
     *      to use, or <code>null</code> if no flags.
     *  <li>The {@link Class} for the expected exception type, or
     *      <code>null</code> if no exception is expected.
     * </ol>
     * 
     * @return The {@link List} of {@link Arguments} for the parameters.
     */
    default List<Arguments> getAddRecordArguments() {
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
                        = (CUSTOMERS.equals(key.dataSourceCode()))
                        ? WATCHLIST
                        : CUSTOMERS;

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

    /**
     * Tests the {@link SzEngine#addRecord(SzRecordKey, String, Set)}
     * functionality.
     * 
     * @param recordKey The {@link SzRecordKey} for the record.
     * @param record The {@link SzRecord} describing the record.
     * @param flags The {@link Set} of {@link SzFlag} instances to
     *              use, or <code>null</code> if no flags should be used.
     * @param expectedExceptionType The {@link Class} for the expected
     *                              exception type, or <code>null</code>
     *                              if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getAddRecordArguments")
    @Order(200)
    default void testAddRecord(SzRecordKey  recordKey, 
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
                SzEngine engine = this.getEngine();

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
                    assertNull(result,
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

    /**
     * Tests the {@link SzEngine#getStats()} function.
     * 
     */
    @Test
    @Order(300)
    default void testGetStats() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

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

    /**
     * Gets the {@link List} of {@link Arguments} for the parameters to 
     * test {@link SzEngine#reevaluateRecord(SzRecordKey, Set)}.  The
     * {@link Arguments} include:
     * <ol>
     *  <li>The {@link SzRecordKey} for the record.
     *  <li>The {@link Set} of {@link SzFlag} instances for the flags
     *      to use, or <code>null</code> if no flags.
     *  <li>The {@link Class} for the expected exception type, or
     *      <code>null</code> if no exception is expected.
     * </li>
     * @return The {@link List} of {@link Arguments} for the parameters.
     */
    default List<Arguments> getReevaluateRecordArguments() {
        SzEntityLookup lookup = this.getTestData().getEntityLookup();

        List<Arguments> result = new LinkedList<>();
        int errorCase = 0;

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

        for (SzRecordKey key : lookup.getMapByRecordKey().keySet()) {
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
            SzRecordKey.of(PASSENGERS, "XXX000"), 
            SZ_NO_FLAGS,
            null));

        result.add(Arguments.of(
            SzRecordKey.of(PASSENGERS, "XXX000"), 
            SZ_WITH_INFO_FLAGS,
            null));
                
        return result;
    }

    /**
     * Tests the {@link SzEngine#reevaluateRecord(SzRecordKey, Set)}
     * funtionality.
     * 
     * @param recordKey The {@link SzRecordKey} for the record.
     * @param flags The {@link Set} of {@link SzFlag} instances to
     *              use, or <code>null</code> if no flags should be used.
     * @param expectedExceptionType The {@link Class} for the expected
     *                              exception type, or <code>null</code>
     *                              if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getReevaluateRecordArguments")
    @Order(400)
    default void testReevaluateRecord(SzRecordKey   recordKey, 
                                      Set<SzFlag>   flags,
                                      Class<?>      expectedExceptionType)
    {
        String testData = "recordKey=[ " + recordKey 
            + " ], withFlags=[ " + SzFlag.toString(flags) 
            + " ], expectedException=[ " + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

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
                    assertNull(result,
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

    /**
     * Gets the {@link List} of {@link Arguments} for the parameters to 
     * test {@link SzEngine#reevaluateEntity(long, Set)}.  The
     * {@link Arguments} include:
     * <ol>
     *  <li>The {@link Long} entity ID for the entity.
     *  <li>The {@link Set} of {@link SzFlag} instances for the flags
     *      to use, or <code>null</code> if no flags.
     *  <li>The {@link Class} for the expected exception type, or
     *      <code>null</code> if no exception is expected.
     * </li>
     * @return The {@link List} of {@link Arguments} for the parameters.
     */
    default List<Arguments> getReevaluateEntityArguments() {
        SzEntityLookup lookup = this.getTestData().getEntityLookup();

        List<Arguments> result = new LinkedList<>();
        int errorCase = 0;

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

        for (Long entityId : lookup.getMapByRecordKey().values()) {
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

    /**
     * Tests the {@link SzEngine#reevaluateEntity(long, Set)}
     * funtionality.
     * 
     * @param entityId The {@link Long} entity ID.
     * @param flags The {@link Set} of {@link SzFlag} instances to
     *              use, or <code>null</code> if no flags should be used.
     * @param expectedExceptionType The {@link Class} for the expected
     *                              exception type, or <code>null</code>
     *                              if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getReevaluateEntityArguments")
    @Order(500)
    default void testReevaluateEntity(Long          entityId,
                                      Set<SzFlag>   flags,
                                      Class<?>      expectedExceptionType) 
    {
        SzEntityLookup lookup = this.getTestData().getEntityLookup();

        String testData = "entityId=[ " + entityId + " ], havingRecords=[ "
            + lookup.getMapByEntityId().get(entityId) + " ], withFlags=[ " 
            + SzFlag.toString(flags) + " ], expectedException=[ "
            + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String result = engine.reevaluateEntity(entityId, flags);

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in reevaluating entity: " + testData);
                }

                // check if we are expecting info
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    // parse the result as JSON and check that it parses
                    JsonObject jsonObject = parseJsonObject(result);

                    if (jsonObject.size() > 0) {
                        assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                    "Info message lacking AFFECTED_ENTITIES key: " + testData);
                    }
                } else {
                    assertNull(result,
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

    /**
     * Gets the {@link List} of {@link Arguments} for the parameters to 
     * test {@link SzEngine#deleteRecord(SzRecordKey, Set)}.  The
     * {@link Arguments} include:
     * <ol>
     *  <li>The {@link SzRecordKey} for the record.
     *  <li>The {@link Set} of {@link SzFlag} instances for the flags
     *      to use, or <code>null</code> if no flags.
     *  <li>The {@link Class} for the expected exception type, or
     *      <code>null</code> if no exception is expected.
     * </li>
     * @return The {@link List} of {@link Arguments} for the parameters.
     */
    default List<Arguments> getDeleteRecordArguments() {
        SzEntityLookup lookup = this.getTestData().getEntityLookup();

        List<Arguments> result = new LinkedList<>();
        int errorCase = 0;

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);

        for (SzRecordKey key : lookup.getMapByRecordKey().keySet()) {
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

    /**
     * Tests the {@link SzEngine#deleteRecord(SzRecordKey, Set)}
     * funtionality.
     * 
     * @param recordKey The {@link SzRecordKey} for the record.
     * @param flags The {@link Set} of {@link SzFlag} instances to
     *              use, or <code>null</code> if no flags should be used.
     * @param expectedExceptionType The {@link Class} for the expected
     *                              exception type, or <code>null</code>
     *                              if no exception is expected.
     */
    @ParameterizedTest
    @MethodSource("getDeleteRecordArguments")
    @Order(600)
    default void testDeleteRecord(SzRecordKey  recordKey, 
                                  Set<SzFlag>  flags,
                                  Class<?>     expectedExceptionType)
    {
        String testData = "recordKey=[ " + recordKey 
            + " ], withFlags=[ " + SzFlag.toString(flags) 
            + " ], expectedException=[ " + expectedExceptionType + " ]";

        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

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
                    assertNull(result,
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

    /**
     * Tests the {@link SzEngine#countRedoRecords()} when there are no
     * redo records.
     */
    @Test
    @Order(700)
    default void testCountRedoRecordsZero() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                long count = engine.countRedoRecords();

                assertEquals(0, count, "Unexpected redo record count");

            } catch (SzException e) {
                fail("Failed to count records when none present", e);
            }
        });
    }

    /**
     * Tests the {@link SzEngine#getRedoRecord()} function when there
     * are no redo records.
     */
    @Test
    @Order(800)
    default void testGetRedoRecordZero() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                String redoRecord = engine.getRedoRecord();

                assertNull(redoRecord, "Unexpected non-null redo record: " + redoRecord);
                
            } catch (SzException e) {
                fail("Failed to get redo record when none present", e);
            }
        });
    }

    /**
     * Tests the {@link SzEngine#countRedoRecords()} function when 
     * there are redo records present.
     */
    @Test
    @Order(900)
    default void testCountRedoRecordsNonZero() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

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

    /**
     * Tests the {@link SzEngine#processRedoRecord(String,Set)} funtion.
     */
    @Test
    @Order(1000)
    default void testProcessRedoRecords() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

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
                        assertNull(result,
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
