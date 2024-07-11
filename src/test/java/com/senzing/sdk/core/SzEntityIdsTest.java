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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.senzing.sdk.SzEntityIds;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(Lifecycle.PER_CLASS)
public class SzEntityIdsTest extends AbstractTest {
    @Test
    void testBuilder() {
        SzEntityIds.Builder builder = SzEntityIds.builder();
        SzEntityIds ids = builder.id(10L).id(20L).build();

        assertTrue(ids.contains(10L), "Expected entity ID 10 to be present");
        assertTrue(ids.contains(20L), "Expected entity ID 20 to be present");

        Set<Long> set = Set.of(10L, 20L);
        assertEquals(set, ids, "SzEntityIds set not as expected");

        SzEntityIds ids2 = SzEntityIds.of(10L, 20L);
        assertEquals(ids2, ids, "SzEntityIds not equal to constructed instance");

        try {
            builder.build();
            fail("Unexpetedly built a second instance from one builder");

        } catch (IllegalStateException expected) {
            // do nothing
        }
    }

    @Test
    void testContains() {
        SzEntityIds ids = new SzEntityIds(10L, 20L);
        assertTrue(ids.contains(10L), "Expected entity ID 10 to be present");
        assertTrue(ids.contains(20L), "Expected entity ID 20 to be present");
        assertFalse(ids.contains(30L), "Entity ID 30 unexpectedly contained");
    }

    @Test
    void testContainsAll() {
        SzEntityIds ids = new SzEntityIds(30L, 40L);
        Set<Long> equalSet  = Set.of(30L, 40L);
        Set<Long> subset    = Set.of(30L);
        Set<Long> superSet  = Set.of(10L, 30L, 40L);
        
        assertTrue(ids.containsAll(equalSet), "The containsAll() function does not contain equivalent set");
        assertTrue(ids.containsAll(subset), "The containsAll() does not contain the subset");
        assertFalse(ids.containsAll(superSet), "The SzEntityIds unexpectedly contains the superset");
    }

    @Test
    void testIterator() {
        SzEntityIds ids = new SzEntityIds(10L, 20L, 30L, 40L, 50L);
        Iterator<Long> iter = ids.iterator();
        Set<Long> expected  = Set.of(10L, 20L, 30L, 40L, 50L);
        Set<Long> actual    = new TreeSet<>();
        while (iter.hasNext()) {
            actual.add(iter.next());
        }
        assertEquals(expected, actual, "Iteration did not yield the same set");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testConstruct(int elementCount) {
        Class<?>[] paramTypes = new Class[elementCount];
        Object[] params = new Object[elementCount];
        Set<Long> expected = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            paramTypes[index] = Long.class;
            long entityId = ((long)(index + 1)) * 10L;
            params[index] = entityId;
            expected.add(entityId);
        }
        
        SzEntityIds                 ids     = null;
        Constructor<SzEntityIds>    ctor    = null;
        try {
            ctor = SzEntityIds.class.getConstructor(paramTypes);
        } catch (Exception e) {
            fail("Failed to get constructor by reflection with "
                 + elementCount + " parameters", e);
        }
        try {
            ids = ctor.newInstance(params);
        } catch (Exception e) {
            fail("Failed to invoke constructor by reflection with "
                 + elementCount + " parameters", e);
        }
        assertEquals(elementCount, ids.size(),
                     "SzEntityIds not of expected size after construction "
                     + "with " + elementCount + " parameters");

        assertEquals(expected, ids, 
                     "SzEntityIds not as expected after construction "
                     + "with " + elementCount + " parameters");
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15})
    void testConstructByArray(int elementCount) {
        Long[] params = new Long[elementCount];
        Set<Long> expected = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            Long entityId = ((long)(index + 1)) * 10L;
            params[index] = entityId;
            expected.add(entityId);
        }
        
        SzEntityIds ids = new SzEntityIds(params);
        assertEquals(elementCount, ids.size(),
                     "SzEntityIds not of expected size after construction "
                     + "by array with size " + elementCount);

        assertEquals(expected, ids, 
                     "SzEntityIds not as expected after construction by "
                     + "array with size " + elementCount);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testOf(int elementCount) {
        Class<?>[] paramTypes = new Class[elementCount];
        Object[] params = new Object[elementCount];
        Set<Long> expected = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            paramTypes[index] = Long.class;
            long entityId = ((long)(index + 1)) * 10L;
            params[index] = entityId;
            expected.add(entityId);
        }
        
        SzEntityIds ids = null;
        Method method = null;
        try {
            method = SzEntityIds.class.getMethod("of", paramTypes);
        } catch (Exception e) {
            fail("Failed to get 'of' method by reflection with "
                 + elementCount + " parameters", e);
        }
        try {
            ids = (SzEntityIds) method.invoke(null, params);
        } catch (Exception e) {
            fail("Failed to invoke 'of' method by reflection with "
                 + elementCount + " parameters: " + method, e);
        }
        assertEquals(elementCount, ids.size(),
                     "SzEntityIds not of expected size after construction "
                     + "via 'of' with " + elementCount + " parameters: " + method);

        assertEquals(expected, ids, 
                     "SzEntityIds not as expected after construction via "
                     + "'of' with " + elementCount + " parameters: " + method);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15})
    void testOfArray(int elementCount) {
        Long[] params = new Long[elementCount];
        Set<Long> expected = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            Long entityId = ((long)(index + 1)) * 10L;
            params[index] = entityId;
            expected.add(entityId);
        }
        
        SzEntityIds ids = SzEntityIds.of(params);
        assertEquals(elementCount, ids.size(),
                     "SzEntityIds not of expected size after construction "
                     + "via 'of-by-array' with " + elementCount + " parameters");

        assertEquals(expected, ids, 
                     "SzEntityIds not as expected after construction via "
                     + "'of-by-array' with " + elementCount + " parameters");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 10})
    void testSize(int elementCount) {
        Set<Long> entityIds = new TreeSet<>();
        for (int index = 0; index < elementCount; index++) {
            Long entityId = ((long)(index + 1)) * 10L;
            entityIds.add(entityId);
        }
        
        SzEntityIds ids = new SzEntityIds(entityIds);
        assertEquals(entityIds.size(), ids.size(),
                     "SzEntityIds not of expected size");
    }

    private List<Arguments> getToStringParameters() {
        List<Arguments> result = new LinkedList<>();

        result.add(Arguments.of(
            Set.of(), "{ }"));
        result.add(Arguments.of(
            Set.of(10L, 20L, 30L), "{ 10, 20, 30 }"));
        result.add(Arguments.of(
            Set.of(50L, 60L, 70L), "{ 50, 60, 70 }"));

        return result;

    }
    @ParameterizedTest
    @MethodSource("getToStringParameters")
    void testToString(Set<Long> set, String expected) {
        SzEntityIds ids = SzEntityIds.of(set);
        assertEquals(expected, ids.toString(),
            "The toString() result is not as expected.");
    }

    @Test
    void testOfNullCollection() {
        Set<Long> set = null;
        SzEntityIds ids = SzEntityIds.of(set);
        assertNull(ids, 
            "The SzEntityIds built from a null Collection is not null");
    }

    @Test
    void testOfNullArray() {
        Long[] array = null;
        SzEntityIds ids = SzEntityIds.of(array);
        assertNull(ids, 
            "The SzEntityIds built from a null Long array is not null");
    }

    @Test
    void testOfNullStringArray() {
        long[] array = null;
        SzEntityIds ids = SzEntityIds.of(array);
        assertNull(ids, 
            "The SzEntityIds built from a null long array is not null");
    }

}
