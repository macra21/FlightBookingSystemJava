# Flight Booking System - Design and Programming Environments (Java)

This repository contains the **Java** implementation of the "Flight Booking System" project, developed for the Design and Programming Environments (MPP) course.

## Solution Organization

To maintain a clean version history and clearly highlight the project's evolution, the assignments are organized using **different branches** for each development stage. Ultimately, the final and fully functional solutions are merged into the `main` branch.

The project stages can be tracked as follows:
* `main` - Contains the stable, up-to-date version of the project.
* `assignment-week-3` - Branch dedicated to the domain model classes and repository interfaces.
* `assignment-week-4` - Branch dedicated to the database repository implementation and logging system.

## Technologies Used
* **Language:** Java
* **Build Tool:** Gradle
* **Database:** MySQL via JDBC
* **Logging:** Apache Log4j 2

---

## Assignment Status & Implemented Features

### Week 3: Domain Model and Interfaces
* Designed and implemented the Domain Model classes (`Flight`, `Booking`, `Employee`).
* Defined the Repository interfaces (`IFlightRepository`, `IBookingRepository`, `IEmployeeRepository`), establishing clear contracts for CRUD operations.

### Week 4: JDBC Persistence and Logging
* Implemented the repository interfaces using relational databases (the `JdbcRepository` classes).
* Configured the connection to the **MySQL** database. Authentication credentials (`url`, `user`, `password`) are dynamically extracted from an external configuration file (`config.properties`), ensuring no sensitive data is hardcoded in the source code.
* Integrated the **Log4j 2** logging framework.
    * The `log4j2.xml` configuration file is set up to record events in an external file (`logs/app.log`).
    * Application flow (method entries/exits via `traceEntry`, `traceExit`), as well as errors and exceptions (`error`, `catching`, `throwing`), are comprehensively logged.

---

## ⚙️ Setup and Run Instructions
1. **Database Setup:** Ensure you have an active MySQL server running. Execute the provided SQL scripts to create the required schema, or manually ensure the table structures (`flights`, `bookings`, `employees`) exist.
2. **Configuration File:** Before running the application, verify and update the `bd.properties` file (located in `src/main/resources`) to match your local MySQL server settings:
   ```properties
   jdbc.url=jdbc:mysql://localhost:3306/your_database_name
   jdbc.user=your_username
   jdbc.pass=your_password