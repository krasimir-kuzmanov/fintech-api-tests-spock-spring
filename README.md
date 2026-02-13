# fintech-api-tests-spock-spring

Spring + Spock API test harness project focused on test-time dependency injection and typed configuration.

## Scope

- No controllers, services, database, or application runtime.
- Everything is under `src/test`.
- Demonstrates:
  - Spring test context wiring via `@ContextConfiguration`
  - Typed config binding from `fintech.api.*`
  - Bean injection into Spock specs
  - Environment/property-driven overrides
  - Real HTTP calls from injected clients

## Stack

- Groovy 4
- Spock 2.4 (stable line)
- Spring Framework (context + test)
- Java HttpClient
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

The auth spec is backend-facing and expects `fintech-backend` running with test support endpoint:

- `POST /test/reset`

## Config Overrides

Defaults come from `src/test/resources/application-test.properties`:

- `fintech.api.base-url=${API_BASE_URL:http://localhost:18080}`
- `fintech.api.timeout-ms=${API_TIMEOUT_MS:10000}`

You can override from command line:

```bash
./gradlew test -Dfintech.api.base-url=http://localhost:8080 -Dfintech.api.timeout-ms=5000
```

## CI

GitHub Actions workflow is in `.github/workflows/ci.yml`.
It runs on pull requests and manual dispatch.
