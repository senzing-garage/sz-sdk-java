package com.senzing.sdk.core;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.json.JsonObject;
import javax.json.JsonValue;

import com.senzing.io.IOUtilities;
import com.senzing.nativeapi.InstallLocations;
import com.senzing.util.JsonUtilities;

import javax.json.JsonArray;

import static com.senzing.io.IOUtilities.UTF_8;
import static com.senzing.util.JsonUtilities.*;

/**
 * Loads the flags JSON meta data file and makes its properties available.
 */
public class SzFlagsMetaData {
    private static final String SYMBOL_KEY = "symbol";
    private static final String BITS_KEY = "bits";
    private static final String VALUE_KEY = "value";
    private static final String DEFINITION_KEY = "definition";
    private static final String GROUPS_KEY = "groups";
    private static final String FLAGS_KEY = "flags";
    
    /**
     * Provides the meta data describing a specific flag.
     */
    public static class SzFlagMetaData {
        private final String symbol;
        private final Set<Integer> bits;
        private final long value;
        private final boolean aggregate;
        private final Set<String> definition;
        private final Set<String> groups;
        private final Set<String> flags;

        private SzFlagMetaData(JsonObject jsonObject) {
            // get the symbol
            this.symbol = getString(jsonObject, SYMBOL_KEY);

            // get the bits
            JsonArray bitsArray = getJsonArray(jsonObject, BITS_KEY);
            Set<Integer> bitSet = new LinkedHashSet<>();
            for (int index = 0; index < bitsArray.size(); index++) {
                bitSet.add(getInteger(bitsArray, index));
            }
            this.bits = Collections.unmodifiableSet(bitSet);

            // get the value
            this.value = getLong(jsonObject, VALUE_KEY);

            // get the definition
            JsonValue defValue = getJsonValue(jsonObject, DEFINITION_KEY);
            Set<String> defSet = new LinkedHashSet<>();
            if (defValue instanceof JsonArray) {
                defSet.addAll(getStrings(jsonObject, DEFINITION_KEY));
            } else {
                defSet.add(getString(jsonObject, DEFINITION_KEY));
            }
            this.definition = Collections.unmodifiableSet(defSet);

            // get the groups
            this.groups = Collections.unmodifiableSet(
                new LinkedHashSet<>(getStrings(jsonObject, GROUPS_KEY)));

            // get the sub-flags (if any)
            List<String> subflags = getStrings(jsonObject, FLAGS_KEY);
            this.flags = (subflags == null) 
                ? null 
                : Collections.unmodifiableSet(new LinkedHashSet<>(subflags));

            this.aggregate = (this.flags != null 
                              || (this.definition.size() == 1
                                  && this.value == 0 
                                  && this.groups.size() == 1));
        }

        /**
         * Gets the symbol for this flag.
         * 
         * @return The sumbol for this flag.
         */
        public String getSymbol() {
            return this.symbol;
        }

        /**
         * Gets the 64-bit <code>long</code> value for this flag.
         * 
         * @return The 64-bit <code>long</code> value for this flag.
         */
        public long getValue() {
            return this.value;
        }

        /**
         * Checks if this represents an aggregate flag or a base flag.  Aggregate
         * flags are bitmasks defined as pre-defined defaults and are meant to 
         * contain other flags even if the default sometimes contains no flags.
         * 
         * @return <code>true</code> if this is an aggregate flag, otherwise
         *         <code>false</code>.
         */
        public boolean isAggregate() {
            return this.aggregate;
        }

        /**
         * Gets the <b>unmodifiable</b> {@link Set} of {@link Integer} 
         * bit indices that are active for this flag.
         * 
         * @return The <b>unmodifiable</b> {@link Set} of {@link Integer} 
         *         bit indices that are active for this flag.
         */
        public Set<Integer> getBits() {
            return this.bits;
        }

        /**
         * Gets the <b>unmodifiable</b> {@link Set} of {@link String}
         * definitions for this flag.  If this flag is defined in terms
         * of its actual value, then there is a single member of the form
         * <code>"1 << N"</code> where <code>N</code> is the bit index
         * that is set.  If this flag is defined in terms of one or more
         * other flags then the members of the {@link Set} are the names
         * of those flags that are used to define this flag through
         * bitwise-OR operations.
         * 
         * @return The <b>unmodifiable</b> {@link Set} of {@link String} 
         *         definitions for this flag.
         */
        public Set<String> getDefinition() {
            return this.definition;
        }

