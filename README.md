# fintech-api-tests-spock-spring

API test suite for Fintech backend services using Spock + Spring test context + Java HttpClient.

## Tech Stack
- Java 21 (Gradle toolchain)
- Groovy 4
- Spock 2.4
- Spring Test / Context
- Java HttpClient
- Gradle Wrapper

## Requirements
- JDK 21 installed
- SDKMAN available (project includes `.sdkmanrc`)
- No local Gradle install required (wrapper included)

The Gradle wrapper still needs a local JDK to run, so ensure `JAVA_HOME` points to JDK 21 (or at least JDK 17+).

## Quick Start
### 1) Start the Backend
```bash
cd ../fintech-backend
./gradlew bootRun
```

Backend default URL:
- `http://localhost:8080`

Health check:
- `http://localhost:8080/actuator/health`

### 2) Run the Spock Suite
From the `fintech-api-tests-spock-spring` project root:

```bash
sdk env
./gradlew clean test
```

Run a single spec:
```bash
./gradlew test --tests "com.example.fintech.spock.specs.PaymentFlowSpec"
```

## What the Suite Covers
- Auth happy path: register + login.
- Auth negatives: invalid login credentials, duplicate registration, invalid registration inputs.
- Account flow: funding and balance state validation.
- Payment flow: authenticated transfer with sender/receiver balance and transaction assertions.
- Payment negatives: insufficient funds, invalid amount formats, same-account transfer rejection.
- Security smoke: unauthenticated access (`401`) and cross-account access with wrong token (`403`).

## Configuration
Loaded from `src/test/resources/application-test.properties`.

Defaults:
- `fintech.api.base-url=${API_BASE_URL:http://localhost:8080}`
- `fintech.api.timeout-ms=${API_TIMEOUT_MS:10000}`

You can override with JVM properties:
```bash
./gradlew test -Dfintech.api.base-url=http://localhost:8080 -Dfintech.api.timeout-ms=5000
```

## Project Structure
- Spring test wiring and typed config: `src/test/groovy/com/example/fintech/spock/config`
- HTTP clients for backend endpoints: `src/test/groovy/com/example/fintech/spock/client`
- Spec classes: `src/test/groovy/com/example/fintech/spock/specs`
- Test properties: `src/test/resources/application-test.properties`

## CI
GitHub Actions workflow:
- `.github/workflows/ci.yml`

It runs on pull requests and manual dispatch.

## Notes
- Tests are backend-facing and use test support endpoint `POST /test/reset` for deterministic isolation.
- API response assertions are intentionally behavior-focused (status + key payload semantics).
