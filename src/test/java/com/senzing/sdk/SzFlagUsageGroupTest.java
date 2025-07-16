package com.senzing.sdk;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.senzing.sdk.Utilities.hexFormat;
import static com.senzing.sdk.SzFlagsMetaData.SzFlagMetaData;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzFlagUsageGroupTest {

    /**
     * The {@link SzFlagsMetaData} describing all the flags.
     */
    private SzFlagsMetaData flagsMetaData = null;

    private List<SzFlagUsageGroup> getEnumFlagGroups() {
        return Arrays.asList(SzFlagUsageGroup.values());
    }

    private List<String> getMetaFlagGroups() {
        return new ArrayList<>(this.flagsMetaData.getGroups());
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

    @BeforeAll
    public void reflectFlags() {
        try {
            this.flagsMetaData = new SzFlagsMetaData();
        } catch (Exception e) {
            fail(e);
        }
    }

    @ParameterizedTest
    @MethodSource("getMetaFlagGroups")
    void testMetaGroupFound(String groupName) {
        try {
            SzFlagUsageGroup group = SzFlagUsageGroup.valueOf(groupName);
            assertNotNull(group, "Group for name was null: " + groupName);
        } catch (Exception e) {
            fail("Could not get group for name: " + groupName, e);
        }
    }

    @ParameterizedTest
    @MethodSource("getEnumFlagGroups")
    void testGroupFoundInMeta(SzFlagUsageGroup group) {
        String groupName = group.name();
        assertTrue(this.flagsMetaData.getGroups().contains(groupName),
                   "Group (" + groupName + ") not found in groups meta data: "
                   + this.flagsMetaData.getGroups());
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
    void testToFlagSetUnrecognized(SzFlagUsageGroup group) {
        // set a value with all the unrecognized bits
        long value = -1L;
        for (SzFlag flag: SzFlag.values()) {
            // remove the flag's bit from the value
            value = value & ~flag.toLong();
        }

        Set<SzFlag> flagSet = group.toFlagSet(value);

        assertTrue(flagSet.isEmpty(), 
                   "The flag set for group (" + group 
                   + ") with unrecognized bits ("
                   + hexFormat(value) + ") was not empty: " 
                   + flagSet);
    }

    @ParameterizedTest
    @MethodSource("getToStringParameters")
    void testToFlagSet(SzFlagUsageGroup group, Set<SzFlag> flags) {
        long value = 0L;
        for (SzFlag flag: flags) {
            value |= flag.toLong();
        }

        Set<SzFlag> flagSet = group.toFlagSet(value);
        assertNotNull(flagSet, "The group.toFlagSet(flag) result is null");
        if (value == 0L) {
            assertTrue(flagSet.size() == 0, "The result for zero is not empty set");
            return;
        }

        Map<Long, SzFlag> groupFlagMap = new LinkedHashMap<>();
        for (SzFlag flag : group.getFlags()) {
            groupFlagMap.put(flag.toLong(), flag);
        }

        for (SzFlag flag : flags) {
            if (flag.getGroups().contains(group)) {
                assertTrue(flagSet.contains(flag),
                    "The flag is unexpectedly NOT in the result set.  group=[ " + group
                        + " ], flag=[ " + flag + " ], flags=[ " + flags + " ]");

            } else if (groupFlagMap.containsKey(flag.toLong())) {
                SzFlag groupFlag = groupFlagMap.get(flag.toLong());
                assertTrue(flagSet.contains(groupFlag),
                    "The alternate group flag is unexpectedly NOT in the result set.  "
                    + "group=[ " + group + " ], flag=[ " + flag + " ], groupFlag=[ "
                    + groupFlag + " ], value=[ " + hexFormat(flag.toLong())
                    + " ], flags=[ " + flags + " ]");

            } else {
                // find the first flag with the same value
                SzFlag altFlag = null;
                for (SzFlag f : SzFlag.values()) {
                    if (f.toLong() == flag.toLong()) {
                        altFlag = f;
                        break;
                    }
                }

                assertTrue(flagSet.contains(altFlag),
                    "The flag value is unexpectedly NOT in the result.  group=[ " + group
                    + " ], flag=[ " + altFlag + " ], origFlag=[ " + flag
                    + " ], flags=[ " + flags + " ]");   
            }
        }

    }

    @ParameterizedTest
    @MethodSource("getEnumFlagGroups")
    void testGetFlags(SzFlagUsageGroup group) {
        Map<String, SzFlagMetaData> flagsMap 
            = this.flagsMetaData.getBaseFlagsByGroup(group.name());

        Set<SzFlag> flags = group.getFlags();
        assertNotNull(flags, "Flags for group should not be null: " + group);

        Set<String> flagNames = new LinkedHashSet<>();
        for (SzFlag flag : flags) {
            flagNames.add(flag.name());
        }
        
        for (String flagName : flagsMap.keySet()) {
            assertTrue(flagNames.contains(flagName),
                       "Flag (" + flagName + ") found in meta data, "
                       + "missing from flag usage group: " + group);
        }
        for (String flagName : flagNames) {
            assertTrue(flagsMap.containsKey(flagName), 
                       "Unexpeted flag (" + flagName + ") found in group, "
                       + "missing from meta: " + group);
        }

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
