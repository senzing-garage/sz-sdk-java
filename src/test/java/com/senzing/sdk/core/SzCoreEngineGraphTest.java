package com.senzing.sdk.core;

import java.util.List;
import java.util.Set;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzRecordKeys;
import com.senzing.sdk.SzEntityIds;
import com.senzing.sdk.SzException;
import com.senzing.sdk.test.SzEngineGraphTest;
import com.senzing.sdk.test.TestDataLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.sdk.SzFlag.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreEngineGraphTest 
    extends AbstractCoreTest 
    implements SzEngineGraphTest
{
    private SzCoreEnvironment env = null;

    private TestData testData = new TestData();

    @Override
    public SzEngine getEngine() throws SzException {
        return this.env.getEngine();
    }

    @Override
    public TestData getTestData() {
        return this.testData;
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
    }

    /**
     * Overridden to configure some data sources.
     */
    protected void prepareRepository() {
        File repoDirectory = this.getRepositoryDirectory();

        TestDataLoader loader = new RepoMgrTestDataLoader(repoDirectory);

        this.testData.loadData(loader);    
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

    public List<Arguments> getEntityPathDefaultParameters()
    {
        List<Arguments> argsList = this.getEntityPathParameters();

        List<Arguments> result = new ArrayList<>(argsList.size());

        argsList.forEach(args -> {
            Object[] arr = args.get();

            // skip the ones that expect an exception
            if (arr[arr.length - 3] != null) return;
            if (arr[arr.length - 4] != null) return;
            result.add(Arguments.of(
                arr[1], arr[3], arr[5], arr[6], arr[7], arr[8], arr[9]));
        });
        return result;
    }

    @ParameterizedTest
    @MethodSource("getEntityPathDefaultParameters")
    public void testFindPathByRecordIdDefaults(
        SzRecordKey         startRecordKey,
        SzRecordKey         endRecordKey,
        int                 maxDegrees,
        Set<SzRecordKey>    avoidances,
        Set<Long>           avoidanceIds,
        Set<String>         requiredSources,
        Set<SzFlag>         unusedFlags)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine)this.env.getEngine();

                String startDataSourceCode = startRecordKey.dataSourceCode();
                String startRecordID = startRecordKey.recordId();

                String endDataSourceCode = endRecordKey.dataSourceCode();
                String endRecordID = endRecordKey.recordId();

                String defaultResult = engine.findPath(startRecordKey,
                                                       endRecordKey,
                                                       maxDegrees,
                                                       SzRecordKeys.of(avoidances),
                                                       requiredSources);

                String explicitResult = engine.findPath(startRecordKey,
                                                        endRecordKey,
                                                        maxDegrees,
                                                        SzRecordKeys.of(avoidances),
                                                        requiredSources,
                                                        SZ_FIND_PATH_DEFAULT_FLAGS);

                NativeEngine nativeEngine = engine.getNativeApi();

                String avoidanceJson = SzCoreEngine.encodeRecordKeys(avoidances);

                String sourcesJson = SzCoreEngine.encodeDataSources(requiredSources);

                StringBuffer sb = new StringBuffer();
                int returnCode;
                if (avoidances == null && requiredSources == null) {
                    returnCode = nativeEngine.findPathByRecordID(
                        startDataSourceCode,
                        startRecordID,
                        endDataSourceCode,
                        endRecordID,
                        maxDegrees,
                        sb);
                } else if (requiredSources == null) {
                    returnCode = nativeEngine.findPathByRecordIDWithAvoids(
                        startDataSourceCode,
                        startRecordID,
                        endDataSourceCode,
                        endRecordID,
                        maxDegrees,
                        avoidanceJson,
                        sb);
                } else {
                    returnCode = nativeEngine.findPathByRecordIDIncludingSource(
                        startDataSourceCode,
                        startRecordID,
                        endDataSourceCode,
                        endRecordID,
                        maxDegrees,
                        avoidanceJson,
                        sourcesJson,
                        sb);
                }
                String nativeResult = sb.toString();

                if (returnCode != 0)
                {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            
            } catch (Exception e) {
                fail("Unexpectedly failed to find path.  startRecord=[ "
                     + startRecordKey + " ], endRecordKey=[ " + endRecordKey
                     + " ], maxDegrees=[ " + maxDegrees + " ], avoidances=[ "
                     + avoidances + " ], requiredSources=[ " + requiredSources
                     + " ]", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getEntityPathDefaultParameters")
    public void testFindPathByEntityIdDefaults(
        SzRecordKey         startRecordKey,
        SzRecordKey         endRecordKey,
        int                 maxDegrees,
        Set<SzRecordKey>    avoidances,
        Set<Long>           avoidanceIds,
        Set<String>         requiredSources,
        Set<SzFlag>         unusedFlags)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine)this.env.getEngine();

                long startEntityId = this.getEntityId(startRecordKey);
                long endEntityId = this.getEntityId(endRecordKey);

                String defaultResult = engine.findPath(startEntityId,
                                                       endEntityId,
                                                       maxDegrees,
                                                       SzEntityIds.of(avoidanceIds),
                                                       requiredSources);

                String explicitResult = engine.findPath(startEntityId,
                                                        endEntityId,
                                                        maxDegrees,
                                                        SzEntityIds.of(avoidanceIds),
                                                        requiredSources,
                                                        SZ_FIND_PATH_DEFAULT_FLAGS);

                NativeEngine nativeEngine = engine.getNativeApi();

                String avoidanceJson = SzCoreEngine.encodeRecordKeys(avoidances);

                String sourcesJson = SzCoreEngine.encodeDataSources(requiredSources);

                StringBuffer sb = new StringBuffer();
                int returnCode;
                if (avoidances == null && requiredSources == null) {
                    returnCode = nativeEngine.findPathByEntityID(
                        startEntityId,
                        endEntityId,
                        maxDegrees,
                        sb);
                } else if (requiredSources == null) {
                    returnCode = nativeEngine.findPathByEntityIDWithAvoids(
                        startEntityId,
                        endEntityId,
                        maxDegrees,
                        avoidanceJson,
                        sb);
                } else {
                    returnCode = nativeEngine.findPathByEntityIDIncludingSource(
                        startEntityId,
                        endEntityId,
                        maxDegrees,
                        avoidanceJson,
                        sourcesJson,
                        sb);
                }
                String nativeResult = sb.toString();

                if (returnCode != 0)
                {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            
            } catch (Exception e) {
                fail("Unexpectedly failed to find path.  startRecord=[ "
                     + startRecordKey + " ], endRecordKey=[ " + endRecordKey
                     + " ], maxDegrees=[ " + maxDegrees + " ], avoidances=[ "
                     + avoidances + " ], requiredSources=[ " + requiredSources
                     + " ]", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getEntityPathDefaultParameters")
    public void testFindPathByRecordIdSimple(
        SzRecordKey         startRecordKey,
        SzRecordKey         endRecordKey,
        int                 maxDegrees,
        Set<SzRecordKey>    avoidances,
        Set<Long>           avoidanceIDs,
        Set<String>         requiredSources,
        Set<SzFlag>         flags)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine)this.env.getEngine();

                String startDataSourceCode = startRecordKey.dataSourceCode();
                String startRecordID = startRecordKey.recordId();

                String endDataSourceCode = endRecordKey.dataSourceCode();
                String endRecordID = endRecordKey.recordId();

                String simpleResult = engine.findPath(startRecordKey,
                                                      endRecordKey,
                                                      maxDegrees,
                                                      flags);

                String explicitResult = engine.findPath(startRecordKey,
                                                        endRecordKey,
                                                        maxDegrees,
                                                        null,
                                                        null,
                                                        flags);

                NativeEngine nativeEngine = engine.getNativeApi();

                StringBuffer sb = new StringBuffer();

                int returnCode = nativeEngine.findPathByRecordID(
                        startDataSourceCode,
                        startRecordID,
                        endDataSourceCode,
                        endRecordID,
                        maxDegrees,
                        SzFlag.toLong(flags),
                        sb);

                String nativeResult = sb.toString();

                if (returnCode != 0)
                {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                assertEquals(explicitResult, simpleResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, simpleResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");

            } catch (Exception e) {
                fail("Unexpectedly failed to find path.  startRecord=[ "
                     + startRecordKey + " ], endRecordKey=[ " + endRecordKey
                     + " ], maxDegrees=[ " + maxDegrees + " ], avoidances=[ "
                     + avoidances + " ], requiredSources=[ " + requiredSources
                     + " ]", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getEntityPathDefaultParameters")
    public void testFindPathByEntityIdSimple(
        SzRecordKey         startRecordKey,
        SzRecordKey         endRecordKey,
        int                 maxDegrees,
        Set<SzRecordKey>    avoidances,
        Set<Long>           avoidanceIDs,
        Set<String>         requiredSources,
        Set<SzFlag>         flags)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                long startEntityID = this.getEntityId(startRecordKey);
                long endEntityID = this.getEntityId(endRecordKey);

                String simpleResult = engine.findPath(startEntityID,
                                                      endEntityID,
                                                      maxDegrees,
                                                      flags);

                String explicitResult = engine.findPath(startEntityID,
                                                        endEntityID,
                                                        maxDegrees,
                                                        null,
                                                        null,
                                                        flags);

                NativeEngine nativeEngine = engine.getNativeApi();

                StringBuffer sb = new StringBuffer();
                
                int returnCode = nativeEngine.findPathByEntityID(
                        startEntityID,
                        endEntityID,
                        maxDegrees,
                        SzFlag.toLong(flags),
                        sb);
                
                String nativeResult = sb.toString();

                if (returnCode != 0) {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                assertEquals(explicitResult, simpleResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, simpleResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            
            } catch (Exception e) {
                fail("Unexpectedly failed to find path.  startRecord=[ "
                     + startRecordKey + " ], endRecordKey=[ " + endRecordKey
                     + " ], maxDegrees=[ " + maxDegrees + " ], avoidances=[ "
                     + avoidances + " ], requiredSources=[ " + requiredSources
                     + " ]", e);
            }
        });
    }

    public List<Arguments> getEntityNetworkDefaultParameters()
    {
        List<Arguments> argsList = this.getEntityNetworkParameters();

        List<Arguments> result = new ArrayList<>(argsList.size());

        argsList.forEach(args -> {
            Object[] arr = args.get();
            
            // skip the ones that expect an exception
            if (arr[arr.length - 4] != null) return;
            if (arr[arr.length - 5] != null) return;
            result.add(Arguments.of(arr[1], arr[3], arr[4], arr[5]));

        });
        return result;
    }

    @ParameterizedTest
    @MethodSource("getEntityNetworkDefaultParameters")
    public void testFindNetworkByRecordIdDefaults(
        Set<SzRecordKey>    recordKeys,
        int                 maxDegrees,
        int                 buildOutDegrees,
        int                 buildOutMaxEntities)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String defaultResult = engine.findNetwork(SzRecordKeys.of(recordKeys),
                                                          maxDegrees,
                                                          buildOutDegrees,
                                                          buildOutMaxEntities);

                String explicitResult = engine.findNetwork(SzRecordKeys.of(recordKeys),
                                                           maxDegrees,
                                                           buildOutDegrees,
                                                           buildOutMaxEntities,
                                                           SZ_FIND_NETWORK_DEFAULT_FLAGS);

                NativeEngine nativeEngine = engine.getNativeApi();

                String recordsJson = SzCoreEngine.encodeRecordKeys(recordKeys);

                StringBuffer sb = new StringBuffer();
                int returnCode = nativeEngine.findNetworkByRecordID(recordsJson,
                                                                    maxDegrees,
                                                                    buildOutDegrees,
                                                                    buildOutMaxEntities,
                                                                    sb);                
                if (returnCode != 0) {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                String nativeResult = sb.toString();

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");

            } catch (Exception e) {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getEntityNetworkDefaultParameters")
    public void testFindNetworkByEntityIDDefaults(
        Set<SzRecordKey>    recordKeys,
        int                 maxDegrees,
        int                 buildOutDegrees,
        int                 buildOutMaxEntities)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                Set<Long> entityIDs = new TreeSet<>(this.getEntityIds(recordKeys));

                String defaultResult = engine.findNetwork(SzEntityIds.of(entityIDs),
                                                          maxDegrees,
                                                          buildOutDegrees,
                                                          buildOutMaxEntities);

                String explicitResult = engine.findNetwork(SzEntityIds.of(entityIDs),
                                                           maxDegrees,
                                                           buildOutDegrees,
                                                           buildOutMaxEntities,
                                                           SZ_FIND_NETWORK_DEFAULT_FLAGS);

                NativeEngine nativeEngine = engine.getNativeApi();

                String entitiesJson = SzCoreEngine.encodeEntityIds(entityIDs);

                StringBuffer sb = new StringBuffer();
                int returnCode = nativeEngine.findNetworkByEntityID(entitiesJson,
                                                                    maxDegrees,
                                                                    buildOutDegrees,
                                                                    buildOutMaxEntities,
                                                                    sb);

                if (returnCode != 0) {
                    fail("Errant return code from native function: " +
                         engine.getNativeApi().getLastExceptionCode()
                         + " / " + engine.getNativeApi().getLastException());
                }

                String nativeResult = sb.toString();

                assertEquals(explicitResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the SDK function.");

                assertEquals(nativeResult, defaultResult,
                    "Explicitly setting default flags yields a different result "
                    + "than omitting the flags parameter to the native function.");
            }
            catch (Exception e)
            {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }
}
