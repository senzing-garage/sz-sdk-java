# Senzing Java SDK (sz-sdk-java) - Claude Code Guide

## Project Overview

The Senzing Java SDK (`com.senzing:sz-sdk`) provides a Java interface to the Senzing Entity Resolution engine via JNI. Version 4.3.0, targeting Java 17+, licensed under Apache 2.0.

**Repository:** senzing-garage/sz-sdk-java

---

## Build System (Maven)

### Quick Reference Commands

```bash
# Standard build (skip tests)
mvn clean package -DskipTests=true

# Build with tests (requires Senzing runtime + database)
mvn clean install

# Checkstyle only
mvn clean validate -Pcheckstyle

# SpotBugs only (includes findsecbugs)
mvn clean compile -Pspotbugs

# JaCoCo coverage
mvn clean test -Pjacoco

# Full analysis (all three profiles)
mvn clean install -Pcheckstyle,jacoco,spotbugs \
  -Djacoco.haltOnFailure=false -Djacoco.ignoreFailure=true

# Javadoc generation
mvn javadoc:javadoc
```

### Build Profiles

| Profile | Activation | Purpose |
|---------|-----------|---------|
| `checkstyle` | `-Pcheckstyle` | Code style enforcement (validate phase) |
| `spotbugs` | `-Pspotbugs` | Static analysis + findsecbugs v1.14.0 (validate phase) |
| `jacoco` | `-Pjacoco` | Code coverage (CI requires 80% overall, 80% changed files) |
| `release` | `-Prelease` | GPG signing for Maven Central deployment |
| `java-17` | Auto (JDK 17) | Strips `@snippet` tags from javadoc |
| `java-18+` | Auto (JDK 18+) | Enables snippet paths for javadoc |

### Key Plugin Versions

- maven-compiler-plugin: 3.15.0 (release=17, `-Xlint:unchecked,deprecation`)
- maven-checkstyle-plugin: 3.6.0 (suppressions: `checkstyle-suppressions.xml`)
- spotbugs-maven-plugin: 4.9.8.2 (+ findsecbugs-plugin 1.14.0)
- jacoco-maven-plugin: 0.8.14
- maven-surefire-plugin: 3.5.4 (parallel test classes, includes `**/*Test.java`, `**/*Demo.java`)
- maven-javadoc-plugin: 3.12.0

### Checkstyle Suppressions

Globally suppressed: LeftCurly, LineLength, FileLength, FinalParameters, RegexpSingleline, AvoidStarImport, HiddenField, ParameterNumber, MagicNumber. MethodLength suppressed for `SzExceptionMapper.java` and `InstallUtilities.java`.

### Runtime Environment

```bash
# macOS
export DYLD_LIBRARY_PATH=$HOME/senzing/er/lib:/opt/homebrew/lib:$DYLD_LIBRARY_PATH

# Linux
export LD_LIBRARY_PATH=/opt/senzing/er/lib:$LD_LIBRARY_PATH
```

### Test Configuration

Tests run with JUnit 5 parallel execution (classes concurrent, methods same-thread). Surefire uses a generated `java-wrapper` script. All dependencies are test-scoped (senzing-commons 4.0.0-beta.3.0, JUnit Jupiter 6.0.3, etc.).

---

## Project Structure

