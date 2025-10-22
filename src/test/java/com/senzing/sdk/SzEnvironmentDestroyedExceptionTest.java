package com.senzing.sdk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.test.AbstractTest;
import com.senzing.text.TextUtilities;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzEnvironmentDestroyedExceptionTest extends AbstractTest {
    @Test
    public void testDefaultConstruct() {
        this.performTest(() -> {
            try {
                Exception e = new SzEnvironmentDestroyedException();
                assertNull(e.getMessage(), "Exception message was not null");
                assertNull(e.getCause(), "Exception cause was not null");
                assertNotNull(e.toString(), "Exception string is null");
                
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    @Test
    public void testMessageConstruct() {
        this.performTest(() -> {
            try {
                String message = TextUtilities.randomAlphanumericText(20);
                Exception e = new SzEnvironmentDestroyedException(message);
                assertEquals(message, e.getMessage(), "Exception message not as expected");
                assertNull(e.getCause(), "Exception cause was not null");
                assertNotNull(e.toString(), "Exception string is null");
                String string = e.toString();
                assertTrue(string.indexOf(message) >= 0,
                    "Exception message not found in string representation");
                
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    @Test
    public void testCauseConstruct() {
        this.performTest(() -> {
            try {
                Exception cause = new Exception();
                Exception e = new SzEnvironmentDestroyedException(cause);
                assertEquals(cause, e.getCause(),
                             "Exception cause not as expected");
                assertTrue(cause == e.getCause(),
                           "Exception cause is not referrentially equal");
                assertNotNull(e.toString(), "Exception string is null");
                
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    @Test
    public void testFullConstruct() {
        this.performTest(() -> {
            try {
                String message = TextUtilities.randomAlphanumericText(20);
                Exception cause = new Exception();
                Exception e = new SzEnvironmentDestroyedException(message, cause);
                assertEquals(message, e.getMessage(), "Exception message not as expected");
                assertEquals(cause, e.getCause(),
                             "Exception cause not as expected");
                assertTrue(cause == e.getCause(),
                           "Exception cause is not referrentially equal");
                assertNotNull(e.toString(), "Exception string is null");
                String string = e.toString();
                assertTrue(string.indexOf(message) >= 0,
                    "Exception message not found in string representation");
                
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }
}
