# Migrating from `g2-sdk-java` to `sz-sdk-java`

If you are beginning your journey with [Senzing],
please start with [Senzing Quick Start guides].

## Overview

Version 4.0 of the Senzing Java SDK (`sz-sdk-java`) is a significant
change from [Senzing G2 Java SDK v3.x] (`g2-sdk-java`).  While the
functionality is largely the same, the semantics differ significantly
in ways that should make development much more straightforward and
natural for Java developers.

## Summary of Changes

1. The `G2` prefix/nomenclature has been replaced with `Sz`.
1. The `com.senzing.g2.engine` package has been retired and replaced with `com.senzing.sdk` (for base interfaces) and `com.sening.sdk.core` (for the "core" implementation).
1. The use of integer return codes to indicate errors has been replaced with exceptions.
1. Initialization and destruction of Senzing components has been unified into a single "environment" class.
1. Rather than mirroring the function names from the Senzing native C SDK leading to
function variants with suffixes such as `V2` and `V3`, method overloading is now
employed in version 4.0 with the providing parameters determing the behavior.
1. The `WithInfo` variant functions no longer exist.  In their place, functions that
can product an "INFO" response now accept a `flags` parameter with one such flag 
indicating if "INFO" response is desired.
1. The use of a `StringBuffer` to simulate an "out parameter" and return a response has been discontinued in favor of directly returning the JSON response.

### Migrating Imports

Previously, you would have used the following import statements:
```java
import com.senzing.g2.engine.*;
```

Replace this with:
```java
import com.senzing.sdk.*;
import com.senzing.sdk.core.SzCoreEnvironment;
```

### Migrating Error Handling

Previously, you would have to handle return codes and check for error codes and messages:

```java
int returnCode = engine.addRecord(dataSource, recordId, recordJson, loadId);
if (returnCode != 0) {
    String msg = "Senzing error [ " + engine.getLastExceptionCode()
        + " ]: " + engine.getLastException();
    logError(msg);
    throw new Exception(msg);
);
}
```

In the version 4 SDK, you would simply catch the exception thrown:

```java
try {
    engine.addRecord(SzRecordKey.of(dataSource, recordId), recordJson, SZ_NO_FLAGS);

} catch (SzException e) {
    logError(e.toString());
    throw e;
}
```

### Migrating Initialization and Destruction

Previously, you would initialize and destroy each Senzing component individually.  For example:

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



[Senzing]: https://senzing.com/
[Senzing Quick Start guides]: https://docs.senzing.com/quickstart/
[Senzing G2 Java SDK v3.x]: https://github.com/senzing-garage/g2-sdk-java

