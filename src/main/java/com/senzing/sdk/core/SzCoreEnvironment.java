package com.senzing.sdk.core;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

import com.senzing.sdk.SzEnvironment;
import com.senzing.sdk.SzException;
import com.senzing.sdk.SzProduct;
import com.senzing.sdk.SzConfigManager;
import com.senzing.sdk.SzEngine;
import com.senzing.sdk.SzDiagnostic;

import static com.senzing.sdk.core.SzCoreUtilities.createSzException;

/**
 * Provides the core implementation of {@link SzEnvironment} that directly
 * initializes the Senzing SDK modules and provides management of the Senzing
 * environment in this process.
 * 
 * <p><b>Usage:</b> 
 * {@snippet class="com.senzing.sdk.SzProductDemo" region="SzEnvironment"}
 * </p>
 * 
 * {@see SzEnvironment}.
 */
public class SzCoreEnvironment implements SzEnvironment {
    /**
     * The default instance name to use for the Senzing initialization.  The
     * value is <code>"{@value}</code>.  An explicit value can be
     * provided via {@link Builder#instanceName(String)} during initialization.
     * 
     * @see #newBuilder()
     * @see Builder#instanceName(String)
     */
    public static final String DEFAULT_INSTANCE_NAME = "Senzing Instance";

    /**
     * The default "bootstrap" settings with which to initialize the {@link
     * SzCoreEnvironment} when an explicit settings value has not been provided
     * via {@link Builder#settings(String)}.  If this is used it will initialize
     * Senzing when installed in its default path and provide full access access
     * to only the {@link SzProduct} interface as well as limited access to the
     * {@link SzConfigManager} interface (i.e.: for functionality <b>not</b>
     * dealing with registered configuration ID's).  The value of this constant
     * is <code>"{ }"</code>.
     * <p>
     * <b>NOTE:</b> Using these settings is only useful for accessing the
     * the full functionality of the {@link SzProduct} interface and limited
     * functionality of the {@link SzConfigManager} interface since {@link
     * SzEngine} and {@link SzDiagnostic} require database access to connect
     * to the Senzing repository.  Further, some {@link SzConfigManager}
     * functionality (particularly any functionality that works with registered
     * configurations that have configuration ID's) also requires database access
     * to connect to the Senzing repository.
     * 
     * <p>
     * The value of this constant is <code>{@value}</code>.
     * 
     * @see #newBuilder()
     * @see Builder#settings(String)
     */
    public static final String DEFAULT_SETTINGS = "{ }";

    /**
     * The number of milliseconds to delay (if not notified) until checking
     * if we are ready to destroy.
     */
    private static final long DESTROY_DELAY = 5000L;

    /**
     * Internal object for class-wide synchronized locking.
     */
    private static final Object CLASS_MONITOR = new Object();

    /**
     * Enumerates the possible states for an instance of {@link SzCoreEnvironment}.
     */
    private enum State {
        /**
         * If an {@link SzCoreEnvironment} instance is in the "active" state then it
         * is initialized and ready to use.  Only one instance of {@link 
         * SzCoreEnvironment} can exist in the {@link #ACTIVE} or {@link #DESTROYING}
         * state because Senzing environment cannot be initialized heterogeneously
         * within a single process.
         * 
         * @see SenzingSdk#getActiveInstance()
         */
        ACTIVE,

        /**
         * An instance {@link SzCoreEnvironment} moves to the "destroying" state when
         * the {@link #destroy()} method has been called but has not yet completed any
         * Senzing operations that are already in-progress.  In this state, the
         * {@link SzCoreEnvironment} will fast-fail any newly attempted operations with
         * an {@link IllegalStateException}, but will complete those Senzing operations
         * that were initiated before {@link #destroy()} was called.
         */
        DESTROYING,

        /**
         * An instance of {@link SzCoreEnvironment} moves to the "destroyed" state when
         * the {@link #destroy()} method has completed and there are no more Senzing
         * operations that are in-progress.  Once an {@link #ACTIVE} instance moves to
         * {@link #DESTROYED} then a new active instance can be created and initialized.
         */
        DESTROYED;
    }

