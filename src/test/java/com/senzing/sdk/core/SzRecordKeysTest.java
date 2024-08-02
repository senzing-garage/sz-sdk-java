package com.senzing.sdk.core;

import java.util.Set;
import java.util.Iterator;
import java.util.TreeSet;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.senzing.sdk.SzEntityIds;
import com.senzing.sdk.SzRecordKey;
import com.senzing.sdk.SzRecordKeys;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(Lifecycle.PER_CLASS)
public class SzRecordKeysTest extends AbstractTest {
    public static final String CUSTOMERS = "CUSTOMERS";

    public static final String EMPLOYEES = "EMPLOYEES";

    public static final String ABC123 = "ABC123";
    public static final String DEF456 = "DEF456";
    public static final String GHI789 = "GHI789";
    public static final String JKL012 = "JKL012";
    public static final String MNO123 = "MNO123";
    public static final String PQR456 = "PQR456";
    public static final String STU789 = "STU789";
    public static final String VWX012 = "VWX012";

    public static final SzRecordKey CUSTOMERS_ABC123
        = SzRecordKey.of(CUSTOMERS, ABC123);

    public static final SzRecordKey CUSTOMERS_DEF456
        = SzRecordKey.of(CUSTOMERS, DEF456);
    
    public static final SzRecordKey CUSTOMERS_GHI789
        = SzRecordKey.of(CUSTOMERS, GHI789);

    public static final SzRecordKey CUSTOMERS_JKL012
        = SzRecordKey.of(CUSTOMERS, JKL012);

    public static final SzRecordKey CUSTOMERS_MNO123
        = SzRecordKey.of(CUSTOMERS, MNO123);

    public static final SzRecordKey CUSTOMERS_PQR456
        = SzRecordKey.of(CUSTOMERS, PQR456);

    public static final SzRecordKey CUSTOMERS_STU789
        = SzRecordKey.of(CUSTOMERS, STU789);

    public static final SzRecordKey CUSTOMERS_VWX012
        = SzRecordKey.of(CUSTOMERS, VWX012);

    public static final SzRecordKey EMPLOYEES_ABC123
        = SzRecordKey.of(EMPLOYEES, ABC123);

    public static final SzRecordKey EMPLOYEES_DEF456
        = SzRecordKey.of(EMPLOYEES, DEF456);
    
    public static final SzRecordKey EMPLOYEES_GHI789
        = SzRecordKey.of(EMPLOYEES, GHI789);

    public static final SzRecordKey EMPLOYEES_JKL012
        = SzRecordKey.of(EMPLOYEES, JKL012);

    public static final SzRecordKey EMPLOYEES_MNO123
        = SzRecordKey.of(EMPLOYEES, MNO123);

    public static final SzRecordKey EMPLOYEES_PQR456
        = SzRecordKey.of(EMPLOYEES, PQR456);

    public static final SzRecordKey EMPLOYEES_STU789
        = SzRecordKey.of(EMPLOYEES, STU789);

    public static final SzRecordKey EMPLOYEES_VWX012
        = SzRecordKey.of(EMPLOYEES, VWX012);

    public static final Set<SzRecordKey> RECORD_KEYS
        = Set.of(CUSTOMERS_ABC123,
                 CUSTOMERS_DEF456,
                 CUSTOMERS_GHI789,
                 CUSTOMERS_JKL012,
                 CUSTOMERS_MNO123,
                 CUSTOMERS_PQR456,
                 CUSTOMERS_STU789,
                 CUSTOMERS_VWX012,
                 EMPLOYEES_ABC123,
                 EMPLOYEES_DEF456,
                 EMPLOYEES_GHI789,
                 EMPLOYEES_JKL012,
                 EMPLOYEES_MNO123,
                 EMPLOYEES_PQR456,
                 EMPLOYEES_STU789,
                 EMPLOYEES_VWX012);
    
    public static final Set<String> DATA_SOURCES
        = Set.of(CUSTOMERS, EMPLOYEES);

