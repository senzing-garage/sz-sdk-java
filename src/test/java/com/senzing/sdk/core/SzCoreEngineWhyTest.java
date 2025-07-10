package com.senzing.sdk.core;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

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
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.test.SzEngineWhyTest;
import com.senzing.sdk.test.SzEntityLookup;
import com.senzing.sdk.test.TestDataLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.sdk.test.SzEngineWhyTest.TestData.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreEngineWhyTest 
    extends AbstractCoreTest 
    implements SzEngineWhyTest
{
    private TestData testData = new TestData();

    private SzCoreEnvironment env = null;

    @Override
    public TestData getTestData() {
        return this.testData;
    }

    @Override
    public SzEngine getEngine() throws SzException {
        return this.env.getEngine();
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

    public List<Arguments> getWhySearchDefaultParameters() {
        List<Arguments> whySearchParams = this.getWhySearchParameters();

        List<Arguments> defaultParams = new ArrayList<>(whySearchParams.size());

        whySearchParams.forEach(args -> {
            Object[] arr = args.get();

            // skip the parameters that expect exceptions
            if (arr[arr.length - 1] != null) return;

            defaultParams.add(Arguments.of(arr[1], arr[2], arr[3], arr[4]));
        });

        return defaultParams;
    }

    @ParameterizedTest
    @MethodSource("getWhySearchDefaultParameters")
    public void testWhySearchDefaults(String        attributes,
                                      SzRecordKey   recordKey,
                                      long          entityId,
                                      String        searchProfile)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String defaultResult = (searchProfile == null)
                    ? engine.whySearch(attributes, entityId)
                    : engine.whySearch(attributes, entityId, searchProfile);

                String explicitResult = (searchProfile == null)
                    ? engine.whySearch(attributes,
                                       entityId,   
                                       SZ_WHY_SEARCH_DEFAULT_FLAGS)
                    : engine.whySearch(attributes,
                                       entityId,
                                       searchProfile,
                                       SZ_WHY_SEARCH_DEFAULT_FLAGS);

                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().whySearch(
                    attributes, entityId, 
                    (searchProfile == null ? "" : searchProfile), sb);
                        
                if (returnCode != 0) {
                    fail("Errant return code from native function: " 
                         + engine.getNativeApi().getLastExceptionCode()
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
                fail("Unexpectedly failed why-search operation", e);
            }
        });
    }

    public static List<Arguments> getRecordCombinations() {
        List<Arguments> result = new ArrayList<>(RECORD_KEYS.size() * RECORD_KEYS.size());

        RECORD_KEYS.forEach(key1 -> {
                RECORD_KEYS.forEach(key2 -> {
                        if (key1.equals(key2)) return;
                        result.add(Arguments.of(key1, key2));
                });
        });

        return result;
    }

    public static List<Arguments> getRecordKeyParameters() {
        List<Arguments> result = new ArrayList<>(RECORD_KEYS.size());

        RECORD_KEYS.forEach(key -> {
                result.add(Arguments.of(key));
        });

        return result;
    }


    @ParameterizedTest
    @MethodSource("getRecordCombinations")
    public void testWhyEntitiesDefaults(SzRecordKey recordKey1,
                                        SzRecordKey recordKey2)
    {
        this.performTest(() -> {
            try {
                SzEntityLookup lookup = this.getTestData().getEntityLookup();

                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                long entityId1 = lookup.getMapByRecordKey().get(recordKey1);
                long entityId2 = lookup.getMapByRecordKey().get(recordKey2);

                String defaultResult = engine.whyEntities(entityId1, entityId2);

                String explicitResult = engine.whyEntities(
                    entityId1, entityId2, SZ_WHY_ENTITIES_DEFAULT_FLAGS);
                
                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().whyEntities(
                    entityId1, entityId2, sb);
                    
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
                fail("Unexpectedly failed analyzing why entities", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getRecordKeyParameters")
    public void testWhyRecordInEntityDefaults(SzRecordKey recordKey) {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String dataSourceCode = recordKey.dataSourceCode();
                String recordID = recordKey.recordId();

                String defaultResult = engine.whyRecordInEntity(recordKey);
                
                String explicitResult = engine.whyRecordInEntity(
                        recordKey, SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS);

                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().whyRecordInEntity(
                    dataSourceCode, recordID, sb);
                    
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
                fail("Unexpectedly failed analyzing why record in entity", e);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("getRecordCombinations")
    public void TestWhyRecordsDefaults(SzRecordKey recordKey1,
                                       SzRecordKey recordKey2)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String dataSourceCode1 = recordKey1.dataSourceCode();
                String recordID1 = recordKey1.recordId();

                String dataSourceCode2 = recordKey2.dataSourceCode();
                String recordID2 = recordKey2.recordId();

                String defaultResult = engine.whyRecords(recordKey1, recordKey2);
                
                String explicitResult = engine.whyRecords(recordKey1,
                                                          recordKey2,
                                                          SZ_WHY_RECORDS_DEFAULT_FLAGS);

                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().whyRecords(
                    dataSourceCode1,
                    recordID1,
                    dataSourceCode2,
                    recordID2,
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
}
