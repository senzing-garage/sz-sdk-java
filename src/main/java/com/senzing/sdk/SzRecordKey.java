package com.senzing.sdk;

import java.util.Objects;

/**
 * Desribes a key for identifying a record as Java record class containing
 * a data source code and record ID.
 * 
 * @param dataSourceCode The non-null {@link String} data source code 
 *                       identifying the data source of the record.
 * 
 * @param recordId The non-null {@link String} record ID identifying 
 *                 the record within the data source.
 */
public record SzRecordKey(String dataSourceCode, String recordId) implements Comparable<SzRecordKey>
{
    /**
     * Constructs with the specified data source code and record ID.  This
     * will throw a {@link NullPointerException} if either parameter is
     * <code>null</code> and throw {@link IllegalArgumentException} if 
     * either parameter is empty-string or only contains whitespace.
     * 
     * @param dataSourceCode The non-null {@link String} data source code 
     *                       identifying the data source of the record.
     * 
     * @param recordId The non-null {@link String} record ID identifying 
     *                 the record within the data source.
     */
    public SzRecordKey(String dataSourceCode, String recordId) 
    {
        Objects.requireNonNull(dataSourceCode, "The data source code cannot be null");
        Objects.requireNonNull(recordId, "The record ID cannot be null");
        if (dataSourceCode.trim().length() == 0 || recordId.trim().length() == 0) {
            throw new IllegalArgumentException(
                "Empty string or all whitespace not allowed for either parameter.  "
                + "dataSourceCode=[ " + dataSourceCode + " ], recordId=[ " + recordId + " ]");
        }
        this.dataSourceCode = dataSourceCode.trim();
        this.recordId = recordId.trim();
    }

    /**
     * Shorthand static for constructing a new instance of {@link SzRecordKey}
     * with the specified data source code and record ID.  The specified 
     * data source code and record ID must not be <code>null</code> and must
     * each contain at least one character that is not whitespace or else an
     * exception is thrown.
     * 
     * @param dataSourceCode The non-null {@link String} data source code 
     *                       identifying the data source of the record.
     * 
     * @param recordId The non-null {@link String} record ID identifying 
     *                 the record within the data source.
     * 
     * @return The constructed {@link SzRecordKey}.
     * 
     * @throws NullPointerException If either parameter is <code>null</code>.
     * @throws IllegalArgumentException If either parameter is empty-string or
     *                                  only contains whitespace.
     */
    public static SzRecordKey of(String dataSourceCode, String recordId) 
        throws NullPointerException, IllegalArgumentException
    {
        return new SzRecordKey(dataSourceCode, recordId);
    }

    /**
     * Implemented to sort {@link SzRecordKey} instances first on data source 
     * code and then on record ID.  This is a null-friendly comparison that sorts
     * <code>null</code> values firsdt.
     * 
     * @param recordKey The {@link SzRecordKey} to compare to.
     * 
     * @return A negative integer, zero (0) or a positive integer depending on
     *         whether this instance sorts less-than, equal-to or greater-than
     *         the specified instance, respectively.
     */
    public int compareTo(SzRecordKey recordKey) {
        if (recordKey == null) {
            return 1; // null-friendly, null first
        }
        int diff = this.dataSourceCode().compareTo(recordKey.dataSourceCode());
        if (diff != 0) {
            return diff;
        }
        return this.recordId().compareTo(recordKey.recordId());
    }

    /**
     * Returns a brief {@link String} representation of this record class.
     * This formats the data source code and record ID with a colon 
     * character in between them.
     * 
     * @return A brief {@link String} representation of this class.
     */
    public String toString() {
        return this.dataSourceCode + ":" + this.recordId;
    }
}