    public static final Set<String> RECORD_IDS
        = Set.of(ABC123,
                 DEF456,
                 GHI789,
                 JKL012,
                 MNO123,
                 PQR456,
                 STU789,
                 VWX012);

    @Test
    void testBuilder() {
        SzRecordKeys.Builder builder = SzRecordKeys.newBuilder();
        SzRecordKeys keys = builder
            .key(CUSTOMERS, ABC123)
            .key(CUSTOMERS, DEF456)
            .key(CUSTOMERS_GHI789)
            .build();

        assertTrue(keys.contains(CUSTOMERS_ABC123), 
            "Expected record key to be present: " + CUSTOMERS_ABC123);
        assertTrue(keys.contains(CUSTOMERS_DEF456),
            "Expected record key to be present: " + CUSTOMERS_DEF456);

        Set<SzRecordKey> set 
            = Set.of(CUSTOMERS_ABC123, CUSTOMERS_DEF456, CUSTOMERS_GHI789);
        assertEquals(set, keys, "SzRecordKeys set not as expected");

        SzRecordKeys keys2
            = SzRecordKeys.of(CUSTOMERS_ABC123, CUSTOMERS_DEF456, CUSTOMERS_GHI789);
        assertEquals(keys2, keys, "SzRecordKeys not equal to constructed instance");

        try {
            builder.build();
            fail("Unexpetedly built a second instance from one builder");

        } catch (IllegalStateException expected) {
            // do nothing
        }

    }

    @Test
    void testContains() {
        SzRecordKeys keys = new SzRecordKeys(CUSTOMERS_ABC123, EMPLOYEES_MNO123);
        assertTrue(keys.contains(CUSTOMERS_ABC123), 
            "Expected record key to be present: " + CUSTOMERS_ABC123);
        assertTrue(keys.contains(EMPLOYEES_MNO123), 
            "Expected record key to be present: " + EMPLOYEES_MNO123);
        assertFalse(keys.contains(CUSTOMERS_DEF456),
            "Record key unexpectedly present: " + CUSTOMERS_DEF456);
    }

    @Test
    void testContainsAll() {
        SzRecordKeys keys = new SzRecordKeys(CUSTOMERS_GHI789, EMPLOYEES_PQR456);
        Set<SzRecordKey> equalSet  = Set.of(CUSTOMERS_GHI789, EMPLOYEES_PQR456);
        Set<SzRecordKey> subset    = Set.of(CUSTOMERS_GHI789);
        Set<SzRecordKey> superSet  = Set.of(CUSTOMERS_GHI789, 
                                            EMPLOYEES_PQR456,
                                            CUSTOMERS_JKL012);
        
        assertTrue(keys.containsAll(equalSet), 
            "The containsAll() function does not contain equivalent set");
        assertTrue(keys.containsAll(subset), 
            "The containsAll() does not contain the subset");
        assertFalse(keys.containsAll(superSet), 
            "The SzRecordKeys unexpectedly contains the superset");
    }

