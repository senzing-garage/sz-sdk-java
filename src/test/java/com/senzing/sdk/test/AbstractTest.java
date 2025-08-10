package com.senzing.sdk.test;

import static com.senzing.io.IOUtilities.UTF_8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.json.JsonObject;

import com.senzing.util.JsonUtilities;

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

    /**
     * Saves the result from a demo in a file in a path based on the
     * class name for this class.
     * 
     * @param region The region name assigned to the demo snippet.
     * @param result The result to write to the file.
     */
    protected void saveDemoResult(String region, String result) {
        this.saveDemoResult(region, result, false);
    }

    /**
     * Saves the result from a demo in a file in a path based on the
     * class name for this class, optionally formatting the result as 
     * a pretty-printed JSON object.
     * 
     * @param region The region name assigned to the demo snippet.
     * @param result The result to write to the file.
     * @param jsonFormat <code>true</code> if the result should be handles as a
     *                   JSON object and pretty-printed, <code>false</code> if 
     *                   should be taken literally.
     */
    protected void saveDemoResult(String region, String result, boolean jsonFormat) {
        if (jsonFormat) {
            JsonObject jsonObj = JsonUtilities.parseJsonObject(result);
            result = JsonUtilities.toJsonText(jsonObj, true).trim();
        }
        String basePath = this.getClass().getPackageName().replace('.', '/');
        String fileName = this.getClass().getSimpleName() + "-" + region + ".txt";
        String targetDirProp = System.getProperty("project.build.directory");

        File targetDir      = new File(targetDirProp);
        File includesDir    = new File(targetDir, "doc-includes");
        File packageDir     = new File(includesDir, basePath);
        File docFilesDir    = new File(packageDir, "doc-files");
        File resultsFile    = new File(docFilesDir, fileName);

        docFilesDir.mkdirs();

        try (FileOutputStream fos = new FileOutputStream(resultsFile);
             OutputStreamWriter osw = new OutputStreamWriter(fos, UTF_8)) 
        {
            osw.write(result);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }        
    }
}
