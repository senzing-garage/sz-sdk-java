package com.senzing.sdk;

import java.util.Set;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Provides an <b>unmodifiable</b> runtime-typed {@link Set}
 * whose elements are strictly non-null instances of
 * {@link SzRecordKey}.
 */
public final class SzRecordKeys extends AbstractSet<SzRecordKey>
{
    /**
     * Provides a builder class for creating instances of
     * {@link SzRecordKeys}.
     */
    public static final class Builder {
        /**
         * The backing {@link Set} of {@link SzRecordKey} instances.
         */
        private Set<SzRecordKey> keys = null;

        /**
         * Private default constructor.
         */
        private Builder() {
            this.keys = new TreeSet<>();
        }

        /**
         * Adds an {@link SzRecordKey} instance to this {@link Builder}.
         * 
         * @param key The {@link SzRecordKey} to add to this {@link Builder}.
         * 
         * @return A reference to this instance.
         * 
         * @throws NullPointerException If either of the parameters is
         *                              <code>null</code>.
         * 
         * @throws IllegalStateException If this builder has already had
         *                               its {@link #build()} method called.
         */
        public Builder key(SzRecordKey key)
            throws NullPointerException, IllegalStateException
        {
            if (this.keys == null) {
                throw new IllegalStateException(
                    "This builder has already built an SzRecordKeys");
            }
            this.keys.add(key);
            return this;
        }

        /**
         * Creates an instance of {@link SzRecordKey} using the specified
         * data source code and record ID and adds that instance to this
         * {@link Builder}.
         * 
         * @param dataSourceCode The data source code for the {@link
         *                       SzRecordKey} to add to this {@link Builder}.
         * 
         * @param recordId The record ID for the {@link SzRecordKey} to add
         *                 to this {@link Builder}.
         * 
         * @return A reference to this instance.
         * 
         * @throws NullPointerException If either of the parameters is
         *                              <code>null</code>.
         * 
         * @throws IllegalStateException If this builder has already had
         *                               its {@link #build()} method called.
         */
        public Builder key(String dataSourceCode, String recordId)
            throws NullPointerException, IllegalStateException
        {
            if (this.keys == null) {
                throw new IllegalStateException(
                    "This builder has already built an SzRecordKeys");
            }
            this.keys.add(SzRecordKey.of(dataSourceCode, recordId));
            return this;
        }

        /**
         * Builds a new instance of {@link SzRecordKeys} using the 
         * {@link SzRecordKey} instances that were added to this builder
         * instance.
         * 
         * @return The newly constructed instance of {@link SzRecordKeys}.
         * 
         * @throws IllegalStateException If this builder has already had
         *                               its {@link #build()} method called.
         */
        public SzRecordKeys build() throws IllegalStateException
        {
            if (this.keys == null) {
                throw new IllegalStateException(
                    "This builder has already built an SzRecordKeys");
            }
            SzRecordKeys result = new SzRecordKeys(this);
            this.keys = null;
            return result;
        }
    }

    /**
     * The underlying {@link Set} of {@link SzRecordKey} instances.
     */
    private Set<SzRecordKey> keys = null;

    /**
     * Private constructor for creating a new instance with a 
     * {@link Builder} instance.
     * 
     * @param builder The {@link Builder} from which to extract the
     *                {@link SzRecordKey} instances.
     */
    private SzRecordKeys(Builder builder) {
        this.keys = Collections.unmodifiableSet(builder.keys);
    }

    /**
     * Constructs with the specified {@link Collection} of
     * non-null {@link SzRecordKey} instanes.
     * 
     * @param keys The {@link Collection} of {@link SzRecordKey} instances.
     * 
     * @throws NullPointerException If the specified {@link Collection}
     *                              is <code>null</code> or contains
     *                              <code>null</code> elements.
     */
    public SzRecordKeys(Collection<SzRecordKey> keys) {
        this.keys = Collections.unmodifiableSet(new TreeSet<>(keys));
    }

    /**
     * Constructs an empty instance with no {@link SzRecordKey} elements.
     */
    public SzRecordKeys() {
        this.keys = Set.of();
    }

