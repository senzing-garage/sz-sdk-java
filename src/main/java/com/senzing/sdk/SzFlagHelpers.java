package com.senzing.sdk;

import java.util.Set;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Contains constants used as placeholders to aid in the 
 * initialization of the {@link SzFlag} class.
 */
final class SzFlagHelpers {
    /**
     * Private constructor.
     */
    private SzFlagHelpers() {
        // do nothing
    }

    /**
     * The package-private <b>unmodifiable</b> {@link Set} to use for {@link SzFlag}
     * instances that can be used for all usage groups.
     */
    static final Set<SzFlagUsageGroup> SZ_ALL_GROUPS_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} to use for {@link SzFlag}
     * instances that can only be used for "modify" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_MODIFY_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_ENTITY_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_ENTITY_SET
        = Collections.unmodifiableSet(new TreeSet<>());

   /**
    * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
    * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_ENTITY_HOW_SET}
    * to help with circular dependencies during initialization.
    */
   static final Set<SzFlagUsageGroup> SZ_ENTITY_HOW_SET
       = Collections.unmodifiableSet(new TreeSet<>());

   /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_RELATION_SET}
     * to help with circular dependencies during initialization.
    */
    static final Set<SzFlagUsageGroup> SZ_RELATION_SET
        = Collections.unmodifiableSet(new TreeSet<>());

    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_ENTITY_RECORD_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_ENTITY_RECORD_SET
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_PREPROCESS_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_PREPROCESS_SET
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_HOW_WHY_SEARCH_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_HOW_WHY_SEARCH_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
    
    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_SEARCH_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_SEARCH_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_WHY_SEARCH_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_WHY_SEARCH_SET 
        = Collections.unmodifiableSet(new TreeSet<>());

    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_EXPORT_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_EXPORT_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_FIND_PATH_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_FIND_PATH_SET
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> empty {@link Set} of {@link 
     * SzFlagUsageGroup} that proxies for {@link SzFlagUsageGroup#SZ_FIND_NETWORK_SET}
     * to help with circular dependencies during initialization.
     */
    static final Set<SzFlagUsageGroup> SZ_FIND_NETWORK_SET
        = Collections.unmodifiableSet(new TreeSet<>());
}
