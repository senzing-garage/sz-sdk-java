package com.senzing.sdk.test;

/**
 * Provides an interface to a test configurator.
 */
public interface TestConfigurator {
    /**
     * Creates a JSON {@link String} configuration from the template
     * configuraiton with the specified zero or more data source codes.
     * 
     * @param datraSourceCodes The zero or more data source codes.
     * 
     * @return The configuration JSON {@link String}.
     */
    String createConfig(String... dataSourceCodes);
}
