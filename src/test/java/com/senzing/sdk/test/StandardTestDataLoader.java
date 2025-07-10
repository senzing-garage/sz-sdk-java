package com.senzing.sdk.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import javax.json.JsonObject;

import com.senzing.sdk.SzEnvironment;
import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzReplaceConflictException;
import com.senzing.io.RecordReader;

import com.senzing.util.JsonUtilities;
import com.senzing.sdk.SzRecordKey;

import static com.senzing.io.IOUtilities.UTF_8;
import static com.senzing.sdk.SzFlag.SZ_NO_FLAGS;
import static com.senzing.util.JsonUtilities.*;

/**
 * Provides an implementation of {@link TestDataLoader} that
 * uses an {@link SzEnvironment} to load the data.
 */
public class StandardTestDataLoader implements TestDataLoader {
    /**
     * The {@link SzEnvironment} to use.
     */
    private SzEnvironment env;

    /**
     * Constructs with the specified {@link SzEnvironment}.
     * 
     * @param env The {@link SzEnvironment} to use.
     */
    public StandardTestDataLoader(SzEnvironment env) {
        this.env = env;
    }

    @Override
    public void configureDataSources(String dataSource, String... addlDataSources) {
        boolean conflict = false;
        do {
            conflict = false;
            try {
                SzConfigManager configMgr = this.env.getConfigManager();
                long configId = configMgr.getDefaultConfigId();
                SzConfig config = (configId == 0L) ? configMgr.createConfig()
                    : configMgr.createConfig(configId);
                
                // add the first data source
                config.registerDataSource(dataSource);

                // add the additional data sources
                if (addlDataSources != null) {
                    for (String dataSourceCode : addlDataSources) {
                        config.registerDataSource(dataSourceCode);
                    }
                }

                // get the config definition
                String configDef = config.export();

                // register the config
                long newConfigId = configMgr.registerConfig(configDef);

                // check if there is no change
                if (configId == newConfigId) {
                    // data sources must have already existed
                    break;
                }

                // set the config def as the default config
                if (configId == 0L) {
                    // set the initial config ID
                    configMgr.setDefaultConfigId(newConfigId);

                } else {
                    // replace the config ID
                    configMgr.replaceDefaultConfigId(configId, newConfigId);
                }

                // attempt to reinitialize (assuming the SzEnvironment supports it)
                try {
                    this.env.reinitialize(newConfigId);
                } catch (UnsupportedOperationException ignore) {
                    // ignore the exception and trust the server side will
                    // auto-reinitialize
                }

            } catch (SzReplaceConflictException e) {
                conflict = true;

            } catch (SzException e) {
                throw new RuntimeException(e);
            }

        } while (!conflict);
    }

    @Override
    public String loadAndGetEntity(String dataSourceCode,
                                   String recordId,
                                   String recordDefinition,
                                   long   flags)
    {
        try {
            SzEngine engine = this.env.getEngine();

            SzRecordKey recordKey = SzRecordKey.of(dataSourceCode, recordId);
            engine.addRecord(recordKey, recordDefinition);

            processRedos(engine);

            return engine.getEntity(recordKey);

        } catch (SzException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<SzRecordKey, String> loadRecords(String  dataSourceCode, 
                                                File    file, 
                                                String  encoding)
    {
        Map<SzRecordKey, String> result = new LinkedHashMap<>();

        if (encoding == null) encoding = UTF_8;

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, encoding))
        {
            RecordReader recordReader = (dataSourceCode != null)
                ? new RecordReader(isr, dataSourceCode)
                : new RecordReader(isr);

            SzEngine engine = this.env.getEngine();

            for (JsonObject record = recordReader.readRecord();
                 record != null;
                 record = recordReader.readRecord())
            {
                String recordDef    = JsonUtilities.toJsonText(record);
                String dataSource   = record.getString("DATA_SOURCE", null);
                String recordId     = record.getString("RECORD_ID", null);

                // check for missing data source code or record ID
                if (dataSource == null || recordId == null) {
                    throw new SzException(
                        "Missing required record fields.  dataSourceCode=[ "
                        + dataSource + " ], recordId=[ " + recordId 
                        + " ], recordDefinition=[ " + recordDef + " ]");
                }

                // build the record key
                SzRecordKey recordKey = SzRecordKey.of(dataSource, recordId);

                // add the record
                engine.addRecord(recordKey, recordDef);

                // add the reord key to the set
                result.put(recordKey, recordDef);
            }

            processRedos(engine);

            // return the set of record keys
            return result;

        } catch (IOException|SzException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Processes all redos that are pending.
     * 
     * @param engine The {@link SzEngine} to use.
     * 
     * @throws SzException If a failure occurs.
     */
    private static void processRedos(SzEngine engine) throws SzException {
        // process the redo records
        for (String redo = engine.getRedoRecord();
                redo != null;
                redo = engine.getRedoRecord()) 
        {
            engine.processRedoRecord(redo);
        }
    }

    @Override
    public SzEntityLookup getEntityLookup(Set<SzRecordKey> recordKeys) {
        Map<SzRecordKey,Long> recordMap = new LinkedHashMap<>();
        try {
            SzEngine engine = this.env.getEngine();

            // get the records that were loaded and find their entities
            for (SzRecordKey key : recordKeys) {
                String entityJson = engine.getEntity(key, SZ_NO_FLAGS);

                JsonObject jsonObj  = parseJsonObject(entityJson);
                JsonObject entity   = getJsonObject(jsonObj, "RESOLVED_ENTITY");
                Long       entityId = getLong(entity, "ENTITY_ID");

                recordMap.put(key, entityId);
            }

            // return the result map
            return new SzEntityLookup(recordMap);

        } catch (SzException e) {
            throw new RuntimeException(e);
        }
    }
}
