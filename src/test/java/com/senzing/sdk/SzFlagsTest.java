package com.senzing.sdk;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.sdk.test.AbstractTest;
import com.senzing.util.SemanticVersion;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.sdk.SzFlagsMetaData.SzFlagMetaData;
import static com.senzing.sdk.SzFlagTestUtilities.*;
import static com.senzing.sdk.Utilities.*;

@TestInstance(Lifecycle.PER_CLASS)
public class SzFlagsTest extends AbstractTest {
    /**
     * The {@link Map} of {@link String} SDK constant names to their
     * values.
     */
    private Map<String, Long> sdkFlagMap = new LinkedHashMap<>();

    /**
     * The {@link SzFlagsMetaData} describing all the flags.
     */
    private SzFlagsMetaData flagsMetaData = null;

    /**
     * The {@link SemanticVersion} for the installed Senzing runtime.
     */
    private SemanticVersion senzingVersion = null;

    @BeforeAll
    public void reflectFlags() {
        this.beginTests();
        try {
            this.flagsMetaData = new SzFlagsMetaData();
        } catch (Exception e) {
            fail(e);
        }

        try {
            this.senzingVersion = getSenzingBuildVersion();
        } catch (Exception e) {
            fail("Failed to get Senzing version", e);
        }

        Field[] fields = SzFlags.class.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (field.getType() != Long.TYPE) continue;
            if (!Modifier.isStatic(modifiers)) continue;
            if (!Modifier.isFinal(modifiers)) continue;
            if (!field.getName().startsWith("SZ_")) continue;

            try {
                this.sdkFlagMap.put(field.getName(), field.getLong(null));

            } catch (IllegalAccessException e) {
                fail("Got exception in reflection.", e);
            }
        }
    }

    @AfterAll
    public void complete() {
        this.endTests();
    }

    private List<Arguments> getSdkMappings() {
        List<Arguments> results = new ArrayList<>(this.sdkFlagMap.size());
        this.sdkFlagMap.forEach((key, value) -> {
            results.add(Arguments.of(key, value));
        });
        return results;
    }

    private List<Arguments> getMetaMappings() {
        Map<String, SzFlagMetaData> metaMap = this.flagsMetaData.getFlags();

        List<Arguments> results = new ArrayList<>(metaMap.size());
        metaMap.values().forEach((metaData) -> {
            results.add(Arguments.of(metaData.getSymbol(), metaData.getValue()));
        });
        return results;
    }


    @ParameterizedTest
    @MethodSource("getMetaMappings")
    public void testMetaFlag(String flagName, long value) {
        this.performTest(() -> {
            assertTrue(this.sdkFlagMap.containsKey(flagName),
                "SDK flag constant (" + flagName +") not found for "
                + "meta flag constant (" + flagName + ")");
            Long sdkValue = this.sdkFlagMap.get(flagName);
            assertEquals(value, sdkValue,
                "SDK flag constant (" + flagName +") has different value ("
                + hexFormat(sdkValue) + ") than meta flag constant ("
                + flagName + "): " + hexFormat(value));
        });
    }

    @ParameterizedTest
    @MethodSource("getSdkMappings")
    public void testSdkFlag(String flagName, long value) {
        this.performTest(() -> {
            if (!flagName.equals("SZ_REDO_DEFAULT_FLAGS")
                && !flagName.equals("SZ_WITH_INFO_FLAGS"))
            {
                SzFlagMetaData metaData = this.flagsMetaData.getFlag(flagName);
                if (metaData == null
                    && getSinceVersion(flagName).compareTo(
                        this.senzingVersion) > 0)
                {
                    return;
                }
                assertNotNull(metaData, "SzFlags flag constant (" + flagName
                            + ") not found in meta data");

                assertEquals(metaData.getValue(), value,
                "SzFlags flag constant (" + flagName +") has different value ("
                    + hexFormat(value) + ") than value found in meta-data: "
                    + hexFormat(metaData.getValue()));
            }
        });
    }
}
