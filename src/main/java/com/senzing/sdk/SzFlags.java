package com.senzing.sdk;

/**
 * Enumerates all the flags used by the Senzing SDK.
 */
public interface SzFlags {
    /**
     * The value representing no flags are being passed.
     * Alternatively, a <code>null</code> value will indicate
     * no flags as well.
     */
    long SZ_NO_FLAGS = 0L;

    /**
     * The bitwise flag for indicating that the Senzing engine should
     * produce and return the INFO document describing the affected
     * entities from an operation that modifies record data in the
     * repository.
     */
    long SZ_WITH_INFO = 1L << 62;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "resolved" relationships
     */
    long SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES = ( 1L << 0 );

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "possibly same" relationships
     */
    long SZ_EXPORT_INCLUDE_POSSIBLY_SAME = ( 1L << 1 );

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "possibly related" relationships
     */
    long SZ_EXPORT_INCLUDE_POSSIBLY_RELATED = ( 1L << 2 );

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "name only" relationships
     */
    long SZ_EXPORT_INCLUDE_NAME_ONLY = ( 1L << 3 );

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "disclosed" relationships
     */
    long SZ_EXPORT_INCLUDE_DISCLOSED = ( 1L << 4 );

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include singleton entities
     */
    long SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES = ( 1L << 5 );

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include all entities
     */
    long SZ_EXPORT_INCLUDE_ALL_ENTITIES 
        = (SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES 
            | SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES);

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include all relationships
     */
    long SZ_EXPORT_INCLUDE_ALL_HAVING_RELATIONSHIPS 
        = (SZ_EXPORT_INCLUDE_POSSIBLY_SAME
            | SZ_EXPORT_INCLUDE_POSSIBLY_RELATED
            | SZ_EXPORT_INCLUDE_NAME_ONLY 
            | SZ_EXPORT_INCLUDE_DISCLOSED);
    
    /**
     * The bitwise flag for including possibly-same relations for entities
     */
    long SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS = ( 1L << 6 );

    /**
     * The bitwise flag for including possibly-related relations for entities
     */
    long SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS = ( 1L << 7 );

    /**
     * The bitwise flag for including name-only relations for entities
     */
    long SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS = ( 1L << 8 );

    /**
     * The bitwise flag for including disclosed relations for entities
     */
    long SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS = ( 1L << 9 );

    /**
     * The bitwise flag for including all relations for entities
     */
    long SZ_ENTITY_INCLUDE_ALL_RELATIONS 
        = (SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS
            | SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS
            | SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS
            | SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS);

    /**
     * The bitwise flag for including all features for entities
     */
    long SZ_ENTITY_INCLUDE_ALL_FEATURES = ( 1L << 10 );
    
    /**
     * The bitwise flag for including representative features for entities
     */
    long SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES = ( 1L << 11 );
    
    /**
     * The bitwise flag for including the name of the entity
     */
    long SZ_ENTITY_INCLUDE_ENTITY_NAME = ( 1L << 12 );
    
    /**
     * The bitwise flag for including the record summary of the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_SUMMARY = ( 1L << 13 );
    
    /**
     * The bitwise flag for including the record types of the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_TYPES = ( 1L << 28 );

    /**
     * The bitwise flag for including the basic record data for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_DATA = ( 1L << 14 );
    
    /**
     * The bitwise flag for including the record matching info for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO = ( 1L << 15 );

    /**
     * The bitwise flag for including the record json data for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_JSON_DATA = ( 1L << 16 );

    /**
     * The bitwise flag for including the record unmapped data for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA = ( 1L << 31 );

    /**
     * The bitwise flag for the features identifiers for the records
     */
    long SZ_ENTITY_INCLUDE_RECORD_FEATURE_IDS = ( 1L << 18 );

    /**
     * The bitwise flag for including the name of the related entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME = ( 1L << 19 );

    /**
     * The bitwise flag for including the record matching info of the related
     * entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO = ( 1L << 20 );

    /**
     * The bitwise flag for including the record summary of the related entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY = ( 1L << 21 );

    /**
     * The bitwise flag for including the record types of the related entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES = ( 1L << 29 );

    /**
     * The bitwise flag for including the basic record data of the related
     * entities.
     */
    long SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA = ( 1L << 22 );

