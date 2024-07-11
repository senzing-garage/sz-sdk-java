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
 * {@link Long} entity ID's.
 */
public final class SzEntityIds extends AbstractSet<Long>
{
    /**
     * Provides a builder class for creating instances of
     * {@link SzEntityIds}.
     */
    public static final class Builder {
        /**
         * The backing {@link Set} of {@link Long} entity ID
         * instances.
         */
        private Set<Long> ids = null;

        /**
         * Private default constructor.
         */
        private Builder() {
            this.ids = new TreeSet<>();
        }

        /**
         * Adds a {@link Long} entity ID instance to this {@link Builder}.
         * 
         * @param entityId The {@link Long} entity ID to add to this
         *                 {@link Builder}.
         * 
         * @return A reference to this instance.
         * 
         * @throws NullPointerException If the specified parameter is
         *                              <code>null</code>.
         * 
         * @throws IllegalStateException If this builder has already had
         *                               its {@link #build()} method called.
         */
        public Builder id(Long entityId)
            throws NullPointerException, IllegalStateException
        {
            if (this.ids == null) {
                throw new IllegalStateException(
                    "This builder has already built an SzEntityIds");
            }
            this.ids.add(entityId);
            return this;
        }

        /**
         * Builds a new instance of {@link SzEntityIds} using the 
         * {@link Long} entity ID instances that were added to this
         * builder instance.
         * 
         * @return The newly constructed instance of {@link SzEntityIds}.
         * 
         * @throws IllegalStateException If this builder has already had
         *                               its {@link #build()} method called.
         */
        public SzEntityIds build() throws IllegalStateException
        {
            if (this.ids == null) {
                throw new IllegalStateException(
                    "This builder has already built an SzEntityIds");
            }
            SzEntityIds result = new SzEntityIds(this);
            this.ids = null;
            return result;
        }
    }

    /**
     * The underlying {@link Set} of {@link Long} entity ID instances.
     */
    private Set<Long> ids = null;

    /**
     * Private constructor for creating a new instance with a 
     * {@link Builder} instance.
     * 
     * @param builder The {@link Builder} from which to extract the
     *                {@link Long} entity ID instances.
     */
    private SzEntityIds(Builder builder) {
        this.ids = Collections.unmodifiableSet(builder.ids);
    }

    /**
     * Constructs with the specified {@link Collection} of
     * non-null {@link Long} entity ID instances.
     * 
     * @param entityIds The {@link Collection} of {@link Long}
     *                  entity ID instances.
     * 
     * @throws NullPointerException If the specified {@link Collection}
     *                              is <code>null</code> or contains
     *                              <code>null</code> elements.
     */
    public SzEntityIds(Collection<Long> entityIds) {
        this.ids = Collections.unmodifiableSet(new TreeSet<>(entityIds));
    }

    /**
     * Constructs an empty instance with no elements.
     */
    public SzEntityIds() {
        this.ids = Set.of();
    }

    /**
     * Constructs an instance with exactly one entity ID.
     * 
     * @param entityId The single entity ID for this instance.
     * 
     * @throws NullPointerException If the specified parameter is
     *                              <code>null</code>.
     */
    public SzEntityIds(Long entityId)
        throws NullPointerException
    {
        this.ids = Set.of(entityId);
    }

