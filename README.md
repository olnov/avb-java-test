#### AVB Microservices Sample App
This is a simple microservices application that utilises Java, Spring Boot, and Spring Cloud technology stack aimed at demonstrating communication between services in a cloud.

#### Microservices
 - user-service
 - company-service
 - api-gw
 - service-discovery

#### How to run

Set env parameters as epr description in .env.example
```
# PostgreSQL servers settings
USERS_DB_HOST=localhost
USERS_DB_NAME=avb_users
USERS_DB_USER=postgres
USERS_DB_PASS=password!1
USERS_DB_PORT=5440

COMPANIES_DB_HOST=localhost
COMPANIES_DB_NAME=avb_companies
COMPANIES_DB_USER=postgres
COMPANIES_DB_PASS=password!1
COMPANIES_DB_PORT=5441

# API server settings and dependencies URLs
USER_SERVICE_DATABASE_URL=postgresql://postgres:password!1@localhost:5440/avb_users
COMPANY_SERVICE_DATABASE_URL=postgresql://postgres:password!1@localhost:5440/avb_companies
```

Use docker compose to run the app:
```cmd
docker compose -f docker-compose.yml up
```

