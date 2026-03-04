# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
[markdownlint](https://dlaa.me/markdownlint/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.3.0] - 2026-03-03

### Changed in 4.3.0

- Changed macOS `DYLD_LIBRARY_PATH` to use `/opt/homebrew/lib` instead of
  `er/lib/macos` for finding `libssl3` and other Homebrew-installed libraries.

## [4.2.1] - 2026-02-19

### Changed in 4.2.1

- Standardized workflows for build-resources v4.
- Bumped `senzing-commons` from 4.0.0-beta.2.0 to 4.0.0-beta.2.1.
- Bumped `sqlite-jdbc` from 3.51.1.0 to 3.51.2.0.
- Bumped `maven-compiler-plugin`.
- Bumped `junit-jupiter` from 6.0.2 to 6.0.3.

## [4.2.0] - 2026-01-28

### Changed in 4.2.0

- Updated `SzExceptionMapper` with latest exception mappings.
- Fixed `testGetRecordPreview()` and minor updates to speed up other unit tests.
- Made `getRecordPreview()` unit tests tolerant of pre-4.2.0 behavior.
- Updated breaking changes link in `Migration.md`.
- Added support for bootstrap builds in `InstallUtilities` inference of the
  install SDK jar file path.
- Refactored `SzConfig.toString()` default implementation.
- Bumped `senzing-commons` from 4.0.0-beta.1.3 to 4.0.0-beta.2.0.
- Bumped `icu4j` from 77.1 to 78.2.
- Bumped `sqlite-jdbc` from 3.50.3.0 to 3.51.1.0.

## [4.1.0] - 2025-10-13

### Changed in 4.1.0

- Added `SzConfigRetryable` annotation and applied it to appropriate SDK methods.
- Added `SzConfigRetryableTest` to test methods are properly annotated.
- Fixed bugs in `SzConfigManager` initialization and error handling.
- Added `SzEnvironmentDestroyedException` so the condition of the `SzEnvironment`.
  being destroyed can be distinguished from other occurrences of `IllegalStateException`.
- Fixed bug which ignored flags in unit tests when using `StandardTestDataLoader`.
- Changed `sz-sdk.jar` to be executable to aid in performing `mvn install:install-file` operations.
- Added validation of `sz-sdk.jar` when initializing `SzCoreEnvironment` with warning message.

## [4.0.0] - 2025-08-11

### Changed in 4.0.0

- Updated documentation to include build-time generated example output.
- Applied changes from documentation review to API documentation.
- Updated error-code/exception mapping to handle new error codes for license.
- Updated unit tests to handle `advSearch` license feature.
- Refactored tests into reusable and specific packages using `senzing-commons`

## [4.0.0-beta.3.0] - 2025-04-30

### Changed in 4.0.0-beta.3.0

- Added compiled examples in Javadoc API documentation
- Modified `pom.xml` to build with Java 21 in addition to Java 17
- Refactored `SzConfig` and `SzConfigManager` so that `SzConfig` now
  represents a configuration and is subordinate to `SzConfigManager`.
- Added `whySearch()` functionality and updated flags
- Added `Migration.md` to describe migration from version 3.x.
- Updated `SzExceptionMapper` with latest exception mappings.

## [4.0.0-beta.2.0] - 2025-02-14

### Changed in 4.0.0-beta.2.0

- Added `SzDatabaseTransientException` class.
- Updated generated `SzExceptionMapper` class for mapping `SzDatabaseTransientException`

## [4.0.0-beta.1.1] - 2025-02-04

### Changed in 4.0.0-beta.1.1

- Changed versioning to match major version of Senzing 4.0 product with beta suffix.
- Made changes to return `null` when INFO is **not** requested.
- Patched `SzCoreEngine.reevaluateEntity()` to return place-holder `NoInfo` when
  INFO requested, but entity not found (pending fix to native engine function).
- Added new engine flags:
  - `SZ_SEARCH_INCLUDE_ALL_CANDIDATES`
  - `SZ_SEARCH_INCLUDE_REQUEST`
  - `SZ_SEARCH_INCLUDE_REQUEST_DETAILS`

## [0.9.1] - 2025-01-17

### Changed in 0.9.1

- Minor update for detection of the `CONFIGPATH` for auto tests
- Added full exception hierarchy:
  - `SzConfigurationException`
  - `SzRetryableException`
    - `SzDatabaseConnectionLostException`
    - `SzRetryTimeoutExceededException`
  - `SzUnrecoverableException`
    - `SzDatabaseException`
    - `SzLicenseException`
    - `SzNotInitializedException`
    - `SzUnhandledException`
- Added full error code to exception mapping from `szerrors.json`

## [0.9.0] - 2024-12-18

- Initial release in preparation for beta testing of Senzing 4.0