    /**
     * Constructs an instance with exactly one {@link SzRecordKey}.
     * 
     * @param key The single {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If the specified parameter is
     *                              <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key)
        throws NullPointerException
    {
        this.keys = Set.of(key);
    }

    /**
     * Constructs an instance with exactly two {@link SzRecordKey}
     * instances.  If the specified {@link SzRecordKey} instances
     * are duplicates of each other then the second will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If either of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1, SzRecordKey key2) 
        throws NullPointerException
    {
        this.keys = Set.of(key1, key2);
    }

    /**
     * Constructs an instance with exactly three {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3) 
        throws NullPointerException
    {
        this.keys = Set.of(key1, key2, key3);
    }

    /**
     * Constructs an instance with exactly four {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3,
                        SzRecordKey key4)
        throws NullPointerException
    {
        this.keys = Set.of(key1, key2, key3, key4);
    }

    /**
     * Constructs an instance with exactly five {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3,
                        SzRecordKey key4,
                        SzRecordKey key5)
        throws NullPointerException
    {
        this.keys = Set.of(key1, key2, key3, key4, key5);
    }

    /**
     * Constructs an instance with exactly six {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3,
                        SzRecordKey key4,
                        SzRecordKey key5,
                        SzRecordKey key6)
        throws NullPointerException
    {
        this.keys = Set.of(key1, key2, key3, key4, key5, key6);
    }
 
    /**
     * Constructs an instance with exactly seven {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3,
                        SzRecordKey key4,
                        SzRecordKey key5,
                        SzRecordKey key6,
                        SzRecordKey key7)
        throws NullPointerException
    {
        this.keys = Set.of(key1, key2, key3, key4, key5, key6, key7);
    }

    /**
     * Constructs an instance with exactly eight {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * @param key8 The eighth {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3,
                        SzRecordKey key4,
                        SzRecordKey key5,
                        SzRecordKey key6,
                        SzRecordKey key7,
                        SzRecordKey key8)
        throws NullPointerException
    {
        this.keys = Set.of(
            key1, key2, key3, key4, key5, key6, key7, key8);
    }

    /**
     * Constructs an instance with exactly nine {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * @param key8 The eighth {@link SzRecordKey} for this instance.
     * @param key9 The ninth {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3,
                        SzRecordKey key4,
                        SzRecordKey key5,
                        SzRecordKey key6,
                        SzRecordKey key7,
                        SzRecordKey key8,
                        SzRecordKey key9)
        throws NullPointerException
    {
        this.keys = Set.of(
            key1, key2, key3, key4, key5, key6, key7, key8, key9);
    }

    /**
     * Constructs an instance with exactly ten {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * @param key8 The eighth {@link SzRecordKey} for this instance.
     * @param key9 The ninth {@link SzRecordKey} for this instance.
     * @param key10 The tenth {@link SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey key1,
                        SzRecordKey key2,
                        SzRecordKey key3,
                        SzRecordKey key4,
                        SzRecordKey key5,
                        SzRecordKey key6,
                        SzRecordKey key7,
                        SzRecordKey key8,
                        SzRecordKey key9,
                        SzRecordKey key10)
        throws NullPointerException
    {
        this.keys = Set.of(
            key1, key2, key3, key4, key5, key6, key7, key8, key9, key10);
    }

    /**
     * Constructs an instance with a variable-argument array of
     * {@link SzRecordKey} instances.  This constructor is called
     * when an array is specified or eleven or more variable argument
     * parameters are provided.  If any of the specified {@link
     * SzRecordKey} instances are duplicated, then the duplicates
     * will be ignored.
     * 
     * @param keys The zero or more {@link SzRecordKey} instances
     *             for this instance.
     * 
     * @throws NullPointerException If any of the specified variable
     *                              argument parameters or elements in
     *                              a specified array is <code>null</code>.
     */
    public SzRecordKeys(SzRecordKey... keys)
        throws NullPointerException
    {
        Set<SzRecordKey> set = new TreeSet<>();
        for (SzRecordKey key : keys) {
            set.add(key);
        }
        this.keys = Collections.unmodifiableSet(set);
    }

