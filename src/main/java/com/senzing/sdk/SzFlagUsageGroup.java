package com.senzing.sdk;

import java.util.Map;
import java.util.IdentityHashMap;
import java.util.EnumSet;
import java.util.Set;
import java.util.Collections;

import static com.senzing.sdk.Utilities.hexFormat;

/**
 * Enumerates the various classifications of usage groups for the
 * {@link SzFlag} instances.
 */
public enum SzFlagUsageGroup {
    /**
     * Flags in this usage group can be used for operations that modify the
     * entity repository by adding records, revaluating records or entities,
     * deleting records or any similar operations.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_WITH_INFO}
     * </ul>
     */
    SZ_MODIFY_FLAGS,

    /**
     * Flags in this usage group can be used for operations that retrieve record
     * data in order to control the level of detail of the returned record.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances for this group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_RECORD_FLAGS,

    /**
     * Flags in this usage group can be used for operations that retrieve
     * entity data in order to control the level of detail of the returned
     * entity.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_INCLUDE_MATCH_KEY_DETAILS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances for this group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that also 
     * support this group for definining entity or record detail levels are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_ENTITY_FLAGS,

    /**
     * Flags in this usage group can be used to control the methodology for
     * finding an entity path, what details to include for the entity 
     * path and the level of detail for the entities on the path that
     * are returned.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_INCLUDE_MATCH_KEY_DETAILS}
     *      <li>{@link SzFlag#SZ_FIND_PATH_STRICT_AVOID}
     *      <li>{@link SzFlag#SZ_FIND_PATH_INCLUDE_MATCHING_INFO}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that use this
     * group and are defined for "find-path" operations are:
     * <ul>
     *      <li>{@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that also 
     * support this group for definining entity or record detail levels are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_FIND_PATH_FLAGS,

    /**
     * Flags in this usage group can be used to control the methodology for
     * finding an entity network, what details to include for the entity 
     * network and the level of detail for the entities in the network
     * that are returned.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_INCLUDE_MATCH_KEY_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that use this
     * group and are defined for "find-path" operations are:
     * <ul>
     *      <li>{@link SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that also 
     * support this group for definining entity or record detail levels are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_FIND_NETWORK_FLAGS,

    /**
     * Flags in this usage group can be used for operations that search for 
     * entities to control how the entities are qualified for inclusion
     * in the search results and the level of detail for the entities
     * returned in the search results.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_INCLUDE_MATCH_KEY_DETAILS}
     *      <li>{@link SzFlag#SZ_INCLUDE_FEATURE_SCORES}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_STATS}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_RESOLVED}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_POSSIBLY_SAME}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_POSSIBLY_RELATED}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_NAME_ONLY}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_ALL_CANDIDATES}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_REQUEST}
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_REQUEST_DETAILS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that use this
     * group and are defined for "search" operations are:
     * <ul>
     *      <li>{@link SzFlag#SZ_SEARCH_INCLUDE_ALL_ENTITIES}
     *      <li>{@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_ALL}
     *      <li>{@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_STRONG}
     *      <li>{@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL}
     *      <li>{@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG}
     *      <li>{@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that also 
     * support this group for definining entity or record detail levels are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_SEARCH_FLAGS,

    /**
     * Flags in this usage group can be used for operations that export 
     * entities to control how the entities are qualified for inclusion
     * in the export and the level of detail for the entities returned
     * in the search results.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES}
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_POSSIBLY_SAME}
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_POSSIBLY_RELATED}
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_NAME_ONLY}
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_DISCLOSED}
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES}
     * </ul>
     * The pre-defined {@link SzFlag} {@link Set} instances that use this
     * group and are defined for "export" operations are:
     * <ul>
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_ALL_ENTITIES}
     *      <li>{@link SzFlag#SZ_EXPORT_INCLUDE_ALL_HAVING_RELATIONSHIPS}
     *      <li>{@link SzFlag#SZ_EXPORT_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that also 
     * support this group for definining entity or record detail levels are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_EXPORT_FLAGS,

    /**
     * Flags in this usage group can be used to control the methodology for
     * performing "why analysis", what details to include for the analysis
     * and the level of detail for the entities in the network that are 
     * returned.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_INCLUDE_MATCH_KEY_DETAILS}
     *      <li>{@link SzFlag#SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that use this
     * group and are defined for "why" operations are:
     * <ul>
     *      <li>{@link SzFlag#SZ_WHY_ENTITIES_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that also 
     * support this group for definining entity or record detail levels are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_WHY_FLAGS,

    /**
     * Flags in this usage group can be used to control the methodology for
     * performing "how analysis", what details to include for the analysis
     * and the level of detail for the entities in the network that are 
     * returned.
     * Flags in this usage group can be used to control the methodology for
     * performing "why analysis", what details to include for the analysis
     * and the level of detail for the entities in the network that are 
     * returned.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_INCLUDE_MATCH_KEY_DETAILS}
     *      <li>{@link SzFlag#SZ_INCLUDE_FEATURE_SCORES}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that use this
     * group and are defined for "how" operations are:
     * <ul>
     *      <li>{@link SzFlag#SZ_HOW_ENTITY_DEFAULT_FLAGS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances that also 
     * support this group for definining entity or record detail levels are:
     * <ul>
     *      <li>{@link SzFlag#SZ_RECORD_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_RELATIONS}
     *      <li>{@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS}
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_HOW_FLAGS,

    /**
     * Flags in this usage group can be used for operations that retrieve
     * virtual entities in order to control the level of detail of the
     * returned virtual entity.
     * <p>
     * The {@link SzFlag} instances included in this usage group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ALL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_ENTITY_NAME}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_SUMMARY}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_TYPES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_RECORD_FEATURE_STATS}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_INTERNAL_FEATURES}
     *      <li>{@link SzFlag#SZ_ENTITY_INCLUDE_FEATURE_STATS}
     * </ul>
     * <p>
     * The pre-defined {@link SzFlag} {@link Set} instances for this group are:
     * <ul>
     *      <li>{@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS}
     * </ul>
     */
    SZ_VIRTUAL_ENTITY_FLAGS;

