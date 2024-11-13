package com.senzing.sdk;

import javax.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.senzing.util.JsonUtilities;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class UtilitiesTest {
    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 2L, 20L, 40L, 80L, 160L, 3200L, 64000L, 128345789L})
    public void testHexFormat(long value) {
        String text = Utilities.hexFormat(value);
        String[] tokens = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String token: tokens) {
            sb.append(token);
        }
        String formatted = sb.toString();
        long reversedValue = Long.parseLong(formatted, 16);
        assertEquals(value, reversedValue, "Value was not as expected");
    }

    @Test
    public void testJsonEscapeNull() {
        String text = Utilities.jsonEscape(null);
        assertEquals("null", text, "The JSON-escaped null value was not \"null\"");
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"Hello", "Hello,\nWorld", "\f\b\\\tHey!\r\n", "Bell \u0007!"})
    public void testJsonEscape(String value) {
        String text = Utilities.jsonEscape(value);
        String objText = "{\"value\": " + text + "}";
        JsonObject obj = JsonUtilities.parseJsonObject(objText);

        String parsedValue = obj.getString("value");
        assertEquals(value, parsedValue, "The reverse parsed value is not as expected");
    }

}
