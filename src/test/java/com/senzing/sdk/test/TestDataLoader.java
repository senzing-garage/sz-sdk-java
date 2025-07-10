package com.senzing.sdk.test;

import java.io.File;
import java.util.Set;
import java.util.Map;

import com.senzing.sdk.SzRecordKey;

/**
 * Provides an interface for loading test records.
 */
public interface TestDataLoader {
    /**
     * Configures the one or more specified data sources.
     * 
     * @param dataSource The first data source.
     * 
     * @param addlDataSources The additional data sources to be configured.
     */
    void configureDataSources(String dataSource, String... addlDataSources);

    /**
     * Loads a single record and gets the entity for the record.
     * 
     * @param dataSourceCode The data source code for the record.
     * @param recordId The record ID for the record.
     * @param recordDefinition The record definition for the record.
     * @param flags The flags to use for retrieving the entity.
     * 
     * @return The entity JSON for the record's entity.
     */
    String loadAndGetEntity(String dataSourceCode,
                            String recordId,
                            String recordDefinition,
                            long   flags);
    
    /**
     * Convenience method that calls {@link #loadRecords(String, File, String)}
     * with <code>null</code> values for the data source code and encoding.
     * 
     * @param file The {@link File} containing the records in either CSV, JSON-lines
     *             or JSON format.
     * 
     * @return The {@link Set} of {@link SzRecordKey} instances for the loaded records.
     */
    default Map<SzRecordKey, String> loadRecords(File file) {
        return this.loadRecords(null, file, null);
    }

    /**
     * Convenience method that calls {@link #loadRecords(String, File, String)}
     * with a <code>null</code> value for the encoding (forcing UTF-8 encoding).
     * 
     * @param dataSourceCode The data source code to assign to the records in the file,
     *                       or <code>null</code> if the records individually specify
     *                       their data source code and that should be used.
     * 
     * @param file The {@link File} containing the records in either CSV, JSON-lines
     *             or JSON format.
     * 
     * @return The {@link Map} of {@link SzRecordKey} keys to {@link String} values
     *         holding the JSON data for the loaded records.
     */
    default Map<SzRecordKey, String> loadRecords(String dataSourceCode, File file) {
        return this.loadRecords(dataSourceCode, file, null);
    }

    /**
     * Convenience method that calls {@link #loadRecords(String, File, String)}
     * with a <code>null</code> value for the data source code.
     * 
     * @param dataSourceCode The data source code to assign to the records in the file,
     *                       or <code>null</code> if the records individually specify
     *                       their data source code and that should be used.
     * 
     * @param file The {@link File} containing the records in either CSV, JSON-lines
     *             or JSON format.
     * 
     * @return The {@link Set} of {@link SzRecordKey} instances for the loaded records.
     */
    default Map<SzRecordKey, String> loadRecords(File file, String encoding) {
        return this.loadRecords(null, file, encoding);
    }

    /**
     * Loads the records obtained from the specified {@link File} which is
     * read using the optionally specified character encoding and optionally
     * specified data source code.
     * 
     * @param dataSourceCode The data source code to assign to the records in the file,
     *                       or <code>null</code> if the records individually specify
     *                       their data source code and that should be used.
     * 
     * @param file The {@link File} containing the records in either CSV, JSON-lines
     *             or JSON format.
     * 
     * @param encoding The character encoding to read the file, or <code>null</code>
     *                 if UTF-8 should be used.
     * 
     * @return The {@link Set} of {@link SzRecordKey} instances for the loaded records.
     */
   Map<SzRecordKey, String> loadRecords(String dataSourceCode, File file, String encoding);
   
    /**
     * Gets the {@link SzEntityLookup} for the specified {@link Set} of
     * {@link SzRecordKey} instances.
     * 
     * @param recordKeys The {@link Set} of {@link SzRecordKey}'s for
     *                   which to create the {@link SzEntityLookup}.
     * 
     * @returns The {@link SzEntityLookup} for looking up entity ID's
     *          by {@link SzRecordKey} and {@link SzRecordKey}'s by
     *          entity ID.
     */
    SzEntityLookup getEntityLookup(Set<SzRecordKey> recordKeys);
}
