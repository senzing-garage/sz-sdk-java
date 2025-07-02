package com.senzing.sdk;

import java.io.File;
import java.io.StringReader;
import java.util.Set;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.core.*;
import javax.json.*;

import static com.senzing.sdk.SzFlag.*;
import static com.senzing.util.JsonUtilities.getJsonObject;
import static com.senzing.util.JsonUtilities.getLong;
import static com.senzing.util.JsonUtilities.parseJsonObject;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.CollectionUtilities.*;

/**
 * Provides examples of using {@link SzEngine}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzEngineDemo extends AbstractTest {
    private SzCoreEnvironment env = null;
    private long addedEntityId = 0L;

    private static final String EMPLOYEES_DATA_SOURCE   = "EMPLOYEES";
    
    private static final String PASSENGERS_DATA_SOURCE  = "PASSENGERS";
    
    private static final String VIPS_DATA_SOURCE        = "VIPS";

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

        this.env = SzCoreEnvironment.newBuilder()
                                      .instanceName(instanceName)
                                      .settings(settings)
                                      .verboseLogging(false)
                                      .build();

        try {
            SzEngine engine = env.getEngine();

            // get the loaded records and entity ID's
            for (SzRecordKey key : GRAPH_RECORD_KEYS) {
                String responseJson = engine.getEntity(key, null);

                // parse the JSON 
                JsonObject  jsonObj     = parseJsonObject(responseJson);
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

        } catch (SzException e) {
            throw new RuntimeException(e);
        }
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

    protected SzEnvironment getEnvironment() {
        return this.env;
    }

    protected static void logError(String message, Exception e) 
        throws Exception
    {
        System.err.println();
        System.err.println("**********************************");
        System.err.println("FAILURE: " + message);
        e.printStackTrace(System.err);
        System.err.println();
        throw e;
    }

    /**
     * Dummy logging function
     * @param message The message to log.
     */
    protected static void log(String message) {
        if (message == null) throw new NullPointerException();
    }

    @Test
    public void getEngineDemo() {
        try {
            // @start region="getEngine"
            // How to obtain an SzEngine instance
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                SzEngine engine = env.getEngine();  // @highlight   

                if (engine == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get SzEngine.", e); // @highlight type="italic"
            }
            // @end region="getEngine"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void primeEngineDemo() {
        try {
            // @start region="primeEngine"
            // How to prime the SzEngine to expedite subsequent operations
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // prime the engine
                engine.primeEngine(); // @highlight

                // use the primed engine to perform additional tasks
                if ("" + engine == "") { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to prime engine.", e); // @highlight type="italic"
            }
            // @end region="primeEngine"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getStatsDemo() {
        try {
            // @start region="getStats"
            // How to get engine stats after loading records
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // load some records to prime the stats
                if ("" + engine == "") { throw new Exception(); } // @replace regex="if.*" replacement="..."

                // get the stats
                String statsJson = engine.getStats(); // @highlight

                // do something with the stats
                log(statsJson); // @highlight type="italic" regex="log.*"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to load records with stats.", e); // @highlight type="italic"
            }
            // @end region="getStats"

        } catch (Exception e) {
            fail(e);
        }
    }


    @Test
    @Order(20)
    public void addRecordDemo() {
        try {
            // @start region="addRecord"
            // How to load a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get a record definition (varies by application)
                String recordDefinition = // @highlight substring="recordDefinition"
                        // @highlight type="italic" region="recordDefinition"
                        """
                        {
                            "DATA_SOURCE": "TEST",
                            "RECORD_ID": "ABC123",
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="recordDefinition"
                
                // add the record to the repository
                // @highlight region="addRecordCall"
                String infoJson = engine.addRecord(
                    SzRecordKey.of("TEST", "ABC123"),
                    recordDefinition,
                    SZ_WITH_INFO_FLAGS);
                // @end region="addRecordCall"

                // do something with the "info JSON" (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(infoJson)).readObject(); // @highlight regex="infoJson"
                if (jsonObject.containsKey("AFFECTED_ENTITIES")) { // @highlight regex=".AFFECTED_ENTITIES."
                    JsonArray affectedArr = jsonObject.getJsonArray("AFFECTED_ENTITIES"); // @highlight regex=".AFFECTED_ENTITIES."
                    for (JsonObject affected : affectedArr.getValuesAs(JsonObject.class)) {
                        long affectedId = affected.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        this.addedEntityId = affectedId; // @replace regex="this.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to add record.", e); // @highlight type="italic"
            }
            // @end region="addRecord"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @Order(25)
    public void addRecordDefaultDemo() {
        try {
            // @start region="addRecordDefault"
            // How to load a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get a record definition (varies by application)
                String recordDefinition = // @highlight substring="recordDefinition"
                        // @highlight type="italic" region="recordDefinition"
                        """
                        {
                            "DATA_SOURCE": "TEST",
                            "RECORD_ID": "ABC123",
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="recordDefinition"
                
                // add the record to the repository
                // @highlight region="addRecordCall"
                engine.addRecord(
                    SzRecordKey.of("TEST", "ABC123"),
                    recordDefinition);
                // @end region="addRecordCall"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to add record.", e); // @highlight type="italic"
            }
            // @end region="addRecordDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getRecordPreviewDemo() {
        try {
            // @start region="getRecordPreview"
            // How to pre-process a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get a record definition (varies by application)
                String recordDefinition =  // @highlight substring="recordDefinition"
                        // @highlight type="italic" region="recordDefinition"
                        """
                        {
                            "DATA_SOURCE": "TEST",
                            "RECORD_ID": "DEF456",
                            "NAME_FULL": "John Doe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "johndoe@nowhere.com"
                        }
                        """;
                        // @end region="recordDefinition"
                
                // preprocess the record
                // @highlight region="preprocessCall"
                String responseJson = engine.getRecordPreview(
                    recordDefinition, SZ_RECORD_PREVIEW_DEFAULT_FLAGS);
                // @end region="preprocessCall"
                
                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject();  // @highlight regex="responseJson"
                
                if (jsonObject.containsKey("FEATURES")) { // @highlight regex=".FEATURES."
                    JsonObject featuresObj = jsonObject.getJsonObject("FEATURES"); // @highlight regex=".FEATURES."
                    featuresObj.forEach((featureName, jsonValue) -> {
                        if (featureName == "" + jsonValue) { throw new RuntimeException(); } // @replace regex="if.*" replacement="..."
                    });
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get preview record.", e); // @highlight type="italic"
            }
            // @end region="getRecordPreview"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getRecordPreviewDefaultDemo() {
        try {
            // @start region="getRecordPreviewDefault"
            // How to pre-process a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get a record definition (varies by application)
                String recordDefinition =  // @highlight substring="recordDefinition"
                        // @highlight type="italic" region="recordDefinition"
                        """
                        {
                            "DATA_SOURCE": "TEST",
                            "RECORD_ID": "DEF456",
                            "NAME_FULL": "John Doe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "johndoe@nowhere.com"
                        }
                        """;
                        // @end region="recordDefinition"
                
                // preprocess the record
                // @highlight region="preprocessCall"
                String responseJson = engine.getRecordPreview(recordDefinition);
                // @end region="preprocessCall"
                
                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject();  // @highlight regex="responseJson"
                
                if (jsonObject.containsKey("FEATURES")) { // @highlight regex=".FEATURES."
                    JsonObject featuresObj = jsonObject.getJsonObject("FEATURES"); // @highlight regex=".FEATURES."
                    featuresObj.forEach((featureName, jsonValue) -> {
                        if (featureName == "" + jsonValue) { throw new RuntimeException(); } // @replace regex="if.*" replacement="..."
                    });
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to preprocess record.", e); // @highlight type="italic"
            }
            // @end region="getRecordPreviewDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @Order(10)
    public void deleteRecordDemo() {
        try {
            // @start region="deleteRecord"
            // How to delete a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // delete the record from the repository
                // @highlight region="deleteRecordCall"
                String infoJson = engine.deleteRecord(
                    SzRecordKey.of("TEST", "ABC123"),
                    SZ_WITH_INFO_FLAGS);
                // @end region="deleteRecordCall"

                // do something with the "info JSON" (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(infoJson)).readObject(); // @highlight regex="infoJson"
                if (jsonObject.containsKey("AFFECTED_ENTITIES")) { // @highlight regex=".AFFECTED_ENTITIES."
                    JsonArray affectedArr = jsonObject.getJsonArray("AFFECTED_ENTITIES"); // @highlight regex=".AFFECTED_ENTITIES."
                    for (JsonObject affected : affectedArr.getValuesAs(JsonObject.class)) {
                        long affectedId = affected.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        if (affectedId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to delete record.", e); // @highlight type="italic"
            }
            // @end region="deleteRecord"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @Order(15)
    public void deleteRecordDefaultDemo() {
        try {
            // @start region="deleteRecordDefault"
            // How to delete a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // delete the record from the repository
                // @highlight region="deleteRecordCall"
                engine.deleteRecord(
                    SzRecordKey.of("TEST", "ABC123"));
                // @end region="deleteRecordCall"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to delete record.", e); // @highlight type="italic"
            }
            // @end region="deleteRecordDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void reevaluateRecordDemo() {
        try {
            // @start region="reevaluateRecord"
            // How to reevaluate a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // reevaluate a record in the repository
                // @highlight region="reevaluateRecordCall"
                String infoJson = engine.reevaluateRecord(
                    SzRecordKey.of("TEST", "ABC123"),
                    SZ_WITH_INFO_FLAGS);
                // @end region="reevaluateRecordCall"

                // do something with the "info JSON" (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(infoJson)).readObject(); // @highlight regex="infoJson"
                if (jsonObject.containsKey("AFFECTED_ENTITIES")) { // @highlight regex=".AFFECTED_ENTITIES."
                    JsonArray affectedArr = jsonObject.getJsonArray("AFFECTED_ENTITIES"); // @highlight regex=".AFFECTED_ENTITIES."
                    for (JsonObject affected : affectedArr.getValuesAs(JsonObject.class)) {
                        long affectedId = affected.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        if (affectedId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate record.", e); // @highlight type="italic"
            }
            // @end region="reevaluateRecord"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void reevaluateRecordDefaultDemo() {
        try {
            // @start region="reevaluateRecordDefault"
            // How to reevaluate a record
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // reevaluate a record in the repository
                // @highlight region="reevaluateRecordCall"
                engine.reevaluateRecord(
                    SzRecordKey.of("TEST", "ABC123"));
                // @end region="reevaluateRecordCall"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate record.", e); // @highlight type="italic"
            }
            // @end region="reevaluateRecordDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Dummy function to return an entity ID.
     * @return An arbitrary entity ID.
     */
    private long getEntityId() {
        return this.addedEntityId;
    }

    @Test
    public void reevaluateEntityDemo() {
        try {
            // @start region="reevaluateEntity"
            // How to reevaluate an entity
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the ID of an entity to reevaluate (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // reevaluate an entity in the repository
                // @highlight region="reevaluateEntityCall"
                String infoJson = engine.reevaluateEntity(entityId, SZ_WITH_INFO_FLAGS);
                // @end region="reevaluateEntityCall"

                // do something with the "info JSON" (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(infoJson)).readObject(); // @highlight regex="infoJson"
                if (jsonObject.containsKey("AFFECTED_ENTITIES")) { // @highlight regex=".AFFECTED_ENTITIES."
                    JsonArray affectedArr = jsonObject.getJsonArray("AFFECTED_ENTITIES"); // @highlight regex=".AFFECTED_ENTITIES."
                    for (JsonObject affected : affectedArr.getValuesAs(JsonObject.class)) {
                        long affectedId = affected.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        if (affectedId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate entity.", e); // @highlight type="italic"
            }
            // @end region="reevaluateEntity"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void reevaluateEntityDefaultDemo() {
        try {
            // @start region="reevaluateEntityDefault"
            // How to reevaluate an entity
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the ID of an entity to reevaluate (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // reevaluate an entity in the repository
                // @highlight region="reevaluateEntityCall"
                engine.reevaluateEntity(entityId);
                // @end region="reevaluateEntityCall"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate entity.", e); // @highlight type="italic"
            }
            // @end region="reevaluateEntityDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void searchByAttributesDemo() {
        try {
            // @start region="searchByAttributes"
            // How to search for entities matching criteria
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // search for matching entities in the repository
                // @highlight region="searchByAttributesCall"
                String responseJson = engine.searchByAttributes(
                    searchAttributes,
                    SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS);
                // @end region="searchByAttributesCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                if (jsonObject.containsKey("RESOLVED_ENTITIES")) { // @highlight regex=".RESOLVED_ENTITIES."
                    JsonArray resultsArr = jsonObject.getJsonArray("RESOLVED_ENTITIES"); // @highlight regex=".RESOLVED_ENTITIES."
                    for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                        // get the match info for the result
                        JsonObject matchInfo = result.getJsonObject("MATCH_INFO"); // @highlight regex=".MATCH_INFO."

                        if (matchInfo == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

                        // get the entity for the result
                        JsonObject  entityInfo  = result.getJsonObject("ENTITY"); // @highlight regex=".ENTITY."
                        JsonObject  entity      = entityInfo.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                        long        entityId    = entity.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        if (entityId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to search for entities.", e); // @highlight type="italic"
            }
            // @end region="searchByAttributes"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void searchByAttributesDefaultDemo() {
        try {
            // @start region="searchByAttributesDefault"
            // How to search for entities matching criteria
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // search for matching entities in the repository
                // @highlight region="searchByAttributesCall"
                String responseJson = engine.searchByAttributes(searchAttributes);
                // @end region="searchByAttributesCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                if (jsonObject.containsKey("RESOLVED_ENTITIES")) { // @highlight regex=".RESOLVED_ENTITIES."
                    JsonArray resultsArr = jsonObject.getJsonArray("RESOLVED_ENTITIES"); // @highlight regex=".RESOLVED_ENTITIES."
                    for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                        // get the match info for the result
                        JsonObject matchInfo = result.getJsonObject("MATCH_INFO"); // @highlight regex=".MATCH_INFO."

                        if (matchInfo == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

                        // get the entity for the result
                        JsonObject  entityInfo  = result.getJsonObject("ENTITY"); // @highlight regex=".ENTITY."
                        JsonObject  entity      = entityInfo.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                        long        entityId    = entity.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        if (entityId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to search for entities.", e); // @highlight type="italic"
            }
            // @end region="searchByAttributesDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Function to return a dummy search profile.
     * @return Returns the <code>"SEARCH"</code> profile.
     */
    private String getSearchProfile() {
        return "SEARCH";
    }

    @Test
    public void searchByAttributesWithProfileDemo() {
        try {
            // @start region="searchByAttributesWithProfile"
            // How to search for entities matching criteria using a search profile
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // get a search profile (varies by application)
                String searchProfile = getSearchProfile(); // @highlight type="italic" substring="getSearchProfile()" @highlight substring="searchProfile"

                // search for matching entities in the repository
                // @highlight region="searchByAttributesCall"
                String responseJson = engine.searchByAttributes(
                    searchAttributes,
                    searchProfile,
                    SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS);
                // @end region="searchByAttributesCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                if (jsonObject.containsKey("RESOLVED_ENTITIES")) { // @highlight regex=".RESOLVED_ENTITIES."
                    JsonArray resultsArr = jsonObject.getJsonArray("RESOLVED_ENTITIES"); // @highlight regex=".RESOLVED_ENTITIES."
                    for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                        // get the match info for the result
                        JsonObject matchInfo = result.getJsonObject("MATCH_INFO"); // @highlight regex=".MATCH_INFO."

                        if (matchInfo == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

                        // get the entity for the result
                        JsonObject  entityInfo  = result.getJsonObject("ENTITY"); // @highlight regex=".ENTITY."
                        JsonObject  entity      = entityInfo.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                        long        entityId    = entity.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        if (entityId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to search for entities.", e); // @highlight type="italic"
            }
            // @end region="searchByAttributesWithProfile"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void searchByAttributesWithProfileDefaultDemo() {
        try {
            // @start region="searchByAttributesWithProfileDefault"
            // How to search for entities matching criteria using a search profile
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // get a search profile (varies by application)
                String searchProfile = getSearchProfile(); // @highlight type="italic" substring="getSearchProfile()" @highlight substring="searchProfile"

                // search for matching entities in the repository
                // @highlight region="searchByAttributesCall"
                String responseJson = engine.searchByAttributes(searchAttributes,
                                                                searchProfile);
                // @end region="searchByAttributesCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                if (jsonObject.containsKey("RESOLVED_ENTITIES")) { // @highlight regex=".RESOLVED_ENTITIES."
                    JsonArray resultsArr = jsonObject.getJsonArray("RESOLVED_ENTITIES"); // @highlight regex=".RESOLVED_ENTITIES."
                    for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                        // get the match info for the result
                        JsonObject matchInfo = result.getJsonObject("MATCH_INFO"); // @highlight regex=".MATCH_INFO."

                        if (matchInfo == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

                        // get the entity for the result
                        JsonObject  entityInfo  = result.getJsonObject("ENTITY"); // @highlight regex=".ENTITY."
                        JsonObject  entity      = entityInfo.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                        long        entityId    = entity.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    
                        if (entityId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to search for entities.", e); // @highlight type="italic"
            }
            // @end region="searchByAttributesWithProfileDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getEntityByEntityIdDemo() {
        try {
            // @start region="getEntityByEntityId"
            // How to retrieve an entity via its entity ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the ID of an entity to retrieve (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // retrieve the entity by entity ID
                // @highlight region="getEntityCall"
                String responseJson = engine.getEntity(entityId, SZ_ENTITY_DEFAULT_FLAGS);
                // @end region="getEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  entity      = jsonObject.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                String      entityName  = entity.getString("ENTITY_NAME"); // @highlight regex=".ENTITY_NAME."
            
                if (entityName == "FOO") { throw new Exception(); } // @replace regex="if.*" replacement="..."

                if (jsonObject.containsKey("RECORDS")) { // @highlight regex=".RECORDS."
                    JsonArray recordArr = jsonObject.getJsonArray("RECORDS"); // @highlight regex=".RECORDS."
                    for (JsonObject record : recordArr.getValuesAs(JsonObject.class)) {
                        String dataSource = record.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                        String recordId   = record.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                    
                        if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity by entity ID.", e); // @highlight type="italic"
            }
            // @end region="getEntityByEntityId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getEntityByEntityIdDefaultDemo() {
        try {
            // @start region="getEntityByEntityIdDefault"
            // How to retrieve an entity via its entity ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the ID of an entity to retrieve (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // retrieve the entity by entity ID
                // @highlight region="getEntityCall"
                String responseJson = engine.getEntity(entityId);
                // @end region="getEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  entity      = jsonObject.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                String      entityName  = entity.getString("ENTITY_NAME"); // @highlight regex=".ENTITY_NAME."
            
                if (entityName == "FOO") { throw new Exception(); } // @replace regex="if.*" replacement="..."

                if (jsonObject.containsKey("RECORDS")) { // @highlight regex=".RECORDS."
                    JsonArray recordArr = jsonObject.getJsonArray("RECORDS"); // @highlight regex=".RECORDS."
                    for (JsonObject record : recordArr.getValuesAs(JsonObject.class)) {
                        String dataSource = record.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                        String recordId   = record.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                    
                        if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity by entity ID.", e); // @highlight type="italic"
            }
            // @end region="getEntityByEntityIdDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getEntityByRecordKeyDemo() {
        try {
            // @start region="getEntityByRecordKey"
            // How to retrieve an entity via its record key
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // retrieve the entity by record key
                // @highlight region="getEntityCall"
                String responseJson = engine.getEntity(
                    SzRecordKey.of("TEST", "ABC123"),
                    SZ_ENTITY_DEFAULT_FLAGS);
                // @end region="getEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  entity      = jsonObject.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                String      entityName  = entity.getString("ENTITY_NAME"); // @highlight regex=".ENTITY_NAME."
            
                if (entityName == "FOO") { throw new Exception(); } // @replace regex="if.*" replacement="..."

                if (jsonObject.containsKey("RECORDS")) { // @highlight regex=".RECORDS."
                    JsonArray recordArr = jsonObject.getJsonArray("RECORDS"); // @highlight regex=".RECORDS."
                    for (JsonObject record : recordArr.getValuesAs(JsonObject.class)) {
                        String dataSource = record.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                        String recordId   = record.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                    
                        if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity by record key.", e); // @highlight type="italic"
            }
            // @end region="getEntityByRecordKey"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getEntityByRecordKeyDefaultDemo() {
        try {
            // @start region="getEntityByRecordKeyDefault"
            // How to retrieve an entity via its record key
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // retrieve the entity by record key
                // @highlight region="getEntityCall"
                String responseJson = engine.getEntity(
                    SzRecordKey.of("TEST", "ABC123"));
                // @end region="getEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  entity      = jsonObject.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                String      entityName  = entity.getString("ENTITY_NAME"); // @highlight regex=".ENTITY_NAME."
            
                if (entityName == "FOO") { throw new Exception(); } // @replace regex="if.*" replacement="..."

                if (jsonObject.containsKey("RECORDS")) { // @highlight regex=".RECORDS."
                    JsonArray recordArr = jsonObject.getJsonArray("RECORDS"); // @highlight regex=".RECORDS."
                    for (JsonObject record : recordArr.getValuesAs(JsonObject.class)) {
                        String dataSource = record.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                        String recordId   = record.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                    
                        if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity by record key.", e); // @highlight type="italic"
            }
            // @end region="getEntityByRecordKeyDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findInterestingByEntityIdDemo() {
        try {
            // @start region="findInterestingByEntityId"
            // How to find interesting entities related to an entity via entity ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the ID of an entity to retrieve (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // find the interesting entities by entity ID
                // @highlight region="findInterestingCall"
                String responseJson = engine.findInterestingEntities(
                    entityId, SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS);
                // @end region="findInterestingCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"

                if (jsonObject == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to find interesting entities by entity ID.", e); // @highlight type="italic"
            }
            // @end region="findInterestingByEntityId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findInterestingByEntityIdDefaultDemo() {
        try {
            // @start region="findInterestingByEntityIdDefault"
            // How to find interesting entities related to an entity via entity ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the ID of an entity to retrieve (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // find the interesting entities by entity ID
                // @highlight region="findInterestingCall"
                String responseJson = engine.findInterestingEntities(entityId);
                // @end region="findInterestingCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"

                if (jsonObject == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to find interesting entities by entity ID.", e); // @highlight type="italic"
            }
            // @end region="findInterestingByEntityIdDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findInterestingByRecordKeyDemo() {
        try {
            // @start region="findInterestingByRecordKey"
            // How to find interesting entities related to an entity via record key
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // retrieve the entity by record key
                // @highlight region="findInterestingCall"
                String responseJson = engine.findInterestingEntities(
                    SzRecordKey.of("TEST", "ABC123"),
                    SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS);
                // @end region="findInterestingCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"

                if (jsonObject == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to find interesting entities by record key.", e); // @highlight type="italic"
            }
            // @end region="findInterestingByRecordKey"

        } catch (Exception e) {
            fail(e);
        }
    }


    @Test
    public void findInterestingByRecordKeyDefaultDemo() {
        try {
            // @start region="findInterestingByRecordKeyDefault"
            // How to find interesting entities related to an entity via record key
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // retrieve the entity by record key
                // @highlight region="findInterestingCall"
                String responseJson = engine.findInterestingEntities(
                    SzRecordKey.of("TEST", "ABC123"));
                // @end region="findInterestingCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"

                if (jsonObject == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to find interesting entities by record key.", e); // @highlight type="italic"
            }
            // @end region="findInterestingByRecordKeyDefault"

        } catch (Exception e) {
            fail(e);
        }
    }


    private SzRecordKey getPathStartRecordKey() {
        return PASSENGER_ABC123;
    }

    private SzRecordKey getPathEndRecordKey() {
        return VIP_JKL456;
    }

    private long getPathStartEntityId() {
        return this.loadedRecordMap.get(this.getPathStartRecordKey());
    }

    private long getPathEndEntityId() {
        return this.loadedRecordMap.get(this.getPathEndRecordKey());
    }

    private Set<SzRecordKey> getPathAvoidRecordKeys() {
        return set(EMPLOYEE_DEF890);
    }

    private Set<Long> getPathAvoidEntityIds() {
        Set<Long> set = new LinkedHashSet<>();
        for (SzRecordKey key : this.getPathAvoidRecordKeys()) {
            set.add(this.loadedRecordMap.get(key));
        }
        return set;
    }

    @Test
    public void findPathByEntityIdDemo() {
        try {
            // @start region="findPathByEntityId"
            // How to find an entity path using entity ID's
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                long startEntityId = getPathStartEntityId(); // @highlight type="italic" substring="getPathStartEntityId()" @highlight substring="startEntityId"
                long endEntityId   = getPathEndEntityId(); // @highlight type="italic" substring="getPathEndEntityId()" @highlight substring="endEntityId"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // determine any entities to be avoided (varies by application)
                Set<Long> avoidEntities = getPathAvoidEntityIds(); // @highlight type="italic" substring="getPathAvoidEntityIds()" @highlight substring="avoidEntities"

                // determine any data sources to require in the path (varies by application)
                Set<String> requiredSources = null; // @highlight type="italic" substring="null" @highlight substring="requiredSources"

                // retrieve the entity path using the entity ID's
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startEntityId,
                                                      endEntityId,
                                                      maxDegrees,
                                                      SzEntityIds.of(avoidEntities),
                                                      requiredSources,
                                                      SZ_FIND_PATH_DEFAULT_FLAGS);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by entity ID.", e); // @highlight type="italic"
            }
            // @end region="findPathByEntityId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findPathByEntityIdDefaultDemo() {
        try {
            // @start region="findPathByEntityIdDefault"
            // How to find an entity path using entity ID's
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                long startEntityId = getPathStartEntityId(); // @highlight type="italic" substring="getPathStartEntityId()" @highlight substring="startEntityId"
                long endEntityId   = getPathEndEntityId(); // @highlight type="italic" substring="getPathEndEntityId()" @highlight substring="endEntityId"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // determine any entities to be avoided (varies by application)
                Set<Long> avoidEntities = getPathAvoidEntityIds(); // @highlight type="italic" substring="getPathAvoidEntityIds()" @highlight substring="avoidEntities"

                // determine any data sources to require in the path (varies by application)
                Set<String> requiredSources = null; // @highlight type="italic" substring="null" @highlight substring="requiredSources"

                // retrieve the entity path using the entity ID's
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startEntityId,
                                                      endEntityId,
                                                      maxDegrees,
                                                      SzEntityIds.of(avoidEntities),
                                                      requiredSources);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by entity ID.", e); // @highlight type="italic"
            }
            // @end region="findPathByEntityIdDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findPathByEntityIdSimpleDemo() {
        try {
            // @start region="findPathByEntityIdSimple"
            // How to find an entity path using entity ID's
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                long startEntityId = getPathStartEntityId(); // @highlight type="italic" substring="getPathStartEntityId()" @highlight substring="startEntityId"
                long endEntityId   = getPathEndEntityId(); // @highlight type="italic" substring="getPathEndEntityId()" @highlight substring="endEntityId"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // retrieve the entity path using the entity ID's
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startEntityId,
                                                      endEntityId,
                                                      maxDegrees,
                                                      SZ_FIND_PATH_DEFAULT_FLAGS);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by entity ID.", e); // @highlight type="italic"
            }
            // @end region="findPathByEntityIdSimple"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findPathByEntityIdSimpleDefaultDemo() {
        try {
            // @start region="findPathByEntityIdSimpleDefault"
            // How to find an entity path using entity ID's
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                long startEntityId = getPathStartEntityId(); // @highlight type="italic" substring="getPathStartEntityId()" @highlight substring="startEntityId"
                long endEntityId   = getPathEndEntityId(); // @highlight type="italic" substring="getPathEndEntityId()" @highlight substring="endEntityId"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // retrieve the entity path using the entity ID's
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startEntityId,
                                                      endEntityId,
                                                      maxDegrees);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by entity ID.", e); // @highlight type="italic"
            }
            // @end region="findPathByEntityIdSimpleDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findPathByRecordKeyDemo() {
        try {
            // @start region="findPathByRecordKey"
            // How to find an entity path using record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                SzRecordKey startRecordKey = getPathStartRecordKey(); // @highlight type="italic" substring="getPathStartRecordKey()" @highlight substring="startRecordKey"
                SzRecordKey endRecordKey   = getPathEndRecordKey(); // @highlight type="italic" substring="getPathEndRecordKey()" @highlight substring="endRecordKey"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // determine any records to be avoided (varies by application)
                Set<SzRecordKey> avoidRecords = getPathAvoidRecordKeys(); // @highlight type="italic" substring="getPathAvoidRecordKeys()" @highlight substring="avoidRecords"

                // determine any data sources to require in the path (varies by application)
                Set<String> requiredSources = null; // @highlight type="italic" substring="null" @highlight substring="requiredSources"

                // retrieve the entity path using the record keys
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startRecordKey,
                                                      endRecordKey,
                                                      maxDegrees,
                                                      SzRecordKeys.of(avoidRecords),
                                                      requiredSources,
                                                      SZ_FIND_PATH_DEFAULT_FLAGS);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by record key.", e); // @highlight type="italic"
            }
            // @end region="findPathByRecordKey"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findPathByRecordKeyDefaultDemo() {
        try {
            // @start region="findPathByRecordKeyDefault"
            // How to find an entity path using record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                SzRecordKey startRecordKey = getPathStartRecordKey(); // @highlight type="italic" substring="getPathStartRecordKey()" @highlight substring="startRecordKey"
                SzRecordKey endRecordKey   = getPathEndRecordKey(); // @highlight type="italic" substring="getPathEndRecordKey()" @highlight substring="endRecordKey"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // determine any records to be avoided (varies by application)
                Set<SzRecordKey> avoidRecords = getPathAvoidRecordKeys(); // @highlight type="italic" substring="getPathAvoidRecordKeys()" @highlight substring="avoidRecords"

                // determine any data sources to require in the path (varies by application)
                Set<String> requiredSources = null; // @highlight type="italic" substring="null" @highlight substring="requiredSources"

                // retrieve the entity path using the record keys
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startRecordKey,
                                                      endRecordKey,
                                                      maxDegrees,
                                                      SzRecordKeys.of(avoidRecords),
                                                      requiredSources);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by record key.", e); // @highlight type="italic"
            }
            // @end region="findPathByRecordKeyDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findPathByRecordKeySimpleDemo() {
        try {
            // @start region="findPathByRecordKeySimple"
            // How to find an entity path using record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                SzRecordKey startRecordKey = getPathStartRecordKey(); // @highlight type="italic" substring="getPathStartRecordKey()" @highlight substring="startRecordKey"
                SzRecordKey endRecordKey   = getPathEndRecordKey(); // @highlight type="italic" substring="getPathEndRecordKey()" @highlight substring="endRecordKey"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // retrieve the entity path using the record keys
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startRecordKey,
                                                      endRecordKey,
                                                      maxDegrees,
                                                      SZ_FIND_PATH_DEFAULT_FLAGS);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by record key.", e); // @highlight type="italic"
            }
            // @end region="findPathByRecordKeySimple"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findPathByRecordKeySimpleDefaultDemo() {
        try {
            // @start region="findPathByRecordKeySimpleDefault"
            // How to find an entity path using record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                SzRecordKey startRecordKey = getPathStartRecordKey(); // @highlight type="italic" substring="getPathStartRecordKey()" @highlight substring="startRecordKey"
                SzRecordKey endRecordKey   = getPathEndRecordKey(); // @highlight type="italic" substring="getPathEndRecordKey()" @highlight substring="endRecordKey"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 4; // @highlight type="italic" substring="4" @highlight substring="maxDegrees"

                // retrieve the entity path using the record keys
                // @highlight region="findPathCall"
                String responseJson = engine.findPath(startRecordKey,
                                                      endRecordKey,
                                                      maxDegrees);
                // @end region="findPathCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    JsonArray entityIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity path by record key.", e); // @highlight type="italic"
            }
            // @end region="findPathByRecordKeySimpleDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    private Set<SzRecordKey> getNetworkRecordKeys() {
        return set(PASSENGER_ABC123, EMPLOYEE_ABC567, VIP_JKL456);
    }

    private Set<Long> getNetworkEntityIds() {
        Set<Long> set = new LinkedHashSet<>();
        for (SzRecordKey key : this.getNetworkRecordKeys()) {
            set.add(this.loadedRecordMap.get(key));
        }
        return set;
    }

    @Test
    public void findNetworkByEntityIdDemo() {
        try {
            // @start region="findNetworkByEntityId"
            // How to find an entity network using entity ID's
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                Set<Long> entityIds = getNetworkEntityIds(); // @highlight type="italic" substring="getNetworkEntityIds()" @highlight substring="entityIds"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 3; // @highlight type="italic" substring="3" @highlight substring="maxDegrees"

                // determine the degrees to build-out the network (varies by application)
                int buildOutDegrees = 0; // @highlight type="italic" substring="0" @highlight substring="buildOutDegrees"

                // determine the max entities to build-out (varies by application)
                int buildOutMaxEntities = 10; // @highlight type="italic" substring="10" @highlight substring="buildOutMaxEntities"

                // retrieve the entity network using the entity ID's
                // @highlight region="findNetworkCall"
                String responseJson = engine.findNetwork(SzEntityIds.of(entityIds),
                                                         maxDegrees,
                                                         buildOutDegrees,
                                                         buildOutMaxEntities,
                                                         SZ_FIND_NETWORK_DEFAULT_FLAGS);
                // @end region="findNetworkCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    long      startEntityId = path.getJsonNumber("START_ENTITY_ID").longValue(); // @highlight regex=".START_ENTITY_ID."
                    long      endEntityId   = path.getJsonNumber("END_ENTITY_ID").longValue(); // @highlight regex=".END_ENTITY_ID."
                    JsonArray entityPathIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityPathIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L || startEntityId < 0L || endEntityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity network.", e); // @highlight type="italic"
            }
            // @end region="findNetworkByEntityId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findNetworkByEntityIdDefaultDemo() {
        try {
            // @start region="findNetworkByEntityIdDefault"
            // How to find an entity network using entity ID's
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                Set<Long> entityIds = getNetworkEntityIds(); // @highlight type="italic" substring="getNetworkEntityIds()" @highlight substring="entityIds"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 3; // @highlight type="italic" substring="3" @highlight substring="maxDegrees"

                // determine the degrees to build-out the network (varies by application)
                int buildOutDegrees = 0; // @highlight type="italic" substring="0" @highlight substring="buildOutDegrees"

                // determine the max entities to build-out (varies by application)
                int buildOutMaxEntities = 10; // @highlight type="italic" substring="10" @highlight substring="buildOutMaxEntities"

                // retrieve the entity network using the entity ID's
                // @highlight region="findNetworkCall"
                String responseJson = engine.findNetwork(SzEntityIds.of(entityIds),
                                                         maxDegrees,
                                                         buildOutDegrees,
                                                         buildOutMaxEntities);
                // @end region="findNetworkCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    long      startEntityId = path.getJsonNumber("START_ENTITY_ID").longValue(); // @highlight regex=".START_ENTITY_ID."
                    long      endEntityId   = path.getJsonNumber("END_ENTITY_ID").longValue(); // @highlight regex=".END_ENTITY_ID."
                    JsonArray entityPathIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityPathIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L || startEntityId < 0L || endEntityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity network.", e); // @highlight type="italic"
            }
            // @end region="findNetworkByEntityIdDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findNetworkByRecordKeyDemo() {
        try {
            // @start region="findNetworkByRecordKey"
            // How to find an entity network using record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                Set<SzRecordKey> recordKeys = getNetworkRecordKeys(); // @highlight type="italic" substring="getNetworkEntityIds()" @highlight substring="entityIds"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 3; // @highlight type="italic" substring="3" @highlight substring="maxDegrees"

                // determine the degrees to build-out the network (varies by application)
                int buildOutDegrees = 0; // @highlight type="italic" substring="0" @highlight substring="buildOutDegrees"

                // determine the max entities to build-out (varies by application)
                int buildOutMaxEntities = 10; // @highlight type="italic" substring="10" @highlight substring="buildOutMaxEntities"

                // retrieve the entity network using the record keys
                // @highlight region="findNetworkCall"
                String responseJson = engine.findNetwork(SzRecordKeys.of(recordKeys),
                                                         maxDegrees,
                                                         buildOutDegrees,
                                                         buildOutMaxEntities,
                                                         SZ_FIND_NETWORK_DEFAULT_FLAGS);
                // @end region="findNetworkCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    long      startEntityId = path.getJsonNumber("START_ENTITY_ID").longValue(); // @highlight regex=".START_ENTITY_ID."
                    long      endEntityId   = path.getJsonNumber("END_ENTITY_ID").longValue(); // @highlight regex=".END_ENTITY_ID."
                    JsonArray entityPathIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityPathIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L || startEntityId < 0L || endEntityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity network.", e); // @highlight type="italic"
            }
            // @end region="findNetworkByRecordKey"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findNetworkByRecordKeyDefaultDemo() {
        try {
            // @start region="findNetworkByRecordKeyDefault"
            // How to find an entity network using record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get entity ID's for the path endpoints (varies by application)
                Set<SzRecordKey> recordKeys = getNetworkRecordKeys(); // @highlight type="italic" substring="getNetworkEntityIds()" @highlight substring="entityIds"

                // determine the maximum path degrees (varies by application)
                int maxDegrees = 3; // @highlight type="italic" substring="3" @highlight substring="maxDegrees"

                // determine the degrees to build-out the network (varies by application)
                int buildOutDegrees = 0; // @highlight type="italic" substring="0" @highlight substring="buildOutDegrees"

                // determine the max entities to build-out (varies by application)
                int buildOutMaxEntities = 10; // @highlight type="italic" substring="10" @highlight substring="buildOutMaxEntities"

                // retrieve the entity network using the record keys
                // @highlight region="findNetworkCall"
                String responseJson = engine.findNetwork(SzRecordKeys.of(recordKeys),
                                                         maxDegrees,
                                                         buildOutDegrees,
                                                         buildOutMaxEntities);
                // @end region="findNetworkCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray   pathArr     = jsonObject.getJsonArray("ENTITY_PATHS"); // @highlight regex=".ENTITY_PATHS."
                for (JsonObject path : pathArr.getValuesAs(JsonObject.class)) {
                    long      startEntityId = path.getJsonNumber("START_ENTITY_ID").longValue(); // @highlight regex=".START_ENTITY_ID."
                    long      endEntityId   = path.getJsonNumber("END_ENTITY_ID").longValue(); // @highlight regex=".END_ENTITY_ID."
                    JsonArray entityPathIds = path.getJsonArray("ENTITIES"); // @highlight regex=".ENTITIES."
                
                    for (JsonNumber number : entityPathIds.getValuesAs(JsonNumber.class)) {
                        long entityId = number.longValue();

                        if (entityId < 0L || startEntityId < 0L || endEntityId < 0L) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve entity network.", e); // @highlight type="italic"
            }
            // @end region="findNetworkByRecordKeyDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whyRecordInEntityDemo() {
        try {
            // @start region="whyRecordInEntity"
            // How to determine why a record is a member of its respective entity
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // determine why the record is part of its entity
                // @highlight region="whyRecordInEntityCall"
                String responseJson = engine.whyRecordInEntity(
                    SzRecordKey.of("TEST", "ABC123"),
                    SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS);
                // @end region="whyRecordInEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long entityId = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                
                    if (entityId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate record.", e); // @highlight type="italic"
            }
            // @end region="whyRecordInEntity"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whyRecordInEntityDefaultDemo() {
        try {
            // @start region="whyRecordInEntityDefault"
            // How to determine why a record is a member of its respective entity
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // determine why the record is part of its entity
                // @highlight region="whyRecordInEntityCall"
                String responseJson = engine.whyRecordInEntity(
                    SzRecordKey.of("TEST", "ABC123"));
                // @end region="whyRecordInEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long entityId = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                
                    if (entityId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate record.", e); // @highlight type="italic"
            }
            // @end region="whyRecordInEntityDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    private SzRecordKey getWhyRecordsKey1() {
        return EMPLOYEE_MNO345;
    }

    private SzRecordKey getWhyRecordsKey2() {
        return EMPLOYEE_DEF890;
    }

    @Test
    public void whyRecordsDemo() {
        try {
            // @start region="whyRecords"
            // How to determine how two records are related
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the records on which to operate (varies by application)
                SzRecordKey recordKey1 = getWhyRecordsKey1(); // @highlight type="italic" substring="getWhyRecordsKey1()" @highlight substring="recordKey1"
                SzRecordKey recordKey2 = getWhyRecordsKey2(); // @highlight type="italic" substring="getWhyRecordsKey2()" @highlight substring="recordKey2"
                
                // determine how the records are related
                // @highlight region="whyRecordsCall"
                String responseJson = engine.whyRecords(recordKey1, 
                                                        recordKey2, 
                                                        SZ_WHY_RECORDS_DEFAULT_FLAGS);
                // @end region="whyRecordsCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long entityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    long entityId2 = result.getJsonNumber("ENTITY_ID_2").longValue(); // @highlight regex=".ENTITY_ID_2."

                    if (entityId1 < 0 || entityId2 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
            
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate record.", e); // @highlight type="italic"
            }
            // @end region="whyRecords"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whyRecordsDefaultDemo() {
        try {
            // @start region="whyRecordsDefault"
            // How to determine how two records are related
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the records on which to operate (varies by application)
                SzRecordKey recordKey1 = getWhyRecordsKey1(); // @highlight type="italic" substring="getWhyRecordsKey1()" @highlight substring="recordKey1"
                SzRecordKey recordKey2 = getWhyRecordsKey2(); // @highlight type="italic" substring="getWhyRecordsKey2()" @highlight substring="recordKey2"
                
                // determine how the records are related
                // @highlight region="whyRecordsCall"
                String responseJson = engine.whyRecords(recordKey1, recordKey2);
                // @end region="whyRecordsCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long entityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    long entityId2 = result.getJsonNumber("ENTITY_ID_2").longValue(); // @highlight regex=".ENTITY_ID_2."

                    if (entityId1 < 0 || entityId2 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
            
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for record key.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to reevaluate record.", e); // @highlight type="italic"
            }
            // @end region="whyRecordsDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    private long getWhyEntitiesId1() {
        return this.loadedRecordMap.get(this.getWhyRecordsKey1());
    }

    private long getWhyEntitiesId2() {
        return this.loadedRecordMap.get(this.getWhyRecordsKey2());
    }

    private long getWhySearchEntityId() {
        return this.loadedRecordMap.get(this.getWhyRecordsKey1());
    }

    @Test
    public void whySearchWithProfileDemo() {
        try {
            // @start region="whySearchWithProfile"
            // How to determine why an entity was excluded from search results
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // get a search profile (varies by application)
                String searchProfile = getSearchProfile(); // @highlight type="italic" substring="getSearchProfile()" @highlight substring="searchProfile"

                // get the entities on which to operate (varies by application)
                long entityId = getWhySearchEntityId(); // @highlight type="italic" substring="getWhySearchEntityId()" @highlight substring="entityId"
                
                // determine how the entities are related
                // @highlight region="whySearchCall"
                String responseJson = engine.whySearch(searchAttributes,
                                                       entityId,
                                                       searchProfile,
                                                       SZ_WHY_SEARCH_DEFAULT_FLAGS);
                // @end region="whySearchCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long whyEntityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."

                    if (whyEntityId1 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to perform why search.", e); // @highlight type="italic"
            }
            // @end region="whySearchWithProfile"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whySearchWithProfileDefaultDemo() {
        try {
            // @start region="whySearchWithProfileDefault"
            // How to determine why an entity was excluded from search results
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // get a search profile (varies by application)
                String searchProfile = getSearchProfile(); // @highlight type="italic" substring="getSearchProfile()" @highlight substring="searchProfile"

                // get the entities on which to operate (varies by application)
                long entityId = getWhySearchEntityId(); // @highlight type="italic" substring="getWhySearchEntityId()" @highlight substring="entityId"
                
                // determine how the entities are related
                // @highlight region="whySearchCall"
                String responseJson = engine.whySearch(searchAttributes,
                                                       entityId,
                                                       searchProfile);
                // @end region="whySearchCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long whyEntityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."

                    if (whyEntityId1 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to perform why search.", e); // @highlight type="italic"
            }
            // @end region="whySearchWithProfileDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whySearchDemo() {
        try {
            // @start region="whySearch"
            // How to determine why an entity was excluded from search results
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // get the entities on which to operate (varies by application)
                long entityId = getWhySearchEntityId(); // @highlight type="italic" substring="getWhySearchEntityId()" @highlight substring="entityId"
                
                // determine how the entities are related
                // @highlight region="whySearchCall"
                String responseJson = engine.whySearch(searchAttributes,
                                                       entityId,
                                                       SZ_WHY_SEARCH_DEFAULT_FLAGS);
                // @end region="whySearchCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long whyEntityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."

                    if (whyEntityId1 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to perform why search.", e); // @highlight type="italic"
            }
            // @end region="whySearch"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whySearchDefaultDemo() {
        try {
            // @start region="whySearchDefault"
            // How to determine why an entity was excluded from search results
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the search attributes (varies by application)
                String searchAttributes = // @highlight substring="searchAttributes"
                        // @highlight type="italic" region="searchAttributes"
                        """
                        {
                            "NAME_FULL": "Joe Schmoe",
                            "PHONE_NUMBER": "702-555-1212",
                            "EMAIL_ADDRESS": "joeschmoe@nowhere.com"
                        }
                        """;
                        // @end region="searchAttributes"
                
                // get the entities on which to operate (varies by application)
                long entityId = getWhySearchEntityId(); // @highlight type="italic" substring="getWhySearchEntityId()" @highlight substring="entityId"
                
                // determine how the entities are related
                // @highlight region="whySearchCall"
                String responseJson = engine.whySearch(searchAttributes, entityId);
                // @end region="whySearchCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long whyEntityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."

                    if (whyEntityId1 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to perform why search.", e); // @highlight type="italic"
            }
            // @end region="whySearchDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whyEntitiesDemo() {
        try {
            // @start region="whyEntities"
            // How to determine how two entities are related
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the entities on which to operate (varies by application)
                long entityId1 = getWhyEntitiesId1(); // @highlight type="italic" substring="getWhyEntitiesId1()" @highlight substring="entityId1"
                long entityId2 = getWhyEntitiesId2(); // @highlight type="italic" substring="getWhyEntitiesId2()" @highlight substring="entityId2"
                
                // determine how the entities are related
                // @highlight region="whyEntitiesCall"
                String responseJson = engine.whyEntities(entityId1,
                                                         entityId2,
                                                         SZ_WHY_ENTITIES_DEFAULT_FLAGS);
                // @end region="whyEntitiesCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long whyEntityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    long whyEntityId2 = result.getJsonNumber("ENTITY_ID_2").longValue(); // @highlight regex=".ENTITY_ID_2."

                    if (whyEntityId1 < 0 || whyEntityId2 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to perform why entities.", e); // @highlight type="italic"
            }
            // @end region="whyEntities"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void whyEntitiesDefaultDemo() {
        try {
            // @start region="whyEntitiesDefault"
            // How to determine how two entities are related
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the entities on which to operate (varies by application)
                long entityId1 = getWhyEntitiesId1(); // @highlight type="italic" substring="getWhyEntitiesId1()" @highlight substring="entityId1"
                long entityId2 = getWhyEntitiesId2(); // @highlight type="italic" substring="getWhyEntitiesId2()" @highlight substring="entityId2"
                
                // determine how the entities are related
                // @highlight region="whyEntitiesCall"
                String responseJson = engine.whyEntities(entityId1, entityId2);
                // @end region="whyEntitiesCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonArray resultsArr = jsonObject.getJsonArray("WHY_RESULTS"); // @highlight regex=".WHY_RESULTS."
                for (JsonObject result : resultsArr.getValuesAs(JsonObject.class)) {
                    long whyEntityId1 = result.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                    long whyEntityId2 = result.getJsonNumber("ENTITY_ID_2").longValue(); // @highlight regex=".ENTITY_ID_2."

                    if (whyEntityId1 < 0 || whyEntityId2 < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
            
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to perform why entities.", e); // @highlight type="italic"
            }
            // @end region="whyEntitiesDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void howEntityDemo() {
        try {
            // @start region="howEntity"
            // How to retrieve the "how analysis" for an entity via its entity ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the entity ID on which to operate (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // determine how the entity was formed
                // @highlight region="howEntityCall"
                String responseJson = engine.howEntity(entityId, SZ_HOW_ENTITY_DEFAULT_FLAGS);
                // @end region="howEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  howResults  = jsonObject.getJsonObject("HOW_RESULTS"); // @highlight regex=".HOW_RESULTS."
                JsonArray   stepsArr    = howResults.getJsonArray("RESOLUTION_STEPS"); // @highlight regex=".RESOLUTION_STEPS."

                for (JsonObject step : stepsArr.getValuesAs(JsonObject.class)) {
                    JsonObject matchInfo = step.getJsonObject("MATCH_INFO"); // @highlight regex=".MATCH_INFO."
                
                    if (matchInfo == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve how analysis.", e); // @highlight type="italic"
            }
            // @end region="howEntity"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void howEntityDefaultDemo() {
        try {
            // @start region="howEntityDefault"
            // How to retrieve the "how analysis" for an entity via its entity ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the entity ID on which to operate (varies by application)
                long entityId = getEntityId(); // @highlight type="italic" substring="getEntityId()" @highlight substring="entityId"

                // determine how the entity was formed
                // @highlight region="howEntityCall"
                String responseJson = engine.howEntity(entityId);
                // @end region="howEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  howResults  = jsonObject.getJsonObject("HOW_RESULTS"); // @highlight regex=".HOW_RESULTS."
                JsonArray   stepsArr    = howResults.getJsonArray("RESOLUTION_STEPS"); // @highlight regex=".RESOLUTION_STEPS."

                for (JsonObject step : stepsArr.getValuesAs(JsonObject.class)) {
                    JsonObject matchInfo = step.getJsonObject("MATCH_INFO"); // @highlight regex=".MATCH_INFO."
                
                    if (matchInfo == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Entity not found for entity ID.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve how analysis.", e); // @highlight type="italic"
            }
            // @end region="howEntityDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    private Set<SzRecordKey> getVirtualEntityRecordKeys() {
        return set(PASSENGER_DEF456, PASSENGER_GHI789, PASSENGER_JKL012);
    }

    @Test
    public void getVirtualEntityDemo() {
        try {
            // @start region="getVirtualEntity"
            // How to retrieve a virtual entity via a set of record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the records to operate on (varies by application)
                Set<SzRecordKey> recordKeys = getVirtualEntityRecordKeys(); // @highlight type="italic" substring="getVirtualEntityRecordKeys()" @highlight substring="recordKeys"

                // retrieve the virtual entity for the record keys
                // @highlight region="getVirtualEntityCall"
                String responseJson = engine.getVirtualEntity(recordKeys, SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS);
                // @end region="getVirtualEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  entity      = jsonObject.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                String      entityName  = entity.getString("ENTITY_NAME"); // @highlight regex=".ENTITY_NAME."
            
                if (entityName == "FOO") { throw new Exception(); } // @replace regex="if.*" replacement="..."

                if (jsonObject.containsKey("RECORDS")) { // @highlight regex=".RECORDS."
                    JsonArray recordArr = jsonObject.getJsonArray("RECORDS"); // @highlight regex=".RECORDS."
                    for (JsonObject record : recordArr.getValuesAs(JsonObject.class)) {
                        String dataSource = record.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                        String recordId   = record.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                    
                        if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Specified record key was not found.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve virtual entity.", e); // @highlight type="italic"
            }
            // @end region="getVirtualEntity"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getVirtualEntityDefaultDemo() {
        try {
            // @start region="getVirtualEntityDefault"
            // How to retrieve a virtual entity via a set of record keys
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // get the records to operate on (varies by application)
                Set<SzRecordKey> recordKeys = getVirtualEntityRecordKeys(); // @highlight type="italic" substring="getVirtualEntityRecordKeys()" @highlight substring="recordKeys"

                // retrieve the virtual entity for the record keys
                // @highlight region="getVirtualEntityCall"
                String responseJson = engine.getVirtualEntity(recordKeys);
                // @end region="getVirtualEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                JsonObject  entity      = jsonObject.getJsonObject("RESOLVED_ENTITY"); // @highlight regex=".RESOLVED_ENTITY."
                String      entityName  = entity.getString("ENTITY_NAME"); // @highlight regex=".ENTITY_NAME."
            
                if (entityName == "FOO") { throw new Exception(); } // @replace regex="if.*" replacement="..."

                if (jsonObject.containsKey("RECORDS")) { // @highlight regex=".RECORDS."
                    JsonArray recordArr = jsonObject.getJsonArray("RECORDS"); // @highlight regex=".RECORDS."
                    for (JsonObject record : recordArr.getValuesAs(JsonObject.class)) {
                        String dataSource = record.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                        String recordId   = record.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                    
                        if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }
                }
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Specified record key was not found.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve virtual entity.", e); // @highlight type="italic"
            }
            // @end region="getVirtualEntityDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getRecordDemo() {
        try {
            // @start region="getRecord"
            // How to retrieve a record via its record key
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // retrieve the entity by record key
                // @highlight region="getEntityCall"
                String responseJson = engine.getRecord(
                    SzRecordKey.of("TEST", "ABC123"),
                    SZ_ENTITY_DEFAULT_FLAGS);
                // @end region="getEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                String      dataSource  = jsonObject.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                String      recordId    = jsonObject.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                
                if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Record not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve record by record key.", e); // @highlight type="italic"
            }
            // @end region="getRecord"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getRecordDefaultDemo() {
        try {
            // @start region="getRecordDefault"
            // How to retrieve a record via its record key
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // retrieve the entity by record key
                // @highlight region="getEntityCall"
                String responseJson = engine.getRecord(
                    SzRecordKey.of("TEST", "ABC123"));
                // @end region="getEntityCall"

                // do something with the response JSON (varies by application)
                // @highlight type="italic" region="doSomething"
                JsonObject  jsonObject  = Json.createReader(new StringReader(responseJson)).readObject(); // @highlight regex="responseJson"
                String      dataSource  = jsonObject.getString("DATA_SOURCE"); // @highlight regex=".DATA_SOURCE."
                String      recordId    = jsonObject.getString("RECORD_ID"); // @highlight regex=".RECORD_ID."
                
                if (dataSource == null && recordId == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzUnknownDataSourceException e) {
                // handle the unknown data source exception
                logError("Expected data source is not configured.", e); // @highlight type="italic"
                
            } catch (SzNotFoundException e) {
                // handle the not-found exception
                logError("Record not found for record key.", e); // @highlight type="italic"
                
            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to retrieve record by record key.", e); // @highlight type="italic"
            }
            // @end region="getRecordDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    private static void processJsonRecord(JsonObject jsonObject) {
        if (jsonObject == null) throw new NullPointerException();
    }

    @Test
    public void exportJsonDemo() {
        try {
            // @start region="exportJson"
            // How to export entity data in JSON format
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // export the JSON data
                long exportHandle = engine.exportJsonEntityReport(SZ_EXPORT_DEFAULT_FLAGS); // @highlight regex="long.*"

                // read the data
                try {
                    // fetch the first JSON record
                    String jsonData = engine.fetchNext(exportHandle); // @highlight regex="String.*"

                    while (jsonData != null) {
                        // parse the JSON data
                        JsonObject jsonObject = Json.createReader(new StringReader(jsonData)).readObject();

                        // do something with the parsed data (varies by application)
                        processJsonRecord(jsonObject); // @highlight type="italic" regex="process.*"
                        
                        // fetch the next JSON record
                        jsonData = engine.fetchNext(exportHandle); // @highlight regex="jsonData.*"
                    }

                } finally {
                    // close the export handle
                    engine.closeExport(exportHandle); // @highlight regex="engine.*"
                }

            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to perform JSON export.", e); // @highlight type="italic"
            }
            // @end region="exportJson"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void exportJsonDefaultDemo() {
        try {
            // @start region="exportJsonDefault"
            // How to export entity data in JSON format
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // export the JSON data
                long exportHandle = engine.exportJsonEntityReport(); // @highlight regex="long.*"

                // read the data
                try {
                    // fetch the first JSON record
                    String jsonData = engine.fetchNext(exportHandle); // @highlight regex="String.*"

                    while (jsonData != null) {
                        // parse the JSON data
                        JsonObject jsonObject = Json.createReader(new StringReader(jsonData)).readObject();

                        // do something with the parsed data (varies by application)
                        processJsonRecord(jsonObject); // @highlight type="italic" regex="process.*"
                        
                        // fetch the next JSON record
                        jsonData = engine.fetchNext(exportHandle); // @highlight regex="jsonData.*"
                    }

                } finally {
                    // close the export handle
                    engine.closeExport(exportHandle); // @highlight regex="engine.*"
                }

            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to perform JSON export.", e); // @highlight type="italic"
            }
            // @end region="exportJsonDefault"

        } catch (Exception e) {
            fail(e);
        }
    }


    private static void processCsvHeaders(String headerLine) {
        if (headerLine == null) throw new NullPointerException();
    }

    private static void processCsvRecord(String recordLine) {
        if (recordLine == null) throw new NullPointerException();
    }

    @Test
    public void exportCsvDemo() {
        try {
            // @start region="exportCsv"
            // How to export entity data in CSV format
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // export the JSON data
                long exportHandle = engine.exportCsvEntityReport("*", SZ_EXPORT_DEFAULT_FLAGS); // @highlight regex="long.*"

                // read the data
                try {
                    // fetch the CSV header line from the exported data
                    String csvHeaders = engine.fetchNext(exportHandle); // @highlight regex="String.*"

                    // process the CSV headers (varies by application)
                    processCsvHeaders(csvHeaders); // @highlight type="italic" regex="process.*"

                    // fetch the first CSV record from the exported data
                    String csvRecord = engine.fetchNext(exportHandle); // @highlight regex="String.*"

                    while (csvRecord != null) {
                        // do something with the exported record (varies by application)
                        processCsvRecord(csvRecord); // @highlight type="italic" regex="process.*"

                        // fetch the next exported CSV record
                        csvRecord = engine.fetchNext(exportHandle); // @highlight regex="csvRecord.*"
                    }

                } finally {
                    // close the export handle
                    engine.closeExport(exportHandle); // @highlight regex="engine.*"
                }

            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to perform CSV export.", e); // @highlight type="italic"
            }
            // @end region="exportCsv"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void exportCsvDefaultDemo() {
        try {
            // @start region="exportCsvDefault"
            // How to export entity data in CSV format
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();
                
                // export the JSON data
                long exportHandle = engine.exportCsvEntityReport("*"); // @highlight regex="long.*"

                // read the data
                try {
                    // fetch the CSV header line from the exported data
                    String csvHeaders = engine.fetchNext(exportHandle); // @highlight regex="String.*"

                    // process the CSV headers (varies by application)
                    processCsvHeaders(csvHeaders); // @highlight type="italic" regex="process.*"

                    // fetch the first CSV record from the exported data
                    String csvRecord = engine.fetchNext(exportHandle); // @highlight regex="String.*"

                    while (csvRecord != null) {
                        // do something with the exported record (varies by application)
                        processCsvRecord(csvRecord); // @highlight type="italic" regex="process.*"

                        // fetch the next exported CSV record
                        csvRecord = engine.fetchNext(exportHandle); // @highlight regex="csvRecord.*"
                    }

                } finally {
                    // close the export handle
                    engine.closeExport(exportHandle); // @highlight regex="engine.*"
                }

            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to perform CSV export.", e); // @highlight type="italic"
            }
            // @end region="exportCsvDefault"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void processRedosDemo() {
        try {
            // @start region="processRedos"
            // How to check for and process redo records
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get the redo count
                long redoCount = engine.countRedoRecords(); // @highlight regex="long.*"

                // check if we have redo records
                if (redoCount > 0L) { // @highlight substring="redoCount"
                    
                    // get the next redo record
                    String redoRecord = engine.getRedoRecord(); // @highlight regex="String.*"

                    // loop while we still have redo records
                    while (redoRecord != null) {
                        try {
                            // process the redo record
                            String infoJson = engine.processRedoRecord(redoRecord, SZ_WITH_INFO_FLAGS); // @highlight regex="String.*"

                            // do something with the "info JSON" (varies by application)
                            // @highlight type="italic" region="doSomething"
                            JsonObject jsonObject = Json.createReader(new StringReader(infoJson)).readObject(); // @highlight regex="infoJson"
                            if (jsonObject.containsKey("AFFECTED_ENTITIES")) { // @highlight regex=".AFFECTED_ENTITIES."
                                JsonArray affectedArr = jsonObject.getJsonArray("AFFECTED_ENTITIES"); // @highlight regex=".AFFECTED_ENTITIES."
                                for (JsonObject affected : affectedArr.getValuesAs(JsonObject.class)) {
                                    long affectedId = affected.getJsonNumber("ENTITY_ID").longValue(); // @highlight regex=".ENTITY_ID."
                                
                                    if (affectedId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                                }
                            }
                            // @end region="doSomething"

                        } catch (SzException e) {
                            // handle or rethrow the other exceptions
                            logError("Failed to process redo record: " + redoRecord, e); // @highlight type="italic"
                        }

                        // get the next redo record
                        redoRecord = engine.getRedoRecord(); // @highlight regex="redoRecord.*"
                    }
                }

            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to process redos.", e); // @highlight type="italic"
            }
            // @end region="processRedos"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void processRedosDefaultDemo() {
        try {
            // @start region="processRedosDefault"
            // How to check for and process redo records
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the engine
                SzEngine engine = env.getEngine();

                // get the redo count
                long redoCount = engine.countRedoRecords(); // @highlight regex="long.*"

                // check if we have redo records
                if (redoCount > 0L) { // @highlight substring="redoCount"
                    
                    // get the next redo record
                    String redoRecord = engine.getRedoRecord(); // @highlight regex="String.*"

                    // loop while we still have redo records
                    while (redoRecord != null) {
                        try {
                            // process the redo record
                            engine.processRedoRecord(redoRecord); // @highlight regex="engine.*"

                        } catch (SzException e) {
                            // handle or rethrow the other exceptions
                            logError("Failed to process redo record: " + redoRecord, e); // @highlight type="italic"
                        }

                        // get the next redo record
                        redoRecord = engine.getRedoRecord(); // @highlight regex="redoRecord.*"
                    }
                }

            } catch (SzException e) {
                // handle or rethrow the other exceptions
                logError("Failed to process redos.", e); // @highlight type="italic"
            }
            // @end region="processRedosDefault"

        } catch (Exception e) {
            fail(e);
        }
    }
}
