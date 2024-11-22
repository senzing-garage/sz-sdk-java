package com.senzing.sdk.core;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Collections;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzFlagUsageGroup;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.senzing.sdk.core.AbstractTest.*;
import static com.senzing.sdk.core.Utilities.hexFormat;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzFlagUsageGroupTest {

    private List<SzFlagUsageGroup> getEnumFlagGroups() {
        return Arrays.asList(SzFlagUsageGroup.values());
    }

    private List<Arguments> getToStringParameters() {
        List<SzFlagUsageGroup> groups = getEnumFlagGroups();
        Set<SzFlag> allFlags = EnumSet.allOf(SzFlag.class);

        List<Arguments> result = new LinkedList<>();
        for (SzFlagUsageGroup group: groups) {
            Set<SzFlag> groupFlags = group.getFlags();
            Set<Long>   groupFlagValues = new LinkedHashSet<>();
            for (SzFlag flag: groupFlags) {
                groupFlagValues.add(flag.toLong());
            }

            // create a subset
            Set<SzFlag> subset = new LinkedHashSet<>();
            for (SzFlag flag : groupFlags) {
                if (subset.size() > 2) {
                    break;
                }
                subset.add(flag);
            }

            result.add(Arguments.of(group, groupFlags));
            result.add(Arguments.of(group, subset));
            result.add(Arguments.of(group, Collections.emptySet()));

            Set<SzFlag> sharedFlags = new LinkedHashSet<>();
            Set<SzFlag> mismatched  = new LinkedHashSet<>();
            for (SzFlag flag: allFlags) {
                if (groupFlags.contains(flag)) continue;
                if (groupFlagValues.contains(flag.toLong())) {
                    sharedFlags.add(flag);
                } else {
                    if (mismatched.size() < 5) {
                        mismatched.add(flag);
                    }
                }
            }

            if (sharedFlags.size() > 0) {
                result.add(Arguments.of(group, sharedFlags));
            }
            if (mismatched.size() > 0) {
                result.add(Arguments.of(group, mismatched));
            }
        }
        return result;
    }   

    @ParameterizedTest
    @MethodSource("getEnumFlagGroups")
    void testZeroToString(SzFlagUsageGroup group) {
        String text = group.toString(0L);
        assertNotNull(text, "The result for zero is null");
        assertTrue(text.indexOf("NONE") >= 0, "The result for zero does not contain NONE");
    }

    @ParameterizedTest
    @MethodSource("getToStringParameters")
    void testToString(SzFlagUsageGroup group, Set<SzFlag> flags) {
        long value = 0L;
        for (SzFlag flag: flags) {
            value |= flag.toLong();
        }
        String text = group.toString(value);
        assertNotNull(text, "The group.toString(flag) result is null");
        if (value == 0L) {
            assertTrue(text.indexOf("NONE") >= 0, "The result for zero does not contain NONE");
            return;
        }

        Map<Long, SzFlag> groupFlagMap = new LinkedHashMap<>();
        for (SzFlag flag : group.getFlags()) {
            groupFlagMap.put(flag.toLong(), flag);
        }

        for (SzFlag flag : flags) {
            if (flag.getGroups().contains(group)) {
                assertTrue(text.indexOf(flag.toString()) >= 0,
                    "The flag name is unexpectedly NOT in the result.  group=[ " + group
                        + " ], flag=[ " + flag + " ], flags=[ " + flags + " ]");
            } else if (groupFlagMap.containsKey(flag.toLong())) {
                SzFlag groupFlag = groupFlagMap.get(flag.toLong());
                assertTrue(text.indexOf(groupFlag.toString()) >= 0,
                    "The alternate group flag name is unexpectedly NOT in the result.  group=[ " + group
                        + " ], flag=[ " + flag + " ], groupFlag=[ " + groupFlag
                        + " ], value=[ " + hexFormat(flag.toLong()) + " ], flags=[ " + flags + " ]");   
            } else {
                String hexValue = hexFormat(flag.toLong());
                assertTrue(text.indexOf(hexValue) >= 0,
                    "The flag hex value is unexpectedly NOT in the result.  group=[ " + group
                        + " ], flag=[ " + flag + " ], hexValue=[ " + hexValue
                        + " ], flags=[ " + flags + " ]");   
            }
        }

    }

    @ParameterizedTest
    @MethodSource("getEnumFlagGroups")
    void testGetFlags(SzFlagUsageGroup group) {
        Set<SzFlag> flags = group.getFlags();
        assertNotNull(flags, "Flags for group should not be null: " + group);
        for (SzFlag flag: flags) {
            Set<SzFlagUsageGroup> groups = flag.getGroups();
            assertNotNull(groups, "Groups for flag should not be null: " + flag);
            assertTrue(groups.contains(group),
                "Group (" + group + ") has flag (" + flag + ") but the "
                + "flag does not have the group.  flagsForGroup=[ " + flags
                + " ], groupsForFlag=[ " + groups + "]");
        }
    }
}
