package com.senzing.sdk;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import com.senzing.sdk.core.*;
import javax.json.*;

import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.util.JsonUtilities.*;

/**
 * Provides examples of using {@link SzDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzDiagnosticDemo extends AbstractCoreTest {
    private static final String TEST_DATA_SOURCE = "TEST";
    private static final String TEST_RECORD_ID = "ABC123";

    private static final Set<SzFlag> FLAGS 
        = Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_ALL_FEATURES,
            SZ_ENTITY_INCLUDE_ENTITY_NAME,
            SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
            SZ_ENTITY_INCLUDE_RECORD_TYPES,
            SZ_ENTITY_INCLUDE_RECORD_DATA,
            SZ_ENTITY_INCLUDE_RECORD_JSON_DATA,
            SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO,
            SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA,
            SZ_ENTITY_INCLUDE_RECORD_FEATURES,
            SZ_ENTITY_INCLUDE_INTERNAL_FEATURES,
            SZ_INCLUDE_MATCH_KEY_DETAILS,
            SZ_INCLUDE_FEATURE_SCORES));
        
    private SzCoreEnvironment env = null;

    private long featureId = 0L;

    private final File repoDirectory;

    public SzDiagnosticDemo() {
        try {
            this.repoDirectory = Files.createTempDirectory("sz-example-").toFile();
            this.repoDirectory.mkdirs();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

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

        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("DATA_SOURCE", "TEST");
        job.add("RECORD_ID", "ABC123");
        job.add("NAME_FULL", "Joe Schmoe");
        job.add("EMAIL_ADDRESS", "joeschmoe@nowhere.com");
        job.add("PHONE_NUMBER", "702-555-1212");
        JsonObject jsonObj = job.build();
        String recordDefinition = jsonObj.toString();

        try {
            SzEngine engine = this.env.getEngine();

            SzRecordKey recordKey = SzRecordKey.of(TEST_DATA_SOURCE, TEST_RECORD_ID);
            engine.addRecord(recordKey, recordDefinition, SZ_NO_FLAGS);

            String entityJson = engine.getEntity(recordKey, FLAGS);

            JsonObject entity = parseJsonObject(entityJson);
            entity = entity.getJsonObject("RESOLVED_ENTITY");
            JsonObject features = entity.getJsonObject("FEATURES");
            for (String featureName : features.keySet()) {
                JsonArray featureArr = features.getJsonArray(featureName);
                for (JsonObject feature : featureArr.getValuesAs(JsonObject.class)) {
                    this.featureId = getLong(feature, "LIB_FEAT_ID");
                    if (this.featureId != 0L) {
                        break;
                    }
                }
            }
        
        } catch (SzException e) {
            throw new RuntimeException(e);
        }

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

    /**
     * Use a simplified path to the repository so the SQLlite file path
     * does not look confusing in the output for {@link SzDiagnostic#getRepositoryInfo()}.
     * 
     * @return A basic temporary directory including the name <code>"sz-example-"</code>.
     */
    protected File getRepositoryDirectory() {
        return this.repoDirectory;
    }

    /**
     * Don't use the hybrid database setup because it looks confusing in the
     * output for {@link SzDiagnostic#getRepositoryInfo()}.
     * 
     * @return <code>false</code>.
     */
    protected boolean isUsingHybridDatabase() {
        return false;
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
    @Order(10)
    public void getDiagnosticDemo() {
        try {
            // @start region="getDiagnostic"
            // How to obtain an SzDiagnostic instance
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                SzDiagnostic diagnostic = env.getDiagnostic();  // @highlight   

                if (diagnostic == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get SzDiagnostic.", e); // @highlight type="italic"
            }
            // @end region="getDiagnostic"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @Order(20)
    public void getRepositoryInfoDemo() {
        try {
            String info = null;
            // @start region="getRepositoryInfo"
            // How to get repository info via SzDiagnostic
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the diagnostic instance
                SzDiagnostic diagnostic = env.getDiagnostic();  // @highlight   

                // get the repository info
                info = diagnostic.getRepositoryInfo(); // @replace regex="info " replacement="String info " @highlight regex="String.*"

                // do something with the returned JSON
                log(info); // @highlight type="italic" regex="log.*"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get the repository info.", e); // @highlight type="italic"
            }
            // @end region="getRepositoryInfo"
            this.saveDemoResult("getRepositoryInfo", info, true);
            
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @Order(30)
    public void checkRepositoryPerformanceDemo() {
        try {
            String result = null;
            // @start region="checkRepositoryPerformance"
            // How to get repository info via SzDiagnostic
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the diagnostic instance
                SzDiagnostic diagnostic = env.getDiagnostic();  // @highlight   

                // check the repository performance
                result = diagnostic.checkRepositoryPerformance(10); // @replace regex="result " replacement="String result " @highlight regex="String.*"

                // do something with the returned JSON
                log(result); // @highlight type="italic" regex="log.*"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to check the repository performance.", e); // @highlight type="italic"
            }
            // @end region="checkRepositoryPerformance"

            this.saveDemoResult("checkRepositoryPerformance", result, true);

        } catch (Exception e) {
            fail(e);
        }
    }

    public long getFeatureId() {
        return this.featureId;
    }

    @Test
    @Order(40)
    public void getFeatureDemo() {
        try {
            String result = null;
            // @start region="getFeature"
            // How to get a feature by its feature ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the diagnostic instance
                SzDiagnostic diagnostic = env.getDiagnostic();  // @highlight   

                // get a valid feature (varies by application)
                long featureId = getFeatureId(); // @highlight type="italic" regex="getFeatureId.."

                // get the feature for the feature ID
                result = diagnostic.getFeature(featureId); // @replace regex="result " replacement="String result " @highlight regex="String.*"

                // do something with the returned JSON
                log(result); // @highlight type="italic" regex="log.*"
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to purge the repository.", e); // @highlight type="italic"
            }
            // @end region="getFeature"
            this.saveDemoResult("getFeature", result, true);

        } catch (Exception e) {
            fail(e);
        }
    }

    public static boolean confirmPurge() {
        return true;
    }

    @Test
    @Order(50)
    public void purgeRepositoryDemo() {
        try {
            // @start region="purgeRepository"
            // How to purge the Senzing repository
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the diagnostic instance
                SzDiagnostic diagnostic = env.getDiagnostic();  // @highlight   

                // purge the repository (MAKE SURE YOU WANT TO DO THIS)
                if (confirmPurge()) {   // @highlight type="italic" regex="confirmPurge.."
                    diagnostic.purgeRepository(); // @highlight regex="diagnostic.*"
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to purge the repository.", e); // @highlight type="italic"
            }
            // @end region="purgeRepository"

        } catch (Exception e) {
            fail(e);
        }
    }

}