```
src/main/java/com/senzing/
  sdk/                          # Public API (interfaces, exceptions, flags)
    SzEnvironment.java          # Environment lifecycle interface
    SzEngine.java               # Entity resolution operations
    SzProduct.java              # Product info (license, version)
    SzConfig.java               # Configuration manipulation
    SzConfigManager.java        # Configuration registry management
    SzDiagnostic.java           # Repository diagnostics
    SzFlag.java                 # Flag enum (45 constants)
    SzFlags.java                # Pre-defined Set<SzFlag> constants
    SzFlagUsageGroup.java       # Flag usage group classifications
    SzRecordKey.java            # Record identifier (dataSource + recordId)
    Sz*Exception.java           # Exception hierarchy (15 classes)
  sdk/core/                     # JNI-backed implementations
    SzCoreEnvironment.java      # Hybrid singleton environment
    SzCoreEngine.java           # Engine implementation
    SzCoreProduct.java          # Product implementation
    SzCoreConfig.java           # Config implementation
    SzCoreConfigManager.java    # ConfigManager implementation
    SzCoreDiagnostic.java       # Diagnostic implementation
    Native*.java                # JNI wrapper interfaces
    SzExceptionMapper.java      # Error code to exception mapping
    InstallUtilities.java       # Platform-specific library loading
src/test/java/com/senzing/      # Tests (unit + integration)
src/demo/java/com/senzing/sdk/  # Demo programs for each SDK module
```

---

## Architecture

### SzEnvironment Interface

Factory interface for obtaining initialized Senzing SDK singletons. Methods:

- `getProduct()` -> `SzProduct`
- `getEngine()` -> `SzEngine`
- `getConfigManager()` -> `SzConfigManager`
- `getDiagnostic()` -> `SzDiagnostic`
- `getActiveConfigId()` -> `long`
- `reinitialize(long configId)` -> `void`
- `destroy()` -> `void`
- `isDestroyed()` -> `boolean`

All getters throw `SzEnvironmentDestroyedException` if the environment has been destroyed.

### SzCoreEnvironment - Hybrid Singleton Pattern

**Only one active `SzCoreEnvironment` instance may exist per process at any time.** Creating a second instance while one is active throws `IllegalStateException`.

#### Lifecycle State Machine

```
ACTIVE --> DESTROYING --> DESTROYED
```

- **ACTIVE**: Ready for operations. Multiple concurrent read operations allowed.
- **DESTROYING**: `destroy()` called; blocks until all in-flight operations complete.
- **DESTROYED**: Fully cleaned up. A new instance can now be created.

#### Singleton Enforcement

```java
// Static class-level monitor ensures only one active instance
private static final Object CLASS_MONITOR = new Object();
private static SzCoreEnvironment currentInstance = null;
```

The constructor (called from `Builder.build()`) synchronizes on `CLASS_MONITOR`, checks `getActiveInstance() == null`, and registers itself as `currentInstance`. If another active instance exists, it throws `IllegalStateException`.

#### Builder Pattern

```java
SzEnvironment env = SzCoreEnvironment.newBuilder()
    .instanceName("MyApp")               // default: "Senzing Instance"
    .settings(settingsJson)              // default: "{ }" (limited functionality)
    .verboseLogging(true)                // default: false
    .configId(12345L)                    // default: null (auto-detect)
    .build();
```

**Default settings `"{ }"` limitation:** Only provides full `SzProduct` access and limited `SzConfigManager`. No `SzEngine` or `SzDiagnostic` (requires database).

#### Concurrency Model

- **Read/Write Lock**: Operations acquire read locks (shared, concurrent). `destroy()` acquires write lock (exclusive, blocks until all reads release).
- **Executing Count**: Tracks in-flight operations. `destroy()` waits for count to reach 0.
- **Lazy Initialization**: SDK modules created on first `getX()` call, within `execute()` wrapper.

#### Destruction Sequence

1. Transition state to `DESTROYING`
2. Acquire exclusive write lock (blocks until in-flight ops complete)
3. Destroy modules: Engine -> Diagnostic -> ConfigManager -> Product
4. Transition state to `DESTROYED`

---

### SDK Service Interfaces

#### SzProduct

Simplest interface - product metadata only:
- `getLicense()` -> JSON with license details
- `getVersion()` -> JSON with version details

#### SzEngine

Primary entity resolution interface (~49 methods with overloads). Key operations:

