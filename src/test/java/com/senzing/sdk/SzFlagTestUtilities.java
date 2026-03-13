package com.senzing.sdk;

import java.lang.reflect.Field;

import com.senzing.util.SemanticVersion;

/**
 * Provides utility methods for flag-related tests to determine
 * the {@link Since} version of flag constants via reflection.
 */
public final class SzFlagTestUtilities {
    /**
     * The default {@link SemanticVersion} assumed for flags without
     * a {@link Since} annotation.
     */
    public static final SemanticVersion DEFAULT_SINCE
        = new SemanticVersion("4.0.0");

    /**
     * Private default constructor.
     */
    private SzFlagTestUtilities() {
        // do nothing
    }

    /**
     * Gets the {@link SemanticVersion} from the {@link Since} annotation
     * on the specified {@link SzFlags} field, or {@link #DEFAULT_SINCE}
     * if no annotation is present.
     *
     * @param fieldName The name of the field on {@link SzFlags}.
     *
     * @return The {@link SemanticVersion} from the {@link Since}
     *         annotation, or {@link #DEFAULT_SINCE} if not annotated.
     */
    public static SemanticVersion getSinceVersion(String fieldName) {
        try {
            Field field = SzFlags.class.getDeclaredField(fieldName);
            Since since = field.getAnnotation(Since.class);
            return (since != null)
                ? new SemanticVersion(since.value()) : DEFAULT_SINCE;
        } catch (NoSuchFieldException e) {
            return DEFAULT_SINCE;
        }
    }

    /**
     * Gets the {@link SemanticVersion} from the {@link Since} annotation
     * on the specified {@link SzFlag} field (enum constant or static
     * {@link java.util.Set} field), or {@link #DEFAULT_SINCE} if no
     * annotation is present.
     *
     * @param name The name of the field on {@link SzFlag}.
     *
     * @return The {@link SemanticVersion} from the {@link Since}
     *         annotation, or {@link #DEFAULT_SINCE} if not annotated.
     */
    public static SemanticVersion getSinceVersionForSzFlag(String name) {
        try {
            Field field = SzFlag.class.getField(name);
            Since since = field.getAnnotation(Since.class);
            return (since != null)
                ? new SemanticVersion(since.value()) : DEFAULT_SINCE;
        } catch (NoSuchFieldException e) {
            return DEFAULT_SINCE;
        }
    }
}
