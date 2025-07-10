package com.senzing.sdk.core;

import java.util.List;
import java.util.Set;
import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzException;
import com.senzing.sdk.test.SzEngineHowTest;
import com.senzing.sdk.test.SzEntityLookup;
import com.senzing.sdk.test.TestDataLoader;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.sdk.SzFlag.*;
import static com.senzing.sdk.test.SzEngineHowTest.TestData.*;

/**
 * Unit tests for {@link SzCoreEngine} how & virtual entity operations.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreEngineHowTest 
    extends AbstractCoreTest 
    implements SzEngineHowTest
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

    public List<Arguments> getVirtualEntityDefaultParameters()
    {
        List<Arguments> argsList = getVirtualEntityParameters();

        List<Arguments> result = new ArrayList<>(argsList.size());

        argsList.forEach(args -> {
            Object[] arr = args.get();

            // skip the ones that expect an exception
            if (arr[arr.length - 1] != null) return;
            result.add(Arguments.of(arr[0]));

        });

        return result;
    }

    @ParameterizedTest
    @MethodSource("getVirtualEntityDefaultParameters")
    public void testVirtualEntityDefaults(Set<SzRecordKey> recordKeys)
    {
        this.performTest(() -> {
            try {
                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                String defaultResult = engine.getVirtualEntity(recordKeys);

                String explicitResult = engine.getVirtualEntity(
                    recordKeys, SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS);
                    
                String encodedRecordKeys = SzCoreEngine.encodeRecordKeys(recordKeys);

                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().getVirtualEntityByRecordID(
                    encodedRecordKeys, sb);
                
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
            catch (Exception e) {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }

    public List<Arguments> getHowEntityDefaultParameters() {
        List<Arguments> results = new ArrayList<>(RECORD_KEYS.size());
        RECORD_KEYS.forEach(key -> {
            results.add(Arguments.of(key));
        });
        return results;
    }

    @ParameterizedTest
    @MethodSource("getHowEntityDefaultParameters")
    public void testHowEntityDefaults(SzRecordKey recordKey) {
        this.performTest(() -> {
            try {
                SzEntityLookup lookup = this.getTestData().getEntityLookup();

                SzCoreEngine engine = (SzCoreEngine) this.env.getEngine();

                long entityID = lookup.getMapByRecordKey().get(recordKey);

                String defaultResult = engine.howEntity(entityID);

                String explicitResult = engine.howEntity(
                    entityID, SZ_HOW_ENTITY_DEFAULT_FLAGS);
                    
                StringBuffer sb = new StringBuffer();
                int returnCode = engine.getNativeApi().howEntityByEntityID(
                    entityID, sb);

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
            catch (Exception e) {
                fail("Unexpectedly failed getting entity by record", e);
            }
        });
    }
}
