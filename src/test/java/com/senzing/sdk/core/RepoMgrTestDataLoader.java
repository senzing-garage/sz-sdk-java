package com.senzing.sdk.core;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.test.SzEntityLookup;
import com.senzing.sdk.test.TestDataLoader;
import com.senzing.util.JsonUtilities;

import static com.senzing.io.IOUtilities.*;
import static com.senzing.util.JsonUtilities.getJsonObject;
import static com.senzing.util.JsonUtilities.getLong;
import static com.senzing.util.JsonUtilities.parseJsonObject;

/**
 * Implements {@link TestDataLoader} to use the {@link RepositoryManager}.
 */
public class RepoMgrTestDataLoader implements TestDataLoader {
    /**
     * The repository directory.
     */
    private File repoDirectory = null;

    /**
     * The settings for the repository.
     */
    private String settings = null;

    /**
     * Constructs with the specified {@link File} identifying the
     * repository directory.
     * 
     * @param repoDirectory The {@link File} identifying the repository drectory.
     * 
     * @throws IOException If a failure occurs reading the repo settings.
     */
    public RepoMgrTestDataLoader(File repoDirectory) 
    {
        try { 
           this.repoDirectory = repoDirectory;
            File initJsonFile = new File(repoDirectory, "sz-init.json");
            this.settings = readTextFileAsString(initJsonFile, UTF_8).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configureDataSources(String dataSource, String... addlDataSources) 
    {
        Set<String> sources = new LinkedHashSet<>();
        sources.add(dataSource);
        if (addlDataSources != null) {
            for (String source : addlDataSources) {
                sources.add(source);
            }
        }

        RepositoryManager.configSources(this.repoDirectory, sources, true);
    }

    @Override
    public String loadAndGetEntity(String dataSourceCode,
                                   String recordId,
                                   String recordDefinition,
                                   long   flags)  
    {
        JsonObject jsonObject = JsonUtilities.parseJsonObject(recordDefinition);
        String jsonRecordId = JsonUtilities.getString(jsonObject, "RECORD_ID");
        if (jsonRecordId == null) {
            JsonObjectBuilder job = Json.createObjectBuilder(jsonObject);
            job.add("RECORD_ID", recordId);
            recordDefinition = JsonUtilities.toJsonText(job);

        } else if (!jsonRecordId.equals(recordId)) {
            throw new IllegalArgumentException(
                "Specified record ID (" + recordId 
                + ") does not match record ID from JSON (" 
                + jsonRecordId + ")");
        }

        RepositoryManager.addRecord(this.repoDirectory, recordDefinition, dataSourceCode, true);
        
        String result = RepositoryManager.getEntity(
            repoDirectory, dataSourceCode, jsonRecordId, flags, true);

        if (result == null) {
            throw new IllegalStateException(
                "Unable to get entity for record.  dataSourceCode=[ " 
                + dataSourceCode + " ], recordId=[ " + recordId + " ]");
        }

        return result;
    }

    @Override
    public SzEntityLookup getEntityLookup(Set<SzRecordKey> recordKeys) {
        Map<SzRecordKey,Long> recordMap = new LinkedHashMap<>();

        NativeEngine engine = new NativeEngineJni();
        int returnCode = engine.init("Test Data Loader", 
                                     this.settings,
                                     false);
        if (returnCode != 0) {
            throw new RuntimeException(engine.getLastException());
        }
        try {
            StringBuffer sb = new StringBuffer();
            for (SzRecordKey key : recordKeys) {
                // clear the buffer
                sb.delete(0, sb.length());
                returnCode = engine.getEntityByRecordID(
                    key.dataSourceCode(), key.recordId(), sb);
                if (returnCode != 0) {
                    throw new RuntimeException(engine.getLastException());
                }
                // parse the JSON 
                JsonObject  jsonObj     = parseJsonObject(sb.toString());
                JsonObject  entity      = getJsonObject(jsonObj, "RESOLVED_ENTITY");
                Long        entityId    = getLong(entity, "ENTITY_ID");

                recordMap.put(key, entityId);
            }
        } finally {
            engine.destroy();
        }

        // return the entity lookup
        return new SzEntityLookup(recordMap);
    }

    @Override
    public Map<SzRecordKey,String> loadRecords(String dataSourceCode, File file, String encoding) 
    {
        // check if the encoding is not supported
        if (encoding != null && !UTF_8.equals(encoding)) {
            throw new IllegalArgumentException(
                "The specified encoding is not supported by repository manager; "
                + encoding);
        }

        Map<SzRecordKey,String> recordMap = new LinkedHashMap<>();
        RepositoryManager.loadFile(this.repoDirectory, 
                                   file,
                                   dataSourceCode,
                                   recordMap,
                                   true);
        return recordMap;
    }

}
