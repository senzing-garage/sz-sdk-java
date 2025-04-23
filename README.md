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

### Prerequisites

1. Java OpenJDK 17 (or later) is required to build.
1. Apache Maven v3.8.5 or later
1. Senzing v4.0 or later (for running unit tests)
1. Set the `SENZING_PATH` environment variable if not using the default locations below:
   - Linux: `export SENZING_PATH=/opt/senzing`
   - macOS: `export SENZING_PATH=$HOME/senzing`
   - Windows: `set SENZING_PATH=%USERPROFILE%\senzing`
1. Set your library path appropriately for Senzing libraries:

   - Linux: Set the `LD_LIBRARY_PATH`:

     ```console
     export LD_LIBRARY_PATH=/opt/senzing/er/lib:$LD_LIBRARY_PATH
     ```

   - macOS: Set `DYLD_LIBRARY_PATH`:

     ```console
     export DYLD_LIBRARY_PATH=$HOME/senzing/er/lib:$HOME/senzing/er/lib/macos:$DYLD_LIBRARY_PATH
     ```

   - Windows: Set `Path`:

     ```console
     set Path=%USERPROFILE%\senzing\er\lib;%Path%
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
[Senzing Quick Start guides]: https://docs.senzing.com/quickstart/
