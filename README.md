# 🌿 IDS 24-25 Alimentari

![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen?logo=spring)

![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle)

![H2 Database](https://img.shields.io/badge/Database-H2-lightgrey?logo=h2)

![Hibernate](https://img.shields.io/badge/Hibernate-JPA-orange?logo=hibernate)

![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-green?logo=springsecurity)

![PayPal](https://img.shields.io/badge/PayPal-Integration-blue?logo=paypal)

![License](https://img.shields.io/badge/License-MIT-yellow)

## 📜 Project Description

This group project, as part of the Unicam Software Engineering exam, teaches us how to approach the development of every software-related artifact following an Iterative Development Model (the Unified Process). The focus is not only on the development of the codebase, but also and above all on the analysis process.

The project focuses on managing a comprehensive marketplace dedicated to selling typical food products, processed goods, and experiential packages linked to local traditions.<br>

It involves various actors, including producers, transformers, distributors, event planners, moderators, customer service, and admins, each with specific roles.

### ✨ Key Features:

- **🛍️ Product Management**: Create, edit, and manage individual products and product packages

- **✅ Content Validation**: Moderator review and approval system for products and events

- **🛒 Shopping Cart**: Advanced cart management with multiple products and quantities

- **📦 Order Management**: Complete order lifecycle from creation to delivery

- **💳 Payment Integration**: PayPal integration for secure transactions

- **👨‍⚖️ User & Business Management**: Multi-role user system with company associations

- **🎉 Event Management**: Organize fairs, tours, and tastings to promote local products

- **🔐 Security**: JWT-based authentication and role-based authorization

- **📧 Email Services**: Automated email notifications for various system events

---

## ⚙️ Setup & Installation

### 📌 Prerequisites

Make sure you have the following installed:

- **☕ JDK 21 or higher**

- **🐘 Gradle 8.x**

### 🚀 Getting Started

### 📥 Clone the Repository

```sh

$  git  clone  https://github.com/PiladeJr/Ingegneria_del_software_24-25_Alimentari.git

$  cd  Ingegneria_del_software_24-25_Alimentari/java/ids_24_25_alimentari

```

### 🛠️ Configure the Database

The application uses H2 in-memory database by default. Configuration is in `src/main/resources/application.properties`:

```properties

# H2 Database (in-memory for development)

spring.datasource.url=jdbc:h2:mem:testdb

spring.datasource.driverClassName=org.h2.Driver

spring.datasource.username=sa

spring.datasource.password=password



# Enable H2 Console (accessible at /h2-console)

spring.h2.console.enabled=true



# Hibernate settings

spring.jpa.hibernate.ddl-auto=update

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

```

### 🔧 Configure PayPal Integration

Create a `dev.properties` file in the root directory with your PayPal credentials:

```properties

PAYPAL_CLIENT_ID=your_paypal_client_id

PAYPAL_CLIENT_SECRET=your_paypal_client_secret

PAYPAL_MODE=sandbox

```

### 🏗️ Build and Run the Project

Using Gradle wrapper:

```sh

# Build the project

$  ./gradlew  build



# Run the application

$  ./gradlew  bootRun

```

### 🌍 Accessing the Application

Once started, the application will be available at:

```

Application: http://localhost:8080

H2 Console: http://localhost:8080/h2-console

```

## 📚 API Documentation

The application provides RESTful APIs for various functionalities:

### 🛍️ Product APIs

- `GET /api/prodotto/visualizza/all` - Get all products (with sorting)

- `GET /api/prodotto/visualizza/id-azienda/{id}` - Get products by company

- `DELETE /api/prodotto/rimuovi-dallo-shop/{id}` - Remove product from shop

### 🛒 Cart APIs

- `POST /api/carrello` - Create cart

- `PUT /api/carrello/{id}/aggiungi` - Add items to cart

- `GET /api/carrello/utente/{userId}` - Get cart by user

### 📦 Order APIs

- `GET /api/ordini` - Get all orders

- `POST /api/ordini` - Create new order

- `GET /api/ordini/{id}/stato` - Get order status with transaction details

### 💳 PayPal APIs

- `POST /api/paypal/create-payment` - Create PayPal payment

- `GET /api/paypal/success` - Handle successful payment

- `GET /api/paypal/cancel` - Handle cancelled payment

### 👥 User APIs

- `POST /api/utenti/register` - User registration

- `POST /api/utenti/login` - User authentication

## 🏗️ Architecture

### 📁 Project Structure

```

src/

├── main/

│ ├── java/it/cs/unicam/ids_24_25_alimentari/

│ │ ├── controllers/ # REST Controllers

│ │ ├── modelli/ # Entity Models

│ │ │ ├── azienda/ # Company entities

│ │ │ ├── carrello/ # Cart entities

│ │ │ ├── contenuto/ # Content entities (products, events)

│ │ │ ├── ordine/ # Order entities

│ │ │ ├── utente/ # User entities

│ │ │ └── transazione/ # Transaction entities

│ │ ├── servizi/ # Business Logic Services

│ │ ├── repositories/ # JPA Repositories

│ │ ├── dto/ # Data Transfer Objects

│ │ └── config/ # Configuration classes

│ └── resources/

│ ├── application.properties

│ └── static/ # Static resources (images, etc.)

└── test/ # Unit and Integration Tests

```

### 🔧 Technologies Used

- **Spring Boot 3.5.0** - Main framework

- **Spring Data JPA** - Data persistence

- **Spring Security** - Authentication and authorization

- **Spring Web** - RESTful web services

- **Spring Mail** - Email services

- **H2 Database** - In-memory database for development

- **JWT** - JSON Web Tokens for authentication

- **PayPal SDK** - Payment processing

- **Lombok** - Boilerplate code reduction

- **Hibernate Validator** - Data validation

## 🗃️ Database Schema

### Key Entities:

- **👤 Utente (User)**: Multi-role user system (Cliente, Produttore, Moderatore, etc.)

- **🏢 Azienda (Company)**: Business entities with products and collaborations

- **🛍️ Prodotto (Product)**: Individual products and packages

- **🛒 Carrello (Cart)**: Shopping cart with multiple items

- **📦 Ordine (Order)**: Order management with status tracking

- **💳 Transazione (Transaction)**: Payment transaction records

- **🎉 Evento (Event)**: Fairs and visits for product promotion

## 🔐 Security Features

- **JWT Authentication**: Secure token-based authentication

- **Role-based Authorization**: Different access levels for different user types

- **Password Encryption**: BCrypt password hashing

- **Request Validation**: Input validation with Bean Validation

## 💻 Development

### 🧪 Running Tests

```sh

$  ./gradlew  test

```

### 📊 Code Quality

The project follows Java best practices and includes:

- Service layer architecture

- Repository pattern with JPA

- DTO pattern for API communication

- Builder pattern for complex object creation

- Strategy pattern for content validation

---

## 👥 Team Members

- **Federico Mengascini**

- **Pilade Jr Tomassini**

- **Davide Di Lorenzo**

## 📜 License

This project is licensed under the MIT License.