    /**
     * The bitwise flag for including internal features in entity output
     */
    long SZ_ENTITY_INCLUDE_INTERNAL_FEATURES = ( 1L << 23 );

    /**
     * The bitwise flag for including feature statistics in entity output.
     */
    long SZ_ENTITY_INCLUDE_FEATURE_STATS = ( 1L << 24 );

    /**
     * The bitwise flag for including feature elements.
     */
    long SZ_ENTITY_INCLUDE_FEATURE_ELEMENTS = ( 1L << 32 );

    /**
     * The bitwise flag for including internal features.
     */
    long SZ_INCLUDE_MATCH_KEY_DETAILS = ( 1L << 34 );

    /**
     * The bitwise flag for find-path functionality to indicate that avoided
     * entities are not allowed under any circumstance -- even if they are
     * the only means by which a path can be found between two entities.
     */
    long SZ_FIND_PATH_STRICT_AVOID = ( 1L << 25 );

    /**
     * The bitwise flag for find-path functionality to include
     * matching info on entity paths
     */
    long SZ_FIND_PATH_INCLUDE_MATCHING_INFO = ( 1L << 30 );

    /**
     * The bitwise flag for find-path functionality to include
     * matching info on entity paths
     */
    long SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO = ( 1L << 33 );

    /**
     * The bitwise flag for including feature scores.
     */
    long SZ_INCLUDE_FEATURE_SCORES = ( 1L << 26 );
    
    /**
     * The bitwise flag for including statistics from search results
     */
    long SZ_SEARCH_INCLUDE_STATS = ( 1L << 27 );

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "resolved" match level results
     *
     */
    long SZ_SEARCH_INCLUDE_RESOLVED 
        = (SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES);

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "possibly same" match level results
     */
    long SZ_SEARCH_INCLUDE_POSSIBLY_SAME 
        = (SZ_EXPORT_INCLUDE_POSSIBLY_SAME);

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "possibly related" match level results
     *
     */
    long SZ_SEARCH_INCLUDE_POSSIBLY_RELATED 
        = (SZ_EXPORT_INCLUDE_POSSIBLY_RELATED);

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "name only" match level results
     *
     */
    long SZ_SEARCH_INCLUDE_NAME_ONLY = (SZ_EXPORT_INCLUDE_NAME_ONLY);

    /**
     * The bitwise flag to use when a repository-modifying operation 
     * is being invoked and the desired repsonse should contain an
     * "INFO" message describing how the repository was affected as
     * a result of the operation. 
     */
    long SZ_WITH_INFO_FLAGS = SZ_WITH_INFO;

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include all match level results
     *
     */
    long SZ_SEARCH_INCLUDE_ALL_ENTITIES
        = (SZ_SEARCH_INCLUDE_RESOLVED 
            | SZ_SEARCH_INCLUDE_POSSIBLY_SAME
            | SZ_SEARCH_INCLUDE_POSSIBLY_RELATED
            | SZ_SEARCH_INCLUDE_NAME_ONLY);

    /**
     * The default recommended bitwise flag values for getting records
     */
    long SZ_RECORD_DEFAULT_FLAGS = (SZ_ENTITY_INCLUDE_RECORD_JSON_DATA);

    /**
     * The default recommended bitwise flag values for getting entities
     */
    long SZ_ENTITY_DEFAULT_FLAGS 
        = (SZ_ENTITY_INCLUDE_ALL_RELATIONS
            | SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES
            | SZ_ENTITY_INCLUDE_ENTITY_NAME
            | SZ_ENTITY_INCLUDE_RECORD_SUMMARY
            | SZ_ENTITY_INCLUDE_RECORD_DATA
            | SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO
            | SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME
            | SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY
            | SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO);

    /**
     * The default recommended bitwise flag values for getting entities
     */
    long SZ_ENTITY_BRIEF_DEFAULT_FLAGS 
        = (SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO
            | SZ_ENTITY_INCLUDE_ALL_RELATIONS
            | SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO);