    /**
     * Constructs an instance with exactly one {@link SzRecordKey}
     * using the specified data source code and record ID.
     * 
     * @param dataSourceCode The non-null data source code of the single
     *                       {@link SzRecordKey} for this instance.
     * 
     * @param recordId The non-null record ID of the single {@link
     *                 SzRecordKey} for this instance.
     * 
     * @throws NullPointerException If either of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String dataSourceCode, String recordId)
        throws NullPointerException
    {
        this.keys = Set.of(SzRecordKey.of(dataSourceCode, recordId));
    }

    /**
     * Constructs an instance with exactly two {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If the specified pairs of data source code and
     * record ID's are duplicates of each other then the second pair
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2) 
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2));
    }

    /**
     * Constructs an instance with exactly three {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3) 
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3));
    }

    /**
     * Constructs an instance with exactly four {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3,
                        String  dataSourceCode4,
                        String  recordId4)
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3),
            SzRecordKey.of(dataSourceCode4, recordId4));
    }

    /**
     * Constructs an instance with exactly five {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3,
                        String  dataSourceCode4,
                        String  recordId4,
                        String  dataSourceCode5,
                        String  recordId5)
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3),
            SzRecordKey.of(dataSourceCode4, recordId4),
            SzRecordKey.of(dataSourceCode5, recordId5));
    }

    /**
     * Constructs an instance with exactly six {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3,
                        String  dataSourceCode4,
                        String  recordId4,
                        String  dataSourceCode5,
                        String  recordId5,
                        String  dataSourceCode6,
                        String  recordId6)
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3),
            SzRecordKey.of(dataSourceCode4, recordId4),
            SzRecordKey.of(dataSourceCode5, recordId5),
            SzRecordKey.of(dataSourceCode6, recordId6));
    }

    /**
     * Constructs an instance with exactly seven {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3,
                        String  dataSourceCode4,
                        String  recordId4,
                        String  dataSourceCode5,
                        String  recordId5,
                        String  dataSourceCode6,
                        String  recordId6,
                        String  dataSourceCode7,
                        String  recordId7)
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3),
            SzRecordKey.of(dataSourceCode4, recordId4),
            SzRecordKey.of(dataSourceCode5, recordId5),
            SzRecordKey.of(dataSourceCode6, recordId6),
            SzRecordKey.of(dataSourceCode7, recordId7));
    }

    /**
     * Constructs an instance with exactly eight {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode8 The data source code of the eighth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId8 The record ID of the eighth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3,
                        String  dataSourceCode4,
                        String  recordId4,
                        String  dataSourceCode5,
                        String  recordId5,
                        String  dataSourceCode6,
                        String  recordId6,
                        String  dataSourceCode7,
                        String  recordId7,
                        String  dataSourceCode8,
                        String  recordId8)
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3),
            SzRecordKey.of(dataSourceCode4, recordId4),
            SzRecordKey.of(dataSourceCode5, recordId5),
            SzRecordKey.of(dataSourceCode6, recordId6),
            SzRecordKey.of(dataSourceCode7, recordId7),
            SzRecordKey.of(dataSourceCode8, recordId8));
    }

    /**
     * Constructs an instance with exactly nine {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode8 The data source code of the eighth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId8 The record ID of the eighth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode9 The data source code of the ninth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId9 The record ID of the ninth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3,
                        String  dataSourceCode4,
                        String  recordId4,
                        String  dataSourceCode5,
                        String  recordId5,
                        String  dataSourceCode6,
                        String  recordId6,
                        String  dataSourceCode7,
                        String  recordId7,
                        String  dataSourceCode8,
                        String  recordId8,
                        String  dataSourceCode9,
                        String  recordId9)
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3),
            SzRecordKey.of(dataSourceCode4, recordId4),
            SzRecordKey.of(dataSourceCode5, recordId5),
            SzRecordKey.of(dataSourceCode6, recordId6),
            SzRecordKey.of(dataSourceCode7, recordId7),
            SzRecordKey.of(dataSourceCode8, recordId8),
            SzRecordKey.of(dataSourceCode9, recordId9));
    }

    /**
     * Constructs an instance with exactly ten {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode8 The data source code of the eighth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId8 The record ID of the eighth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode9 The data source code of the ninth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId9 The record ID of the ninth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode10 The data source code of the tenth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId10 The record ID of the tenth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String  dataSourceCode1,
                        String  recordId1,
                        String  dataSourceCode2,
                        String  recordId2,
                        String  dataSourceCode3,
                        String  recordId3,
                        String  dataSourceCode4,
                        String  recordId4,
                        String  dataSourceCode5,
                        String  recordId5,
                        String  dataSourceCode6,
                        String  recordId6,
                        String  dataSourceCode7,
                        String  recordId7,
                        String  dataSourceCode8,
                        String  recordId8,
                        String  dataSourceCode9,
                        String  recordId9,
                        String  dataSourceCode10,
                        String  recordId10)
        throws NullPointerException
    {
        this.keys = Set.of(
            SzRecordKey.of(dataSourceCode1, recordId1),
            SzRecordKey.of(dataSourceCode2, recordId2),
            SzRecordKey.of(dataSourceCode3, recordId3),
            SzRecordKey.of(dataSourceCode4, recordId4),
            SzRecordKey.of(dataSourceCode5, recordId5),
            SzRecordKey.of(dataSourceCode6, recordId6),
            SzRecordKey.of(dataSourceCode7, recordId7),
            SzRecordKey.of(dataSourceCode8, recordId8),
            SzRecordKey.of(dataSourceCode9, recordId9),
            SzRecordKey.of(dataSourceCode10, recordId10));
    }

    /**
     * Constructs an instance with a variable-argument array of
     * {@link String} instances with an even-number length representing
     * pairs of data source codes and record ID's for the
     * {@link SzRecordKey} instances for this instance.  This constructor
     * is called when an array {@link String} instances is specified with
     * twenty-one or more variable argument parameters.  If any of the
     * specified pairs of data source code and record ID's is duplicated,
     * then the duplicate pairs will be ignored.
     * 
     * @param keyParts The pairs of data source code and record ID pairs
     *                 for the {@link SzRecordKey} instances for this
     *                 instance.
     * 
     * @throws IllegalArgumentException If an odd number of parameters
     *                                  is specified or an array with odd-number
     *                                  length is specified.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzRecordKeys(String... keyParts)
        throws NullPointerException
    {
        if (keyParts.length % 2 != 0) {
            throw new IllegalArgumentException(
                "An even number of parameters must be specified");
        }
        Set<SzRecordKey> set = new TreeSet<>();
        for (int index = 0; index < keyParts.length; index += 2) {
            String dataSourceCode   = keyParts[index];
            String recordId         = keyParts[index+1];
            set.add(SzRecordKey.of(dataSourceCode, recordId));
        }
        this.keys = Collections.unmodifiableSet(set);
    }

    /**
     * Constructs with the specified {@link Collection} of
     * non-null {@link SzRecordKey} instanes.  Unlike the 
     * {@link #SzRecordKeys(Collection)} constructor, this static
     * factory method allows the parameter to be <code>null</code>,
     * resulting in a <code>null</code> return value.
     * 
     * @param keys The {@link Collection} of {@link SzRecordKey} instances.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}, or
     *         <code>null</code> if the specified parameter is <code>null</code>.
     * 
     * @throws NullPointerException If the specified {@link Collection} 
     *                              contains <code>null</code> elements.
     */
    public static SzRecordKeys of(Collection<SzRecordKey> keys) {
        if (keys == null) return null;
        return new SzRecordKeys(new TreeSet<>(keys));
    }

