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
     * Returns overview information about the repository.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="getRepositoryInfo"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzDiagnosticDemo-getRepositoryInfo.txt"}
     * </p>
     *
     * @return A JSON {@link String} describing the repository.
     *
     * @throws SzException If a failure occurs.
     */
    String getRepositoryInfo() throws SzException;

    /**
     * Conducts a rudimentary repository test to gauge I/O and network performance.
     * 
     * <p>
     * Typically, this is only run when troubleshooting performance.  This is a
     * non-destructive test.
     * </p>
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="checkRepositoryPerformance"}
     * </p>
     * 
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzDiagnosticDemo-checkRepositoryPerformance.txt"}
     * </p>
     *
     * @param secondsToRun How long to run the database performance test.
     * 
     * @return The JSON {@link String} describing the results of the 
     *         performance test.
     *
     * @throws SzException If a failure occurs.
     */
    String checkRepositoryPerformance(int secondsToRun) throws SzException;

    /**
     * Permanently deletes all data in the repository, except the configuration.
     * 
     * <p>
     * <b>WARNING:</b> This method is destructive, it will delete all loaded
     * records and entity resolution decisions.  Senzing does not provide a 
     * means to the restore the data.  The only means of recovery would be
     * restoring from a database backup.
     * </p>
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
     * Experimental/internal for Senzing support use.
     * 
     * <p><b>Usage:</b>
     * {@snippet class="com.senzing.sdk.SzDiagnosticDemo" region="getFeature"}
     * </p>
     *
     * <p><b>Example Result:</b> (formatted for readability)
     * {@snippet file="com/senzing/sdk/doc-files/SzDiagnosticDemo-getFeature.txt"}
     * </p>
     * 
     * @param featureId The identifier for the feature.
     * 
     * @return The feature definition describing the feature for the specified
     *         feature ID.
     * 
     * @throws SzException If a failure occurs.
     */
    @SzConfigRetryable
    String getFeature(long featureId) throws SzException;

}
