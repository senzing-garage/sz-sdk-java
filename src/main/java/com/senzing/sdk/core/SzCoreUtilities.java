package com.senzing.sdk.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.senzing.sdk.SzException;

/**
 * Provides utility functions that are useful when implementing
 * the Senzing SDK interfaces from the <code>com.senzing.sdk</code>
 * package.  This is functionality leveraged by the "core" implementation
 * that is made visible to aid in other implementations.
 * 
 * None of the functionality provided here is required to use the 
 * {@link SzCoreEnvironment} class, but may be useful if creating an
 * alternate implementation of {@link com.senzing.sdk.SzEnvironment}.
 */
public final class SzCoreUtilities {
    /**
     * The <b>unmodifiable</b> {@link Set} of default data sources.
     */
    private static final Set<String> DEFAULT_SOURCES 
        = Set.of("TEST", "SEARCH");


    /**
     * The <b>unmodifiable</b> {@link Map} of integer error code keys 
     * to {@link Class} values representing the exception class associated
     * with the respective error code.  The {@link Map} does not store
     * entries that map to {@link SzException} since that is the default
     * for any error code not otherwise mapped.
     */
    static final Map<Integer, Class<? extends SzException>> EXCEPTION_MAP;

    static {
        Map<Integer, Class<? extends SzException>>   map     = new LinkedHashMap<>();
        Map<Integer, Class<? extends SzException>>   result  = new LinkedHashMap<>();
        SzExceptionMapper.registerExceptions(map);
        map.forEach((errorCode, exceptionClass) -> {
            if (exceptionClass != SzException.class) {
                result.put(errorCode, exceptionClass);
            }
        });
        EXCEPTION_MAP = Collections.unmodifiableMap(result);
    }

    /**
     * Private default constructor.
     */
    private SzCoreUtilities() {
        // do nothing
    }

    /**
     * Creates the appropriate {@link SzException} instance for the specified
     * error code.
     * <p>
     * If there is a failure in creating the {@link SzException} instance,
     * then a generic {@link SzException} is created with the specified 
     * parameters and "caused by" exception describing the failure.
     * 
     * @param errorCode The error code to use to determine the specific type
     *                  for the {@link SzException} instance.
     * 
     * @param message The error message to associate with the exception.
     * 
     * @return A new instance of {@link SzException} (or one of its derived
     *         classes) for the specified error code and message.
     */
    public static SzException createSzException(int errorCode, String message)
    {
        // get the exception class
        Class<? extends SzException> exceptionClass 
            = EXCEPTION_MAP.containsKey(errorCode)
            ? EXCEPTION_MAP.get(errorCode)
            : SzException.class;
        
        try {
            return exceptionClass.getConstructor(Integer.TYPE, String.class)
                                 .newInstance(errorCode, message);
            
        } catch (Exception e) {
            return new SzException(errorCode, message, e);
        }
    }

    /**
     * Produce an auto-generated configuration comment describing
     * a configuration.  This is useful when implementing the 
     * {@link com.senzing.sdk.SzConfigManager#registerConfig(String)}
     * function.
     * 
     * <p>
     * This function will not fail on an invalid configuration,
     * but rather will simply return an empty-string for the 
     * comment.
     * </p>
     * 
     * @param configDefinition The JSON {@link String} configuration
     *                         definition.
     * 
     * @return The auto-generated comment, which may be empty-string
     *         if an auto-generated comment could not be inferred.
     */
    public static String createConfigComment(String configDefinition) 
    {
        int index = configDefinition.indexOf("\"CFG_DSRC\"");
        if (index < 0) {
            return "";
        }
        char[] charArray = configDefinition.toCharArray();

        // advance past any whitespace
        index = eatWhiteSpace(charArray, index + "\"CFG_DSRC\"".length());
        
        // check for the colon
        if (index >= charArray.length || charArray[index++] != ':') {
            return "";
        }

        // advance past any whitespace
        index = eatWhiteSpace(charArray, index);
        
        // check for the open bracket
        if (index >= charArray.length || charArray[index++] != '[') {
            return "";
        }

        // find the end index
        int endIndex = configDefinition.indexOf("]", index);
        if (endIndex < 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Data Sources: ");
        String prefix = "";
        int dataSourceCount = 0;
        Set<String> defaultSources = new TreeSet<>();
        while (index > 0 && index < endIndex) {
            index = configDefinition.indexOf("\"DSRC_CODE\"", index);
            if (index < 0 || index >= endIndex) {
                continue;
            }
            index = eatWhiteSpace(charArray, index + "\"DSRC_CODE\"".length());
            
            // check for the colon
            if (index >= endIndex || charArray[index++] != ':') {
                return "";
            }

            index = eatWhiteSpace(charArray, index);

            // check for the quote
            if (index >= endIndex || charArray[index++] != '"') {
                return "";
            }
            int start = index;

            // find the ending quote
            while (index < endIndex && charArray[index] != '"') {
                index++;
            }
            if (index >= endIndex) {
                return "";
            }
            
            // get the data source code
            String dataSource = configDefinition.substring(start, index);
            if (DEFAULT_SOURCES.contains(dataSource)) {
                defaultSources.add(dataSource);
                continue;
            }
            sb.append(prefix);
            sb.append(dataSource);
            dataSourceCount++;
            prefix = ", ";
        }

        // check if only the default data sources
        if (dataSourceCount == 0 && defaultSources.size() == 0) {
            sb.append("[ NONE ]");
        } else if (dataSourceCount == 0 
                   && defaultSources.size() == DEFAULT_SOURCES.size()) 
        {
            sb.append("[ ONLY DEFAULT ]");

        } else if (dataSourceCount == 0) {

            sb.append("[ SOME DEFAULT (");
            prefix = "";
            for (String source : defaultSources) {
                sb.append(prefix);
                sb.append(source);
                prefix = ", ";
            }
            sb.append(") ]");
        }

        // return the constructed string
        return sb.toString();
    }

    /**
     * Finds the index of the first non-whitespace character after the
     * specified index from the specified character array.
     * 
     * @param charArray The character array.
     * @param fromIndex The starting index.
     * 
     * @return The index of the first non-whitespace character or the 
     *         length of the character array if no non-whitespace 
     *         character is found.
     */
    private static int eatWhiteSpace(char[] charArray, int fromIndex) {
        int index = fromIndex;
        
        // advance past any whitespace
        while (index < charArray.length && Character.isWhitespace(charArray[index])) {
            index++;
        }

        // return the index
        return index;
    }

}
