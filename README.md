# RunLater

RunLater is a Spring Boot delivery platform for scheduling reliable email and webhook notifications. Authenticated users can create future deliveries, observe their lifecycle, retry failures, and inspect every delivery attempt.

## What it demonstrates

- JWT-secured REST API with per-user job ownership
- PostgreSQL persistence and Flyway migrations
- A poller that atomically claims ready jobs before dispatching asynchronous workers
- Email and signed webhook channels
- Retry policy with exponential backoff and persisted attempt history
- Idempotency keys, cancellation, rescheduling, manual retry, and health metrics
- Docker Compose local development with PostgreSQL and MailHog

## Architecture

`REST API → jobs table → scheduled dispatcher → worker executor → email/webhook provider`

The dispatcher changes a due job to `PROCESSING` inside its transaction, then submits the worker **only after commit**. That prevents a subsequent polling cycle from delivering the same job twice. A failed attempt enters `RETRYING` with an exponential delay; exhausted jobs become `FAILED`.

## Run locally

Requirements: Java 21+ and Docker.

```bash
docker compose up -d
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`; MailHog is available at `http://localhost:8025`.

Copy `.env.example` if you need to set environment variables for another PostgreSQL or SMTP provider. Never commit secrets. The previous local configuration, if present, is retained as the ignored `application.properties.bak` backup so it cannot override the environment-based v2 settings.

## API overview

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/signUp` | Create an account |
| `POST` | `/logIn` | Get a JWT |
| `POST` | `/api/v1/jobs/email` | Schedule one or more emails |
| `POST` | `/api/v1/jobs/webhook` | Schedule a JSON webhook |
| `GET` | `/api/v1/jobs` | List the current user's jobs |
| `GET` | `/api/v1/jobs/{id}` | Get a job and attempt history |
| `POST` | `/api/v1/jobs/{id}/cancel` | Cancel a queued job |
| `POST` | `/api/v1/jobs/{id}/retry` | Requeue a failed/cancelled job |
| `PATCH` | `/api/v1/jobs/{id}/schedule?scheduledAt={ISO-8601}` | Reschedule a queued job |

Send `Authorization: Bearer <token>` to every job endpoint. The create endpoints accept an optional `Idempotency-Key` header.

Example email request:

```json
{
  "maxAttempts": 3,
  "scheduledAt": "2026-11-01T09:00:00Z",
  "from": "notifications@runlater.local",
  "to": ["recipient@example.com"],
  "subject": "Your scheduled reminder",
  "body": "This was delivered by RunLater.",
  "contentType": "text/plain"
}
```

## Test

```bash
./mvnw test
```

Tests use an isolated in-memory H2 database; running the application uses PostgreSQL.

## Frontend

The companion Next.js dashboard is in [`../RunLaterFE`](../RunLaterFE). It supports account creation, sign-in, scheduling email/webhook jobs, and managing the job lifecycle.
