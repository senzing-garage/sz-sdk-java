package com.senzing.sdk.core;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeSet;

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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzFlags;
import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzBadInputException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.util.CollectionUtilities.*;
import static com.senzing.util.JsonUtilities.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreEngineWhyTest extends AbstractTest {
    private static final String CUSTOMERS = "CUSTOMERS";
    private static final String COMPANIES = "COMPANIES";
    private static final String EMPLOYEES = "EMPLOYEES";
    private static final String PASSENGERS = "PASSENGERS";
    private static final String VIPS = "VIPS";
    private static final String CONTACTS = "CONTACTS";
    private static final String UNKNOWN_DATA_SOURCE = "UNKNOWN";

    private static final SzRecordKey ABC123 = SzRecordKey.of(PASSENGERS, "ABC123");
    private static final SzRecordKey DEF456 = SzRecordKey.of(PASSENGERS, "DEF456");
    private static final SzRecordKey GHI789 = SzRecordKey.of(PASSENGERS, "GHI789");
    private static final SzRecordKey JKL012 = SzRecordKey.of(PASSENGERS, "JKL012");
    private static final SzRecordKey MNO345 = SzRecordKey.of(CUSTOMERS, "MNO345");
    private static final SzRecordKey PQR678 = SzRecordKey.of(CUSTOMERS, "PQR678");
    private static final SzRecordKey ABC567 = SzRecordKey.of(CUSTOMERS, "ABC567");
    private static final SzRecordKey DEF890 = SzRecordKey.of(CUSTOMERS, "DEF890");
    private static final SzRecordKey STU901 = SzRecordKey.of(VIPS, "STU901");
    private static final SzRecordKey XYZ234 = SzRecordKey.of(VIPS, "XYZ234");
    private static final SzRecordKey GHI123 = SzRecordKey.of(VIPS, "GHI123");
    private static final SzRecordKey JKL456 = SzRecordKey.of(VIPS, "JKL456");

    private static final List<SzRecordKey> RECORD_KEYS;

    private static final SzRecordKey COMPANY_1 = SzRecordKey.of(COMPANIES, "COMPANY_1");
    private static final SzRecordKey COMPANY_2 = SzRecordKey.of(COMPANIES, "COMPANY_2");
    private static final SzRecordKey EMPLOYEE_1 = SzRecordKey.of(EMPLOYEES, "EMPLOYEE_1");
    private static final SzRecordKey EMPLOYEE_2 = SzRecordKey.of(EMPLOYEES, "EMPLOYEE_2");
    private static final SzRecordKey EMPLOYEE_3 = SzRecordKey.of(EMPLOYEES, "EMPLOYEE_3");
    private static final SzRecordKey CONTACT_1 = SzRecordKey.of(CONTACTS, "CONTACT_1");
    private static final SzRecordKey CONTACT_2 = SzRecordKey.of(CONTACTS, "CONTACT_2");
    private static final SzRecordKey CONTACT_3 = SzRecordKey.of(CONTACTS, "CONTACT_3");
    private static final SzRecordKey CONTACT_4 = SzRecordKey.of(CONTACTS, "CONTACT_4");

    private static final List<SzRecordKey> RELATED_RECORD_KEYS;

    static {
        List<SzRecordKey> recordKeys = new ArrayList<>(12);
        List<SzRecordKey> relatedKeys = new ArrayList<>(9);

        try {
            recordKeys.add(ABC123);
            recordKeys.add(DEF456);
            recordKeys.add(GHI789);
            recordKeys.add(JKL012);
            recordKeys.add(MNO345);
            recordKeys.add(PQR678);
            recordKeys.add(ABC567);
            recordKeys.add(DEF890);
            recordKeys.add(STU901);
            recordKeys.add(XYZ234);
            recordKeys.add(GHI123);
            recordKeys.add(JKL456);

            relatedKeys.add(COMPANY_1);
            relatedKeys.add(COMPANY_2);
            relatedKeys.add(EMPLOYEE_1);
            relatedKeys.add(EMPLOYEE_2);
            relatedKeys.add(EMPLOYEE_3);
            relatedKeys.add(CONTACT_1);
            relatedKeys.add(CONTACT_2);
            relatedKeys.add(CONTACT_3);
            relatedKeys.add(CONTACT_4);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);

        } finally {
            RECORD_KEYS = Collections.unmodifiableList(recordKeys);
            RELATED_RECORD_KEYS = Collections.unmodifiableList(relatedKeys);
        }
    }

    private static final List<Set<SzFlag>> WHY_ENTITIES_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new LinkedList<>();
        list.add(null);
        list.add(SZ_NO_FLAGS);
        list.add(SZ_WHY_ENTITIES_DEFAULT_FLAGS);
        list.add(SZ_WHY_ENTITIES_ALL_FLAGS);
        list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
        WHY_ENTITIES_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private static final List<Set<SzFlag>> WHY_SEARCH_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new LinkedList<>();
        list.add(null);
        list.add(SZ_NO_FLAGS);
        list.add(SZ_WHY_SEARCH_DEFAULT_FLAGS);
        list.add(SZ_WHY_SEARCH_ALL_FLAGS);
        list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_SEARCH_INCLUDE_REQUEST,
                SZ_SEARCH_INCLUDE_REQUEST_DETAILS,
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_SEARCH_INCLUDE_REQUEST_DETAILS,
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_SEARCH_INCLUDE_REQUEST,
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(
                EnumSet.of(SZ_SEARCH_INCLUDE_STATS, SZ_ENTITY_INCLUDE_ENTITY_NAME)));
        WHY_SEARCH_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private static final List<Set<SzFlag>> WHY_RECORDS_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new LinkedList<>();
        list.add(null);
        list.add(SZ_NO_FLAGS);
        list.add(SZ_WHY_RECORDS_DEFAULT_FLAGS);
        list.add(SZ_WHY_RECORDS_ALL_FLAGS);
        list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
        WHY_RECORDS_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private static final List<Set<SzFlag>> WHY_RECORD_IN_ENITTY_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new LinkedList<>();
        list.add(null);
        list.add(SZ_NO_FLAGS);
        list.add(SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS);
        list.add(SZ_WHY_RECORD_IN_ENTITY_ALL_FLAGS);
        list.add(Collections.unmodifiableSet(EnumSet.of(
                SZ_ENTITY_INCLUDE_ENTITY_NAME,
                SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                SZ_ENTITY_INCLUDE_RECORD_DATA,
                SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
        WHY_RECORD_IN_ENITTY_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private Map<SzRecordKey, Long> loadedRecordMap = new LinkedHashMap<>();

    private Map<Long, Set<SzRecordKey>> loadedEntityMap = new LinkedHashMap<>();

    private Map<SzRecordKey, String> attributesMap = new HashMap<>();

    private SzCoreEnvironment env = null;

    public Long getEntityId(SzRecordKey recordKey) {
        return this.loadedRecordMap.get(recordKey);
    }

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
            Set<SzRecordKey> recordKeys = new LinkedHashSet<>();
            recordKeys.addAll(RECORD_KEYS);
            recordKeys.addAll(RELATED_RECORD_KEYS);
            for (SzRecordKey key : recordKeys) {
                // clear the buffer
                sb.delete(0, sb.length());
                returnCode = nativeEngine.getEntityByRecordID(
                        key.dataSourceCode(), key.recordId(), sb);
                if (returnCode != 0) {
                    throw new RuntimeException(nativeEngine.getLastException());
                }
                // parse the JSON
                JsonObject jsonObj = parseJsonObject(sb.toString());
                JsonObject entity = getJsonObject(jsonObj, "RESOLVED_ENTITY");
                Long entityId = getLong(entity, "ENTITY_ID");
                this.loadedRecordMap.put(key, entityId);
                Set<SzRecordKey> recordKeySet = this.loadedEntityMap.get(entityId);
                if (recordKeySet == null) {
                    recordKeySet = new LinkedHashSet<>();
                    this.loadedEntityMap.put(entityId, recordKeySet);
                }
                recordKeySet.add(key);

                // lookup the record by record key
                sb.delete(0, sb.length());
                returnCode = nativeEngine.getRecord(
                        key.dataSourceCode(), key.recordId(),
                        SzFlags.SZ_ENTITY_INCLUDE_RECORD_JSON_DATA,
                        sb);
                if (returnCode != 0) {
                    throw new RuntimeException(nativeEngine.getLastException());
                }
                // parse the JSON
                jsonObj = parseJsonObject(sb.toString());
                jsonObj = jsonObj.getJsonObject("JSON_DATA");
                JsonObjectBuilder job = Json.createObjectBuilder(jsonObj);
                job.remove("RECORD_ID");
                job.remove("DATA_SOURCE");
                job.remove("RELATIONSHIP_LIST");
                String attributes = toJsonText(job);
                this.attributesMap.put(key, attributes);
            }
            ;

        } finally {
            nativeEngine.destroy();
            this.attributesMap = Collections.unmodifiableMap(this.attributesMap);
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
        dataSources.add(CUSTOMERS);
        dataSources.add(VIPS);
        dataSources.add(COMPANIES);
        dataSources.add(EMPLOYEES);
        dataSources.add(CONTACTS);

        File passengerFile = this.preparePassengerFile();
        File customerFile = this.prepareCustomerFile();
        File vipFile = this.prepareVipFile();

        File companyFile = this.prepareCompanyFile();
        File employeeFile = this.prepareEmployeeFile();
        File contactFile = this.prepareContactFile();

        customerFile.deleteOnExit();
        passengerFile.deleteOnExit();
        vipFile.deleteOnExit();
        companyFile.deleteOnExit();
        employeeFile.deleteOnExit();
        contactFile.deleteOnExit();

        RepositoryManager.configSources(repoDirectory,
                dataSources,
                true);

        RepositoryManager.loadFile(repoDirectory,
                passengerFile,
                PASSENGERS,
                true);

        RepositoryManager.loadFile(repoDirectory,
                customerFile,
                CUSTOMERS,
                true);

        RepositoryManager.loadFile(repoDirectory,
                vipFile,
                VIPS,
                true);

        RepositoryManager.loadFile(repoDirectory,
                companyFile,
                COMPANIES,
                true);

        RepositoryManager.loadFile(repoDirectory,
                employeeFile,
                EMPLOYEES,
                true);

        RepositoryManager.loadFile(repoDirectory,
                contactFile,
                CONTACTS,
                true);
    }

    private File preparePassengerFile() {
        String[] headers = {
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
                "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH" };

        String[][] passengers = {
                { ABC123.recordId(), "Joe", "Schmoe", "702-555-1212", "702-777-2424",
                        "101 Main Street, Las Vegas, NV 89101", "12-JAN-1981" },
                { DEF456.recordId(), "Joann", "Smith", "702-555-1212", "702-888-3939",
                        "101 Fifth Ave, Las Vegas, NV 10018", "15-MAY-1983" },
                { GHI789.recordId(), "John", "Doe", "818-555-1313", "818-999-2121",
                        "101 Fifth Ave, Las Vegas, NV 10018", "17-OCT-1978" },
                { JKL012.recordId(), "Jane", "Doe", "818-555-1313", "818-222-3131",
                        "400 River Street, Pasadena, CA 90034", "23-APR-1974" }
        };
        return this.prepareCSVFile("test-passengers-", headers, passengers);
    }

    private File prepareCustomerFile() {
        String[] headers = {
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
                "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH" };

        String[][] customers = {
                { MNO345.recordId(), "Joseph", "Schmoe", "702-555-1212", "702-777-2424",
                        "101 Main Street, Las Vegas, NV 89101", "12-JAN-1981" },
                { PQR678.recordId(), "Craig", "Smith", "212-555-1212", "702-888-3939",
                        "451 Dover Street, Las Vegas, NV 89108", "17-NOV-1982" },
                { ABC567.recordId(), "Kim", "Long", "702-246-8024", "702-135-7913",
                        "451 Dover Street, Las Vegas, NV 89108", "24-OCT-1976" },
                { DEF890.recordId(), "Kathy", "Osborne", "702-444-2121", "702-111-2222",
                        "707 Seventh Ave, Las Vegas, NV 89143", "27-JUL-1981" }
        };

        return this.prepareJsonArrayFile("test-customers-", headers, customers);
    }

    private File prepareVipFile() {
        String[] headers = {
                "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
                "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH" };

        String[][] vips = {
                { STU901.recordId(), "Martha", "Wayne", "818-891-9292", "818-987-1234",
                        "888 Sepulveda Blvd, Los Angeles, CA 90034", "27-NOV-1973" },
                { XYZ234.recordId(), "Jane", "Doe", "818-555-1313", "818-222-3131",
                        "400 River Street, Pasadena, CA 90034", "23-APR-1974" },
                { GHI123.recordId(), "Martha", "Kent", "818-333-5757", "702-123-9876",
                        "888 Sepulveda Blvd, Los Angeles, CA 90034", "17-OCT-1978" },
                { JKL456.recordId(), "Katherine", "Osborne", "702-444-2121", "702-111-2222",
                        "707 Seventh Ave, Las Vegas, NV 89143", "27-JUL-1981" }
        };

        return this.prepareJsonFile("test-vips-", headers, vips);
    }

    private File prepareCompanyFile() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("RECORD_ID", COMPANY_1.recordId());
        job.add("DATA_SOURCE", COMPANY_1.dataSourceCode());
        job.add("NAME_ORG", "Acme Corporation");
        JsonArrayBuilder relJab = Json.createArrayBuilder();
        JsonObjectBuilder relJob = Json.createObjectBuilder();
        relJob.add("REL_ANCHOR_DOMAIN", "EMPLOYER_ID");
        relJob.add("REL_ANCHOR_KEY", "ACME_CORP_KEY");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_ANCHOR_DOMAIN", "CORP_HIERARCHY");
        relJob.add("REL_ANCHOR_KEY", "ACME_CORP_KEY");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "CORP_HIERARCHY");
        relJob.add("REL_POINTER_KEY", "COYOTE_SOLUTIONS_KEY");
        relJob.add("REL_POINTER_ROLE", "ULTIMATE_PARENT");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "CORP_HIERARCHY");
        relJob.add("REL_POINTER_KEY", "COYOTE_SOLUTIONS_KEY");
        relJob.add("REL_POINTER_ROLE", "PARENT");
        relJab.add(relJob);
        job.add("RELATIONSHIP_LIST", relJab);

        jab.add(job);
        job = Json.createObjectBuilder();
        job.add("RECORD_ID", COMPANY_2.recordId());
        job.add("DATA_SOURCE", COMPANY_2.dataSourceCode());
        job.add("NAME_ORG", "Coyote Solutions");
        relJab = Json.createArrayBuilder();
        relJob = Json.createObjectBuilder();
        relJob.add("REL_ANCHOR_DOMAIN", "EMPLOYER_ID");
        relJob.add("REL_ANCHOR_KEY", "COYOTE_SOLUTIONS_KEY");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_ANCHOR_DOMAIN", "CORP_HIERARCHY");
        relJob.add("REL_ANCHOR_KEY", "COYOTE_SOLUTIONS_KEY");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "CORP_HIERARCHY");
        relJob.add("REL_POINTER_KEY", "ACME_CORP_KEY");
        relJob.add("REL_POINTER_ROLE", "SUBSIDIARY");
        relJab.add(relJob);
        job.add("RELATIONSHIP_LIST", relJab);
        jab.add(job);

        return this.prepareJsonFile("test-companies-", jab.build());
    }

    private File prepareEmployeeFile() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("RECORD_ID", EMPLOYEE_1.recordId());
        job.add("DATA_SOURCE", EMPLOYEE_1.dataSourceCode());
        job.add("NAME_FULL", "Jeff Founder");
        JsonArrayBuilder relJab = Json.createArrayBuilder();
        JsonObjectBuilder relJob = Json.createObjectBuilder();
        relJob.add("REL_ANCHOR_DOMAIN", "EMPLOYEE_NUM");
        relJob.add("REL_ANCHOR_KEY", "1");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "EMPLOYER_ID");
        relJob.add("REL_POINTER_KEY", "ACME_CORP_KEY");
        relJob.add("REL_POINTER_ROLE", "EMPLOYED_BY");
        relJab.add(relJob);
        job.add("RELATIONSHIP_LIST", relJab);
        jab.add(job);

        job = Json.createObjectBuilder();
        job.add("RECORD_ID", EMPLOYEE_2.recordId());
        job.add("DATA_SOURCE", EMPLOYEE_2.dataSourceCode());
        job.add("NAME_FULL", "Jane Leader");
        relJab = Json.createArrayBuilder();
        relJob = Json.createObjectBuilder();
        relJob.add("REL_ANCHOR_DOMAIN", "EMPLOYEE_NUM");
        relJob.add("REL_ANCHOR_KEY", "2");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "EMPLOYEE_NUM");
        relJob.add("REL_POINTER_KEY", "1");
        relJob.add("REL_POINTER_ROLE", "MANAGED_BY");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "EMPLOYER_ID");
        relJob.add("REL_POINTER_KEY", "ACME_CORP_KEY");
        relJob.add("REL_POINTER_ROLE", "EMPLOYED_BY");
        relJab.add(relJob);
        job.add("RELATIONSHIP_LIST", relJab);
        jab.add(job);

        job = Json.createObjectBuilder();
        job.add("RECORD_ID", EMPLOYEE_3.recordId());
        job.add("DATA_SOURCE", EMPLOYEE_3.dataSourceCode());
        job.add("NAME_FULL", "Joe Workman");
        relJab = Json.createArrayBuilder();
        relJob = Json.createObjectBuilder();
        relJob.add("REL_ANCHOR_DOMAIN", "EMPLOYEE_NUM");
        relJob.add("REL_ANCHOR_KEY", "6");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "EMPLOYEE_NUM");
        relJob.add("REL_POINTER_KEY", "2");
        relJob.add("REL_POINTER_ROLE", "MANAGED_BY");
        relJab.add(relJob);
        relJob = Json.createObjectBuilder();
        relJob.add("REL_POINTER_DOMAIN", "EMPLOYER_ID");
        relJob.add("REL_POINTER_KEY", "ACME_CORP_KEY");
        relJob.add("REL_POINTER_ROLE", "EMPLOYED_BY");
        relJab.add(relJob);
        job.add("RELATIONSHIP_LIST", relJab);
        jab.add(job);

        return this.prepareJsonFile("test-employees-", jab.build());
    }

    private File prepareContactFile() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("RECORD_ID", CONTACT_1.recordId());
        job.add("DATA_SOURCE", CONTACT_1.dataSourceCode());
        job.add("NAME_FULL", "Richard Couples");
        job.add("PHONE_NUMBER", "718-949-8812");
        job.add("ADDR_FULL", "10010 WOODLAND AVE; ATLANTA, GA 30334");
        JsonArrayBuilder relJab = Json.createArrayBuilder();
        JsonObjectBuilder relJob = Json.createObjectBuilder();
        relJob.add("RELATIONSHIP_TYPE", "SPOUSE");
        relJob.add("RELATIONSHIP_KEY", "SPOUSES-1-2");
        relJob.add("RELATIONSHIP_ROLE", "WIFE");
        relJab.add(relJob);
        job.add("RELATIONSHIP_LIST", relJab);
        jab.add(job);

        job = Json.createObjectBuilder();
        job.add("RECORD_ID", CONTACT_2.recordId());
        job.add("DATA_SOURCE", CONTACT_2.dataSourceCode());
        job.add("NAME_FULL", "Brianna Couples");
        job.add("PHONE_NUMBER", "718-949-8812");
        job.add("ADDR_FULL", "10010 WOODLAND AVE; ATLANTA, GA 30334");
        relJab = Json.createArrayBuilder();
        relJob = Json.createObjectBuilder();
        relJob.add("RELATIONSHIP_TYPE", "SPOUSE");
        relJob.add("RELATIONSHIP_KEY", "SPOUSES-1-2");
        relJob.add("RELATIONSHIP_ROLE", "HUSBAND");
        relJab.add(relJob);
        job.add("RELATIONSHIP_LIST", relJab);
        jab.add(job);

        job = Json.createObjectBuilder();
        job.add("RECORD_ID", CONTACT_3.recordId());
        job.add("DATA_SOURCE", CONTACT_3.dataSourceCode());
        job.add("NAME_FULL", "Samuel Strong");
        job.add("PHONE_NUMBER", "312-889-3340");
        job.add("ADDR_FULL", "10010 LAKE VIEW RD; SPRINGFIELD, MO 65807");
        jab.add(job);

        job = Json.createObjectBuilder();
        job.add("RECORD_ID", CONTACT_4.recordId());
        job.add("DATA_SOURCE", CONTACT_4.dataSourceCode());
        job.add("NAME_FULL", "Melissa Powers");
        job.add("PHONE_NUMBER", "312-885-4236");
        job.add("ADDR_FULL", "10010 LAKE VIEW RD; SPRINGFIELD, MO 65807");
        jab.add(job);

        return this.prepareJsonFile("test-contacts-", jab.build());
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

    public List<Arguments> getWhyEntitiesParameters() {
        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WHY_ENTITIES_FLAG_SETS);

        List<Arguments> result = new LinkedList<>();
        List<List<?>> recordKeyCombos = generateCombinations(
                RELATED_RECORD_KEYS, RELATED_RECORD_KEYS);

        Iterator<List<?>> iter = recordKeyCombos.iterator();
        while (iter.hasNext()) {
            List<?> list = (List<?>) iter.next();
            SzRecordKey key1 = (SzRecordKey) list.get(0);
            SzRecordKey key2 = (SzRecordKey) list.get(1);

            // thin the list out to reduce the number of tests
            if (key1.equals(key2)) {
                iter.remove();
            } else {
                int index1 = RELATED_RECORD_KEYS.indexOf(key1);
                int index2 = RELATED_RECORD_KEYS.indexOf(key2);
                if (Math.abs(index2 - index1) > 4) {
                    iter.remove();
                }
            }
        }

        final Class<?> NOT_FOUND = SzNotFoundException.class;

        for (List<?> recordKeyPair : recordKeyCombos) {
            SzRecordKey recordKey1 = (SzRecordKey) recordKeyPair.get(0);
            SzRecordKey recordKey2 = (SzRecordKey) recordKeyPair.get(1);

            result.add(Arguments.of(
                    "Test " + recordKey1 + " vs " + recordKey2,
                    recordKey1,
                    this.loadedRecordMap.get(recordKey1),
                    recordKey2,
                    this.loadedRecordMap.get(recordKey2),
                    flagSetIter.next(),
                    null));
        }

        result.add(Arguments.of(
                "Why entities with same entity twice: " + getEntityId(COMPANY_1),
                COMPANY_1,
                getEntityId(COMPANY_1),
                COMPANY_1,
                getEntityId(COMPANY_1),
                flagSetIter.next(),
                null));

        result.add(Arguments.of(
                "Not found entity ID test",
                SzRecordKey.of(PASSENGERS, "XXX000"),
                10000000L,
                COMPANY_1,
                this.loadedRecordMap.get(COMPANY_1),
                flagSetIter.next(),
                NOT_FOUND));

        result.add(Arguments.of(
                "Illegal entity ID test",
                SzRecordKey.of(PASSENGERS, "XXX000"),
                -100L,
                COMPANY_1,
                this.loadedRecordMap.get(COMPANY_1),
                flagSetIter.next(),
                NOT_FOUND));

        return result;
    }

    public List<Arguments> getWhySearchParameters() {
        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WHY_SEARCH_FLAG_SETS);

        Set<String> searchProfiles = new LinkedHashSet<>();
        searchProfiles.add(null);
        searchProfiles.add("SEARCH");
        searchProfiles.add("INGEST");
        Iterator<String> profileIter = circularIterator(searchProfiles);

        List<Arguments> result = new LinkedList<>();
        List<List<?>> recordKeyCombos = generateCombinations(
                RELATED_RECORD_KEYS, RELATED_RECORD_KEYS);

        Iterator<List<?>> iter = recordKeyCombos.iterator();
        while (iter.hasNext()) {
            List<?> list = (List<?>) iter.next();
            SzRecordKey key1 = (SzRecordKey) list.get(0);
            SzRecordKey key2 = (SzRecordKey) list.get(1);

            // thin the list out to reduce the number of tests
            if (key1.equals(key2)) {
                iter.remove();
            } else {
                int index1 = RELATED_RECORD_KEYS.indexOf(key1);
                int index2 = RELATED_RECORD_KEYS.indexOf(key2);
                if (Math.abs(index2 - index1) > 4) {
                    iter.remove();
                }
            }
        }

        final Class<?> NOT_FOUND = SzNotFoundException.class;

        for (List<?> recordKeyPair : recordKeyCombos) {
            SzRecordKey recordKey1 = (SzRecordKey) recordKeyPair.get(0);
            SzRecordKey recordKey2 = (SzRecordKey) recordKeyPair.get(1);

            String attributes = this.attributesMap.get(recordKey1);

            if (result.size() == 0) {
                result.add(Arguments.of(
                        "Bad search profile test",
                        this.attributesMap.get(recordKey1),
                        recordKey2,
                        this.loadedRecordMap.get(recordKey2),
                        "BAD_SEARCH_PROFILE",
                        flagSetIter.next(),
                        SzBadInputException.class));

            }

            String profile = profileIter.next();
            result.add(Arguments.of(
                    "Test " + recordKey2 + " vs " + attributes + " with " + profile,
                    this.attributesMap.get(recordKey1),
                    recordKey2,
                    this.loadedRecordMap.get(recordKey2),
                    profile,
                    flagSetIter.next(),
                    null));
        }

        result.add(Arguments.of(
                "Why search with entity against its own attributes: " + getEntityId(COMPANY_1),
                this.attributesMap.get(COMPANY_1),
                COMPANY_1,
                getEntityId(COMPANY_1),
                profileIter.next(),
                flagSetIter.next(),
                null));

        result.add(Arguments.of(
                "Not found entity ID test",
                this.attributesMap.get(COMPANY_1),
                SzRecordKey.of(PASSENGERS, "XXX000"),
                10000000L,
                null,
                flagSetIter.next(),
                NOT_FOUND));

        result.add(Arguments.of(
                "Illegal entity ID test",
                this.attributesMap.get(COMPANY_1),
                SzRecordKey.of(PASSENGERS, "XXX000"),
                -100L,
                null,
                flagSetIter.next(),
                NOT_FOUND));

        return result;
    }

    public static void validateWhyEntities(String whyEntitiesResult,
            String testData,
            SzRecordKey recordKey1,
            long entityId1,
            SzRecordKey recordKey2,
            long entityId2,
            Set<SzFlag> flags) {
        JsonObject jsonObject = parseJsonObject(whyEntitiesResult);

        JsonArray whyResults = getJsonArray(jsonObject, "WHY_RESULTS");

        assertNotNull(whyResults,
                "Missing WHY_RESULTS from whyEntities() result JSON: " + testData);

        assertEquals(1, whyResults.size(),
                "The WHY_RESULTS array is not of the expected size: " + testData);

        JsonObject whyResult = getJsonObject(whyResults, 0);

        assertNotNull(whyResult,
                "First WHY_RESULTS element was null: " + testData);

        Long whyId1 = getLong(whyResult, "ENTITY_ID");

        assertNotNull(whyId1, "First entity ID was null: whyResult=[ "
                + whyResult + " ], " + testData);

        Long whyId2 = getLong(whyResult, "ENTITY_ID_2");

        assertNotNull(whyId2, "Second entity ID was null: whyResult=[ "
                + whyResult + " ], " + testData);

        Set<Long> entityIds = new TreeSet<>();
        entityIds.add(whyId1);
        entityIds.add(whyId2);

        assertTrue(entityIds.contains(entityId1),
                "First entity ID not found in why result: whyResult=[ "
                        + whyResult + " ], " + testData);

        assertTrue(entityIds.contains(entityId2),
                "Second entity ID not found in why result: whyResult=[ "
                        + whyResult + " ], " + testData);

        JsonArray entities = getJsonArray(jsonObject, "ENTITIES");

        assertNotNull(entities,
                "Entity details are missing: " + testData);

        assertEquals(entityIds.size(), entities.size(),
                "Unexpected number of entities in entity details. testData=[ "
                        + testData + " ]");

        // check that the entities we found are those requested
        Set<Long> detailEntityIds = new HashSet<>();
        for (JsonObject entity : entities.getValuesAs(JsonObject.class)) {
            assertNotNull(entity, "Entity detail was null: "
                    + entities + ", " + testData);

            entity = getJsonObject(entity, "RESOLVED_ENTITY");
            assertNotNull(entity, "Resolved entity in details was null: "
                    + entities + ", " + testData);

            // get the entity ID
            Long id = getLong(entity, "ENTITY_ID");
            assertNotNull(
                    id, "The entity detail was missing or has a null "
                            + "ENTITY_ID: " + entity + ", " + testData);

            // add to the ID set
            detailEntityIds.add(id);
        }

        assertEquals(entityIds, detailEntityIds,
                "Entity detail entity ID's are not as expected: " + testData);
    }

    public static void validateWhySearch(String whySearchResult,
            String testData,
            String attributes,
            SzRecordKey recordKey,
            long entityId,
            Set<SzFlag> flags) {
        JsonObject jsonObject = parseJsonObject(whySearchResult);

        JsonArray whyResults = getJsonArray(jsonObject, "WHY_RESULTS");

        assertNotNull(whyResults,
                "Missing WHY_RESULTS from whySearch() result JSON: " + testData);

        assertEquals(1, whyResults.size(),
                "The WHY_RESULTS array is not of the expected size: " + testData);

        JsonObject whyResult = getJsonObject(whyResults, 0);

        assertNotNull(whyResult,
                "First WHY_RESULTS element was null: " + testData);

        JsonObject searchRequest = getJsonObject(jsonObject, "SEARCH_REQUEST");

        if (flags != null
                && (flags.contains(SZ_SEARCH_INCLUDE_REQUEST)
                        || flags.contains(SZ_SEARCH_INCLUDE_REQUEST_DETAILS))) {
            assertNotNull(searchRequest,
                    "Missing SEARCH_REQUEST from whySearch() result JSON: " + testData);
        } else {
            assertNull(searchRequest,
                    "Unexpected SEARCH_REQUEST in whySearch() result JSON: " + testData);
        }

        Long whyId = getLong(whyResult, "ENTITY_ID");

        assertNotNull(whyId, "The why entity ID was null: whyResult=[ "
                + whyResult + " ], " + testData);

        assertEquals(entityId, whyId,
                "The entity ID (" + entityId
                        + ") not found in why result: whyResult=[ "
                        + whyResult + " ], " + testData);

        JsonArray entities = getJsonArray(jsonObject, "ENTITIES");

        assertNotNull(entities,
                "Entity details are missing: " + testData);

        assertEquals(1, entities.size(),
                "Unexpected number of entities in entity details. testData=[ "
                        + testData + " ]");

        // check that the entities we found are those requested
        Set<Long> detailEntityIds = new HashSet<>();
        for (JsonObject entity : entities.getValuesAs(JsonObject.class)) {
            assertNotNull(entity, "Entity detail was null: "
                    + entities + ", " + testData);

            entity = getJsonObject(entity, "RESOLVED_ENTITY");
            assertNotNull(entity, "Resolved entity in details was null: "
                    + entities + ", " + testData);

            // get the entity ID
            Long id = getLong(entity, "ENTITY_ID");
            assertNotNull(
                    id, "The entity detail was missing or has a null "
                            + "ENTITY_ID: " + entity + ", " + testData);

            // add to the ID set
            detailEntityIds.add(id);
        }

        assertEquals(1, detailEntityIds.size(),
                "Entity detail entity ID count is not as expected: "
                        + testData);

        assertTrue(detailEntityIds.contains(entityId),
                "Entity ID (" + entityId
                        + ") not found in detail entity ID's: "
                        + testData);
    }

    @ParameterizedTest
    @MethodSource("getWhySearchParameters")
    void testWhySearch(String testDescription,
            String attributes,
            SzRecordKey recordKey,
            long entityId,
            String searchProfile,
            Set<SzFlag> flags,
            Class<?> exceptionType) {
        StringBuilder sb = new StringBuilder(
                "description=[ " + testDescription + " ], attributes=[ "
                        + attributes + " ], recordKey=[ " + recordKey
                        + " ], entityId=[ " + entityId + " ], searchProfile=[ "
                        + searchProfile + " ], flags=[ " + SzFlag.toString(flags)
                        + " ], expectedException=[ " + exceptionType + " ]");

        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.whySearch(attributes, entityId, searchProfile, flags);

                if (exceptionType != null) {
                    fail("Unexpectedly succeeded whySearch(): " + testData);
                }

                validateWhySearch(result,
                        testDescription,
                        attributes,
                        recordKey,
                        entityId,
                        flags);

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
                    fail("Unexpectedly failed whySearch(): "
                            + testData + ", " + description, e);

                } else if (exceptionType != e.getClass()) {
                    assertInstanceOf(
                            exceptionType, e,
                            "whySearch() failed with an unexpected exception type: "
                                    + testData + ", " + description);
                }
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getWhyEntitiesParameters")
    void testWhyEntities(String testDescription,
            SzRecordKey recordKey1,
            long entityId1,
            SzRecordKey recordKey2,
            long entityId2,
            Set<SzFlag> flags,
            Class<?> exceptionType) {
        StringBuilder sb = new StringBuilder(
                "description=[ " + testDescription + " ], recordKey1=[ "
                        + recordKey1 + " ], entityId1=[ " + entityId1
                        + " ], recordKey2=[ " + recordKey2 + " ], entityId2=[ "
                        + entityId2 + " ], flags=[ " + SzFlag.toString(flags)
                        + " ], expectedException=[ " + exceptionType + " ]");

        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.whyEntities(entityId1, entityId2, flags);

                if (exceptionType != null) {
                    fail("Unexpectedly succeeded whyEntities(): " + testData);
                }

                validateWhyEntities(result,
                        testData,
                        recordKey1,
                        entityId1,
                        recordKey2,
                        entityId2,
                        flags);

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

    public List<Arguments> getWhyRecordInEntityParameters() {
        List<Arguments> result = new LinkedList<>();

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WHY_RECORD_IN_ENITTY_FLAG_SETS);

        final Class<?> NOT_FOUND = SzNotFoundException.class;
        final Class<?> UNKNOWN_SOURCE = SzUnknownDataSourceException.class;

        for (SzRecordKey recordKey : RECORD_KEYS) {
            result.add(Arguments.of(
                    "Why " + recordKey + " in Entity " + this.loadedRecordMap.get(recordKey),
                    recordKey,
                    flagSetIter.next(),
                    null));
        }

        result.add(Arguments.of(
                "Unknown data source code test",
                SzRecordKey.of(UNKNOWN_DATA_SOURCE, "ABC123"),
                flagSetIter.next(),
                UNKNOWN_SOURCE));

        result.add(Arguments.of(
                "Not found record ID test",
                SzRecordKey.of(PASSENGERS, "XXX000"),
                flagSetIter.next(),
                NOT_FOUND));

        return result;
    }

    public void validateWhyRecordInEntity(String whyResultJson,
            String testData,
            SzRecordKey recordKey,
            Set<SzFlag> flags) {
        JsonObject jsonObject = parseJsonObject(whyResultJson);

        JsonArray whyResults = getJsonArray(jsonObject, "WHY_RESULTS");

        assertNotNull(whyResults,
                "Missing WHY_RESULTS from whyEntities() result JSON: " + testData);

        assertEquals(1, whyResults.size(),
                "The WHY_RESULTS array is not of the expected size: " + testData);

        JsonObject whyResult = getJsonObject(whyResults, 0);

        assertNotNull(whyResult,
                "First WHY_RESULTS element was null: " + testData);

        Long whyId = getLong(whyResult, "ENTITY_ID");

        assertNotNull(whyId, "The entity ID was null: whyResult=[ "
                + whyResult + " ], " + testData);

        long entityId = this.loadedRecordMap.get(recordKey);

        assertEquals(entityId, whyId,
                "The entity ID in the why result was not as expected: "
                        + "whyResult=[ " + whyResult + " ], " + testData);

        JsonArray entities = getJsonArray(jsonObject, "ENTITIES");

        assertNotNull(entities,
                "Entity details are missing: " + testData);

        assertEquals(1, entities.size(),
                "Unexpected number of entities in entity details. testData=[ "
                        + testData + " ]");

        JsonArray focusRecords = getJsonArray(whyResult, "FOCUS_RECORDS");

        assertNotNull(focusRecords, "The FOCUS_RECORDS array is missing "
                + "from the why results: whyResult=[ " + whyResult + " ], "
                + testData);

        assertEquals(1, focusRecords.size(),
                "Size of FOCUS_RECORDS array not as expected: focusRecords=[ "
                        + focusRecords + " ], " + testData);

        JsonObject focusRecord = getJsonObject(focusRecords, 0);
        assertNotNull(focusRecords, "The first element of FOCUS_RECORDS array "
                + "is missing or null: focusRecords=[ " + focusRecords + " ], "
                + testData);

        String dataSource = getString(focusRecord, "DATA_SOURCE");
        String recordId = getString(focusRecord, "RECORD_ID");

        assertEquals(recordKey.dataSourceCode(), dataSource,
                "Focus record data source is not as expected: focusRecord=[ "
                        + focusRecord + " ], " + testData);

        assertEquals(recordKey.recordId(), recordId,
                "Focus record ID is not as expected: focusRecord=[ "
                        + focusRecord + " ], " + testData);

        // check that the entities we found are those requested
        Set<Long> detailEntityIds = new HashSet<>();
        for (JsonObject entity : entities.getValuesAs(JsonObject.class)) {
            assertNotNull(entity, "Entity detail was null: "
                    + entities + ", " + testData);

            entity = getJsonObject(entity, "RESOLVED_ENTITY");
            assertNotNull(entity, "Resolved entity in details was null: "
                    + entities + ", " + testData);

            // get the entity ID
            Long id = getLong(entity, "ENTITY_ID");
            assertNotNull(
                    id, "The entity detail was missing or has a null "
                            + "ENTITY_ID: " + entity + ", " + testData);

            // add to the ID set
            detailEntityIds.add(id);
        }

        assertEquals(Set.of(entityId), detailEntityIds,
                "Entity detail entity ID's are not as expected: " + testData);
    }

    @ParameterizedTest
    @MethodSource("getWhyRecordInEntityParameters")
    void testWhyRecordInEntity(String testDescription,
            SzRecordKey recordKey,
            Set<SzFlag> flags,
            Class<?> exceptionType) {
        StringBuilder sb = new StringBuilder(
                "description=[ " + testDescription + " ], recordKey=[ "
                        + recordKey + " ], flags=[ " + SzFlag.toString(flags)
                        + " ], expectedException=[ " + exceptionType + " ]");

        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.whyRecordInEntity(recordKey, flags);

                if (exceptionType != null) {
                    fail("Unexpectedly succeeded whyRecordInEntity(): "
                            + testData);
                }

                validateWhyRecordInEntity(
                        result, testData, recordKey, flags);

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
                    fail("Unexpectedly failed whyRecordInEntity(): "
                            + testData + ", " + description, e);

                } else if (exceptionType != e.getClass()) {
                    assertInstanceOf(
                            exceptionType, e,
                            "whyRecordInEntity() failed with an unexpected "
                                    + "exception type: " + testData + ", " + description);
                }
            }
        });
    }

    public static List<Arguments> getWhyRecordsParameters() {
        List<Arguments> result = new LinkedList<>();

        List<List<?>> recordKeyCombos = generateCombinations(
                RECORD_KEYS, RECORD_KEYS);

        Iterator<List<?>> iter = recordKeyCombos.iterator();
        while (iter.hasNext()) {
            List<?> list = (List<?>) iter.next();
            SzRecordKey key1 = (SzRecordKey) list.get(0);
            SzRecordKey key2 = (SzRecordKey) list.get(1);

            // thin the list out to reduce the number of tests
            if (key1.equals(key2)) {
                iter.remove();
            } else {
                int index1 = RECORD_KEYS.indexOf(key1);
                int index2 = RECORD_KEYS.indexOf(key2);
                if (Math.abs(index2 - index1) > 4) {
                    iter.remove();
                }
            }
        }

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WHY_RECORDS_FLAG_SETS);

        final Class<?> NOT_FOUND = SzNotFoundException.class;
        final Class<?> UNKNOWN_SOURCE = SzUnknownDataSourceException.class;

        for (List<?> recordKeys : recordKeyCombos) {
            SzRecordKey recordKey1 = (SzRecordKey) recordKeys.get(0);
            SzRecordKey recordKey2 = (SzRecordKey) recordKeys.get(1);

            result.add(Arguments.of(
                    "Why " + recordKey1 + " versus " + recordKey2,
                    recordKey1,
                    recordKey2,
                    flagSetIter.next(),
                    null));
        }

        result.add(Arguments.of(
                "Why records with same record twice: " + RECORD_KEYS.get(0),
                RECORD_KEYS.get(0),
                RECORD_KEYS.get(0),
                flagSetIter.next(),
                null));

        result.add(Arguments.of(
                "Unknown data source code test",
                SzRecordKey.of(UNKNOWN_DATA_SOURCE, "ABC123"),
                DEF890,
                flagSetIter.next(),
                UNKNOWN_SOURCE));

        result.add(Arguments.of(
                "Not found record ID test",
                SzRecordKey.of(PASSENGERS, "XXX000"),
                DEF890,
                flagSetIter.next(),
                NOT_FOUND));

        return result;
    }

    public void validateWhyRecords(String whyResultJson,
            String testData,
            SzRecordKey recordKey1,
            SzRecordKey recordKey2,
            Set<SzFlag> flags) {
        JsonObject jsonObject = parseJsonObject(whyResultJson);

        JsonArray whyResults = getJsonArray(jsonObject, "WHY_RESULTS");

        assertNotNull(whyResults,
                "Missing WHY_RESULTS from whyRecords() result JSON: " + testData);

        assertEquals(1, whyResults.size(),
                "The WHY_RESULTS array is not of the expected size: " + testData);

        JsonObject whyResult = getJsonObject(whyResults, 0);

        assertNotNull(whyResult,
                "First WHY_RESULTS element was null: " + testData);

        Long whyId1 = getLong(whyResult, "ENTITY_ID");
        Long whyId2 = getLong(whyResult, "ENTITY_ID_2");
        Set<Long> whyIds = set(whyId1, whyId2);

        assertNotNull(whyId1, "The first entity ID was null: whyResult=[ "
                + whyResult + " ], " + testData);
        assertNotNull(whyId2, "The second entity ID was null: whyResult=[ "
                + whyResult + " ], " + testData);

        long entityId1 = this.loadedRecordMap.get(recordKey1);
        long entityId2 = this.loadedRecordMap.get(recordKey2);

        Set<Long> entityIds = set(entityId1, entityId2);

        assertEquals(entityIds, whyIds,
                "The entity ID's in the why result were not as expected: "
                        + "whyResult=[ " + whyResult + " ], " + testData);

        JsonArray entities = getJsonArray(jsonObject, "ENTITIES");

        assertNotNull(entities,
                "Entity details are missing: " + testData);

        assertEquals(entityIds.size(), entities.size(),
                "Unexpected number of entities in entity details. testData=[ "
                        + testData + " ]");

        JsonArray focusRecords1 = getJsonArray(whyResult, "FOCUS_RECORDS");

        assertNotNull(focusRecords1, "The FOCUS_RECORDS array is missing "
                + "from the why results: whyResult=[ " + whyResult + " ], "
                + testData);

        assertEquals(1, focusRecords1.size(),
                "Size of FOCUS_RECORDS array not as expected: focusRecord1=[ "
                        + focusRecords1 + " ], " + testData);

        JsonObject focusRecord1 = getJsonObject(focusRecords1, 0);
        assertNotNull(focusRecords1, "The first element of FOCUS_RECORDS array "
                + "is missing or null: focusRecord1=[ " + focusRecords1 + " ], "
                + testData);

        String dataSource1 = getString(focusRecord1, "DATA_SOURCE");
        String recordId1 = getString(focusRecord1, "RECORD_ID");

        JsonArray focusRecords2 = getJsonArray(whyResult, "FOCUS_RECORDS_2");

        assertNotNull(focusRecords2, "The FOCUS_RECORDS_2 array is missing "
                + "from the why results: whyResult=[ " + whyResult + " ], "
                + testData);

        assertEquals(1, focusRecords2.size(),
                "Size of FOCUS_RECORDS_2 array not as expected: focusRecords2=[ "
                        + focusRecords2 + " ], " + testData);

        JsonObject focusRecord2 = getJsonObject(focusRecords2, 0);
        assertNotNull(focusRecords2, "The first element of FOCUS_RECORDS_2 "
                + "array is missing or null: focusRecords=[ " + focusRecords2
                + " ], " + testData);

        String dataSource2 = getString(focusRecord2, "DATA_SOURCE");
        String recordId2 = getString(focusRecord2, "RECORD_ID");

        SzRecordKey whyKey1 = SzRecordKey.of(dataSource1, recordId1);
        SzRecordKey whyKey2 = SzRecordKey.of(dataSource2, recordId2);

        Set<SzRecordKey> whyKeys = set(whyKey1, whyKey2);
        Set<SzRecordKey> recordKeys = set(recordKey1, recordKey2);

        assertEquals(recordKeys, whyKeys,
                "Focus records not as expected: focusRecord=[ "
                        + focusRecord1 + " ], " + testData);

        // check that the entities we found are those requested
        Set<Long> detailEntityIds = new HashSet<>();
        for (JsonObject entity : entities.getValuesAs(JsonObject.class)) {
            assertNotNull(entity, "Entity detail was null: "
                    + entities + ", " + testData);

            entity = getJsonObject(entity, "RESOLVED_ENTITY");
            assertNotNull(entity, "Resolved entity in details was null: "
                    + entities + ", " + testData);

            // get the entity ID
            Long id = getLong(entity, "ENTITY_ID");
            assertNotNull(
                    id, "The entity detail was missing or has a null "
                            + "ENTITY_ID: " + entity + ", " + testData);

            // add to the ID set
            detailEntityIds.add(id);
        }

        assertEquals(entityIds, detailEntityIds,
                "Entity detail entity ID's are not as expected: " + testData);
    }

    @ParameterizedTest
    @MethodSource("getWhyRecordsParameters")
    void testWhyRecords(String testDescription,
            SzRecordKey recordKey1,
            SzRecordKey recordKey2,
            Set<SzFlag> flags,
            Class<?> exceptionType) {
        StringBuilder sb = new StringBuilder(
                "description=[ " + testDescription + " ], recordKey1=[ "
                        + recordKey1 + " ], recordKey2=[ " + recordKey2
                        + " ], flags=[ " + SzFlag.toString(flags)
                        + " ], expectedException=[ " + exceptionType + " ]");

        String testData = sb.toString();

        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = engine.whyRecords(
                        recordKey1, recordKey2, flags);

                if (exceptionType != null) {
                    fail("Unexpectedly succeeded whyRecords(): "
                            + testData);
                }

                validateWhyRecords(
                        result, testData, recordKey1, recordKey2, flags);

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
                    fail("Unexpectedly failed whyRecords(): "
                            + testData + ", " + description, e);

                } else if (exceptionType != e.getClass()) {
                    assertInstanceOf(
                            exceptionType, e,
                            "whyRecords() failed with an unexpected exception "
                                    + "type: " + testData + ", " + description);
                }
            }
        });

    }
}