    /**
     * The unmodifiable {@link Set} of {@link SzFlag} instances that 
     * belong to this group.  Initialization is deferred until the
     * static initializer so all class instances can be constructed
     * before attempting to trigger initialization of the {@link SzFlag}
     * class due to the interdependence of the two classes.
     */
    private Set<SzFlag> flags = null;

    /**
     * The flag symbols for each bit with the respective array index.
     * This will be populated by the static initializer.  This allows
     * the {@link SzFlag} to initialize after all instances of this
     * class are constructed.
     */
    private SzFlag[] lookup = null;

    /**
     * The number of possible flags bits that can be set.
     */
    private static final int FLAGS_BIT_COUNT = 64;

    /**
     * The number of bins needed for name lookup -- one bin for each of the
     * 64 bits that can be set plus one to handle the name for the flags
     * value of zero (0).
     */
    private static final int LOOKUP_BIN_COUNT = FLAGS_BIT_COUNT + 1;

    /**
     * Default private constructor.
     */
    SzFlagUsageGroup() {
        this.flags  = null;
        this.lookup = new SzFlag[LOOKUP_BIN_COUNT];
    }

    /**
     * Gets the {@link EnumSet} of {@link SzFlag} instances that have this
     * {@link SzFlagUsageGroup}.
     * 
     * @return The {@link EnumSet} of {@link SzFlag} instances that have this
     *         {@link SzFlagUsageGroup}.
     */
    public Set<SzFlag> getFlags() {
        return this.flags;
    }

    /**
     * Formats the {@link String} representation of the specified <code>long</code>
     * flags value according to the {@link SzFlag} instances included for this
     * {@link SzFlagUsageGroup}.
     * <p>
     * <b>NOTE:</b> This method is useful in logging which flags were past to a
     * particular method using the {@link SzFlagUsageGroup} for the flags that
     * are accepted by that method.
     * <p>
     * Some {@link SzFlag} values have the same underlying bitwise flag value, but
     * none of the {@link SzFlag} instances for a single {@link SzFlagUsageGroup}
     * should overlap in bitwise values and this method will prefer the {@link SzFlag}
     * belonging to this {@link SzFlagUsageGroup} for formatting the {@link String}.
     * 
     * @param flagsValue The <code>long</code> flags to format as a {@link String}.
     * 
     * @return The {@link String} describing the specified flags.
     */
    public String toString(long flagsValue) {
        StringBuilder sb = new StringBuilder();

        String prefix = "";
        if (flagsValue == 0L) {
            // handle the zero
            sb.append((this.lookup[FLAGS_BIT_COUNT] != null) 
                        ? this.lookup[FLAGS_BIT_COUNT].name() : "{ NONE }");

        } else {
            for (int index = 0; index < FLAGS_BIT_COUNT; index++) {
                if ((1L << index & flagsValue) != 0L) {
                    sb.append(prefix);
                    if (lookup[index] == null) {
                        sb.append(hexFormat(1L << index));
                    } else {
                        sb.append(lookup[index].name());
                    }
                    prefix = " | ";
                }
            }
        }
        sb.append(" [");
        sb.append(hexFormat(flagsValue));
        sb.append("]");
        return sb.toString();
    }