| Category | Methods |
|----------|---------|
| **Record CRUD** | `addRecord`, `deleteRecord`, `getRecord` |
| **Preview** | `getRecordPreview` |
| **Entity Retrieval** | `getEntity` (by ID or record key) |
| **Entity Resolution** | `reevaluateRecord`, `reevaluateEntity` |
| **Search** | `searchByAttributes` (with optional search profile) |
| **Relationship Analysis** | `findPath`, `findNetwork`, `findInterestingEntities` |
| **Why Analysis** | `whyEntities`, `whyRecords`, `whyRecordInEntity`, `whySearch` |
| **How Analysis** | `howEntity` |
| **Virtual Entity** | `getVirtualEntity` |
| **Export** | `exportJsonEntityReport`, `exportCsvEntityReport`, `fetchNext`, `closeExportReport` |
| **Redo** | `getRedoRecord`, `countRedoRecords`, `processRedoRecord` |
| **Utility** | `primeEngine`, `getStats` |

Most methods have overloads: full (with `Set<SzFlag>`) and convenience (using default flags).

#### SzConfig

In-memory configuration manipulation:
- `export()` -> JSON config definition
- `getDataSourceRegistry()` -> JSON data sources
- `registerDataSource(String dataSourceCode)` -> JSON result
- `unregisterDataSource(String dataSourceCode)` -> void

Changes are in-memory only. Must export and register via `SzConfigManager` to persist.

#### SzConfigManager

Configuration registry (persistent storage):
- `createConfig()` / `createConfig(String)` / `createConfig(long)` -> `SzConfig`
- `registerConfig(String definition, String comment)` -> config ID
- `getConfigRegistry()` -> JSON registry
- `getDefaultConfigId()` -> config ID (0 if unset)
- `setDefaultConfigId(long)` / `replaceDefaultConfigId(long current, long new)` -> void
- `setDefaultConfig(String definition, String comment)` -> config ID (register + set default)

`replaceDefaultConfigId` is atomic to prevent race conditions.

#### SzDiagnostic

Repository diagnostics:
- `getRepositoryInfo()` -> JSON repository overview
- `checkRepositoryPerformance(int seconds)` -> JSON performance results
- `purgeRepository()` -> **DESTRUCTIVE**: deletes all data except configuration
- `getFeature(long featureId)` -> experimental, internal use

### Core Implementation Pattern

All `SzCore*` implementations:
1. Hold reference to parent `SzCoreEnvironment`
2. Hold reference to `Native*Jni` instance
3. Initialize via `env.execute()` (lifecycle-safe)
4. All operations execute through `env.execute(Callable<T>)` for thread safety
5. Native return codes checked via `env.handleReturnCode()`
6. Flags filtered with `SDK_FLAG_MASK = ~(SzFlags.SZ_WITH_INFO)` before passing to native layer

---

## Exception Hierarchy

```
Exception
  SzException (base, with optional errorCode)
    SzBadInputException
    SzConfigurationException
    SzDatabaseException
      SzDatabaseConnectionLostException
      SzDatabaseTransientException
    SzLicenseException
    SzNotFoundException
    SzNotInitializedException
    SzReplaceConflictException
    SzRetryableException
      SzRetryTimeoutExceededException
    SzUnhandledException
    SzUnknownDataSourceException
    SzUnrecoverableException

RuntimeException > IllegalStateException
  SzEnvironmentDestroyedException (NOT an SzException)
```

---

## SzFlag System

### SzFlag Enum

45 enum constants, each with a `long` bitmask value (`1L << n`) and assigned to usage groups via `SzFlagUsageGroup`.

### SzFlagUsageGroup Enum

19 groups mapping flags to SDK operations: `SZ_ADD_RECORD_FLAGS`, `SZ_DELETE_RECORD_FLAGS`, `SZ_ENTITY_FLAGS`, `SZ_SEARCH_FLAGS`, `SZ_EXPORT_FLAGS`, `SZ_FIND_PATH_FLAGS`, `SZ_FIND_NETWORK_FLAGS`, `SZ_WHY_RECORDS_FLAGS`, `SZ_WHY_ENTITIES_FLAGS`, `SZ_HOW_FLAGS`, `SZ_VIRTUAL_ENTITY_FLAGS`, etc.

