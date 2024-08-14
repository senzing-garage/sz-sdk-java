# sz-sdk-java

If you are beginning your journey with [Senzing],
please start with [Senzing Quick Start guides].

## Overview

The Senzing Java SDK provides the Java interface to the native Senzing SDK's.
This repository is dependent on the Senzing native shared library (`.so` or
`.dll`) that is part of the Senzing product and function without it.

While this SDK is being made available as open source the actual `sz-sdk.jar`
file that you use should be obtained from Senzing product installation to
ensure that the Java code version matches the native library version.  

### Building

To build simply execute:

```console
mvn install
```

The JAR file will be contained in the `target` directory under the
name `sz-sdk.jar`.

[Senzing]: https://senzing.com/
[Senzing Garage]: https://github.com/senzing-garage
[Senzing Quick Start guides]: https://docs.senzing.com/quickstart/
