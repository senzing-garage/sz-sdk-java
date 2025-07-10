package com.senzing.sdk;

import java.io.StringReader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.core.*;
import javax.json.*;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Provides examples of using {@link SzProduct}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzProductDemo extends AbstractCoreTest {
    private SzCoreEnvironment env = null;

    protected String getInstanceName() {
        return this.getClass().getSimpleName();
    }

    private String getSettings() {
        return this.getRepoSettings();
    }

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();

        // @start region="SzEnvironment"
        // get the settings (varies by application)
        String settings = getSettings(); // @highlight type="italic" substring="getSettings()" @highlight substring="settings"
        
        // get the instance name (varies by application)
        String instanceName = getInstanceName(); // @highlight type="italic" substring="getInstanceName()" @highlight substring="instanceName"

        // construct the environment
        // @highlight region="buildEnvironment"
        SzEnvironment env = SzCoreEnvironment.newBuilder()
                                             .instanceName(instanceName)
                                             .settings(settings)
                                             .verboseLogging(false)
                                             .build();
        // @end region="buildEnvironment"

        // use the environment for some time (usually as long as application is running)
        if (env == null) { throw new NullPointerException(); } // @replace regex="if.*" replacement="..."

        // destroy the environment when done (sometimes in a finally block)
        env.destroy(); // @highlight regex="env.*"

        // @end region="SzEnvironment"
        this.env = SzCoreEnvironment.newBuilder()
                                    .instanceName(instanceName)
                                    .settings(settings)
                                    .verboseLogging(false)
                                    .build();
    }

    @AfterAll
    public void teardownEnvironment() {
        try {
            if (this.env != null) {
                // @start region="destroyEnvironment"
                // obtain the SzEnvironment (varies by application)
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"

                if (!env.isDestroyed()) { // @highlight substring="env.isDestroyed()"
                    // destroy the environment
                    env.destroy(); // @highlight regex="env.*"
                } 

                // @end region="destroyEnvironment"
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

    @Test
    public void getProductDemo() {
        try {
            // @start region="getProduct"
            // How to obtain an SzProduct instance
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                SzProduct product = env.getProduct();  // @highlight   

                if (product == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get SzProduct.", e); // @highlight type="italic"
            }
            // @end region="getProduct"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void licenseDemo() {
        try {
            // @start region="getLicense"
            // How to obtain the Senzing product license JSON 
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzProduct instance
                SzProduct product = env.getProduct();
            
                // obtain the license JSON
                String license = product.getLicense(); // @highlight type="bold" regex="String.*;"

                // do something with the returned JSON (e.g.: parse it and extract values)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObj = Json.createReader(
                    new StringReader(license)).readObject();            // @highlight regex="license"
                
                String  expiration  = jsonObj.getString("expireDate");  // @highlight regex="[^ ]expireDate[^ ]"
                int     recordLimit = jsonObj.getInt("recordLimit");    // @highlight regex="[^ ]recordLimit[^ ]"

                if (expiration == "" + recordLimit) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get license information.", e); // @highlight type="italic"
            }
            // @end region="getLicense"
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void versionDemo() {
        try {
            // @start region="getVersion"
            // How to obtain the Senzing product version JSON 
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzProduct instance
                SzProduct product = env.getProduct();
            
                // obtain the version JSON
                String versionJson = product.getVersion(); // @highlight

                // do something with the returned JSON (e.g.: parse it and extract values)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObj = Json.createReader(
                    new StringReader(versionJson)).readObject();        // @highlight regex="versionJson"
                    
                String version      = jsonObj.getString("VERSION");     // @highlight regex=".VERSION."
                String buildDate    = jsonObj.getString("BUILD_DATE");  // @highlight regex=".BUILD_DATE."

                if (version == buildDate) { throw new Exception(); }    // @replace regex="if.*" replacement="..."
                // @end region="doSomething"

            } catch (SzException e) {
                logError("Failed to get version information.", e); // @highlight type="italic"
            }
            // @end
        } catch (Exception e) {
            fail(e);
        }
    }
}
