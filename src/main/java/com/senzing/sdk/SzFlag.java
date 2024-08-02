package com.senzing.sdk;

import java.util.EnumSet;
import java.util.Set;

import java.util.Collections;

import static com.senzing.sdk.SzFlagHelpers.*;
import static com.senzing.sdk.SzFlagUsageGroup.SZ_GROUP_SET_LOOKUP;
import static com.senzing.sdk.Utilities.hexFormat;

/**
 * Enumerates the Senzing flag values from {@link SzFlags} so they can be
 * referred to as objects, used in {@link EnumSet} instances and coverted
 * to sensible {@link String}'s with symbolic names and hexadecimal values.
 * <p>
 * Each {@link SzFlag} belongs to one or more {@link SzFlagUsageGroup}
 * instances which can be obtained via {@link #getGroups()}.  This helps
 * in identifying which flags are applicable to which functions since a
 * function will document which {@link SzFlagUsageGroup} to refer to for
 * applicable flags.  Passing an {@link SzFlag} to a function to which
 * it does not apply will either have no effect or activate an applicable
 * {@link SzFlag} that happens to have the same bitwise value as the
 * non-applicable {@link SzFlag}.
 * 
 * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
 */
public enum SzFlag {
    /**
     * Indicates that the Senzing engine should produce and return the INFO
     * document describing the affected entities from an operation.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_MODIFY} 
     * </ul>
     */
    SZ_WITH_INFO(SzFlags.SZ_WITH_INFO, SZ_MODIFY_SET),

