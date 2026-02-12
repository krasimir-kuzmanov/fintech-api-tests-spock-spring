# fintech-api-tests-spock-spring

Spring + Spock API test harness project focused on test-time dependency injection and typed configuration.

## Scope

- No controllers, services, database, or application runtime.
- Everything is under `src/test`.
- Demonstrates:
  - Spring test context wiring via `@ContextConfiguration`
  - Bean injection into Spock specs
  - Environment/property-driven config

## Stack

- Groovy 4
- Spock 2
- Spring Framework (context + test)
- Gradle

## Project Structure

```text
src
└─ test
   ├─ groovy
   │  └─ com.example.fintech.spock
   │     ├─ config
   │     │  ├─ TestConfig.groovy
   │     │  └─ ApiProperties.groovy
   │     ├─ client
   │     │  └─ AuthClient.groovy
   │     └─ specs
   │        └─ AuthSpec.groovy
   └─ resources
      └─ application-test.properties
```

## Run

```bash
./gradlew clean test
```

## CI

GitHub Actions workflow is in `.github/workflows/ci.yml`.
It runs on pull requests and manual dispatch.
