package com.senzing.sdk.core;

import java.util.Set;

import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzFlag;
import com.senzing.sdk.SzFlags;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzRecordKeys;
import com.senzing.sdk.SzEntityIds;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzUnknownDataSourceException;
import com.senzing.sdk.SzBadInputException;
import com.senzing.sdk.SzNotFoundException;

import static com.senzing.sdk.core.Utilities.jsonEscape;
import static com.senzing.sdk.SzFlag.*;

/**
 * The package-protected implementation of {@link SzEngine} that works
 * with the {@link SzCoreEnvironment} class.
 */
public class SzCoreEngine implements SzEngine {
    /**
     * The mask for removing SDK-specific flags that don't go downstream.
     */
    private static final long SDK_FLAG_MASK = ~(SzFlags.SZ_WITH_INFO);

    /**
     * The empty response for operations where the info can optionally
     * generated but was not requested.
     */
    static final String NO_INFO = "{}";

    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link NativeEngineJni} instance.
     */
    private NativeEngineJni nativeApi = null;

    /**
     * Internal object for instance-wide synchronized locking.
     */
    private final Object monitor = new Object();

    /**
     * Constructs with the specified {@link SzCoreEnvironment}.
     * 
     * @param environment
     *            The {@link SzCoreEnvironment} with which to
     *            construct.
     * 
     * @throws IllegalStateException
     *             If the underlying {@link SzCoreEnvironment} instance
     *             has already been destroyed.
     * @throws SzException
     *             If a Senzing failure occurs during initialization.
     */
    SzCoreEngine(SzCoreEnvironment environment)
            throws IllegalStateException, SzException {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new NativeEngineJni();

            // check if we are initializing with a config ID
            if (this.env.getConfigId() == null) {
                // if not then call the basic init
                int returnCode = this.nativeApi.init(
                        this.env.getInstanceName(),
                        this.env.getSettings(),
                        this.env.isVerboseLogging());

                // handle any failure
                this.env.handleReturnCode(returnCode, this.nativeApi);

            } else {
                // if so then call init with config ID
                int returnCode = this.nativeApi.initWithConfigID(
                        this.env.getInstanceName(),
                        this.env.getSettings(),
                        this.env.getConfigId(),
                        this.env.isVerboseLogging());

                // handle any failure
                this.env.handleReturnCode(returnCode, this.nativeApi);
            }

            return null;
        });
    }


    /**
     * Gets the associated {@link NativeEngineJni} instance.
     * 
     * @return The associated {@link NativeEngineJni} instance.
     */
    NativeEngineJni getNativeApi() {
        return this.nativeApi;
    }

    /**
     * The package-protected function to destroy the Senzing Engine SDK.
     */
    void destroy() {
        synchronized (this.monitor) {
            if (this.nativeApi == null) {
                return;
            }
            this.nativeApi.destroy();
            this.nativeApi = null;
        }
    }

    /**
     * Checks if this instance has been destroyed by the associated
     * {@link SzCoreEnvironment}.
     * 
     * @return <code>true</code> if this instance has been destroyed,
     *         otherwise <code>false</code>.
     */
    protected boolean isDestroyed() {
        synchronized (this.monitor) {
            return (this.nativeApi == null);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String addRecord(SzRecordKey recordKey,
                            String      recordDefinition,
                            Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzBadInputException, 
               SzException
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;
            // check if we have flags to pass downstream or need info
            if (downstreamFlags == 0L
                && (flags == null || !flags.contains(SZ_WITH_INFO)))
            {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.addRecord(
                    recordKey.dataSourceCode(),
                    recordKey.recordId(),
                    recordDefinition);

            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.addRecordWithInfo(
                    recordKey.dataSourceCode(),
                    recordKey.recordId(),
                    recordDefinition, 
                    downstreamFlags, 
                    sb);

                // set the info result if requested
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    result = sb.toString();
                }
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return result;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String preprocessRecord(String       recordDefinition,
                                   Set<SzFlag>  flags)
        throws SzException
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            StringBuffer sb = new StringBuffer();
            
            int returnCode = this.nativeApi.preprocessRecord(
                    recordDefinition, downstreamFlags, sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public void closeExport(long exportHandle) throws SzException {
        this.env.execute(() -> {
            int returnCode = this.nativeApi.closeExport(exportHandle);

            this.env.handleReturnCode(returnCode, this.nativeApi);

            // no return value, return null
            return null;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public long countRedoRecords() throws SzException {
        return this.env.execute(() -> {
            long count = this.nativeApi.countRedoRecords();

            if (count < 0L) {
                this.env.handleReturnCode((int) count, this.nativeApi);
            }

            // return the count
            return count;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String deleteRecord(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;
            // check if we have flags to pass downstream or need info
            if (downstreamFlags == 0L 
                && (flags == null || !flags.contains(SZ_WITH_INFO))) 
            {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.deleteRecord(
                    recordKey.dataSourceCode(),
                    recordKey.recordId());

            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.deleteRecordWithInfo(
                    recordKey.dataSourceCode(),
                    recordKey.recordId(), 
                    downstreamFlags,
                    sb);

                // set the info result if requested
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    result = sb.toString();
                }
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return result;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public long exportCsvEntityReport(String csvColumnList, Set<SzFlag> flags)
            throws SzException 
    {
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            Result<Long> result = new Result<>();

            int returnCode = this.nativeApi.exportCSVEntityReport(
                csvColumnList, downstreamFlags, result);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the export handle
            return result.getValue();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public long exportJsonEntityReport(Set<SzFlag> flags) throws SzException {
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            Result<Long> result = new Result<>();

            int returnCode = this.nativeApi.exportJSONEntityReport(
                downstreamFlags, result);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the export handle
            return result.getValue();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String fetchNext(long exportHandle) throws SzException {
        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            int returnCode = this.nativeApi.fetchNext(exportHandle, sb);

            this.env.handleReturnCode(returnCode, this.nativeApi);

            // get the result
            String result = sb.toString();
            if (result.length() == 0) {
                result = null;
            }

            // return the next export chunk
            return result;
        });
    }

    /**
     * Encodes the {@link Set} of {@link Long} entity ID's as JSON.
     * The JSON is formatted as:
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
     * @param entityIds The non-null {@link Set} of non-null {@link Long}
     *                  entity ID's.
     * 
     * @return The encoded JSON string of entity ID's.
     */
    protected static String encodeEntityIds(Set<Long> entityIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ENTITIES\":[");
        if (entityIds != null) {
            String prefix = "";
            for (Long entityId : entityIds) {
                sb.append(prefix);
                sb.append("{\"ENTITY_ID\":").append(entityId).append("}");
                prefix = ",";
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * Encodes the {@link Set} of {@link SzRecordKey} instances as JSON.
     * The JSON is formatted as:
     * <pre>
     *   {
     *     "RECORDS": [
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
     * @param recordKeys The non-null {@link Set} of non-null
     *                   {@link SzRecordKey} instances.
     * 
     * @return The encoded JSON string of record keys.
     */
    protected static String encodeRecordKeys(Set<SzRecordKey> recordKeys) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"RECORDS\":[");
        if (recordKeys != null) {
            String prefix = "";
            for (SzRecordKey recordKey : recordKeys) {
                String dataSourceCode   = (recordKey == null) ? null : recordKey.dataSourceCode();
                String recordId         = (recordKey == null) ? null : recordKey.recordId();
                sb.append(prefix);
                sb.append("{\"DATA_SOURCE\":");
                sb.append(jsonEscape(dataSourceCode));
                sb.append(",\"RECORD_ID\":");
                sb.append(jsonEscape(recordId));
                sb.append("}");
                prefix = ",";
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * Encodes the {@link Set} of {@link String} data source codes
     * as JSON.  The JSON is formatted as:
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     * @param dataSources The {@link Set} of {@link String} data source codes.
     * 
     * @return The encoded JSON string of record keys.
     */
    protected static String encodeDataSources(Set<String> dataSources) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"DATA_SOURCES\":[");
        if (dataSources != null) {
            String prefix = "";
            for (String dataSourceCode : dataSources) {
                sb.append(prefix);
                sb.append(jsonEscape(dataSourceCode));
                prefix = ",";
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String findNetwork(SzEntityIds   entityIds, 
                              int           maxDegrees,
                              int           buildOutDegrees,
                              int           buildOutMaxEntities,
                              Set<SzFlag>   flags)
            throws SzNotFoundException, SzException 
    {        
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            String jsonEntityIds = encodeEntityIds(entityIds);

            int returnCode = this.nativeApi.findNetworkByEntityID(
                jsonEntityIds,
                maxDegrees,
                buildOutDegrees,
                buildOutMaxEntities,
                downstreamFlags,
                sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String findNetwork(SzRecordKeys  recordKeys,
                              int           maxDegrees,
                              int           buildOutDegrees,
                              int           buildOutMaxEntities,
                              Set<SzFlag>   flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {        
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            String jsonRecordKeys = encodeRecordKeys(recordKeys);

            int returnCode = this.nativeApi.findNetworkByRecordID(
                jsonRecordKeys,
                maxDegrees,
                buildOutDegrees,
                buildOutMaxEntities,
                downstreamFlags,
                sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String findPath(long         startEntityId, 
                           long         endEntityId,
                           int          maxDegrees,
                           SzEntityIds  avoidEntityIds,
                           Set<String>  requiredDataSources,
                           Set<SzFlag>  flags)
        throws SzNotFoundException,
               SzUnknownDataSourceException,
               SzException
    {
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            int returnCode = 0;
            
            if ((avoidEntityIds == null || avoidEntityIds.isEmpty())
                && (requiredDataSources == null || requiredDataSources.isEmpty()))
            {
                // call the base function
                returnCode = this.nativeApi.findPathByEntityID(
                    startEntityId, endEntityId, maxDegrees, downstreamFlags, sb);

            } else if (requiredDataSources == null || requiredDataSources.isEmpty()) {
                // encode the entity ID's
                String avoidanceJson = encodeEntityIds(avoidEntityIds);

                // call the variant with avoidances, but without required data sources
                returnCode = this.nativeApi.findPathByEntityIDWithAvoids(
                    startEntityId, endEntityId, maxDegrees, avoidanceJson, 
                    downstreamFlags, sb);

            } else {
                // encode the entity ID's
                String avoidanceJson = encodeEntityIds(avoidEntityIds);

                // encode the data sources
                String dataSourceJson = encodeDataSources(requiredDataSources);

                // we have to call the full-blown variant of the function
                returnCode = this.nativeApi.findPathByEntityIDIncludingSource(
                    startEntityId, endEntityId, maxDegrees, avoidanceJson, 
                    dataSourceJson, downstreamFlags, sb);
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String findPath(SzRecordKey  startRecordKey,
                           SzRecordKey  endRecordKey,
                           int          maxDegrees,
                           SzRecordKeys avoidRecordKeys,
                           Set<String>  requiredDataSources,
                           Set<SzFlag>  flags)
            throws SzNotFoundException,
                   SzUnknownDataSourceException,
                   SzException
    {
        // clear out the SDK-specific flags
        long downstreamFlags = (SzFlag.toLong(flags) & SDK_FLAG_MASK);

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            int returnCode = 0;
            if ((avoidRecordKeys == null || avoidRecordKeys.isEmpty())
                && (requiredDataSources == null || requiredDataSources.isEmpty()))
            {
                // call the base function
                returnCode = this.nativeApi.findPathByRecordID(
                    startRecordKey.dataSourceCode(), 
                    startRecordKey.recordId(), 
                    endRecordKey.dataSourceCode(), 
                    endRecordKey.recordId(),
                    maxDegrees,
                    downstreamFlags,
                    sb);

            } else if (requiredDataSources == null || requiredDataSources.isEmpty()) {
                // encode the entity ID's
                String avoidanceJson = encodeRecordKeys(avoidRecordKeys);

                // call the variant with avoidances, but without required data sources
                returnCode = this.nativeApi.findPathByRecordIDWithAvoids(
                    startRecordKey.dataSourceCode(), 
                    startRecordKey.recordId(), 
                    endRecordKey.dataSourceCode(), 
                    endRecordKey.recordId(),
                    maxDegrees,
                    avoidanceJson,
                    downstreamFlags,
                    sb);
            
            } else {
                // encode the entity ID's
                String avoidanceJson = encodeRecordKeys(avoidRecordKeys);

                // encode the data sources
                String dataSourceJson = encodeDataSources(requiredDataSources);

                // we have to call the full-blown variant of the function
                returnCode = this.nativeApi.findPathByRecordIDIncludingSource(
                    startRecordKey.dataSourceCode(), 
                    startRecordKey.recordId(), 
                    endRecordKey.dataSourceCode(), 
                    endRecordKey.recordId(),
                    maxDegrees,
                    avoidanceJson,
                    dataSourceJson,
                    downstreamFlags,
                    sb);
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getEntity(long entityId, Set<SzFlag> flags)
            throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getEntityByEntityID(
                entityId, downstreamFlags, sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getEntity(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException,
                   SzNotFoundException,
                   SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getEntityByRecordID(
                recordKey.dataSourceCode(),
                recordKey.recordId(),
                downstreamFlags,
                sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getRecord(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getRecord(
                recordKey.dataSourceCode(),
                recordKey.recordId(),
                downstreamFlags,
                sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);
            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getRedoRecord() throws SzException {
        return this.env.execute(() -> {
            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getRedoRecord(sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            String redoRecord = sb.toString();
            return (redoRecord.length() == 0) ? null : redoRecord;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getStats() throws SzException {
        return this.env.execute(() -> {
            return this.nativeApi.stats();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String getVirtualEntity(Set<SzRecordKey> recordKeys,
                                   Set<SzFlag>      flags)
        throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // get the record ID JSON
            String jsonRecordString = encodeRecordKeys(recordKeys);

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getVirtualEntityByRecordID(
                jsonRecordString, downstreamFlags, sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String howEntity(long entityId, Set<SzFlag> flags)
            throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.howEntityByEntityID(
                entityId, downstreamFlags, sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public void primeEngine() throws SzException {
        this.env.execute(() -> {
            // check if we have flags to pass downstream
            int returnCode = this.nativeApi.primeEngine();

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return null;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String processRedoRecord(String redoRecord, Set<SzFlag> flags)
            throws SzException 
    {
        return this.env.execute(() -> {
            int returnCode = 0;
            String result = NO_INFO;
            // check if we have flags to pass downstream
            if (flags == null || !flags.contains(SZ_WITH_INFO)) {
                returnCode = this.nativeApi.processRedoRecord(redoRecord);

            } else {
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.processRedoRecordWithInfo(redoRecord, sb);
                result = sb.toString();
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return result;
        });

    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String reevaluateEntity(long entityId, Set<SzFlag> flags)
            throws SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;

            // check if we have flags to pass downstream or need info
            if (flags == null || !flags.contains(SZ_WITH_INFO)) {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.reevaluateEntity(
                    entityId, downstreamFlags);
                
            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.reevaluateEntityWithInfo(
                    entityId, downstreamFlags, sb);
                    
                // set the info result if requested
                result = sb.toString();

                // check if record not found yields empty INFO
                if (result.length() == 0) {
                    result = NO_INFO;
                }
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return result;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String reevaluateRecord(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;

            // check if we have flags to pass downstream or need info
            if (flags == null || !flags.contains(SZ_WITH_INFO)) {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.reevaluateRecord(
                    recordKey.dataSourceCode(),
                    recordKey.recordId(),
                    downstreamFlags);
                
            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.reevaluateRecordWithInfo(
                    recordKey.dataSourceCode(),
                    recordKey.recordId(),
                    downstreamFlags,
                    sb);
                    
                // set the info result
                result = sb.toString();

                // TODO(bcaceres): remove this if not-found records produce an error
                // check if record not found yields empty INFO
                if (result.length() == 0) {
                    result = NO_INFO;
                }
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return result;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String searchByAttributes(String         attributes,
                                     String         searchProfile,
                                     Set<SzFlag>    flags) 
        throws SzException
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // declare the result variables
            StringBuffer    sb          = new StringBuffer();
            int             returnCode  = 0;
            
            // check if have a search profile
            if (searchProfile == null) {
                // search with the default search profile
                returnCode = this.nativeApi.searchByAttributes(
                    attributes, downstreamFlags, sb);

            } else {
                returnCode = this.nativeApi.searchByAttributes(
                    attributes, searchProfile, downstreamFlags, sb);
            }

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String searchByAttributes(String attributes, Set<SzFlag> flags) 
        throws SzException
    {
        return this.searchByAttributes(attributes, null, flags);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String whyEntities(long entityId1, long entityId2, Set<SzFlag> flags)
            throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.whyEntities(
                entityId1, entityId2, downstreamFlags, sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String whyRecordInEntity(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.whyRecordInEntity(
                recordKey.dataSourceCode(),
                recordKey.recordId(),
                downstreamFlags,
                sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implemented to call the underlying native API.
     */
    @Override
    public String whyRecords(SzRecordKey recordKey1, 
                             SzRecordKey recordKey2,
                             Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.whyRecords(
                recordKey1.dataSourceCode(),
                recordKey1.recordId(),
                recordKey2.dataSourceCode(),
                recordKey2.recordId(),
                downstreamFlags,
                sb);

            // check the return code
            this.env.handleReturnCode(returnCode, this.nativeApi);

            // return the result
            return sb.toString();
        });
    }
}