    /**
     * The package-private <b>unmodifiable</b> {@link Set} to use for {@link SzFlag}
     * instances that can be used for all usage groups.
     */
    static final Set<SzFlagUsageGroup> SZ_ALL_GROUPS_SET 
        = Collections.unmodifiableSet(EnumSet.allOf(SzFlagUsageGroup.class));
    
    /**
     * The package-private <b>unmodifiable</b> {@link Set} to use for {@link SzFlag}
     * instances that can only be used for "modify" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_MODIFY_SET 
        = Collections.unmodifiableSet(EnumSet.of(SZ_MODIFY_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to apply to {@link SzFlag} instances that retrieve entity data since
     * they are used by most operations.  Flags that use this {@link Set} should not
     * affect inclusion of or details of related entities.
     */
    static final Set<SzFlagUsageGroup> SZ_ENTITY_SET
        = Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_FLAGS,
                                                 SZ_SEARCH_FLAGS,
                                                 SZ_EXPORT_FLAGS,
                                                 SZ_FIND_PATH_FLAGS,
                                                 SZ_FIND_NETWORK_FLAGS,
                                                 SZ_WHY_FLAGS,
                                                 SZ_VIRTUAL_ENTITY_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to apply to {@link SzFlag} instances that retrieve entity data 
     * pertaining to inclusion of related entities and the details of related 
     * entities.
     */
    static final Set<SzFlagUsageGroup> SZ_RELATION_SET
        = Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_FLAGS,
                                                 SZ_SEARCH_FLAGS,
                                                 SZ_EXPORT_FLAGS,
                                                 SZ_FIND_PATH_FLAGS,
                                                 SZ_FIND_NETWORK_FLAGS,
                                                 SZ_WHY_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to apply to {@link SzFlag} instances that retrieve entity data
     * <b>and</b> also apply to "how" operations.  Flags that use this {@link Set}
     * should not affect inclusion of or details of related entities.
     */
    static final Set<SzFlagUsageGroup> SZ_ENTITY_HOW_SET
        = Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_FLAGS,
                                                 SZ_SEARCH_FLAGS,
                                                 SZ_EXPORT_FLAGS,
                                                 SZ_FIND_PATH_FLAGS,
                                                 SZ_FIND_NETWORK_FLAGS,
                                                 SZ_HOW_FLAGS,
                                                 SZ_WHY_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to apply to {@link SzFlag} instances that retrieve record data since
     * they are used by most other groups and can be specifically used for retrieving
     * a single record.
     */
    static final Set<SzFlagUsageGroup> SZ_ENTITY_RECORD_SET
        = Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_FLAGS, 
                                                 SZ_RECORD_FLAGS,
                                                 SZ_SEARCH_FLAGS,
                                                 SZ_EXPORT_FLAGS,
                                                 SZ_FIND_PATH_FLAGS,
                                                 SZ_FIND_NETWORK_FLAGS,
                                                 SZ_WHY_FLAGS,
                                                 SZ_VIRTUAL_ENTITY_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can be used only for
     * "how analysis" and "why analysis" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_HOW_WHY_SET 
        = Collections.unmodifiableSet(EnumSet.of(SZ_WHY_FLAGS, SZ_HOW_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for "search"
     * operations.
     */
    static final Set<SzFlagUsageGroup> SZ_SEARCH_SET 
        = Collections.unmodifiableSet(EnumSet.of(SZ_SEARCH_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for 
     * "export" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_EXPORT_SET 
        = Collections.unmodifiableSet(EnumSet.of(SZ_EXPORT_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for 
     * "find path" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_FIND_PATH_SET
        = Collections.unmodifiableSet(EnumSet.of(SZ_FIND_PATH_FLAGS));

    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for 
     * "find network" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_FIND_NETWORK_SET
        = Collections.unmodifiableSet(EnumSet.of(SZ_FIND_NETWORK_FLAGS));

    /**
     * Provides an identity lookup map to map the bootstrap {@link Set}'s' of
     * {@link SzFlagUsageGroup} instances to the actual {@link Set}'s of 
     * {@link SzFlagUsageGroup} instances.
     */
    static final Map<Set<SzFlagUsageGroup>, Set<SzFlagUsageGroup>> SZ_GROUP_SET_LOOKUP;

    /**
     * Gets the single bit that is set for the specified {@link SzFlag}
     * or returns negative one (-1) if more than one or no bits are set.
     * 
     * @param flag The flag for which to determine the single bit index.
     * @return The single bit index or negative one if not a single bit.
     */
    private static int getSingleBit(SzFlag flag) {
        // get the value and check if the constant is a single-bit value
        long    value       = flag.toLong();
        long    baseValue   = 1L;
        int     bit         = -1;

        // loop through the bits
        for (int index = 0; 
             index < FLAGS_BIT_COUNT; 
             index++, baseValue *= 2L) 
        {
            if (value == baseValue) {
                bit = index;
                break;
            }
        }
        return bit;
    }

    /**
     * Sets the flag name in the group's lookup array.  This is
     * used internally for initializing the specified group.
     * 
     * @param group The {@link SzFlagUsageGroup} to initialize.
     * @param flag The flag to initialize it for.
     * @param bit The bit index for the specified flag.
     */
    private static void setGroupFlagName(SzFlagUsageGroup   group,
                                         SzFlag             flag,
                                         int                bit)
    {
        // convert to a long value
        long value = flag.toLong();
        String groupName = group.name();
        int    groupLength = groupName.length();
        String groupPrefix = groupName.substring(
            0, groupLength - "_FLAGS".length());
        
        // check if the bit already has a conflicting symbol
        if (group.lookup[bit] != null) {

            // check if the conflicting symbol is the primary symbol
            if (flag.name().startsWith(groupPrefix)) {
                // replace the existing one
                group.lookup[bit] = flag;

            } else if (!group.lookup[bit].name().startsWith(groupPrefix)) {
                // if there is a conflict with no primary flag, then fail
                throw new IllegalStateException(
                    "Conflicting symbol (" + group.lookup[bit] 
                    + ") at bit (" + bit + ") for value (" 
                    + hexFormat(value) + ") in symbol map: "
                    + flag.name());    
            }
        } else {
            // if no conflict, then set the symbol for the bit
            group.lookup[bit] = flag;
        }
    }

    static {
        Map<Set<SzFlagUsageGroup>, Set<SzFlagUsageGroup>> map = new IdentityHashMap<>();
        map.put(SzFlagHelpers.SZ_ALL_GROUPS_SET, SzFlagUsageGroup.SZ_ALL_GROUPS_SET);
        map.put(SzFlagHelpers.SZ_MODIFY_SET, SzFlagUsageGroup.SZ_MODIFY_SET);
        map.put(SzFlagHelpers.SZ_ENTITY_SET, SzFlagUsageGroup.SZ_ENTITY_SET);
        map.put(SzFlagHelpers.SZ_RELATION_SET, SzFlagUsageGroup.SZ_RELATION_SET);
        map.put(SzFlagHelpers.SZ_ENTITY_RECORD_SET, SzFlagUsageGroup.SZ_ENTITY_RECORD_SET);
        map.put(SzFlagHelpers.SZ_ENTITY_HOW_SET, SzFlagUsageGroup.SZ_ENTITY_HOW_SET);
        map.put(SzFlagHelpers.SZ_HOW_WHY_SET, SzFlagUsageGroup.SZ_HOW_WHY_SET);
        map.put(SzFlagHelpers.SZ_SEARCH_SET, SzFlagUsageGroup.SZ_SEARCH_SET);
        map.put(SzFlagHelpers.SZ_EXPORT_SET, SzFlagUsageGroup.SZ_EXPORT_SET);
        map.put(SzFlagHelpers.SZ_FIND_PATH_SET, SzFlagUsageGroup.SZ_FIND_PATH_SET);
        map.put(SzFlagHelpers.SZ_FIND_NETWORK_SET, SzFlagUsageGroup.SZ_FIND_NETWORK_SET);
        SZ_GROUP_SET_LOOKUP = Collections.unmodifiableMap(map);
        
        // setup the symbol lookup map
        for (SzFlagUsageGroup group : SzFlagUsageGroup.values()) {
            group.flags = EnumSet.noneOf(SzFlag.class);
        }

        // iterate over the flags
        for (SzFlag flag : SzFlag.values()) {
            int     bit         = getSingleBit(flag);
            boolean singleBit   = (bit >= 0);

            // loop through the groups for this flag
            Set<SzFlagUsageGroup> groups = flag.getGroups();
            if (SZ_GROUP_SET_LOOKUP.containsKey(groups)) {
                groups = SZ_GROUP_SET_LOOKUP.get(groups);
            }

            for (SzFlagUsageGroup group : groups) {
                group.flags.add(flag);
                
                // if single bit then record the name
                if (singleBit) {
                    setGroupFlagName(group, flag, bit);
                }
            }
        }

        // now make the flags unmodifiable
        for (SzFlagUsageGroup group : SzFlagUsageGroup.values()) {
            group.flags = Collections.unmodifiableSet(group.flags);
        }
    }
}
