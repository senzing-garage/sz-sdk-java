# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
[markdownlint](https://dlaa.me/markdownlint/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
