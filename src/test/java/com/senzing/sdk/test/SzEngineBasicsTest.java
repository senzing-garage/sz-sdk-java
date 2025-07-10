package com.senzing.sdk.test;

import org.junit.jupiter.api.Test;
import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for basic {@link SzEngine} functionality.
 */
public interface SzEngineBasicsTest extends SdkTest {
    /**
     * Gets the {@link SzEngine} to use for this test.
     * 
     * @return The {@link SzEngine} to use for this test.
     * 
     * @throws SzException If a failure occurs.
     */
    SzEngine getEngine() throws SzException;

    /**
     * Tests priming the engine.
     */
    @Test
    default void testPrimeEngine() {
        this.performTest(() -> {
            try {
                SzEngine engine = this.getEngine();

                engine.primeEngine();

            } catch (Exception e) {
                fail("Priming engine failed with an exception", e);
            }
        });
    }
}