    /**
     * Constructs an instance with exactly two entity ID's.
     * If the specified entity ID instances are duplicates of
     * each other then the second will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * 
     * @throws NullPointerException If either of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1, Long entityId2) 
        throws NullPointerException
    {
        this.ids = Set.of(entityId1, entityId2);
    }

    /**
     * Constructs an instance with exactly three entity ID's.  If any
     * of the specified entity ID instances are duplicated, then
     * the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1, Long entityId2, Long entityId3) 
        throws NullPointerException
    {
        this.ids = Set.of(entityId1, entityId2, entityId3);
    }

    /**
     * Constructs an instance with exactly four entity ID instances.
     * If any of the specified entityr ID instances are duplicated,
     * then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1, 
                       Long entityId2,
                       Long entityId3,
                       Long entityId4)
        throws NullPointerException
    {
        this.ids = Set.of(
            entityId1, entityId2, entityId3, entityId4);
    }

    /**
     * Constructs an instance with exactly five entity ID instances.
     * If any of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1,
                       Long entityId2,
                       Long entityId3,
                       Long entityId4,
                       Long entityId5)
        throws NullPointerException
    {
        this.ids = Set.of(
            entityId1, entityId2, entityId3, entityId4, entityId5);
    }

    /**
     * Constructs an instance with exactly six entity ID instances.
     * If any of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1,
                       Long entityId2,
                       Long entityId3,
                       Long entityId4,
                       Long entityId5,
                       Long entityId6)
        throws NullPointerException
    {
        this.ids = Set.of(
            entityId1, entityId2, entityId3,
            entityId4, entityId5, entityId6);
    }
 
    /**
     * Constructs an instance with exactly seven entity ID instances.
     * If any of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1,
                       Long entityId2,
                       Long entityId3,
                       Long entityId4,
                       Long entityId5,
                       Long entityId6,
                       Long entityId7)
        throws NullPointerException
    {
        this.ids = Set.of(
            entityId1, entityId2, entityId3, entityId4,
            entityId5, entityId6, entityId7);
    }

    /**
     * Constructs an instance with exactly eight entity ID instances.
     * If any of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * @param entityId8 The eighth entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1,
                       Long entityId2,
                       Long entityId3,
                       Long entityId4,
                       Long entityId5,
                       Long entityId6,
                       Long entityId7,
                       Long entityId8)
        throws NullPointerException
    {
        this.ids = Set.of(
            entityId1, entityId2, entityId3, entityId4,
            entityId5, entityId6, entityId7, entityId8);
    }

    /**
     * Constructs an instance with exactly nine entity ID instances.
     * If any of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * @param entityId8 The eighth entity ID for this instance.
     * @param entityId9 The ninth entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1,
                       Long entityId2,
                       Long entityId3,
                       Long entityId4,
                       Long entityId5,
                       Long entityId6,
                       Long entityId7,
                       Long entityId8,
                       Long entityId9)
        throws NullPointerException
    {
        this.ids = Set.of(
            entityId1, entityId2, entityId3, entityId4, entityId5,
            entityId6, entityId7, entityId8, entityId9);
    }

    /**
     * Constructs an instance with exactly ten entity ID instances.
     * If any of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * @param entityId8 The eighth entity ID for this instance.
     * @param entityId9 The ninth entity ID for this instance.
     * @param entityId10 The tenth entity ID for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long entityId1,
                       Long entityId2,
                       Long entityId3,
                       Long entityId4,
                       Long entityId5,
                       Long entityId6,
                       Long entityId7,
                       Long entityId8,
                       Long entityId9,
                       Long entityId10)
        throws NullPointerException
    {
        this.ids = Set.of(
            entityId1, entityId2, entityId3, entityId4, entityId5,
            entityId6, entityId7, entityId8, entityId9, entityId10);
    }

    /**
     * Constructs an instance with a variable-argument array of
     * {@link Long} entity ID instances.  This constructor is called
     * when an array is specified or eleven or more variable argument
     * parameters are provided.  If any of the specified {@link
     * Long} entity ID instances are duplicated, then the duplicates
     * will be ignored.
     * 
     * @param entityIds The zero or more {@link Long} entity ID 
     *                  instances for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(Long... entityIds)
        throws NullPointerException
    {
        this.ids = Set.of(entityIds);
    }

    /**
     * Constructs an instance with a variable-argument array of
     * <code>long</code> entity ID instances.  This constructor is called
     * when an array is specified or eleven or more variable argument
     * parameters are provided.  If any of the specified {@link
     * Long} entity ID instances are duplicated, then the duplicates
     * will be ignored.
     * 
     * @param entityIds The zero or more <code>long</code> entity ID 
     *                  instances for this instance.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public SzEntityIds(long... entityIds)
        throws NullPointerException
    {
        Long[] array = new Long[entityIds.length];
        for (int index = 0; index < entityIds.length; index++) {
            array[index] = entityIds[index];
        }
        this.ids = Set.of(array);
    }

    /**
     * Constructs with the specified {@link Collection} of
     * non-null {@link Long} entity ID instanes.  Unlike the
     * {@link #SzEntityIds(Collection)} constructor, this static
     * factory method allows the parameter to be <code>null</code>,
     * resulting in a <code>null</code> return value.
     * 
     * @param entityIds The {@link Collection} of {@link Long}
     *                  entity ID instances.
     * 
     * @return The newly constructed instance of {@link SzEntityIds},
     *         or <code>null</code> if the specified parameter is
     *         <code>null</code>.
     * 
     * @throws NullPointerException If the specified {@link Collection}
     *                              contains <code>null</code> elements.
     */
    public static SzEntityIds of(Collection<Long> entityIds) {
        if (entityIds == null) return null;
        return new SzEntityIds(new TreeSet<>(entityIds));
    }

