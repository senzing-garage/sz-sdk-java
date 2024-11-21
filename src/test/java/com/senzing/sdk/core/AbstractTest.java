package com.senzing.sdk.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.senzing.util.JsonUtilities;

import static com.senzing.io.IOUtilities.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 
 */
public abstract class AbstractTest {
    /**
     * The number of tests that failed for this instance.
     */
    private int failureCount = 0;

    /**
     * The number of tests that succeeded for this instance.
     */
    private int successCount = 0;

    /**
     * The time of the last progress log.
     */
    private long progressLogTimestamp = -1L;

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
    protected AbstractTest() {
        this(null);
    }

    /**
     * Protected constructor allowing the derived class to specify the
     * location for the entity respository.
     *
     * @param repoDirectory The directory in which to include the entity
     *                      repository.
     */
    protected AbstractTest(File repoDirectory) {
        if (repoDirectory == null) {
            repoDirectory = createTestRepoDirectory(this.getClass());
        }
        this.repoDirectory = repoDirectory;
    }

    /**
     * Signals the beginning of the current test suite.
     *
     * @return <tt>true</tt> if replaying previous results and <tt>false</tt>
     *         if using the live API.
     */
    protected void beginTests() {
        // do nothing for now
    }

    /**
     * Signals the end of the current test suite.
     */
    protected void endTests() {
        this.conditionallyLogCounts(true);
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
        int returnCode = configMgr.addConfig(config, comment, result);
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
        int returnCode = config.addDataSource(configHandle, json, sb);
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
        int returnCode = config.save(configHandle, sb);
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

    /**
     * Increments the failure count and returns the new failure count.
     * 
     * @return The new failure count.
     */
    protected int incrementFailureCount() {
        this.failureCount++;
        this.conditionallyLogCounts(false);
        return this.failureCount;
    }

    /**
     * Increments the success count and returns the new success count.
     * 
     * @return The new success count.
     */
    protected int incrementSuccessCount() {
        this.successCount++;
        this.conditionallyLogCounts(false);
        return this.successCount;
    }

    /**
     * Conditionally logs the progress of the tests.
     *
     * @param complete <tt>true</tt> if tests are complete for this class,
     *                 otherwise <tt>false</tt>.
     */
    protected void conditionallyLogCounts(boolean complete) {
        int successCount = this.getSuccessCount();
        int failureCount = this.getFailureCount();

        long now = System.currentTimeMillis();
        long lapse = (this.progressLogTimestamp > 0L)
                ? (now - this.progressLogTimestamp)
                : 0L;

        if (complete || (lapse > 30000L)) {
            System.out.println(this.getClass().getSimpleName()
                    + (complete ? " Complete: " : " Progress: ")
                    + successCount + " (succeeded) / " + failureCount
                    + " (failed)");
            this.progressLogTimestamp = now;
        }
        if (complete) {
            System.out.println();
        }
        if (this.progressLogTimestamp < 0L) {
            this.progressLogTimestamp = now;
        }
    }

    /**
     * Returns the current failure count. The failure count is incremented via
     * {@link #incrementFailureCount()}.
     *
     * @return The current success count.
     */
    protected int getFailureCount() {
        return this.failureCount;
    }

    /**
     * Returns the current success count. The success count is incremented via
     * {@link #incrementSuccessCount()}.
     *
     * @return The current success count.
     */
    protected int getSuccessCount() {
        return this.successCount;
    }

    /**
     * Wrapper function for performing a test that will first check if
     * the native API is available via {@link #assumeNativeApiAvailable()} and
     * then record a success or failure.
     *
     * @param testFunction The {@link Runnable} to execute.
     */
    protected void performTest(Runnable testFunction) {
        boolean success = false;
        try {
            testFunction.run();
            success = true;

        } catch (Error | RuntimeException e) {
            e.printStackTrace();
            System.err.flush();
            if ("true".equals(System.getProperty("com.senzing.api.test.fastFail"))) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException ignore) {
                    // do nothing
                }
                System.exit(1);
            }
            throw e;
        } finally {
            if (!success)
                this.incrementFailureCount();
            else
                this.incrementSuccessCount();
        }
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that no unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMap(Object jsonData, String... expectedKeys) {
        validateJsonDataMap(null,
                jsonData,
                true,
                expectedKeys);
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that no unexpected keys are present.
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMap(String testInfo,
            Object jsonData,
            String... expectedKeys) {
        validateJsonDataMap(testInfo, jsonData, true, expectedKeys);
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that, optionally, no unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys -- these are
     *                     either a minimum or exact set depending on the
     *                     <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMap(Object jsonData,
            boolean strict,
            String... expectedKeys) {
        validateJsonDataMap(null, jsonData, strict, expectedKeys);
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that, optionally, no unexpected keys are present.
     *
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys -- these are
     *                     either a minimum or exact set depending on the
     *                     <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMap(String testInfo,
            Object jsonData,
            boolean strict,
            String... expectedKeys) {
        String suffix = (testInfo != null && testInfo.trim().length() > 0)
                ? " ( " + testInfo + " )"
                : "";

        if (jsonData == null) {
            fail("Expected json data but got null value" + suffix);
        }

        if (!(jsonData instanceof Map)) {
            fail("Raw data is not a JSON object: " + jsonData + suffix);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) jsonData;
        Set<String> expectedKeySet = new HashSet<>();
        Set<String> actualKeySet = map.keySet();
        for (String key : expectedKeys) {
            expectedKeySet.add(key);
            if (!actualKeySet.contains(key)) {
                fail("JSON property missing from json data: " + key + " / " + map
                        + suffix);
            }
        }
        if (strict && expectedKeySet.size() != actualKeySet.size()) {
            Set<String> extraKeySet = new HashSet<>(actualKeySet);
            extraKeySet.removeAll(expectedKeySet);
            fail("Unexpected JSON properties in json data: " + extraKeySet + suffix);
        }

    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that no
     * unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMapArray(Object jsonData,
            String... expectedKeys) {
        validateJsonDataMapArray(null, jsonData, true, expectedKeys);
    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that no
     * unexpected keys are present.
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMapArray(String testInfo,
            Object jsonData,
            String... expectedKeys) {
        validateJsonDataMapArray(testInfo, jsonData, true, expectedKeys);
    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that,
     * optionally, no unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys for the array
     *                     objects -- these are either a minimum or exact set
     *                     depending on the <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMapArray(Object jsonData,
            boolean strict,
            String... expectedKeys) {
        validateJsonDataMapArray(null, jsonData, strict, expectedKeys);
    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that,
     * optionally, no unexpected keys are present.
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys for the array
     *                     objects -- these are either a minimum or exact set
     *                     depending on the <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMapArray(String testInfo,
            Object jsonData,
            boolean strict,
            String... expectedKeys) {
        String suffix = (testInfo != null && testInfo.trim().length() > 0)
                ? " ( " + testInfo + " )"
                : "";

        if (jsonData == null) {
            fail("Expected json data but got null value" + suffix);
        }

        if (!(jsonData instanceof Collection)) {
            fail("Raw data is not a JSON array: " + jsonData + suffix);
        }

        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) jsonData;
        Set<String> expectedKeySet = new HashSet<>();
        for (String key : expectedKeys) {
            expectedKeySet.add(key);
        }

        for (Object obj : collection) {
            if (!(obj instanceof Map)) {
                fail("Raw data is not a JSON array of JSON objects: " + jsonData + suffix);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;

            Set<String> actualKeySet = map.keySet();
            for (String key : expectedKeySet) {
                if (!actualKeySet.contains(key)) {
                    fail("JSON property missing from json data array element: "
                            + key + " / " + map + suffix);
                }
            }
            if (strict && expectedKeySet.size() != actualKeySet.size()) {
                Set<String> extraKeySet = new HashSet<>(actualKeySet);
                extraKeySet.removeAll(expectedKeySet);
                fail("Unexpected JSON properties in json data: " + extraKeySet + suffix);
            }
        }
    }

    /**
     * Generates option combos for the specified variants.
     *
     * @param variants
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static List<List<?>> generateCombinations(List<?>... variants) {
        // determine the total number of combinations
        int comboCount = 1;
        for (List<?> v : variants) {
            comboCount *= v.size();
        }

        // determine the intervals for each variant
        List<Integer> intervals = new ArrayList<>(variants.length);
        // iterate over the variants
        for (int index = 0; index < variants.length; index++) {
            // default the interval count to one (1)
            int intervalCount = 1;

            // loop over the remaining variants after the current
            for (int index2 = index + 1; index2 < variants.length; index2++) {
                // multiply the interval count by the remaining variant sizes
                intervalCount *= variants[index2].size();
            }

            // add the interval count
            intervals.add(intervalCount);
        }

        ArrayList<List<?>> optionCombos = new ArrayList<>(comboCount);
        for (int comboIndex = 0; comboIndex < comboCount; comboIndex++) {
            List optionCombo = new ArrayList(variants.length);

            for (int index = 0; index < variants.length; index++) {
                int interval = intervals.get(index);
                int valueIndex = (comboIndex / interval) % variants[index].size();
                optionCombo.add(variants[index].get(valueIndex));
            }

            optionCombos.add(optionCombo);
        }

        return optionCombos;
    }

    /**
     * Gets the variant possible values of booleans for the specified
     * parameter count. This includes <tt>null</tt> values.
     *
     * @param paramCount The number of boolean parameters.
     * @param includeNull Whether or not to include null values.
     * @return The {@link List} of parameter value lists.
     */
    protected static List<List<Boolean>> getBooleanVariants(
        int         paramCount,
        boolean     includeNull) 
    {
        Boolean[] boolsSansNull = { true, false };
        Boolean[] boolsWithNull = { null, true, false };

        Boolean[] booleanValues = (includeNull) ? boolsWithNull : boolsSansNull;
        
    
        int variantCount = (int) Math.ceil(Math.pow(booleanValues.length, paramCount));
        List<List<Boolean>> variants = new ArrayList<>(variantCount);

        // iterate over the variants
        for (int index1 = 0; index1 < variantCount; index1++) {
            // create the parameter list
            List<Boolean> params = new ArrayList<>(paramCount);

            // iterate over the parameter slots
            for (int index2 = 0; index2 < paramCount; index2++) {
                int repeat = (int) Math.ceil(Math.pow(booleanValues.length, index2));
                int valueIndex = (index1 / repeat) % booleanValues.length;
                params.add(booleanValues[valueIndex]);
            }

            // add the combinatorial variant
            variants.add(params);
        }
        return variants;
    }

    /**
     * Utility class for circular iteration.
     */
    private static class CircularIterator<T> implements Iterator<T> {
        private Collection<T> collection = null;
        private Iterator<T> iterator = null;

        private CircularIterator(Collection<T> collection) {
            this.collection = collection;
            this.iterator = this.collection.iterator();
        }

        public boolean hasNext() {
            return (this.collection.size() > 0);
        }

        public T next() {
            if (!this.iterator.hasNext()) {
                this.iterator = this.collection.iterator();
            }
            return this.iterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "Cannot remove from a circular iterator.");
        }
    }

    /**
     * Returns an iterator that iterates over the specified {@link Collection}
     * in a circular fashion.
     *
     * @param collection The {@link Collection} to iterate over.
     * @return The circular {@link Iterator}.
     */
    protected static <T> Iterator<T> circularIterator(Collection<T> collection) {
        return new CircularIterator<>(collection);
    }

  /**
   * Creates a CSV temp file with the specified headers and records.
   *
   * @param filePrefix The prefix for the temp file name.
   * @param headers    The CSV headers.
   * @param records    The one or more records.
   * @return The {@link File} that was created.
   */
  protected File prepareCSVFile(String filePrefix,
                                String[] headers,
                                String[]... records) {
    // check the arguments
    int count = headers.length;
    for (int index = 0; index < records.length; index++) {
      String[] record = records[index];
      if (record.length != count) {
        throw new IllegalArgumentException(
            "The header and records do not all have the same number of "
                + "elements.  expected=[ " + count + " ], received=[ "
                + record.length + " ], index=[ " + index + " ]");
      }
    }

    try {
      File csvFile = File.createTempFile(filePrefix, ".csv");

      // populate the file as a CSV
      try (FileOutputStream fos = new FileOutputStream(csvFile);
           OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
           PrintWriter pw = new PrintWriter(osw)) {
        String prefix = "";
        for (String header : headers) {
          pw.print(prefix);
          pw.print(csvQuote(header));
          prefix = ",";
        }
        pw.println();
        pw.flush();

        for (String[] record : records) {
          prefix = "";
          for (String value : record) {
            pw.print(prefix);
            pw.print(csvQuote(value));
            prefix = ",";
          }
          pw.println();
          pw.flush();
        }
        pw.flush();

      }

      return csvFile;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Creates a JSON array temp file with the specified headers and records.
   *
   * @param filePrefix The prefix for the temp file name.
   * @param headers    The CSV headers.
   * @param records    The one or more records.
   * @return The {@link File} that was created.
   */
  protected File prepareJsonArrayFile(String filePrefix,
                                      String[] headers,
                                      String[]... records) {
    // check the arguments
    int count = headers.length;
    for (int index = 0; index < records.length; index++) {
      String[] record = records[index];
      if (record.length != count) {
        throw new IllegalArgumentException(
            "The header and records do not all have the same number of "
                + "elements.  expected=[ " + count + " ], received=[ "
                + record.length + " ], index=[ " + index + " ]");
      }
    }

    try {
      File jsonFile = File.createTempFile(filePrefix, ".json");

      // populate the file with a JSON array
      try (FileOutputStream fos = new FileOutputStream(jsonFile);
           OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8")) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (String[] record : records) {
          for (int index = 0; index < record.length; index++) {
            String key = headers[index];
            String value = record[index];
            job.add(key, value);
          }
          jab.add(job);
        }

        String jsonText = JsonUtilities.toJsonText(jab);
        osw.write(jsonText);
        osw.flush();
      }

      return jsonFile;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a JSON temp file with the specified headers and records.
   *
   * @param filePrefix The prefix for the temp file name.
   * @param headers    The CSV headers.
   * @param records    The one or more records.
   * @return The {@link File} that was created.
   */
  protected File prepareJsonFile(String filePrefix,
                                 String[] headers,
                                 String[]... records) {
    // check the arguments
    int count = headers.length;
    for (int index = 0; index < records.length; index++) {
      String[] record = records[index];
      if (record.length != count) {
        throw new IllegalArgumentException(
            "The header and records do not all have the same number of "
                + "elements.  expected=[ " + count + " ], received=[ "
                + record.length + " ], index=[ " + index + " ]");
      }
    }

    try {
      File jsonFile = File.createTempFile(filePrefix, ".json");

      // populate the file as one JSON record per line
      try (FileOutputStream fos = new FileOutputStream(jsonFile);
           OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
           PrintWriter pw = new PrintWriter(osw)) {
        for (String[] record : records) {
          JsonObjectBuilder job = Json.createObjectBuilder();
          for (int index = 0; index < record.length; index++) {
            String key = headers[index];
            String value = record[index];
            job.add(key, value);
          }
          String jsonText = JsonUtilities.toJsonText(job);
          pw.println(jsonText);
          pw.flush();
        }
        pw.flush();
      }

      return jsonFile;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a JSON-lines temp file with the specified headers and records.
   *
   * @param filePrefix The prefix for the temp file name.
   * @param jsonArray The {@link JsonArray} describing the records.
   * @return The {@link File} that was created.
   */
  protected File prepareJsonArrayFile(String filePrefix, JsonArray jsonArray) {
    try {
      File jsonFile = File.createTempFile(filePrefix, ".json");

      // populate the file as one JSON record per line
      try (FileOutputStream fos = new FileOutputStream(jsonFile);
           OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
           PrintWriter pw = new PrintWriter(osw))
      {
        String jsonText = JsonUtilities.toJsonText(jsonArray, true);
        pw.println(jsonText);
        pw.flush();
      }

      return jsonFile;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a JSON-lines temp file with the specified headers and records.
   *
   * @param filePrefix The prefix for the temp file name.
   * @param jsonArray The {@link JsonArray} describing the records.
   * @return The {@link File} that was created.
   */
  protected File prepareJsonFile(String filePrefix, JsonArray jsonArray) {
    try {
      File jsonFile = File.createTempFile(filePrefix, ".json");

      // populate the file as one JSON record per line
      try (FileOutputStream fos = new FileOutputStream(jsonFile);
           OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
           PrintWriter pw = new PrintWriter(osw)) {
        for (JsonObject record: jsonArray.getValuesAs(JsonObject.class)) {
          String jsonText = JsonUtilities.toJsonText(record);
          pw.println(jsonText);
          pw.flush();
        }
        pw.flush();
      }

      return jsonFile;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

   /**
   * Quotes the specified text as a quoted string for a CSV value or header.
   *
   * @param text The text to be quoted.
   * @return The quoted text.
   */
  protected String csvQuote(String text) {
    if (text.indexOf("\"") < 0 && text.indexOf("\\") < 0) {
      return "\"" + text + "\"";
    }
    char[] textChars = text.toCharArray();
    StringBuilder sb = new StringBuilder(text.length() * 2);
    for (char c : textChars) {
      if (c == '"' || c == '\\') {
        sb.append('\\');
      }
      sb.append(c);
    }
    return sb.toString();
  }

}