    /**
     * The default recommended bitwise flag values for exporting entities
     */
    long SZ_EXPORT_DEFAULT_FLAGS 
        = (SZ_EXPORT_INCLUDE_ALL_ENTITIES
            | SZ_ENTITY_DEFAULT_FLAGS);

    /**
     * The default recommended bitwise flag values for finding entity paths
     */
    long SZ_FIND_PATH_DEFAULT_FLAGS 
        = (SZ_FIND_PATH_INCLUDE_MATCHING_INFO
            | SZ_ENTITY_INCLUDE_ENTITY_NAME
            | SZ_ENTITY_INCLUDE_RECORD_SUMMARY);

    /**
     * The default recommended bitwise flag values for finding entity networks
     */
    long SZ_FIND_NETWORK_DEFAULT_FLAGS 
        = (SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO
            | SZ_ENTITY_INCLUDE_ENTITY_NAME
            | SZ_ENTITY_INCLUDE_RECORD_SUMMARY);

    /**
     * The default recommended bitwise flag values for why-entities analysis on
     * entities
     */
    long SZ_WHY_ENTITIES_DEFAULT_FLAGS 
        = (SZ_ENTITY_DEFAULT_FLAGS
            | SZ_ENTITY_INCLUDE_INTERNAL_FEATURES
            | SZ_ENTITY_INCLUDE_FEATURE_STATS
            | SZ_INCLUDE_FEATURE_SCORES);

    /**
     * The default recommended bitwise flag values for why-records analysis on
     * entities
     */
    long SZ_WHY_RECORDS_DEFAULT_FLAGS 
        = (SZ_ENTITY_DEFAULT_FLAGS
            | SZ_ENTITY_INCLUDE_INTERNAL_FEATURES
            | SZ_ENTITY_INCLUDE_FEATURE_STATS
            | SZ_INCLUDE_FEATURE_SCORES);

    /**
     * The default recommended bitwise flag values for why-record-in analysis on
     * entities
     */
    long SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS 
        = (SZ_ENTITY_DEFAULT_FLAGS
            | SZ_ENTITY_INCLUDE_INTERNAL_FEATURES
            | SZ_ENTITY_INCLUDE_FEATURE_STATS
            | SZ_INCLUDE_FEATURE_SCORES);

    /**
     * The default recommended bitwise flag values for how-analysis on entities
     */
    long SZ_HOW_ENTITY_DEFAULT_FLAGS 
        = (SZ_INCLUDE_FEATURE_SCORES);

    /**
     * The default recommended bitwise flag values for virtual-entity-analysis on
     * entities
     */
    long SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS 
        = (SZ_ENTITY_DEFAULT_FLAGS);

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning all matching entities
     */
    long SZ_SEARCH_BY_ATTRIBUTES_ALL 
        = (SZ_SEARCH_INCLUDE_ALL_ENTITIES
            | SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES
            | SZ_ENTITY_INCLUDE_ENTITY_NAME
            | SZ_ENTITY_INCLUDE_RECORD_SUMMARY
            | SZ_INCLUDE_FEATURE_SCORES);

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning only strongly matching entities
     */
    long SZ_SEARCH_BY_ATTRIBUTES_STRONG 
        = (SZ_SEARCH_INCLUDE_RESOLVED
            | SZ_SEARCH_INCLUDE_POSSIBLY_SAME
            | SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES
            | SZ_ENTITY_INCLUDE_ENTITY_NAME
            | SZ_ENTITY_INCLUDE_RECORD_SUMMARY
            | SZ_INCLUDE_FEATURE_SCORES);

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning minimal data with all matches
     */
    long SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL = (SZ_SEARCH_INCLUDE_ALL_ENTITIES);

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning the minimal data, and returning only the strongest matches
     */
    long SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG 
        = (SZ_SEARCH_INCLUDE_RESOLVED | SZ_SEARCH_INCLUDE_POSSIBLY_SAME);
        
    /**
     * The default recommended bitwise flag values for searching by attributes
     */
    long SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS = (SZ_SEARCH_BY_ATTRIBUTES_ALL);
}
