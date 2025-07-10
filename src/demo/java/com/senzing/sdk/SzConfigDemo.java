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
public class SzConfigDemo extends AbstractCoreTest {
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
      throws Exception {
    System.err.println();
    System.err.println("**********************************");
    System.err.println("FAILURE: " + message);
    e.printStackTrace(System.err);
    System.err.println();
    throw e;
  }

  @Test
  public void createConfigFromTemplateDemo() {
    try {
      // @start region = "createConfigFromTemplate"
      // How to create an SzConfig instance representing the template configuration
      try {
        // obtain the SzEnvironment (varies by application)
        // @link region="env" regex="SzEnvironment" target="SzEnvironment"
        SzEnvironment env = getEnvironment(); // @highlight type = "italic" substring = "getEnvironment()"
        // @end region = "env"

        // get the SzConfigManager instance
        SzConfigManager configMgr = env.getConfigManager();

        // create the config from the template
        SzConfig config = configMgr.createConfig(); // @highlight type = "bold" regex = "SzConfig.*"

        // do something with the SzConfig
        if (config == null) {
          throw new Exception();
        } // @replace regex = "if.*" replacement = "..."

      } catch (SzException e) {
        // handle or rethrow the exception
        logError("Failed to create new SzConfig from the template.", e); // @highlight type = "italic"
      }
      // @end region = "createConfigFromTemplate"
    } catch (Exception e) {
      fail(e);
    }
  }

  /**
   * Simulates reading a config file from disk.
   * 
   * @return The config definition JSON.
   */
  public String readConfigFile() throws SzException {
    SzEnvironment env = getEnvironment();
    SzConfigManager configMgr = env.getConfigManager();
    SzConfig config = configMgr.createConfig();
    return config.export();
  }

