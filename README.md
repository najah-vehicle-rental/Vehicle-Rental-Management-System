# Vehicle Rental Management System

A Java console application for managing vehicle rentals.

The system allows managers to authenticate, view available vehicles, rent and return vehicles, prevent double booking, enforce rental policies, calculate rental costs and late penalties, and generate rental-expiry reminders.

## Features

* Manager login and logout
* Protection of manager-only operations
* Display of available vehicles
* Vehicle rental creation
* Double-booking prevention
* Rental-duration validation
* Customer-information validation
* Vehicle-specific rental rules
* Vehicle returns
* Rental-record closure
* Rental-cost calculation
* Late-return penalty calculation
* Rental-expiry reminders
* Observer notifications for rental events
* Text-file data persistence

## Technologies

* Java 8+
* Maven
* JUnit 5
* Mockito
* JaCoCo
* Git and GitHub
* PlantUML
* IntelliJ IDEA
* Text-file persistence

The project requirements also include GitHub Actions CI/CD and SonarQube. These integrations must be configured before the final submission.

## Project Architecture

The application follows a layered architecture.

* `Domain`: business entities and vehicle subclasses
* `Persistence`: repositories and text-file data access
* `service`: application and business logic
* `strategy`: vehicle-specific rental-validation strategies
* `observer`: rental-event publishers and observers
* `factory`: creation of the correct vehicle subclass
* `presentation`: console-based user interface

## Project Structure

```text
Vehicle-Rental-Management-System
├── docs
│   ├── REQUIREMENTS_CHECKLIST.md
│   └── VehicleRentalSystem.puml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── najah
│   │   │       └── eng
│   │   │           └── Application
│   │   │               ├── Domain
│   │   │               ├── Persistence
│   │   │               ├── factory
│   │   │               ├── observer
│   │   │               ├── presentation
│   │   │               ├── service
│   │   │               └── strategy
│   │   └── resources
│   │       ├── managers.txt
│   │       ├── rentals.txt
│   │       └── vehicles.txt
│   └── test
│       └── java
│           └── najah
│               └── eng
│                   └── Application
│                       ├── Domain
│                       ├── Persistence
│                       ├── factory
│                       ├── observer
│                       ├── service
│                       └── strategy
├── pom.xml
└── README.md
```

## Functional Requirements

### Sprint 1: Authentication and Vehicle Catalog

#### Manager Login

* Accept valid manager credentials.
* Reject invalid manager credentials.
* Display an appropriate login result.

#### Manager Logout

* Allow the manager to log out.
* Require authentication again before accessing protected operations.

#### View Available Vehicles

* Display vehicles with the status `Available`.
* Hide vehicles with the status `Rented`.
* Hide vehicles with the status `Unavailable`.

### Sprint 2: Rental Operations

#### Rent a Vehicle

* Rent an available vehicle.
* Create and save a rental record.
* Change the vehicle status to `Rented`.
* Validate the customer name and email.
* Restore the original vehicle status if saving the rental fails.

#### Prevent Double Booking

* Reject rental requests for vehicles that are not available.
* Reject rental requests when an active rental already exists.
* Prevent multiple active rentals for the same vehicle.

#### Rental-Duration Limits

* Minimum rental duration: 1 day
* Maximum rental duration: 30 days
* Reject rental periods outside the accepted range.

### Sprint 3: Notifications and Mocking

#### Rental Expiry Reminder

* Find active rentals that require reminders.
* Generate rental-expiry reminders.
* Send reminders through a notification interface.
* Mock the notification service during unit testing.

### Sprint 4: Returns and Billing

#### Return Vehicle

* Return a vehicle with an active rental.
* Change the vehicle status to `Available`.
* Close the active rental record.
* Restore the vehicle status to `Rented` if closing the rental fails.

#### Calculate Rental Cost

* Calculate the basic rental cost from the rental duration.
* Display the calculated rental cost.

#### Apply Late-Return Penalty

* Calculate the number of late days.
* Apply a penalty for every late day.
* Add the late penalty to the basic rental cost.
* Display the final total.

### Sprint 5: Vehicle Types and Polymorphism

The system supports the following vehicle types:

* Car
* Van
* Truck
* Motorcycle
* Electric Vehicle

Each vehicle type is implemented as a subclass of the abstract `Vehicle` class.

## Business Rules

### General Rental Rules

* The rental duration must be between 1 and 30 days.
* The vehicle must exist.
* The vehicle status must be `Available`.
* The vehicle must not have another active rental.
* The customer name must not be empty.
* The customer email must not be empty.
* The customer email must use a valid format.

