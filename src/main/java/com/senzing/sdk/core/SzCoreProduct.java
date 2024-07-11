package com.senzing.sdk.core;

import com.senzing.sdk.SzProduct;
import com.senzing.sdk.SzException;

/**
 * The package-protected implementation of {@link SzProduct} that works
 * with the {@link SzCoreEnvironment} class.
 */
public class SzCoreProduct implements SzProduct {
    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link NativeProductJni} instance.
     */
    NativeProductJni nativeApi = null;

    /**
     * Default constructor.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreEnvironment} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreProduct(SzCoreEnvironment environment) throws IllegalStateException, SzException {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new NativeProductJni();

            int returnCode = this.nativeApi.init(this.env.getInstanceName(),
                                                 this.env.getSettings(),
                                                 this.env.isVerboseLogging());

            this.env.handleReturnCode(returnCode, this.nativeApi);

            // no return value, so return null
            return null;
        });
    }

    /**
     * The package-protected function to destroy the Senzing Product SDK.
     */
    void destroy() {
        synchronized (this) {
            if (this.nativeApi == null) return;
            this.nativeApi.destroy();
            this.nativeApi = null;
        }
    }

    /**
     * Checks if this instance has been destroyed by the associated
     * {@link SzCoreEnvironment}.
     * 
     * @return <code>true</code> if this instance has been destroyed,
     *         otherwise <code>false</code>.
     */
    protected boolean isDestroyed() {
        synchronized (this) {
            return (this.nativeApi == null);
        }
    }

    @Override
    public String getLicense() throws SzException {
        return this.env.execute(() -> {
            return this.nativeApi.license();
        });
    }

    @Override
    public String getVersion() throws SzException {
        return this.env.execute(() -> {
            return this.nativeApi.version();
        });
    }
}