### Key Flag Categories

| Category | Flags |
|----------|-------|
| **Modification** | `SZ_WITH_INFO` (produces INFO document on add/delete/reevaluate) |
| **Export Inclusion** | `SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES`, `_POSSIBLY_SAME`, `_POSSIBLY_RELATED`, `_NAME_ONLY`, `_DISCLOSED`, `_SINGLE_RECORD_ENTITIES` |
| **Entity Relations** | `SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS`, `_POSSIBLY_RELATED_RELATIONS`, `_NAME_ONLY_RELATIONS`, `_DISCLOSED_RELATIONS` |
| **Entity Data** | `SZ_ENTITY_INCLUDE_ALL_FEATURES`, `_REPRESENTATIVE_FEATURES`, `_ENTITY_NAME`, `_RECORD_SUMMARY`, `_RECORD_TYPES`, `_RECORD_DATA`, `_RECORD_MATCHING_INFO`, `_RECORD_DATES`, `_RECORD_JSON_DATA`, `_RECORD_UNMAPPED_DATA`, `_RECORD_FEATURES`, `_RECORD_FEATURE_DETAILS`, `_RECORD_FEATURE_STATS`, `_INTERNAL_FEATURES`, `_FEATURE_STATS` |
| **Related Entity Data** | `SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME`, `_RELATED_MATCHING_INFO`, `_RELATED_RECORD_SUMMARY`, `_RELATED_RECORD_TYPES`, `_RELATED_RECORD_DATA` |
| **Analysis** | `SZ_INCLUDE_MATCH_KEY_DETAILS`, `SZ_INCLUDE_FEATURE_SCORES`, `SZ_INCLUDE_FEATURE_HASHES` |
| **Path Finding** | `SZ_FIND_PATH_STRICT_AVOID`, `SZ_FIND_PATH_INCLUDE_MATCHING_INFO`, `SZ_FIND_NETWORK_INCLUDE_MATCHING_INFO` |
| **Search** | `SZ_SEARCH_INCLUDE_RESOLVED`, `_POSSIBLY_SAME`, `_POSSIBLY_RELATED`, `_NAME_ONLY`, `_ALL_CANDIDATES`, `_STATS`, `_REQUEST`, `_REQUEST_DETAILS` |

### Pre-defined `Set<SzFlag>` Constants (in `SzFlag` class)

#### Empty / Info Sets
- `SZ_NO_FLAGS` - empty set
- `SZ_WITH_INFO_FLAGS` - `{SZ_WITH_INFO}`

#### Default Flag Sets (commonly used)
- `SZ_ADD_RECORD_DEFAULT_FLAGS` = `SZ_NO_FLAGS`
- `SZ_DELETE_RECORD_DEFAULT_FLAGS` = `SZ_NO_FLAGS`
- `SZ_REEVALUATE_RECORD_DEFAULT_FLAGS` = `SZ_NO_FLAGS`
- `SZ_REEVALUATE_ENTITY_DEFAULT_FLAGS` = `SZ_NO_FLAGS`
- `SZ_REDO_DEFAULT_FLAGS` = `SZ_NO_FLAGS`
- `SZ_RECORD_DEFAULT_FLAGS` = `{SZ_ENTITY_INCLUDE_RECORD_JSON_DATA}`
- `SZ_RECORD_PREVIEW_DEFAULT_FLAGS` = `{SZ_ENTITY_INCLUDE_RECORD_FEATURE_DETAILS}`
- `SZ_FIND_INTERESTING_ENTITIES_DEFAULT_FLAGS` = `SZ_NO_FLAGS`