### Type-Specific Rules

#### Car

Cars have no additional rental requirements.

#### Van

Vans have no additional rental requirements.

#### Truck

The customer must provide confirmation of a valid special truck licence.

#### Motorcycle

The customer must be at least 18 years old.

#### Electric Vehicle

The battery level must be between 30% and 100%.

## Billing Rules

The current billing implementation uses the following fixed values:

* Daily rental rate: 50 ILS
* Late-return penalty: 25 ILS per late day

```text
Rental Cost = Rental Days × Daily Rate

Late Penalty = Late Days × Late Penalty Per Day

Total Cost = Rental Cost + Late Penalty
```

## Design Patterns

### Strategy Pattern

The Strategy Pattern is used to apply different rental-validation rules to different vehicle types.

Strategy interface:

```text
RentalRuleStrategy
```

Concrete strategies:

```text
DefaultRentalRuleStrategy
TruckLicenseStrategy
MotorcycleAgeStrategy
ElectricBatteryStrategy
```

Each vehicle is assigned the appropriate rental-rule strategy. The rental service delegates type-specific validation to that strategy.

This design avoids placing every vehicle rule inside one large conditional method.

The current Strategy implementation handles rental validation. Pricing is currently calculated directly by `BillingService`.

### Observer Pattern

The Observer Pattern is used to react to rental events.

Publisher:

```text
RentalEventPublisher
```

Observer interface:

```text
RentalObserver
```

Concrete observers:

```text
EmailRentalObserver
AuditLogObserver
```

Supported rental events include:

```text
RENTED
RETURNED
LATE_RETURNED
```

When a rental event occurs, the publisher notifies all registered observers.

### Factory Pattern

The Factory Pattern is used to create the appropriate vehicle subclass from the vehicle type stored in `vehicles.txt`.

Factory class:

```text
VehicleFactory
```

The factory supports the creation of:

```text
Car
Van
Truck
Motorcycle
ElectricVehicle
```

This keeps vehicle-object creation outside `VehicleRepository` and avoids placing creation logic throughout the application.

## Data Persistence

The application stores data in text files under:

```text
src/main/resources
```

### managers.txt

Format:

```text
username,password
```

Example:

```text
admin,1234
```

### vehicles.txt

Format:

```text
vehicleId,vehicleName,vehicleType,status
```

Example:

```text
1,Toyota Corolla,Car,Available
12,Tesla Model 3,Electric Vehicle,Available
```

Supported vehicle types:

```text
Car
Van
Truck
Motorcycle
Electric Vehicle
```

Supported vehicle statuses:

```text
Available
Rented
Unavailable
```

### rentals.txt

Format:

```text
vehicleId,customerName,customerEmail,rentalDays,expiryDate,status
```

Example:

```text
1,Fadi,fadi@example.com,5,2026-07-23,Active
```

Supported rental statuses:

```text
Active
Closed
```

## Running the Application

### Using IntelliJ IDEA

1. Open the `Vehicle-Rental-Management-System` project folder.
2. Allow IntelliJ IDEA to load the Maven project.
3. Open:

```text
src/main/java/najah/eng/Application/presentation/Main.java
```

4. Run the `main` method.

### Using Maven

Make sure Java and Maven are installed.

Compile the project:

```bash
mvn clean compile
```

Run the tests:

```bash
mvn clean test
```

Run tests and generate the JaCoCo report:

```bash
mvn clean verify
```

## Console Menu

```text
1. View Available Vehicles
2. Rent a Vehicle
3. Return a Vehicle
4. Generate Expiry Reminders
5. Logout
6. Exit
```

## Unit Testing

The project uses JUnit 5.

The test suite covers areas including:

* Manager authentication
* Manager logout
* Protected operations
* Manager repository
* Vehicle repository
* Rental repository
* Vehicle factory
* Vehicle subclasses
* General rental rules
* Truck licence validation
* Motorcycle age validation
* Electric-vehicle battery validation
* Rental service
* Double-booking prevention
* Rental-duration validation
* Return service
* Billing service
* Reminder service
* Observer registration
* Observer removal
* Observer notification
* Email observer behavior
* Repository failures
* Rollback behavior
* Missing files
* Invalid file records

Run the complete test suite before submission:

```bash
mvn clean verify
```

The final README must only include an exact test result after it has been confirmed from the latest Maven execution.

Example format:

