package com.senzing.sdk.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.senzing.sdk.test.AbstractTest;

import static com.senzing.io.IOUtilities.*;

/**
 * 
 */
public abstract class AbstractCoreTest extends AbstractTest {
    /**
     * The class-wide repo directory.
     */
    private File repoDirectory = null;

    /**
     * Whether or not the repository has been created.
     */
    private boolean repoCreated = false;

    /**
     * Protected default constructor.
     */
    protected AbstractCoreTest() {
        this(null);
    }

    /**
     * Protected constructor allowing the derived class to specify the
     * location for the entity respository.
     *
     * @param repoDirectory The directory in which to include the entity
     *                      repository.
     */
    protected AbstractCoreTest(File repoDirectory) {
        if (repoDirectory == null) {
            repoDirectory = createTestRepoDirectory(this.getClass());
        }
        this.repoDirectory = repoDirectory;
    }

    /**
     * Initializes the specified {@link NativeConfig} with the specified parameters.
     * 
     * @param config       The {@link NativeConfig} to initialize.
     * @param instanceName The instance name to use for initialization.
     * @param settings     The settings to use for initialization.
     */
    protected void init(NativeConfig config, String instanceName, String settings) {
        int returnCode = config.init(instanceName, settings, false);
        if (returnCode != 0) {
            throw new RuntimeException(config.getLastException());
        }
    }

    /**
     * Initializes the specified {@link NativeConfigManager} with the specified
     * parameters.
     * 
     * @param config       The {@link NativeConfigManager} to initialize.
     * @param instanceName The instance name to use for initialization.
     * @param settings     The settings to use for initialization.
     */
    protected void init(NativeConfigManager configMgr, String instanceName, String settings) {
        int returnCode = configMgr.init(instanceName, settings, false);
        if (returnCode != 0) {
            throw new RuntimeException(configMgr.getLastException());
        }
    }

    /**
     * Destroys the specified {@link NativeConfig} instance.
     * 
     * @param config The {@link NativeConfig} instance to destroy.
     * 
     * @return Always returns <code>null</code>.
     */
    protected NativeConfig destroy(NativeConfig config) {
        if (config == null)
            return null;
        config.destroy();
        return null;
    }

    /**
     * Destroys the specified {@link NativeConfigManager} instance.
     * 
     * @param config The {@link NativeConfigManager} instance to destroy.
     * 
     * @return Always returns <code>null</code>.
     */
    protected NativeConfigManager destroy(NativeConfigManager configMgr) {
        if (configMgr == null)
            return null;
        configMgr.destroy();
        return null;
    }

    /**
     * Creates a default config and returns the config handle.
     * 
     * @param config The {@link NativeConfig} instance to use.
     * 
     * @return The config handle for the config.
     */
    protected long createDefaultConfig(NativeConfig config) {
        // create the default config
        Result<Long> result = new Result<>();
        int returnCode = config.create(result);
        if (returnCode != 0) {
            throw new RuntimeException(config.getLastException());
        }
        return result.getValue();
    }

    /**
     * Adds the specified config to the repository using the specified
     * {@link NativeConfigManager}.
     * 
     * @param configMgr The {@link NativeConfigManager} to use.
     * 
     * @param config    The {@link String} config to be added.
     * 
     * @param comment   The {@link String} comment to associated with the config.
     * 
     * @return The configuration ID for the added config.
     */
    protected long addConfig(NativeConfigManager configMgr, String config, String comment) {
        Result<Long> result = new Result<>();
        int returnCode = configMgr.registerConfig(config, comment, result);
        if (returnCode != 0) {
            throw new RuntimeException(configMgr.getLastException());
        }
        return result.getValue();
    }

    /**
     * Gets the default config ID using the specified {@link NativeConfigManager}.
     * 
     * @param configMgr The {@link NativeConfigManager} to use
     * 
     * @return The configuration ID for the default config ID.
     */
    protected long getDefaultConfigId(NativeConfigManager configMgr) {
        Result<Long> result = new Result<>();
        int returnCode = configMgr.getDefaultConfigID(result);
        if (returnCode != 0) {
            throw new RuntimeException(configMgr.getLastException());
        }
        return result.getValue();
    }

    /**
     * Closes the config associated with the specified config handle.
     * This does nothing if the specified {@link NativeConfig} is
     * <code>null</code> <b>or</b> the specified config handle is
     * zero (0).
     * 
     * @param config       The {@link NativeConfig} instance to use.
     * @param configHandle The config handle to close.
     * 
     * @return Always returns zero (0).
     */
    protected long closeConfig(NativeConfig config, long configHandle) {
        if (config == null || configHandle == 0l)
            return 0L;
        config.close(configHandle);
        return 0L;
    }

