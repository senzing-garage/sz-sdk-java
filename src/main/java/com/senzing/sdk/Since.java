package com.senzing.sdk;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the Senzing SDK version in which the annotated element was
 * first introduced.  This is used to identify flags and constants that
 * may not be present in the installed Senzing runtime if the installed
 * version predates the annotated version.
 *
 * @since 4.3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface Since {
    /**
     * The version string (e.g., {@code "4.3.0"}) identifying when the
     * annotated element was introduced.
     *
     * @return The version string.
     */
    String value();
}