    /**
     * Creates a new instance of {@link Builder} for setting up an instance
     * of {@link SzCoreEnvironment}.  Keep in mind that while multiple {@link Builder}
     * instances can exists, <b>only one active instance</b> of {@link SzCoreEnvironment}
     * can exist at time.  An active instance is one that has not yet been
     * destroyed.
     * 
     * @return The {@link Builder} for configuring and initializing the
     *         {@link SzCoreEnvironment}.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * The currently instance of the {@link SzCoreEnvironment}.
     */
    private static SzCoreEnvironment currentInstance = null;

    /** 
     * Gets the current active instance of {@link SzCoreEnvironment}.  An active instance
     * is is one that has been constructed and has not yet been destroyed.  There
     * can be at most one active instance.  If no active instance exists then 
     * <code>null</code> is returned.
     * 
     * @return The current active instance of {@link SzCoreEnvironment}, or 
     *         <code>null</code> if there is no active instance.
     */
    public static SzCoreEnvironment getActiveInstance() {
        synchronized (CLASS_MONITOR) {
            if (currentInstance == null) {
                return null;
            }
            synchronized (currentInstance.monitor) {
                State state = currentInstance.state;
                switch (state) {
                    case DESTROYING:
                        // wait until destroyed and fall through
                        waitUntilDestroyed(currentInstance);
                    case DESTROYED:
                        // if still set but destroyed, clear it and fall through
                        currentInstance = null;
                    case ACTIVE:
                        // return the current instance
                        return currentInstance;
                    default:
                        throw new IllegalStateException(
                            "Unrecognized SzCoreEnvironment state: " + state);
                }
            }
        }
    }

    /**
     * The instance name to initialize the API's with.
     */
    private String instanceName = null;

    /**
     * The settings to initialize the API's with.
     */
    private String settings = null;

    /**
     * The flag indicating if verbose logging is enabled.
     */
    private boolean verboseLogging = false;

    /**
     * The explicit configuration ID to initialize with or <code>null</code> if
     * using the default configuration.
     */
    private Long configId = null;

    /**
     * The {@link SzCoreProduct} singleton instance to use.
     */
    private SzCoreProduct coreProduct = null;

    /**
     * The {@link SzCoreEngine} singleton instance to use.
     */
    private SzCoreEngine coreEngine = null;

    /**
     * The {@link SzCoreConfigManager} singleton instance to use.
     */
    private SzCoreConfigManager coreConfigMgr = null;

    /**
     * The {@link SzCoreDiagnostic} singleton instance to use.
     */
    private SzCoreDiagnostic coreDiagnostic = null;

    /**
     * The {@link State} for this instance.
     */
    private State state = null;

    /** 
     * The number of currently executing operations.
     */
    private int executingCount = 0;

    /**
     * The {@link ReadWriteLock} for this instance.
     */
    private final ReadWriteLock readWriteLock;

    /**
     * Internal object for instance-wide synchronized locking.
     */
    private final Object monitor = new Object();

    /**
     * Protected constructor used by the {@link Builder} to construct the
     * instance.
     *  
     * @param initializer The {@link Initializer} with which to construct
     *                    (typically an instance of {@link AbstractBuilder}).
     */
    protected SzCoreEnvironment(Initializer initializer) 
    {
        // set the fields
        this.readWriteLock  = new ReentrantReadWriteLock(true);
        this.instanceName   = initializer.getInstanceName();
        this.settings       = initializer.getSettings();
        this.verboseLogging = initializer.isVerboseLogging();
        this.configId       = initializer.getConfigId();

        synchronized (CLASS_MONITOR) {
            SzCoreEnvironment activeEnvironment = getActiveInstance();
            if (activeEnvironment != null) {
                throw new IllegalStateException(
                    "At most one active instance of SzCoreEnvironment can be "
                    + "initialized.  Another instance was previously initialized "
                    + "and has not yet been destroyed.");
            }

            // set the state
            this.state = State.ACTIVE;

            // set the current instance
            currentInstance = this;
        }
    }