  @Test
  public void createConfigFromDefinitionDemo() {
    try {
      // @start region = "createConfigFromDefinition"
      // How to create an SzConfig instance representing a specified config definition
      try {
        // obtain the SzEnvironment (varies by application)
        // @link region="env" regex="SzEnvironment" target="SzEnvironment"
        SzEnvironment env = getEnvironment(); // @highlight type = "italic" substring = "getEnvironment()"
        // @end region = "env"

        // get the SzConfigManager instance
        SzConfigManager configMgr = env.getConfigManager();

        // obtain a JSON config definition (varies by application)
        String configDefinition = readConfigFile(); // @highlight type = "italic" regex = "readConfigFile.."

        // create the config using the config definition
        SzConfig config = configMgr.createConfig(configDefinition); // @highlight type = "bold" regex = "SzConfig.*;"

        // do something with the SzConfig
        if (config == null) {
          throw new Exception();
        } // @replace regex = "if.*" replacement = "..."

      } catch (SzException e) {
        // handle or rethrow the exception
        logError("Failed to create a new SzConfig from a definition.", e); // @highlight type = "italic"
      }
      // @end region = "createConfigFromDefinition"
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void exportConfigDemo() {
    try {
      // @start region = "exportConfig"
      // How to export config JSON from a config handle
      try {
        // obtain the SzEnvironment (varies by application)
        // @link region="env" regex="SzEnvironment" target="SzEnvironment"
        SzEnvironment env = getEnvironment(); // @highlight type = "italic" substring = "getEnvironment()"
        // @end region = "env"

        // get the SzConfigManager instance
        SzConfigManager configMgr = env.getConfigManager();

        // get an SzConfig object (varies by application)
        SzConfig config = configMgr.createConfig(); // @highlight type = "italic" regex = "configMgr.createConfig.."

        // export the config
        String configDefinition = config.export(); // @highlight regex = "String.*"

        if (configDefinition == null) {
          throw new Exception();
        } // @replace regex = "if.*" replacement = "..."

      } catch (SzException e) {
        // handle or rethrow the exception
        logError("Failed to export configuration.", e); // @highlight type = "italic"
      }
      // @end region = "exportConfig"
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void getDataSourceRegistryDemo() {
    try {
      // @start region = "getDataSourceRegistry"
      // How to get the data source registry from an in-memory config
      try {
        // obtain the SzEnvironment (varies by application)
        // @link region="env" regex="SzEnvironment" target="SzEnvironment"
        SzEnvironment env = getEnvironment(); // @highlight type = "italic" substring = "getEnvironment()"
        // @end region = "env"

        // get the SzConfigManager instance
        SzConfigManager configMgr = env.getConfigManager();

        // get an SzConfig object (varies by application)
        SzConfig config = configMgr.createConfig(); // @highlight type = "italic" regex = "configMgr.createConfig.."

        // get the data source registry
        String sourcesJson = config.getDataSourceRegistry(); // @highlight regex = "String.*"

        // do something with the returned JSON (e.g.: parse it and extract values)
        // @highlight type="italic" region="doSomething"
        JsonObject jsonObj = Json.createReader(
            new StringReader(sourcesJson)).readObject(); // @highlight regex = "sourcesJson"

        JsonArray jsonArr = jsonObj.getJsonArray("DATA_SOURCES"); // @highlight regex = ".DATA_SOURCES."

        // iterate over the data sources
        for (JsonObject sourceObj : jsonArr.getValuesAs(JsonObject.class)) {
          String dataSourceCode = sourceObj.getString("DSRC_CODE"); // @highlight regex = ".DSRC_CODE."

          if (dataSourceCode == null) {
            throw new Exception();
          } // @replace regex = "if.*" replacement = "..."
        }
        // @end region = "doSomething"

      } catch (SzException e) {
        // handle or rethrow the exception
        logError("Failed to get data source registry.", e); // @highlight type = "italic"
      }
      // @end region = "getDataSourceRegistry"
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void registerDataSourceDemo() {
    try {
      // @start region = "registerDataSource"
      // How to register data sources to an in-memory config
      try {
        // obtain the SzEnvironment (varies by application)
        // @link region="env" regex="SzEnvironment" target="SzEnvironment"
        SzEnvironment env = getEnvironment(); // @highlight type = "italic" substring = "getEnvironment()"
        // @end region = "env"

        // get the SzConfigManager instance
        SzConfigManager configMgr = env.getConfigManager();

        // get an SzConfig object (varies by application)
        SzConfig config = configMgr.createConfig(); // @highlight type = "italic" regex = "configMgr.createConfig.."

        // register data sources in the config
        config.registerDataSource("CUSTOMERS"); // @highlight regex = "config.*"

        config.registerDataSource("EMPLOYEES"); // @highlight regex = "config.*"

        config.registerDataSource("WATCHLIST"); // @highlight regex = "config.*"

        if (config == configMgr) {
          throw new Exception();
        } // @replace regex = "if.*" replacement = "..."

      } catch (SzException e) {
        // handle or rethrow the exception
        logError("Failed to register data sources.", e); // @highlight type = "italic"
      }
      // @end region = "registerDataSource"

    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void unregisterDataSourceDemo() {
    try {
      // @start region = "unregisterDataSource"
      // How to unregister a data source from an in-memory config
      try {
        // obtain the SzEnvironment (varies by application)
        // @link region="env" regex="SzEnvironment" target="SzEnvironment"
        SzEnvironment env = getEnvironment(); // @highlight type = "italic" substring = "getEnvironment()"
        // @end region = "env"

        // get the SzConfigManager instance
        SzConfigManager configMgr = env.getConfigManager();

        // get an SzConfig object (varies by application)
        SzConfig config = configMgr.createConfig(); // @highlight type = "italic" regex = "configMgr.createConfig.."

        // unregister the data source from the config
        config.unregisterDataSource("CUSTOMERS"); // @highlight regex = "config.*"

        if (config == configMgr) {
          throw new Exception();
        } // @replace regex = "if.*" replacement = "..."

      } catch (SzException e) {
        // handle or rethrow the exception
        logError("Failed to unregister data source.", e); // @highlight type = "italic"
      }
      // @end region = "unregisterDataSource"

    } catch (Exception e) {
      fail(e);
    }
  }
}