        /**
         * Gets the <b>unmodifiable</b> {@link Set} of {@link String} base flag
         * names that identify the single-bit base flags aggregated by this flag,
         * or <code>null</code> if this flag is itself a base flag.
         * <p>
         * <b>NOTE:</b> A flag may still be a base flag even if it is defined 
         * in terms of another flag because some flags have the same underlying
         * value.  Several of the "search" related flags are examples of this as
         * they are defined in terms of the "export" flags that perform a similar
         * function.
         * 
         * @return The <b>unmodifiable</b> {@link Set} of {@link String} base flag
         *         names that identify the single-bit base flags aggregated by this
         *         flag, or <code>null</code> if this flag is itself a base flag.
         */
        public Set<String> getBaseFlags() {
            return this.flags;
        }

        /**
         * Gets the <b>unmodifiable</b> {@link Set} of flag usage group names
         * identifying the usage groups to which this flag is applicable.
         * 
         * @return The <b>unmodifiable</b> {@link Set} of flag usage group
         *         names identifying the usage groups to which this flag is
         *         applicable.
         */
        public Set<String> getGroups() {
            return this.groups;
        }
    }

    private Set<String> groups;

    private Map<String, SzFlagMetaData> flagsByName;

    private Map<String, Map<String, SzFlagMetaData>> flagsByGroup;

    private Map<String, SzFlagMetaData> baseFlagsByName;

    private Map<String, Map<String, SzFlagMetaData>> baseFlagsByGroup;

    private Map<String, SzFlagMetaData> aggrFlagsByName;

    private Map<String, Map<String, SzFlagMetaData>> aggrFlagsByGroup;

    private static File getFlagsMetaDataFile() throws IOException {
        InstallLocations locations = InstallLocations.findLocations();
        File baseDir = locations.getInstallDirectory();
        File sdkDir = new File(baseDir, "sdk");
        return new File(sdkDir, "szflags.json");
    }

    public SzFlagsMetaData() throws IOException {
        this(getFlagsMetaDataFile());
    }

    public SzFlagsMetaData(File flagsFile) throws IOException
    {
        Map<String, SzFlagMetaData> flagsMap = new LinkedHashMap<>();
        String jsonText = IOUtilities.readTextFileAsString(flagsFile, UTF_8);
        JsonArray jsonArray = JsonUtilities.parseJsonArray(jsonText);
        for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
            SzFlagMetaData info = new SzFlagMetaData(jsonObject);
            flagsMap.put(info.symbol, info);
        }
        this.flagsByName = Collections.unmodifiableMap(flagsMap);

        Set<String> groupSet = new TreeSet<>();
        Map<String, SzFlagMetaData> baseMap = new LinkedHashMap<>();
        Map<String, SzFlagMetaData> aggrMap = new LinkedHashMap<>();
        Map<String, Map<String, SzFlagMetaData>> groupMap = new LinkedHashMap<>();
        Map<String, Map<String, SzFlagMetaData>> baseGroupMap = new LinkedHashMap<>();
        Map<String, Map<String, SzFlagMetaData>> aggrGroupMap = new LinkedHashMap<>();
        
        for (SzFlagMetaData fmd : this.flagsByName.values()) {
            if (fmd.isAggregate()) {
                aggrMap.put(fmd.getSymbol(), fmd);
            } else {
                baseMap.put(fmd.getSymbol(), fmd);
            }

            // get the groups
            for (String group : fmd.getGroups()) {
                groupSet.add(group);
                Map<String, SzFlagMetaData> groupFlags = groupMap.get(group);
                if (groupFlags == null) {
                    groupFlags = new LinkedHashMap<>();
                    groupMap.put(group, groupFlags);
                }
                groupFlags.put(fmd.getSymbol(), fmd);
                
                Map<String, Map<String, SzFlagMetaData>> parentMap = null;
                if (fmd.isAggregate()) {
                    parentMap = aggrGroupMap;
                } else {
                    parentMap = baseGroupMap;
                }

                groupFlags = parentMap.get(group);
                if (groupFlags == null) {
                    groupFlags = new LinkedHashMap<>();
                    parentMap.put(group, groupFlags);
                }
                groupFlags.put(fmd.getSymbol(), fmd);           
            }
        }

        //  make all sub-maps unmodifiable
        List<Map<String, Map<String, SzFlagMetaData>>> parentMaps
            = List.of(groupMap, baseGroupMap, aggrGroupMap);
        
        for (Map<String, Map<String, SzFlagMetaData>> parent : parentMaps) {
            for (Map.Entry<String, Map<String, SzFlagMetaData>> entry : parent.entrySet())
            {
                Map<String, SzFlagMetaData> map = entry.getValue();
                map = Collections.unmodifiableMap(map);
                entry.setValue(map);
            }
        }

        // handle groups that have no flags
        Map<String, SzFlagMetaData> emptyMap = Collections.emptyMap();
        for (String group : groupSet) {
            if (!groupMap.containsKey(group)) {
                groupMap.put(group, emptyMap);
            }
            if (!baseGroupMap.containsKey(group)) {
                baseGroupMap.put(group, emptyMap);
            }
            if (!aggrGroupMap.containsKey(group)) {
                aggrGroupMap.put(group, emptyMap);
            }
        }

