# Full-stack webshop

A compact, production-shaped webshop portfolio project. It uses a Java/Spring Boot API, a React/TypeScript client, and a real MySQL database. Docker Compose starts the complete stack with seeded demo data; no cloud account or paid subscription is required.

## What it demonstrates

- Java 17 and Spring Boot REST API
- Spring Data JPA persistence and Flyway database migrations
- MySQL 8 with health checks and a persistent Docker volume
- React 19 and strict TypeScript
- Responsive product catalog, search, tag filters, cart and checkout flow
- Bean Validation, centralized JSON error responses and CORS configuration
- Multi-stage Docker builds and Nginx reverse proxying
- H2 unit/integration tests plus a MySQL CI test job
- GitHub Actions checks for both backend and frontend

## Architecture

```text
Browser :5173
  └─ Nginx + React/TypeScript
       └─ /api/* → Spring Boot :8080
                       └─ JDBC/JPA → MySQL :3306 (internal only)
```

MySQL is deliberately not exposed to the host in the full-stack configuration. The backend reaches it through Compose's internal network. Flyway creates the schema, Hibernate validates it, and the `demo` profile inserts a customer and five products.

## Run on Windows with Docker Desktop

Requirements: Docker Desktop with the Linux engine running.

```powershell
git clone https://github.com/osman-mo/webshop-backend-java.git
cd webshop-backend-java
Copy-Item .env.example .env
docker compose up --build --wait
```

Or use the helper, which creates `.env` if it is missing and shows logs on failure:

```powershell
.\scripts\start.ps1
```

Open [http://localhost:5173](http://localhost:5173). The API health endpoint is [http://localhost:8080/api/actuator/health](http://localhost:8080/api/actuator/health).

Stop the stack without deleting its database:

```powershell
.\scripts\stop.ps1
```

To intentionally remove the local database volume as well:

```powershell
docker compose down --volumes
```

## Local development

The default backend connection is:

```text
jdbc:mysql://localhost:3306/webshop
username: webshop
password: value of DB_PASSWORD
```

Run the backend against a local MySQL instance:

```powershell
$env:DB_PASSWORD = "your-local-password"
.\gradlew.bat bootRun --args="--spring.profiles.active=demo"
```

Run the frontend in a second terminal:

```powershell
cd frontend
npm install
npm run dev
```

Vite proxies `/api` to the Java application on port 8080.

## Test and build

```powershell
.\gradlew.bat test
cd frontend
npm install
npm run lint
npm run build
```

The backend integration test persists a product, customer order, and order position, then loads the cart in a later HTTP request. CI runs it against both H2 in MySQL compatibility mode and a real MySQL 8 service.

## Main endpoints

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/api/products` | List products; optional `?tag=` filter |
| `GET` | `/api/products/{id}` | Get one product |
| `POST` | `/api/products` | Create a validated product |
| `DELETE` | `/api/products/{id}` | Delete a product |
| `GET` | `/api/customers/{id}` | Get a customer |
| `GET` | `/api/customers/{id}/shoppingcart` | Load all positions in NEW orders |
| `POST` | `/api/orders` | Create an order |
| `POST` | `/api/orders/{id}/positions` | Add a product and quantity |
| `POST` | `/api/orders/{id}/checkout` | Confirm an order |

## Configuration

| Variable | Default / Compose value | Meaning |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/webshop` | JDBC connection URL |
| `DB_USERNAME` | `webshop` | Database user |
| `DB_PASSWORD` | required | Database password; stored only in local `.env` |
| `SPRING_PROFILES_ACTIVE` | Compose uses `demo` | Enables demo records |
| `CORS_ALLOWED_ORIGIN` | `http://localhost:5173` | Allowed frontend development origin |

This is a software-development portfolio project. It contains no machine-learning functionality and makes no claim of a cloud deployment that was not performed.