    /**
     * Constructs an empty instance with no {@link SzRecordKey} elements.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     */
    public static SzRecordKeys of() {
        return new SzRecordKeys();
    }

    /**
     * Constructs an instance with exactly one {@link SzRecordKey}.
     * 
     * @param key The single {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If the specified parameter is
     *                              <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey key)
        throws NullPointerException
    {
        return new SzRecordKeys(key);
    }

    /**
     * Constructs an instance with exactly two {@link SzRecordKey}
     * instances.  If the specified {@link SzRecordKey} instances
     * are duplicates of each other then the second will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If either of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey key1, SzRecordKey key2) 
        throws NullPointerException
    {
        return new SzRecordKeys(key1, key2);
    }

    /**
     * Constructs an instance with exactly three {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3) 
        throws NullPointerException
    {
        return new SzRecordKeys(key1, key2, key3);
    }

    /**
     * Constructs an instance with exactly four {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3,
                                  SzRecordKey   key4)
        throws NullPointerException
    {
        return new SzRecordKeys(key1, key2, key3, key4);
    }

    /**
     * Constructs an instance with exactly five {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3,
                                  SzRecordKey   key4,
                                  SzRecordKey   key5)
        throws NullPointerException
    {
        return new SzRecordKeys(key1, key2, key3, key4, key5);
    }

    /**
     * Constructs an instance with exactly six {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3,
                                  SzRecordKey   key4,
                                  SzRecordKey   key5,
                                  SzRecordKey   key6)
        throws NullPointerException
    {
        return new SzRecordKeys(key1, key2, key3, key4, key5, key6);
    }
 
    /**
     * Constructs an instance with exactly seven {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3,
                                  SzRecordKey   key4,
                                  SzRecordKey   key5,
                                  SzRecordKey   key6,
                                  SzRecordKey   key7)
        throws NullPointerException
    {
        return new SzRecordKeys(key1, key2, key3, key4, key5, key6, key7);
    }

    /**
     * Constructs an instance with exactly eight {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * @param key8 The eighth {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3,
                                  SzRecordKey   key4,
                                  SzRecordKey   key5,
                                  SzRecordKey   key6,
                                  SzRecordKey   key7,
                                  SzRecordKey   key8)
        throws NullPointerException
    {
        return new SzRecordKeys(
            key1, key2, key3, key4, key5, key6, key7, key8);
    }

    /**
     * Constructs an instance with exactly nine {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * @param key8 The eighth {@link SzRecordKey} for this instance.
     * @param key9 The ninth {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3,
                                  SzRecordKey   key4,
                                  SzRecordKey   key5,
                                  SzRecordKey   key6,
                                  SzRecordKey   key7,
                                  SzRecordKey   key8,
                                  SzRecordKey   key9)
        throws NullPointerException
    {
        return new SzRecordKeys(
            key1, key2, key3, key4, key5, key6, key7, key8, key9);
    }

    /**
     * Constructs an instance with exactly ten {@link SzRecordKey}
     * instances.  If any of the specified {@link SzRecordKey}
     * instances are duplicated, then the duplicates will be ignored.
     * 
     * @param key1 The first {@link SzRecordKey} for this instance.
     * @param key2 The second {@link SzRecordKey} for this instance.
     * @param key3 The third {@link SzRecordKey} for this instance.
     * @param key4 The fourth {@link SzRecordKey} for this instance.
     * @param key5 The fifth {@link SzRecordKey} for this instance.
     * @param key6 The sixth {@link SzRecordKey} for this instance.
     * @param key7 The seventh {@link SzRecordKey} for this instance.
     * @param key8 The eighth {@link SzRecordKey} for this instance.
     * @param key9 The ninth {@link SzRecordKey} for this instance.
     * @param key10 The tenth {@link SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey   key1,
                                  SzRecordKey   key2,
                                  SzRecordKey   key3,
                                  SzRecordKey   key4,
                                  SzRecordKey   key5,
                                  SzRecordKey   key6,
                                  SzRecordKey   key7,
                                  SzRecordKey   key8,
                                  SzRecordKey   key9,
                                  SzRecordKey   key10)
        throws NullPointerException
    {
        return new SzRecordKeys(
            key1, key2, key3, key4, key5, key6, key7, key8, key9, key10);
    }

    /**
     * Constructs an instance with a variable-argument array of
     * {@link SzRecordKey} instances.  This method is called when
     * an array is specified or eleven or more variable argument
     * parameters are provided.  If any of the specified {@link
     * SzRecordKey} instances are duplicated, then the duplicates
     * will be ignored.
     * <p>
     * Unlike the {@link #SzRecordKeys(SzRecordKey[])} constructor,
     * this static factory method allows the parameter to be
     * <code>null</code>, resulting in a <code>null</code> return
     * value.
     * 
     * @param keys The zero or more {@link SzRecordKey} instances
     *             for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys},
     *         or <code>null</code> if called with a <code>null</code>
     *         array.
     * 
     * @throws NullPointerException If any of the variable argument
     *                              parameters is <code>null</code>.
     */
    public static SzRecordKeys of(SzRecordKey... keys)
        throws NullPointerException
    {
        if (keys == null) return null;
        return new SzRecordKeys(keys);
    }