        this.groups             = Collections.unmodifiableSet(new LinkedHashSet<>(groupSet));
        this.baseFlagsByName    = Collections.unmodifiableMap(baseMap);
        this.aggrFlagsByName    = Collections.unmodifiableMap(aggrMap);
        this.flagsByGroup       = Collections.unmodifiableMap(groupMap);
        this.baseFlagsByGroup   = Collections.unmodifiableMap(baseGroupMap);
        this.aggrFlagsByGroup   = Collections.unmodifiableMap(aggrGroupMap);
    }

    /**
     * Gets an <b>unmodifiable</b> {@link Map} of {@link String} flag name
     * keys to {@link SzFlagMetaData} values for all flags.
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link String} flag
     *         name keys to {@link SzFlagMetaData} values for all flags.
     */
    public Map<String, SzFlagMetaData> getFlags() {
        return this.flagsByName;
    }

    /**
     * Gets the {@link SzFlagMetaData} for the flag with the specified 
     * name.  This returns <code>null</code> if the specified name is not
     * recognized.
     * 
     * @returns The {@link SzFlagMetaData} for the flag with the specified
     *          name, or <code>null</code> if the specified name is not 
     *          recognized.
     */
    public SzFlagMetaData getFlag(String name) {
        return this.flagsByName.get(name);
    }

    /**
     * Gets an <b>unmodifiable</b> {@link Set} of {@link String} group names
     * for all groups that are found for the flags.
     * 
     * @return An <b>unmodifiable</b> {@link Set} of {@link String} group
     *         names for all groups that are found for the flags.
     */
    public Set<String> getGroups() {
        return this.groups;
    }

    /**
     * Gets an <b>unmodifiable</b> {@link Map} of {@link String} flag name
     * keys to {@link SzFlagMetaData} values for all flags belonging to 
     * the group identified by the specified name.  If the specified group
     * name is not recognized then <code>null</code> is returned.
     * 
     * @param group The group name identifying the group.
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link String} flag
     *         name keys to {@link SzFlagMetaData} values for all flags
     *         belonging to the group identified by the specified name,
     *         or <code>null</code> if the specified group name is not 
     *         recognized.
     */
    public Map<String, SzFlagMetaData> getFlagsByGroup(String group) {
        return this.flagsByGroup.get(group);
    }

    /**
     * Gets an <b>unmodifiable</b> {@link Map} of {@link String} flag name
     * keys to {@link SzFlagMetaData} values for all base flags (i.e.: those
     * flags that do <b>not</b> have composite base flags).
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link String} flag
     *         name keys to {@link SzFlagMetaData} values for all base flags.
     */
    public Map<String, SzFlagMetaData> getBaseFlags() {
        return this.baseFlagsByName;
    }

    /**
     * Gets an <b>unmodifiable</b> {@link Map} of {@link String} flag name
     * keys to {@link SzFlagMetaData} values for all aggregate flags (i.e.:
     * those flags that do have composite base flags).
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link String} flag
     *         name keys to {@link SzFlagMetaData} values for all aggregate
     *         flags.
     */
    public Map<String, SzFlagMetaData> getAggregateFlags() {
        return this.aggrFlagsByName;
    }

    /**
     * Gets an <b>unmodifiable</b> {@link Map} of {@link String} flag name
     * keys to {@link SzFlagMetaData} values for all base flags belonging
     * to the group identified by the specified name.  Base flags are those
     * that do <b>not</b> have composite base flags.  If the specified group
     * name is not recognized then <code>null</code> is returned.
     * 
     * @param group The group name identifying the group.
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link String} flag
     *         name keys to {@link SzFlagMetaData} values for all base 
     *         flags belonging to the group identified by the specified
     *         name, or <code>null</code> if the specified group name is
     *         not recognized.
     */
    public Map<String, SzFlagMetaData> getBaseFlagsByGroup(String group) {
        return this.baseFlagsByGroup.get(group);
    }

    /**
     * Gets an <b>unmodifiable</b> {@link Map} of {@link String} flag name
     * keys to {@link SzFlagMetaData} values for all aggregate flags
     * belonging to the group identified by the specified name.  Aggregate
     * flags are those that do have composite base flags.  If the specified
     * group name is not recognized then <code>null</code> is returned.
     * 
     * @param group The group name identifying the group.
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link String} flag
     *         name keys to {@link SzFlagMetaData} values for all aggregate
     *         flags belonging to the group identified by the specified
     *         name, or <code>null</code> if the specified group name is
     *         not recognized.
     */
    public Map<String, SzFlagMetaData> getAggregateFlagsByGroup(String group) {
        return this.aggrFlagsByGroup.get(group);
    }
}
