/**********************************************************************************
 © Copyright Senzing, Inc. 2017-2024
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Defines the Java interface to the Senzing engine functions. The Senzing
 * engine functions primarily provide means of working with identity data
 * records, entities and their relationships.
 */
interface NativeEngine extends NativeApi {
    /**
     * Initializes the Senzing Engine API with the specified module name,
     * init parameters and flag indicating verbose logging. If the
     * <code>G2CONFIGFILE</code> init parameter is absent then the default
     * configuration from the repository is used.
     *
     * @param moduleName     A short name given to this instance of the engine
     *                       object
     * @param iniParams      A JSON string containing configuration parameters
     * @param verboseLogging Enable diagnostic logging which will print a massive
     *                       amount of information to stdout
     *
     * @return Zero (0) on success, non-zero on failure.
     */
    int init(String moduleName, String iniParams, boolean verboseLogging);

    /**
     * Initializes the Senzing Engine object with the module name, initialization
     * parameters, verbose logging flag and a specific configuration ID
     * identifying the configuration to use.
     *
     * @param moduleName     The module name with which to initialize.
     * @param iniParams      The JSON initialization parameters.
     * @param initConfigID   The specific configuration ID to initialize with.
     * @param verboseLogging Whether or not to initialize with verbose logging.
     *
     * @return Zero (0) on success, non-zero on failure.
     */
    int initWithConfigID(String moduleName,
            String iniParams,
            long initConfigID,
            boolean verboseLogging);

    /**
     * Reinitializes with the specified configuration ID.
     *
     * @param initConfigID The configuration ID with which to reinitialize.
     *
     * @return Zero (0) on success, non-zero on failure.
     */
    int reinit(long initConfigID);

    /**
     * Uninitializes the Senzing engine.
     *
     * @return Zero (0) on success, negative-one (-1) if the engine is not
     *         initialized.
     */
    int destroy();

    /**
     * May optionally be called to pre-initialize some of the heavier weight
     * internal resources of the Senzing engine.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int primeEngine();

    /**
     * Returns the current internal engine workload statistics for the process.
     * The counters are reset after each call.
     *
     * @return JSON document of workload statistics
     */
    String stats();

    /**
     * Returns an identifier for the loaded Senzing engine configuration.
     *
     * @param configID The identifier value for the config
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getActiveConfigID(Result<Long> configID);

    /**
     * Loads the JSON record with the specified data source code and record ID.
     * The specified JSON data may contain the <code>DATA_SOURCE</code> and
     * <code>RECORD_ID</code> elements, but, if so, they must match the specified
     * parameters.
     *
     * @param dataSourceCode The data source for the record.
     * @param recordID       The ID for the record
     * @param jsonData       A JSON document containing the attribute information
     *                       for the record.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int addRecord(String dataSourceCode,
            String recordID,
            String jsonData);

    /**
     * Loads the JSON record with the specified data source code and record ID
     * using the specified flags responding with a JSON document describing
     * how the repository was changed. This works similarly to the
     * {@link #addRecord(String, String, String)} function, but will
     * additionally provide a JSON response describing how the repository was
     * affected by adding the record.
     *
     * @param dataSourceCode The data source for the record.
     * @param recordID       The ID for the record.
     * @param jsonData       A JSON document containing the attribute information
     *                       for
     *                       the record.
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     * 
     * @return Zero (0) on success and non-zero on failure.
     */
    int addRecordWithInfo(String dataSourceCode,
            String recordID,
            String jsonData,
            long flags,
            StringBuffer response);

    /**
     * Performs a hypothetical load of the specified JSON record without
     * actually loading the record responding with a JSON document describing
     * how the record would be loaded and how the repository would be changed.
     * 
     * @param jsonData A JSON document containing the attribute information for
     *                 the record.
     * @param flags    The flags to control how the operation is performed and
     *                 specifically the content of the response JSON document.
     * @param response A {@link StringBuffer} for returning the response document.
     * 
     * @return Zero (0) on success and non-zero on failure.
     */
    int getRecordPreview(String         jsonData,
                         long           flags,
                         StringBuffer   response);

