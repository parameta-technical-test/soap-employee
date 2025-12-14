# Parameta Technical Test – SOAP Service

## 📌 Overview

This module provides the **SOAP layer** for the Parameta technical test. It exposes SOAP endpoints to:

- **Save or update employee information** (based on system parameters)
- **Retrieve full employee information** (by employee id or document data)
- Optionally **return an informative PDF** stored in **Amazon S3**
- Calculate and return **additional employee information** (time linked to the company and current age)

The service is implemented with:

- **Spring Boot 4 (Java 17)**
- **Spring Web Services (SOAP)**
- **Spring Security (JWT)**
- **Spring Data JPA (MySQL)**
- **JAXB (XSD → generated classes via Maven plugin)**
- **MapStruct**
- **AWS SDK (S3)** via the shared **commons** library

---

## 🧱 Architecture (High Level)

```

SOAP Client
│
▼
SOAP Endpoints (Spring-WS)
│
├── Services (Save / View info)
│     ├── DB access (JPA repositories)
│     ├── System parameters (enable/disable behavior)
│     └── S3 PDF retrieval (optional)
│
├── Mappers (MapStruct + manual mapping helpers)
│
└── Commons library (DTOs, Entities, JWT utilities, shared config)

````

---

## 🔌 SOAP Endpoints

### 1) SaveEmployeeRequest
- **Operation**: Saves a new employee or updates existing employee data (only if allowed by system parameter).
- **Payload root**:
  - Namespace: `http://parameta.co/technical/test/employee`
  - LocalPart: `SaveEmployeeRequest`

### 2) GetAllInformationEmployeeRequest
- **Operation**: Retrieves full employee information using:
  - `idEmployee` OR
  - `typeDocument + numberDocument`
- May include `informativePdf` (byte[]) if enabled by system parameter and a PDF key exists.

---

## 📄 WSDL

Once the application is running, the WSDL is exposed at:

```txt
http://localhost:8002/ws/employee.wsdl
````

> Note: WSDL publication and URL mapping are configured in `SoapWebServiceConfig`.

---

## 🔐 Security (JWT)

This SOAP service is protected using **JWT** and a **stateless** security configuration:

* WSDL is **public**:

    * `/ws/**.wsdl` → `permitAll()`
* SOAP endpoint calls require authentication:

    * `/ws/**` → `authenticated()`

The JWT filter validates:

* `Authorization: Bearer <token>`
* Token integrity and expiration
* Optional blacklist validation (revoked tokens)

---

## 🗄️ Database (MySQL)

This service persists and reads employee information using **Spring Data JPA** with MySQL.

Entities (via commons module):

* `EmployeeEntity`
* `TypeDocumentEntity`
* `PositionEntity`
* `AdministratorUserEntity`
* `SystemParameterEntity`

The service behavior is controlled by DB parameters:

* `UPDATE_INFORMATION` → allow/disallow updating existing employee info
* `GET_PDF_EMPLOYEE` → allow/disallow PDF retrieval from S3

---

## ☁️ Amazon S3 (PDF Retrieval)

If enabled by system parameter, the service retrieves the PDF from S3 using:

* Bucket from config: `aws.s3.bucket`
* Key from employee record: `storageLocationReport`

Service:

* `GetPdfS3Service` → reads S3 object bytes and returns `byte[]`

---

## ⚙️ Configuration

Configuration is externalized via environment variables and `.env` file import:

```yaml
spring:
  config:
    import: optional:file:.env[.properties]
```

### Main settings

* **Application name**
* **Datasource (MySQL + HikariCP)**
* **JPA / Hibernate**
* **JWT secret**
* **AWS region & S3 bucket**
* **Logging + SOAP tracing**
* **Actuator endpoints**

### Example `.env`

```properties
SPRING_APPLICATION_NAME=parameta-soap
SERVER_PORT=8002

SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/technical_test
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=secret

JWT_SECRET=base64url-encoded-secret

AWS_REGION=us-east-1
AWS_NAME_BUCKET=your-bucket-name
```

> Logging includes SOAP tracing:

* `org.springframework.ws.server.MessageTracing=TRACE`
* `org.springframework.ws.soap.server.SoapMessageDispatcher=TRACE`

---

## 📦 Dependencies

Key dependencies from `pom.xml`:

* `spring-boot-starter-web-services`
* `spring-boot-starter-security`
* `spring-boot-starter-data-jpa`
* `mysql-connector-j`
* `technical.test.commons:0.0.54` (GitHub Packages)
* `mapstruct`
* `wsdl4j`

---

## 🧬 JAXB Code Generation (XSD → Java)

SOAP classes are generated from:

* `src/main/resources/employee.xsd`

Using Maven plugin:

* `jaxb2-maven-plugin`
* Output directory:

    * `target/generated-sources/jaxb`
* Generated package:

    * `com.parameta.technical.test.soap.gen`

The build-helper plugin adds generated sources automatically to the compilation.

---

## 🚀 Run Locally

### Requirements

* Java 17+
* Maven 3.9+
* MySQL running and accessible
* `.env` configured (recommended)

### Run with Maven

```bash
mvn clean package
mvn spring-boot:run
```

---

## 🐳 Docker Support

This project includes a **multi-stage Dockerfile** for reproducible builds and lightweight runtime images.

### Dockerfile (summary)

* **Build stage**

    * Uses Maven image to compile and package the JAR
    * Uses `dependency:go-offline` to speed up rebuilds
* **Runtime stage**

    * Runs the final JAR with Java 17
    * Exposes port **8002**
    * Sets JVM memory tuning

### Build image

```bash
docker build -t parameta-soap-api .
```

### Run container

```bash
docker run -p 8002:8002 --env-file .env parameta-soap-api
```

---

## 📤 Publish to Docker Hub

This module is published to **Docker Hub** using the Dockerfile.

### 1) Login

```bash
docker login
```

### 2) Tag image

```bash
docker tag parameta-soap-api <your-dockerhub-user>/parameta-soap-api:latest
```

### 3) Push

```bash
docker push <your-dockerhub-user>/parameta-soap-api:latest
```

> You can also version it:

```bash
docker tag parameta-soap-api <your-dockerhub-user>/parameta-soap-api:0.0.1
docker push <your-dockerhub-user>/parameta-soap-api:0.0.1
```

---

## ✅ Quality & Coverage

The build includes **JaCoCo coverage** with a minimum threshold:

* **Line coverage ≥ 80%**
* Exclusions:

    * `**/configuration/**`
    * `**/util/**`
    * `**/SoapApplication.java`

---

## 👤 Author

**Technical test developed for Parameta**
Author: **Brahian Alexander Caceres Guevara**

