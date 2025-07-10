package com.senzing.sdk.test;

/**
 * 
 */
public abstract class AbstractTest implements SdkTest {
    /**
     * The number of tests that failed for this instance.
     */
    private int failureCount = 0;

    /**
     * The number of tests that succeeded for this instance.
     */
    private int successCount = 0;

    /**
     * The time of the last progress log.
     */
    private long progressLogTimestamp = -1L;

    /**
     * Protected default constructor.
     */
    protected AbstractTest() {
        // do nothing
    }

    /**
     * Signals the beginning of the current test suite.
     *
     * @return <tt>true</tt> if replaying previous results and <tt>false</tt>
     *         if using the live API.
     */
    protected void beginTests() {
        // do nothing for now
    }

    /**
     * Signals the end of the current test suite.
     */
    protected void endTests() {
        this.conditionallyLogCounts(true);
    }

    /**
     * Increments the failure count and returns the new failure count.
     * 
     * @return The new failure count.
     */
    @Override
    public int incrementFailureCount() {
        this.failureCount++;
        this.conditionallyLogCounts(false);
        return this.failureCount;
    }

    /**
     * Increments the success count and returns the new success count.
     * 
     * @return The new success count.
     */
    @Override
    public int incrementSuccessCount() {
        this.successCount++;
        this.conditionallyLogCounts(false);
        return this.successCount;
    }

    /**
     * Conditionally logs the progress of the tests.
     *
     * @param complete <tt>true</tt> if tests are complete for this class,
     *                 otherwise <tt>false</tt>.
     */
    protected void conditionallyLogCounts(boolean complete) {
        int successCount = this.getSuccessCount();
        int failureCount = this.getFailureCount();

        long now = System.currentTimeMillis();
        long lapse = (this.progressLogTimestamp > 0L)
                ? (now - this.progressLogTimestamp)
                : 0L;

        if (complete || (lapse > 30000L)) {
            System.out.println(this.getClass().getSimpleName()
                    + (complete ? " Complete: " : " Progress: ")
                    + successCount + " (succeeded) / " + failureCount
                    + " (failed)");
            this.progressLogTimestamp = now;
        }
        if (complete) {
            System.out.println();
        }
        if (this.progressLogTimestamp < 0L) {
            this.progressLogTimestamp = now;
        }
    }

    /**
     * Returns the current failure count. The failure count is incremented via
     * {@link #incrementFailureCount()}.
     *
     * @return The current success count.
     */
    protected int getFailureCount() {
        return this.failureCount;
    }

    /**
     * Returns the current success count. The success count is incremented via
     * {@link #incrementSuccessCount()}.
     *
     * @return The current success count.
     */
    protected int getSuccessCount() {
        return this.successCount;
    }
}
