package com.senzing.sdk.core;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzFlags;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static com.senzing.sdk.core.Utilities.*;

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


    @BeforeAll
    public void reflectFlags() {
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

    @ParameterizedTest
    @MethodSource("getNativeMappings")
    public void testNativeFlag(String flagName, long value) {
        String sdkFlagName = "SZ" + flagName.substring(2);
        assertTrue(this.sdkFlagMap.containsKey(sdkFlagName),
            "SDK flag constant (" + sdkFlagName +") not found for "
            + "native flag constant (" + flagName + ")");
        Long sdkValue = this.sdkFlagMap.get(sdkFlagName);
        assertEquals(value, sdkValue, 
            "SDK flag constant (" + sdkFlagName +") has different value ("
            + hexFormat(sdkValue) + ") than native flag constant (" 
            + flagName + "): " + hexFormat(value));
    }

    @ParameterizedTest
    @MethodSource("getSdkMappings")
    public void testSdkFlag(String flagName, long value) {
        String nativeFlagName = flagName;

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
