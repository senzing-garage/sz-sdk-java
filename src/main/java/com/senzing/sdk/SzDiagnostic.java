package com.senzing.sdk;

/**
 * <p>
 * Defines the Java interface to the Senzing diagnostic functions.  The Senzing
 * diagnostic functions provide diagnostics and statistics pertaining to the 
 * host system and the Senzing repository.
 * </p>
 * 
 * <p>
 * An {@link SzDiagnostic} instance is typically obtained from an
 * {@link SzEnvironment} instance via the {@link SzEnvironment#getDiagnostic()}
 * method as follows:
 *
 * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="getDiagnostic"}
 * </p>
 */
public interface SzDiagnostic {
    /**
     * Gathers detailed information on the data store and returns it as a
     * JSON {@link String}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="getDatastoreInfo"}
     * </p>
     *
     * @return A JSON {@link String} describing the datastore.
     *
     * @throws SzException If a failure occurs.
     */
    String getDatastoreInfo() throws SzException;

    /**
     * Runs non-destruction DB performance tests and returns detail of the 
     * result as a JSON {@link String}.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="checkDatastorePerformance"}
     * </p>
     * 
     * @param secondsToRun How long to run the database performance test.
     * 
     * @return The JSON {@link String} describing the results of the 
     *         performance test.
     *
     * @throws SzException If a failure occurs.
     */
    String checkDatastorePerformance(int secondsToRun) throws SzException;

    /**
     * Purges all data in the configured repository.
     * <p>
     * <b>WARNING:</b> There is no undoing from this.  Make sure your
     * repository is regularly backed up.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="purgeRepository"}
     * </p>
     *
     * @throws SzException If a failure occurs.
     * 
     * @see <a href="https://raw.githubusercontent.com/Senzing/code-snippets-v4/refs/heads/main/java/snippets/initialization/PurgeRepository.java">Purge Repository Code Snippet</a>
     */
    void purgeRepository() throws SzException;

    /**
     * Experimental/internal method for obtaining diagnostic feature definition
     * for the specified feature identifier.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="getFeature"}
     * </p>
     *
     * @param featureId The identifier for the feature.
     * 
     * @return The feature definition describing the feature for the specified
     *         feature ID.
     * 
     * @throws SzException If a failure occurs.
     */
    String getFeature(long featureId) throws SzException;

}