    @Test
    void testIterator() {
        SzRecordKeys keys = new SzRecordKeys(CUSTOMERS_ABC123,
                                             CUSTOMERS_DEF456,
                                             CUSTOMERS_GHI789,
                                             CUSTOMERS_JKL012,
                                             EMPLOYEES_MNO123);
        
        Set<SzRecordKey> expected = Set.of(CUSTOMERS_ABC123,
                                           CUSTOMERS_DEF456,
                                           CUSTOMERS_GHI789,
                                           CUSTOMERS_JKL012,
                                           EMPLOYEES_MNO123);

        Set<SzRecordKey> actual = new TreeSet<>();
        Iterator<SzRecordKey> iter = keys.iterator();
        while (iter.hasNext()) {
            actual.add(iter.next());
        }
        assertEquals(expected, actual, 
            "Iteration did not yield the same set");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testConstructByKeys(int elementCount) {
        Class<?>[]              paramTypes  = new Class[elementCount];
        Object[]                params      = new Object[elementCount];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        for (int index = 0; index < elementCount; index++) {
            paramTypes[index] = SzRecordKey.class;
            SzRecordKey key = keyIter.next();
            params[index] = key;
            expected.add(key);
        }
        
        SzRecordKeys                keys    = null;
        Constructor<SzRecordKeys>   ctor    = null;
        try {
            ctor = SzRecordKeys.class.getConstructor(paramTypes);
        } catch (Exception e) {
            fail("Failed to get constructor by reflection with "
                 + elementCount + " SzRecordKey parameters", e);
        }
        try {
            keys = ctor.newInstance(params);
        } catch (Exception e) {
            fail("Failed to invoke constructor by reflection with "
                 + elementCount + " parameters: " + ctor, e);
        }
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "with " + elementCount + " SzRecordKey parameters");

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction "
                     + "with " + elementCount + " SzRecordKey parameters");
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18})
    void testConstructByKeyArray(int elementCount) {
        SzRecordKey[]           params      = new SzRecordKey[elementCount];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        for (int index = 0; index < elementCount; index++) {
            SzRecordKey key = keyIter.next();
            params[index] = key;
            expected.add(key);
        }
        
        SzRecordKeys keys = new SzRecordKeys(params);
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "by SzRecordKey array with size: " + elementCount);

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction by "
                     + "SzRecordKey array with size: " + elementCount);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testConstructByStrings(int elementCount) {
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        Class<?>[]              paramTypes  = new Class[elementCount * 2];
        Object[]                params      = new Object[elementCount * 2];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            int         index1  = (index * 2);
            int         index2  = (index * 2) + 1;
            SzRecordKey key     = keyIter.next();

            paramTypes[index1]  = String.class;
            paramTypes[index2]  = String.class;
            params[index1]      = key.dataSourceCode();
            params[index2]      = key.recordId();
            expected.add(key);
        }
        
        SzRecordKeys                keys    = null;
        Constructor<SzRecordKeys>   ctor    = null;
        try {
            ctor = SzRecordKeys.class.getConstructor(paramTypes);
        } catch (Exception e) {
            fail("Failed to get constructor by reflection with "
                 + (elementCount*2) + " String parameters", e);
        }
        try {
            keys = ctor.newInstance(params);
        } catch (Exception e) {
            fail("Failed to invoke constructor by reflection with "
                 + (elementCount*2) + " String parameters: " + ctor, e);
        }
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "with " + (elementCount*2) + " String parameters");

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction "
                     + "with " + (elementCount*2) + " String parameters");
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15})
    void testConstructByStringArray(int elementCount) {
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        String[]                params      = new String[elementCount * 2];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            int         index1  = (index * 2);
            int         index2  = (index * 2) + 1;
            SzRecordKey key     = keyIter.next();

            params[index1]      = key.dataSourceCode();
            params[index2]      = key.recordId();
            expected.add(key);
        }
        
        SzRecordKeys keys = new SzRecordKeys(params);
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "by String array with size: " + (elementCount * 2));

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction by "
                     + "String array with size: " + (elementCount * 2));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 5, 7, 9 })
    void testConstructByOddStringArray(int paramCount) {
        String[]            params      = new String[paramCount];
        Iterator<String>    sourceIter  = circularIterator(DATA_SOURCES);
        Iterator<String>    idIter      = circularIterator(RECORD_IDS);
        for (int index = 0; index < paramCount; index++) {
            params[index] = (index%2 == 0) 
                ? sourceIter.next() : idIter.next();
        }
        
        try {
            new SzRecordKeys(params);
            fail("Unexpectedly succeeded when constructing with an odd number "
                 + "of elements in the String array: " + paramCount);

        } catch (IllegalArgumentException expected) {
            // this is what we expect when constructing with an 
            // odd number of string parameters

        } catch (Exception e) {
            fail("Expected an IllegalArgumentException when constructing with "
                 + "an odd number of elements in the String array: " + paramCount, e);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testOfByKeys(int elementCount) {
        Class<?>[]              paramTypes  = new Class[elementCount];
        Object[]                params      = new Object[elementCount];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        for (int index = 0; index < elementCount; index++) {
            paramTypes[index] = SzRecordKey.class;
            SzRecordKey key = keyIter.next();
            params[index] = key;
            expected.add(key);
        }
        
        SzRecordKeys    keys    = null;
        Method          method  = null;
        try {
            method = SzRecordKeys.class.getMethod("of", paramTypes);
        } catch (Exception e) {
            fail("Failed to get 'of' method by reflection with "
                 + elementCount + " SzRecordKey parameters", e);
        }
        try {
            keys = (SzRecordKeys) method.invoke(null, params);
            
        } catch (Exception e) {
            fail("Failed to invoke 'of' method by reflection with "
                 + elementCount + " parameters: " + method, e);
        }
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "via 'of' method with " + elementCount
                     + " SzRecordKey parameters: " + method);

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction "
                     + "via 'of' method with " + elementCount
                     + " SzRecordKey parameters: " + method);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18})
    void testOfByKeyArray(int elementCount) {
        SzRecordKey[]           params      = new SzRecordKey[elementCount];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        for (int index = 0; index < elementCount; index++) {
            SzRecordKey key = keyIter.next();
            params[index] = key;
            expected.add(key);
        }
        
        SzRecordKeys keys = SzRecordKeys.of(params);
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "via 'of' method with an SzRecordKey array with size: " 
                     + elementCount);

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction via "
                     + "'of' method with an SzRecordKey array with size: "
                     + elementCount);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testOfByStrings(int elementCount) {
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        Class<?>[]              paramTypes  = new Class[elementCount * 2];
        Object[]                params      = new Object[elementCount * 2];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            int         index1  = (index * 2);
            int         index2  = (index * 2) + 1;
            SzRecordKey key     = keyIter.next();

            paramTypes[index1]  = String.class;
            paramTypes[index2]  = String.class;
            params[index1]      = key.dataSourceCode();
            params[index2]      = key.recordId();
            expected.add(key);
        }
        
        SzRecordKeys    keys    = null;
        Method          method  = null;
        try {
            method = SzRecordKeys.class.getMethod("of", paramTypes);
        } catch (Exception e) {
            fail("Failed to get 'of' method by reflection with "
                 + (elementCount*2) + " String parameters", e);
        }
        try {
            keys = (SzRecordKeys) method.invoke(null, params);
        } catch (Exception e) {
            fail("Failed to invoke 'of' method by reflection with "
                 + (elementCount*2) + " String parameters: " + method, 
                 e);
        }
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "with " + (elementCount*2) + " String parameters");

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction "
                     + "with " + (elementCount*2) + " String parameters");
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15})
    void testOfByStringArray(int elementCount) {
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        String[]                params      = new String[elementCount * 2];
        Set<SzRecordKey>        expected    = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            int         index1  = (index * 2);
            int         index2  = (index * 2) + 1;
            SzRecordKey key     = keyIter.next();

            params[index1]      = key.dataSourceCode();
            params[index2]      = key.recordId();
            expected.add(key);
        }
        
        SzRecordKeys keys = SzRecordKeys.of(params);
        assertEquals(expected.size(), keys.size(),
                     "SzRecordKeys not of expected size after construction "
                     + "via 'of' method using String array with size: " 
                     + (elementCount * 2));

        assertEquals(expected, keys, 
                     "SzRecordKeys not as expected after construction via "
                     + "'of' method using String array with size: "
                     + (elementCount * 2));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 5, 7, 9 })
    void testOfByOddStringArray(int paramCount) {
        String[]            params      = new String[paramCount];
        Iterator<String>    sourceIter  = circularIterator(DATA_SOURCES);
        Iterator<String>    idIter      = circularIterator(RECORD_IDS);
        for (int index = 0; index < paramCount; index++) {
            params[index] = (index%2 == 0) 
                ? sourceIter.next() : idIter.next();
        }
        
        try {
            SzRecordKeys.of(params);
            fail("Unexpectedly succeeded when constructing via 'of' method "
                 + "with an odd number of elements in the String array: "
                 + paramCount);

        } catch (IllegalArgumentException expected) {
            // this is what we expect when constructing with an 
            // odd number of string parameters

        } catch (Exception e) {
            fail("Expected an IllegalArgumentException when constructing via "
                 + "'of' method with an odd number of elements in the String "
                 + "array: " + paramCount, e);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 10})
    void testSize(int elementCount) {
        Iterator<SzRecordKey>   keyIter     = circularIterator(RECORD_KEYS);
        Set<SzRecordKey>        recordKeys = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            recordKeys.add(keyIter.next());
        }
        
        SzRecordKeys keys = new SzRecordKeys(recordKeys);
        assertEquals(recordKeys.size(), keys.size(),
                     "SzRecordKeys not of expected size");
    }

    private List<Arguments> getToStringParameters() {
        List<Arguments> result = new LinkedList<>();

        result.add(Arguments.of(
            Set.of(), "{ }"));
        result.add(Arguments.of(
            Set.of(CUSTOMERS_ABC123, EMPLOYEES_DEF456), 
            "{ [ " + CUSTOMERS_ABC123 + " ], [ " + EMPLOYEES_DEF456 + " ] }"));
        result.add(Arguments.of(
            Set.of(CUSTOMERS_DEF456, EMPLOYEES_ABC123, CUSTOMERS_PQR456),
            "{ [ " + CUSTOMERS_DEF456 + " ], [ " + CUSTOMERS_PQR456
            + " ], [ " + EMPLOYEES_ABC123 + " ] }"));

        return result;

    }
    @ParameterizedTest
    @MethodSource("getToStringParameters")
    void testToString(Set<SzRecordKey> set, String expected) {
        SzRecordKeys keys = SzRecordKeys.of(set);
        assertEquals(expected, keys.toString(),
            "The toString() result is not as expected.");
    }

    @Test
    void testOfNullCollection() {
        Set<SzRecordKey> set = null;
        SzRecordKeys keys = SzRecordKeys.of(set);
        assertNull(keys, 
            "The SzRecordKeys built from a null Collection is not null");
    }

    @Test
    void testOfNullKeyArray() {
        SzRecordKey[] array = null;
        SzRecordKeys keys = SzRecordKeys.of(array);
        assertNull(keys, 
            "The SzRecordKeys built from a null SzRecordKey array is not null");
    }

    @Test
    void testOfNullStringArray() {
        String[] array = null;
        SzRecordKeys keys = SzRecordKeys.of(array);
        assertNull(keys, 
            "The SzRecordKeys built from a null String array is not null");
    }

    @Test
    void testEmptyDataSourceCode() {
        try {
            new SzRecordKey("", "ABC-123");
            fail("Expected failure with empty data source code");
        } catch (IllegalArgumentException expected) {
            // all good
        }
    }

    @Test
    void testEmptyRecordId() {
        try {
            new SzRecordKey("CUSTOMERS", "");
            fail("Expected failure with empty record ID");
        } catch (IllegalArgumentException expected) {
            // all good
        }
    }

    @Test
    void compareVersusNull() {
        SzRecordKey key = new SzRecordKey("CUSTOMERS", "ABC-123");
        int result = key.compareTo(null);
        assertTrue((result > 0), "Compare result was not as expected: " + result);
        
    }

    @Test
    void testIllegalBuilderState() {
        SzRecordKeys.Builder builder = SzRecordKeys.newBuilder();
        builder.key("CUSTOMERS", "ABC-123").build();

        try {
            builder.key("EMPLOYEES", "DEF-456");

            fail("Builder not invalidated after build");

        } catch (IllegalStateException expected) {
            // all is well
        } 
    }

    @Test
    void testIllegalBuilderStateWithKey() {
        SzRecordKeys.Builder builder = SzRecordKeys.newBuilder();
        builder.key(CUSTOMERS_ABC123).build();

        try {
            builder.key(CUSTOMERS_DEF456);

            fail("Builder not invalidated after build");

        } catch (IllegalStateException expected) {
            // all is well
        } 
    }

}