    /**
     * Waits until the specified {@link SzCoreEnvironment} instance has been destroyed.
     * Use this when obtaining an instance of {@link SzCoreEnvironment} in the {@link 
     * State#DESTROYING} and you want to wait until it is fully destroyed.
     * 
     * @param environment The non-null {@link SzCoreEnvironment} instance to wait on.
     * 
     * @throws NullPointerException If the specified parameter is <code>null</code>.
     */
    private static void waitUntilDestroyed(SzCoreEnvironment environment) 
    {
        Objects.requireNonNull(environment, "The specified instance cannot be null");
        synchronized (environment.monitor) {
            while (environment.state != State.DESTROYED) {
                try {
                    environment.monitor.wait(DESTROY_DELAY);
                } catch (InterruptedException ignore) {
                    // ignore the exception
                }
            }
        }
    }

    /**
     * Gets the associated Senzing instance name for initialization.
     * 
     * @return The associated Senzing instance name for initialization.
     */
    String getInstanceName() {
        return this.instanceName;
    }

    /**
     * Gets the associated Senzing settings for initialization.
     * 
     * @return The associated Senzing settings for initialization.
     */
    String getSettings() {
        return this.settings;
    }

    /**
     * Gets the verbose logging setting to indicating if verbose logging
     * should be enabled (<code>true</code>) or disabled (<code>false</code>).
     * 
     * @return <code>true</code> if verbose logging should be enabled, otherwise
     *         <code>false</code>.
     */
    boolean isVerboseLogging() {
        return this.verboseLogging;
    }   

    /**
     * Gets the explicit configuration ID with which to initialize the Senzing 
     * environment.  This returns <code>null</code> if the default
     * configuration ID configured in the repository should be used.
     * 
     * @return The explicit configuration ID with which to initialize the Senzing
     *         environment, or <code>null</code> if the default configuration ID
     *         configured in the repository should be used.
     */
    Long getConfigId() {
        return this.configId;
    }
    
    /**
     * Executes the specified {@link Callable} task via {@link #doExecute(Callable)} 
     * after ensuring that this instance is not {@linkplain #destroy() destroyed} 
     * and will not be {@linkplain #destroy() destroyed} before the task's completion.
     * 
     * <p>
     * This is used by the core implementations of {@link SzEngine}, {@link SzProduct}, 
     * {@link SzConfigManager}, {@link com.senzing.sdk.SzConfig} and 
     * {@link SzDiagnostic} to execute their functionality and ensure the environment
     * is stable during execution.
     * </p>
     * 
     * <p>
     * If successful, this will return the result of the {@link Callable} task.
     * If not successful, this will throw any exception produced by the
     * {@link Callable} task, wrapping it in an {@link SzException} if it is 
     * a checked exception that is not of type {@link SzException}.
     * </p>
     * 
     * @param <T> The return type.
     * @param task The {@link Callable} task to execute.
     * @return The result from the {@link Callable} task.
     * @throws SzException If the {@link Callable} task triggers a failure.
     * @throws IllegalStateException If this {@link SzCoreEnvironment} instance has
     *                               already been destroyed.
     */
    protected <T> T execute(Callable<T> task)
        throws SzException, IllegalStateException
    {
        Lock lock = null;
        try {
            // acquire a write lock while checking if active
            lock = this.acquireReadLock();
            synchronized (this.monitor) {
                if (this.state != State.ACTIVE) {
                    throw new IllegalStateException(
                        "SzEnvironment has been destroyed");
                }

                // increment the executing count
                this.executingCount++;
            }
        
            return this.doExecute(task);

        } catch (SzException | RuntimeException e) {
            throw e; 

        } catch (Exception e) {
            throw new SzException(e);

        } finally {
            synchronized (this.monitor) {
                this.executingCount--;
                this.monitor.notifyAll();
            }
            lock = releaseLock(lock);
        }
    }

