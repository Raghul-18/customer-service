# 🏦 Complete Microservices API Plan

Based on your ER diagram, here's the comprehensive API specification for all 4 services:

---

## 🌐 **1. Web Gateway Service (Port 8080)**

### **Static Files**
```
GET  /                     → index.html (OTP login)
GET  /register             → register.html (customer registration)
GET  /upload-kyc           → upload-kyc.html (document upload)
GET  /dashboard            → dashboard.html (customer dashboard)
GET  /admin                → admin.html (admin panel)
GET  /assets/**            → CSS, JS, images
```

### **Authentication APIs**
```
POST /api/auth/send-otp
Body: { "phone": "+919876543210" }
Response: { "success": true, "message": "OTP sent" }

POST /api/auth/verify-otp
Body: { "phone": "+919876543210", "otp": "123456" }
Response: { 
  "success": true, 
  "token": "jwt_token_here",
  "user": { "user_id": 1, "role": "CUSTOMER" }
}

POST /api/auth/admin-login
Body: { "username": "admin", "password": "password" }
Response: { 
  "success": true, 
  "token": "jwt_token_here",
  "user": { "user_id": 2, "role": "ADMIN" }
}

POST /api/auth/refresh
Headers: { "Authorization": "Bearer jwt_token" }
Response: { "token": "new_jwt_token" }
```

### **Proxy Routes**
```
/api/customers/**          → customer-service
/api/kyc/**               → kyc-service  
/api/accounts/**          → account-service
```

---

## 👤 **2. Customer Service (Port 8081)**

### **Customer Management**
```
POST /api/customers/register
Body: {
  "full_name": "John Doe",
  "email": "john@example.com", 
  "phone": "+919876543210",
  "dob": "1990-01-15",
  "address": "123 Main St, City",
  "pan": "ABCDE1234F",
  "aadhaar": "123456789012"
}
Response: {
  "customer_id": 1,
  "message": "Customer registered successfully",
  "kyc_status": "PENDING"
}

GET /api/customers/{customerId}
Headers: { "Authorization": "Bearer jwt_token" }
Response: {
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

PUT /api/customers/{customerId}
Headers: { "Authorization": "Bearer jwt_token" }
Body: {
  "full_name": "John Updated",
  "email": "john.new@example.com",
  "address": "456 New St, City"
}
Response: { "message": "Customer updated successfully" }

GET /api/customers/{customerId}/status
Headers: { "Authorization": "Bearer jwt_token" }
Response: {
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

### **User Management (Internal)**
```
POST /api/users/create
Body: {
  "username": "customer_123456789012",
  "password_hash": "hashed_password",
  "role": "CUSTOMER",
  "enabled": true
}
Response: { "user_id": 1, "message": "User created" }

PUT /api/users/{userId}/status
Body: { "enabled": false }
Response: { "message": "User status updated" }

GET /api/users/{userId}
Response: {
  "user_id": 1,
  "username": "customer_123456789012",
  "role": "CUSTOMER",
  "enabled": true,
  "created_at": "2024-01-15T10:30:00Z"
}
```

### **KYC Status Management (Internal - called by KYC Service)**
```
PUT /api/customers/{customerId}/kyc-status
Body: { "kyc_status": "VERIFIED" }
Response: { "message": "KYC status updated" }
```

---

## 📋 **3. KYC Service (Port 8082)**

### **Document Management**
```
POST /api/kyc/upload/{customerId}
Headers: { "Authorization": "Bearer jwt_token" }
Body: FormData {
  "pan_document": file,
  "aadhaar_document": file,  
  "photo": file,
  "signature": file
}
Response: {
  "message": "Documents uploaded successfully",
  "uploaded_documents": [
    {
      "document_id": 1,
      "document_type": "PAN",
      "status": "PENDING",
      "uploaded_at": "2024-01-15T10:30:00Z"
    },
    {
      "document_id": 2, 
      "document_type": "AADHAAR",
      "status": "PENDING",
      "uploaded_at": "2024-01-15T10:30:00Z"
    }
  ]
}

