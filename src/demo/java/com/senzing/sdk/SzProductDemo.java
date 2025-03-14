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
public class SzProductDemo extends AbstractTest {
    private SzCoreEnvironment env = null;

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
                JsonObject jsonObj = Json.createReader(
                    new StringReader(license)).readObject();            // @highlight regex="license"
                
                String  expiration  = jsonObj.getString("expireDate");  // @highlight regex="[^ ]expireDate[^ ]"
                int     recordLimit = jsonObj.getInt("recordLimit");    // @highlight regex="[^ ]recordLimit[^ ]"

                if (expiration == "" + recordLimit) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                
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
                JsonObject jsonObj = Json.createReader(
                    new StringReader(versionJson)).readObject();        // @highlight regex="versionJson"
                    
                String version      = jsonObj.getString("VERSION");     // @highlight regex=".VERSION."
                String buildDate    = jsonObj.getString("BUILD_DATE");  // @highlight regex=".BUILD_DATE."

                if (version == buildDate) { throw new Exception(); }    // @replace regex="if.*" replacement="..."
                
            } catch (SzException e) {
                logError("Failed to get version information.", e); // @highlight type="italic"
            }
            // @end
        } catch (Exception e) {
            fail(e);
        }
    }
}