    /**
     * Delete the record that has already been loaded.
     *
     * @param dataSourceCode The data source for the record.
     * @param recordID       The ID for the record
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int deleteRecord(String dataSourceCode, String recordID);

    /**
     * Delete the record that has already been loaded. Returns a list
     * of modified resolved entities.
     *
     * @param dataSourceCode The data source for the record.
     * @param recordID       The ID for the record
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int deleteRecordWithInfo(String dataSourceCode,
            String recordID,
            long flags,
            StringBuffer response);

    /**
     * Reevaluate a record that has already been loaded.
     *
     * @param dataSourceCode The data source for the record.
     * @param recordID       The ID for the record
     * @param flags          The flags to control how the operation is performed.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int reevaluateRecord(String dataSourceCode, String recordID, long flags);

    /**
     * Reevaluate a record that has already been loaded. Returns a list
     * of resolved entities.
     *
     * @param dataSourceCode The data source for the record.
     * @param recordID       The ID for the record
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int reevaluateRecordWithInfo(String dataSourceCode,
            String recordID,
            long flags,
            StringBuffer response);

    /**
     * Reevaluate a resolved entity identified by the specified entity ID.
     *
     * @param entityID The ID of the resolved entity to reevaluate
     * @param flags    The flags to control how the operation is performed.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int reevaluateEntity(long entityID, long flags);

    /**
     * Reevaluate a resolved entity and return a list of resolved entities.
     *
     * @param entityID The ID of the resolved entity to reevaluate
     * @param flags    The flags to control how the operation is performed and
     *                 specifically the content of the response JSON document.
     * @param response A {@link StringBuffer} for returning the response document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int reevaluateEntityWithInfo(long entityID,
            long flags,
            StringBuffer response);

    /**
     * Searches for entities that contain attribute information that are
     * relevant to a set of input search attributes.
     *
     * @param jsonData A JSON document containing the attribute information
     *                 to search for
     * @param response A {@link StringBuffer} for returning the response document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int searchByAttributes(String jsonData, StringBuffer response);

    /**
     * Searches for entities that contain attribute information that are
     * relevant to a set of input search attributes.
     *
     * @param jsonData A JSON document containing the attribute information
     *                 to search for
     * @param flags    The flags to control how the operation is performed and
     *                 specifically the content of the response JSON document.
     * @param response A {@link StringBuffer} for returning the response document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int searchByAttributes(String jsonData, long flags, StringBuffer response);

    /**
     * Searches for entities that contain attribute information that are
     * relevant to a set of input search attributes.
     *
     * @param jsonData      A JSON document containing the attribute information
     *                      to search for
     * @param searchProfile A search-profile identifier
     * @param flags         The flags to control how the operation is performed and
     *                      specifically the content of the response JSON document.
     * @param response      A {@link StringBuffer} for returning the response
     *                      document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int searchByAttributes(String jsonData, String searchProfile, long flags, StringBuffer response);

    /**
     * Compares the entity identified by the specified entity ID against the
     * search criteria attributes, typically to determine why the entity was
     * <b>not</b> included in the search results.
     *
     * @param jsonData A JSON document containing the attribute information
     *                 to search for
     * @param entityId The entity ID of the entity to compare against the
     *                 search criteria.
     * @param searchProfile A search-profile identifier
     * @param response A {@link StringBuffer} for returning the response document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int whySearch(String jsonData, long entityId, String searchProfile, StringBuffer response);

    /**
     * Searches for entities that contain attribute information that are
     * relevant to a set of input search attributes.
     *
     * @param jsonData      A JSON document containing the attribute information
     *                      for which the search was performed.
     * @param entityId      The entity ID of the entity to compare against the
     *                      search criteria.
     * @param searchProfile A search-profile identifier
     * @param flags         The flags to control how the operation is performed and
     *                      specifically the content of the response JSON document.
     * @param response      A {@link StringBuffer} for returning the response
     *                      document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int whySearch(String jsonData, long entityId, String searchProfile, long flags, StringBuffer response);


    /**
     * Retrieves information about a specific resolved entity. The information
     * is returned as a JSON document.
     *
     * An entityID may be named ENTITY_ID, RESOLVED_ID, or RELATED_ID in
     * the JSON or CSV function output.
     *
     * @param entityID The resolved entity to retrieve information for
     * @param response A {@link StringBuffer} for returning the response document.
     *
     * @return Returns zero (0) for success. Returns negative-one (-1) if the
     *         response status indicates failure or the G2 module is not
     *         initialized. Returns negative-two (-2) if an exception was thrown
     *         and caught.
     */
    int getEntityByEntityID(long entityID, StringBuffer response);

