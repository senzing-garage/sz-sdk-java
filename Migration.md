# Migrating from V3 to V4 Senzing SDK

## Overview

Version 4.0 of the Senzing Java SDK ([`sz-sdk-java`]) is a significant
change from the Senzing G2 Java SDK v3.x ([`g2-sdk-java`]).  While the
functionality is largely the same, the semantics differ significantly
in ways that should make development much more straightforward and
natural for Java developers.

## The G2 Naming and Prefix Retired

The "G2" naming and prefix has been retired.  Its replacement depends on the usage as given below.

1. The `G2` prefix as in the class name `G2Engine` is replaced with `Sz` as in `SzEngine`.  For example:
    - `G2Engine` -> `SzEngine`
    - `G2Product` -> `SzProduct`
    - `G2Config` -> `SzConfig`
    - `G2ConfigMgr` -> `SzConfigManager`
    - `G2Diagnostic` -> `SzDiagnostic`

1. The `com.senzing.g2.engine` package name has been replaced with `com.senzing.sdk` and `com.senzing.sdk.core`.  Previously, you would have used the following import statements:

    ```java
    import com.senzing.g2.engine.*;
    ```

    Replace this with:

    ```java
    import com.senzing.sdk.*;
    import com.senzing.sdk.core.SzCoreEnvironment;
    ```

1. The `g2` directory in `[SENZING_PATH]/g2/lib` (e.g.: `/opt/senzing/g2/lib`) has been renamed to `er` as in `[SENZING_PATH]/er/lib` (e.g.: `/opt/senzing/er/lib`).  This will require that your native library path settings be updated as follows:
    - Linux:

        ```console
        export LD_LIBRARY_PATH=$SENZING_PATH/er/lib
        ```

    - macOS:

        ```console
        export DYLD_LIBRARY_PATH=$SENZING_PATH/er/lib:$DYLD_LIBRARY_PATH$SENZING_PATH/er/lib/macos:$DYLD_LIBRARY_PATH
        ```

    - Windows:

        ```console
        set Path=%SENZING_PATH%\er\lib;%Path%
        ```

1. The name of the JAR file containing the Java SDK classes has been changed from `g2.jar` to `sz-sdk.jar`.  Further, it has been relocated from `[SENZING_PATH]/g2/lib/g2.jar` to `[SENZING_PATH]/er/sdk/java/sz-sdk.jar`.  Thus, your `pom.xml` file would change as follows:
    - Version 3.x `pom.xml` dependency definition:

        ```xml
        <dependency>
            <groupId>com.senzing</groupId>
            <artifactId>g2</artifactId>
            <version>3.12.6</version>
            <scope>system</scope>
            <systemPath>${SENZING_PATH}/g2/lib/g2.jar</systemPath>
        </dependency>
        ```

    - Version 4.x `pom.xml` dependency definition:

        ```xml
        <dependency>
            <groupId>com.senzing</groupId>
            <artifactId>sz-sdk</artifactId>
            <version>4.0.0</version>
            <scope>system</scope>
            <systemPath>${SENZING_PATH}/er/sdk/java/sz-sdk.jar</systemPath>
        </dependency>
        ```

## Exceptions Replace Return Codes

Previously, you would have to handle return codes from functions and check for a thread-local error code.  Error handling in Version 3.x might have looked like this:

```java
// add a record
int returnCode = engine.addRecord(dataSource, recordId, recordJson, loadId);

// check for an error
if (returnCode != 0) {
    String msg = "Senzing error [ " + engine.getLastExceptionCode()
        + " ]: " + engine.getLastException();
    logError(msg);
    throw new Exception(msg);
}
```

In the version 4.0 SDK, you would simply catch the exception thrown:

```java
try {
    // add a record
    engine.addRecord(SzRecordKey.of(dataSource, recordId), recordJson, SZ_NO_FLAGS);

} catch (SzException e) {
    // log the any Senzing exception
    logError(e.toString());
    throw e;
}
```

Further, an exception hierarchy has been introduced so you can respond differently to different types of exceptions.  For example:

```java
try {
    // add the record
    engine.addRecord(SzRecordKey.of(dataSource, recordId), recordJson, SZ_NO_FLAGS);

} catch (SzBadInputException e) {
    // log bad records found in the input file
    logBadRecord(dataSource, recordId, recordJson);

} catch (SzRetryableException e) {
    // queue retryable records to be tried again later
    queueRecordForRetry(dataSource, recordId, recordJson);

} catch (Exception e) {
    // trap any other exception (including SzException)
    logError(e.toString());
    throw e;
}
```

