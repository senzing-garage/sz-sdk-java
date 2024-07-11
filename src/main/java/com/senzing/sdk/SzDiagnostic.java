package com.senzing.sdk;

/**
 * Defines the Java interface to the Senzing diagnostic functions.  The Senzing
 * diagnostic functions provide diagnostics and statistics pertaining to the 
 * host system and the Senzing repository.
 */
public interface SzDiagnostic {
    /**
     * Gathers detailed information on the data store and returns it as a
     * JSON {@link String}.
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
     * @throws SzException If a failure occurs.
     */
    void purgeRepository() throws SzException;

    /**
     * Experimental/internal method for obtaining diagnostic feature definition
     * for the specified feature identifier.
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