    /**
     * Called by {@link #execute(Callable)} after ensuring that this
     * instance is not {@linkplain #destroy() destroyed} and ensuring
     * any calls to {@link #destroy()} will block until after this 
     * method's invocation is completed.
     * 
     * <p>
     * If successful, this will return the result of the {@link Callable} task.
     * If not successful, this will throw any exception produced by the
     * {@link Callable} task.
     * </p>
     * 
     * @param <T> The return type.
     * @param task The {@link Callable} task to execute.
     * 
     * @return The result from the specified {@link Callable} task.
     * 
     * @throws Exception If a failure occurs.
     */
    protected <T> T doExecute(Callable<T> task) 
        throws Exception
    {
        return task.call();
    }

    /**
     * Gets the number of currently executing operations.
     * 
     * @return The number of currently executing operations.
     */
    int getExecutingCount() {
        synchronized (this.monitor) {
            return this.executingCount;
        }
    }

    /**
     * Ensures this instance is still active and if not will throw 
     * an {@link IllegalStateException}.
     *
     * @throws IllegalStateException If this instance is not active.
     */
    void ensureActive() throws IllegalStateException {
        synchronized (this.monitor) {
            if (this.state != State.ACTIVE) {
                throw new IllegalStateException(
                    "This instance has already been destroyed.");
            }
        }
    }

