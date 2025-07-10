package com.senzing.sdk.core;

import com.senzing.sdk.test.TestConfigurator;

import static com.senzing.sdk.core.LoggingUtilities.formatError;

/**
 * Implements the {@link TestConfigurator} to use the {@link NativeConfig}
 * interface.
 */
public class NativeTestConfigurator implements TestConfigurator {
    /**
     * The {@link NativeConfig} to use.
     */
    private NativeConfig nativeConfig = null;

    /**
     * Constructs with the specified {@link NativeConfig}.
     * 
     * @param nativeConfig The {@link NativeConfig} to use.
     */
    public NativeTestConfigurator(NativeConfig nativeConfig) {
        this.nativeConfig = nativeConfig;
    }

    @Override
    public String createConfig(String... dataSourceCodes) {
        Result<Long> result = new Result<>();
        int returnCode = this.nativeConfig.create(result);
        if (returnCode != 0) {
            throw new RuntimeException(
                formatError("NativeConfig.create", nativeConfig));
        }
        long configHandle = result.getValue();
        try {
            StringBuffer sb = new StringBuffer();
            for (String dataSourceCode : dataSourceCodes) {
                sb.delete(0, sb.length());

                String inputJson = "{\"DSRC_CODE\":\"" + dataSourceCode + "\"}";

                returnCode = this.nativeConfig.addDataSource(
                    configHandle, inputJson, sb);

                if (returnCode != 0) {
                    throw new RuntimeException(
                        formatError("NativeConfig.create", nativeConfig));
                }
            }

            // clear the buffer
            sb.delete(0, sb.length());

            // export the config JSON
            this.nativeConfig.save(configHandle, sb);

            // return the resulting config JSON
            return sb.toString();

        } finally {
            this.nativeConfig.close(configHandle);
        }
    }
}
