package com.senzing.sdk.test;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;

import com.senzing.sdk.SzRecordKey;

import static java.util.Collections.*;

/**
 * Provides a hub for looking up entity ID's by {@link SzRecordKey}
 * and {@link SzRecordKey}'s by entity ID.
 */
public class SzEntityLookup {
    /**
     * The <b>unmodifiable</b> {@link Map} of {@link SzRecordKey}
     * keys to {@link Long} entity ID values.
     */
    private Map<SzRecordKey, Long> recordMap;

    /**
     * An <b>unmodifiable</b> {@link Map} of {@link Long}
     * entity ID keys to <b>unmodifiable</b> {@link Set} values
     * containing {@link SzRecordKey} instances identifying the
     * records for the respective entity.
     */
    private Map<Long, Set<SzRecordKey>> entityMap;

    /**
     * Constructs with the specified {@link Map} of {@link SzRecordKey}
     * keys to {@link Long} entity ID values.
     * 
     * @return recordMap The {@link Map} of {@link SzRecordKey} keys
     *                   to {@link Long} entity ID values.
     */
    public SzEntityLookup(Map<SzRecordKey, Long> recordMap) {
        this.recordMap = unmodifiableMap(new LinkedHashMap<>(recordMap));
        this.entityMap = new LinkedHashMap<>();
        this.recordMap.forEach((recordKey, entityId) -> {
            Set<SzRecordKey> recordKeys = this.entityMap.get(entityId);
            if (recordKeys == null) {
                recordKeys = new LinkedHashSet<>();
                this.entityMap.put(entityId, recordKeys);
            }
            recordKeys.add(recordKey);
        });

        for (Map.Entry<Long,Set<SzRecordKey>> entry : this.entityMap.entrySet())
        {
            Set<SzRecordKey> set = entry.getValue();
            entry.setValue(unmodifiableSet(set));
        }
        this.entityMap = unmodifiableMap(this.entityMap);
    }
    
    /**
     * Returns an <b>unmodifiable</b> {@link Map} of {@link SzRecordKey}
     * keys to {@link Long} entity ID values.
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link SzRecordKey}
     *         keys to {@link Long} entity ID values.
     */
    public Map<SzRecordKey, Long> getMapByRecordKey() {
        return this.recordMap;
    }

    /**
     * Returns an <b>unmodifiable</b> {@link Map} of {@link Long}
     * entity ID keys to <b>unmodifiable</b> {@link Set} values
     * containing {@link SzRecordKey} instances identifying the
     * records for the respective entity.
     * 
     * @return An <b>unmodifiable</b> {@link Map} of {@link Long}
     *         entity ID keys to <b>unmodifiable</b> {@link Set}
     *         values containing {@link SzRecordKey} instances.
     */
    public Map<Long, Set<SzRecordKey>> getMapByEntityId() {
        return this.entityMap;
    }
    
}
