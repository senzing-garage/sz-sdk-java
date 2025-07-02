package com.senzing.sdk;

import java.util.EnumSet;
import java.util.Set;

import java.util.Collections;

import static com.senzing.sdk.SzFlagHelpers.*;
import static com.senzing.sdk.SzFlagUsageGroup.SZ_GROUP_SET_LOOKUP;
import static com.senzing.sdk.Utilities.hexFormat;

/**
 * Enumerates the Senzing flag values from {@link SzFlags} so they can be
 * referred to as objects, used in {@link EnumSet} instances and converted
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
     *     <li>{@link SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_REDO_FLAGS}
     * </ul>
     */
    SZ_WITH_INFO(SzFlags.SZ_WITH_INFO, SZ_MODIFY_SET),

    /**
     * The value for export functionality to indicate "resolved" relationships
     * (i.e.: entities with multiple records) should be included in the export.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES(
        SzFlags.SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES, SZ_EXPORT_SET),
    
    /**
     * The value for export functionality to indicate that "possibly same"
     * relationships should be included in the export.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_POSSIBLY_SAME(
        SzFlags.SZ_EXPORT_INCLUDE_POSSIBLY_SAME, SZ_EXPORT_SET),
    
    /**
     * The value for export functionality to indicate that "possibly related"
     * relationships should be included in the export.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_POSSIBLY_RELATED(
        SzFlags.SZ_EXPORT_INCLUDE_POSSIBLY_RELATED, SZ_EXPORT_SET),
    
    /**
     * The value for export functionality to indicate that "name only" relationships
     * should be included in the export.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_NAME_ONLY(
        SzFlags.SZ_EXPORT_INCLUDE_NAME_ONLY, SZ_EXPORT_SET),

    /**
     * The value for export functionality to indicate that "disclosed" relationships
     * should be included in the export.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_EXPORT_INCLUDE_DISCLOSED(
        SzFlags.SZ_EXPORT_INCLUDE_DISCLOSED, SZ_EXPORT_SET),

    /**
     * The value for export functionality to indicate that single-record entities
     * should be included in the export.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_TYPES(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_TYPES, SZ_ENTITY_SET),

    /**
     * The value for including the basic record data for the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO, SZ_ENTITY_SET),

    /**
     * The value for including first seen and last seen date-time-stamps
     * for returned records.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_DATES(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_DATES, SZ_ENTITY_RECORD_SET),

    /**
     * The value for including the record json data for the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_JSON_DATA(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_JSON_DATA, SZ_PREPROCESS_SET),

    /**
     * The value for including the record unmapped data for the entity.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA, SZ_PREPROCESS_SET),

    /**
     * The value for including features identifiers in the records
     * segment, referencing back to the entity features.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_FEATURES(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_FEATURES, SZ_PREPROCESS_SET),

    /**
     * The value for including record-level feature details in
     * the record segment of an entity or in a record response.
     * This is affected by {@link 
     * #SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS, SZ_PREPROCESS_SET),

    /**
     * The value for including record-level feature statistics
     * in the record segment of an entity or in a record
     * response.  This is affected by {@link 
     * #SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS(
        SzFlags.SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS, SZ_PREPROCESS_SET),

    /**
     * The value for including the name of the related entities.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA(
        SzFlags.SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA, SZ_RELATION_SET),

    /**
     * The value for including internal features in an entity response or
     * record response.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_INTERNAL_FEATURES(
        SzFlags.SZ_ENTITY_INCLUDE_INTERNAL_FEATURES, SZ_PREPROCESS_SET),

    /**
     * The value for including feature statistics in entity output.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_ENTITY_INCLUDE_FEATURE_STATS(
        SzFlags.SZ_ENTITY_INCLUDE_FEATURE_STATS, SZ_ENTITY_SET),

    /**
     * The value for including match key details in addition to the standard
     * match key.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_HOW_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_HOW_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_INCLUDE_FEATURE_SCORES(
        SzFlags.SZ_INCLUDE_FEATURE_SCORES, SZ_HOW_WHY_SEARCH_SET),

    /**
     * The value for including statistics from search results.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_STATS(
        SzFlags.SZ_SEARCH_INCLUDE_STATS, SZ_WHY_SEARCH_SET),
    
    /**
     * The value for search functionality to indicate that "resolved" match
     * level results should be included.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_RESOLVED(
        SzFlags.SZ_SEARCH_INCLUDE_RESOLVED, SZ_SEARCH_SET),
    
    /**
     * The value for search functionality to indicate that "possibly same"
     * match level results should be included.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_POSSIBLY_SAME(
        SzFlags.SZ_SEARCH_INCLUDE_POSSIBLY_SAME, SZ_SEARCH_SET),

    /**
     * The value for search functionality to indicate that "possibly related"
     * match level results should be included.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_POSSIBLY_RELATED(
        SzFlags.SZ_SEARCH_INCLUDE_POSSIBLY_RELATED, SZ_SEARCH_SET),

    /**
     * The value for search functionality to indicate that "name only"
     * match level results should be included.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_NAME_ONLY(
        SzFlags.SZ_SEARCH_INCLUDE_NAME_ONLY, SZ_SEARCH_SET),

    /**
     * The value for search functionality to indicate that
     * search results should not only include those entities that
     * satisfy resolution rule, but also those that present on the
     * candidate list but fail to satisfy a resolution rule.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_ALL_CANDIDATES(
        SzFlags.SZ_SEARCH_INCLUDE_ALL_CANDIDATES, SZ_SEARCH_SET),
    
    /**
     * The value for search functionality to indicate that 
     * the search response should include the basic feature
     * information for the search criteria features.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    SZ_SEARCH_INCLUDE_REQUEST(
        SzFlags.SZ_SEARCH_INCLUDE_REQUEST, SZ_WHY_SEARCH_SET),

    /**
     * The value for search functionality to indicate that 
     * the search response should include detailed feature 
     * information for search criteria features (including feature
     * stats and generic status).  This flag has no effect unless
     * {@link #SZ_SEARCH_INCLUDE_REQUEST} is also specified.
     * <p>
     * This flag belongs to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     * 
     */
    SZ_SEARCH_INCLUDE_REQUEST_DETAILS(
        SzFlags.SZ_SEARCH_INCLUDE_REQUEST_DETAILS, SZ_WHY_SEARCH_SET);

    /**
     * An <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances 
     * containing <b>NO</b> flags (an empty set).
     */
    public static final Set<SzFlag> SZ_NO_FLAGS 
        = Collections.unmodifiableSet(EnumSet.noneOf(SzFlag.class));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the {@link 
     * SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS
     */
    public static final Set<SzFlag> SZ_ADD_RECORD_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_WITH_INFO));
        
    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the {@link 
     * SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS
     */
    public static final Set<SzFlag> SZ_DELETE_RECORD_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_WITH_INFO));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the {@link 
     * SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS
     */
    public static final Set<SzFlag> SZ_REEVALUATE_RECORD_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_WITH_INFO));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the {@link 
     * SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS
     */
    public static final Set<SzFlag> SZ_REEVALUATE_ENTITY_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_WITH_INFO));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the {@link 
     * SzFlagUsageGroup#SZ_REDO_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_REDO_FLAGS
     */
    public static final Set<SzFlag> SZ_REDO_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_WITH_INFO));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_RECORD_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_RECORD_FLAGS
     */
    public static final Set<SzFlag> SZ_RECORD_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_INTERNAL_FEATURES,
            SZ_ENTITY_INCLUDE_RECORD_FEATURES,
            SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS,
            SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS,
            SZ_ENTITY_INCLUDE_RECORD_DATES,
            SZ_ENTITY_INCLUDE_RECORD_JSON_DATA,
            SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS
     */
    public static final Set<SzFlag> SZ_RECORD_PREVIEW_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_INTERNAL_FEATURES,
            SZ_ENTITY_INCLUDE_RECORD_FEATURES,
            SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS,
            SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS,
            SZ_ENTITY_INCLUDE_RECORD_JSON_DATA,
            SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_ENTITY_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_ENTITY_FLAGS
     */
    public static final Set<SzFlag> SZ_ENTITY_ALL_FLAGS;

    static {
        EnumSet<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.add(SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS);
        flagSet.add(SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS);
        flagSet.add(SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS);
        flagSet.add(SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS);
        flagSet.add(SZ_ENTITY_INCLUDE_ALL_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_ENTITY_NAME);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_SUMMARY);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_TYPES);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_DATA);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_DATES);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_JSON_DATA);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS);
        flagSet.add(SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME);
        flagSet.add(SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
        flagSet.add(SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY);
        flagSet.add(SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES);
        flagSet.add(SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA);
        flagSet.add(SZ_ENTITY_INCLUDE_INTERNAL_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_FEATURE_STATS);
        flagSet.add(SZ_INCLUDE_MATCH_KEY_DETAILS);
        SZ_ENTITY_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_FIND_PATH_FLAGS
     */
    public static final Set<SzFlag> SZ_FIND_PATH_ALL_FLAGS;

    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_FIND_PATH_STRICT_AVOID);
        flagSet.add(SZ_FIND_PATH_INCLUDE_MATCHING_INFO);
        SZ_FIND_PATH_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS
     */
    public static final Set<SzFlag> SZ_FIND_NETWORK_ALL_FLAGS;

    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO);
        SZ_FIND_NETWORK_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS
     */
    public static final Set<SzFlag> SZ_FIND_INTERESTING_ENTITIES_ALL_FLAGS
        = Collections.unmodifiableSet(EnumSet.noneOf(SzFlag.class));

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_SEARCH_FLAGS
     */
    public static final Set<SzFlag> SZ_SEARCH_ALL_FLAGS;

    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_INCLUDE_MATCH_KEY_DETAILS);
        flagSet.add(SZ_INCLUDE_FEATURE_SCORES);
        flagSet.add(SZ_SEARCH_INCLUDE_STATS);
        flagSet.add(SZ_SEARCH_INCLUDE_RESOLVED);
        flagSet.add(SZ_SEARCH_INCLUDE_POSSIBLY_SAME);
        flagSet.add(SZ_SEARCH_INCLUDE_POSSIBLY_RELATED);
        flagSet.add(SZ_SEARCH_INCLUDE_NAME_ONLY);
        flagSet.add(SZ_SEARCH_INCLUDE_ALL_CANDIDATES);
        flagSet.add(SZ_SEARCH_INCLUDE_REQUEST);
        flagSet.add(SZ_SEARCH_INCLUDE_REQUEST_DETAILS);
        SZ_SEARCH_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_EXPORT_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_EXPORT_FLAGS
     */
    public static final Set<SzFlag> SZ_EXPORT_ALL_FLAGS;

    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES);
        flagSet.add(SZ_EXPORT_INCLUDE_POSSIBLY_SAME);
        flagSet.add(SZ_EXPORT_INCLUDE_POSSIBLY_RELATED);
        flagSet.add(SZ_EXPORT_INCLUDE_NAME_ONLY);
        flagSet.add(SZ_EXPORT_INCLUDE_DISCLOSED);
        flagSet.add(SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES);
        SZ_EXPORT_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS
     */
    public static final Set<SzFlag> SZ_WHY_RECORDS_ALL_FLAGS;
    
    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_INCLUDE_FEATURE_SCORES);
        SZ_WHY_RECORDS_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS
     */
    public static final Set<SzFlag> SZ_WHY_ENTITIES_ALL_FLAGS;
    
    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_INCLUDE_FEATURE_SCORES);
        SZ_WHY_ENTITIES_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS
     */
    public static final Set<SzFlag> SZ_WHY_RECORD_IN_ENTITY_ALL_FLAGS;
    
    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_INCLUDE_FEATURE_SCORES);
        SZ_WHY_RECORD_IN_ENTITY_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS
     */
    public static final Set<SzFlag> SZ_WHY_SEARCH_ALL_FLAGS;
    
    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.addAll(SZ_ENTITY_ALL_FLAGS);
        flagSet.add(SZ_INCLUDE_FEATURE_SCORES);
        flagSet.add(SZ_SEARCH_INCLUDE_STATS);
        flagSet.add(SZ_SEARCH_INCLUDE_REQUEST);
        flagSet.add(SZ_SEARCH_INCLUDE_REQUEST_DETAILS);
        SZ_WHY_SEARCH_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_HOW_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_HOW_FLAGS
     */
    public static final Set<SzFlag> SZ_HOW_ALL_FLAGS;
    
    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.add(SZ_INCLUDE_MATCH_KEY_DETAILS);
        flagSet.add(SZ_INCLUDE_FEATURE_SCORES);
        SZ_HOW_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances
     * containing all {@link SzFlag} instances belonging to the
     * {@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS} usage group.
     * 
     * @see SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS
     */
    public static final Set<SzFlag> SZ_VIRTUAL_ENTITY_ALL_FLAGS;

    static {
        Set<SzFlag> flagSet = EnumSet.noneOf(SzFlag.class);
        flagSet.add(SZ_ENTITY_INCLUDE_ALL_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_ENTITY_NAME);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_SUMMARY);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_TYPES);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_DATA);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_DATES);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_JSON_DATA);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS);
        flagSet.add(SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS);
        flagSet.add(SZ_ENTITY_INCLUDE_INTERNAL_FEATURES);
        flagSet.add(SZ_ENTITY_INCLUDE_FEATURE_STATS);
        SZ_VIRTUAL_ENTITY_ALL_FLAGS = Collections.unmodifiableSet(flagSet);
    }

    /**
     * The <b>unmodifiable</b> {@link Set} of {@link SzFlag} instances containing
     * the {@link SzFlag} instances to use when a repository-modifying operation 
     * is being invoked and the desired response should contain an "INFO" message
     * describing how the repository was affected as a result of the operation.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *     <li>{@link #SZ_WITH_INFO}
     * </ul>
     * <p>
     * All the flags in this {@link Set} belong to the following usage groups:
     * <ul>
     *     <li>{@link SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS}
     *     <li>{@link SzFlagUsageGroup#SZ_REDO_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
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
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
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
     * to the {@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}, and by extension
     * belong to the following usage groups which are super sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     * results should include entities that are related to the search attributes
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
     * {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} usage group.
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
     * The {@link Set} of {@link SzFlag} instances representing the default
     * level of detail when retrieving records.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *  <li>{@link #SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the {@link 
     * SzFlagUsageGroup#SZ_RECORD_FLAGS} usage group, and by extension belong to the
     * following groups which are super sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_RECORD_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_RECORD_DEFAULT_FLAGS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_ENTITY_INCLUDE_RECORD_JSON_DATA));

    /**
     * The {@link Set} of {@link SzFlag} instances representing the flags
     * for obtaining the typical basic entity content without any related
     * entity content.  This constant is used in building other
     * {@link Set}'s of aggregate {@link SzFlag} instances, but can be
     * used directly when retrieving entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_DATA}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}, and by extension
     * belong to the following usage groups which are super sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_ENTITY_CORE_FLAGS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES,
                       SZ_ENTITY_INCLUDE_ENTITY_NAME,
                       SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                       SZ_ENTITY_INCLUDE_RECORD_DATA,
                       SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO));

    /**
     * The {@link Set} of {@link SzFlag} instances representing the default'
     * level of detail when retrieving entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_CORE_FLAGS}
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}, and by extension
     * belong to the following usage groups which are super sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_ENTITY_DEFAULT_FLAGS;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_ENTITY_CORE_FLAGS);
        set.addAll(SZ_ENTITY_INCLUDE_ALL_RELATIONS);
        set.add(SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME);
        set.add(SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY);
        set.add(SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
        SZ_ENTITY_DEFAULT_FLAGS = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances representing the default
     * flags for a "brief" level of detail when retrieving entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}, and by extension
     * belong to the following usage groups which are super sets:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_ENTITY_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_SEARCH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_EXPORT_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS}
     *    <li>{@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
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
     * The {@link Set} of {@link SzFlag} instances representing the default flags
     * for exporting entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_EXPORT_INCLUDE_ALL_ENTITIES}
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_EXPORT_FLAGS} usage group.
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
     * The {@link Set} of {@link SzFlag} instances representing the defaults
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
     * {@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_FIND_PATH_DEFAULT_FLAGS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_FIND_PATH_INCLUDE_MATCHING_INFO,
                       SZ_ENTITY_INCLUDE_ENTITY_NAME,
                       SZ_ENTITY_INCLUDE_RECORD_SUMMARY));

    /**
     * The {@link Set} of {@link SzFlag} instances representing the defaults
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
     * {@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_FIND_NETWORK_DEFAULT_FLAGS
        = Collections.unmodifiableSet(
            EnumSet.of(SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO,
                       SZ_ENTITY_INCLUDE_ENTITY_NAME,
                       SZ_ENTITY_INCLUDE_RECORD_SUMMARY));


    /**
     * The {@link Set} of {@link SzFlag} instances representing the
     * defaults for "why entities" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_WHY_ENTITIES_DEFAULT_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_INCLUDE_FEATURE_SCORES));

    /**
     * The {@link Set} of {@link SzFlag} instances representing the
     * defaults for "why records" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_WHY_RECORDS_DEFAULT_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_INCLUDE_FEATURE_SCORES));

    /**
     * The {@link Set} of {@link SzFlag} instances representing the defaults
     * for "why record in entity" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_INCLUDE_FEATURE_SCORES));

     /**
     * The {@link Set} of {@link SzFlag} instances representing the defaults
     * for "how entity" operations that analyze how an entity was formed.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong to the
     * {@link SzFlagUsageGroup#SZ_HOW_FLAGS} usage group.
     *
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_HOW_ENTITY_DEFAULT_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_INCLUDE_FEATURE_SCORES));

     /**
     * The {@link Set} of {@link SzFlag} instances representing the defaults
     * when retrieving virtual entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_ENTITY_CORE_FLAGS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} belong to the following usage groups:
     * <ul>
     *    <li>{@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     * </ul>
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS = SZ_ENTITY_CORE_FLAGS;

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that search
     * results should include all matching entities regardless of match level.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_SEARCH_INCLUDE_ALL_ENTITIES}
     *   <li>{@link #SZ_SEARCH_INCLUDE_STATS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} usage group.
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
        set.add(SZ_SEARCH_INCLUDE_STATS);
        SZ_SEARCH_BY_ATTRIBUTES_ALL = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that search
     * results should only include strongly matching entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_SEARCH_INCLUDE_RESOLVED}
     *   <li>{@link #SZ_SEARCH_INCLUDE_POSSIBLY_SAME}
     *   <li>{@link #SZ_SEARCH_INCLUDE_STATS}
     *   <li>{@link #SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *   <li>{@link #SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_STRONG
        = Collections.unmodifiableSet(EnumSet.of(SZ_SEARCH_INCLUDE_RESOLVED,
                                                 SZ_SEARCH_INCLUDE_POSSIBLY_SAME,
                                                 SZ_SEARCH_INCLUDE_STATS,
                                                 SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES,
                                                 SZ_ENTITY_INCLUDE_ENTITY_NAME,
                                                 SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
                                                 SZ_INCLUDE_FEATURE_SCORES));

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that search
     * results should include all matching entities regardless of match level
     * while returning minimal data for those matching entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>All {@link SzFlag} instances from {@link #SZ_SEARCH_INCLUDE_ALL_ENTITIES}
     *   <li>{@link #SZ_SEARCH_INCLUDE_STATS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */    
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL;

    static {
        EnumSet<SzFlag> set = EnumSet.copyOf(SZ_SEARCH_INCLUDE_ALL_ENTITIES);
        set.add(SZ_SEARCH_INCLUDE_STATS);
        SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL = Collections.unmodifiableSet(set);
    }

    /**
     * The {@link Set} of {@link SzFlag} instances indicating that search
     * results should only include strongly matching entities while returning
     * minimal data for those matching entities.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_SEARCH_INCLUDE_RESOLVED}
     *   <li>{@link #SZ_SEARCH_INCLUDE_POSSIBLY_SAME}
     *   <li>{@link #SZ_SEARCH_INCLUDE_STATS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
        = Collections.unmodifiableSet(EnumSet.of(SZ_SEARCH_INCLUDE_RESOLVED, 
                                                 SZ_SEARCH_INCLUDE_POSSIBLY_SAME,
                                                 SZ_SEARCH_INCLUDE_STATS));

    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for searching by attributes and returning
     * basic entity data.
     * <p>
     * This is equivalent to {@link #SZ_SEARCH_BY_ATTRIBUTES_ALL}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} usage group.
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
     * The {@link Set} of {@link SzFlag} instances that are a recommended
     * default for "why search" operations and returning basic entity data.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_INCLUDE_FEATURE_SCORES}
     *   <li>{@link #SZ_SEARCH_INCLUDE_REQUEST_DETAILS}
     *   <li>{@link #SZ_SEARCH_INCLUDE_STATS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_WHY_SEARCH_DEFAULT_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_INCLUDE_FEATURE_SCORES, 
                                                 SZ_SEARCH_INCLUDE_REQUEST_DETAILS,
                                                 SZ_SEARCH_INCLUDE_STATS));
    
    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for "add record".
     * <p>
     * Currently this is equivalent to {@link #SZ_NO_FLAGS}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_ADD_RECORD_DEFAULT_FLAGS = SZ_NO_FLAGS;
    
    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for "delete record".
     * <p>
     * Currently this is equivalent to {@link #SZ_NO_FLAGS}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_DELETE_RECORD_DEFAULT_FLAGS = SZ_NO_FLAGS;

    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for "preprocess record" operations.
     * <p>
     * The contained {@link SzFlag} instances are:
     * <ul>
     *   <li>{@link #SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     * </ul>
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_RECORD_PREVIEW_DEFAULT_FLAGS
        = Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS));
    
    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for "reevaluate record" operations.
     * <p>
     * Currently this is equivalent to {@link #SZ_NO_FLAGS}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_REEVALUATE_RECORD_DEFAULT_FLAGS = SZ_NO_FLAGS;

    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for "reevaluate entity" operations.
     * <p>
     * Currently this is equivalent to {@link #SZ_NO_FLAGS}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS
        = SZ_REEVALUATE_RECORD_DEFAULT_FLAGS;

    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for "find interesting entities" operations.
     * <p>
     * Currently this is equivalent to {@link #SZ_NO_FLAGS}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS
        = SZ_NO_FLAGS;

    /**
     * The {@link Set} of {@link SzFlag} instances that are a
     * recommended default for "process redo" operations.
     * <p>
     * Currently this is equivalent to {@link #SZ_NO_FLAGS}.
     * <p>
     * All the flags in this {@link Set} are guaranteed to belong
     * to the {@link SzFlagUsageGroup#SZ_REDO_FLAGS} usage group.
     * 
     * @see <a href="https://docs.senzing.com/flags/index.html">https://docs.senzing.com/flags/index.html</a>
     */
    public static final Set<SzFlag> SZ_REDO_DEFAULT_FLAGS = SZ_NO_FLAGS;

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
