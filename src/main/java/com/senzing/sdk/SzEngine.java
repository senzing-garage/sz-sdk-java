package com.senzing.sdk;

import static com.senzing.sdk.SzFlag.*;

import java.util.Set;

/**
 * <p>
 * Defines the Java interface to the Senzing engine functions.  The Senzing
 * engine functions primarily provide means of working with identity data
 * records, entities and their relationships.
 * </p>
 * 
 * <p>
 * An {@link SzEngine} instance is typically obtained from an {@link SzEnvironment}
 * instance via the {@link SzEnvironment#getEngine()} method as follows:
 *
 * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getEngine"}
 * </p>
 * 
 * @since 4.0.0
 */
public interface SzEngine {
    /**
     * Pre-loads engine resources.
     * 
     * <p>
     * Explicitly calling this method ensures the performance cost is incurred
     * at a predictable time rather than unexpectedly with the first call
     * requiring the resources.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="primeEngine"}
     * </p>
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/initialization/EnginePriming.java">Code Snippet: Engine Priming</a>
     * 
     * @since 4.0.0
     */
    void primeEngine() throws SzException;

    /**
     * Gets and resets the internal engine workload statistics for the
     * current operating system process.
     * 
     * <p>
     * The output is helpful when interacting with Senzing support.
     * Best practice to periodically log the results.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getStats"}
     * </p>
     *
     * <p><b>Example Result:</b><br>
     * The example result is rather large, but can viewed 
     * <a target="_blank" href="doc-files/SzEngineDemo-getStats.txt">here</a> 
     * (formatted for readability).
     * </p>
     * 
     * @return The {@link String} describing the statistics as JSON.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadWithStatsViaLoop.java">Code Snippet: Load with Stats</a>
     * 
     * @since 4.0.0
     */
    String getStats() throws SzException;

