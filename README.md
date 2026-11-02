# Smart Food Delivery Management System

## 1. Project Overview

The Smart Food Delivery Management System is a Java-based application designed to manage food ordering, delivery operations, pricing, and payments.

The project demonstrates Object-Oriented Programming (OOP) principles, software design patterns, exception handling, automated testing, and code documentation.

It is developed using Java, Maven, and Eclipse IDE.

## 2. Main Features

### User Authentication
- Authenticates users using a username and password.
- Retrieves user information from `users.txt`.
- Rejects invalid credentials.
- Provides a console-based login interface.

### Restaurant Management
- Filters restaurants based on their availability.
- Retrieves available menu items.
- Supports restaurant-specific delivery zones and minimum order requirements.

### Order Management
- Creates standard, express, and scheduled orders.
- Validates restaurant availability, menu items, quantities, and delivery information.
- Assigns unique order IDs.
- Supports order cancellation and order status management.

### Driver and Delivery Management
- Supports multiple driver types, including bicycle, motorcycle, car, and electric vehicle drivers.
- Selects drivers using different selection strategies.
- Manages driver assignment and order delivery status.
- Supports delivery-related notifications.

### Pricing Management
The system supports multiple pricing strategies:

- **Normal Pricing:** Uses the original order total.
- **Express Pricing:** Adds a fixed fee of 10.
- **Peak Hour Pricing:** Increases the order total by 20%.
- **Promotional Pricing:** Applies a 10% discount.

Delivery costs are calculated based on distance.

### Payment Management
- Supports cash payments.
- Validates the payment amount against the final bill.
- Prevents duplicate payments.
- Accepts payment only when the order has been delivered.

## 3. Object-Oriented Programming Concepts

The project applies several OOP concepts:

- **Encapsulation:** Protects object data through fields and methods.
- **Inheritance:** Supports specialized order and driver classes.
- **Polymorphism:** Allows different implementations of common interfaces.
- **Abstraction:** Uses abstract classes and interfaces to define shared behavior.

## 4. Design Patterns

### Strategy Pattern

The Strategy Pattern is used to support interchangeable algorithms.

**Pricing strategies:**
- `PricingStrategy`
- `NormalPricing`
- `ExpressPricing`
- `PeakHourPricing`
- `PromotionalPricing`

**Driver selection strategies:**
- `DriverSelectionStrategy`
- `NearestDriverStrategy`
- `FastestDriverStrategy`

### Observer Pattern

The Observer Pattern is used to notify registered observers about delivery-related events.

Main components:
- `OrderObserver`
- `ConsoleOrderObserver`
- `DeliveryService`

The `DeliveryService` manages registered observers and notifies them when relevant delivery events occur.

## 5. Project Structure

```text
smart_food_delivery/
|
|-- pom.xml
|-- README.md
|
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   |-- smart_food_delivery/
|   |   |       |-- domain/
|   |   |       |-- service/
|   |   |       |-- repository/
|   |   |       |-- presentation/
|   |   |
|   |   |-- resources/
|   |       |-- users.txt
|   |
|   |-- test/
|       |-- java/
|           |-- smart_food_delivery/
|
|-- docs/
|   |-- javadoc/
```

### Package Responsibilities

| Package | Responsibility |
|---|---|
| `domain` | Core entities, order types, driver types, and statuses |
| `service` | Business logic, pricing, delivery, and authentication |
| `repository` | User data retrieval |
| `presentation` | Console-based user interface |

## 6. Technologies Used

- Java
- Eclipse IDE
- Apache Maven
- JUnit 5
- Mockito
- JaCoCo
- Javadoc
- Git and GitHub

## 7. How to Run the Project

### Requirements

- Java JDK 8 or later
- Eclipse IDE or another Java IDE
- Maven support

### Steps

1. Clone or download the project from GitHub.
2. Import the project into Eclipse as an existing Maven project.
3. Allow Maven to download the required dependencies.
4. Open `src/main/java/smart_food_delivery/presentation/LoginUI.java`.
5. Right-click the file and select **Run As → Java Application**.
6. Enter a username and password stored in `src/main/resources/users.txt`.

The application displays whether authentication was successful.

**Note:** The console entry point demonstrates login functionality. Other business features are implemented in the domain and service layers and exercised through automated tests.

## 8. Testing

The project includes automated unit and integration tests using JUnit 5 and Mockito.

Tests cover:

- User authentication
- Restaurant management
- Order creation and validation
- Order status transitions
- Driver selection and delivery
- Pricing calculations
- Cash payment processing
- Observer notifications
- Integration between system components

### Running Tests

From the project directory, execute:

```bash
mvn clean test
```

To run the full Maven verification lifecycle:

```bash
mvn clean verify
```

Tests can also be executed in Eclipse using **Run As → JUnit Test**.

## 9. Code Coverage

JaCoCo is integrated with Maven to measure automated test coverage.

The previously generated coverage report showed approximately:

- **Instruction Coverage:** 96%
- **Branch Coverage:** 89%

These figures reflect a previous successful test run and should be checked again after future modifications.

To generate an updated coverage report:

```bash
mvn clean verify
```

The report can be viewed at:

`target/site/jacoco/index.html`

## 10. Documentation

The project includes Javadoc documentation describing classes, constructors, and methods.

Generated documentation is available at:

`docs/javadoc/index.html`

## 11. UML Class Diagram

The UML Class Diagram documents the main classes, interfaces, inheritance relationships, and design patterns used in the system.

It provides a visual representation of the application's object-oriented structure.

## 12. Project Status

The project currently includes its core domain model, business services, design patterns, unit tests, integration tests, and Javadoc documentation.

Final verification and submission preparation should be completed before delivery.