package com.senzing.sdk.core;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzFlags;
import com.senzing.sdk.core.SzFlagsMetaData.SzFlagMetaData;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import static com.senzing.sdk.core.Utilities.*;
import static com.senzing.sdk.core.SzFlagsMetaData.SzFlagMetaData;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzFlagsTest extends AbstractTest {
    /**
     * The {@link Map} of {@link String} native constant names to their
     * values.
     */
    private Map<String, Long> nativeFlagMap = new LinkedHashMap<>();

    /**
     * The {@link Map} of {@link String} SDK constant names to their
     * values.
     */
    private Map<String, Long> sdkFlagMap = new LinkedHashMap<>();

    /**
     * The {@link SzFlagsMetaData} describing all the flags.
     */
    private SzFlagsMetaData flagsMetaData = null;

    @BeforeAll
    public void reflectFlags() {
        try {
            this.flagsMetaData = new SzFlagsMetaData();
        } catch (Exception e) {
            fail(e);
        }

        Field[] fields = NativeEngine.class.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (field.getType() != Long.TYPE) continue;
            if (!Modifier.isStatic(modifiers)) continue;
            if (!Modifier.isFinal(modifiers)) continue;
            if (!field.getName().startsWith("SZ_")) continue;
            
            try {
                this.nativeFlagMap.put(field.getName(), field.getLong(null));

            } catch (IllegalAccessException e) {
                fail("Got exception in reflection.", e);
            }
        }

        fields = SzFlags.class.getDeclaredFields();
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

    private List<Arguments> getNativeMappings() {
        List<Arguments> results = new ArrayList<>(this.nativeFlagMap.size());
        this.nativeFlagMap.forEach((key, value) -> {
            results.add(Arguments.of(key, value));
        });
        return results;
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
    @MethodSource("getNativeMappings")
    public void testNativeFlag(String flagName, long value) {
        assertTrue(this.sdkFlagMap.containsKey(flagName),
            "SDK flag constant (" + flagName +") not found for "
            + "native flag constant (" + flagName + ")");
        Long sdkValue = this.sdkFlagMap.get(flagName);
        assertEquals(value, sdkValue, 
            "SDK flag constant (" + flagName +") has different value ("
            + hexFormat(sdkValue) + ") than native flag constant (" 
            + flagName + "): " + hexFormat(value));
        SzFlagMetaData metaData = this.flagsMetaData.getFlag(flagName);
        assertNotNull(metaData, "Native flag constant (" + flagName 
                      + ") not found in meta data");
        assertEquals(metaData.getValue(), value,
           "Native flag constant (" + flagName +") has different value ("
            + hexFormat(value) + ") than value found in meta-data: "
            + hexFormat(metaData.getValue()));
    }

    @ParameterizedTest
    @MethodSource("getMetaMappings")
    public void testMetaFlag(String flagName, long value) {
        assertTrue(this.sdkFlagMap.containsKey(flagName),
            "SDK flag constant (" + flagName +") not found for "
            + "meta flag constant (" + flagName + ")");
        Long sdkValue = this.sdkFlagMap.get(flagName);
        assertEquals(value, sdkValue, 
            "SDK flag constant (" + flagName +") has different value ("
            + hexFormat(sdkValue) + ") than meta flag constant (" 
            + flagName + "): " + hexFormat(value));

        if (!flagName.equals("SZ_WITH_INFO")) {
            assertTrue(this.nativeFlagMap.containsKey(flagName),
                "Meta flag constant (" + flagName +") not found for "
                + "native flag constant (" + flagName + ")");
            Long nativeValue = this.nativeFlagMap.get(flagName);
            assertEquals(value, nativeValue, 
                "Native flag constant (" + flagName +") has different value ("
                + hexFormat(nativeValue) + ") than meta flag constant (" 
                + flagName + "): " + hexFormat(value));
        } else {
            assertFalse(this.nativeFlagMap.containsKey(flagName),
                "Meta flag constant (" + flagName + ") unexpectedly found for "
                + "in the nativd flag constants.");
        }
    }

    @ParameterizedTest
    @MethodSource("getSdkMappings")
    public void testSdkFlag(String flagName, long value) {
        String nativeFlagName = flagName;

        if (!flagName.equals("SZ_NO_FLAGS")
            && !flagName.equals("SZ_WITH_INFO_FLAGS"))
        {
            SzFlagMetaData metaData = this.flagsMetaData.getFlag(flagName);
            assertNotNull(metaData, "SzFlags flag constant (" + flagName 
                        + ") not found in meta data");

            assertEquals(metaData.getValue(), value,
            "SzFlags flag constant (" + flagName +") has different value ("
                + hexFormat(value) + ") than value found in meta-data: "
                + hexFormat(metaData.getValue()));
        }

        if (nativeFlagName.equals("SZ_WITH_INFO") 
            || nativeFlagName.equals("SZ_NO_FLAGS")
            || nativeFlagName.equals("SZ_WITH_INFO_FLAGS"))
        {
            assertFalse(this.nativeFlagMap.containsKey(nativeFlagName),
                "Native flags unexpectedly includes " + nativeFlagName);
        
        } else {
            assertTrue(this.nativeFlagMap.containsKey(nativeFlagName),
                "Native flag constant (" + nativeFlagName +") not found for "
                + "SDK flag constant (" + flagName + ")");
            Long nativeValue = this.nativeFlagMap.get(nativeFlagName);
            assertEquals(value, nativeValue, 
                "Native flag constant (" + nativeFlagName +") has different value ("
                + hexFormat(nativeValue) + ") than SDK flag constant (" 
                + flagName + "): " + hexFormat(value));
        }
    }
}