    /**
     * Loads a record into the repository and performs entity resolution.
     * 
     * <p>
     * If a record already exists with the same data source code and
     * record ID, it will be replaced.  If the record definition contains
     * <code>DATA_SOURCE</code> and <code>RECORD_ID</code> JSON keys, the 
     * values must match the <code>dataSourceCode</code> and 
     * <code>recordId</code> parameters.
     * </p>
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS} group will be recognized (other 
     * {@link SzFlag} instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="addRecord"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-addRecord.txt"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record ID of the record being added.
     * 
     * @param recordDefinition The {@link String} that defines the record, typically
     *                         in JSON format.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS} group to
     *              control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link
     *              SzFlag#SZ_NO_FLAGS} or {@link SzFlag#SZ_WITH_INFO_FLAGS}
     *              for an INFO response.  {@link SzFlag#SZ_ADD_RECORD_DEFAULT_FLAGS}
     *              is also available if you desire to use recommended defaults.
     * 
     * @return The JSON {@link String} result produced by adding the record to the
     *         repository, or <code>null</code> if the specified flags do not 
     *         indicate that an INFO message should be returned.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzBadInputException If the specified record definition has a data source
     *                             or record ID value that conflicts with the specified
     *                             data source code and/or record ID values.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlag#SZ_ADD_RECORD_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ADD_RECORD_FLAGS
     * 
     * @see #addRecord(SzRecordKey, String)
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadRecords.java">Code Snippet: Load Records</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadTruthSetWithInfoViaLoop.java">Code Snippet: Load Truth Set "With Info"</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadViaFutures.java">Code Snippet: Load via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadViaLoop.java">Code Snippet: Load via Loop</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadViaQueue.java">Code Snippet: Load via Queue</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadWithInfoViaFutures.java">Code Snippet: Load "With Info" via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadWithStatsViaLoop.java">Code Snippet: Load "With Stats" Via Loop</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String addRecord(SzRecordKey        recordKey,
                     String             recordDefinition,
                     Set<SzFlag>        flags)
        throws SzUnknownDataSourceException, SzBadInputException, SzException;

    /**
     * Convenience method for calling {@link #addRecord(SzRecordKey, String, Set)}
     * using {@link SzFlag#SZ_ADD_RECORD_DEFAULT_FLAGS} as the value for
     * the <code>flags</code> parameter.  See the {@link
     * #addRecord(SzRecordKey, String, Set)} documentation for details.
     * <p>
     * <b>NOTE:</b> The {@link String} return type is still used despite the
     * fact that in the current version this will always return <code>null</code>
     * due to {@link SzFlag#SZ_ADD_RECORD_DEFAULT_FLAGS} being equivalent to 
     * {@link SzFlag#SZ_NO_FLAGS}.  However, having a <code>void</code> return 
     * type would cause incompatibilities if a future release introduced a
     * different value for {@link SzFlag#SZ_ADD_RECORD_DEFAULT_FLAGS} that did 
     * trigger a non-null return value.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="addRecordDefault"}
     * </p>
     *
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record ID of the record being added.
     * 
     * @param recordDefinition The {@link String} that defines the record, typically
     *                         in JSON format.
     * 
     * @return The JSON {@link String} result produced by adding the record to the
     *         repository, which will always be <code>null</code> in the current
     *         version (see above).
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzBadInputException If the specified record definition has a data source
     *                             or record ID value that conflicts with the specified
     *                             data source code and/or record ID values.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_ADD_RECORD_DEFAULT_FLAGS
     * 
     * @see #addRecord(SzRecordKey, String, Set)
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadRecords.java">Code Snippet: Load Records</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadTruthSetWithInfoViaLoop.java">Code Snippet: Load Truth Set "With Info"</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadViaFutures.java">Code Snippet: Load via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadViaLoop.java">Code Snippet: Load via Loop</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadViaQueue.java">Code Snippet: Load via Queue</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadWithInfoViaFutures.java">Code Snippet: Load "With Info" via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/loading/LoadWithStatsViaLoop.java">Code Snippet: Load "With Stats" Via Loop</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String addRecord(SzRecordKey        recordKey,
                             String             recordDefinition)
        throws SzUnknownDataSourceException, SzBadInputException, SzException
    {
        return this.addRecord(recordKey, recordDefinition, SZ_ADD_RECORD_DEFAULT_FLAGS);
    }

    /**
     * Describes the features resulting from the hypothetical load of a record.
     * 
     * <p>
     * This method is used to preview the features for a record that has not been
     * loaded.
     * </p>
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS} group will be recognized
     * (other {@link SzFlag} instances will be ignored).
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getRecordPreview"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getRecordPreview.txt"}
     * </p>
     * 
     * @param recordDefinition The {@link String} that defines the record, typically
     *                         in JSON format.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS} group
     *              to control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link 
     *              SzFlag#SZ_NO_FLAGS} or {@link SzFlag#SZ_RECORD_PREVIEW_DEFAULT_FLAGS} 
     *              for the default recommended flags.
     * 
     * @return The JSON {@link String} record preview (depending on the specified flags).
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_RECORD_PREVIEW_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_RECORD_PREVIEW_FLAGS
     * 
     * @see #getRecordPreview(String)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String getRecordPreview(String recordDefinition, Set<SzFlag> flags)
        throws SzException;

    /**
     * Convenience method for calling {@link #getRecordPreview(String, Set)}
     * using {@link SzFlag#SZ_RECORD_PREVIEW_DEFAULT_FLAGS} as the value
     * for the <code>flags</code> parameter.  See the {@link
     * #getRecordPreview(String, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getRecordPreviewDefault"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getRecordPreviewDefault.txt"}
     * </p>
     * 
     * @param recordDefinition The {@link String} that defines the record, typically
     *                         in JSON format.
     * 
     * @return The JSON {@link String} record preview using the default flags.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_RECORD_PREVIEW_DEFAULT_FLAGS
     * 
     * @see #getRecordPreview(String, Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String getRecordPreview(String recordDefinition)
        throws SzException 
    {
        return this.getRecordPreview(recordDefinition, SZ_RECORD_PREVIEW_DEFAULT_FLAGS);
    }

    /**
     * Deletes a record from the repository and performs entity resolution.
     * 
     * <p><b>NOTE:</b> This method is idempotent in that it succeeds with
     * no changes being made when the record is not found in the repository.
     * </p>
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS} group will be recognized
     * (other {@link SzFlag} instances will be ignored).  <b>NOTE:</b>
     * {@link java.util.EnumSet} offers an efficient means of constructing
     * a {@link Set} of {@link SzFlag}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="deleteRecord"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-deleteRecord.txt"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the record to delete.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS} group to
     *              control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link
     *              SzFlag#SZ_NO_FLAGS} or {@link SzFlag#SZ_WITH_INFO_FLAGS}
     *              for an INFO response.  {@link SzFlag#SZ_DELETE_RECORD_DEFAULT_FLAGS}
     *              is also available if you desire to use recommended defaults.
     *
     * @return The JSON {@link String} result produced by deleting the record from
     *         the repository, or <code>null</code> if the specified flags do not 
     *         indicate that an INFO message should be returned.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlag#SZ_DELETE_RECORD_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_DELETE_RECORD_FLAGS
     * 
     * @see #deleteRecord(SzRecordKey)
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/deleting/DeleteViaLoop.java">Code Snippet: Delete via Loop</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/deleting/DeleteViaFutures.java">Code Snippet: Delete via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/deleting/DeleteWithInfoViaFutures.java">Code Snippet: Delete "With Info" via Futures</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String deleteRecord(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzException;

    /**
     * Convenience method for calling {@link #deleteRecord(SzRecordKey, Set)}
     * using {@link SzFlag#SZ_DELETE_RECORD_DEFAULT_FLAGS} as the value for
     * the <code>flags</code> parameter.  See the {@link
     * #deleteRecord(SzRecordKey, Set)} documentation for details.
     * <p>
     * <b>NOTE:</b> The {@link String} return type is still used despite the
     * fact that in the current version this will always return <code>null</code>
     * due to {@link SzFlag#SZ_DELETE_RECORD_DEFAULT_FLAGS} being equivalent to 
     * {@link SzFlag#SZ_NO_FLAGS}.  However, having a <code>void</code> return 
     * type would cause incompatibilities if a future release introduced a
     * different value for {@link SzFlag#SZ_DELETE_RECORD_DEFAULT_FLAGS} that did 
     * trigger a non-null return value.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="deleteRecordDefault"}
     * </p>
     *
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the record to delete.
     * 
     * @return The JSON {@link String} result produced by deleting the record from
     *         the repository, which will always be <code>null</code> in the
     *         current version (see above).
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_DELETE_RECORD_DEFAULT_FLAGS
     * 
     * @see #deleteRecord(SzRecordKey, Set)
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/deleting/DeleteViaLoop.java">Code Snippet: Delete via Loop</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/deleting/DeleteViaFutures.java">Code Snippet: Delete via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/deleting/DeleteWithInfoViaFutures.java">Code Snippet: Delete "With Info" via Futures</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String deleteRecord(SzRecordKey recordKey)
        throws SzUnknownDataSourceException, SzException 
    {
        return this.deleteRecord(recordKey, SZ_DELETE_RECORD_DEFAULT_FLAGS);
    }

    /**
     * Reevaluates an entity by record ID.
     * 
     * <p>
     * This operation performs entity resolution.  If the record is not found,
     * then no changes are made.
     * </p>
     *
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS} group will be recognized (other
     * {@link SzFlag} instances will be ignored).  <b>NOTE:</b>
     * {@link java.util.EnumSet} offers an efficient means of constructing a
     * {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="reevaluateRecord"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-reevaluateRecord.txt"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the record to reevaluate.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS} group
     *              to control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *              {@link SzFlag#SZ_REEVALUATE_RECORD_DEFAULT_FLAGS} is also available
     *              if you desire to use recommended defaults.
     *
     * @return The JSON {@link String} result produced by reevaluating the record
     *         in the repository, or <code>null</code> if the specified flags do
     *         not indicate that an INFO message should be returned.
     *
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_REEVALUATE_RECORD_DEFAULT_FLAGS
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_REEVALUATE_RECORD_FLAGS
     * 
     * @see #reevaluateRecord(SzRecordKey)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String reevaluateRecord(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzException;

    /**
     * Convenience method for calling {@link #reevaluateRecord(SzRecordKey, Set)}
     * using {@link SzFlag#SZ_REEVALUATE_RECORD_DEFAULT_FLAGS} as the value for
     * the <code>flags</code> parameter.  See the {@link
     * #reevaluateRecord(SzRecordKey, Set)} documentation for details.
     * <p>
     * <b>NOTE:</b> The {@link String} return type is still used despite the
     * fact that in the current version this will always return <code>null</code>
     * due to {@link SzFlag#SZ_REEVALUATE_RECORD_DEFAULT_FLAGS} being equivalent
     * to {@link SzFlag#SZ_NO_FLAGS}.  However, having a <code>void</code> return 
     * type would cause incompatibilities if a future release introduced a
     * different value for {@link SzFlag#SZ_REEVALUATE_RECORD_DEFAULT_FLAGS} that
     * did trigger a non-null return value.
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="reevaluateRecordDefault"}
     * </p>
     *
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the record to reevaluate.
     * 
     * @return The JSON {@link String} result produced by reevaluating the record
     *         in the repository, which will always be <code>null</code> in the
     *         current version (see above).
     *
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_REEVALUATE_RECORD_DEFAULT_FLAGS
     * 
     * @see #reevaluateRecord(SzRecordKey, Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String reevaluateRecord(SzRecordKey recordKey)
        throws SzUnknownDataSourceException, SzException
    {
        return this.reevaluateRecord(recordKey, SZ_REEVALUATE_RECORD_DEFAULT_FLAGS);
    }

    /**
     * Reevaluates an entity by entity ID.
     * 
     * <p>
     * This operation performs entity resolution.  If the entity is not found,
     * then no changes are made.
     * </p>
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS} group will be recognized (other
     * {@link SzFlag} instances will be ignored).  <b>NOTE:</b> {@link
     * java.util.EnumSet} offers an efficient means of constructing a {@link Set}
     * of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="reevaluateEntity"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-reevaluateEntity.txt"}
     * </p>
     * 
     * @param entityId The ID of the resolved entity to reevaluate.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS} group
     *              to control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *              {@link SzFlag#SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS} is also available
     *              if you desire to use recommended defaults.
     *
     * @return The JSON {@link String} result produced by reevaluating the entity
     *         in the repository, or <code>null</code> if the specified flags do
     *         not indicate that an INFO message should be returned.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_REEVALUATE_ENTITY_FLAGS
     * 
     * @see #reevaluateEntity(long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String reevaluateEntity(long entityId, Set<SzFlag> flags)
        throws SzException;

    /**
     * Convenience method for calling {@link #reevaluateEntity(long, Set)}
     * using {@link SzFlag#SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS} as the value for
     * the <code>flags</code> parameter.  See the {@link
     * #reevaluateEntity(long, Set)} documentation for details.
     * 
     * <p>
     * <b>NOTE:</b> The {@link String} return type is still used despite the
     * fact that in the current version this will always return <code>null</code>
     * due to {@link SzFlag#SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS} being equivalent
     * to {@link SzFlag#SZ_NO_FLAGS}.  However, having a <code>void</code> return 
     * type would cause incompatibilities if a future release introduced a
     * different value for {@link SzFlag#SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS} that
     * did trigger a non-null return value.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="reevaluateEntityDefault"}
     * </p>
     *
     * @param entityId The ID of the resolved entity to reevaluate.
     * 
     * @return The JSON {@link String} result produced by reevaluating the entity
     *         in the repository, which will always be <code>null</code> in the
     *         current version (see above).
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS
     * 
     * @see #reevaluateEntity(long, Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String reevaluateEntity(long entityId)
        throws SzException
    {
        return this.reevaluateEntity(entityId, SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS);
    }

    /**
     * Searches for entities that match or relate to the provided attributes.
     * 
     * <p>
     * The default search profile is <code>"SEARCH"</code>.  Alternatively,
     * <code>"INGEST"</code> may be used.
     * </p>
     * 
     * <p>
     * If the specified search profile is <code>null</code> then the default
     * will be used (alternatively, use {@link #searchByAttributes(String, Set)} 
     * as a convenience method to omit the parameter).
     * </p>
     *
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_SEARCH_FLAGS} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit
     * flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     *  
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="searchByAttributesWithProfile"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-searchByAttributesWithProfile.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param searchProfile The optional search profile identifier, or 
     *                      <code>null</code> if the default search profile
     *                      should be used for the search.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS} for
     *              the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see #searchByAttributes(String, Set)
     * @see #searchByAttributes(String)
     * @see #searchByAttributes(String, String)
     * 
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH_FLAGS
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchRecords.java">Code Snippet: Search Records</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchViaFutures.java">Code Snippet: Search via Futures</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String searchByAttributes(String        attributes, 
                              String        searchProfile,
                              Set<SzFlag>   flags)
        throws SzException;

    /**
     * Convenience method for calling {@link #searchByAttributes(String, String, Set)}
     * using {@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS} as the value
     * for the <code>flags</code> parameter.  See the {@link
     * #searchByAttributes(String, String, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" 
     *           region="searchByAttributesWithProfileDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-searchByAttributesWithProfileDefault.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param searchProfile The optional search profile identifier, or 
     *                      <code>null</code> if the default search profile
     *                      should be used for the search.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see #searchByAttributes(String)
     * @see #searchByAttributes(String, Set)
     * @see #searchByAttributes(String, String, Set)
     * 
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchRecords.java">Code Snippet: Search Records</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchViaFutures.java">Code Snippet: Search via Futures</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String searchByAttributes(String attributes, String searchProfile)
        throws SzException
    {
        return this.searchByAttributes(
            attributes, searchProfile, SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS);
    }

    /**
     * Convenience method for calling {@link 
     * #searchByAttributes(String, String, Set)} with a <code>null</code> value
     * for the search profile parameter.  See the {@link 
     * #searchByAttributes(String, String, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="searchByAttributes"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-searchByAttributes.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_SEARCH_FLAGS} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS} for
     *              the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see #searchByAttributes(String, String, Set)
     * 
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH_FLAGS
     * 
     * @see #searchByAttributes(String)
     * @see #searchByAttributes(String, String, Set)
     * @see #searchByAttributes(String, String)
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchRecords.java">Code Snippet: Search Records</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchViaFutures.java">Code Snippet: Search via Futures</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String searchByAttributes(String attributes, Set<SzFlag> flags)
        throws SzException
    {
        return this.searchByAttributes(attributes, null, flags);
    }

    /**
     * Convenience method for calling {@link 
     * #searchByAttributes(String, String, Set)} with a <code>null</code>
     * value for the search profile parameter and {@link 
     * SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS} as the value for the 
     * <code>flags</code> parameter.  See the {@link 
     * #searchByAttributes(String, String, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="searchByAttributesDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-searchByAttributesDefault.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH_FLAGS
     * 
     * @see #searchByAttributes(String, Set)
     * @see #searchByAttributes(String, String, Set)
     * @see #searchByAttributes(String, String)
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchRecords.java">Code Snippet: Search Records</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/searching/SearchViaFutures.java">Code Snippet: Search via Futures</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String searchByAttributes(String attributes) throws SzException
    {
        return this.searchByAttributes(attributes, SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS);
    }

    /**
     * Describes the ways a set of search attributes relate to an entity.
     * 
     * <p>
     * The default search profile is <code>"SEARCH"</code>.  Alternatively,
     * <code>"INGEST"</code> may be used.
     * </p>
     * 
     * <p>
     * If the specified search profile is <code>null</code> then the default
     * will be used (alternatively, use {@link #whySearch(String, long, Set)} 
     * as a convenience method to omit the parameter).
     * </p>
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS} group are guaranteed to be recognized
     * (other {@link SzFlag} instances will be ignored unless they have equivalent
     * bit flags to supported flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whySearchWithProfile"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whySearchWithProfile.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param entityId The entity ID identifying the entity to analyze against the
     *                 search attribute criteria.
     * 
     * @param searchProfile The optional search profile identifier, or 
     *                      <code>null</code> if the default search profile
     *                      should be used for the search.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS} group t
     *              control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link
     *              SzFlag#SZ_NO_FLAGS} or {@link SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the
     *         why analysis against the search criteria.
     * 
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS
     * 
     * @see #whySearch(String, long, String)
     * @see #whySearch(String, long, Set)
     * @see #whySearch(String, long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String whySearch(String         attributes,
                     long           entityId,
                     String         searchProfile,
                     Set<SzFlag>    flags)
        throws SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #whySearch(String, long, String, Set)}
     * using {@link SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the {@link
     * #whySearch(String, long, String, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whySearchDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whySearchDefault.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param entityId The entity ID identifying the entity to analyze against the
     *                 search attribute criteria.
     * 
     * @param searchProfile The optional search profile identifier, or 
     *                      <code>null</code> if the default search profile
     *                      should be used for the search.
     * 
     * @return The resulting JSON {@link String} describing the result of the
     *         why analysis against the search criteria.
     * 
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS
     * 
     * @see #whySearch(String, long, String, Set)
     * @see #whySearch(String, long)
     * @see #whySearch(String, long, Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String whySearch(String     attributes,
                             long       entityId,
                             String     searchProfile)
        throws SzNotFoundException, SzException
    {
        return this.whySearch(attributes,
                              entityId,
                              searchProfile,
                              SZ_WHY_SEARCH_DEFAULT_FLAGS);
    }

    /**
     * Convenience method for calling {@link #whySearch(String, long, String, Set)}
     * with a <code>null</code> value for the search profile parameter.  See the
     * {@link #whySearch(String, long, String, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whySearch"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whySearch.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param entityId The entity ID identifying the entity to analyze against the
     *                 search attribute criteria.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS} group t
     *              control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link
     *              SzFlag#SZ_NO_FLAGS} or {@link SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the
     *         why analysis against the search criteria.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY_SEARCH_FLAGS
     * 
     * @see #whySearch(String, long, String, Set)
     * @see #whySearch(String, long, String)
     * @see #whySearch(String, long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String whySearch(String         attributes,
                             long           entityId,
                             Set<SzFlag>    flags)
        throws SzNotFoundException, SzException
    {
        return this.whySearch(attributes, entityId, null, flags);
    }

    /**
     * Convenience method for calling {@link #whySearch(String, long, String, Set)}
     * with a <code>null</code> value for the search profile parameter and {@link 
     * SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS} as the value for the <code>flags</code>
     * parameter.  See the {@link #whySearch(String, long, String, Set)} 
     * documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whySearchDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whySearchDefault.txt"}
     * </p>
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param entityId The entity ID identifying the entity to analyze against the
     *                 search attribute criteria.
     * 
     * @return The resulting JSON {@link String} describing the result of the
     *         why analysis against the search criteria.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_SEARCH_DEFAULT_FLAGS
     * 
     * @see #whySearch(String, long, String, Set)
     * @see #whySearch(String, long, String)
     * @see #whySearch(String, long, Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String whySearch(String attributes, long entityId)
        throws SzNotFoundException, SzException
    {
        return this.whySearch(
            attributes, entityId, null, SZ_WHY_SEARCH_DEFAULT_FLAGS);
    }

    /**
     * Retrieves information about an entity, specified by entity ID.
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_ENTITY_FLAGS} group are guaranteed to be recognized 
     * (other {@link SzFlag} instances will be ignored unless they have
     * equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getEntityByEntityId"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getEntityByEntityId.txt"}
     * </p>
     * 
     * @param entityId The entity ID identifying the entity to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_ENTITY_FLAGS} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS} for the default
     *              recommended flags.
     *
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_ENTITY_DEFAULT_FLAGS
     * @see SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ENTITY_FLAGS
     * 
     * @see #getEntity(long)
     * @see #getEntity(SzRecordKey, Set)
     * @see #getEntity(SzRecordKey)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String getEntity(long entityId, Set<SzFlag> flags)
        throws SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #getEntity(long,Set)} using
     * {@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS} as the value for the<code>flags</code>
     * parameter.  See the {@link #getEntity(long, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getEntityByEntityIdDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getEntityByEntityIdDefault.txt"}
     * </p>
     * 
     * @param entityId The entity ID identifying the entity to retrieve.
     * 
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_ENTITY_DEFAULT_FLAGS
     * 
     * @see #getEntity(long, Set)
     * @see #getEntity(SzRecordKey, Set)
     * @see #getEntity(SzRecordKey)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String getEntity(long entityId)
        throws SzNotFoundException, SzException
    {
        return this.getEntity(entityId, SZ_ENTITY_DEFAULT_FLAGS);
    }

    /**
     * Retrieves information about an entity, specified by record ID.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_ENTITY_FLAGS} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit
     * flags).
     * </p>
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getEntityByRecordKey"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getEntityByRecordKey.txt"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the constituent record
     *                  for the entity to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_ENTITY_FLAGS} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_ENTITY_DEFAULT_FLAGS
     * @see SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ENTITY_FLAGS
     * 
     * @see #getEntity(SzRecordKey)
     * @see #getEntity(long, Set)
     * @see #getEntity(long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String getEntity(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #getEntity(SzRecordKey,Set)}
     * using {@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the {@link #getEntity(SzRecordKey, Set)}
     * documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="getEntityByRecordKeyDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getEntityByRecordKeyDefault.txt"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the constituent record
     *                  for the entity to retrieve.
     * 
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_ENTITY_DEFAULT_FLAGS
     * 
     * @see #getEntity(SzRecordKey, Set)
     * @see #getEntity(long, Set)
     * @see #getEntity(long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String getEntity(SzRecordKey recordKey)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {
        return this.getEntity(recordKey, SZ_ENTITY_DEFAULT_FLAGS);
    }

    /**
     * Experimental method.
     * 
     * <p>
     * Contact Senzing support for further information.
     * </p>
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags to supported flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="findInterestingByEntityId"}
     * </p>
     * 
     * @param entityId The entity ID identifying the entity that will be the 
     *                 focus for the interesting entities to be returned.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS}
     *              group to control how the operation is performed and the content of
     *              the response, or <code>null</code> to default to {@link
     *              SzFlag#SZ_NO_FLAGS} or {@link
     *              SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS} for the default
     *              recommended flags.
     *
     * @return The JSON {@link String} describing the interesting entities.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS
     * 
     * @see #findInterestingEntities(long)
     * @see #findInterestingEntities(SzRecordKey, Set)
     * @see #findInterestingEntities(SzRecordKey)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String findInterestingEntities(long entityId, Set<SzFlag> flags)
        throws SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #findInterestingEntities(long, Set)}
     * with {@link SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS} as the
     * value for the <code>flags</code> parameter.  See the {@link 
     * #findInterestingEntities(long, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="findInterestingByEntityIdDefault"}
     * </p>
     * 
     * @param entityId The entity ID identifying the entity that will be the 
     *                 focus for the interesting entities to be returned.
     * 
     * @return The JSON {@link String} describing the interesting entities.
     * 
     * @throws SzNotFoundException If no entity could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS
     * 
     * @see #findInterestingEntities(long, Set)
     * @see #findInterestingEntities(SzRecordKey, Set)
     * @see #findInterestingEntities(SzRecordKey)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findInterestingEntities(long entityId)
        throws SzNotFoundException, SzException
    {
        return this.findInterestingEntities(
            entityId, SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS);
    }

    /**
     * Experimental method.
     * 
     * <p>
     * Contact Senzing support for further information.
     * </p>
     * 
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags to supported flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="findInterestingByRecordKey"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the constituent record
     *                  for the entity that is the focus for the interesting
     *                  entities to be returned.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS}
     *              group to control how the operation is performed and the content of
     *              the response, or <code>null</code> to default to {@link
     *              SzFlag#SZ_NO_FLAGS} or {@link
     *              SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS} for the default
     *              recommended flags.
     *
     * @return The JSON {@link String} describing the interesting entities.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If no record could be found with the
     *                             specified record ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_INTERESTING_ENTITIES_FLAGS
     * 
     * @see #findInterestingEntities(SzRecordKey)
     * @see #findInterestingEntities(long, Set)
     * @see #findInterestingEntities(long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String findInterestingEntities(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Convenience method for calling
     * {@link #findInterestingEntities(SzRecordKey, Set)}
     * with {@link SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS} as the
     * value for the <code>flags</code> parameter.  See the {@link 
     * #findInterestingEntities(SzRecordKey, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="findInterestingByRecordKeyDefault"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the constituent record
     *                  for the entity that is the focus for the interesting
     *                  entities to be returned.
     * 
     * @return The JSON {@link String} describing the interesting entities.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If no record could be found with the
     *                             specified record ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS
     * @see #findInterestingEntities(SzRecordKey, Set)
     * @see #findInterestingEntities(long, Set)
     * @see #findInterestingEntities(long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findInterestingEntities(SzRecordKey recordKey)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {
        return this.findInterestingEntities(
            recordKey, SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS);
    }

    /**
     * Searches for the shortest relationship path between two entities,
     * specified by entity IDs.
     * 
     * <p>
     * The returned path is the shortest path among the paths that satisfy
     * the parameters.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the path and the entities on the path.  The {@link Set} may contain any
     * {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} group will be recognized (other {@link SzFlag}
     * instance will be ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="findPathByEntityId"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByEntityId.txt"}
     * </p>
     *
     * @param startEntityId The entity ID of the first entity.
     * 
     * @param endEntityId The entity ID of the second entity.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param avoidEntityIds The optional {@link SzEntityIds} describing the
     *                       {@link Set} of non-null {@link Long} entity ID's
     *                       identifying entities to be avoided when finding
     *                       the path, or <code>null</code> if no entities are
     *                       to be avoided.  By default the entities will be
     *                       avoided unless necessary to find the path.  To 
     *                       strictly avoid the entities specify the
     *                       {@link SzFlag#SZ_FIND_PATH_STRICT_AVOID} flag.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end entities
     *                             for the specified entity ID's cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized required data source
     *                                      is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH_FLAGS
     * 
     * @see #findPath(long,long,int,SzEntityIds,Set)
     * @see #findPath(long,long,int,Set)
     * @see #findPath(long,long,int)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String findPath(long        startEntityId,
                    long        endEntityId,
                    int         maxDegrees,
                    SzEntityIds avoidEntityIds,
                    Set<String> requiredDataSources,
                    Set<SzFlag> flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Convenience method for calling 
     * {@link #findPath(long, long, int, SzEntityIds, Set, Set)}
     * using {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} as the value for the 
     * <code>flags</code> parameter.  See the {@link 
     * #findPath(long, long, int, SzEntityIds, Set, Set)} documentation for
     * details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" 
     *           region="findPathByEntityIdDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByEntityIdDefault.txt"}
     * </p>
     *
     * @param startEntityId The entity ID of the first entity.
     * 
     * @param endEntityId The entity ID of the second entity.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param avoidEntityIds The optional {@link SzEntityIds} describing the
     *                       {@link Set} of non-null {@link Long} entity ID's
     *                       identifying entities to be avoided when finding
     *                       the path, or <code>null</code> if no entities are
     *                       to be avoided.  By default the entities will be
     *                       avoided unless necessary to find the path.  To 
     *                       strictly avoid the entities specify the
     *                       {@link SzFlag#SZ_FIND_PATH_STRICT_AVOID} flag.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end entities
     *                             for the specified entity ID's cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized required data source
     *                                      is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * 
     * @see #findPath(long,long,int,SzEntityIds,Set,Set)
     * @see #findPath(long,long,int,Set)
     * @see #findPath(long,long,int)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findPath(long        startEntityId,
                            long        endEntityId,
                            int         maxDegrees,
                            SzEntityIds avoidEntityIds,
                            Set<String> requiredDataSources)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException
    {
        return this.findPath(startEntityId, 
                             endEntityId,
                             maxDegrees,
                             avoidEntityIds,
                             requiredDataSources,
                             SZ_FIND_PATH_DEFAULT_FLAGS);
    }

    /**
     * Convenience method for calling 
     * {@link #findPath(long, long, int, SzEntityIds, Set, Set)}
     * using <code>null</code> as the value for both the "avoid entity ID's"
     * and the "required data sources" parameters.  See the {@link 
     * #findPath(long, long, int, SzEntityIds, Set, Set)} documentation for
     * details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" 
     *           region="findPathByEntityIdSimple"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByEntityIdSimple.txt"}
     * </p>
     *
     * @param startEntityId The entity ID of the first entity.
     * 
     * @param endEntityId The entity ID of the second entity.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end entities
     *                             for the specified entity ID's cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized required data source
     *                                      is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH_FLAGS
     * 
     * @see #findPath(long,long,int,SzEntityIds,Set,Set)
     * @see #findPath(long,long,int,SzEntityIds,Set)
     * @see #findPath(long,long,int)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findPath(long        startEntityId,
                            long        endEntityId,
                            int         maxDegrees,
                            Set<SzFlag> flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException
    {
        return this.findPath(startEntityId, 
                             endEntityId,
                             maxDegrees,
                             null,
                             null,
                             flags);
    }

    /**
     * Convenience method for calling 
     * {@link #findPath(long, long, int, SzEntityIds, Set, Set)}
     * using <code>null</code> as the value for both the "avoid entity ID's" and the
     * "required data sources" parameters and {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS}
     * as the value for the <code>flags</code> parameter.  See the {@link 
     * #findPath(long, long, int, SzEntityIds, Set, Set)} documentation for
     * details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" 
     *           region="findPathByEntityIdSimpleDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByEntityIdSimpleDefault.txt"}
     * </p>
     *
     * @param startEntityId The entity ID of the first entity.
     * 
     * @param endEntityId The entity ID of the second entity.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end entities
     *                             for the specified entity ID's cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized required data source
     *                                      is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * 
     * @see #findPath(long,long,int,SzEntityIds,Set,Set)
     * @see #findPath(long,long,int,SzEntityIds,Set)
     * @see #findPath(long,long,int,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findPath(long        startEntityId,
                            long        endEntityId,
                            int         maxDegrees)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException
    {
        return this.findPath(startEntityId, 
                             endEntityId,
                             maxDegrees,
                             null,
                             null,
                             SZ_FIND_PATH_DEFAULT_FLAGS);
    }

    /**
     * Searches for the shortest relationship path between two entities, 
     * specified by record IDs.
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the path and the entities on the path.  The {@link Set} may contain any
     * {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} group will be recognized (other {@link SzFlag}
     * instance will be ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="findPathByRecordKey"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByRecordKey.txt"}
     * </p>
     *
     * @param startRecordKey The {@link SzRecordKey} containing the data source
     *                       code and record ID identifying the record at the
     *                       start of the path.
     * 
     * @param endRecordKey The {@link SzRecordKey} containing the data source
     *                     code and record ID identifying the record at the end
     *                     of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param avoidRecordKeys The optional {@link SzRecordKeys} describing the 
     *                        {@link Set} of non-null {@link SzRecordKey} instances
     *                        providing the data source code and record ID pairs of
     *                        the records whose entities are to be avoided when
     *                        finding the path, or <code>null</code> if no entities
     *                        identified by are to be avoided.  By default the 
     *                        entities will be avoided unless necessary to find the
     *                        path. To strictly avoid the entities specify the
     *                        {@link SzFlag#SZ_FIND_PATH_STRICT_AVOID} flag.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH_FLAGS
     * 
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int)
     * @see #findPath(long,long,int,SzEntityIds,Set,Set)
     * @see #findPath(long,long,int,SzEntityIds,Set)
     * @see #findPath(long,long,int,Set)
     * @see #findPath(long,long,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String findPath(SzRecordKey       startRecordKey,
                    SzRecordKey       endRecordKey,
                    int               maxDegrees,
                    SzRecordKeys      avoidRecordKeys,
                    Set<String>       requiredDataSources,
                    Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Convenience method for calling {@link 
     * #findPath(SzRecordKey, SzRecordKey, int, SzRecordKeys, Set, Set)}
     * using {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} as the value for the 
     * <code>flags</code> parameter.  See the {@link 
     * #findPath(SzRecordKey, SzRecordKey, int, SzRecordKeys, Set, Set)}
     * documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="findPathByRecordKeyDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByRecordKeyDefault.txt"}
     * </p>
     *
     * @param startRecordKey The {@link SzRecordKey} containing the data source
     *                       code and record ID identifying the record at the
     *                       start of the path.
     * 
     * @param endRecordKey The {@link SzRecordKey} containing the data source
     *                     code and record ID identifying the record at the end
     *                     of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param avoidRecordKeys The optional {@link SzRecordKeys} describing the 
     *                        {@link Set} of non-null {@link SzRecordKey} instances
     *                        providing the data source code and record ID pairs of
     *                        the records whose entities are to be avoided when
     *                        finding the path, or <code>null</code> if no entities
     *                        identified by are to be avoided.  By default the 
     *                        entities will be avoided unless necessary to find the
     *                        path. To strictly avoid the entities specify the
     *                        {@link SzFlag#SZ_FIND_PATH_STRICT_AVOID} flag.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH_FLAGS
     * 
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int)
     * @see #findPath(long,long,int,SzEntityIds,Set,Set)
     * @see #findPath(long,long,int,SzEntityIds,Set)
     * @see #findPath(long,long,int,Set)
     * @see #findPath(long,long,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findPath(SzRecordKey       startRecordKey,
                            SzRecordKey       endRecordKey,
                            int               maxDegrees,
                            SzRecordKeys      avoidRecordKeys,
                            Set<String>       requiredDataSources)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException
    {
        return this.findPath(startRecordKey, 
                             endRecordKey, 
                             maxDegrees, 
                             avoidRecordKeys, 
                             requiredDataSources,
                             SZ_FIND_PATH_DEFAULT_FLAGS);
    }

    /**
     * Convenience method for calling {@link 
     * #findPath(SzRecordKey, SzRecordKey, int, SzRecordKeys, Set, Set)}
     * using <code>null</code> as the value for both the "avoid record keys"
     * and the "required data sources" parameters.  See the {@link 
     * #findPath(SzRecordKey, SzRecordKey, int, SzRecordKeys, Set, Set)}
     * documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="findPathByRecordKeySimple"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByRecordKeySimple.txt"}
     * </p>
     *
     * @param startRecordKey The {@link SzRecordKey} containing the data source
     *                       code and record ID identifying the record at the
     *                       start of the path.
     * 
     * @param endRecordKey The {@link SzRecordKey} containing the data source
     *                     code and record ID identifying the record at the end
     *                     of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_PATH_FLAGS} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH_FLAGS
     * 
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int)
     * @see #findPath(long,long,int,SzEntityIds,Set,Set)
     * @see #findPath(long,long,int,SzEntityIds,Set)
     * @see #findPath(long,long,int,Set)
     * @see #findPath(long,long,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findPath(SzRecordKey       startRecordKey,
                            SzRecordKey       endRecordKey,
                            int               maxDegrees,
                            Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException
    {
        return this.findPath(startRecordKey, 
                             endRecordKey,
                             maxDegrees, 
                             null,
                             null,
                             flags);
    }

    /**
     * Convenience method for calling {@link 
     * #findPath(SzRecordKey, SzRecordKey, int, SzRecordKeys, Set, Set)}
     * using <code>null</code> as the value for both the "avoid record keys"
     * and the "required data sources" parameters and 
     * {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the {@link 
     * #findPath(SzRecordKey, SzRecordKey, int, SzRecordKeys, Set, Set)}
     * documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="findPathByRecordKeySimpleDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findPathByRecordKeySimpleDefault.txt"}
     * </p>
     *
     * @param startRecordKey The {@link SzRecordKey} containing the data source
     *                       code and record ID identifying the record at the
     *                       start of the path.
     * 
     * @param endRecordKey The {@link SzRecordKey} containing the data source
     *                     code and record ID identifying the record at the end
     *                     of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * 
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,SzRecordKeys,Set)
     * @see #findPath(SzRecordKey,SzRecordKey,int,Set)
     * @see #findPath(long,long,int,SzEntityIds,Set,Set)
     * @see #findPath(long,long,int,SzEntityIds,Set)
     * @see #findPath(long,long,int,Set)
     * @see #findPath(long,long,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findPath(SzRecordKey       startRecordKey,
                            SzRecordKey       endRecordKey,
                            int               maxDegrees)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException
    {
        return this.findPath(startRecordKey, 
                             endRecordKey,
                             maxDegrees, 
                             null,
                             null,
                             SZ_FIND_PATH_DEFAULT_FLAGS);
    }

    /**
     * Retrieves a network of relationships among entities, specified by entity IDs.
     * 
     * <p>
     * <b>WARNING:</b> Entity networks may be very large due to the volume of
     * inter-related data in the repository.  The parameters of this method can be
     * used to limit the information returned.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the network and the entities on the network.  The {@link Set} may contain
     * any {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="findNetworkByEntityId"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findNetworkByEntityId.txt"}
     * </p>
     *
     * @param entityIds The {@link SzEntityIds} describing the {@link Set} of non-null
     *                  {@link Long} entity ID's identifying the entities for which to
     *                  build the network.
     * 
     * @param maxDegrees The maximum number of degrees for the path search between the
     *                   specified entities.  The maximum degrees of separation for the
     *                   paths between entities must be specified so as to prevent the 
     *                   network growing beyond the desired size.
     * 
     * @param buildOutDegrees The number of relationship degrees to build out from each 
     *                        of the found entities on the network, or zero to prevent 
     *                        network build-out.  If this is non-zero, the size of the
     *                        network can be limited to a maximum total number of 
     *                        build-out entities for the whole network.
     * 
     * @param buildOutMaxEntities The maximum number of entities to build out for the 
     *                            entire network.  This limits the size of the build-out
     *                            network when the build-out degrees is non-zero.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzNotFoundException If any of the entities for the specified
     *                             entity ID's cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS
     * 
     * @see #findNetwork(SzEntityIds,int,int,int)
     * @see #findNetwork(SzRecordKeys,int,int,int,Set)
     * @see #findNetwork(SzRecordKeys,int,int,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String findNetwork(SzEntityIds  entityIds,
                       int          maxDegrees,
                       int          buildOutDegrees,
                       int          buildOutMaxEntities,
                       Set<SzFlag>  flags)
        throws SzNotFoundException, SzException;

    /**
     * Convenience method for calling 
     * {@link #findNetwork(SzEntityIds, int, int, int, Set)} with 
     * {@link SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the 
     * {@link #findNetwork(SzEntityIds, int, int, int, Set)} documentation
     * for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="findNetworkByEntityIdDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findNetworkByEntityIdDefault.txt"}
     * </p>
     *
     * @param entityIds The {@link SzEntityIds} describing the {@link Set} of non-null
     *                  {@link Long} entity ID's identifying the entities for which to
     *                  build the network.
     * 
     * @param maxDegrees The maximum number of degrees for the path search between the
     *                   specified entities.  The maximum degrees of separation for the
     *                   paths between entities must be specified so as to prevent the 
     *                   network growing beyond the desired size.
     * 
     * @param buildOutDegrees The number of relationship degrees to build out from each 
     *                        of the found entities on the network, or zero to prevent 
     *                        network build-out.  If this is non-zero, the size of the
     *                        network can be limited to a maximum total number of 
     *                        build-out entities for the whole network.
     * 
     * @param buildOutMaxEntities The maximum number of entities to build out for the 
     *                            entire network.  This limits the size of the build-out
     *                            network when the build-out degrees is non-zero.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzNotFoundException If any of the entities for the specified
     *                             entity ID's cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * 
     * @see #findNetwork(SzEntityIds,int,int,int,Set)
     * @see #findNetwork(SzRecordKeys,int,int,int,Set)
     * @see #findNetwork(SzRecordKeys,int,int,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findNetwork(SzEntityIds  entityIds,
                               int          maxDegrees,
                               int          buildOutDegrees,
                               int          buildOutMaxEntities)
        throws SzNotFoundException, SzException
    {
        return this.findNetwork(entityIds, 
                                maxDegrees,
                                buildOutDegrees,
                                buildOutMaxEntities,
                                SZ_FIND_NETWORK_DEFAULT_FLAGS);
    }

    /**
     * Retrieves a network of relationships among entities, specified by record IDs.
     * 
     * <p>
     * <b>WARNING:</b> Entity networks may be very large due to the volume of
     * inter-related data in the repository.  The parameters of this method can be
     * used to limit the information returned.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the network and the entities on the network.  The {@link Set} may contain
     * any {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="findNetworkByRecordKey"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findNetworkByRecordKey.txt"}
     * </p>
     *
     * @param recordKeys The {@link SzRecordKeys} describing the {@link Set} of
     *                   non-null {@link SzRecordKey} instances providing the
     *                   data source code and record ID pairs for the constituent
     *                   records of the entities for which to build the network.
     * 
     * @param maxDegrees The maximum number of degrees for the path search between the
     *                   specified entities.  The maximum degrees of separation for the
     *                   paths between entities must be specified so as to prevent the 
     *                   network growing beyond the desired size.
     * 
     * @param buildOutDegrees The number of relationship degrees to build out from each 
     *                        of the found entities on the network, or zero to prevent 
     *                        network build-out.  If this is non-zero, the size of the
     *                        network can be limited to a maximum total number of 
     *                        build-out entities for the whole network.
     * 
     * @param buildOutMaxEntities The maximum number of entities to build out for the 
     *                            entire network.  This limits the size of the build-out
     *                            network when the build-out degrees is non-zero.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS
     * 
     * @see #findNetwork(SzRecordKeys,int,int,int)
     * @see #findNetwork(SzEntityIds,int,int,int,Set)
     * @see #findNetwork(SzEntityIds,int,int,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String findNetwork(SzRecordKeys recordKeys,
                       int          maxDegrees,
                       int          buildOutDegrees,
                       int          buildOutMaxEntities,
                       Set<SzFlag>  flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Convenience method for calling 
     * {@link #findNetwork(SzRecordKeys, int, int, int, Set)}
     * with {@link SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the 
     * {@link #findNetwork(SzRecordKeys, int, int, int, Set)} documentation
     * for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" 
     *           region="findNetworkByRecordKeyDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-findNetworkByRecordKeyDefault.txt"}
     * </p>
     *
     * @param recordKeys The {@link SzRecordKeys} describing the {@link Set} of
     *                   non-null {@link SzRecordKey} instances providing the
     *                   data source code and record ID pairs for the constituent
     *                   records of the entities for which to build the network.
     * 
     * @param maxDegrees The maximum number of degrees for the path search between the
     *                   specified entities.  The maximum degrees of separation for the
     *                   paths between entities must be specified so as to prevent the 
     *                   network growing beyond the desired size.
     * 
     * @param buildOutDegrees The number of relationship degrees to build out from each 
     *                        of the found entities on the network, or zero to prevent 
     *                        network build-out.  If this is non-zero, the size of the
     *                        network can be limited to a maximum total number of 
     *                        build-out entities for the whole network.
     * 
     * @param buildOutMaxEntities The maximum number of entities to build out for the 
     *                            entire network.  This limits the size of the build-out
     *                            network when the build-out degrees is non-zero.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK_FLAGS
     * 
     * @see #findNetwork(SzRecordKeys,int,int,int,Set)
     * @see #findNetwork(SzEntityIds,int,int,int,Set)
     * @see #findNetwork(SzEntityIds,int,int,int)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String findNetwork(SzRecordKeys recordKeys,
                               int          maxDegrees,
                               int          buildOutDegrees,
                               int          buildOutMaxEntities)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {
        return this.findNetwork(recordKeys,
                                maxDegrees,
                                buildOutDegrees,
                                buildOutMaxEntities,
                                SZ_FIND_NETWORK_DEFAULT_FLAGS);
    }

    /**
     * Describes the ways a record relates to the rest of its respective entity.
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     * group are guaranteed to be recognized (other {@link SzFlag} instances
     * will be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whyRecordInEntity"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whyRecordInEntity.txt"}
     * </p>
     *
     * @param recordKey The {@link SzRecordKey} that has the data source code
     *                  and record ID identifying the record.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS}
     *              group to control how the operation is performed and the content
     *              of the response, or <code>null</code> to default to {@link
     *              SzFlag#SZ_NO_FLAGS} or {@link SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The JSON {@link String} describing why the record is included
     *         in its respective entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY_RECORD_IN_ENTITY_FLAGS
     * 
     * @see #whyRecordInEntity(SzRecordKey)
     * @see #whyEntities(long, long, Set)
     * @see #whyEntities(long, long)
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String whyRecordInEntity(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #whyRecordInEntity(SzRecordKey, Set)}
     * with {@link SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS} as the value for
     * the <code>flags</code> parameter. See the {@link 
     * #whyRecordInEntity(SzRecordKey, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo"
     *           region="whyRecordInEntityDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whyRecordInEntityDefault.txt"}
     * </p>
     *
     * @param recordKey The {@link SzRecordKey} that has the data source code
     *                  and record ID identifying the record.
     * 
     * @return The JSON {@link String} describing why the record is included
     *         in its respective entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS
     * 
     * @see #whyRecordInEntity(SzRecordKey,Set)
     * @see #whyEntities(long, long, Set)
     * @see #whyEntities(long, long)
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String whyRecordInEntity(SzRecordKey recordKey)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {
        return this.whyRecordInEntity(recordKey, SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS);
    }

    /**
     * Describes the ways two records relate to each other.
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS} group
     * are guaranteed to be recognized (other {@link SzFlag} instances will
     * be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whyRecords"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whyRecords.txt"}
     * </p>
     *
     * @param recordKey1 The non-null {@link SzRecordKey} providing the
     *                   data source code and record ID for the first record.
     * 
     * @param recordKey2 The non-null {@link SzRecordKey} providing the
     *                   data source code and record ID for the second record.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS} group to
     *        control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the ways in which the records
     *         are related to one another.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If either of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY_RECORDS_FLAGS
     * 
     * @see #whyRecords(SzRecordKey, SzRecordKey)
     * @see #whyRecordInEntity(SzRecordKey,Set)
     * @see #whyRecordInEntity(SzRecordKey)
     * @see #whyEntities(long, long, Set)
     * @see #whyEntities(long, long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String whyRecords(SzRecordKey       recordKey1,
                      SzRecordKey       recordKey2,
                      Set<SzFlag>       flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link 
     * #whyRecords(SzRecordKey, SzRecordKey, Set)} with {@link 
     * SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the {@link 
     * #whyRecordInEntity(SzRecordKey, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whyRecordsDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whyRecordsDefault.txt"}
     * </p>
     *
     * @param recordKey1 The non-null {@link SzRecordKey} providing the
     *                   data source code and record ID for the first record.
     * 
     * @param recordKey2 The non-null {@link SzRecordKey} providing the
     *                   data source code and record ID for the second record.
     * 
     * @return The JSON {@link String} describing the ways in which the records
     *         are related to one another.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If either of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS
     * 
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     * @see #whyRecordInEntity(SzRecordKey,Set)
     * @see #whyRecordInEntity(SzRecordKey)
     * @see #whyEntities(long, long, Set)
     * @see #whyEntities(long, long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String whyRecords(SzRecordKey recordKey1, SzRecordKey recordKey2)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {
        return this.whyRecords(recordKey1, recordKey2, SZ_WHY_RECORDS_DEFAULT_FLAGS);
    }

    /**
     * Describes the ways two entities relate to each other.
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS} group
     * are guaranteed to be recognized (other {@link SzFlag} instances will
     * be ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whyEntities"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whyEntities.txt"}
     * </p>
     *
     * @param entityId1 The entity ID of the first entity.
     * 
     * @param entityId2 The entity ID of the second entity.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS} group to
     *              control how the operation is performed and the content of the
     *              response, or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WHY_ENTITIES_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the ways in which the entities
     *         are related to one another.
     * 
     * @throws SzNotFoundException If either of the entities for the specified
     *                             entity ID's could not be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_ENTITIES_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY_ENTITIES_FLAGS
     * 
     * @see #whyEntities(long, long)
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey)
     * @see #whyRecordInEntity(SzRecordKey,Set)
     * @see #whyRecordInEntity(SzRecordKey)
    */
    @SzConfigRetryable
    String whyEntities(long entityId1, long entityId2, Set<SzFlag> flags)
        throws SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #whyEntities(long, long, Set)}
     * with {@link SzFlag#SZ_WHY_ENTITIES_DEFAULT_FLAGS} as the value for 
     * the <code>flags</code> parameter.  See the {@link 
     * #whyEntities(long, long, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="whyEntitiesDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-whyEntitiesDefault.txt"}
     * </p>
     *
     * @param entityId1 The entity ID of the first entity.
     * 
     * @param entityId2 The entity ID of the second entity.
     * 
     * @return The JSON {@link String} describing the ways in which the entities
     *         are related to one another.
     * 
     * @throws SzNotFoundException If either of the entities for the specified
     *                             entity ID's could not be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_ENTITIES_DEFAULT_FLAGS
     * 
     * @see #whyEntities(long, long, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey)
     * @see #whyRecordInEntity(SzRecordKey,Set)
     * @see #whyRecordInEntity(SzRecordKey)
    */
    @SzConfigRetryable
    default String whyEntities(long entityId1, long entityId2)
        throws SzNotFoundException, SzException
    {
        return this.whyEntities(entityId1, entityId2, SZ_WHY_ENTITIES_DEFAULT_FLAGS);
    }

    /**
     * Explains how an entity was constructed from its records.
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_HOW_FLAGS} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="howEntity"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-howEntity.txt"}
     * </p>
     *
     * @param entityId The entity ID of the entity.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_HOW_FLAGS} group to control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_HOW_ENTITY_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the how the entity was
     *         constructed.
     * 
     * @throws SzNotFoundException If the entity for the specified entity ID
     *                             could not be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_HOW_ENTITY_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_HOW_FLAGS
     * 
     * @see #howEntity(long)
     * @see #getVirtualEntity(Set, Set)
     * @see #getVirtualEntity(Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String howEntity(long entityId, Set<SzFlag> flags)
        throws SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #howEntity(long, Set)} using 
     * {@link SzFlags#SZ_HOW_ENTITY_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the {@link #howEntity(long, Set)}
     * documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="howEntityDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-howEntityDefault.txt"}
     * </p>
     *
     * @param entityId The entity ID of the entity.
     * 
     * @return The JSON {@link String} describing the how the entity was
     *         constructed.
     * 
     * @throws SzNotFoundException If the entity for the specified entity ID
     *                             could not be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_HOW_ENTITY_DEFAULT_FLAGS
     * 
     * @see #howEntity(long, Set)
     * @see #getVirtualEntity(Set, Set)
     * @see #getVirtualEntity(Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String howEntity(long entityId)
        throws SzNotFoundException, SzException
    {
        return this.howEntity(entityId, SZ_HOW_ENTITY_DEFAULT_FLAGS);
    }

    /**
     * Describes how an entity would look if composed of a given set of records.
     * 
     * <p>
     * Virtual entities do not have relationships.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getVirtualEntity"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getVirtualEntity.txt"}
     * </p>
     *
     * @param recordKeys The non-null non-empty {@link Set} of non-null {@link
     *                   SzRecordKey} instances that identify the records to 
     *                   use to build the virtual entity.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances
     *              belonging to the {@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS}
     *              group to control how the operation is performed and
     *              the content of the response, or <code>null</code> to
     *              default to {@link SzFlag#SZ_NO_FLAGS} or
     *              {@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the virtual entity having
     *         the specified constituent records.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_VIRTUAL_ENTITY_FLAGS
     * 
     * @see #howEntity(long, Set)
     * @see #howEntity(long)
     * @see #getVirtualEntity(Set)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String getVirtualEntity(Set<SzRecordKey>  recordKeys,
                            Set<SzFlag>       flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #getVirtualEntity(Set, Set)} using 
     * {@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS} as the value for the
     * <code>flags</code> parameter.  See the {@link #getVirtualEntity(Set, Set)}
     * documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getVirtualEntityDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getVirtualEntityDefault.txt"}
     * </p>
     *
     * @param recordKeys The non-null non-empty {@link Set} of non-null {@link
     *                   SzRecordKey} instances that identify the records to 
     *                   use to build the virtual entity.
     * 
     * @return The JSON {@link String} describing the virtual entity having
     *         the specified constituent records.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS
     * 
     * @see #getVirtualEntity(Set)
     * @see #howEntity(long, Set)
     * @see #howEntity(long)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String getVirtualEntity(Set<SzRecordKey> recordKeys)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {
        return this.getVirtualEntity(recordKeys, SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS);
    }

    /**
     * Retrieves information about a record.
     * 
     * <p>
     * The information contains the original record data that was loaded 
     * and may contain other information depending on the flags parameter.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_RECORD_FLAGS} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getRecord"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getRecord.txt"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} providing the 
     *                  data source code and record ID that identify the
     *                  record to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_RECORD_FLAGS} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_RECORD_DEFAULT_FLAGS} for the default 
     *              recommended flags.
     *
     * @return The JSON {@link String} describing the record.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If the record for the specified data source
     *                             code and record ID pairs cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_RECORD_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_RECORD_FLAGS
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String getRecord(SzRecordKey        recordKey,
                     Set<SzFlag>        flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Convenience method for calling {@link #getRecord(SzRecordKey, Set)} using 
     * {@link SzFlag#SZ_RECORD_DEFAULT_FLAGS} as the value for the 
     * <code>flags</code> parameter.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="getRecordDefault"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-getRecordDefault.txt"}
     * </p>
     * 
     * @param recordKey The non-null {@link SzRecordKey} providing the 
     *                  data source code and record ID that identify the
     *                  record to retrieve.
     *
     * @return The JSON {@link String} describing the record.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If the record for the specified data source
     *                             code and record ID pairs cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_RECORD_DEFAULT_FLAGS
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String getRecord(SzRecordKey recordKey)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {
        return this.getRecord(recordKey, SZ_RECORD_DEFAULT_FLAGS);
    }

    /**
     * Initiates an export report of entity data in JSON Lines format.
     * 
     * <p>
     * This is used in conjunction with {@link #fetchNext(long)} and
     * {@link #closeExportReport(long)}.  Each {@link #fetchNext(long)} 
     * call returns exported entity data as a JSON object.
     * </p>
     * 
     * <p>
     * <b>WARNING:</b> Use with large repositories is <b>not</b> advised.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_EXPORT_FLAGS} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportJson"}
     * </p>
     * 
     * <p><b>Example Complete Export Report:</b>
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-exportJson.txt"}
     * </p>
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_EXPORT_FLAGS} group to control how
     *              the operation is performed and the content of the export, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_EXPORT_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The export handle to use for retrieving the export data.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_EXPORT_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_EXPORT_FLAGS
     * 
     * @see #exportJsonEntityReport()
     * @see #fetchNext(long)
     * @see #closeExportReport(long)
     * @see #exportCsvEntityReport(String, Set)
     * @see #exportCsvEntityReport(String)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    long exportJsonEntityReport(Set<SzFlag> flags) throws SzException;

    /**
     * Convenience method for calling {@link #exportJsonEntityReport(Set)} using
     * {@link SzFlag#SZ_EXPORT_DEFAULT_FLAGS} as the value for the 
     * <code>flags</code> parameter.  See the {@link 
     * #exportJsonEntityReport(Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportJsonDefault"}
     * </p>
     * 
     * <p><b>Example Complete Export Report:</b>
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-exportJsonDefault.txt"}
     * </p>
     * 
     * @return The export handle to use for retrieving the export data.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_EXPORT_DEFAULT_FLAGS
     * 
     * @see #exportJsonEntityReport()
     * @see #fetchNext(long)
     * @see #closeExportReport(long)
     * @see #exportCsvEntityReport(String, Set)
     * @see #exportCsvEntityReport(String)
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default long exportJsonEntityReport() throws SzException
    {
        return this.exportJsonEntityReport(SZ_EXPORT_DEFAULT_FLAGS);
    }

    /**
     * Initiates an export report of entity data in CSV format.
     * 
     * <p>
     * This is used in conjunction with {@link #fetchNext(long)} and
     * {@link #closeExportReport(long)}.  The first {@link #fetchNext(long)}
     * call after calling this method returns the CSV header.  Subsequent
     * {@link #fetchNext(long)} calls return exported entity data in 
     * CSV format.
     * </p>
     * 
     * <p>
     * <b>WARNING:</b> Use with large repositories is <b>not</b> advised.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_EXPORT_FLAGS} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportCsv"}
     * </p>
     * 
     * <p><b>Example Complete Export Report:</b>
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-exportCsv.txt"}
     * </p>
     *
     * @param csvColumnList Specify <code>"*"</code> to indicate "all columns",
     *                      specify empty-string to indicate the "standard
     *                      columns", otherwise specify a comma-separated list of
     *                      column names.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_EXPORT_FLAGS} group to control how
     *              the operation is performed and the content of the export, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_EXPORT_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The export handle to use for retrieving the export data.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_EXPORT_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_EXPORT_FLAGS
     * 
     * @see #exportCsvEntityReport(String)
     * @see #fetchNext(long)
     * @see #closeExportReport(long)
     * @see #exportJsonEntityReport(Set)
     * @see #exportJsonEntityReport()
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    long exportCsvEntityReport(String csvColumnList, Set<SzFlag> flags)
        throws SzException;

    /**
     * Convenience method for calling {@link #exportCsvEntityReport(String, Set)}
     * using {@link SzFlag#SZ_EXPORT_DEFAULT_FLAGS} as the value for the 
     * <code>flags</code> parameter.  See the {@link 
     * #exportCsvEntityReport(String, Set)} documentation for details.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportCsvDefault"}
     * </p>
     * 
     * <p><b>Example Complete Export Report:</b>
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-exportCsvDefault.txt"}
     * </p>
     *
     * @param csvColumnList Specify <code>"*"</code> to indicate "all columns",
     *                      specify empty-string to indicate the "standard
     *                      columns", otherwise specify a comma-separated list of
     *                      column names.
     * 
     * @return The export handle to use for retrieving the export data.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_EXPORT_DEFAULT_FLAGS
     * 
     * @see #exportCsvEntityReport(String,Set)
     * @see #fetchNext(long)
     * @see #closeExportReport(long)
     * @see #exportJsonEntityReport(Set)
     * @see #exportJsonEntityReport()
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default long exportCsvEntityReport(String csvColumnList)
        throws SzException
    {
        return this.exportCsvEntityReport(csvColumnList, SZ_EXPORT_DEFAULT_FLAGS);
    }

    /**
     * Fetches the next line of entity data from an open export report.
     * 
     * <p>
     * Used in conjunction with {@link #exportJsonEntityReport(Set)}, {@link 
     * #exportCsvEntityReport(String, Set)} and {@link #closeExportReport(long)}.
     * </p>
     * 
     * <p>
     * If the export handle was obtained from {@link 
     * #exportCsvEntityReport(String, Set)} this returns the CSV header on the
     * first call and exported entity data in CSV format on subsequent calls.
     * </p>
     * 
     * <p>
     * When <code>null</code> is returned, the export report is complete
     * and the caller should invoke {@link #closeExportReport(long)} to
     * free resources.
     * </p>
     * 
     * <p><b>Usage (JSON export):</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportJson"}
     * </p>
     * 
     * <p><b>Example JSON Line Result:</b>
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-exportJson-fetchNext.txt"}
     * </p>
     * 
     * <p><b>Usage (CSV export):</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportCsv"}
     * </p>
     * 
     * <p><b>Example CSV Header Result:</b>
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-exportCsv-fetchNext-header.txt"}
     * </p>
     * 
     * <p><b>Example CSV Data Result:</b>
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-exportCsv-fetchNext-data.txt"}
     * </p>
     * 
     * @param exportHandle The export handle for the export report from
     *                     which to retrieve the next line of data.
     * 
     * @return The next line of export data whose format depends on
     *         which function was used to initiate the export, or
     *         <code>null</code> if there is no more data to be
     *         exported via the specified handle.
     * 
     * @see #exportCsvEntityReport(String, Set)
     * @see #exportJsonEntityReport(Set)
     * @see #closeExportReport(long)
     * 
     * @throws SzException If the specified export handle has already been
     *                     {@linkplain #closeExportReport(long) closed} or
     *                     if any other failure occurs.
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String fetchNext(long exportHandle) throws SzException;

    /**
     * Closes an export report.
     * 
     * <p>
     * Used in conjunction with {@link #exportJsonEntityReport(Set)}, {@link 
     * #exportCsvEntityReport(String, Set)} and {@link #fetchNext(long)}.
     * </p>

     * <p><b>Usage (JSON export):</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportJson"}
     * </p>
     * 
     * <p><b>Usage (CSV export):</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="exportCsv"}
     * </p>
     *
     * @param exportHandle The export handle of the export report to close.
     * 
     * @throws SzException If the specified export handle has already been
     *                     closed or if any other failure occurs.
     * 
     * @see #exportCsvEntityReport(String, Set)
     * @see #exportJsonEntityReport(Set)
     * @see #fetchNext(long)
     * 
     * @since 4.0.0
     */
    void closeExportReport(long exportHandle) throws SzException;

    /**
     * Processes the provided redo record.
     * 
     * <p>
     * This operation performs entity resolution.  Calling this method 
     * has the potential to create more redo records in certain situations.
     * </p>
     * 
     * <p>
     * This method is used in conjunction with {@link #getRedoRecord()}
     * and {@link #countRedoRecords()}.
     * </p>
     * 
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_REDO_FLAGS} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * </p>
     * 
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="processRedos"}
     * </p>
     * 
     * <p><b>Example Info Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzEngineDemo-addRecord.txt"}
     * </p>
     *
     * @param redoRecord The redo record to be processed.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_REDO_FLAGS} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS} or
     *              {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *              {@link SzFlag#SZ_REDO_DEFAULT_FLAGS} is also available if you 
     *              desire to use the recommended defaults.
     * 
     * @return The JSON {@link String} result produced by processing the redo record
     *         in the repository, or <code>null</code> if the specified flags do not 
     *         indicate that an INFO message should be returned.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_REDO_DEFAULT_FLAGS
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_REDO_FLAGS
     * 
     * @see #getRedoRecord()
     * @see #countRedoRecords()
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/LoadWithRedoViaLoop.java">Code Snippet: Processing Redos while Loading</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuous.java">Code Snippet: Continuous Redo Processing</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuousViaFutures.java">Code Snippet: Continuous Redo Processing via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoWithInfoContinuous.java">Code Snippet: Continuous Redo "With Info" Processing</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String processRedoRecord(String redoRecord, Set<SzFlag> flags)
        throws SzException;

    /**
     * Convenience method for calling {@link #processRedoRecord(String, Set)}
     * using {@link SzFlag#SZ_REDO_DEFAULT_FLAGS} as the value for
     * the <code>flags</code> parameter.
     * 
     * <p>
     * <b>NOTE:</b> The {@link String} return type is still used despite the
     * fact that in the current version this will always return <code>null</code>
     * due to {@link SzFlag#SZ_REDO_DEFAULT_FLAGS} being equivalent
     * to {@link SzFlag#SZ_NO_FLAGS}.  However, having a <code>void</code> return 
     * type would cause incompatibilities if a future release introduced a
     * different value for {@link SzFlag#SZ_REDO_DEFAULT_FLAGS} that did trigger
     * a non-null return value.
     * </p>
     *
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="processRedosDefault"}
     * </p>
     *
     * @param redoRecord The redo record to be processed.
     * 
     * @return The JSON {@link String} result produced by processing the redo
     *         record in the repository, which will always be <code>null</code>
     *         in the current version (see above).
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_NO_FLAGS
     * @see SzFlag#SZ_REDO_DEFAULT_FLAGS
     * 
     * @see #getRedoRecord()
     * @see #countRedoRecords()
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/LoadWithRedoViaLoop.java">Code Snippet: Processing Redos while Loading</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuous.java">Code Snippet: Continuous Redo Processing</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuousViaFutures.java">Code Snippet: Continuous Redo Processing via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoWithInfoContinuous.java">Code Snippet: Continuous Redo "With Info" Processing</a>
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    default String processRedoRecord(String redoRecord)
        throws SzException
    {
        return this.processRedoRecord(redoRecord, SZ_REDO_DEFAULT_FLAGS);
    }

    /**
     * Retrieves and removes a pending redo record.
     * 
     * <p>
     * A <code>null</code> value will be returned if there are no pending
     * redo records.  Use {@link #processRedoRecord(String, Set)} to
     * process the result of this method.  Once Once a redo record is
     * retrieved, it is no longer tracked by Senzing.  The redo record may
     * be stored externally for later processing.
     * </p>
     * 
     * <p>
     * This method is used in conjunction with {@link 
     * #processRedoRecord(String, Set)} and {@link #countRedoRecords()}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="processRedos"}
     * </p>
     *
     * @return The retrieved redo record or <code>null</code> if there were
     *         no pending redo records.
     * 
     * @see #processRedoRecord(String, Set)
     * @see #countRedoRecords()
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/LoadWithRedoViaLoop.java">Code Snippet: Processing Redos while Loading</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuous.java">Code Snippet: Continuous Redo Processing</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuousViaFutures.java">Code Snippet: Continuous Redo Processing via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoWithInfoContinuous.java">Code Snippet: Continuous Redo "With Info" Processing</a>
     * 
     * @throws SzException If a failure occurs.
     * 
     * @since 4.0.0
     */
    @SzConfigRetryable
    String getRedoRecord() throws SzException;

    /**
     * Gets the number of redo records pending processing.
     * 
     * <p>
     * This method is used in conjunction with {@link #getRedoRecord()}
     * and {@link #processRedoRecord(String, Set)}.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzEngineDemo" region="processRedos"}
     * </p>
     *
     * @return The number of redo records pending to be processed.
     * 
     * @see #processRedoRecord(String, Set)
     * @see #getRedoRecord()
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/LoadWithRedoViaLoop.java">Code Snippet: Processing Redos while Loading</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuous.java">Code Snippet: Continuous Redo Processing</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoContinuousViaFutures.java">Code Snippet: Continuous Redo Processing via Futures</a>
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/redo/RedoWithInfoContinuous.java">Code Snippet: Continuous Redo "With Info" Processing</a>
     * 
     * @throws SzException If a failure occurs.
     * 
     * @since 4.0.0
     */
    long countRedoRecords() throws SzException;
}
