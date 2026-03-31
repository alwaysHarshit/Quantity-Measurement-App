# Quantity Measurement App 📏⚖️

A robust, enterprise-grade Spring Boot application designed for complex unit conversions and arithmetic operations. This project evolved through a modular, feature-driven development approach, encompassing multiple measurement systems, secure authentication, and persistent history tracking.

---

## 🚀 Key Features

- **Unit Comparison & Conversion**: Compare and convert between Length, Volume, Weight, and Temperature.
- **Advanced Arithmetic**: Perform Addition, Subtraction, and Division on mixed units (e.g., adding 1 Feet to 12 Inches).
- **Targeted Results**: Automatically convert arithmetic results to a desired target unit.
- **JWT Security**: Secure API endpoints with User Registration and Login using JSON Web Tokens.
- **Persistence & Audit**: Full history of every operation stored in MySQL, including error logs for incompatible operations.
- **N-Tier Architecture**: Clean separation of concerns between Controllers, Services, Repositories, and Models.

---

## 🛠️ Technology Stack

- **Backend**: Java 21, Spring Boot 4.x
- **Security**: Spring Security, JJWT (JSON Web Token)
- **Database**: MySQL, Spring Data JPA, Hibernate
- **Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Utilities**: Lombok, Maven, Validation API

---

## 🧬 Development Journey (Branch History)

This project was built iteratively across several specialized branches, ensuring each feature was isolated and tested before being merged into `main`:

*   **Architecture & Core**:
    *   `feature/Spring-Framework-Integration` & `feature/N-tier`: Established the foundational Spring Boot structure.
    *   `feature/generic-quantity`: Implemented a generic design for handling various measurement types.
*   **Measurement Units**:
    *   `feature/UC1-FeetEquality` & `feature/UC2-FeetEquality`: Initial unit equality logic.
    *   `feature/unit-converstion` & `feature/standalone-conversion`: Core conversion algorithms.
    *   `feature/volume-measurement`, `feature/weight-measurement`, `temperature-measurement`: Specific logic for different categories.
    *   `feature/multi-category-support` & `feature/extend-unit-support`: Unified the system to support diverse units.
*   **Arithmetic Logic**:
    *   `centralized-arithmetic-logic`: Unified the math engine for all units.
    *   `feature/addition-units` & `feature/addition-with-specificunits`: Enabled cross-unit addition.
    *   `subtraction-division-operations`: Completed the arithmetic suite.
*   **Persistence & Security**:
    *   `feature/DB-Integration`: Integrated MySQL for history and user data.
    *   `feature/auth-services`: Implemented JWT-based authentication and authorization.

---

## 📐 Supported Units

| Category | Units Supported |
| :--- | :--- |
| **Length** | Feet, Inches, Yards, Centimeters (CM) |
| **Volume** | Litre, Millilitre, Gallon |
| **Weight** | Kilogram, Gram, Pound |
| **Temperature** | Celsius, Fahrenheit |

---

## 📡 API Endpoints

### Authentication
- `POST /auth/signup` - Register a new account.
- `POST /auth/login` - Authenticate and receive a JWT token.

### Operations (Auth Required)
- `POST /quantity/compare` - Compare two quantities.
- `POST /quantity/convert` - Convert to a specific unit.
- `POST /quantity/add` - Add two quantities.
- `POST /quantity/add-with-target-unit` - Add and convert to result unit.
- `POST /quantity/subtract` - Subtract quantities.
- `POST /quantity/divide` - Divide quantities.

### History
- `GET /quantity/history/operation/{op}` - Filter history by operation (ADD, CONVERT, etc.).
- `GET /quantity/history/type/{type}` - Filter by category (LengthUnit, etc.).
- `GET /quantity/history/errored` - View operations that failed validation.

---

## ⚙️ Setup Instructions

1.  **Database**: Create a MySQL database:
    ```sql
    CREATE DATABASE quantity_measurement_entity;
    ```
2.  **Configuration**: Update `src/main/resources/application.properties` with your MySQL `username` and `password`.
3.  **Build**:
    ```bash
    mvn clean install
    ```
4.  **Run**:
    ```bash
    mvn spring-boot:run
    ```
5.  **Docs**: Access the interactive API documentation at `http://localhost:8080/swagger-ui.html`

---

## 🧪 Testing
The project includes a comprehensive suite of unit and integration tests. Run them using:
```bash
mvn test
```
Detailed `.http` request files are provided in the root directory for manual testing.
