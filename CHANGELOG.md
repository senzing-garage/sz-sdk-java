# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
[markdownlint](https://dlaa.me/markdownlint/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.1.0] - 2025-09-XX

### Changed in 4.1.0

- Added `SzConfigRetryable` annotation and applied it to appropriate
  SDK methods.
- Added `SzConfigRetryableTest` to test methods are properly
  annotated.

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
