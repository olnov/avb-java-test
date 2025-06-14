#### AVB Microservices Sample App
This is a simple microservices application that utilises Java, Spring Boot, and Spring Cloud technology stack aimed at demonstrating communication between services in a cloud.

#### Microservices
 - user-service
 - company-service
 - api-gw
 - service-discovery

#### How to run

Set env parameters as epr description in .env.example
```dotenv
# PostgreSQL servers settings
USERS_DB_HOST=pg-users
USERS_DB_NAME=avb_users
USERS_DB_USER=postgres
USERS_DB_PASS=password!1
USERS_DB_PORT=5440

COMPANIES_DB_HOST=pg-companies
COMPANIES_DB_NAME=avb_companies
COMPANIES_DB_USER=postgres
COMPANIES_DB_PASS=password!1
COMPANIES_DB_PORT=5441

# User service settings
USER_SERVICE_APP_PORT=8081

# Company service settings
COMPANY_SERVICE_APP_PORT=8082

# Application gateway settings
GATEWAY_SERVICE_APP_PORT=8080

# Service discovery settings
DISCOVERY_SERVICE_APP_PORT=8761


# User service URL
USER_SERVICE_URL=http://user-service:8081

# Company service URL
COMPANY_SERVICE_URL=http://company-service:8082
```

Use docker compose to run the app:
```console
docker compose -f docker-compose.yml up
```
#### How to test
1. Check if your service discovery up and running - http://localhost:8761. It the Eureka dashboard you should see 3 regestired services: API-GW, USER-SERVICE, COMPANY-SERVICE
2. You can use SwaggerUI to test user and copmany services separatelly.
    - User service: http://localhost:8081/api/v1/swagger-ui/index.html
    - Company service: http://localhost:8082/api/v1/swagger-ui/index.html

## Code snippets for testing
 - Creating a new company. POST http://localhost:8080/api/v1/companies
 ```json
 {
  "companyName": "Microsoft",
  "budget": 1000.00,
}
```
 - Adding user to a company. POST http://localhost:8080/api/v1/users
 ```json
 {
  "firstName": "Bill",
  "lastName": "Gates",
  "phoneNumber": "+79222222222", <-- Supports multinational formats using google phonelibnumber
  "companyId": 3 <-- Id of a company created
}
```