    /**
     * Constructs an instance with exactly one {@link SzRecordKey}
     * using the specified data source code and record ID.
     * 
     * @param dataSourceCode The non-null data source code of the single
     *                       {@link SzRecordKey} for this instance.
     * 
     * @param recordId The non-null record ID of the single {@link
     *                 SzRecordKey} for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If either of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String dataSourceCode, String recordId)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode, recordId);
    }

    /**
     * Constructs an instance with exactly two {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If the specified pairs of data source code and
     * record ID's are duplicates of each other then the second pair
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2) 
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2);
    }

    /**
     * Constructs an instance with exactly three {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3) 
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3);
    }

    /**
     * Constructs an instance with exactly four {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3,
                                  String    dataSourceCode4,
                                  String    recordId4)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3,
                                dataSourceCode4, recordId4);
    }

    /**
     * Constructs an instance with exactly five {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3,
                                  String    dataSourceCode4,
                                  String    recordId4,
                                  String    dataSourceCode5,
                                  String    recordId5)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3,
                                dataSourceCode4, recordId4,
                                dataSourceCode5, recordId5);
    }

    /**
     * Constructs an instance with exactly six {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3,
                                  String    dataSourceCode4,
                                  String    recordId4,
                                  String    dataSourceCode5,
                                  String    recordId5,
                                  String    dataSourceCode6,
                                  String    recordId6)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3,
                                dataSourceCode4, recordId4,
                                dataSourceCode5, recordId5,
                                dataSourceCode6, recordId6);
    }

    /**
     * Constructs an instance with exactly seven {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3,
                                  String    dataSourceCode4,
                                  String    recordId4,
                                  String    dataSourceCode5,
                                  String    recordId5,
                                  String    dataSourceCode6,
                                  String    recordId6,
                                  String    dataSourceCode7,
                                  String    recordId7)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3,
                                dataSourceCode4, recordId4,
                                dataSourceCode5, recordId5,
                                dataSourceCode6, recordId6,
                                dataSourceCode7, recordId7);
    }

    /**
     * Constructs an instance with exactly eight {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode8 The data source code of the eighth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId8 The record ID of the eighth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3,
                                  String    dataSourceCode4,
                                  String    recordId4,
                                  String    dataSourceCode5,
                                  String    recordId5,
                                  String    dataSourceCode6,
                                  String    recordId6,
                                  String    dataSourceCode7,
                                  String    recordId7,
                                  String    dataSourceCode8,
                                  String    recordId8)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3,
                                dataSourceCode4, recordId4,
                                dataSourceCode5, recordId5,
                                dataSourceCode6, recordId6,
                                dataSourceCode7, recordId7,
                                dataSourceCode8, recordId8);
    }

    /**
     * Constructs an instance with exactly nine {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode8 The data source code of the eighth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId8 The record ID of the eighth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode9 The data source code of the ninth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId9 The record ID of the ninth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3,
                                  String    dataSourceCode4,
                                  String    recordId4,
                                  String    dataSourceCode5,
                                  String    recordId5,
                                  String    dataSourceCode6,
                                  String    recordId6,
                                  String    dataSourceCode7,
                                  String    recordId7,
                                  String    dataSourceCode8,
                                  String    recordId8,
                                  String    dataSourceCode9,
                                  String    recordId9)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3,
                                dataSourceCode4, recordId4,
                                dataSourceCode5, recordId5,
                                dataSourceCode6, recordId6,
                                dataSourceCode7, recordId7,
                                dataSourceCode8, recordId8,
                                dataSourceCode9, recordId9);
    }

    /**
     * Constructs an instance with exactly ten {@link SzRecordKey}
     * instances using the specified pairs of data source codes and
     * record ID's.  If any of the specified pairs of data source
     * code and record ID's are duplicated, then the duplicate pairs
     * will be ignored.
     * 
     * @param dataSourceCode1 The data source code of the first 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId1 The record ID of the first {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode2 The data source code of the second 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId2 The record ID of the second {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode3 The data source code of the third 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId3 The record ID of the third {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode4 The data source code of the fourth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId4 The record ID of the fourth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode5 The data source code of the fifth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId5 The record ID of the fifth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode6 The data source code of the sixth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId6 The record ID of the sixth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode7 The data source code of the seventh 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId7 The record ID of the seventh {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode8 The data source code of the eighth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId8 The record ID of the eighth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode9 The data source code of the ninth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId9 The record ID of the ninth {@link SzRecordKey}
     *                  for this instance.
     * @param dataSourceCode10 The data source code of the tenth 
     *                        {@link SzRecordKey} for this instance.
     * @param recordId10 The record ID of the tenth {@link SzRecordKey}
     *                  for this instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzRecordKeys of(String    dataSourceCode1,
                                  String    recordId1,
                                  String    dataSourceCode2,
                                  String    recordId2,
                                  String    dataSourceCode3,
                                  String    recordId3,
                                  String    dataSourceCode4,
                                  String    recordId4,
                                  String    dataSourceCode5,
                                  String    recordId5,
                                  String    dataSourceCode6,
                                  String    recordId6,
                                  String    dataSourceCode7,
                                  String    recordId7,
                                  String    dataSourceCode8,
                                  String    recordId8,
                                  String    dataSourceCode9,
                                  String    recordId9,
                                  String    dataSourceCode10,
                                  String    recordId10)
        throws NullPointerException
    {
        return new SzRecordKeys(dataSourceCode1, recordId1,
                                dataSourceCode2, recordId2,
                                dataSourceCode3, recordId3,
                                dataSourceCode4, recordId4,
                                dataSourceCode5, recordId5,
                                dataSourceCode6, recordId6,
                                dataSourceCode7, recordId7,
                                dataSourceCode8, recordId8,
                                dataSourceCode9, recordId9,
                                dataSourceCode10, recordId10);
    }

    /**
     * Constructs an instance with a variable-argument array of
     * {@link String} instances with an even-number length
     * representing pairs of data source codes and record ID's
     * for the {@link SzRecordKey} instances for this instance.
     * This static factory method is called when an array of 
     * {@link String} instances is specified with an odd number of
     * variable argument parameters or twenty-one or more variable
     * argument parameters.  If any of the specified pairs of data
     * source code and record ID's is duplicated, then the duplicate
     * pairs will be ignored.
     * <p>
     * Unlike the {@link #SzRecordKeys(String[])} constructor,
     * this static factory method allows the parameter to be
     * <code>null</code>, resulting in a <code>null</code> return
     * value.
    * 
     * @param keyParts The pairs of data source code and record ID pairs
     *                 for the {@link SzRecordKey} instances for this
     *                 instance.
     * 
     * @return The newly constructed instance of {@link SzRecordKeys}, or
     *         <code>null</code> if called with a <code>null</code> array.
     * 
     * @throws IllegalArgumentException If an odd number of parameters
     *                                  is specified or an array with odd-number
     *                                  length is specified.
     * 
     * @throws NullPointerException If any of variable argument parameters
     *                              or elements in an array parameter is 
     *                              <code>null</code>.
     */
    public static SzRecordKeys of(String... keyParts)
        throws NullPointerException
    {
        if (keyParts == null) return null;
        return new SzRecordKeys(keyParts);
    }

    /**
     * Creates a new {@link Builder} for creating an instance of
     * {@link SzRecordKeys}.
     * 
     * @return A new {@link Builder} for creating an instance of
     *         {@link SzRecordKeys}.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override 
    public boolean contains(Object element) {
        return this.keys.contains(element);
    }
    
    @Override
    public boolean containsAll(Collection<?> elements) {
        return this.keys.containsAll(elements);
    }
    
    @Override
    public Iterator<SzRecordKey> iterator() {
        return this.keys.iterator();
    }

    @Override
    public int size() {
        return this.keys.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String prefix = " ";
        for (SzRecordKey key : this.keys) {
            sb.append(prefix);
            sb.append("[ ").append(key).append(" ]");
            prefix = ", ";
        }
        sb.append(" }");
        return sb.toString();
    }

}
