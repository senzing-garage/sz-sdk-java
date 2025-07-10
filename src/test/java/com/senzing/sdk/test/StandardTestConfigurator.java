package com.senzing.sdk.test;

import com.senzing.sdk.SzEnvironment;
import com.senzing.sdk.SzConfig;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzException;

/**
 * Provides an implementation of {@link TestDataLoader} that
 * uses an {@link SzEnvironment} to load the data.
 */
public class StandardTestConfigurator implements TestConfigurator {
    /**
     * The {@link SzEnvironment} to use.
     */
    private SzEnvironment env;

    /**
     * Constructs with the specified {@link SzEnvironment}.
     * 
     * @param env The {@link SzEnvironment} to use.
     */
    public StandardTestConfigurator(SzEnvironment env) {
        this.env = env;
    }

    @Override
    public String createConfig(String... dataSourceCodes) 
    {
        try {
            SzConfigManager configMgr = this.env.getConfigManager();
            SzConfig config = configMgr.createConfig();
            for (String dataSourceCode : dataSourceCodes) {
                config.addDataSource(dataSourceCode);
            }
            return config.export();

        } catch (SzException e) {
            throw new RuntimeException(e);
        }
    }
}
