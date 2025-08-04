# 👤 Customer Service

The **Customer Service** is a Spring Boot microservice responsible for customer onboarding, profile management, and internal user creation within the secure banking onboarding platform.

---

## 📦 Features

- Customer registration & profile update
- Internal user account creation
- KYC status update (called by KYC service)
- Customer dashboard info with KYC & account summary
- Fully secured with JWT authentication

---

---

## 📦 ER

![ER Diagram](https://github.com/Raghul-18/customer-service/blob/master/ER-POS.PNG?raw=true)

---

## 🚀 Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Web, Spring Data JPA
- Oracle 23c (ojdbc17)
- Flyway for DB migration
- MapStruct, Lombok
- JWT (via API Gateway)

---

## 🌐 API Endpoints & Examples

---

### ✅ Register Customer

**POST** `/api/customers/register`

#### 📤 Sample Request

```json
{
  "full_name": "John Doe",
  "email": "john@example.com",
  "phone": "+919876543210",
  "dob": "1990-01-15",
  "address": "123 Main St, City",
  "pan": "ABCDE1234F",
  "aadhaar": "123456789012"
}
```

#### 📥 Sample Response

```json
{
  "customer_id": 1,
  "message": "Customer registered successfully",
  "kyc_status": "PENDING"
}
```

---

### 🔍 Get Customer Details

**GET** `/api/customers/{customerId}`  
**Auth:** `Bearer jwt_token`

#### 📥 Sample Response

```json
{
  "customer_id": 1,
  "full_name": "John Doe",
  "email": "john@example.com",
  "phone": "+919876543210",
  "dob": "1990-01-15",
  "address": "123 Main St, City",
  "pan": "ABCDE1234F",
  "aadhaar": "123456789012",
  "kyc_status": "PENDING",
  "registered_at": "2024-01-15T10:30:00Z"
}
```

---

### ✏️ Update Customer Profile

**PUT** `/api/customers/{customerId}`  
**Auth:** `Bearer jwt_token`

#### 📤 Sample Request

```json
{
  "full_name": "John Updated",
  "email": "john.new@example.com",
  "address": "456 New St, City"
}
```

#### 📥 Sample Response

```json
{
  "message": "Customer updated successfully"
}
```

---

### 📊 Get Customer KYC & Account Status

**GET** `/api/customers/{customerId}/status`  
**Auth:** `Bearer jwt_token`

#### 📥 Sample Response

```json
{
  "customer_id": 1,
  "kyc_status": "VERIFIED",
  "has_account": true,
  "account_details": {
    "account_id": 101,
    "account_number": "ACC001234567890",
    "account_status": "ACTIVE"
  }
}
```

---

### 🧑‍💼 Create Internal User

**POST** `/api/users/create`

#### 📤 Sample Request

```json
{
  "username": "customer_123456789012",
  "password_hash": "hashed_password_here",
  "role": "CUSTOMER",
  "enabled": true
}
```

#### 📥 Sample Response

```json
{
  "user_id": 1,
  "message": "User created"
}
```

---

### 🔄 Update User Status

**PUT** `/api/users/{userId}/status`

#### 📤 Sample Request

```json
{
  "enabled": false
}
```

#### 📥 Sample Response

```json
{
  "message": "User status updated"
}
```

---

### 📄 Get Internal User

**GET** `/api/users/{userId}`

#### 📥 Sample Response

```json
{
  "user_id": 1,
  "username": "customer_123456789012",
  "role": "CUSTOMER",
  "enabled": true,
  "created_at": "2024-01-15T10:30:00Z"
}
```

---

### 🛂 Update KYC Status (internal only)

**PUT** `/api/customers/{customerId}/kyc-status`

#### 📤 Sample Request

```json
{
  "kyc_status": "VERIFIED"
}
```

#### 📥 Sample Response

```json
{
  "message": "KYC status updated"
}
```

---

## 🔐 Security

- All endpoints are JWT-secured via Gateway.
- Roles and tokens are enforced via `Authorization` header.
- Role: `CUSTOMER`, `ADMIN` (user-level), internal only as needed.

---

## 🧪 Local Setup

### 1. Clone

```bash
git clone https://your-repo-url/customer-service.git
cd customer-service
```

### 2. Configure `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/yourservicename
    username: RAGHUL
    password: yourpassword
  flyway:
    enabled: true
    locations: classpath:db/migration
server:
  port: 8081
```

### 3. Run

```bash
./mvnw spring-boot:run
```

---

## 📁 Project Structure

```
src/
├── controller/
├── service/
├── dto/
├── entity/
├── mapper/
├── repository/
├── exception/
├── config/
```

---

## 📂 Inter-Service Calls

- ✅ Called by: Web Gateway
- 🛂 Receives KYC updates from: KYC Service
- 🔄 Optionally calls Account Service for account lookup

---

## 📄 License

MIT License – 2025 © Raghul Prasanth