    /**
     * Adds a data source to the config associated with the specified
     * config handle.
     * 
     * @param config       The {@link NativeConfig} to use.
     * 
     * @param configHandle The config handle associated with the config
     *                     to be modified.
     * 
     * @param dataSource   The data source to add to the config.
     */
    protected void addDataSource(NativeConfig config,
            long configHandle,
            String dataSource) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("DSRC_CODE", dataSource);
        String json = job.build().toString();

        StringBuffer sb = new StringBuffer();
        int returnCode = config.registerDataSource(configHandle, json, sb);
        if (returnCode != 0) {
            throw new RuntimeException(config.getLastException());
        }
    }

    /**
     * Exports the config associated with the specified config handle as
     * a {@link String}.
     * 
     * @param config       The {@link NativeConfig} to use.
     * 
     * @param configHandle The config handle associated with the config
     *                     to be exported.
     * 
     * @return The JSON {@link String} for the config.
     */

    protected String exportConfig(NativeConfig config, long configHandle) {
        StringBuffer sb = new StringBuffer();
        int returnCode = config.export(configHandle, sb);
        if (returnCode != 0) {
            throw new RuntimeException(config.getLastException());
        }
        return sb.toString();
    }

    /**
     * Creates a new default config and adds the specified zero or more
     * data sources to it and then returns the JSON {@link String} for that
     * config.
     * 
     * @param config      The {@link NativeConfig} to use.
     * 
     * @param dataSources The zero or more data sources to add to the config.
     * 
     * @return The JSON {@link String} that for the created config.
     */
    protected String createConfig(NativeConfig config, String... dataSources) {
        String result = null;
        long configHandle = createDefaultConfig(config);
        try {
            for (String dataSource : dataSources) {
                addDataSource(config, configHandle, dataSource);
            }
            result = exportConfig(config, configHandle);
        } finally {
            configHandle = closeConfig(config, configHandle);
        }
        return result;
    }

    /**
     * Returns the instance name with which to initialize the server. This
     * returns the simple class name of the test class. Override to use a
     * different instance name.
     *
     * @return The instance name with which to initialize the API.
     */
    protected String getInstanceName() {
        return this.getInstanceName(null);
    }

    /**
     * Returns the instance name with which to initialize the server. This
     * returns the simple class name of the test class optionally
     * concatenated with a specified non-null suffix. Override to use a
     * different instance name.
     *
     * @param suffix The optional suffix to append to the instance name.
     *
     * @return The instance name with which to initialize the API.
     */
    protected String getInstanceName(String suffix) {
        if (suffix != null && suffix.trim().length() > 0) {
            suffix = " - " + suffix.trim();
        } else {
            suffix = "";
        }
        return this.getClass().getSimpleName() + suffix;
    }

    /**
     * Returns the contents of the JSON init file from the default
     * repository directory.
     * 
     * @return The contents of the JSON init file as a {@link String}
     */
    protected String getRepoSettings() {
        return this.readRepoSettingsFile(this.getRepositoryDirectory());
    }

    /**
     * Returns the contents of the JSON init file as a {@link String}.
     *
     * @param repoDirectory The {@link File} representing the directory
     *                      of the test repository.
     * 
     * @return The contents of the JSON init file as a {@link String}.
     */
    protected String readRepoSettingsFile(File repoDirectory) {
        try {
            File initJsonFile = new File(repoDirectory, "sz-init.json");

            return readTextFileAsString(initJsonFile, "UTF-8").trim();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a temp repository directory for the test class.
     *
     * @return The {@link File} representing the directory.
     */
    protected File createTestRepoDirectory() {
        return createTestRepoDirectory(this.getClass(), null);
    }

    /**
     * Creates a temp repository directory for a specific test.
     *
     * @param testName The name of the test that the directory will be used for.
     *
     * @return The {@link File} representing the directory.
     */
    protected File createTestRepoDirectory(String testName) {
        return createTestRepoDirectory(this.getClass(), testName);
    }

    /**
     * Creates a temp repository directory for a specific test.
     *
     * @param c The {@link Class} for which the directory is being created.
     *
     * @return The {@link File} representing the directory.
     */
    protected static File createTestRepoDirectory(Class<?> c) {
        return createTestRepoDirectory(c, null);
    }

    /**
     * Creates a temp repository directory for a specific test.
     *
     * @param c        The {@link Class} for which the directory is being created.
     * 
     * @param testName The name of the test that the directory will be used for.
     *
     * @return The {@link File} representing the directory.
     */
    protected static File createTestRepoDirectory(Class<?> c, String testName) {
        String prefix = "sz-repo-" + c.getSimpleName() + "-"
                + (testName == null ? "" : (testName + "-"));
        return doCreateTestRepoDirectory(prefix);
    }

    /**
     * Creates a temp repository directory.
     *
     * @param prefix The directory name prefix for the temp repo directory.
     *
     * @return The {@link File} representing the directory.
     */
    protected static File doCreateTestRepoDirectory(String prefix) {
        try {
            File targetDir = null;
            String buildDirProp = System.getProperty("project.build.directory");
            if (buildDirProp != null) {
                targetDir = new File(buildDirProp);
            } else {
                String workingDir = System.getProperty("user.dir");
                File currentDir = new File(workingDir);
                targetDir = new File(currentDir, "target");
            }

            boolean forceTempRepos = false;
            String prop = System.getProperty("senzing.test.tmp.repos");
            if (prop != null && prop.toLowerCase().trim().equals("true")) {
                forceTempRepos = true;
            }

            // check if we have a target directory (i.e.: maven build)
            if (forceTempRepos || !targetDir.exists()) {
                // if no target directory then use the temp directory
                return Files.createTempDirectory(prefix).toFile();
            }

            // if we have a target directory then use it as a parent for our test repo
            File testRepoDir = new File(targetDir, "test-repos");
            if (!testRepoDir.exists()) {
                testRepoDir.mkdirs();
            }

            // create a temp directory inside the test repo directory
            return Files.createTempDirectory(testRepoDir.toPath(), prefix).toFile();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the {@link File} identifying the repository directory used for
     * the test. This can be specified in the constructor, but if not specified
     * is a newly created temporary directory.
     * 
     * @return The {@link File} identifying the repository directory used for
     *         the test.
     */
    protected File getRepositoryDirectory() {
        return this.repoDirectory;
    }

    /**
     * Checks if the test should use the hybrid database setup for test
     * repository to increase performance.
     * 
     * The default implementation returns <code>true</code>.
     * 
     * @return <code>true</code> if using the hybird database setup for the 
     *         test repository, or <code>false</code> if not using the standard
     *         database setup.
     */
    protected boolean isUsingHybridDatabase() {
        return true;
    }

    /**
     * This method can typically be called from a method annotated with
     * <code>"@BeforeAll"</code>. It will create and initialize the
     * Senzing entity repository.
     */
    protected void initializeTestEnvironment() {
        this.initializeTestEnvironment(false);
    }

    /**
     * This method can typically be called from a method annotated with
     * <code>"@BeforeAll"</code>. It will create a Senzing entity
     * repository and initialize and start the Senzing API Server.
     * 
     * @param excludeConfig <code>true</code> if the default configuration
     *                      should be excluded from the repository, and
     *                      <code>false</code> if it should be included.
     */
    protected void initializeTestEnvironment(boolean excludeConfig) {
        String moduleName = this.getInstanceName("RepoMgr (create)");
        RepositoryManager.setThreadModuleName(moduleName);
        boolean concluded = false;
        try {
            RepositoryManager.createRepo(this.getRepositoryDirectory(),
                                         this.isUsingHybridDatabase(),
                                         excludeConfig,
                                         true);
            
            this.repoCreated = true;

            this.prepareRepository();

            RepositoryManager.conclude();
            concluded = true;

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Error e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!concluded)
                RepositoryManager.conclude();
            RepositoryManager.clearThreadModuleName();
        }
    }

    /**
     * This method can typically be called from a method annotated with
     * "@AfterAll". It will delete the entity repository that was created
     * for the tests if there are no test failures recorded via
     * {@link #incrementFailureCount()}.
     */
    protected void teardownTestEnvironment() {
        int failureCount = this.getFailureCount();
        teardownTestEnvironment((failureCount == 0));
    }

    /**
     * This method can typically be called from a method annotated with
     * "@AfterAll". It will optionally delete the entity repository that
     * was created for the tests.
     *
     * @param deleteRepository <tt>true</tt> if the test repository should be
     *                         deleted, otherwise <tt>false</tt>
     */
    protected void teardownTestEnvironment(boolean deleteRepository) {
        String preserveProp = System.getProperty("senzing.test.preserve.repos");
        if (preserveProp != null)
            preserveProp = preserveProp.trim().toLowerCase();
        boolean preserve = (preserveProp != null && preserveProp.equals("true"));

        // cleanup the repo directory
        if (this.repoCreated && deleteRepository && !preserve) {
            deleteRepository(this.repoDirectory);
        }
    }

    /**
     * Deletes the repository at the specified repository directory.
     * 
     * @param repoDirectory The repository directory to delete.
     */
    protected static void deleteRepository(File repoDirectory) {
        if (repoDirectory.exists() && repoDirectory.isDirectory()) {
            try {
                // delete the repository
                Files.walk(repoDirectory.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Override this function to prepare the repository by configuring
     * data sources or loading records. By default this function does nothing.
     * The repository directory can be obtained via {@link
     * #getRepositoryDirectory()}.
     */
    protected void prepareRepository() {
        // do nothing
    }
}
