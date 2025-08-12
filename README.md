# Customer Service

A Spring Boot microservice responsible for customer onboarding, profile management, and KYC status tracking within a secure banking platform. This service integrates with JWT-based authentication and event-driven architecture using Apache Kafka.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Security Implementation](#security-implementation)
- [Event-Driven Integration](#event-driven-integration)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Customer Service is a core component of the banking microservices ecosystem, providing comprehensive customer lifecycle management capabilities. It handles customer registration, profile updates, KYC status tracking, and integration with other banking services through secure APIs and event messaging.

### Key Features

- **Customer Registration**: Secure onboarding with comprehensive validation
- **Profile Management**: Update customer information with proper authorization
- **KYC Integration**: Status tracking and updates from KYC verification service
- **JWT Authentication**: Role-based access control with Bearer token validation
- **Event Publishing**: Kafka integration for account creation workflows
- **Admin Operations**: Administrative endpoints for customer management
- **Data Validation**: Comprehensive input validation for Indian banking standards

## Architecture

The service follows a layered architecture pattern:

```
┌─────────────────┐
│   Controllers   │ ← REST API Layer
├─────────────────┤
│    Services     │ ← Business Logic Layer
├─────────────────┤
│   Repositories  │ ← Data Access Layer
├─────────────────┤
│    Database     │ ← Oracle 23c
└─────────────────┘
```

### Inter-Service Communication

- **Inbound**: Web Gateway (JWT-secured requests)
- **Outbound**: Kafka events to Account Service
- **Internal**: KYC Service status updates

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Runtime | Java | 17+ |
| Framework | Spring Boot | 3.x |
| Web | Spring Web MVC | 3.x |
| Data Access | Spring Data JPA | 3.x |
| Database | Oracle Database | 23c |
| Migration | Flyway | Latest |
| Mapping | MapStruct | Latest |
| Utilities | Lombok | Latest |
| Messaging | Apache Kafka | Latest |
| Authentication | JWT (jjwt) | Latest |
| Documentation | SpringDoc OpenAPI | Latest |

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6+
- Oracle Database 23c
- Apache Kafka 2.8+
- Git

## Installation & Setup

### 1. Clone Repository

```bash
git clone <repository-url>
cd customer-service
```

### 2. Database Setup

Ensure Oracle Database is running and accessible:

```sql
-- Create user and schema
CREATE USER RAGHUL IDENTIFIED BY your_password;
GRANT CONNECT, RESOURCE, DBA TO RAGHUL;
```

### 3. Kafka Setup

Start Kafka services:

```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka Server
bin/kafka-server-start.sh config/server.properties
```

### 4. Build Application

```bash
./mvnw clean compile
```

### 5. Run Application

```bash
./mvnw spring-boot:run
```

The application will start on port 8081.

## Configuration

### Application Configuration (application.yml)

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/FREEPDB1
    username: RAGHUL
    password: your_password
    driver-class-name: oracle.jdbc.OracleDriver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  flyway:
    enabled: true
    table: flyway_schema_history_customer_service
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
    out-of-order: true
  
  kafka:
    bootstrap-servers: localhost:9092

jwt:
  secret: your_base64_encoded_secret_key
  expiration-ms: 3600000

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Oracle database connection URL | `jdbc:oracle:thin:@localhost:1521/FREEPDB1` |
| `DB_USERNAME` | Database username | `RAGHUL` |
| `DB_PASSWORD` | Database password | Required |
| `KAFKA_SERVERS` | Kafka bootstrap servers | `localhost:9092` |
| `JWT_SECRET` | JWT signing secret (Base64) | Required |

## API Documentation

### Authentication

All endpoints except registration require JWT authentication via the `Authorization` header:

```
Authorization: Bearer <jwt_token>
```

### Customer Operations

#### Register Customer

**POST** `/api/customers/register`

Registers a new customer in the system. The user ID is extracted from the JWT token.

**Request Body:**
```json
{
  "fullName": "John Doe",
  "phone": "9876543210",
  "email": "john@example.com",
  "dob": "1990-01-15",
  "address": "123 Main Street, City",
  "pan": "ABCDE1234F",
  "aadhaar": "123456789012"
}
```

**Response:**
```json
{
  "customerId": 1,
  "fullName": "John Doe",
  "phone": "9876543210",
  "email": "john@example.com",
  "dob": "1990-01-15",
  "address": "123 Main Street, City",
  "pan": "ABCDE1234F",
  "aadhaar": "123456789012",
  "kycStatus": "PENDING",
  "registeredAt": "2024-01-15T10:30:00Z",
  "message": "Customer registered successfully"
}
```

#### Get Customer Details

**GET** `/api/customers/{customerId}`

Retrieves customer information. Access is restricted to the customer owner or admin users.

#### Update Customer Profile

**PUT** `/api/customers/{customerId}`

Updates customer profile information (name, email, address only).

**Request Body:**
```json
{
  "fullName": "John Updated",
  "email": "john.new@example.com",
  "address": "456 New Street, City"
}
```

#### Get Customer Status

**GET** `/api/customers/{customerId}/status`

Returns customer KYC and account status information.

### Administrative Operations

#### Get All Customers

**GET** `/api/customers/admin/all`

Returns all customers in the system. Requires ADMIN role.

#### Get Customer by ID (Admin)

**GET** `/api/customers/admin/{customerId}`

Administrative access to any customer record.

#### Update KYC Status

**PUT** `/api/customers/admin/{customerId}/kyc-status`

Updates customer KYC status. Triggers account creation event when status is set to VERIFIED.

### Internal Operations

#### Get Customer ID by User ID

**GET** `/api/customers/user/{userId}/customer-id`

Resolves user ID to customer ID for internal service communication.

#### Verify Customer Ownership

**GET** `/api/customers/{customerId}/verify-ownership/{userId}`

Verifies if a specific user owns a customer record.

### API Documentation Access

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8081/v3/api-docs`

## Database Schema

### Customers Table

```sql
CREATE TABLE CUSTOMERS (
    customer_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER(19) NOT NULL UNIQUE,
    full_name VARCHAR2(100) NOT NULL,
    phone VARCHAR2(20) UNIQUE NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    dob DATE,
    address VARCHAR2(255) NOT NULL,
    pan VARCHAR2(20) UNIQUE NOT NULL,
    aadhaar VARCHAR2(20) UNIQUE NOT NULL,
    kyc_status VARCHAR2(20) DEFAULT 'PENDING',
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Data Validation Rules

- **PAN**: Must follow format `ABCDE1234F` (5 letters, 4 digits, 1 letter)
- **Aadhaar**: Must be exactly 12 digits
- **Phone**: Must be exactly 10 digits
- **Email**: Must be valid email format
- **KYC Status**: PENDING, VERIFIED, or REJECTED

## Security Implementation

### JWT Authentication

The service implements JWT-based authentication with the following features:

- **Token Validation**: All protected endpoints validate JWT tokens
- **Role-Based Access**: ADMIN and CUSTOMER roles with different permissions
- **User Context**: Authenticated user information available in request context
- **Access Control**: Customers can only access their own records

### Security Interceptor

The `JwtAuthInterceptor` handles:
- Token extraction from Authorization header
- Token validation using HMAC-SHA256
- User context establishment
- Role-based authorization

### Protected Endpoints

All endpoints except `/api/customers/register` require valid JWT authentication.

## Event-Driven Integration

### Kafka Integration

The service publishes events to Apache Kafka for inter-service communication:

**Topic**: `account-creation-topic`

**Event**: When customer KYC status is updated to VERIFIED, an account creation event is published:

```json
{
  "customerId": 123
}
```

### Event Flow

1. KYC Service verifies customer documents
2. KYC Service calls Customer Service to update status
3. Customer Service updates database
4. If status is VERIFIED, Customer Service publishes Kafka event
5. Account Service consumes event and creates bank account

## Testing

### Running Tests

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

### Postman Collection

Import the provided Postman collection (`Customer Service API Collection.postman_collection.json`) for API testing.

**Environment Variables:**
- `base_url`: `localhost:8081`

### Sample Test Scenarios

1. **Customer Registration Flow**
   - Register new customer
   - Verify registration response
   - Check customer status

2. **Profile Update Flow**
   - Authenticate as customer
   - Update profile information
   - Verify changes

3. **Admin Operations**
   - Authenticate as admin
   - View all customers
   - Update KYC status

## Project Structure

```
src/main/java/com/bank/customerservice/
├── config/                    # Configuration classes
│   ├── KafkaProducerConfig.java
│   ├── SwaggerConfig.java
│   └── WebConfig.java
├── controller/                # REST controllers
│   ├── CustomerController.java
│   └── CustomerAdminController.java
├── dto/                      # Data transfer objects
│   ├── CustomerRegistrationRequest.java
│   ├── CustomerResponse.java
│   ├── CustomerUpdateRequest.java
│   ├── CustomerIdResponse.java
│   └── KycStatusUpdateRequest.java
├── entity/                   # JPA entities
│   ├── Customer.java
│   └── KycStatus.java
├── events/                   # Event objects
│   └── AccountCreationEvent.java
├── exception/                # Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── BadRequestException.java
├── kafka/                    # Kafka integration
│   └── KafkaEventProducer.java
├── mapper/                   # MapStruct mappers
│   └── CustomerMapper.java
├── repository/               # Data repositories
│   └── CustomerRepository.java
├── security/                 # Security components
│   ├── JwtAuthInterceptor.java
│   └── JwtUtils.java
├── service/                  # Business logic
│   ├── CustomerService.java
│   └── impl/
│       └── CustomerServiceImpl.java
├── util/                     # Utility classes
│   └── AuthenticatedUser.java
└── CustomerServiceApplication.java
```

## Error Handling

The service implements comprehensive error handling:

### Exception Types

- **ResourceNotFoundException**: When requested customer is not found
- **BadRequestException**: For invalid request data
- **SecurityException**: For authorization failures
- **ValidationException**: For constraint violations

### Error Response Format

```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Customer not found with id: 123",
    "timestamp": "2024-01-15T10:30:00Z"
  }
}
```

## Logging

### Logging Configuration

The service uses SLF4J with Logback for structured logging:

- **INFO**: Business operations and service interactions
- **DEBUG**: Detailed tracing for development
- **ERROR**: Exception handling and system errors

## Integration Points

### External Dependencies

1. **API Gateway**: JWT token validation and request routing
2. **KYC Service**: Receives KYC status updates
3. **Account Service**: Consumes account creation events via Kafka
4. **Oracle Database**: Primary data storage
5. **Apache Kafka**: Event messaging system

### Internal APIs

The service exposes several internal endpoints for microservice communication:
- User-to-Customer ID resolution
- Customer ownership verification
- Administrative operations

## Data Migration

Database schema is managed using Flyway migrations located in `src/main/resources/db/migration/`:

- **V1__Create_customers_table.sql**: Initial customer table creation and user ID column addition

### Migration Commands

```bash
# Check migration status
./mvnw flyway:info

# Migrate to latest version
./mvnw flyway:migrate

# Validate current schema
./mvnw flyway:validate
```

### Pull Request Process

1. Create feature branch from main
2. Implement changes with tests
3. Update documentation
4. Submit pull request with detailed description
5. Code review and approval required

## Troubleshooting

### Common Issues

1. **Database Connection Failures**
   - Verify Oracle service is running
   - Check connection parameters in application.yml
   - Validate user permissions

2. **JWT Authentication Errors**
   - Verify JWT secret configuration
   - Check token expiration settings
   - Validate token format in requests

3. **Kafka Connection Issues**
   - Ensure Kafka broker is accessible
   - Check bootstrap servers configuration
   - Verify topic creation

### Debug Mode

Enable debug logging for troubleshooting:

```yaml
logging:
  level:
    com.bank.customerservice: DEBUG
    org.springframework.kafka: DEBUG
    org.flywaydb.core: DEBUG
```
