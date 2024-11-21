package com.senzing.sdk.core;

import java.lang.reflect.*;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzFlags;
import com.senzing.sdk.SzFlagUsageGroup;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import static com.senzing.sdk.SzFlag.*;
import static com.senzing.sdk.core.Utilities.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzFlagTest extends AbstractTest {
    /**
     * The {@link Map} of {@link String} constant names from
     * {@link SzFlags} to their values.
     */
    private Map<String, Long> flagsMap = new LinkedHashMap<>();

    /**
     * The {@link Map} of {@link String} constant names from
     * {@link SzFlag} to their values.
     */
    private Map<String, Long> enumsMap = new LinkedHashMap<>();

    /**
     * The {@link Map} of {@link String} field names for declared 
     * constants of {@link Set}'s of {@link SzFlag} instance to the
     * actual {@link Set} of {@link SzFlag} instances.
     */
    private Map<String, Set<SzFlag>> setsMap = new LinkedHashMap<>();

    @BeforeAll
    public void reflectFlags() {
        this.beginTests();
        // initialize the enum classes
        SzFlag.values();
        SzFlagUsageGroup.values();

        Field[] fields = SzFlags.class.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (field.getType() != Long.TYPE) continue;
            if (!Modifier.isStatic(modifiers)) continue;
            if (!Modifier.isFinal(modifiers)) continue;
            if (!field.getName().startsWith("SZ_")) continue;
            
            try {
                this.flagsMap.put(field.getName(), field.getLong(null));

            } catch (IllegalAccessException e) {
                fail("Got exception in reflection.", e);
            }
        }

        // populate the enums
        for (SzFlag flag : SzFlag.values()) {
            this.enumsMap.put(flag.name(), flag.toLong());
        }

        // populate the enum sets
        fields = SzFlag.class.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (field.getType() != Set.class) continue;
            if (!Modifier.isStatic(modifiers)) continue;
            if (!Modifier.isFinal(modifiers)) continue;
            if (!field.getName().startsWith("SZ_")) continue;
            try {
                @SuppressWarnings("unchecked")
                Set<SzFlag> flags = (Set<SzFlag>) field.get(null);
                long value = SzFlag.toLong(flags);
                this.enumsMap.put(field.getName(), value);
                this.setsMap.put(field.getName(), flags);

            } catch (IllegalAccessException e) {
                fail("Got exception in reflection.", e);
            }
        }
    }

    @AfterAll
    public void complete() {
        this.endTests();
    }

    private List<Arguments> getFlagsMappings() {
        List<Arguments> results = new ArrayList<>(this.flagsMap.size());
        this.flagsMap.forEach((key, value) -> {
            results.add(Arguments.of(key, value));
        });
        return results;
    }

    private List<Arguments> getEnumMappings() {
        List<Arguments> results = new ArrayList<>(this.enumsMap.size());
        this.enumsMap.forEach((key, value) -> {
            results.add(Arguments.of(key, value));
        });
        return results;
    }

    private List<SzFlag> getEnumFlags() {
        return Arrays.asList(SzFlag.values());
    }

    @ParameterizedTest
    @MethodSource("getFlagsMappings")
    public void testPrimitiveFlag(String flagName, long value) {
        assertTrue(this.enumsMap.containsKey(flagName),
            "Enum flag constant (" + flagName +") not found for "
            + "native flag constant.");
        Long enumValue = this.enumsMap.get(flagName);
        assertEquals(value, enumValue, 
            "Enum flag constant (" + flagName +") has different value ("
            + hexFormat(enumValue) + ") than native flag constant: " 
            + hexFormat(value));
    }

    @ParameterizedTest
    @MethodSource("getEnumMappings")
    public void testEnumFlag(String name, long value) {
        if (name.endsWith("_ALL_FLAGS")) {
            int length = name.length();
            String prefix = name.substring(0, length - "_ALL_FLAGS".length());
            String groupName = prefix + "_FLAGS";
            SzFlagUsageGroup group = null;
            try {
                group = SzFlagUsageGroup.valueOf(groupName);

            } catch (Exception e) {
                fail("Failed to get SzFlagUsageGroup for ALL_FLAGS set: "
                    + "set=[ " + name + "], group=[ " + groupName + "]");
            }
            long groupValue = SzFlag.toLong(group.getFlags());
            Set<SzFlag> flagSet = null;
            try {
                @SuppressWarnings("unchecked")
                Set<SzFlag> set = (Set<SzFlag>) SzFlag.class.getField(name).get(null);
                flagSet = set;
            } catch (NoSuchFieldException|IllegalAccessException e) {
                fail("Failed to obtain field value: " + name, e);
            }
            assertEquals(value, groupValue, 
                         "Value for group (" + group + ") has a different "
                        + "primitive long value (" + hexFormat(groupValue)
                        + " / " + SzFlag.toString(group.getFlags()) 
                        + ") than expected (" + hexFormat(value) 
                        + " / " + SzFlag.toString(flagSet) + "): " + name);
            Set<SzFlag> set = this.setsMap.get(name);
            assertNotNull(set, "Failed to get Set of SzFlag for field: " + name);
            assertEquals(group.getFlags(), set, 
                "The set of all flags for the group (" + group + ") is not "
                + "equal to the set defined for the declared constant (" 
                + name + ").  expected=[ " + SzFlag.toString(group.getFlags())
                + " ], actual=[ " + SzFlag.toString(set) + " ]");
        } else {
            assertTrue(this.flagsMap.containsKey(name),
                "Primitive long flag constant not found for "
                + "enum flag constant: " + name);
            Long flagsValue = this.flagsMap.get(name);
            assertEquals(value, flagsValue, 
                "Flag constant (" + name +") has a different primitive "
                + "long value (" + hexFormat(flagsValue) 
                + ") than enum flag constant (" + name + "): "
                + hexFormat(value));
        }
    }

    @ParameterizedTest
    @MethodSource("getEnumFlags")
    void testGetGroups(SzFlag flag) {
        Set<SzFlagUsageGroup> groups = flag.getGroups();
        assertNotNull(groups, "Groups for flag should not be null: " + flag);
        for (SzFlagUsageGroup group : groups) {
            Set<SzFlag> flags = group.getFlags();
            assertNotNull(flags, "Flags for group should not be null: " + group);
            assertTrue(flags.contains(flag),
                "Flag (" + flag + ") has group (" + group + ") but the "
                + "group does not have the flag.  groupsForFlag=[ " + groups
                + " ], flagsForGroup=[ " + flags + "]");
        }
    }

    private List<Arguments> getSetToLongParams() {
        List<Arguments> results = new ArrayList<>();
        results.add(Arguments.of(null, 0L));
        results.add(Arguments.of(SZ_NO_FLAGS, 0L));
    
        SzFlag[] flags = SzFlag.values();
        int start = 0;
        for (int loop = 0; loop < 3; loop++) {
            for (int count = 1; count < 10; count++) {
                // initialize the set
                EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);

                // initialize the composite value
                long value = 0L;

                // reset the start if need be
                if (start > flags.length - count) {
                    start = count;
                }
                int end = start + count;

                // loop through flags and add them to the set
                for (int index = start; index < end; index++, start++)
                {
                    SzFlag flag = flags[index];
                    set.add(flag);
                    value |= flag.toLong();
                }

                results.add(Arguments.of(set, value));

                Set<SzFlag> set2 = new LinkedHashSet<>(set);
                set2.add(null);
                results.add(Arguments.of(set2, value));
            }
        }
    
        return results;
    }

    private List<Arguments> getSetToStringParams() {
        List<Arguments> results = new ArrayList<>();
        results.add(Arguments.of(
            null, 
            "{ NONE } [0000 0000 0000 0000]"));
        results.add(Arguments.of(
            SZ_NO_FLAGS, "{ NONE } [0000 0000 0000 0000]"));

        StringBuilder sb = new StringBuilder(300);
        SzFlag[] flags = SzFlag.values();
        int start = 0;
        for (int loop = 0; loop < 3; loop++) {
            for (int count = 1; count < 10; count++) {
                // clear the string builder
                sb.delete(0, sb.length());

                // initialize the set
                EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);

                // initialize the composite value
                long value = 0L;

                // reset the start if need be
                if (start > flags.length - count) {
                    start = count;
                }

                String prefix = "";

                int end = start + count;

                // loop through flags and add them to the set
                for (int index = start; index < end; index++, start++)
                {
                    SzFlag flag = flags[index];
                    set.add(flag);
                    value |= flag.toLong();
                    sb.append(prefix);
                    sb.append(flag.name());
                    prefix = " | ";
                }
                sb.append(" [").append(hexFormat(value)).append("]");

                String expected = sb.toString();
                results.add(Arguments.of(set, expected));
                Set<SzFlag> set2 = new LinkedHashSet<>(set);
                set2.add(null);
                results.add(Arguments.of(set2, expected));
            }
        }

        return results;
    }

    @ParameterizedTest
    @MethodSource("getSetToLongParams")
    void testSetToLong(Set<SzFlag> flagSet, long expected) {
        long actual = SzFlag.toLong(flagSet);
        assertEquals(expected, actual, 
            "toLong(EnumSet<SzFlag>) returned " + hexFormat(actual)
            + " instead of " + hexFormat(expected) + ": " + flagSet);
    }

    @ParameterizedTest
    @MethodSource("getSetToStringParams")
    void testSetToString(Set<SzFlag> flagSet, String expected) {
        String actual = SzFlag.toString(flagSet);
        assertEquals(expected, actual, 
            "toString(EnumSet<SzFlag>) did not return as expected: "
            + flagSet);
    }

    private List<Arguments> getFlagSetIntersections() {
        List<Arguments> result = new LinkedList<>();
        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES,SZ_ENTITY_INCLUDE_FEATURE_STATS),
            EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME,SZ_ENTITY_INCLUDE_RECORD_DATA),
            EnumSet.noneOf(SzFlag.class),
            false));

        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES),
            EnumSet.noneOf(SzFlag.class),
            EnumSet.noneOf(SzFlag.class),
            false));
        
        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES),
            null,
            EnumSet.noneOf(SzFlag.class),
            false));

        result.add(Arguments.of(
            null,
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES),
            EnumSet.noneOf(SzFlag.class),
            false));

        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_FEATURE_STATS),
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES,SZ_ENTITY_INCLUDE_FEATURE_STATS),
            EnumSet.of(SZ_ENTITY_INCLUDE_FEATURE_STATS),
            true));
        
        result.add(Arguments.of(
            EnumSet.allOf(SzFlag.class),
            EnumSet.of(SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS,SZ_ENTITY_INCLUDE_ENTITY_NAME),
            EnumSet.of(SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS,SZ_ENTITY_INCLUDE_ENTITY_NAME),
            true));
        
        return result;
    }

    @ParameterizedTest
    @MethodSource("getFlagSetIntersections")
    void testIntersects(Set<SzFlag> set1,
                        Set<SzFlag> set2,
                        Set<SzFlag> intersection,
                        boolean     intersects) 
    {
        boolean actual = SzFlag.intersects(set1, set2);
        assertEquals(intersects, actual, "Intersection not as expected: "
                     + "set1=[ " + set1 + " ], set2=[ " + set2 + " ]");
    }

    @ParameterizedTest
    @MethodSource("getFlagSetIntersections")
    void testIntersection(Set<SzFlag>   set1,
                          Set<SzFlag>   set2,
                          Set<SzFlag>   intersection,
                          boolean       intersects) 
    {
        Set<SzFlag> actual = SzFlag.intersect(set1, set2);
        assertEquals(intersection, actual, "Intersection not as expected: "
                     + "set1=[ " + set1 + " ], set2=[ " + set2 + " ]");
    }

    private List<Arguments> getFlagSetUnions() {
        List<Arguments> result = new LinkedList<>();
        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES,SZ_ENTITY_INCLUDE_FEATURE_STATS),
            EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME,SZ_ENTITY_INCLUDE_RECORD_DATA),
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES,SZ_ENTITY_INCLUDE_FEATURE_STATS,
                       SZ_ENTITY_INCLUDE_ENTITY_NAME,SZ_ENTITY_INCLUDE_RECORD_DATA)));

        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES),
            EnumSet.noneOf(SzFlag.class),
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES)));
        
        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES),
            null,
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES)));
        
        result.add(Arguments.of(
            null,
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES),
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES)));

        result.add(Arguments.of(
            EnumSet.of(SZ_ENTITY_INCLUDE_FEATURE_STATS),
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES,SZ_ENTITY_INCLUDE_FEATURE_STATS),
            EnumSet.of(SZ_ENTITY_INCLUDE_ALL_FEATURES,SZ_ENTITY_INCLUDE_FEATURE_STATS)));
        
        result.add(Arguments.of(
            EnumSet.allOf(SzFlag.class),
            EnumSet.of(SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS,SZ_ENTITY_INCLUDE_ENTITY_NAME),
            EnumSet.allOf(SzFlag.class)));
        
        return result;
    }

    @ParameterizedTest
    @MethodSource("getFlagSetUnions")
    void testUnion(Set<SzFlag> set1, Set<SzFlag> set2, Set<SzFlag> union) 
    {
        Set<SzFlag> actual = SzFlag.union(set1, set2);
        assertEquals(union, actual, "Union not as expected: "
                     + "set1=[ " + set1 + " ], set2=[ " + set2 + " ]");
    }
}
