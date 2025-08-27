package com.senzing.sdk.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.io.IOUtilities;
import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzConfigRetryable;
import com.senzing.sdk.SzDiagnostic;
import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzEntityIds;
import com.senzing.sdk.SzEnvironment;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzProduct;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzRecordKeys;
import com.senzing.sdk.test.SzRecord;
import com.senzing.sdk.test.SzRecord.SzFullName;
import com.senzing.sdk.test.SzRecord.SzSocialSecurity;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;

import static com.senzing.io.IOUtilities.UTF_8;
import static com.senzing.sdk.SzFlag.SZ_ADD_RECORD_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_DELETE_RECORD_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_ENTITY_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_EXPORT_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES;
import static com.senzing.sdk.SzFlag.SZ_FIND_NETWORK_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_FIND_PATH_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_HOW_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_RECORD_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_RECORD_PREVIEW_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_REDO_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_REEVALUATE_ENTITY_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_REEVALUATE_RECORD_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_SEARCH_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_VIRTUAL_ENTITY_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_WHY_ENTITIES_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_WHY_RECORDS_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_WHY_RECORD_IN_ENTITY_ALL_FLAGS;
import static com.senzing.sdk.SzFlag.SZ_WHY_SEARCH_ALL_FLAGS;
import static com.senzing.sdk.test.SdkTest.circularIterator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzConfigRetryableTest extends AbstractCoreTest 
{
    /**
     * An empty object array.
     */
    private static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * A {@link Getter} that returns an empty array.
     */
    private static final Getter<Object[]> EMPTY_GETTER
        = (test, pre) -> EMPTY_ARRAY;

    /**
     * The data source code for the customers data source.
     */
    public static final String CUSTOMERS = "CUSTOMERS";

    /**
     * The data source code for the employees data source.
     */
    public static final String EMPLOYEES = "EMPLOYEES";

    /**
     * The data source code for the employees data source.
     */
    public static final String PASSENGERS = "PASSENGERS";

    /**
     * The {@link SzRecordKey} for customer ABC123.
     */
    public static final SzRecordKey CUSTOMER_ABC123
        = SzRecordKey.of(CUSTOMERS, "ABC123");
    
    /**
     * The {@link SzRecordKey} for customer DEF456.
     */
    public static final SzRecordKey CUSTOMER_DEF456
        = SzRecordKey.of(CUSTOMERS, "DEF456");

    /**
     * The {@link SzRecordKey} for employee ABC123.
     */
    public static final SzRecordKey EMPLOYEE_ABC123
        = SzRecordKey.of(EMPLOYEES, "ABC123");
    
    /**
     * The {@link SzRecordKey} for employee DEF456.
     */
    public static final SzRecordKey EMPLOYEE_DEF456
        = SzRecordKey.of(EMPLOYEES, "DEF456");
    
    /**
     * The {@link SzRecordKey} for passenger ABC123.
     */
    public static final SzRecordKey PASSENGER_ABC123
        = SzRecordKey.of(PASSENGERS, "ABC123");
    
    /**
     * The {@link SzRecordKey} for passenger DEF456.
     */
    public static final SzRecordKey PASSENGER_DEF456
        = SzRecordKey.of(PASSENGERS, "DEF456");

    /**
     * The record definition for the {@link #CUSTOMER_ABC123} 
     * and {@link #EMPLOYEE_ABC123} record keys.
     */
    public static final String RECORD_ABC123 = """
            {
                "NAME_FULL": "Joe Schmoe",
                "HOME_PHONE_NUMBER": "702-555-1212",
                "MOBILE_PHONE_NUMBER": "702-555-1313",
                "ADDR_FULL": "101 Main Street, Las Vegas, NV 89101"
            }
            """;
    
    /**
     * The record definition for the {@link #CUSTOMER_DEF456}
     * and {@link #EMPLOYEE_DEF456} record keys.
     */
    public static final String RECORD_DEF456 = """
            {
                "NAME_FULL": "Jane Schmoe",
                "HOME_PHONE_NUMBER": "702-555-1212",
                "MOBILE_PHONE_NUMBER": "702-555-1414",
                "ADDR_FULL": "101 Main Street, Las Vegas, NV 89101",
                "SSN_NUMBER": "888-88-8888"
            }
            """;
    
    /**
     * The {@link Set} of {@link SzRecord} instances to trigger
     * a redo so {@link SzEngine#processRedoRecord(String)} can be tested.
     */
    public static final Set<SzRecord> PROCESS_REDO_TRIGGER_RECORDS = Set.of(
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-11"),
            SzFullName.of("Anthony Stark"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-12"),
            SzFullName.of("Janet Van Dyne"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-13"),
            SzFullName.of("Henry Pym"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-14"),
            SzFullName.of("Bruce Banner"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-15"),
            SzFullName.of("Steven Rogers"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-16"),
            SzFullName.of("Clinton Barton"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-17"),
            SzFullName.of("Wanda Maximoff"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-18"),
            SzFullName.of("Victor Shade"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-19"),
            SzFullName.of("Natasha Romanoff"),
            SzSocialSecurity.of("888-88-8888")),
        new SzRecord(
            SzRecordKey.of(EMPLOYEES, "SAME-SSN-20"),
            SzFullName.of("James Rhodes"),
            SzSocialSecurity.of("888-88-8888")));

    /**
     * A fake redo record for attempting redo pre-reinitialize.
     */
    public static final Iterator<String> FAKE_REDO_RECORDS;
    
    static {
        List<String> list = new LinkedList<>();
        for (SzRecord record : PROCESS_REDO_TRIGGER_RECORDS) {
            SzRecordKey recordKey = record.getRecordKey();
            list.add("""
                {
                    "REASON": "LibFeatID[?] of FTypeID[7] went generic for CANDIDATES",
                    "DATA_SOURCE": "<DATA_SOURCE>",
                    "RECORD_ID": "<RECORD_ID>",
                    "REEVAL_ITERATION": 1,
                    "ENTITY_CORRUPTION_TRANSIENT": false,
                    "DSRC_ACTION": "X"
                }
                """.replaceAll("<DATA_SOURCE>", recordKey.dataSourceCode())
                   .replaceAll("<RECORD_ID>", recordKey.recordId()));
        }
        FAKE_REDO_RECORDS = circularIterator(list);
    }

    /**
     * The environment for this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The {@link Set} of feature ID's to use for the tests.
     */
    private Set<Long> featureIds = null;

    /**
     * The {@link Map} if {@link SzRecordKey} keys to {@link Long}
     * entity ID values.
     */
    private Map<SzRecordKey, Long> byRecordKeyLookup = null;

    /**
     * Gets the entity ID for the specified {@link SzRecordKey}.
     * 
     * @param key The {@link SzRecordKey} for which to lookup the entity.
     * 
     * @return The entity ID for the specified {@link SzRecordKey}, or
     *         <code>null</code> if not found.
     */
    private Long getEntityId(SzRecordKey key) {
        return this.byRecordKeyLookup.get(key);
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

        try {
            SzEngine engine = this.env.getEngine();
            engine.addRecord(EMPLOYEE_ABC123, RECORD_ABC123);
            engine.addRecord(EMPLOYEE_DEF456, RECORD_DEF456);
            for (SzRecord record : PROCESS_REDO_TRIGGER_RECORDS) {
                engine.addRecord(record.getRecordKey(), record.toString());
            }

        } catch (SzException e) {
            fail("Failed to load record", e);
        }
 
        // change config and load data in a sub-process
        this.executeSubProcess();
    }

    /**
     * Overridden to configure <b>ONLY</b> the {@link #EMPLOYEES} data source.
     * 
     * @param excludeConfig 
     */
    protected void prepareRepository() {
        File repoDirectory = this.getRepositoryDirectory();
        Set<String> dataSources = Set.of(EMPLOYEES);

        RepositoryManager.configSources(repoDirectory, dataSources, true);
    }

    /**
     * Executes a sub-process that will add a data source to the config
     * and load two records to that data source and then wait for that
     * process to commplete.
     */
    private void executeSubProcess() {
        try {
            File repoDirectory = this.getRepositoryDirectory();
            File initFile = new File(repoDirectory, "sz-init.json");

            File outputFile = File.createTempFile(this.getClass().getSimpleName() + "-", ".json");
            
            String buildDirProp = System.getProperty("project.build.directory");
            File buildDir       = new File(buildDirProp);
            File wrapperDir     = new File(buildDir, "java-wrapper");
            File binDir         = new File(wrapperDir, "bin");
            File wrapper        = new File(binDir, "java-wrapper.bat");

            String[] cmdArray = new String[] {
                wrapper.getCanonicalPath(),
                "-cp",
                System.getProperty("java.class.path"),
                "-Xmx1000M",
                "-Dproject.build.directory=" + buildDirProp,
                SzConfigRetryableTest.class.getName(),
                initFile.getCanonicalPath(),
                outputFile.getCanonicalPath()
            };

            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(cmdArray);

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                fail("Failed to launch alternate process to update config");
            }

            String output = IOUtilities.readTextFileAsString(outputFile, UTF_8);

            JsonObject  jsonObj = parseJsonObject(output);
            JsonArray   jsonArr = getJsonArray(jsonObj, "ENTITIES");

            Map<SzRecordKey, Long>      byRecordKeyMap  = new LinkedHashMap<>();
            Set<Long>                   featureIds      = new LinkedHashSet<>();

            for (JsonObject entityObj : jsonArr.getValuesAs(JsonObject.class)) {
                entityObj = getJsonObject(entityObj, "RESOLVED_ENTITY");
                
                // get the feature ID's
                JsonObject features = getJsonObject(entityObj, "FEATURES");
                features.values().forEach((jsonVal) -> {
                    JsonArray featureArr = (JsonArray) jsonVal;
                    for (JsonObject featureObj : featureArr.getValuesAs(JsonObject.class)) {
                        Long featureId = getLong(featureObj, "LIB_FEAT_ID");
                        featureIds.add(featureId);
                    }
                });

                // get the entity ID
                Long entityId = getLong(entityObj, "ENTITY_ID");

                // get the record keys
                JsonArray recordArr = getJsonArray(entityObj, "RECORDS");
                for (JsonObject recordObj : recordArr.getValuesAs(JsonObject.class)) {
                    String dataSource   = getString(recordObj, "DATA_SOURCE");
                    String recordId     = getString(recordObj, "RECORD_ID");

                    if (CUSTOMERS.equals(dataSource)) {
                        SzRecordKey recordKey = SzRecordKey.of(dataSource, recordId);
                        byRecordKeyMap.put(recordKey, entityId);
                    }
                }
            }

            this.featureIds         = Collections.unmodifiableSet(featureIds);
            this.byRecordKeyLookup  = Collections.unmodifiableMap(byRecordKeyMap);

        } catch (InterruptedException|IOException e) {
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

    public interface Getter<T> {
        T get(SzConfigRetryableTest test, Object pre) throws SzException;
    }

    public interface PreProcess {
        Object process(SzConfigRetryableTest test)
            throws SzException;
    }

    public interface PostProcess {
        void process(SzConfigRetryableTest test, Object pre, Object result)
            throws SzException;
    }

    private static Object[] arrayOf(Object... elems) {
        return elems;
    }

    private static void addMethod(Set<Method>       handledMethods,
                                  List<Arguments>   results,
                                  Getter<?>         getter,
                                  Method            method, 
                                  Boolean           expectRetryable,
                                  Getter<Object[]>  paramGetter)
    {
        addMethod(handledMethods, results, getter, method, expectRetryable, paramGetter, null, null);
    }

    private static void addMethod(Set<Method>       handledMethods,
                                  List<Arguments>   results,
                                  Getter<?>         getter,
                                  Method            method, 
                                  Boolean           expectRetryable,
                                  Getter<Object[]>  paramGetter,
                                  PreProcess        preProcess,
                                  PostProcess       postProcess)
                                  
    {
        if (handledMethods.contains(method)) return;
        results.add(Arguments.of(getter, method, expectRetryable, paramGetter, preProcess, postProcess));
        handledMethods.add(method);
    }

    private static void addProductMethods(Set<Method>       handledMethods,
                                          List<Arguments>   results)
    {
        try {
            addMethod(handledMethods, 
                     results, 
                     (test, pre) -> test.env.getProduct(),
                     SzProduct.class.getMethod("getVersion"),
                     false,
                     EMPTY_GETTER);

            addMethod(handledMethods, 
                     results, 
                     (test, pre) -> test.env.getProduct(),
                     SzProduct.class.getMethod("getLicense"),
                     false,
                     EMPTY_GETTER);

            // handle any methods not explicitly handled
            Method[] methods = SzProduct.class.getMethods();
            for (Method method : methods) {
                addMethod(handledMethods, results, 
                          (test, pre) -> null,
                          method,
                          null,
                          null);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addConfigManagerMethods(Set<Method>     handledMethods,
                                                List<Arguments> results)
    {
        try {
            // handle config manager methods
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getConfigManager(), 
                      SzConfigManager.class.getMethod("createConfig"),
                      Boolean.FALSE,
                      EMPTY_GETTER);

            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("createConfig", Long.TYPE),
                      Boolean.FALSE,
                      (test, pre) -> arrayOf(test.env.getConfigManager().getDefaultConfigId()));

            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("createConfig", String.class),
                      Boolean.FALSE,
                      (test, pre) -> arrayOf(test.env.getConfigManager().createConfig().export()));

            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod(
                        "registerConfig", String.class, String.class),
                      Boolean.FALSE,
                      (test, pre) -> arrayOf(test.env.getConfigManager().createConfig().export(), "Template Config"));

            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod(
                        "registerConfig", String.class),
                      Boolean.FALSE,
                      (test, pre) -> {
                        SzConfig config = test.env.getConfigManager().createConfig();
                        config.registerDataSource(EMPLOYEES);
                        return arrayOf(config.export());
                      });
                
            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("getConfigRegistry"),
                      Boolean.FALSE,
                      EMPTY_GETTER);

            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("getDefaultConfigId"),
                      Boolean.FALSE,
                      EMPTY_GETTER);
            
            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("replaceDefaultConfigId", Long.TYPE, Long.TYPE),
                      Boolean.FALSE,
                      null);

            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("setDefaultConfigId", Long.TYPE),
                      Boolean.FALSE,
                      null);
            
            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("setDefaultConfig", String.class, String.class),
                      Boolean.FALSE,
                      null);

            addMethod(handledMethods,
                      results, 
                      (test, pre) -> test.env.getConfigManager(),
                      SzConfigManager.class.getMethod("setDefaultConfig", String.class),
                      Boolean.FALSE,
                      null);

            Method[] methods = SzConfigManager.class.getMethods();
            for (Method method : methods) {
                addMethod(handledMethods, 
                          results,
                          (test, pre) -> null,
                          method,
                          null,
                          null);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private static void addConfigMethods(Set<Method>     handledMethods,
                                         List<Arguments> results)
    {
        try {
            // handle config methods
            addMethod(handledMethods, 
                      results,
                      (test, pre) -> test.env.getConfigManager().createConfig(),
                      SzConfig.class.getMethod("export"),
                      Boolean.FALSE,
                      EMPTY_GETTER);
            
            addMethod(handledMethods, 
                      results,
                      (test, pre) -> test.env.getConfigManager().createConfig(),
                      SzConfig.class.getMethod("getDataSourceRegistry"),
                      Boolean.FALSE,
                      EMPTY_GETTER);
            
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getConfigManager().createConfig(),
                      SzConfig.class.getMethod("registerDataSource", String.class),
                      Boolean.FALSE,
                      (test, pre) -> arrayOf(CUSTOMERS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getConfigManager().createConfig(
                        test.env.getConfigManager().getDefaultConfigId()),
                      SzConfig.class.getMethod("unregisterDataSource", String.class),
                      Boolean.FALSE,
                      (test, pre) -> arrayOf(CUSTOMERS));

            Method[] methods = SzConfig.class.getMethods();
            for (Method method : methods) {
                addMethod(handledMethods, 
                          results,
                          (test, pre) -> null,
                          method,
                          null,
                          null);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private static void addDiagnosticMethods(Set<Method>        handledMethods,
                                             List<Arguments>    results)
    {
        try {
            // handle config methods
            addMethod(handledMethods, 
                      results,
                      (test, pre) -> test.env.getDiagnostic(),
                      SzDiagnostic.class.getMethod("getRepositoryInfo"),
                      Boolean.FALSE,
                      EMPTY_GETTER);
            
            addMethod(handledMethods, 
                      results,
                      (test, pre) -> test.env.getDiagnostic(),
                      SzDiagnostic.class.getMethod("checkRepositoryPerformance", Integer.TYPE),
                      Boolean.FALSE,
                      (test, pre) -> arrayOf(5));
            
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getDiagnostic(),
                      SzDiagnostic.class.getMethod("purgeRepository"),
                      Boolean.FALSE,
                      null);

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getDiagnostic(),
                      SzDiagnostic.class.getMethod("getFeature", Long.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.featureIds.iterator().next()));

            Method[] methods = SzDiagnostic.class.getMethods();
            for (Method method : methods) {
                addMethod(handledMethods, 
                          results,
                          (test, pre) -> null,
                          method,
                          null,
                          null);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private static void addEngineMethods(Set<Method>        handledMethods,
                                         List<Arguments>    results)
    {
        try {
            // handle config methods
            addMethod(handledMethods, 
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("primeEngine"),
                      Boolean.FALSE,
                      EMPTY_GETTER);
            
            addMethod(handledMethods, 
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getStats"),
                      Boolean.FALSE,
                      EMPTY_GETTER);
            
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getRecordPreview", String.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, SZ_RECORD_PREVIEW_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getRecordPreview", String.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("searchByAttributes", String.class, String.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, null, SZ_SEARCH_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("searchByAttributes", String.class, String.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, null));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("searchByAttributes", String.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, SZ_SEARCH_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("searchByAttributes", String.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("whySearch", String.class, Long.TYPE, String.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, test.getEntityId(CUSTOMER_DEF456), null, SZ_WHY_SEARCH_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("whySearch", String.class, Long.TYPE, String.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, test.getEntityId(CUSTOMER_DEF456), null));
            
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("whySearch", String.class, Long.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, test.getEntityId(CUSTOMER_DEF456), SZ_WHY_SEARCH_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("whySearch", String.class, Long.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(RECORD_ABC123, test.getEntityId(CUSTOMER_DEF456)));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getEntity", Long.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123), SZ_ENTITY_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getEntity", Long.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123)));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getEntity", SzRecordKey.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, SZ_ENTITY_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getEntity", SzRecordKey.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("findInterestingEntities", Long.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123), SZ_ENTITY_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("findInterestingEntities", Long.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123)));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("findInterestingEntities", SzRecordKey.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, SZ_ENTITY_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("findInterestingEntities", SzRecordKey.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", Long.TYPE, Long.TYPE, Integer.TYPE, SzEntityIds.class, Set.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123),
                                        test.getEntityId(CUSTOMER_DEF456),
                                        3,
                                        null,
                                        null,
                                        SZ_FIND_PATH_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", Long.TYPE, Long.TYPE, Integer.TYPE, SzEntityIds.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123),
                                        test.getEntityId(CUSTOMER_DEF456),
                                        3,
                                        null,
                                        null));
                      
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", Long.TYPE, Long.TYPE, Integer.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123),
                                        test.getEntityId(CUSTOMER_DEF456),
                                        3,
                                        SZ_FIND_PATH_ALL_FLAGS));
            
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", Long.TYPE, Long.TYPE, Integer.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123),
                                        test.getEntityId(CUSTOMER_DEF456),
                                        3));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", SzRecordKey.class, SzRecordKey.class, Integer.TYPE, SzRecordKeys.class, Set.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123,
                                        CUSTOMER_DEF456,
                                        3,
                                        null,
                                        null,
                                        SZ_FIND_PATH_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", SzRecordKey.class, SzRecordKey.class, Integer.TYPE, SzRecordKeys.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123,
                                        CUSTOMER_DEF456,
                                        3,
                                        null,
                                        null));
                      
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", SzRecordKey.class, SzRecordKey.class, Integer.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123,
                                        CUSTOMER_DEF456,
                                        3,
                                        SZ_FIND_PATH_ALL_FLAGS));
            
            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findPath", SzRecordKey.class, SzRecordKey.class, Integer.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, CUSTOMER_DEF456, 3));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findNetwork", SzEntityIds.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(SzEntityIds.of(test.getEntityId(CUSTOMER_ABC123), 
                                                       test.getEntityId(CUSTOMER_DEF456)),
                                        3, 0, 10, SZ_FIND_NETWORK_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findNetwork", SzEntityIds.class, Integer.TYPE, Integer.TYPE, Integer.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(SzEntityIds.of(test.getEntityId(CUSTOMER_ABC123), 
                                                       test.getEntityId(CUSTOMER_DEF456)),
                                        3, 0, 10));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findNetwork", SzRecordKeys.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(SzRecordKeys.of(CUSTOMER_ABC123, CUSTOMER_DEF456),
                                        3, 0, 10, SZ_FIND_NETWORK_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "findNetwork", SzRecordKeys.class, Integer.TYPE, Integer.TYPE, Integer.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(SzRecordKeys.of(CUSTOMER_ABC123, CUSTOMER_DEF456), 3, 0, 10));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "whyRecordInEntity", SzRecordKey.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, SZ_WHY_RECORD_IN_ENTITY_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "whyRecordInEntity", SzRecordKey.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "whyRecords", SzRecordKey.class, SzRecordKey.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, CUSTOMER_DEF456, SZ_WHY_RECORDS_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "whyRecords", SzRecordKey.class, SzRecordKey.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, CUSTOMER_DEF456));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "whyEntities", Long.TYPE, Long.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123), 
                                        test.getEntityId(CUSTOMER_DEF456), 
                                        SZ_WHY_ENTITIES_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "whyEntities", Long.TYPE, Long.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123), 
                                        test.getEntityId(CUSTOMER_DEF456)));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod(
                        "howEntity", Long.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123), 
                                             SZ_HOW_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("howEntity", Long.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123)));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getVirtualEntity", Set.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(SzRecordKeys.of(CUSTOMER_ABC123, CUSTOMER_DEF456), 
                                        SZ_VIRTUAL_ENTITY_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getVirtualEntity", Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(SzRecordKeys.of(CUSTOMER_ABC123, CUSTOMER_DEF456)));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getRecord", SzRecordKey.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, SZ_RECORD_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getRecord", SzRecordKey.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("exportJsonEntityReport", Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(SZ_EXPORT_ALL_FLAGS),
                      null,
                      (test, pre, handle) -> { 
                        if (handle != null) { test.env.getEngine().closeExportReport((Long) handle); }});

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("exportJsonEntityReport"),
                      Boolean.TRUE,
                      EMPTY_GETTER,
                      null,
                      (test, pre, handle) -> { 
                        if (handle != null) { test.env.getEngine().closeExportReport((Long) handle); }});

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("exportCsvEntityReport", String.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf("*", SZ_EXPORT_ALL_FLAGS),
                      null,
                      (test, pre, handle) -> { 
                        if (handle != null) { test.env.getEngine().closeExportReport((Long) handle); }});

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("exportCsvEntityReport", String.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf("*"),
                      null,
                      (test, pre, handle) -> { 
                        if (handle != null) { test.env.getEngine().closeExportReport((Long) handle); }});

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("closeExportReport", Long.TYPE),
                      Boolean.FALSE,
                      null); // requires a valid export handle which cannot be gotten

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("fetchNext", Long.TYPE),
                      Boolean.TRUE,
                      null); // requires a valid export handle which cannot be gotten

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("countRedoRecords"),
                      Boolean.FALSE,
                      EMPTY_GETTER);

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("getRedoRecord"),
                      Boolean.TRUE,
                      EMPTY_GETTER);

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("processRedoRecord", String.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(FAKE_REDO_RECORDS.next(), SZ_REDO_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("processRedoRecord", String.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(FAKE_REDO_RECORDS.next()));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("addRecord", SzRecordKey.class, String.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(PASSENGER_ABC123, RECORD_ABC123, SZ_ADD_RECORD_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("addRecord", SzRecordKey.class, String.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(PASSENGER_DEF456, RECORD_DEF456));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("reevaluateRecord", SzRecordKey.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_ABC123, SZ_REEVALUATE_RECORD_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("reevaluateRecord", SzRecordKey.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(CUSTOMER_DEF456));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("reevaluateEntity", Long.TYPE, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_ABC123), SZ_REEVALUATE_ENTITY_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("reevaluateEntity", Long.TYPE),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(test.getEntityId(CUSTOMER_DEF456)));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("deleteRecord", SzRecordKey.class, Set.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(EMPLOYEE_ABC123, SZ_DELETE_RECORD_ALL_FLAGS));

            addMethod(handledMethods,
                      results,
                      (test, pre) -> test.env.getEngine(),
                      SzEngine.class.getMethod("deleteRecord", SzRecordKey.class),
                      Boolean.TRUE,
                      (test, pre) -> arrayOf(EMPLOYEE_DEF456));

            Method[] methods = SzEngine.class.getMethods();
            for (Method method : methods) {
                addMethod(handledMethods, 
                          results,
                          (test, pre) -> null,
                          method,
                          null,
                          null);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Arguments> getTestParameters() {
        List<Arguments> results = new LinkedList<>();

        Set<Method> handledMethods = new LinkedHashSet<>();

        addProductMethods(handledMethods, results);

        addConfigManagerMethods(handledMethods, results);

        addConfigMethods(handledMethods, results);

        addDiagnosticMethods(handledMethods, results);

        addEngineMethods(handledMethods, results);

        // handle the diagnostic methods
        return results;
    }


    @Order(10)
    @ParameterizedTest
    @MethodSource("getTestParameters")
    public void testMethodPreReinitialize(Getter<?>         getter,
                                          Method            method,
                                          Boolean           expectRetryable,
                                          Getter<Object[]>  paramGetter,
                                          PreProcess        preProcess,
                                          PostProcess       postProcess) 
    {
        this.performTest(() -> {
            try {
                SzConfigRetryable retryable = method.getAnnotation(SzConfigRetryable.class);
                if (expectRetryable == null) {
                    fail("Method from interface (" + method.getDeclaringClass().getSimpleName()
                        + ") is " + (retryable == null ? "not " : "") 
                        + "annotated and has no explicit config retryable test defined: "
                        + method.toString());
                }
                
                assertEquals(expectRetryable, (retryable != null),
                            "Method from interface (" + method.getDeclaringClass().getSimpleName() 
                            + ") is " + ((retryable == null) ? "not " : "") + "retryable but should "
                            + ((retryable == null) ? "not " : "") + "be: " + method.toString());
                
                if (paramGetter == null) return;

                Object preProcessResult = (preProcess == null) ? null : preProcess.process(this);
                
                Object target = getter.get(this, preProcessResult);
                
                Object[] params = paramGetter.get(this, preProcessResult);

                Object result = null;
                try {
                    // this may or may not succeed
                    result = method.invoke(target, params);

                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (!(cause instanceof SzException)) {
                        fail("Method from " + method.getDeclaringClass().getSimpleName() + " got an "
                            + "unexpected exception: " + method.toString(), e);
                    }
                    if (!expectRetryable) {
                        fail("Non-annotated method from " + method.getDeclaringClass().getSimpleName()
                            + "got an exception before reinitialization: " + method.toString(), e);
                    }
                } finally {
                    if (postProcess != null) {
                        postProcess.process(this, preProcessResult, result);
                    }
                }

            } catch (Exception e) {
                fail("Method from " + method.getDeclaringClass().getSimpleName() 
                    + " failed with exception: " + method.toString(), e);
            }
        });
    }

    @Order(20)
    @Test
    public void testReinitialize() {
        this.performTest(() -> {
            try {
                this.env.reinitialize(this.env.getConfigManager().getDefaultConfigId());
                
                Map<SzRecordKey, Long> map = new LinkedHashMap<>();
                for (SzRecordKey recordKey : this.byRecordKeyLookup.keySet()) {
                    try {
                        String entity = this.env.getEngine().getEntity(recordKey, null);

                        JsonObject jsonObj = parseJsonObject(entity);
                        jsonObj = getJsonObject(jsonObj, "RESOLVED_ENTITY");
                        
                        Long entityId = getLong(jsonObj, "ENTITY_ID");

                        map.put(recordKey, entityId);

                    } catch (SzException e) {
                        fail("Failed to update entity ID for record key: " + recordKey, e);
                    }
                }
                this.byRecordKeyLookup = Collections.unmodifiableMap(map);

            } catch (Exception e) {
                fail("Failed to reinitialize", e);
            }
        });
    }

    @Order(30)
    @ParameterizedTest
    @MethodSource("getTestParameters")
    public void testMethodPostReinitialize(Getter<?>        getter,
                                           Method           method,
                                           Boolean          expectRetryable,
                                           Getter<Object[]> paramGetter,
                                           PreProcess       preProcess,
                                           PostProcess      postProcess) 
    {
        this.performTest(() -> {
            try {
                SzConfigRetryable retryable = method.getAnnotation(SzConfigRetryable.class);
                assertEquals(expectRetryable, (retryable != null),
                            "Method from interface (" + method.getDeclaringClass() + ") is "
                            + ((retryable == null) ? "not " : "") + "retryable but should "
                            + ((retryable == null) ? "not " : "") + "be: " + method.toString());
                
                if (paramGetter == null) return;

                Object preProcessResult = (preProcess == null) ? null : preProcess.process(this);
                
                Object target = getter.get(this, preProcessResult);

                Object[] params = paramGetter.get(this, preProcessResult);

                Object result = null;
                try {
                    result = method.invoke(target, params);

                } catch (InvocationTargetException e) {
                    fail((expectRetryable ? "Annotated" : "Non-annotated") + " method from " 
                        + method.getDeclaringClass().getSimpleName()
                        + " got an exception AFTER reinitialization: " + method.toString(), e);

                } finally {
                    if (postProcess != null) {
                        postProcess.process(this, preProcessResult, result);
                    }
                }

            } catch (Exception e) {
                fail("Method from " + method.getDeclaringClass().getSimpleName() 
                    + " failed with exception: " + method.toString(), e);
            }
        });
    }

    /**
     * Provides the main for the second process that changes the 
     * config and loads records.  The path to the repository's
     * initialization file is the expected command-line argument
     * and the path to the output file.
     * 
     * 
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        SzEnvironment env = null;
        try {
            if (args.length < 2) {
                System.err.println("Must specify the following command-line arguments:");
                System.err.println("  1: Path to setting JSON file for the repository");
                System.err.println("  2: Path to the output file for the results");
                System.exit(1);
            }

            String  initFilePath    = args[0];
            String  outputFilePath  = args[1];
            File    initFile        = new File(initFilePath);
            File    outputFile      = new File(outputFilePath);

            if (!initFile.exists()) {
                System.err.println("Settings file does not exist: " + initFilePath);
                System.exit(1);
            }

            String initJson = IOUtilities.readTextFileAsString(initFile, UTF_8);

            env = SzCoreEnvironment.newBuilder()
                .settings(initJson)
                .instanceName("Alternate SzConfigRetryable")
                .build();

            SzConfigManager configMgr = env.getConfigManager();

            SzConfig config = configMgr.createConfig(configMgr.getDefaultConfigId());
            config.registerDataSource(CUSTOMERS);
            config.registerDataSource(PASSENGERS);
            long configId = configMgr.setDefaultConfig(config.export());
            env.reinitialize(configId);

            SzEngine engine = env.getEngine();

            engine.addRecord(CUSTOMER_ABC123, RECORD_ABC123);
            engine.addRecord(CUSTOMER_DEF456, RECORD_DEF456);

            String network = engine.findNetwork(
                SzRecordKeys.of(CUSTOMER_ABC123, CUSTOMER_DEF456),
                2, 0, 0,
                EnumSet.allOf(SzFlag.class));

            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, UTF_8))
            {
                osw.write(network);
                osw.flush();
            }

        } catch (Exception e) {
            String  buildDirProp    = System.getProperty("project.build.directory");
            File    buildDir        = new File(buildDirProp);
            String  className       = SzConfigRetryableTest.class.getSimpleName();
            String  outFileName     = className + "-errors.txt";
            File    outFile         = new File(buildDir, outFileName);
            try (FileOutputStream   fos = new FileOutputStream(outFile);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, UTF_8);
                 PrintWriter        pw  = new PrintWriter(osw))
            {
                for (int index = 0; index < args.length; index++) {
                    pw.println("ARG " + index + ": " + args[index]);
                }
                pw.println();
                e.printStackTrace(pw);
                pw.flush();

            } catch (IOException e2) {
                e2.printStackTrace();
            }

            e.printStackTrace();
            System.exit(1);

        } finally {
            if (env != null) {
                env.destroy();
            }
        }
    }
}