#### Entity Flag Compositions
- `SZ_ENTITY_INCLUDE_ALL_RELATIONS` = all 4 relation type flags
- `SZ_ENTITY_CORE_FLAGS` = representative features + name + record summary + record data + matching info
- `SZ_ENTITY_DEFAULT_FLAGS` = core + all relations + related entity name/summary/matching
- `SZ_ENTITY_BRIEF_DEFAULT_FLAGS` = all relations + record matching + related matching
- `SZ_ENTITY_ALL_FLAGS` = all 25 entity-applicable flags

#### Path/Network Defaults
- `SZ_FIND_PATH_DEFAULT_FLAGS` = matching info + entity name + record summary
- `SZ_FIND_NETWORK_DEFAULT_FLAGS` = matching info + entity name + record summary

#### Why/How Defaults
- `SZ_WHY_RECORDS_DEFAULT_FLAGS` = `{SZ_INCLUDE_FEATURE_SCORES}`
- `SZ_WHY_ENTITIES_DEFAULT_FLAGS` = `{SZ_INCLUDE_FEATURE_SCORES}`
- `SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS` = `{SZ_INCLUDE_FEATURE_SCORES}`
- `SZ_WHY_SEARCH_DEFAULT_FLAGS` = feature scores + request details + stats
- `SZ_HOW_ENTITY_DEFAULT_FLAGS` = `{SZ_INCLUDE_FEATURE_SCORES}`
- `SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS` = `SZ_ENTITY_CORE_FLAGS`

#### Search Defaults
- `SZ_SEARCH_BY_ATTRIBUTES_ALL` = all entity types + stats + representative features + name + summary + scores
- `SZ_SEARCH_BY_ATTRIBUTES_STRONG` = resolved + possibly same (no name-only/possibly-related)
- `SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL` = all entity types + stats only
- `SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG` = resolved + possibly same + stats only
- `SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS` = alias for `SZ_SEARCH_BY_ATTRIBUTES_ALL`

#### Export Defaults
- `SZ_EXPORT_INCLUDE_ALL_ENTITIES` = multi-record + single-record
- `SZ_EXPORT_INCLUDE_ALL_HAVING_RELATIONSHIPS` = possibly same/related + name only + disclosed
- `SZ_EXPORT_DEFAULT_FLAGS` = all entities + entity default flags

#### All-Flags Sets (maximum detail)
- `SZ_*_ALL_FLAGS` variants exist for each operation type

### Flag Design Notes

- Flags use bitwise positions (`1L << n`), allowing combination via bitwise OR
- Some flags share the same bit value across different usage contexts (e.g., `SZ_SEARCH_INCLUDE_RESOLVED` and `SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES` both = `1L << 0`)
- All pre-defined sets are unmodifiable (`Collections.unmodifiableSet`)
- `SzFlag.toLong(Set<SzFlag>)` converts a flag set to its `long` bitmask
- `SzCoreEngine` masks out `SZ_WITH_INFO` before passing flags downstream (`SDK_FLAG_MASK`)

---

## CI/CD

- **Linux** (`maven-linux.yaml`): Java 17/21, checkstyle + spotbugs + jacoco, Senzing production-v4/staging-v4
- **macOS** (`maven-darwin.yaml`): Java 17/21, jacoco only, adds `/opt/homebrew/lib` to `DYLD_LIBRARY_PATH`
- **Windows** (`maven-windows.yaml`): Java 17/21, jacoco only
- Coverage threshold: 80% overall, 80% on changed files (via `madrapps/jacoco-report`)

---

## Key Conventions

- All public API interfaces are in `com.senzing.sdk`; implementations in `com.senzing.sdk.core`
- Core implementations are package-private (not public)
- All SDK operations route through `SzCoreEnvironment.execute(Callable)` for thread safety
- Native return codes are always checked via `handleReturnCode()`
- `SzConfig` changes are in-memory; must register via `SzConfigManager` to persist
- Demo programs in `src/demo/java` serve as code examples and are included in test execution
- The JAR's main class is `InstallUtilities` (for library installation support)