```text
Tests run: <actual number>
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Mocking

Mockito is used to isolate dependencies during unit testing.

Mocked components include:

* `ManagerRepository`
* `VehicleRepository`
* `RentalRepository`
* `NotificationService`
* `RentalEventPublisher`
* `RentalObserver`

Mockito is used to verify:

* Repository interactions
* Notification delivery
* Observer notifications
* Rejected operations
* Unwanted interactions
* Rollback behavior

## Code Coverage

JaCoCo is configured through Maven to measure test coverage.

Generate the coverage report with:

```bash
mvn clean verify
```

Open the generated report from:

```text
target/site/jacoco/index.html
```

The final coverage percentage should be recorded only after successfully running the latest version of the complete test suite.

## Javadocs

The project requirements state that all classes, constructors, methods, and fields must have Javadoc documentation.

Generate the Javadoc documentation with:

```bash
mvn javadoc:javadoc
```

The generated documentation is normally available under:

```text
target/site/apidocs
```

All source files should be reviewed to ensure that the Javadoc requirement is fully satisfied before submission.

## UML Class Diagram

The PlantUML source for the class diagram is stored in:

```text
docs/VehicleRentalSystem.puml
```

A rendered version should also be exported before submission, for example:

```text
docs/VehicleRentalSystem.png
```

or:

```text
docs/VehicleRentalSystem.pdf
```

## Manual Acceptance Test

1. Start the application.
2. Log in using valid manager credentials.
3. Verify that invalid credentials are rejected.
4. View the available vehicles.
5. Confirm that unavailable and rented vehicles are hidden.
6. Rent an available car.
7. Verify that its status changes to `Rented`.
8. Try to rent the same vehicle again.
9. Verify that the second rental is rejected.
10. Try a rental duration below 1 day.
11. Try a rental duration above 30 days.
12. Verify that both invalid durations are rejected.
13. Rent a truck without a special licence.
14. Verify that the rental is rejected.
15. Rent a truck with a special licence.
16. Verify that the rental is accepted.
17. Rent a motorcycle for a customer under 18.
18. Verify that the rental is rejected.
19. Rent an electric vehicle with a battery level below 30%.
20. Verify that the rental is rejected.
21. Return a vehicle with an active rental.
22. Verify that the vehicle status changes to `Available`.
23. Verify that the rental status changes to `Closed`.
24. Verify the basic rental cost.
25. Verify the number of late days.
26. Verify the late-return penalty.
27. Verify the total rental cost.
28. Generate rental-expiry reminders.
29. Log out.
30. Verify that protected actions require another login.
31. Run `mvn clean verify`.
32. Confirm that all tests pass.
33. Review the JaCoCo coverage report.

## GitHub Actions CI/CD

GitHub Actions CI/CD is required by the project specification.

The workflow should be stored under:

```text
.github/workflows
```

For example:

```text
.github/workflows/maven.yml
```

The workflow should perform at least the following operations:

* Check out the repository.
* Configure the required Java version.
* Build the Maven project.
* Run all JUnit tests.
* Generate the JaCoCo coverage report.
* Fail when the build or tests fail.

At the time of writing, this integration still needs to be added to the repository.

## SonarQube

SonarQube analysis is required by the project specification.

The project should be configured to analyze:

* Bugs
* Vulnerabilities
* Code smells
* Duplicated code
* Maintainability
* Test coverage

At the time of writing, SonarQube integration still needs to be added to the repository.

## Requirements Checklist

The requirements checklist is stored in:

```text
docs/REQUIREMENTS_CHECKLIST.md
```

The checklist should be updated after running the final tests, generating coverage, configuring CI/CD, configuring SonarQube, completing Javadocs, and exporting the UML diagram.

## AI Refactoring Report

The project specification requires an AI refactoring report containing:

1. Files refactored with AI assistance
2. Prompts used
3. Original code
4. Refactored code
5. Reasons for accepting or rejecting the suggestions

The report should be added before submission, for example:

```text
docs/AI_REFACTORING_REPORT.md
```

The report must accurately describe the work performed and follow the university’s AI-usage policy.

## Current Submission Status

The main Sprint 1–5 functionality is implemented.

Before final submission, verify or complete the following:

* Run the complete Maven test suite.
* Record the real test result.
* Generate and review the JaCoCo coverage report.
* Add Javadocs to all required source elements.
* Export the UML diagram to PNG or PDF.
* Configure GitHub Actions CI/CD.
* Configure SonarQube.
* Add the AI Refactoring Report.
* Confirm that all required files are committed and pushed to GitHub.
