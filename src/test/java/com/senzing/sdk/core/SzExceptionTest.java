package com.senzing.sdk.core;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.senzing.sdk.SzException;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzExceptionTest {
    @ParameterizedTest
    @ValueSource(ints = { 10, 20, 30, 40 })
    @Order(10)
    void testGetErrorCode(int errorCode) {
        SzException e = new SzException(errorCode, "Test");
        assertEquals(errorCode, e.getErrorCode(), "Error code not as expected");
    }
}