GET /api/kyc/documents/{customerId}
Headers: { "Authorization": "Bearer jwt_token" }
Response: {
  "customer_id": 1,
  "documents": [
    {
      "document_id": 1,
      "document_type": "PAN", 
      "status": "VERIFIED",
      "remarks": null,
      "verified_by": 2,
      "uploaded_at": "2024-01-15T10:30:00Z",
      "verified_at": "2024-01-15T14:20:00Z"
    }
  ]
}

GET /api/kyc/document/{documentId}/download
Headers: { "Authorization": "Bearer jwt_token" }
Response: Binary file data

DELETE /api/kyc/document/{documentId}
Headers: { "Authorization": "Bearer jwt_token" }
Response: { "message": "Document deleted successfully" }
```

### **Admin Verification APIs**
```
GET /api/kyc/pending-verifications
Headers: { "Authorization": "Bearer jwt_token" }
Query: ?page=0&size=10&document_type=PAN
Response: {
  "content": [
    {
      "document_id": 1,
      "customer_id": 1,
      "customer_name": "John Doe",
      "document_type": "PAN",
      "uploaded_at": "2024-01-15T10:30:00Z",
      "status": "PENDING"
    }
  ],
  "totalElements": 50,
  "totalPages": 5
}

PUT /api/kyc/verify/{documentId}
Headers: { "Authorization": "Bearer jwt_token" }
Body: {
  "status": "VERIFIED",
  "remarks": "Document verified successfully"
}
Response: { 
  "message": "Document verified",
  "customer_kyc_status": "VERIFIED"
}

PUT /api/kyc/reject/{documentId}  
Headers: { "Authorization": "Bearer jwt_token" }
Body: {
  "status": "REJECTED",
  "remarks": "Document unclear, please reupload"
}
Response: { 
  "message": "Document rejected",
  "customer_kyc_status": "PENDING"
}

GET /api/kyc/customer/{customerId}/status
Headers: { "Authorization": "Bearer jwt_token" }
Response: {
  "customer_id": 1,
  "overall_status": "VERIFIED",
  "document_statuses": {
    "PAN": "VERIFIED",
    "AADHAAR": "VERIFIED", 
    "PHOTO": "VERIFIED",
    "SIGNATURE": "VERIFIED"
  }
}
```

### **Audit & Reporting**
```
GET /api/kyc/audit-logs
Headers: { "Authorization": "Bearer jwt_token" }
Query: ?page=0&size=20&entity_type=KYC_DOCUMENT&action=VERIFY
Response: {
  "content": [
    {
      "log_id": 1,
      "user_id": 2,
      "action": "VERIFY",
      "entity_type": "KYC_DOCUMENT", 
      "entity_id": 1,
      "timestamp": "2024-01-15T14:20:00Z",
      "details": "Document verified by admin"
    }
  ]
}

GET /api/kyc/stats
Headers: { "Authorization": "Bearer jwt_token" }
Response: {
  "total_submissions": 100,
  "pending_verification": 25,
  "verified": 70,
  "rejected": 5,
  "verification_rate": 75.0
}
```

---

## 🏧 **4. Account Service (Port 8083)**

### **Account Creation**  
```
POST /api/accounts/create
Headers: { "Authorization": "Bearer jwt_token" }
Body: {
  "customer_id": 1,
  "account_type": "SAVINGS",
  "initial_deposit": 1000.00
}
Response: {
  "account_id": 101,
  "account_number": "ACC001234567890",
  "account_type": "SAVINGS", 
  "account_status": "ACTIVE",
  "balance": 1000.00,
  "created_at": "2024-01-15T15:30:00Z"
}