## Unified Initialization and Destruction

Previously, you would initialize and destroy each Senzing component individually.  For example, if you were using both the `G2Product` and `G2Engine` components, then your code might look like this:

```java
G2Product product = new G2ProductJNI();
G2Engine engine = new G2EngineJNI();

int returnCode = product.init(moduleName, settings, false);
if (returnCode != 0) {
    throw new Exception(
        "Senzing error [ " + product.getLastExceptionCode()
        + " ]: " + product.getLastException());
}

returnCode = engine.init(moduleName, settings, false);
if (returnCode != 0) {
    product.destroy();
    throw new Exception(
        "Senzing error [ " + engine.getLastExceptionCode()
        + " ]: " + engine.getLastException());
}

try {
    // do some work with the engine and product components
    ...
    
} finally {
    // destroy each component (typically in reverse order of init)
    engine.destroy();
    product.destroy();
}
```

With the version 4 SDK, you would initialize and destroy the `SzEnvironment`
and obtain the individual components from the `SzEnvironment`:

```java
SzEnvironment env = SzCoreEnvironment.newBuilder()
                                     .moduleName(moduleName)
                                     .settings(settings)
                                     .verbose(false)
                                     .build();

try {
    SzProduct product = env.getProduct();
    SzEngine engine = env.getEngine();

    // do some work with the engine and product components
    ...

} catch (SzException e) {
    handleError(e);

} finally {
    // destroy the environment
    env.destroy();
}
```

## Output Parameters Replaced By Return Values

In the version 3.x SDK the use of return codes for error handling precluded the
possibility of returning results directly.  As such many methods used the
`java.lang.StringBuffer` class or `com.senzing.g2.engine.Result` class as parameter
types to populate parameters with the results of the operation.

An operation that leveraged in `StringBuffer` in version 3.x might look like the following:

```java
StringBuffer sb = new StringBuffer();
int returnCode = engine.getEntityByEntityID(entityID, sb);
if (returnCode != 0) {
    throw new Exception(
        "Senzing error [ " + engine.getLastExceptionCode()
        + " ]: " + engine.getLastException());
}
String entityResult = sb.toString();

. . .
```

In version 4.0, the call above would look like this:

```java
try {
    String entityResult = engine.getEntity(entityId, SZ_ENTITY_DEFAULT_FLAGS);

    . . .

} catch (SzException e) {
    handleError(e);
}
```

A function call leveraging the `com.senzing.g2.engine.Result` class in version 3.x 
might look like:

```java
Result<Long> result = new Result<>();
int returnCode = engine.exportJSONEntityReport(SZ_EXPORT_DEFAULT_FLAGS, result);
if (returnCode != 0) {
    throw new Exception(
        "Senzing error [ " + engine.getLastExceptionCode()
        + " ]: " + engine.getLastException());
}
long exportHandle = result.getValue();
try {
    . . .

} finally {
    engine.closeExport(exportHandle);
}
```

The same function call using the version 4 SDK would look like the following:

```java
long exportHandle = 0;
try {
    exportHandle = engine.exportJsonEntityReport(SZ_EXPORT_DEFAULT_FLAGS);

    . . .

} catch (SzException e) {
    handleError(e);

} finally {
    if (exportHandle != 0L) {
        engine.closeExport(exportHandle);
    }
}
```

# UNDER CONSTRUCTION
## ITEMS BELOW THIS POINT ARE JUST NOTES

## TBD: Function Overloading
Rather than mirroring the function names from the Senzing native C SDK leading to
function variants with suffixes such as `V2` and `V3`, method overloading is now
employed in version 4.0 with the providing parameters determing the behavior.

## TBD: WithInfo Functions
The `WithInfo` variant functions no longer exist.  In their place, functions that
can product an "INFO" response now accept a `flags` parameter with one such flag indicating if "INFO" response is desired.

## TBD: Redo Processing
Processing redo records has changed....

## TBD: JSON-formatted Input Parameters
JSON Input Parameters (adding data source, specifying entity ID's or record keys) TBD

## TBD: Changes to Config & ConfigManager
Changes to Config and ConfigManager

### TBD: REFERENCE BREAKING CHANGES

### TBD: REFERENCE CODE SNIPPETS

### TBD: Table of method names

[`sz-sdk-java`]: https://github.com/senzing-garage/sz-sdk-java
[`g2-sdk-java`]: https://github.com/senzing-garage/g2-sdk-java



