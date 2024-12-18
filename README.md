# sz-sdk-java

If you are beginning your journey with [Senzing],
please start with [Senzing Quick Start guides].

## Overview

The Senzing Java SDK provides the Java interface to the native Senzing SDK's.
This repository is dependent on the Senzing native shared library (`.so`,
`.dylib` or `.dll`) that is part of the Senzing product and function without it.

While this SDK is being made available as open source the actual `sz-sdk.jar`
file that you use should be obtained from Senzing product installation to
ensure that the Java code version matches the native library version.  

### Prerequistes
1. Java OpenJDK 17 is required to build.  Later versions of Java may function, but have not been tested.
1. Apache Maven v3.8.5 or later
1. Senzing v4.0 or later (for running unit tests)
1. Set the `SENZING_DIR` environment variable:
    - Linux: `export SENZING_DIR=/opt/senzing/er`
    - macOS: `export SENZING_DIR=/Library/Senzing/er`
    - Windows: `set SENZING_DIR=C:\Senzing\er`
1. Set your library path appropriately for Senzing libraries:
    - Linux: Set the `LD_LIBRARY_PATH`:
        ```
        export LD_LIBRARY_PATH=/opt/senzing/er/lib:$LD_LIBRARY_PATH
        ```
    - macOS: Set `DYLD_LIBRARY_PATH`:
        ```
        export DYLD_LIBRARY_PATH=/Library/Senzing/er/lib:/Library/Senzing/er/lib/macOS:$DYLD_LIBRARY_PATH
        ```
    - Windows: Set `Path`:
        ```
        set Path=C:\Senzing\er\lib;C:\Senzing\er\lib\windows;%Path%
        ```

### Building

1. To build simply execute:

    ```console
    mvn package
    ```
    - The JAR file will be located at `target/sz-sdk.jar`.
    - The Javadoc JAR file will be located at `target/sz-sdk-javadoc.jar`.

1. To build and install in the local maven repository:
    ```console
    mvn install
    ```
    - The JAR file will be located at `target/sz-sdk.jar`.
    - The Javadoc JAR file will be located at `target/sz-sdk-javadoc.jar`.

1. To build **without** running unit tests:
    ```console
    mvn -DskipTests=true package
    ```

1. Clean up build artifacts:
    ```console
    mvn clean
    ```

The JAR file will be located at `target/sz-sdk.jar`.

The Javadoc JAR file will be located at `target/sz-sdk-javadoc.jar`

[Senzing]: https://senzing.com/
[Senzing Garage]: https://github.com/senzing-garage
[Senzing Quick Start guides]: https://docs.senzing.com/quickstart/