    /**
     * The value for export functionality to indicate that we should include
     * "resolved" relationships.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES(
        SzFlags.SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES, SZ_EXPORT_SET),
    
    /**
     * The value for export functionality toindicate that we should include
     * "possibly same" relationships.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_POSSIBLY_SAME(
        SzFlags.SZ_EXPORT_INCLUDE_POSSIBLY_SAME, SZ_EXPORT_SET),
    
    /**
     * The value for export functionality to indicate that we should include
     * "possibly related" relationships.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_POSSIBLY_RELATED(
        SzFlags.SZ_EXPORT_INCLUDE_POSSIBLY_RELATED, SZ_EXPORT_SET),
    
    /**
     * The value for export functionality to indicate that we should include
     * "name only" relationships.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_NAME_ONLY(
        SzFlags.SZ_EXPORT_INCLUDE_NAME_ONLY, SZ_EXPORT_SET),

    /**
     * The value for export functionality to indicate that we should include
     * "disclosed" relationships.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_DISCLOSED(
        SzFlags.SZ_EXPORT_INCLUDE_DISCLOSED, SZ_EXPORT_SET),

    /**
     * The value for export functionality to indicate that we should include
     * "disclosed" relationships.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES(
        SzFlags.SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES, SZ_EXPORT_SET),
    
    /**
     * The value for including possibly-same relations for entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS(
        SzFlags.SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS, SZ_RELATION_SET),

    /**
     * The value for including possibly-related relations for entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS(
        SzFlags.SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS, SZ_RELATION_SET),

    /**
     * The value for including name-only relations for entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS(
        SzFlags.SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS, SZ_RELATION_SET),

    /**
     * The value for including disclosed relations for entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS(
        SzFlags.SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS, SZ_RELATION_SET),

    /**
     * The value for including all features for entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_ALL_FEATURES(
        SzFlags.SZ_ENTITY_INCLUDE_ALL_FEATURES, SZ_ENTITY_SET),
    
    /**
     * The value for including representative features for entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES(
        SzFlags.SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES, SZ_ENTITY_SET),
    
    /**
     * The value for including the name of the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_ENTITY_NAME(
        SzFlags.SZ_ENTITY_INCLUDE_ENTITY_NAME, SZ_ENTITY_SET),
    
    /**
     * The value for including the record summary of the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_SUMMARY(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_SUMMARY, SZ_ENTITY_SET),
    
    /**
     * The value for including the record types of the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD} 
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_TYPES(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_TYPES, SZ_ENTITY_RECORD_SET),

    /**
     * The value for including the basic record data for the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_DATA(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_DATA, SZ_ENTITY_SET),
    
    /**
     * The value for including the record matching info for the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD} 
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO, SZ_ENTITY_RECORD_SET),

    /**
     * The value for including the record json data for the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD} 
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_JSON_DATA(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_JSON_DATA, SZ_ENTITY_RECORD_SET),

    /**
     * The value for including the record unmapped data for the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD} 
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA, SZ_ENTITY_RECORD_SET),

    /**
     * The value for the features identifiers for the records.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_FEATURE_IDS(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_FEATURE_IDS, SZ_ENTITY_SET),

    /**
     * The value for including the name of the related entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME(
        SzFlags.SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME, SZ_RELATION_SET),

    /**
     * The value for including the record matching info of the related
     * entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO(
        SzFlags.SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO, SZ_RELATION_SET),

    /**
     * The value for including the record summary of the related entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY(
        SzFlags.SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY, SZ_RELATION_SET),

    /**
     * The value for including the record types of the related entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES(
        SzFlags.SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES, SZ_RELATION_SET),

    /**
     * The value for including the basic record data of the related
     * entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA(
        SzFlags.SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA, SZ_RELATION_SET),

    /**
     * The value for including internal features in entity output.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_INTERNAL_FEATURES(
        SzFlags.SZ_ENTITY_INCLUDE_INTERNAL_FEATURES, SZ_ENTITY_SET),

    /**
     * The value for including feature statistics in entity output.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_FEATURE_STATS(
        SzFlags.SZ_ENTITY_INCLUDE_FEATURE_STATS, SZ_ENTITY_SET),

    /**
     * The value for including feature elements.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_FEATURE_ELEMENTS(
        SzFlags.SZ_ENTITY_INCLUDE_FEATURE_ELEMENTS, SZ_ENTITY_SET),

    /**
     * The value for including internal features.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_HOW}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_INCLUDE_MATCH_KEY_DETAILS(
        SzFlags.SZ_INCLUDE_MATCH_KEY_DETAILS, SZ_ENTITY_HOW_SET),

    /**
     * The value for find-path functionality to indicate that avoided entities
     * must be strictly avoided even if using an avoided entity would be the 
     * only means to find a path between two entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_FIND_PATH_STRICT_AVOID(
        SzFlags.SZ_FIND_PATH_STRICT_AVOID, SZ_FIND_PATH_SET),

    /**
     * The value for find-path functionality to include matching info on entity paths.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_FIND_PATH_INCLUDE_MATCHING_INFO(
        SzFlags.SZ_FIND_PATH_INCLUDE_MATCHING_INFO, SZ_FIND_PATH_SET),

    /**
     * The value for find-network functionality to include matching info on the
     * entity paths of the network.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO(
        SzFlags.SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO, SZ_FIND_NETWORK_SET),

    /**
     * The value for including feature scores.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     *    <li>{@link SzFlagUsageGroup#SZ_HOW}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_INCLUDE_FEATURE_SCORES(
        SzFlags.SZ_INCLUDE_FEATURE_SCORES, SZ_HOW_WHY_SET),

    /**
     * The value for including statistics from search results.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_STATS(
        SzFlags.SZ_SEARCH_INCLUDE_STATS, SZ_SEARCH_SET),
    
    /**
     * The value for search functionality to indicate that we should
     * include "resolved" match level results.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_RESOLVED(
        SzFlags.SZ_SEARCH_INCLUDE_RESOLVED, SZ_SEARCH_SET),
    
    /**
     * The value for search functionality to indicate that we should
     * include "possibly same" match level results.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_POSSIBLY_SAME(
        SzFlags.SZ_SEARCH_INCLUDE_POSSIBLY_SAME, SZ_SEARCH_SET),

    /**
     * The value for search functionality to indicate that we should
     * include "possibly related" match level results.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_POSSIBLY_RELATED(
        SzFlags.SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, SZ_SEARCH_SET),

    /**
     * The value for search functionality to indicate that we should
     * include "name only" match level results.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_NAME_ONLY(
        SzFlags.SZ_SEARCH_INCLUDE_NAME_ONLY, SZ_SEARCH_SET);

    /**
     * An <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances 
     * containing <b>NO</b> flags (an empty set).
     */
    public static final Set<SzFlag> SZ_NO_FLAGS 
        = Collections.unmodifiableSet(EnumSet.noneOf(SzFlag.class));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the {@link 
     * SzFlagUsageGroup#SZ_MODIFY} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    public static final Set<SzFlag> SZ_MODIFY_ALL_FLAGS
        = SzFlagUsageGroup.SZ_MODIFY.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_RECORD} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_RECORD
     */
    public static final Set<SzFlag> SZ_RECORD_ALL_FLAGS
        = SzFlagUsageGroup.SZ_RECORD.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_ENTITY} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_ENTITY
     */
    public static final Set<SzFlag> SZ_ENTITY_ALL_FLAGS
        = SzFlagUsageGroup.SZ_ENTITY.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_FIND_PATH} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     */
    public static final Set<SzFlag> SZ_FIND_PATH_ALL_FLAGS
        = SzFlagUsageGroup.SZ_FIND_PATH.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_FIND_NETWORK} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK
     */
    public static final Set<SzFlag> SZ_FIND_NETWORK_ALL_FLAGS
        = SzFlagUsageGroup.SZ_FIND_NETWORK.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_SEARCH} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_SEARCH
     */
    public static final Set<SzFlag> SZ_SEARCH_ALL_FLAGS
        = SzFlagUsageGroup.SZ_SEARCH.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_EXPORT} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_EXPORT
     */
    public static final Set<SzFlag> SZ_EXPORT_ALL_FLAGS
        = SzFlagUsageGroup.SZ_EXPORT.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_WHY} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_WHY
     */
    public static final Set<SzFlag> SZ_WHY_ALL_FLAGS
        = SzFlagUsageGroup.SZ_WHY.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_HOW} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_HOW
     */
    public static final Set<SzFlag> SZ_HOW_ALL_FLAGS
        = SzFlagUsageGroup.SZ_HOW.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_VIRTUAL_ENTITY
     */
    public static final Set<SzFlag> SZ_VIRTUAL_ENTITY_ALL_FLAGS
        = SzFlagUsageGroup.SZ_VIRTUAL_ENTITY.getFlags();

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances containing
     * the {@link SzFlag} instances to use when a repository-modifying operation 
     * is being invoked and the desired repsonse should contain an "INFO" message
     * describing how the repository was affected as a result of the operation.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *     <li>{@link #SZ_WITH_INFO}
     * </ul>
     * <p>
     * All the flags in this {@link Set} belong to the following usage groups:
     * <ul>
     *     <li>{@link SzFlagUsageGroup#SZ_MODIFY}
     * </ul> 
     */
    public static final Set<SzFlag> SZ_WITH_INFO_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_WITH_INFO));
    
    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances indicating
     * that an export should include all entities in the export.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES}
     *  <li>{@link #SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} belong to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     */
    public static final Set<SzFlag> SZ_EXPORT_INCLUDE_ALL_ENTITIES
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES,
                       SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES));

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that 
     * an export should include all entities having a relationships of
     * any kind.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_EXPORT_INCLUDE_POSSIBLY_SAME}
     *  <li>{@link #SZ_EXPORT_INCLUDE_POSSIBLY_RELATED}
     *  <li>{@link #SZ_EXPORT_INCLUDE_NAME_ONLY}
     *  <li>{@link #SZ_EXPORT_INCLUDE_DISCLOSED}
     * </ul>
     * <p>
     * All the flags in this {@link Set} belong to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_EXPORT_INCLUDE_ALL_HAVING_RELATIONSHIPS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_EXPORT_INCLUDE_POSSIBLY_SAME,
                       SZ_EXPORT_INCLUDE_POSSIBLY_RELATED,
                       SZ_EXPORT_INCLUDE_NAME_ONLY,
                       SZ_EXPORT_INCLUDE_DISCLOSED));

    /**
     * The unmodifiable {@link Set} of {@link SzFlag} instances indicating
     * that that entities that are returned should include all relationships
     * regardless of the kind of relationship.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS}
     *  <li>{@link #SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS}
     *  <li>{@link #SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS}
     *  <li>{@link #SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_ENTITY}, and by extension
     * belong to the following usage groups which are super-sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_ENTITY_INCLUDE_ALL_RELATIONS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS,
                       SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS,
                       SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS,
                       SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS));

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that search
     * results should include entities that related to the search attributes
     * at any match level.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_SEARCH_INCLUDE_RESOLVED}
     *  <li>{@link #SZ_SEARCH_INCLUDE_POSSIBLY_SAME}
     *  <li>{@link #SZ_SEARCH_INCLUDE_POSSIBLY_RELATED}
     *  <li>{@link #SZ_SEARCH_INCLUDE_NAME_ONLY}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_SEARCH} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_SEARCH_INCLUDE_ALL_ENTITIES
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_SEARCH_INCLUDE_RESOLVED,
                       SZ_SEARCH_INCLUDE_POSSIBLY_SAME,
                       SZ_SEARCH_INCLUDE_POSSIBLY_RELATED,
                       SZ_SEARCH_INCLUDE_NAME_ONLY));

    /**
     * The {@link Set} of {@link SzFlag} instances for the default level of detail
     * when retrieving records.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the {@link 
     * SzFlagUsageGroup#SZ_RECORD} usage group, and by extension belong to the
     * following groups which are super-sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD} 
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_RECORD_DEFAULT_FLAGS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_ENTITY_INCLUDE_RECORD_JSON_DATA));

    /**
     * The {@link Set} of {@link SzFlag} instances for the default level of detail
     * when retrieving entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_DATA}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_ENTITY}, and by extension
     * belong to the following usage groups which are super-sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_ENTITY_DEFAULT_FLAGS;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_ENTITY_INCLUDE_ALL_RELATIONS);
        set.add(SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES);
        set.add(SZ_ENTITY_INCLUDE_ENTITY_NAME);
        set.add(SZ_ENTITY_INCLUDE_RECORD_SUMMARY);
        set.add(SZ_ENTITY_INCLUDE_RECORD_DATA);
        set.add(SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO);
        set.add(SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME);
        set.add(SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY);
        set.add(SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
        SZ_ENTITY_DEFAULT_FLAGS = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances for the default flags for a 
     * "brief" level of detail when retrieving entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_ENTITY}, and by extension
     * belong to the following usage groups which are super-sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH} 
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH} 
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK} 
     *    <li>{@link SzFlagUsageGroup#SZ_WHY}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_ENTITY_BRIEF_DEFAULT_FLAGS;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_ENTITY_INCLUDE_ALL_RELATIONS);
        set.add(SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO);
        set.add(SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
        SZ_ENTITY_BRIEF_DEFAULT_FLAGS = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances for the default flags for
     * exporting entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_EXPORT_INCLUDE_ALL_ENTITIES}
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_EXPORT} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_EXPORT_DEFAULT_FLAGS;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_EXPORT_INCLUDE_ALL_ENTITIES);
        set.addAll(SZ_ENTITY_DEFAULT_FLAGS);
        SZ_EXPORT_DEFAULT_FLAGS = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that are good defaults
     * for "find path" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_FIND_PATH_INCLUDE_MATCHING_INFO}
     *  <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *  <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_FIND_PATH} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_FIND_PATH_DEFAULT_FLAGS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_FIND_PATH_INCLUDE_MATCHING_INFO,
                       SZ_ENTITY_INCLUDE_ENTITY_NAME,
                       SZ_ENTITY_INCLUDE_RECORD_SUMMARY));

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that are good defaults
     * for "find network" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO}
     *  <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *  <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_FIND_NETWORK} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_FIND_NETWORK_DEFAULT_FLAGS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO,
                       SZ_ENTITY_INCLUDE_ENTITY_NAME,
                       SZ_ENTITY_INCLUDE_RECORD_SUMMARY));


    /**
     * The {@link Set} of {@link SzFlag} instances indicating that are good defaults
     * for "why entities" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_DEFAULT_FLAGS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_WHY} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_WHY_ENTITIES_DEFAULT_FLAGS;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_ENTITY_DEFAULT_FLAGS);
        set.add(SZ_ENTITY_INCLUDE_INTERNAL_FEATURES);
        set.add(SZ_ENTITY_INCLUDE_FEATURE_STATS);
        set.add(SZ_INCLUDE_FEATURE_SCORES);

        SZ_WHY_ENTITIES_DEFAULT_FLAGS = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that are good defaults
     * for "why records" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_DEFAULT_FLAGS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_WHY} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_WHY_RECORDS_DEFAULT_FLAGS;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_ENTITY_DEFAULT_FLAGS);
        set.add(SZ_ENTITY_INCLUDE_INTERNAL_FEATURES);
        set.add(SZ_ENTITY_INCLUDE_FEATURE_STATS);
        set.add(SZ_INCLUDE_FEATURE_SCORES);

        SZ_WHY_RECORDS_DEFAULT_FLAGS = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that are good defaults
     * for "why record in entity" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_DEFAULT_FLAGS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_WHY} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_ENTITY_DEFAULT_FLAGS);
        set.add(SZ_ENTITY_INCLUDE_INTERNAL_FEATURES);
        set.add(SZ_ENTITY_INCLUDE_FEATURE_STATS);
        set.add(SZ_INCLUDE_FEATURE_SCORES);

        SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS = Collections.unmodifiableSet(set);
    }

     /**
     * The {@link Set} of {@link SzFlag} instances indicating that are good defaults
     * for "how entity" operations that analyze how an entity was formed.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_HOW} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_HOW_ENTITY_DEFAULT_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_INCLUDE_FEATURE_SCORES));

     /**
     * The {@link Set} of {@link SzFlag} instances indicating that are good defaults
     * for "how entity" operations that analyze how an entity was formed.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} belong to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY} 
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS
        = SZ_ENTITY_DEFAULT_FLAGS;

    /**
     * The {@link Set} of {@link SzFlag} instances that are a good
     * default when searching for all matching entities regardless 
     * of match level.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_SEARCH_INCLUDE_ALL_ENTITIES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_ALL;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_SEARCH_INCLUDE_ALL_ENTITIES);
        set.add(SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES);
        set.add(SZ_ENTITY_INCLUDE_ENTITY_NAME);
        set.add(SZ_ENTITY_INCLUDE_RECORD_SUMMARY);
        set.add(SZ_INCLUDE_FEATURE_SCORES);
        SZ_SEARCH_BY_ATTRIBUTES_ALL = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances that are a good
     * default when searching foor strongly matching entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_SEARCH_INCLUDE_RESOLVED}
     *   <li>{@link #SZ_SEARCH_INCLUDE_POSSIBLY_SAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_STRONG
        = Collections.unmodifiableSet(EnumSet.of(SZ_SEARCH_INCLUDE_RESOLVED,
                                                 SZ_SEARCH_INCLUDE_POSSIBLY_SAME,
                                                 SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES,
                                                 SZ_ENTITY_INCLUDE_ENTITY_NAME,
                                                 SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                                                 SZ_INCLUDE_FEATURE_SCORES));

    /**
     * The {@link Set} of {@link SzFlag} instances that are a good for searching
     * by attributes when returning minimal data for the entities.
     * <p>
     * This is equivalent to {@link #SZ_SEARCH_INCLUDE_ALL_ENTITIES}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */    
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
        = Collections.unmodifiableSet(EnumSet.copyOf(SZ_SEARCH_INCLUDE_ALL_ENTITIES));

    /**
     * The {@link Set} of {@link SzFlag} instances that are a good for searching by
     * attributes for strong search matches and returning minimal data for the entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_SEARCH_INCLUDE_RESOLVED}
     *   <li>{@link #SZ_SEARCH_INCLUDE_POSSIBLY_SAME}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
        = Collections.unmodifiableSet(EnumSet.of(SZ_SEARCH_INCLUDE_RESOLVED, 
                                                 SZ_SEARCH_INCLUDE_POSSIBLY_SAME));

    /**
     * The {@link Set} of {@link SzFlag} instances that are a good default for
     * searching by attributes and returning basic entity data.
     * <p>
     * This is equivalent to {@link #SZ_SEARCH_BY_ATTRIBUTES_ALL}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */    
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
        = SZ_SEARCH_BY_ATTRIBUTES_ALL;

    static {        
        for (SzFlag flag : SzFlag.values()) {
            flag.groups = SZ_GROUP_SET_LOOKUP.get(flag.groups);
        }
    }
    
    /**
     * The underlying value.
     */
    private final long value;

    /**
     * The {@link EnumSet} containing the {@link SzFlagUsageGroup} instances pertaining
     * to this instance.
     */
    private Set<SzFlagUsageGroup> groups;

    /**
     * Private constructor that takes only the value.
     * 
     * @param value The long integer value representing the flags.
     * @param groups The {@link Set} containing the {@link SzFlagUsageGroup}
     *               instances pertaining to this instance.
     */
    SzFlag(long value, Set<SzFlagUsageGroup> groups) {
        this.value  = value;
        this.groups = groups;
    }

    /**
     * Converts this instance to its 64-bit <code>long</code> integer
     * equivalent representing the bitwise flags.
     * 
     * @return The 64-bit <code>long</code> integer equivalent representing
     *         the bitwise flags.
     */
    public long toLong() {
        return this.value;
    }

    /**
     * Gets the <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances describing which usage groups that this {@link SzFlag}
     * belongs to.  The returned <b>unmodifiable</b> {@link Set} will be
     * backed by an {@link EnumSet}.
     * 
     * @return The <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     *         instances describing which usage groups that this {@link SzFlag}
     *         belongs to.
     */
    public Set<SzFlagUsageGroup> getGroups() {
        return this.groups;
    }

    /**
     * Converts the specified {@link EnumSet} to its 64-bit <code>long</code>
     * integer equivalent representing the bitwise flags.  If <code>null</code>
     * is specified it will be treated as an empty {@link Set}.
     * 
     * @param set The {@link Set} for which the bitwise equivalent is
     *            being requested.
     * 
     * @return The 64-bit <code>long</code> integer equivalent representing
     *         the bitwise flags of the specified {@link Set}.
     */
    public static long toLong(Set<SzFlag> set) {
        if (set == null) {
            return 0L;
        }
        long value = 0L;
        for (SzFlag flag : set) {
            if (flag == null) {
                continue;
            }
            value |= flag.toLong();
        }
        return value;
    }

    /**
     * Checks if any intersection exists between two sets of {@link SzFlag}
     * instances.
     * 
     * @param set1 The first {@link Set} of {@link SzFlag} instances.
     * @param set2 The second {@link Set} of {@link SzFlag} instances.
     * 
     * @return <code>true</code> if there is any intersection between the
     *         specified sets, or <code>false</code> if there is no
     *         intersection.
     */
    public static boolean intersects(Set<SzFlag> set1, Set<SzFlag> set2) {
        if (set1 == null || set2 == null) {
            return false;
        }
        for (SzFlag flag : set1) {
            if (set2.contains(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new {@link EnumSet} of {@link SzFlag} instances that
     * represents the intersection between the two specified sets of
     * {@link SzFlag} instances.
     * 
     * @param set1 The first {@link Set} of {@link SzFlag} instances.
     * @param set2 The second {@link Set} of {@link SzFlag} instances.
     * 
     * @return A new {@link Set} of {@link SzFlag} instances representing
     *         intersection between the two specified sets.
     */
    public static EnumSet<SzFlag> intersect(Set<SzFlag> set1, Set<SzFlag> set2) {
        EnumSet<SzFlag> result = EnumSet.noneOf(SzFlag.class);
        if (set1 == null || set2 == null) {
            return result;
        }
        for (SzFlag flag : set1) {
            if (set2.contains(flag)) {
                result.add(flag);
            }
        }
        return result;
    }

    /**
     * Creates a new {@link EnumSet} of {@link SzFlag} instances that
     * represents the union between the two specified sets of
     * {@link SzFlag} instances.
     * 
     * @param set1 The first {@link Set} of {@link SzFlag} instances.
     * @param set2 The second {@link Set} of {@link SzFlag} instances.
     * 
     * @return A new {@link Set} of {@link SzFlag} instances representing
     *         union between the two specified sets.
     */
    public static EnumSet<SzFlag> union(Set<SzFlag> set1, Set<SzFlag> set2) {
        EnumSet<SzFlag> result = EnumSet.noneOf(SzFlag.class);
        if (set1 != null) {
            result.addAll(set1);
        }
        if (set2 != null) {
            result.addAll(set2);
        }
        return result;
    }

    /**
     * Returns the {@link String} representation describing the {@link SzFlag}
     * instances in the specified {@link Set}.
     * 
     * @param flagSet The {@link Set} of {@link SzFlag} instances to convert
     *                to a {@link String}.
     * 
     * @return The {@link String} representation describing the specified 
     *         {@link Set} of {@link SzFlag} instances.
     */
    public static String toString(Set<SzFlag> flagSet) {
    
        StringBuilder sb = new StringBuilder();
        String prefix  = "";
        if (flagSet == null || flagSet.size() == 0) {
            sb.append("{ NONE }");
        } else {
            for (SzFlag flag : flagSet) {
                if (flag == null) {
                    continue;
                }
                sb.append(prefix);
                sb.append(flag.name());
                prefix = " | ";
            }
        }
        sb.append(" [");
        sb.append(hexFormat(toLong(flagSet)));
        sb.append("]");
        return sb.toString();
    }
}
