package com.senzing.sdk.core;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import com.senzing.sdk.SzBadInputException;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzNotFoundException;
import com.senzing.sdk.SzReplaceConflictException;
import com.senzing.sdk.SzUnknownDataSourceException;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.sdk.core.AbstractTest.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzExceptionTest {
    public List<Class<? extends SzException>> getExceptionTypes() {
        return List.of(
            SzException.class,
            SzBadInputException.class,
            SzNotFoundException.class,
            SzUnknownDataSourceException.class,
            SzReplaceConflictException.class);
    }

    public List<Arguments> getErrorCodeParameters() {
        List<Class<? extends SzException>> types = this.getExceptionTypes();
        List<Integer> codes = List.of(10, 20, 30, 40);
        List<List<?>> combos = generateCombinations(types, codes);

        List<Arguments> result = new ArrayList<>(combos.size());
        for (List<?> args : combos) {
            result.add(Arguments.of(args.toArray()));
        }
        return result;
    }

    @ParameterizedTest
    @MethodSource("getExceptionTypes")
    public void testDefaultConstruct(Class<? extends SzException> exceptionType) {
        SzException sze = null;
        try {
            sze = exceptionType.getConstructor().newInstance();
        } catch (Exception e) {
            fail("Failed to construct exception of type: " + exceptionType, e);
        }
        assertNull(sze.getMessage(), "Exception message not null: " + exceptionType);
        assertNull(sze.getCause(), "Exception cause not null: " + exceptionType);
        assertNull(sze.getErrorCode(), "Exception error code is not null: " + exceptionType);
        assertNotNull(sze.toString(), "Exception string is null: " + exceptionType);
    }

    @ParameterizedTest
    @MethodSource("getExceptionTypes")
    public void testMessageConstruct(Class<? extends SzException> exceptionType) {
        String message = "Some Message";
        SzException sze = null;
        try {
            sze = exceptionType.getConstructor(String.class)
                .newInstance(message);
        } catch (Exception e) {
            fail("Failed to construct exception of type: " + exceptionType, e);
        }

        assertEquals(message, sze.getMessage(), 
            "Exception message not as expected: " + exceptionType);
        assertNull(sze.getCause(), "Exception cause not null: " + exceptionType);
        assertNull(sze.getErrorCode(), "Exception error code is not null: " + exceptionType);
        assertNotNull(sze.toString(), "Exception string is null: " + exceptionType);
        String string = sze.toString();
        assertTrue(string.indexOf(message) >= 0,
            "Exception message not found in string representation: " + exceptionType);
    }

    @ParameterizedTest
    @MethodSource("getExceptionTypes")
    public void testCodeAndMessageConstruct(Class<? extends SzException> exceptionType) 
    {
        String message = "Some Message";
        int errorCode = 105;
        SzException sze = null;
        try {
            sze = exceptionType.getConstructor(Integer.TYPE, String.class)
                .newInstance(errorCode, message);
        } catch (Exception e) {
            fail("Failed to construct exception of type: " + exceptionType, e);
        }

        assertEquals(message, sze.getMessage(), 
            "Exception message not as expected: " + exceptionType);
        assertEquals(errorCode, sze.getErrorCode(), 
            "Exception error code is not as expected: " + exceptionType);
        assertNull(sze.getCause(), "Exception cause not null: " + exceptionType);
        assertNotNull(sze.toString(), "Exception string is null: " + exceptionType);
        String string = sze.toString();
        assertTrue(string.indexOf(message) >= 0,
            "Exception message not found in string representation: " + exceptionType);
    }

    @ParameterizedTest
    @MethodSource("getExceptionTypes")
    public void testCauseConstruct(Class<? extends SzException> exceptionType) {
        IllegalStateException cause = new IllegalStateException();
        SzException sze = null;
        try {
            sze = exceptionType.getConstructor(Throwable.class)
                .newInstance(cause);
        } catch (Exception e) {
            fail("Failed to construct exception of type: " + exceptionType, e);
        }

        assertEquals(cause, sze.getCause(), "Exception cause not as expected: " + exceptionType);
        assertTrue(cause == sze.getCause(), "Exception cause is not referrentially equal: " + exceptionType);
        assertNull(sze.getErrorCode(), "Exception error code is not null: " + exceptionType);
        assertNotNull(sze.toString(), "Exception string is null: " + exceptionType);
    }

    @ParameterizedTest
    @MethodSource("getExceptionTypes")
    public void testMessageAndCauseConstruct(Class<? extends SzException> exceptionType) {
        IllegalStateException cause = new IllegalStateException();
        String message = "Some Message";
        SzException sze = null;
        try {
            sze = exceptionType.getConstructor(String.class, Throwable.class)
                .newInstance(message, cause);
        } catch (Exception e) {
            fail("Failed to construct exception of type: " + exceptionType, e);
        }

        assertEquals(message, sze.getMessage(), 
            "Exception message not as expected: " + exceptionType);
        assertEquals(cause, sze.getCause(), "Exception cause not as expected: " + exceptionType);
        assertTrue(cause == sze.getCause(), "Exception cause is not referrentially equal: " + exceptionType);
        assertNull(sze.getErrorCode(), "Exception error code is not null: " + exceptionType);
        assertNotNull(sze.toString(), "Exception string is null: " + exceptionType);
        String string = sze.toString();
        assertTrue(string.indexOf(message) >= 0,
            "Exception message not found in string representation: " + exceptionType);
    }

    @ParameterizedTest
    @MethodSource("getExceptionTypes")
    public void testFullConstruct(Class<? extends SzException> exceptionType) {
        IllegalStateException cause = new IllegalStateException();
        String message = "Some Message";
        int errorCode = 105;
        SzException sze = null;
        try {
            sze = exceptionType
                .getConstructor(Integer.TYPE, String.class, Throwable.class)
                .newInstance(errorCode, message, cause);
        } catch (Exception e) {
            fail("Failed to construct exception of type: " + exceptionType, e);
        }
        
        assertEquals(message, sze.getMessage(), 
            "Exception message not as expected: " + exceptionType);
        assertEquals(cause, sze.getCause(), "Exception cause not as expected: " + exceptionType);
        assertTrue(cause == sze.getCause(), "Exception cause is not referrentially equal: " + exceptionType);
        assertEquals(errorCode, sze.getErrorCode(), 
            "Exception error code is not as expected: " + exceptionType);
        assertNotNull(sze.toString(), "Exception string is null: " + exceptionType);
        String string = sze.toString();
        assertTrue(string.indexOf(message) >= 0,
            "Exception message not found in string representation: " + exceptionType);
    }

    @ParameterizedTest
    @MethodSource("getErrorCodeParameters")
    void testGetErrorCode(Class<? extends SzException>  exceptionType, 
                          int                           errorCode) 
    {
        SzException sze = null;
        try {
            sze = exceptionType
                .getConstructor(Integer.TYPE, String.class)
                .newInstance(errorCode, "Test");
        } catch (Exception e) {
            fail("Failed to construct exception of type: " + exceptionType, e);
        }

        assertEquals(errorCode, sze.getErrorCode(), 
                    "Error code not as expected: " + exceptionType);
    }
}