    /**
     * Handles the Senzing native return code and coverts it to the proper
     * {@link SzException} if it is not zero (0).
     * 
     * @param returnCode The return code to handle.
     * @param nativeApi The {@link NativeApi} implementation that produced the
     *                  return code on this current thread.
     */
    void handleReturnCode(int returnCode, NativeApi nativeApi)
        throws SzException
    {
        if (returnCode == 0) {
            return;
        }

        // get the error code and message
        int     errorCode   = nativeApi.getLastExceptionCode();
        String  message     = nativeApi.getLastException();
        nativeApi.clearLastException();

        throw createSzException(errorCode, message);
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * <p>
     * Implemented to return an instance of {@link SzConfigManager} that 
     * will execute all of its functionality via {@link #execute(Callable)}.
     * If you need to change the functionality of the returned instance 
     * consider using {@link java.lang.reflect.Proxy} and 
     * {@link java.lang.reflect.InvocationHandler} to proxy the returned
     * instance.
     * </p>
     */
    @Override
    public SzConfigManager getConfigManager()
       throws IllegalStateException, SzException 
    {
        synchronized (this.monitor) {
            this.ensureActive();
            if (this.coreConfigMgr == null) {
                this.coreConfigMgr = new SzCoreConfigManager(this);
            }

            // return the configured instance
            return this.coreConfigMgr;
        }
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * <p>
     * Implemented to return an instance of {@link SzDiagnostic} that 
     * will execute all of its functionality via {@link #execute(Callable)}.
     * If you need to change the functionality of the returned instance 
     * consider using {@link java.lang.reflect.Proxy} and 
     * {@link java.lang.reflect.InvocationHandler} to proxy the returned
     * instance.
     * </p>
     */
    @Override
    public SzDiagnostic getDiagnostic() 
       throws IllegalStateException, SzException 
    {
        synchronized (this.monitor) {
            this.ensureActive();
            if (this.coreDiagnostic == null) {
                this.coreDiagnostic = new SzCoreDiagnostic(this);
            }
            // return the configured instance
            return this.coreDiagnostic;
        }
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * <p>
     * Implemented to return an instance of {@link SzEngine} that 
     * will execute all of its functionality via {@link #execute(Callable)}.
     * If you need to change the functionality of the returned instance 
     * consider using {@link java.lang.reflect.Proxy} and 
     * {@link java.lang.reflect.InvocationHandler} to proxy the returned
     * instance.
     * </p>
     */
    @Override
    public SzEngine getEngine() 
       throws IllegalStateException, SzException 
    {
        synchronized (this.monitor) {
            this.ensureActive();
            if (this.coreEngine == null) {
                this.coreEngine = new SzCoreEngine(this);
            }
            // return the configured instance
            return this.coreEngine;
        }
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * <p>
     * Implemented to return an instance of {@link SzProduct} that 
     * will execute all of its functionality via {@link #execute(Callable)}.
     * If you need to change the functionality of the returned instance 
     * consider using {@link java.lang.reflect.Proxy} and 
     * {@link java.lang.reflect.InvocationHandler} to proxy the returned
     * instance.
     * </p>
     */
    @Override
    public SzProduct getProduct() 
       throws IllegalStateException, SzException 
    {
        synchronized (this.monitor) {
            this.ensureActive();
            if (this.coreProduct == null) {
                this.coreProduct = new SzCoreProduct(this);
            }
            // return the configured instance
            return this.coreProduct;
        }
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * <p>
     * Implemented to block until all concurrent in-flight operations that are
     * executing via the {@link #execute(Callable)} are complete before 
     * destroying this instance.  Once destroyed, a new instance of this class
     * can be created, but only one instance can be {@linkplain 
     * #getActiveInstance() active} concurrently in the same process.
     * </p>
     * 
     * <p>
     * This method will invalidate any {@link com.senzing.sdk.SzEngine}, {@link 
     * com.senzing.sdk.SzProduct}, {@link com.senzing.sdk.SzDiagnostic}, {@link 
     * com.senzing.sdk.SzConfigManager} or {@link com.senzing.sdk.SzConfig} that
     * was provided through this instance.
     * </p>
     */
    @Override
    public void destroy() {
        Lock lock = null;
        try {
            synchronized (this.monitor) {
                // check if this has already been called
                if (this.state != State.ACTIVE) {
                    return;
                }

                // set the flag for destroying
                this.state = State.DESTROYING;
                this.monitor.notifyAll();
            }

            // acquire an exclusive lock for destroying to ensure
            // all executing tasks have completed
            lock = this.acquireWriteLock();

            // ensure completion of in-flight executions
            int exeCount = this.getExecutingCount();
            if (exeCount > 0) {
                throw new IllegalStateException(
                    "Acquired write lock for destroying environment while tasks "
                    + "still executing: " + exeCount);
            }

            // once we get here we can really shut things down
            if (this.coreEngine != null) {
                this.coreEngine.destroy();
                this.coreEngine = null;
            }
            if (this.coreDiagnostic != null) {
                this.coreDiagnostic.destroy();
                this.coreDiagnostic = null;
            }
            if (this.coreConfigMgr != null) {
                this.coreConfigMgr.destroy();
                this.coreConfigMgr = null;
            }
            if (this.coreProduct != null) {
                this.coreProduct.destroy();
                this.coreProduct = null;
            }

            // set the state
            synchronized (this.monitor) {
                this.state = State.DESTROYED;
                this.monitor.notifyAll();
            }
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    /**
     * Implemented from {@link SzEnvironment#isDestroyed()}.
     * 
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public boolean isDestroyed() {
        synchronized (this.monitor) {
            return this.state != State.ACTIVE;
        }
    }

    /**
     * Implemented from {@link SzEnvironment#getActiveConfigId()}.
     * 
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public long getActiveConfigId()
        throws IllegalStateException, SzException
    {
        Lock lock = null;
        try {
            // get a read lock to ensure we remain active while
            // executing the operation
            lock = this.acquireReadLock();
            
            // ensure we have initialized the engine or diagnostic
            synchronized (this.monitor) {
                this.ensureActive();

                // check if the core engine has been initialized
                if (this.coreEngine == null) {
                    // initialize the engine if not yet initialized
                    this.getEngine();
                }
            }

            // get the active config ID from the native engine
            long configId = this.execute(() -> {
                Result<Long> result = new Result<>();
                NativeEngine nativeEngine = this.coreEngine.getNativeApi(); 
                int returnCode = nativeEngine.getActiveConfigID(result);
                this.handleReturnCode(returnCode, nativeEngine);
                return result.getValue();
            });

            // return the config ID
            return configId;

        } finally {
            lock = this.releaseLock(lock);
        }
    }

    /**
     * Implemented from {@link SzEnvironment#reinitialize(long)}.
     * 
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public void reinitialize(long configId)
        throws IllegalStateException, SzException
    {
        Lock lock = null;
        try {
            // get an exclusive write lock
            lock = this.acquireWriteLock();
            
            synchronized (this.monitor) {
                // set the config ID for future native initializations
                this.configId = configId;

                // check if we have already initialized the engine or diagnostic
                if (this.coreEngine != null) {
                    // engine already initialized so we need to reinitialize
                    this.execute(() -> {
                        int returnCode = this.coreEngine.getNativeApi().reinit(configId);
                        this.handleReturnCode(returnCode, this.coreEngine.getNativeApi());
                        return null;
                    });

                } else if (this.coreDiagnostic != null) {
                    // diagnostic already initialized so we need to reinitialize
                    // NOTE: we do not need to do this if we reinitialized the
                    // engine since the configuration ID is globally set
                    this.execute(() -> {
                        int returnCode = this.coreDiagnostic.getNativeApi().reinit(configId);
                        this.handleReturnCode(returnCode, this.coreDiagnostic.getNativeApi());
                        return null;
                    });
                } else {
                    // force initialization to ensure the configuration ID is valid
                    this.getEngine();
                }
            }
        } finally {
            lock = this.releaseLock(lock);
        }
    }

    /**
     * Provides an interface for initializing an instance of
     * {@link SzCoreEnvironment}.
     * 
     * <p>
     * This interface is not needed to use {@link SzCoreEnvironment}.
     * It is only needed if you are extending {@link SzCoreEnvironment}.
     * </p>
     * 
     * <p>
     * This is provided for derived classes of {@link SzCoreEnvironment}
     * to initialize their super class and is typically implemented by
     * extending {@link AbstractBuilder} in creating a derived
     * builder implementation.
     * </p>
     */
    public interface Initializer {
        /**
         * Gets the Senzing settings with which to initialize
         * the {@link SzCoreEnvironment}.
         * 
         * @return The Senzing settings with which to configure 
         *         the {@link SzCoreEnvironment}.
         */
        String getSettings();

        /**
         * Gets the Senzing instance name with which to initialize 
         * the {@link SzCoreEnvironment}.
         * 
         * @return The Senzing instance name with which to initialize
         *         the {@link SzCoreEnvironment}.
         */
        String getInstanceName();

        /**
         * Checks if configuring the {@link SzCoreEnvironment} with verbose logging.
         * 
         * @return <code>true</code> if verbose logging will be enabled, 
         *         otherwise <code>false</code>.
         */
        boolean isVerboseLogging();

        /**
         * Gets the explicit configuration ID (if any) with which to initialize
         * the {@link SzCoreEnvironment}.  This returns <code>null</code> if no
         * explicit configuration ID has been provided and the default
         * configuration ID from the Senzing repository should be used.
         * 
         * @return The explicit configuration ID with which to initialize the 
         *         {@link SzCoreEnvironment}, or <code>null</code> if none and
         *         the default configuration ID from the Senzing repository
         *         should be used.
         */
        Long getConfigId();
    }
    
    /**
     * Provides a base class for builder implementations of
     * {@link SzCoreEnvironment} and its derived classes.
     * 
     * <p>
     * This class is not used in the usage of {@link SzCoreEnvironment}.
     * It is only needed if you are extending {@link SzCoreEnvironment}.
     * </p>
     * 
     * <p>
     * This class allows the derived builder to return references to its
     * own environment class and to its own builder type rather than base 
     * classes.  When extending {@link SzCoreEnvironment} you should
     * provide an implementation of this class that is specific to your
     * derived class.
     * </p>
     * 
     * @param <E> The {@link SzCoreEnvironment}-derived class built by instances
     *            of this class.
     * @param <B> The {@link AbstractBuilder}-derived class of the implementation.
     */
    public static abstract class AbstractBuilder<
        E extends SzCoreEnvironment, B extends AbstractBuilder<E,B>> 
        implements Initializer
    {
        /**
         * The settings for the builder which default to {@link 
         * SzCoreEnvironment#DEFAULT_SETTINGS}.
         */
        private String settings = DEFAULT_SETTINGS;

        /**
         * The instance name for the builder which defaults to {@link 
         * SzCoreEnvironment#DEFAULT_INSTANCE_NAME}.
         */
        private String instanceName = DEFAULT_INSTANCE_NAME;

        /**
         * The verbose logging setting for the builder which defaults
         * to <code>false</code>.
         */
        private boolean verboseLogging = false;

        /**
         * The config ID for the builder.  If not provided explicitly then
         * the configured default configuration in the Senzing repository
         * is used.
         */
        private Long configId = null;

        /**
         * Default constructor.
         */
        protected AbstractBuilder() {
            this.settings       = DEFAULT_SETTINGS;
            this.instanceName   = DEFAULT_INSTANCE_NAME;
            this.verboseLogging = false;
            this.configId       = null;
        }

        /**
         * Provides the Senzing settings to initialize the {@link SzCoreEnvironment}.
         * If this is set to <code>null</code> or empty-string then {@link
         * SzCoreEnvironment#DEFAULT_SETTINGS} will be used to provide limited 
         * functionality.
         * 
         * @param settings The Senzing settings, or <code>null</code> or 
         *                 empty-string to restore the default value.
         * 
         * @return A reference to this instance.
         * 
         * @see SzCoreEnvironment#DEFAULT_SETTINGS                
         */
        @SuppressWarnings("unchecked")
        public B settings(String settings) {
            if (settings != null && settings.trim().length() == 0) {
                settings = null;
            }

            this.settings = (settings == null)
                ? DEFAULT_SETTINGS : settings.trim();

            return ((B) this);
        }

        /**
         * Gets the Senzing settings with which to initialize
         * the {@link SzCoreEnvironment}.
         * 
         * @return The Senzing settings with which to configure 
         *         the {@link SzCoreEnvironment}.
         */
        public String getSettings() {
            return this.settings;
        }

        /**
         * Provides the Senzing instance name to initialize the 
         * {@link SzCoreEnvironment}.  Call this method to override the
         * default value of {@link SzCoreEnvironment#DEFAULT_INSTANCE_NAME}.
         * 
         * @param instanceName The instance name to initialize the 
         *                     {@link SzCoreEnvironment}, or <code>null</code>
         *                     or empty-string to restore the default.
         * 
         * @return A reference to this instance.
         * 
         * @see SzCoreEnvironment#DEFAULT_INSTANCE_NAME
         */
        @SuppressWarnings("unchecked")
        public B instanceName(String instanceName) {
            if (instanceName != null && instanceName.trim().length() == 0) {
                instanceName = null;
            }

            this.instanceName = (instanceName == null) 
                ? DEFAULT_INSTANCE_NAME : instanceName.trim();

            return ((B) this);
        }

        /**
         * Gets the Senzing instance name with which to initialize 
         * the {@link SzCoreEnvironment}.
         * 
         * @return The Senzing instance name with which to initialize
         *         the {@link SzCoreEnvironment}.
         */
        public String getInstanceName() {
            return this.instanceName;
        }

        /**
         * Sets the verbose logging flag for initializing the 
         * {@link SzCoreEnvironment}.  Call this method to explicitly set
         * the value.  If not called, the default value is <code>false</code>.
         * 
         * @param verboseLogging <code>true</code> if verbose logging should be
         *                       enabled, otherwise <code>false</code>.
         * 
         * @return A reference to this instance.
         */
        @SuppressWarnings("unchecked")
        public B verboseLogging(boolean verboseLogging) {
            this.verboseLogging = verboseLogging;
            return ((B) this);
        }

        /**
         * Checks if configuring the {@link SzCoreEnvironment} with verbose logging.
         * 
         * @return <code>true</code> if verbose logging will be enabled, 
         *         otherwise <code>false</code>.
         */
        public boolean isVerboseLogging() {
            return this.verboseLogging;
        }

        /**
         * Sets the explicit configuration ID to use to initialize the {@link
         * SzCoreEnvironment}.  If not specified then the default configuration
         * ID obtained from the Senzing repository is used.
         * 
         * @param configId The explicit configuration ID to use to initialize the
         *                 {@link SzCoreEnvironment}, or <code>null</code> if the
         *                 default configuration ID from the Senzing repository
         *                 should be used.
         * 
         * @return A reference to this instance.
         */
        @SuppressWarnings("unchecked")
        public B configId(Long configId) {
            this.configId = configId;
            return ((B) this);
        }

        /**
         * Gets the explicit configuration ID (if any) with which to initialize
         * the {@link SzCoreEnvironment}.  This returns <code>null</code> if no
         * explicit configuration ID has been provided and the default
         * configuration ID from the Senzing repository should be used.
         * 
         * @return The explicit configuration ID with which to initialize the 
         *         {@link SzCoreEnvironment}, or <code>null</code> if none and
         *         the default configuration ID from the Senzing repository
         *         should be used.
         */
        public Long getConfigId() {
            return this.configId;
        }

        /**
         * Implement this method to create a new {@link SzCoreEnvironment}
         * instance of type <code>E</code> based on this builder instance.
         * This method will throw an {@link IllegalStateException} if another
         * active {@link SzCoreEnvironment} instance exists since only one
         * active instance can exist within a process at any given time.  An
         * active instance is one that has been constructed, but has not yet
         * been destroyed.
         * 
         * @return The newly created {@link SzCoreEnvironment} instance
         *         of type <code>E</code>.
         * 
         * @throws IllegalStateException If another active {@link SzCoreEnvironment}
         *                               instance exists when this method is
         *                               invoked.
         */
        public abstract E build() throws IllegalStateException;
    }

    /**
     * The builder class for creating an instance of {@link SzCoreEnvironment}.
     */
    public static class Builder
        extends AbstractBuilder<SzCoreEnvironment, Builder> 
    {
        /**
         * Default constructor.
         */
        protected Builder() {
            super();
        }

        /**
         * Creates a new {@link SzCoreEnvironment} instance based on this
         * {@link Builder} instance.  This method will throw an {@link 
         * IllegalStateException} if another active {@link SzCoreEnvironment}
         * instance exists since only one active instance can exist within a
         * process at any given time.  An active instance is one that has
         * been constructed, but has not yet been destroyed.
         * 
         * @return The newly created {@link SzCoreEnvironment} instance.
         * 
         * @throws IllegalStateException If another active {@link SzCoreEnvironment}
         *                               instance exists when this method is
         *                               invoked.
         */
        @Override
        public SzCoreEnvironment build() throws IllegalStateException
        {
            return new SzCoreEnvironment(this);
        }
    }

    /**
     * Acquires an exclusive write lock from this instance's
     * {@link ReentrantReadWriteLock}.
     * 
     * @return The {@link Lock} that was acquired.
     */
    private Lock acquireWriteLock() {
        Lock lock = this.readWriteLock.writeLock();
        lock.lock();
        return lock;
    }

    /**
     * Acquires a shared read lock from this instance's 
     * {@link ReentrantReadWriteLock}.
     * 
     * @return The {@link Lock} that was acquired.
     */
    private Lock acquireReadLock() {
        Lock lock = this.readWriteLock.readLock();
        lock.lock();
        return lock;
    }

    /**
     * Releases the specified {@link Lock} if not <code>null</code>.
     * 
     * @param lock The {@link Lock} to be released.
     * 
     * @return Always returns <code>null</code>.
     */
    private Lock releaseLock(Lock lock) {
        if (lock != null) {
            lock.unlock();
        }
        return null;
    }
}