    /**
     * Retrieves information about a specific resolved entity. The information
     * is returned as a JSON document.
     *
     * An entityID may be named ENTITY_ID, RESOLVED_ID, or RELATED_ID in
     * the JSON or CSV function output.
     *
     * @param entityID The resolved entity to retrieve information for
     * @param flags    The flags to control how the operation is performed and
     *                 specifically the content of the response JSON document.
     * @param response A {@link StringBuffer} for returning the response document.
     *
     * @return Returns zero (0) for success. Returns negative-one (-1) if the
     *         response status indicates failure or the G2 module is not
     *         initialized. Returns negative-two (-2) if an exception was thrown
     *         and caught.
     */
    int getEntityByEntityID(long entityID,
            long flags,
            StringBuffer response);

    /**
     * Retrieves information about the resolved entity containing a particular
     * record record.
     *
     * @param dataSourceCode The data source of the record to search for
     * @param recordID       The record ID of the record to search for
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getEntityByRecordID(String dataSourceCode,
            String recordID,
            StringBuffer response);

    /**
     * Retrieves information about the resolved entity containing a particular
     * record record.
     *
     * @param dataSourceCode The data source of the record to search for
     * @param recordID       The record ID of the record to search for
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getEntityByRecordID(String dataSourceCode,
            String recordID,
            long flags,
            StringBuffer response);

    /**
     * Finds interesting entities close to a specific resolved entity.
     * The information is returned as a JSON document.
     *
     * @param entityID The resolved entity to search around
     * @param flags    The flags to control how the operation is performed.
     * @param response A {@link StringBuffer} for returning the response document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int findInterestingEntitiesByEntityID(long entityID,
            long flags,
            StringBuffer response);

    /**
     * Finds interesting entities close to a specific resolved entity
     * containing a particular record record.
     *
     * @param dataSourceCode The data source of the record to search around
     * @param recordID       The record ID of the record to search around
     * @param flags          The flags to control how the operation is performed.
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int findInterestingEntitiesByRecordID(String dataSourceCode,
            String recordID,
            long flags,
            StringBuffer response);

    /**
     * Finds a relationship path between entities that are identified by
     * entity ID.
     *
     * @param entityID1  The entity ID of the first entity.
     * @param entityID2  The entity ID of the second entity.
     * @param maxDegrees The maximum number of degrees for the path search.
     * @param response   The {@link StringBuffer} to write the JSON response
     *                   document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByEntityID(long entityID1,
            long entityID2,
            int maxDegrees,
            StringBuffer response);

    /**
     * Finds a relationship path between entities that are identified
     * by entity ID.
     *
     * @param entityID1  The entity ID of the first entity.
     * @param entityID2  The entity ID of the second entity.
     * @param maxDegrees The maximum number of degrees for the path search.
     * @param flags      The flags to control how the operation is performed and
     *                   specifically the content of the response JSON document.
     * @param response   The {@link StringBuffer} to write the JSON response
     *                   document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByEntityID(long entityID1,
            long entityID2,
            int maxDegrees,
            long flags,
            StringBuffer response);

    /**
     * Finds a relationship path between entities that are identified by
     * the data source code and record ID of records in each of the entities.
     *
     * @param dataSourceCode1 The data source code of the first record.
     * @param recordID1       The record ID of the first record.
     * @param dataSourceCode2 The data source code of the second record.
     * @param recordID2       The record ID of the second record.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByRecordID(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            int maxDegrees,
            StringBuffer response);

    /**
     * Finds a relationship path between entities that are identified by
     * the data source code and record ID of records in each of the entities.
     *
     * @param dataSourceCode1 The data source code of the first record.
     * @param recordID1       The record ID of the first record.
     * @param dataSourceCode2 The data source code of the second record.
     * @param recordID2       The record ID of the second record.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByRecordID(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            int maxDegrees,
            long flags,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's that avoids one or more entities, also identified by
     * their entity ID's.
     *
     * <p>
     * The avoided entities are identified by their entity ID's in a JSON
     * document with the following format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ]
     *   }
     * </pre>
     *
     * @param entityID1       The entity ID of the first entity.
     * @param entityID2       The entity ID of the second entity.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByEntityIDWithAvoids(long entityID1,
            long entityID2,
            int maxDegrees,
            String avoidedEntities,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's that avoids one or more entities, also identified by
     * their entity ID's.
     *
     * <p>
     * The avoided entities are identified by their entity ID's in a JSON
     * document with the following format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ]
     *   }
     * </pre>
     *
     * @param entityID1       The entity ID of the first entity.
     * @param entityID2       The entity ID of the second entity.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByEntityIDWithAvoids(long entityID1,
            long entityID2,
            int maxDegrees,
            String avoidedEntities,
            long flags,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by the
     * data source codes and record IDs of their composite records where
     * that path avoids one or more entities, also identified by the data
     * source codes and record IDs of their composite records.
     *
     * <p>
     * The avoided entities are identified by the data source codes and record
     * ID's of their composite records in a JSON document with the following
     * format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        {
     *          "DATA_SOURCE": "&lt;data_source1&gt;",
     *          "RECORD_ID":  "&lt;record_id1&gt;"
     *        },
     *        {
     *          "DATA_SOURCE": "&lt;data_source2&gt;",
     *          "RECORD_ID":  "&lt;record_id2&gt;"
     *        },
     *        . . .
     *        {
     *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
     *          "RECORD_ID":  "&lt;record_idN&gt;"
     *        }
     *     ]
     *   }
     * </pre>
     *
     * @param dataSourceCode1 The data source code of the first record.
     * @param recordID1       The record ID of the first record.
     * @param dataSourceCode2 The data source code of the second record.
     * @param recordID2       The record ID of the second record.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByRecordIDWithAvoids(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            int maxDegrees,
            String avoidedEntities,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by the data
     * source codes and record IDs of their composite records where that path
     * avoids one or more entities, also identified by the data source codes
     * and record IDs of their composite records.
     *
     * <p>
     * The avoided entities are identified by the data source codes and record
     * ID's of their composite records in a JSON document with the following
     * format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        {
     *          "DATA_SOURCE": "&lt;data_source1&gt;",
     *          "RECORD_ID":  "&lt;record_id1&gt;"
     *        },
     *        {
     *          "DATA_SOURCE": "&lt;data_source2&gt;",
     *          "RECORD_ID":  "&lt;record_id2&gt;"
     *        },
     *        . . .
     *        {
     *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
     *          "RECORD_ID":  "&lt;record_idN&gt;"
     *        }
     *     ]
     *   }
     * </pre>
     *
     * @param dataSourceCode1 The data source code of the first record.
     * @param recordID1       The record ID of the first record.
     * @param dataSourceCode2 The data source code of the second record.
     * @param recordID2       The record ID of the second record.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByRecordIDWithAvoids(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            int maxDegrees,
            String avoidedEntities,
            long flags,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's. The path will avoid the one or more entities, also
     * identified by the specified entity ID's and will require that the
     * path contains <b>at least one</b> of the data sources identified
     * by the one or more specified data sources codes.
     *
     * <p>
     * The avoided entities are identified by their entity ID's in a JSON
     * document with the following format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ]
     *   }
     * </pre>
     *
     * <p>
     * The required set of data sources are identified by their data source codes
     * in a JSON document with the following format:
     * 
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     *
     * @param entityID1       The entity ID of the first entity.
     * @param entityID2       The entity ID of the second entity.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param requiredSources The JSON document identifying the data sources that
     *                        must be included on the path.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByEntityIDIncludingSource(long entityID1,
            long entityID2,
            int maxDegrees,
            String avoidedEntities,
            String requiredSources,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's. The path will avoid the one or more entities, also
     * identified by the specified entity ID's and will require that the
     * path contains <b>at least one</b> of the data sources identified
     * by the one or more specified data sources codes.
     *
     * <p>
     * The avoided entities are identified by their entity ID's in a JSON
     * document with the following format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ]
     *   }
     * </pre>
     *
     * <p>
     * The required set of data sources are identified by their data source codes
     * in a JSON document with the following format:
     * 
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     *
     * @param entityID1       The entity ID of the first entity.
     * @param entityID2       The entity ID of the second entity.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param requiredSources The JSON document identifying the data sources that
     *                        must be included on the path.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByEntityIDIncludingSource(long entityID1,
            long entityID2,
            int maxDegrees,
            String avoidedEntities,
            String requiredSources,
            long flags,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by the data
     * source codes and record IDs of their composite records. The path will
     * avoid the one or more entities also identified by the specified data
     * source code and record ID pairs that identify the composite records of
     * the avoided entities and further will require the path contains
     * <b>at least one</b> of the data sources identified by the one or more
     * specified data sources codes.
     *
     * <p>
     * The avoided entities are identified by the data source codes and record
     * ID's of their composite records in a JSON document with the following
     * format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        {
     *          "DATA_SOURCE": "&lt;data_source1&gt;",
     *          "RECORD_ID":  "&lt;record_id1&gt;"
     *        },
     *        {
     *          "DATA_SOURCE": "&lt;data_source2&gt;",
     *          "RECORD_ID":  "&lt;record_id2&gt;"
     *        },
     *        . . .
     *        {
     *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
     *          "RECORD_ID":  "&lt;record_idN&gt;"
     *        }
     *     ]
     *   }
     * </pre>
     *
     * <p>
     * The required set of data sources are identified by their data source codes
     * in a JSON document with the following format:
     * 
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     *
     * @param dataSourceCode1 The data source code of the first record.
     * @param recordID1       The record ID of the first record.
     * @param dataSourceCode2 The data source code of the second record.
     * @param recordID2       The record ID of the second record.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param requiredSources The JSON document identifying the data sources that
     *                        must be included on the path.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByRecordIDIncludingSource(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            int maxDegrees,
            String avoidedEntities,
            String requiredSources,
            StringBuffer response);

    /**
     * Finds a relationship path between two entities identified by the data
     * source codes and record IDs of their composite records. THe path will
     * avoid the one or more entities also identified by the specified data
     * source code and record ID pairs that identify the composite records of
     * the avoided entities and further will require the path contains
     * <b>at least one</b> of the data sources identified by the one or more
     * specified data sources codes.
     *
     * <p>
     * The avoided entities are identified by the data source codes and record
     * ID's of their composite records in a JSON document with the following
     * format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        {
     *          "DATA_SOURCE": "&lt;data_source1&gt;",
     *          "RECORD_ID":  "&lt;record_id1&gt;"
     *        },
     *        {
     *          "DATA_SOURCE": "&lt;data_source2&gt;",
     *          "RECORD_ID":  "&lt;record_id2&gt;"
     *        },
     *        . . .
     *        {
     *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
     *          "RECORD_ID":  "&lt;record_idN&gt;"
     *        }
     *     ]
     *   }
     * </pre>
     *
     * <p>
     * The required set of data sources are identified by their data source codes
     * in a JSON document with the following format:
     * 
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     *
     * @param dataSourceCode1 The data source code of the first record.
     * @param recordID1       The record ID of the first record.
     * @param dataSourceCode2 The data source code of the second record.
     * @param recordID2       The record ID of the second record.
     * @param maxDegrees      The maximum number of degrees for the path search.
     * @param avoidedEntities The JSON document identifying the avoided entities
     *                        via their entity ID's.
     * @param requiredSources The JSON document identifying the data sources that
     *                        must be included on the path.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findPathByRecordIDIncludingSource(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            int maxDegrees,
            String avoidedEntities,
            String requiredSources,
            long flags,
            StringBuffer response);

    /**
     * Finds a network of entity relationships, surrounding the paths
     * between a set of entities. The entities are identified by their
     * entity IDs.
     *
     * <p>
     * The desired entities are identified by their entity ID's in a JSON
     * document with the following format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ]
     *   }
     * </pre>
     *
     * @param entityList      The JSON document specifying the entity ID's of the
     *                        desired entities.
     * @param maxDegrees      The maximum number of degrees for the path search
     *                        between the specified entities.
     * @param buildOutDegrees The number of relationship degrees to build out
     *                        from each of the found entities.
     * @param maxEntities     The maximum number of entities to build out to.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findNetworkByEntityID(String entityList,
            int maxDegrees,
            int buildOutDegrees,
            int maxEntities,
            StringBuffer response);

    /**
     * Finds a network of entity relationships, surrounding the paths between
     * a set of entities. The entities are identified by their entity IDs.
     *
     * <p>
     * The desired entities are identified by their entity ID's in a JSON
     * document with the following format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ]
     *   }
     * </pre>
     *
     * @param entityList      The JSON document specifying the entity ID's of the
     *                        desired entities.
     * @param maxDegrees      The maximum number of degrees for the path search
     *                        between the specified entities.
     * @param buildOutDegrees The number of relationship degrees to build out
     *                        from each of the found entities.
     * @param maxEntities     The maximum number of entities to build out to.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findNetworkByEntityID(String entityList,
            int maxDegrees,
            int buildOutDegrees,
            int maxEntities,
            long flags,
            StringBuffer response);

    /**
     * Finds a network of entity relationships, surrounding the paths between
     * a set of entities. The entities are identified by their composite
     * records having the specified data source code and record ID pairs.
     *
     * <p>
     * The composite records af the desired entities are identified by the
     * data source code and record ID pairs in a JSON document with the following
     * format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        {
     *          "DATA_SOURCE": "&lt;data_source1&gt;",
     *          "RECORD_ID":  "&lt;record_id1&gt;"
     *        },
     *        {
     *          "DATA_SOURCE": "&lt;data_source2&gt;",
     *          "RECORD_ID":  "&lt;record_id2&gt;"
     *        },
     *        . . .
     *        {
     *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
     *          "RECORD_ID":  "&lt;record_idN&gt;"
     *        }
     *     ]
     *   }
     * </pre>
     *
     * @param recordList      The JSON document containing the data source code and
     *                        record ID pairs for the composite records of the
     *                        desired
     *                        entities.
     * @param maxDegrees      The maximum number of degrees for the path search
     *                        between the specified entities.
     * @param buildOutDegrees The number of relationship degrees to build out
     *                        from each of the found entities.
     * @param maxEntities     The maximum number of entities to build out to.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findNetworkByRecordID(String recordList,
            int maxDegrees,
            int buildOutDegrees,
            int maxEntities,
            StringBuffer response);

    /**
     * Finds a network of entity relationships, surrounding the paths between
     * a set of entities. The entities are identified by their composite
     * records having the specified data source code and record ID pairs.
     *
     * <p>
     * The composite records af the desired entities are identified by the
     * data source code and record ID pairs in a JSON document with the following
     * format:
     * 
     * <pre>
     *   {
     *     "ENTITIES": [
     *        {
     *          "DATA_SOURCE": "&lt;data_source1&gt;",
     *          "RECORD_ID":  "&lt;record_id1&gt;"
     *        },
     *        {
     *          "DATA_SOURCE": "&lt;data_source2&gt;",
     *          "RECORD_ID":  "&lt;record_id2&gt;"
     *        },
     *        . . .
     *        {
     *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
     *          "RECORD_ID":  "&lt;record_idN&gt;"
     *        }
     *     ]
     *   }
     * </pre>
     *
     * @param recordList      The JSON document containing the data source code and
     *                        record ID pairs for the composite records of the
     *                        desired
     *                        entities.
     * @param maxDegrees      The maximum number of degrees for the path search
     *                        between the specified entities.
     * @param buildOutDegrees The number of relationship degrees to build out
     *                        from each of the found entities.
     * @param maxEntities     The maximum number of entities to build out to.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int findNetworkByRecordID(String recordList,
            int maxDegrees,
            int buildOutDegrees,
            int maxEntities,
            long flags,
            StringBuffer response);

    /**
     * Determines why a particular record is included in its resolved entity.
     *
     * @param dataSourceCode The data source code for the composite record of the
     *                       subject entity.
     * @param recordID       The record ID for the composite record of the subject
     *                       entity.
     * @param response       The {@link StringBuffer} to write the JSON response
     *                       document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecordInEntity(String dataSourceCode,
            String recordID,
            StringBuffer response);

    /**
     * Determines why a particular record is included in its resolved entity.
     *
     * @param dataSourceCode The data source code for the composite record of the
     *                       subject entity.
     * @param recordID       The record ID for the composite record of the subject
     *                       entity.
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       The {@link StringBuffer} to write the JSON response
     *                       document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecordInEntity(String dataSourceCode,
            String recordID,
            long flags,
            StringBuffer response);

    /**
     * Determines how two records are related to each other.
     *
     * @param dataSourceCode1 The data source code for the first record.
     * @param recordID1       The record ID for the first record.
     * @param dataSourceCode2 The data source code for the second record.
     * @param recordID2       The record ID for the second record.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecords(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            StringBuffer response);

    /**
     * Determines how two records are related to each other.
     *
     * @param dataSourceCode1 The data source code for the first record.
     * @param recordID1       The record ID for the first record.
     * @param dataSourceCode2 The data source code for the second record.
     * @param recordID2       The record ID for the second record.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecords(String dataSourceCode1,
            String recordID1,
            String dataSourceCode2,
            String recordID2,
            long flags,
            StringBuffer response);

    /**
     * Determines how two entities are related to each other.
     *
     * @param entityID1 The entity ID of the first entity.
     * @param entityID2 The entity ID of the second entity.
     * @param response  The {@link StringBuffer} to write the JSON response
     *                  document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyEntities(long entityID1, long entityID2, StringBuffer response);

    /**
     * Determines how two entities are related to each other.
     *
     * @param entityID1 The entity ID of the first entity.
     * @param entityID2 The entity ID of the second entity.
     * @param flags     The flags to control how the operation is performed and
     *                  specifically the content of the response JSON document.
     * @param response  The {@link StringBuffer} to write the JSON response
     *                  document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyEntities(long entityID1,
            long entityID2,
            long flags,
            StringBuffer response);

    /**
     * Describes how an entity was constructed from its base records.
     *
     * @param entityID The entity ID of the entity.
     * @param response The {@link StringBuffer} to write the JSON response
     *                 document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int howEntityByEntityID(long entityID,
            StringBuffer response);

    /**
     * Describes how an entity was constructed from its base records.
     *
     * @param entityID The entity ID.
     * @param flags    The flags to control how the operation is performed and
     *                 specifically the content of the response JSON document.
     * @param response The {@link StringBuffer} to write the JSON response
     *                 document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int howEntityByEntityID(long entityID,
            long flags,
            StringBuffer response);

    /**
     * Provides a description of a hypothetical entity composed of the
     * specified records.
     *
     * @param recordList The list of records used to build the virtual entity.
     * @param response   The {@link StringBuffer} to write the JSON response
     *                   document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int getVirtualEntityByRecordID(String recordList,
            StringBuffer response);

    /**
     * Provides a description of a hypothetical entity composed of the
     * specified records using the specified flags.
     *
     * @param recordList The list of records used to build the virtual entity.
     * @param flags      The flags to control how the operation is performed and
     *                   specifically the content of the response JSON document.
     * @param response   The {@link StringBuffer} to write the JSON response
     *                   document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int getVirtualEntityByRecordID(String recordList,
            long flags,
            StringBuffer response);

    /**
     * Retrieves a record for a given data source code and record ID.
     * 
     * @param dataSourceCode The data source of the record to search for
     * @param recordID       The record ID of the record to search for
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *                       If an error occurred, an error response is stored here.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getRecord(String dataSourceCode, String recordID, StringBuffer response);

    /**
     * Retrieves a record for a given data source code and record ID using
     * the specified flags.
     * 
     * @param dataSourceCode The data source of the record to search for
     * @param recordID       The record ID of the record to search for
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *                       If an error occurred, an error response is stored here.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getRecord(String dataSourceCode,
            String recordID,
            long flags,
            StringBuffer response);

    /**
     * Exports entity data in JSON format from known entities in the repository.
     * This function returns an export-handle that can be read from to get the
     * export data in JSON format. The export-handle should be read using the
     * {@link #fetchNext(long, StringBuffer)} function, and {@linkplain
     * #closeExportReport(long) closed} when work is complete. Each output row contains
     * the exported entity data for a single resolved entity.
     *
     * @param flags        A bit mask specifying control flags. The default and
     *                     recommended
     *                     value is "SZ_EXPORT_DEFAULT_FLAGS".
     * @param exportHandle The {@link Result} object for storing the export
     *                     handle.
     * @return Zero (0) on success and non-zero on failure.
     */
    int exportJSONEntityReport(long flags, Result<Long> exportHandle);