    /**
     * Constructs an empty instance with no {@link Long} entity ID 
     * elements.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     */
    public static SzEntityIds of() {
        return new SzEntityIds();
    }

    /**
     * Constructs an instance with exactly one {@link Long} entity ID.
     * 
     * @param entityId The single entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If the specified parameter is
     *                              <code>null</code>.
     */
    public static SzEntityIds of(Long entityId)
        throws NullPointerException
    {
        return new SzEntityIds(entityId);
    }

    /**
     * Constructs an instance with exactly two entity ID's.  If the
     * specified {@link Long} entity ID instances are duplicates of
     * each other then the second will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If either of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long entityId1, Long entityId2) 
        throws NullPointerException
    {
        return new SzEntityIds(entityId1, entityId2);
    }

    /**
     * Constructs an instance with exactly three entity ID's.  If any
     * of the specified {@link Long} entity ID instances are duplicated,
     * then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3) 
        throws NullPointerException
    {
        return new SzEntityIds(entityId1, entityId2, entityId3);
    }

    /**
     * Constructs an instance with exactly four entity ID's.  If any
     * of the specified {@link Long} entity ID instances are duplicated,
     * then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3,
                                 Long   entityId4)
        throws NullPointerException
    {
        return new SzEntityIds(
            entityId1, entityId2, entityId3, entityId4);
    }

    /**
     * Constructs an instance with exactly five entity ID's.  If any
     * of the specified {@link Long} entity ID instances are duplicated,
     * then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3,
                                 Long   entityId4,
                                 Long   entityId5)
        throws NullPointerException
    {
        return new SzEntityIds(
            entityId1, entityId2, entityId3, entityId4, entityId5);
    }

    /**
     * Constructs an instance with exactly six entity ID's.  If any
     * of the specified {@link Long} entity ID instances are duplicated,
     * then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3,
                                 Long   entityId4,
                                 Long   entityId5,
                                 Long   entityId6)
        throws NullPointerException
    {
        return new SzEntityIds(
            entityId1, entityId2, entityId3,
            entityId4, entityId5, entityId6);
    }
 
    /**
     * Constructs an instance with exactly seven entity ID's.  If any
     * of the specified {@link Long} entity ID instances are duplicated,
     * then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3,
                                 Long   entityId4,
                                 Long   entityId5,
                                 Long   entityId6,
                                 Long   entityId7)
        throws NullPointerException
    {
        return new SzEntityIds(
            entityId1, entityId2, entityId3, entityId4, 
            entityId5, entityId6, entityId7);
    }

    /**
     * Constructs an instance with exactly eight entity ID's.  If any
     * of the specified {@link Long} entity ID instances are duplicated,
     * then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * @param entityId8 The eighth entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3,
                                 Long   entityId4,
                                 Long   entityId5,
                                 Long   entityId6,
                                 Long   entityId7,
                                 Long   entityId8)
        throws NullPointerException
    {
        return new SzEntityIds(
            entityId1, entityId2, entityId3, entityId4,
            entityId5, entityId6, entityId7, entityId8);
    }

    /**
     * Constructs an instance with exactly nine entity ID's.  If any
     * of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * @param entityId8 The eighth entity ID for this instance.
     * @param entityId9 The ninth entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3,
                                 Long   entityId4,
                                 Long   entityId5,
                                 Long   entityId6,
                                 Long   entityId7,
                                 Long   entityId8,
                                 Long   entityId9)
        throws NullPointerException
    {
        return new SzEntityIds(
            entityId1, entityId2, entityId3, entityId4, entityId5,
            entityId6, entityId7, entityId8, entityId9);
    }

    /**
     * Constructs an instance with exactly ten entity ID's.  If any
     * of the specified {@link Long} entity ID instances are
     * duplicated, then the duplicates will be ignored.
     * 
     * @param entityId1 The first entity ID for this instance.
     * @param entityId2 The second entity ID for this instance.
     * @param entityId3 The third entity ID for this instance.
     * @param entityId4 The fourth entity ID for this instance.
     * @param entityId5 The fifth entity ID for this instance.
     * @param entityId6 The sixth entity ID for this instance.
     * @param entityId7 The seventh entity ID for this instance.
     * @param entityId8 The eighth entity ID for this instance.
     * @param entityId9 The ninth entity ID for this instance.
     * @param entityId10 The tenth entity ID for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long   entityId1,
                                 Long   entityId2,
                                 Long   entityId3,
                                 Long   entityId4,
                                 Long   entityId5,
                                 Long   entityId6,
                                 Long   entityId7,
                                 Long   entityId8,
                                 Long   entityId9,
                                 Long   entityId10)
        throws NullPointerException
    {
        return new SzEntityIds(
            entityId1, entityId2, entityId3, entityId4, entityId5,
            entityId6, entityId7, entityId8, entityId9, entityId10);
    }

    /**
     * Constructs an instance with a variable-argument array of
     * {@link Long} entity ID instances.  This static fatory method
     * is called when an array is specified or eleven or more variable
     * argument parameters are provided.  If any of the specified
     * {@link Long} entity ID instances are duplicated, then the
     * duplicates will be ignored.
     * <p>
     * Unlike the {@link #SzEntityIds(Long...)} constructor, this static
     * factory method allows the parameter to be <code>null</code>,
     * resulting in a <code>null</code> return value.
     * 
     * @param entityIds The zero or more {@link Long} entity ID 
     *                  instances for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}, or
     *         <code>null</code> if the specified array is <code>null</code>.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(Long... entityIds)
        throws NullPointerException
    {
        if (entityIds == null) return null;
        return new SzEntityIds(entityIds);
    }

    /**
     * Constructs an instance with a variable-argument array of
     * <code>long</code> entity ID instances.  This static fatory method
     * is called when an array is specified or eleven or more variable
     * argument parameters are provided.  If any of the specified
     * <code>long</code> entity ID instances are duplicated, then the
     * duplicates will be ignored.
     * <p>
     * Unlike the {@link #SzEntityIds(long...)} constructor, this static
     * factory method allows the parameter to be <code>null</code>,
     * resulting in a <code>null</code> return value.
     * 
     * @param entityIds The zero or more {@link Long} entity ID 
     *                  instances for this instance.
     * 
     * @return The newly constructed instance of {@link SzEntityIds}, or
     *         <code>null</code> if the specified array is <code>null</code>.
     * 
     * @throws NullPointerException If any of the specified parameters
     *                              is <code>null</code>.
     */
    public static SzEntityIds of(long... entityIds)
        throws NullPointerException
    {
        if (entityIds == null) return null;
        Long[] array = new Long[entityIds.length];
        for (int index = 0; index < entityIds.length; index++) {
            array[index] = entityIds[index];
        }
        return SzEntityIds.of(array);
    }

    /**
     * Creates a new {@link Builder} for creating an instance of
     * {@link SzEntityIds}.
     * 
     * @return A new {@link Builder} for creating an instance of
     *         {@link SzEntityIds}.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override 
    public boolean contains(Object element) {
        return this.ids.contains(element);
    }
    
    @Override
    public boolean containsAll(Collection<?> elements) {
        return this.ids.containsAll(elements);
    }
    
    @Override
    public Iterator<Long> iterator() {
        return this.ids.iterator();
    }

    @Override
    public int size() {
        return this.ids.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String prefix = " ";
        for (Long id : this.ids) {
            sb.append(prefix);
            sb.append(id);
            prefix = ", ";
        }
        sb.append(" }");
        return sb.toString();
    }
}
