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
 * Provides examples of using {@link SzConfigManager}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzConfigManagerDemo extends AbstractCoreTest {
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
    public void getConfigManagerDemo() {
        try {
            // @start region="getConfigManager"
            // How to obtain an SzConfigManager instance
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();  // @highlight   

                if (configMgr == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get SzConfigManager.", e); // @highlight type="italic"
            }
            // @end region="getConfigManager"

        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Simulates reading a config file from disk.
     * 
     * @param dataSources The zero or more data sources to add to the config.
     * 
     * @return The config definition JSON.
     */
    public String createConfigWithDataSources(String... dataSources) throws SzException {
        SzEnvironment env = getEnvironment();
        SzConfigManager configMgr = env.getConfigManager();

        SzConfig config = configMgr.createConfig();

        if (dataSources != null) {
            for (String dataSource : dataSources) {
                config.registerDataSource(dataSource);
            }
        }

        return config.export();
    } 

    /**
     * Simulates reading a config file from disk.
     * 
     * @param dataSources The zero or more data sources to add to the config.
     * 
     * @return The config definition JSON.
     */
    public String addDataSourcesToConfig(long configId, String... dataSources) throws SzException {
        SzEnvironment env = getEnvironment();
        
        SzConfigManager configMgr = env.getConfigManager();
        
        SzConfig config = configMgr.createConfig(configId);

        if (dataSources != null) {
            for (String dataSource : dataSources) {
                config.registerDataSource(dataSource);
            }
        }
        return config.export();
    } 

    @Test
    public void registerConfigDemo() {
        try {
            // @start region="registerConfig"
            // How to register a configuration with the Senzing repository
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // obtain a JSON config definition (will vary by application)
                String configDefinition = createConfigWithDataSources("EMPLOYEES"); // @highlight type="italic" regex="createConfigWith.*"

                // register the config (using an auto-generated comment)
                long configId = configMgr.registerConfig(configDefinition); // @highlight regex="long.*"
                
                // do something with the config ID
                if (configId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to register configuration.", e); // @highlight type="italic"
            }
            // @end region="registerConfig"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void registerConfigWithCommentDemo() {
        try {
            // @start region="registerConfigWithComment"
            // How to register a configuration with the Senzing repository
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // obtain a JSON config definition (will vary by application)
                String configDefinition = createConfigWithDataSources("CUSTOMERS"); // @highlight type="italic" regex="createConfigWith.*"

                // register the config with a custom comment
                long configId = configMgr.registerConfig(configDefinition, "Added CUSTOMERS data source"); // @highlight regex="long.*"

                // do something with the config ID
                if (configId < 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to register configuration.", e); // @highlight type="italic"
            }
            // @end region="registerConfigWithComment"

        } catch (Exception e) {
            fail(e);
        }
    }


    @Test
    public void createConfigFromConfigIdDemo() {
        try {
            // @start region="createConfigFromConfigId"
            // How to get a config definition by its configuration ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // get a valid configuration ID (will vary by application)
                long configId = configMgr.getDefaultConfigId(); // @highlight type="italic" regex="getDefault.*"

                // get the config definition for the config ID
                SzConfig config = configMgr.createConfig(configId); // @highlight regex="SzConfig.*"

                // do something with the SzConfig
                if (config == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to create SzConfig from config ID.", e); // @highlight type="italic"
            }
            // @end region="createConfigFromConfigId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getConfigRegistryDemo() {
        try {
            // @start region="getConfigRegistry"
            // How to get a JSON document describing all registered configs
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // get the config definition for the config ID
                String configRegistry = configMgr.getConfigRegistry(); // @highlight regex="String.*"

                // do something with the returned JSON (e.g.: parse it and extract values)
                // @highlight type="italic" region="doSomething"
                JsonObject jsonObj = Json.createReader(
                    new StringReader(configRegistry)).readObject();    // @highlight regex="configsJson"
                
                JsonArray jsonArr = jsonObj.getJsonArray("CONFIGS");   // @highlight regex=".CONFIGS."

                // iterate over the registered configurations
                for (JsonObject configObj : jsonArr.getValuesAs(JsonObject.class)) {
                    long    configId    = configObj.getJsonNumber("CONFIG_ID").longValue(); // @highlight regex=".CONFIG_ID."
                    String  createdOn   = configObj.getString("SYS_CREATE_DT");             // @highlight regex=".SYS_CREATE_DT."
                    String  comment     = configObj.getString("CONFIG_COMMENTS");           // @highlight regex=".CONFIG_COMMENTS."

                    if (createdOn + comment == "" + configId) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get configurations.", e); // @highlight type="italic"
            }
            // @end region="getConfigRegistry"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getDefaultConfigIdDemo() {
        try {
            // @start region="getDefaultConfigId"
            // How to get the registered default configuration ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // get the default configuration ID
                long configId = configMgr.getDefaultConfigId(); // @highlight regex="long.*"
                
                // check if no default configuration ID is registered
                // @highlight type="italic" region="doSomething"
                if (configId == 0) {
                    // handle having no registered configuration ID
                    throw new IllegalStateException(); // @replace regex="throw.*" replacement="..."
                } else {
                    // do something with the configuration ID
                    if (configId == 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get the default configuration ID.", e); // @highlight type="italic"
            }
            // @end region="getDefaultConfigId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void setDefaultConfigIdDemo() {
        try {
            // @start region="setDefaultConfigId"
            // How to set the registered default configuration ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // get the configuration ID (varies by application)
                String configDefinition = createConfigWithDataSources("WATCHLIST"); // @highlight type="italic" regex="String.*"
                long configId = configMgr.registerConfig(configDefinition);
                
                // set the default config ID
                configMgr.setDefaultConfigId(configId); // @highlight regex="configMgr.*"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to set default configuration ID.", e); // @highlight type="italic"
            }
            // @end region="setDefaultConfigId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void setDefaultConfigDemo() {
        try {
            // @start region="setDefaultConfig"
            // How to set the registered default configuration ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // get the configuration ID (varies by application)
                String configDefinition = createConfigWithDataSources("VIPS"); // @highlight type="italic" regex="String.*"
                
                // set the default config (using an auto-generated comment)
                long configId = configMgr.setDefaultConfig(configDefinition); // @highlight regex="long.*"

                // do something with the registered config ID
                if (configId == 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to set default configuration.", e); // @highlight type="italic"
            }
            // @end region="setDefaultConfig"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void setDefaultConfigWithCommentDemo() {
        try {
            // @start region="setDefaultConfigWithComment"
            // How to set the registered default configuration ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                // get the configuration ID (varies by application)
                String configDefinition = createConfigWithDataSources("COMPANIES"); // @highlight type="italic" regex="String.*"
                
                // set the default config (using a specific comment)
                long configId = configMgr.setDefaultConfig(configDefinition, "Initial config with COMPANIES"); // @highlight regex="long.*"

                // do something with the registered config ID
                if (configId == 0) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to set default configuration.", e); // @highlight type="italic"
            }
            // @end region="setDefaultConfigWithComment"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void replaceDefaultConfigIdDemo() {
        try {
            // @start region="replaceDefaultConfigId"
            // How to replace the registered default configuration ID
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the config manager
                SzConfigManager configMgr = env.getConfigManager();

                do {
                    // get the current default configuration ID
                    long oldConfigId = configMgr.getDefaultConfigId(); // @highlight regex="long.*"

                    // create a new config (usually modifying the current -- varies by application)
                    String configDefinition = addDataSourcesToConfig(oldConfigId, "PASSENGERS"); // @highlight type="italic" regex="String.*"
                    long   newConfigId      = configMgr.registerConfig(configDefinition, "Added PASSENGERS data source"); // @highlight type="italic" regex="configMgr.*"
                
                    try {
                        // replace the default config ID with the new config ID
                        configMgr.replaceDefaultConfigId(oldConfigId, newConfigId); // @highlight regex="configMgr.*"

                        // if we get here then break out of the loop
                        break;

                    } catch (SzReplaceConflictException e) { // @highlight regex="SzReplaceConflictException"
                        // race condition detected
                        // do nothing so we loop through and try again
                    }

                } while (true);

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to replace default configuration ID.", e); // @highlight type="italic"
            }
            // @end region="replaceDefaultConfigId"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getActiveConfigIdDemo() {
        try {
            // @start region="getActiveConfigId"
            // How to get the active config ID for the SzEnvironment
            try {
                // obtain the SzEnvironment (varies by application)
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"

                // get the active config ID
                long activeConfigId = env.getActiveConfigId(); // @highlight regex="long.*"

                // do something with the active config ID (varies by application)
                // @highlight type="italic" region="doSomething"
                SzConfigManager configMgr = env.getConfigManager();

                long defaultConfigId = configMgr.getDefaultConfigId();

                if (activeConfigId != defaultConfigId) {
                    // reinitialize the environment with the default config ID                    
                    env.reinitialize(defaultConfigId); // @highlight regex="env.*"
                }
                // @end region="doSomething"

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to verify the active config ID.", e); // @highlight type="italic"
            }
            // @end region="getActiveConfigId"

        } catch (Exception e) {
            fail(e);
        }
    }


}