POST /api/accounts/create-after-kyc (Internal API)
Headers: { "X-Service-Token": "internal_service_token" }
Body: { "customer_id": 1 }
Response: { 
  "account_id": 101,
  "account_number": "ACC001234567890",
  "message": "Account created successfully"
}
```

### **Account Management**
```
GET /api/accounts/{customerId}
Headers: { "Authorization": "Bearer jwt_token" }  
Response: {
  "accounts": [
    {
      "account_id": 101,
      "account_number": "ACC001234567890",
      "account_type": "SAVINGS",
      "account_status": "ACTIVE", 
      "balance": 1500.00,
      "created_at": "2024-01-15T15:30:00Z"
    }
  ]
}

GET /api/accounts/details/{accountId}
Headers: { "Authorization": "Bearer jwt_token" }
Response: {
  "account_id": 101,
  "customer_id": 1,
  "account_number": "ACC001234567890",
  "account_type": "SAVINGS",
  "account_status": "ACTIVE",
  "balance": 1500.00,
  "created_at": "2024-01-15T15:30:00Z"
}

PUT /api/accounts/{accountId}/status
Headers: { "Authorization": "Bearer jwt_token" }
Body: { 
  "account_status": "BLOCKED",
  "reason": "Suspicious activity detected"
}
Response: { "message": "Account status updated" }

PUT /api/accounts/{accountId}/balance
Headers: { "Authorization": "Bearer jwt_token" }
Body: { 
  "balance": 2000.00,
  "transaction_type": "CREDIT",
  "description": "Deposit"
}
Response: { 
  "message": "Balance updated",
  "new_balance": 2000.00
}
```

### **Admin Account Operations**
```
GET /api/accounts/all
Headers: { "Authorization": "Bearer jwt_token" }
Query: ?page=0&size=20&status=ACTIVE
Response: {
  "content": [
    {
      "account_id": 101,
      "customer_id": 1,
      "customer_name": "John Doe",
      "account_number": "ACC001234567890", 
      "account_type": "SAVINGS",
      "account_status": "ACTIVE",
      "balance": 1500.00,
      "created_at": "2024-01-15T15:30:00Z"
    }
  ],
  "totalElements": 150,
  "totalPages": 8
}

GET /api/accounts/stats
Headers: { "Authorization": "Bearer jwt_token" }
Response: {
  "total_accounts": 150,
  "active_accounts": 145,
  "blocked_accounts": 3,
  "closed_accounts": 2,
  "total_balance": 2500000.00
}
```

---

## 🔄 **Inter-Service Communication**

### **Customer Service → KYC Service**
```
GET /customers/{customerId}/kyc-status
Response: { "kyc_status": "VERIFIED" }
```

### **KYC Service → Customer Service**  
```
PUT /customers/{customerId}/kyc-status
Body: { "kyc_status": "VERIFIED" }
```

### **KYC Service → Account Service**
```
POST /accounts/create-after-kyc
Body: { "customer_id": 1 }
```

### **Account Service → Customer Service**
```
GET /customers/{customerId}/basic-info
Response: { "customer_id": 1, "full_name": "John Doe" }
```

---

## 🔒 **Authentication & Authorization**

### **JWT Token Structure**
```json
{
  "sub": "1",
  "role": "CUSTOMER", 
  "customer_id": 1,
  "exp": 1642780800,
  "iat": 1642777200
}
```

### **Role-Based Access**
```
CUSTOMER Role:
- Can access own customer data
- Can upload KYC documents  
- Can view own account details

ADMIN Role:
- Can verify KYC documents
- Can view all customers
- Can manage account status
- Can view audit logs

VERIFIER Role:
- Can only verify KYC documents
- Can view pending verifications
```

---

## 📊 **Error Response Format**

```json
{
  "error": {
    "code": "CUSTOMER_NOT_FOUND",
    "message": "Customer with ID 123 not found", 
    "timestamp": "2024-01-15T10:30:00Z",
    "path": "/api/customers/123"
  }
}
```

---

## 🚀 **Implementation Priority**

1. **Phase 1**: Customer Service + Basic Gateway
2. **Phase 2**: KYC Service + Document Upload  
3. **Phase 3**: Account Service + Integration
4. **Phase 4**: Admin Panel + Audit Features

This API plan covers all your ER diagram entities and provides a complete microservices architecture!