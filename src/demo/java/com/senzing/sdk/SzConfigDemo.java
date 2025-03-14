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
 * Provides examples of using {@link SzConfig}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzConfigDemo extends AbstractTest {
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
    public void getConfigDemo() {
        try {
            // @start region="getConfig"
            // How to obtain an SzConfig instance
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                SzConfig config = env.getConfig();  // @highlight   

                if (config == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get SzConfig.", e); // @highlight type="italic"
            }
            // @end region="getConfig"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void createConfigDemo() {
        try {
            // @start region="createConfig"
            // How to create a new in-memory config and obtain a handle
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzConfig instance
                SzConfig config = env.getConfig();
            
                // create the config and obtain the handle
                long configHandle = config.createConfig(); // @highlight type="bold" regex="long.*;"

                // do something with the config handle
                try {
                    if (configHandle == 0) { throw new Exception(); }// @replace regex="if.*;" replacement="..."

                } finally {
                    // close the config handle
                    config.closeConfig(configHandle); // @highlight type="bold" regex="config.*;"
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to create new configuration.", e); // @highlight type="italic"
            }
            // @end region="createConfig"
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Simulates reading a config file from disk.
     * 
     * @return The config defintion JSON.
     */
    public String readConfigFile() throws SzException {
        SzEnvironment env = getEnvironment();
        SzConfig config = env.getConfig();
        long configHandle = config.createConfig();
        try {
            return config.exportConfig(configHandle);
        } finally {
            config.closeConfig(configHandle);
        }
    } 

    @Test
    public void importConfigDemo() {
        try {
            // @start region="importConfig"
            // How to import config JSON into an in-memory config
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzConfig instance
                SzConfig config = env.getConfig();
            
                // obtain a JSON config definition
                String configDefinition = readConfigFile(); // @highlight type="italic" regex="readConfigFile.."

                // import the config and obtain the handle
                long configHandle = config.importConfig(configDefinition); // @highlight type="bold" regex="long.*;"

                // do something with the config handle
                try {
                    if (configHandle == 0) { throw new Exception(); }// @replace regex="if.*;" replacement="..."

                } finally {
                    // close the config handle
                    config.closeConfig(configHandle); // @highlight type="bold" regex="config.*;"
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to import configuration.", e); // @highlight type="italic"
            }
            // @end region="importConfig"
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void exportConfigDemo() {
        try {
            // @start region="exportConfig"
            // How to export config JSON from a config handle
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzConfig instance
                SzConfig config = env.getConfig();
            
                // get a valid config handle
                long configHandle = config.createConfig(); // @highlight type="italic" regex="config.createConfig.."

                // export the config
                try {
                    String configDefinition = config.exportConfig(configHandle); // @highlight regex="String.*"

                    if (configDefinition == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."

                } finally {
                    // close the config handle
                    config.closeConfig(configHandle);
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to export configuration.", e); // @highlight type="italic"
            }
            // @end region="exportConfig"
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void closeConfigDemo() {
        try {
            // @start region="closeConfig"
            // How to close a previously obtained config handle.
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzConfig instance
                SzConfig config = env.getConfig();
            
                // obtain a config handle in some way
                long configHandle = config.createConfig(); // @highlight type="italic" regex="config.createConfig.."

                // do something with the config handle
                try {
                    if (configHandle == 0) { throw new Exception(); }// @replace regex="if.*;" replacement="..."

                } finally {
                    // close the config handle
                    config.closeConfig(configHandle); // @highlight type="bold" regex="config.*;"
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed config operation.", e); // @highlight type="italic"
            }
            // @end region="closeConfig"
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getDataSourcesDemo() {
        try {
            // @start region="getDataSources"
            // How to get the data sources from an in-memory config
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzConfig instance
                SzConfig config = env.getConfig();
            
                // get a valid config handle
                long configHandle = config.createConfig(); // @highlight type="italic" regex="config.createConfig.."

                // get the data sources
                try {
                    String sourcesJson = config.getDataSources(configHandle); // @highlight regex="String.*"

                    // do something with the returned JSON (e.g.: parse it and extract values)
                    JsonObject jsonObj = Json.createReader(
                        new StringReader(sourcesJson)).readObject();            // @highlight regex="sourcesJson"
                    
                    JsonArray jsonArr = jsonObj.getJsonArray("DATA_SOURCES");   // @highlight regex=".DATA_SOURCES."

                    // iterate over the data sources
                    for (JsonObject sourceObj : jsonArr.getValuesAs(JsonObject.class)) {
                        String dataSourceCode = sourceObj.getString("DSRC_CODE");   // @highlight regex=".DSRC_CODE."

                        if (dataSourceCode == null) { throw new Exception(); } // @replace regex="if.*" replacement="..."
                    }

                } finally {
                    // close the config handle
                    config.closeConfig(configHandle);   // @highlight type="bold" regex="config.*;"
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to get data sources.", e); // @highlight type="italic"
            }
            // @end region="getDataSources"
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void addDataSourceDemo() {
        try {
            // @start region="addDataSource"
            // How to add data sources to an in-memory config
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzConfig instance
                SzConfig config = env.getConfig();
            
                // get a valid config handle
                long configHandle = config.createConfig(); // @highlight type="italic" regex="config.createConfig.."

                try {
                    // add data sources to the config
                    config.addDataSource(configHandle, "CUSTOMERS"); // @highlight regex="config.*"
                    
                    config.addDataSource(configHandle, "EMPLOYEES"); // @highlight regex="config.*"
                    
                    config.addDataSource(configHandle, "WATCHLIST"); // @highlight regex="config.*"

                } finally {
                    // close the config handle
                    config.closeConfig(configHandle);   // @highlight type="bold" regex="config.*;"
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to add data sources.", e); // @highlight type="italic"
            }
            // @end region="addDataSource"

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void deleteDataSourceDemo() {
        try {
            // @start region="deleteDataSource"
            // How to delete a data source from an in-memory config
            try {
                // obtain the SzEnvironment (varies by application)
                // @link region="env" regex="SzEnvironment" target="SzEnvironment"
                SzEnvironment env = getEnvironment(); // @highlight type="italic" substring="getEnvironment()"
                // @end region="env"

                // get the SzConfig instance
                SzConfig config = env.getConfig();
            
                // get a valid config handle
                long configHandle = config.createConfig(); // @highlight type="italic" regex="config.createConfig.."

                try {
                    // delete the data soure from the config
                    config.deleteDataSource(configHandle, "CUSTOMERS"); // @highlight regex="config.*"
                    
                } finally {
                    // close the config handle
                    config.closeConfig(configHandle);   // @highlight type="bold" regex="config.*;"
                }
                
            } catch (SzException e) {
                // handle or rethrow the exception
                logError("Failed to delete data source.", e); // @highlight type="italic"
            }
            // @end region="deleteDataSource"
            
        } catch (Exception e) {
            fail(e);
        }
    }
}
