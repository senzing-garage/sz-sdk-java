package com.senzing.sdk.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArray;
import javax.json.JsonObject;

import com.senzing.io.RecordReader.Format;
import com.senzing.sdk.SzEnvironment;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzProduct;
import com.senzing.util.JsonUtilities;
import com.senzing.util.SemanticVersion;

import static com.senzing.io.RecordReader.Format;

public interface SdkTest {
    /**
     * Increments the failure count and returns the new failure count.
     * 
     * @return The new failure count.
     */
    int incrementFailureCount();

    /**
     * Increments the success count and returns the new success count.
     * 
     * @return The new success count.
     */
    int incrementSuccessCount();

    /**
     * Wrapper function for performing a test and recording the 
     * success or failure.
     *
     * @param testFunction The {@link Runnable} to execute.
     */
    default void performTest(Runnable testFunction) {
        boolean success = false;
        try {
            testFunction.run();
            success = true;

        } catch (Error | RuntimeException e) {
            e.printStackTrace();
            System.err.flush();
            if ("true".equals(System.getProperty("com.senzing.sdk.test.fastFail"))) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException ignore) {
                    // do nothing
                }
                System.exit(1);
            }
            throw e;
        } finally {
            if (!success)
                this.incrementFailureCount();
            else
                this.incrementSuccessCount();
        }
    }

    /**
     * Obtains the Senzing version number using the specified 
     * {@link SzEnvironment} and returns it as an instance of
     * {@link SemanticVersion}.
     * 
     * @param env The {@link SzEnvironment} to use.
     * 
     * @return The {@link SemanticVersion} obtained.
     * 
     * @throws IllegalStateException If the version cannot be obtained.
     * 
     * @throws NullPointerException If the specified parameter is <code>null</code>.
     */
    public static SemanticVersion getSenzingVersion(SzEnvironment env) {
        Objects.requireNonNull(env, "The SzEnvironment cannot be null");
        try {
            SzProduct product = env.getProduct();

            String versionJson = product.getVersion();
            
            JsonObject jsonObj = JsonUtilities.parseJsonObject(versionJson);

            String version = JsonUtilities.getString(jsonObj, "VERSION");
            
            if (version == null) {
                throw new IllegalStateException(
                    "Did not find Senzing VERSION in JSON: " + versionJson);
            }

            return new SemanticVersion(version);

        } catch (SzException e) {
            throw new IllegalStateException("Unexpected failure getting version", e);
        }
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that no unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMap(Object jsonData, String... expectedKeys) {
        validateJsonDataMap(null,
                jsonData,
                true,
                expectedKeys);
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that no unexpected keys are present.
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMap(String testInfo,
            Object jsonData,
            String... expectedKeys) {
        validateJsonDataMap(testInfo, jsonData, true, expectedKeys);
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that, optionally, no unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys -- these are
     *                     either a minimum or exact set depending on the
     *                     <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMap(Object jsonData,
            boolean strict,
            String... expectedKeys) {
        validateJsonDataMap(null, jsonData, strict, expectedKeys);
    }

    /**
     * Validates the json data and ensures the expected JSON property keys are
     * present and that, optionally, no unexpected keys are present.
     *
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys -- these are
     *                     either a minimum or exact set depending on the
     *                     <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMap(String testInfo,
            Object jsonData,
            boolean strict,
            String... expectedKeys) {
        String suffix = (testInfo != null && testInfo.trim().length() > 0)
                ? " ( " + testInfo + " )"
                : "";

        if (jsonData == null) {
            fail("Expected json data but got null value" + suffix);
        }

        if (!(jsonData instanceof Map)) {
            fail("Raw data is not a JSON object: " + jsonData + suffix);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) jsonData;
        Set<String> expectedKeySet = new HashSet<>();
        Set<String> actualKeySet = map.keySet();
        for (String key : expectedKeys) {
            expectedKeySet.add(key);
            if (!actualKeySet.contains(key)) {
                fail("JSON property missing from json data: " + key + " / " + map
                        + suffix);
            }
        }
        if (strict && expectedKeySet.size() != actualKeySet.size()) {
            Set<String> extraKeySet = new HashSet<>(actualKeySet);
            extraKeySet.removeAll(expectedKeySet);
            fail("Unexpected JSON properties in json data: " + extraKeySet + suffix);
        }

    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that no
     * unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMapArray(Object jsonData,
            String... expectedKeys) {
        validateJsonDataMapArray(null, jsonData, true, expectedKeys);
    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that no
     * unexpected keys are present.
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param expectedKeys The zero or more expected property keys.
     */
    public static void validateJsonDataMapArray(String testInfo,
            Object jsonData,
            String... expectedKeys) {
        validateJsonDataMapArray(testInfo, jsonData, true, expectedKeys);
    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that,
     * optionally, no unexpected keys are present.
     *
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys for the array
     *                     objects -- these are either a minimum or exact set
     *                     depending on the <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMapArray(Object jsonData,
            boolean strict,
            String... expectedKeys) {
        validateJsonDataMapArray(null, jsonData, strict, expectedKeys);
    }

    /**
     * Validates the json data and ensures it is a collection of objects and the
     * expected JSON property keys are present in the array objects and that,
     * optionally, no unexpected keys are present.
     *
     * @param testInfo     Additional test information to be logged with failures.
     * @param jsonData     The json data to validate.
     * @param strict       Whether or not property keys other than those specified
     *                     are
     *                     allowed to be present.
     * @param expectedKeys The zero or more expected property keys for the array
     *                     objects -- these are either a minimum or exact set
     *                     depending on the <tt>strict</tt> parameter.
     */
    public static void validateJsonDataMapArray(String testInfo,
            Object jsonData,
            boolean strict,
            String... expectedKeys) {
        String suffix = (testInfo != null && testInfo.trim().length() > 0)
                ? " ( " + testInfo + " )"
                : "";

        if (jsonData == null) {
            fail("Expected json data but got null value" + suffix);
        }

        if (!(jsonData instanceof Collection)) {
            fail("Raw data is not a JSON array: " + jsonData + suffix);
        }

        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) jsonData;
        Set<String> expectedKeySet = new HashSet<>();
        for (String key : expectedKeys) {
            expectedKeySet.add(key);
        }

        for (Object obj : collection) {
            if (!(obj instanceof Map)) {
                fail("Raw data is not a JSON array of JSON objects: " + jsonData + suffix);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;

            Set<String> actualKeySet = map.keySet();
            for (String key : expectedKeySet) {
                if (!actualKeySet.contains(key)) {
                    fail("JSON property missing from json data array element: "
                            + key + " / " + map + suffix);
                }
            }
            if (strict && expectedKeySet.size() != actualKeySet.size()) {
                Set<String> extraKeySet = new HashSet<>(actualKeySet);
                extraKeySet.removeAll(expectedKeySet);
                fail("Unexpected JSON properties in json data: " + extraKeySet + suffix);
            }
        }
    }

    /**
     * Generates option combos for the specified variants.
     *
     * @param variants
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static List<List<?>> generateCombinations(List<?>... variants) {
        // determine the total number of combinations
        int comboCount = 1;
        for (List<?> v : variants) {
            comboCount *= v.size();
        }

        // determine the intervals for each variant
        List<Integer> intervals = new ArrayList<>(variants.length);
        // iterate over the variants
        for (int index = 0; index < variants.length; index++) {
            // default the interval count to one (1)
            int intervalCount = 1;

            // loop over the remaining variants after the current
            for (int index2 = index + 1; index2 < variants.length; index2++) {
                // multiply the interval count by the remaining variant sizes
                intervalCount *= variants[index2].size();
            }

            // add the interval count
            intervals.add(intervalCount);
        }

        ArrayList<List<?>> optionCombos = new ArrayList<>(comboCount);
        for (int comboIndex = 0; comboIndex < comboCount; comboIndex++) {
            List optionCombo = new ArrayList(variants.length);

            for (int index = 0; index < variants.length; index++) {
                int interval = intervals.get(index);
                int valueIndex = (comboIndex / interval) % variants[index].size();
                optionCombo.add(variants[index].get(valueIndex));
            }

            optionCombos.add(optionCombo);
        }

        return optionCombos;
    }

    /**
     * Gets the variant possible values of booleans for the specified
     * parameter count. This includes <tt>null</tt> values.
     *
     * @param paramCount  The number of boolean parameters.
     * @param includeNull Whether or not to include null values.
     * @return The {@link List} of parameter value lists.
     */
    public static List<List<Boolean>> getBooleanVariants(
            int paramCount,
            boolean includeNull) {
        Boolean[] boolsSansNull = { true, false };
        Boolean[] boolsWithNull = { null, true, false };

        Boolean[] booleanValues = (includeNull) ? boolsWithNull : boolsSansNull;

        int variantCount = (int) Math.ceil(Math.pow(booleanValues.length, paramCount));
        List<List<Boolean>> variants = new ArrayList<>(variantCount);

        // iterate over the variants
        for (int index1 = 0; index1 < variantCount; index1++) {
            // create the parameter list
            List<Boolean> params = new ArrayList<>(paramCount);

            // iterate over the parameter slots
            for (int index2 = 0; index2 < paramCount; index2++) {
                int repeat = (int) Math.ceil(Math.pow(booleanValues.length, index2));
                int valueIndex = (index1 / repeat) % booleanValues.length;
                params.add(booleanValues[valueIndex]);
            }

            // add the combinatorial variant
            variants.add(params);
        }
        return variants;
    }

    /**
     * Returns an iterator that iterates over the specified {@link Collection}
     * in a circular fashion.
     *
     * @param collection The {@link Collection} to iterate over.
     * @return The circular {@link Iterator}.
     */
    public static <T> Iterator<T> circularIterator(Collection<T> collection) {
        return new CircularIterator<>(collection);
    }

    /**
     * Creates a temporary data file with the specified headers and records
     * named using the specified file prefix.
     * 
     * @param format The {@link Format} for the file.
     * 
     * @param filePrefix The prefix for the temp file name.
     * 
     * @param headers The CSV headers.
     * 
     * @param records The one or more records.
     * 
     * @return The {@link File} object for the created file.
     */
    public static File prepareDataFile(Format       format,
                                       String       filePrefix,
                                       String[]     headers,
                                       String[]...  records)
    {
        switch (format) {
            case CSV:
                return prepareCSVFile(filePrefix, headers, records);

            case JSON:
                return prepareJsonArrayFile(filePrefix, headers, records);
                
            case JSON_LINES:
                return prepareJsonLinesFile(filePrefix, headers, records);

            default:
                throw new IllegalArgumentException("Unrecognized format; " + format);
        }
    }

    /**
     * Creates a CSV temp file with the specified headers and records.
     *
     * @param filePrefix The prefix for the temp file name.
     * @param headers    The CSV headers.
     * @param records    The one or more records.
     * @return The {@link File} that was created.
     */
    public static File prepareCSVFile(String      filePrefix,
                                        String[]    headers,
                                        String[]... records) 
    {
        // check the arguments
        int count = headers.length;
        for (int index = 0; index < records.length; index++) {
        String[] record = records[index];
        if (record.length != count) {
            throw new IllegalArgumentException(
                "The header and records do not all have the same number of "
                    + "elements.  expected=[ " + count + " ], received=[ "
                    + record.length + " ], index=[ " + index + " ]");
        }
        }

        try {
        File csvFile = File.createTempFile(filePrefix, ".csv");

        // populate the file as a CSV
        try (FileOutputStream fos = new FileOutputStream(csvFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            PrintWriter pw = new PrintWriter(osw)) {
            String prefix = "";
            for (String header : headers) {
            pw.print(prefix);
            pw.print(csvQuote(header));
            prefix = ",";
            }
            pw.println();
            pw.flush();

            for (String[] record : records) {
            prefix = "";
            for (String value : record) {
                pw.print(prefix);
                pw.print(csvQuote(value));
                prefix = ",";
            }
            pw.println();
            pw.flush();
            }
            pw.flush();

        }

        return csvFile;

        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    /**
     * Creates a JSON array temp file with the specified headers and records.
     *
     * @param filePrefix The prefix for the temp file name.
     * @param headers    The CSV headers.
     * @param records    The one or more records.
     * @return The {@link File} that was created.
     */
    public static File prepareJsonArrayFile(String      filePrefix,
                                            String[]    headers,
                                            String[]... records) 
    {
        // check the arguments
        int count = headers.length;
        for (int index = 0; index < records.length; index++) {
        String[] record = records[index];
        if (record.length != count) {
            throw new IllegalArgumentException(
                "The header and records do not all have the same number of "
                    + "elements.  expected=[ " + count + " ], received=[ "
                    + record.length + " ], index=[ " + index + " ]");
        }
        }

        try {
        File jsonFile = File.createTempFile(filePrefix, ".json");

        // populate the file with a JSON array
        try (FileOutputStream fos = new FileOutputStream(jsonFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8")) {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            JsonObjectBuilder job = Json.createObjectBuilder();
            for (String[] record : records) {
            for (int index = 0; index < record.length; index++) {
                String key = headers[index];
                String value = record[index];
                job.add(key, value);
            }
            jab.add(job);
            }

            String jsonText = JsonUtilities.toJsonText(jab);
            osw.write(jsonText);
            osw.flush();
        }

        return jsonFile;

        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    /**
     * Creates a JSON temp file with the specified headers and records.
     *
     * @param filePrefix The prefix for the temp file name.
     * @param headers    The CSV headers.
     * @param records    The one or more records.
     * @return The {@link File} that was created.
     */
    public static File prepareJsonLinesFile(String      filePrefix,
                                            String[]    headers,
                                            String[]... records) 
    {
        // check the arguments
        int count = headers.length;
        for (int index = 0; index < records.length; index++) {
        String[] record = records[index];
        if (record.length != count) {
            throw new IllegalArgumentException(
                "The header and records do not all have the same number of "
                    + "elements.  expected=[ " + count + " ], received=[ "
                    + record.length + " ], index=[ " + index + " ]");
        }
        }

        try {
        File jsonFile = File.createTempFile(filePrefix, ".json");

        // populate the file as one JSON record per line
        try (FileOutputStream fos = new FileOutputStream(jsonFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            PrintWriter pw = new PrintWriter(osw)) {
            for (String[] record : records) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            for (int index = 0; index < record.length; index++) {
                String key = headers[index];
                String value = record[index];
                job.add(key, value);
            }
            String jsonText = JsonUtilities.toJsonText(job);
            pw.println(jsonText);
            pw.flush();
            }
            pw.flush();
        }

        return jsonFile;

        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    /**
     * Creates a JSON-lines temp file with the specified headers and records.
     *
     * @param filePrefix The prefix for the temp file name.
     * @param jsonArray The {@link JsonArray} describing the records.
     * @return The {@link File} that was created.
     */
    public static File prepareJsonArrayFile(String filePrefix, JsonArray jsonArray) 
    {
        try {
        File jsonFile = File.createTempFile(filePrefix, ".json");

        // populate the file as one JSON record per line
        try (FileOutputStream fos = new FileOutputStream(jsonFile);
             OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
             PrintWriter pw = new PrintWriter(osw))
        {
            String jsonText = JsonUtilities.toJsonText(jsonArray, true);
            pw.println(jsonText);
            pw.flush();
        }

        return jsonFile;

        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    /**
     * Creates a JSON-lines temp file with the specified headers and records.
     *
     * @param filePrefix The prefix for the temp file name.
     * @param jsonArray The {@link JsonArray} describing the records.
     * @return The {@link File} that was created.
     */
    public static File prepareJsonFile(String filePrefix, JsonArray jsonArray) {
        try {
        File jsonFile = File.createTempFile(filePrefix, ".json");

        // populate the file as one JSON record per line
        try (FileOutputStream fos = new FileOutputStream(jsonFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            PrintWriter pw = new PrintWriter(osw)) {
            for (JsonObject record: jsonArray.getValuesAs(JsonObject.class)) {
            String jsonText = JsonUtilities.toJsonText(record);
            pw.println(jsonText);
            pw.flush();
            }
            pw.flush();
        }

        return jsonFile;

        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    /**
     * Quotes the specified text as a quoted string for a CSV value or header.
     *
     * @param text The text to be quoted.
     * @return The quoted text.
     */
    public static String csvQuote(String text) {
        if (text.indexOf("\"") < 0 && text.indexOf("\\") < 0) {
        return "\"" + text + "\"";
        }
        char[] textChars = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length() * 2);
        for (char c : textChars) {
        if (c == '"' || c == '\\') {
            sb.append('\\');
        }
        sb.append(c);
        }
        return sb.toString();
    }

}