    /**
     * Exports entity data in CSV format from known entities in the repository.
     * This function returns an export-handle that can be read from to get the
     * export data in CSV format. The export-handle should be read using the
     * {@link #fetchNext(long, StringBuffer)} function, and {@linkplain
     * #closeExportReport(long) closed} when work is complete. The first output row
     * returned by the export-handle contains the JSON column headers as a string.
     * Each following row contains the exported entity data.
     *
     * @param csvColumnList Specify <code>"*"</code> to indicate "all columns",
     *                      specify empty-string to indicate the "standard
     *                      columns", otherwise specify a comma-separated list of
     *                      column names.
     * @param flags         A bit mask specifying other control flags. The default
     *                      and recommended
     *                      value is "SZ_EXPORT_DEFAULT_FLAGS".
     * @param exportHandle  The {@link Result} object for storing the export
     *                      handle.
     *
     * @return Returns an export handle that the entity data can be read from.
     */
    int exportCSVEntityReport(String csvColumnList,
            long flags,
            Result<Long> exportHandle);

    /**
     * Reads the next chunk of entity data from an export handle, one
     * entity at a time.
     *
     * @param exportHandle The export handle to retrieve data from
     * @param response     The {@link StringBuffer} to write the next exported
     *                     record to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int fetchNext(long exportHandle, StringBuffer response);

    /**
     * Closes an export handle to clean up system resources.
     * 
     * @param exportHandle The export handle of the export to close.
     * @return Zero (0) on success and non-zero on failure.
     */
    int closeExportReport(long exportHandle);

    /**
     * Processes a redo record.
     *
     * @param redoRecord The record to be processed.
     * @return Zero (0) on success and non-zero on failure.
     */
    int processRedoRecord(String redoRecord);

    /**
     * Processes a redo record.
     *
     * @param redoRecord The record to be processed.
     * @param response   A {@link StringBuffer} for returning the response document.
     *                   If an error occurred, an error response is stored here.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int processRedoRecordWithInfo(String redoRecord,
            StringBuffer response);

    /**
     * Retrieves a pending redo record from the reevaluation queue.
     *
     * @param jsonData A {@link StringBuffer} to write the redo record to.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getRedoRecord(StringBuffer jsonData);

    /**
     * Gets the number of redo records waiting to be processed.
     *
     * @return The number of redo records waiting to be processed.
     */
    long countRedoRecords();
}
