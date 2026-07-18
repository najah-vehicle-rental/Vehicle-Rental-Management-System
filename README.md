# Vehicle Rental Management System

A Java console application for managing vehicle rentals.

The system allows managers to log in, view available vehicles, rent vehicles, prevent double booking, validate vehicle-specific rental rules, return vehicles, calculate rental costs, apply late return penalties, and generate rental expiry reminders.

## Technologies

- Java
- Maven
- JUnit 5
- Mockito
- Text-file persistence
- Git and GitHub
- IntelliJ IDEA

## Project Architecture

The project follows a layered architecture:

- `Domain`: business entities
- `Persistence`: text-file repositories
- `service`: business logic
- `strategy`: vehicle rental validation strategies
- `observer`: rental event observers
- `factory`: vehicle creation
- `presentation`: console user interface

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── najah.eng.Application
│   │       ├── Domain
│   │       ├── Persistence
│   │       ├── factory
│   │       ├── observer
│   │       ├── presentation
│   │       ├── service
│   │       └── strategy
│   └── resources
│       ├── managers.txt
│       ├── vehicles.txt
│       └── rentals.txt
└── test
    └── java
        └── najah.eng.Application
            ├── Domain
            ├── Persistence
            ├── factory
            ├── observer
            ├── service
            └── strategy
```

## Functional Requirements

### Sprint 1: Authentication and Vehicle Catalog

- Manager login
- Invalid login rejection
- Manager logout
- Protected actions require login
- View available vehicles
- Hide rented and unavailable vehicles

### Sprint 2: Rental Operations

- Rent an available vehicle
- Save the rental record
- Change vehicle status to `Rented`
- Prevent double booking
- Reject a vehicle that already has an active rental
- Enforce a rental duration between 1 and 30 days
- Validate customer name and email
- Restore the vehicle status if saving the rental fails

### Sprint 3: Notifications

- Generate expiry reminders for active rentals
- Send reminder notifications through a notification interface
- Use a mocked notification service during unit testing

### Sprint 4: Returns and Billing

- Return a rented vehicle
- Change vehicle status to `Available`
- Close the active rental
- Restore vehicle status if closing the rental fails
- Calculate rental cost
- Calculate late-return days
- Apply late-return penalties
- Display the total rental cost

### Sprint 5: Vehicle Types

The system supports the following vehicle types:

- Car
- Van
- Truck
- Motorcycle
- Electric Vehicle

Each vehicle type is implemented as a subclass of the abstract `Vehicle` class.

## Business Rules

### General Rental Rules

- Minimum rental duration: 1 day
- Maximum rental duration: 30 days
- A vehicle must have the status `Available`
- A vehicle must not have another active rental
- Customer name is required
- Customer email is required

### Type-Specific Rules

- Cars have no additional requirements
- Vans have no additional requirements
- Trucks require a special truck license
- Motorcycles require the customer to be at least 18 years old
- Electric vehicles require a battery level between 30% and 100%

### Billing Rules

- Daily rental rate: 50 ILS
- Late-return penalty: 25 ILS per late day

```text
Rental Cost = Rental Days × Daily Rate

Late Penalty = Late Days × Late Penalty Per Day

Total Cost = Rental Cost + Late Penalty
```

## Design Patterns

The project contains three design patterns.

### 1. Strategy Pattern

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

Each `Vehicle` receives the correct strategy, and rental validation is delegated to that strategy.

This avoids placing all vehicle rules inside one large conditional method.

### 2. Observer Pattern

The Observer Pattern is used to react to rental events.

Subject:

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

Supported events:

```text
RENTED
RETURNED
LATE_RETURNED
```

When a rental event occurs, all registered observers are notified.

### 3. Factory Pattern

The Factory Pattern is used to create the correct vehicle subclass from the type stored in `vehicles.txt`.

Factory:

```text
VehicleFactory
```

The factory can create:

```text
Car
Van
Truck
Motorcycle
ElectricVehicle
```

This keeps object-creation logic outside `VehicleRepository`.

## Unit Testing

The project uses JUnit 5.

The test suite covers:

- Authentication
- Manager repository
- Vehicle repository
- Rental repository
- Vehicle factory
- Strategy rules
- Vehicle type rules
- Rental service
- Double-booking prevention
- Return service
- Billing service
- Reminder service
- Observer registration and notification
- Email observer behavior
- Rollback scenarios
- Missing and invalid file records

Current test result:

```text
Tests run: 63
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Mocking

Mockito is used to isolate classes during unit testing.

Mocked components include:

- `ManagerRepository`
- `VehicleRepository`
- `RentalRepository`
- `NotificationService`
- `RentalEventPublisher`
- `RentalObserver`

Mockito verifies:

- Repository interactions
- Notification sending
- Observer notifications
- Prevented operations
- Rollback behavior

## Text File Formats

### managers.txt

```text
username,password
```

Example:

```text
admin,1234
```

### vehicles.txt

```text
vehicleId,vehicleName,vehicleType,status
```

Example:

```text
1,Toyota Corolla,Car,Available
12,Tesla Model 3,Electric Vehicle,Available
```

Supported statuses:

```text
Available
Rented
Unavailable
```

### rentals.txt

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

### IntelliJ IDEA

1. Open the inner `Vehicle-Rental-Management-System` folder.
2. Reload the Maven project.
3. Open:

```text
src/main/java/najah/eng/Application/presentation/Main.java
```

4. Run `Main.main()`.

### Maven Tests

From IntelliJ:

```text
Maven → Lifecycle → test
```

Or with Maven installed:

```bash
mvn clean test
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

## Manual Acceptance Test

1. Log in using valid manager credentials.
2. Verify that invalid credentials are rejected.
3. View available vehicles.
4. Rent an available car.
5. Verify that its status changes to `Rented`.
6. Try to rent it again and verify rejection.
7. Rent a truck without a special license and verify rejection.
8. Rent a truck with a special license and verify acceptance.
9. Rent a motorcycle using an age below 18 and verify rejection.
10. Rent an electric vehicle using a battery level below 30 and verify rejection.
11. Return an active rented vehicle.
12. Verify that its status changes to `Available`.
13. Verify that the rental becomes `Closed`.
14. Verify the rental cost and late penalty.
15. Generate expiry reminders.
16. Run all Maven tests and verify `BUILD SUCCESS`.

## UML Diagram

The complete UML class diagram is stored in:

```text
docs/VehicleRentalSystem.puml
```

## Project Requirements Status

The complete requirements checklist is stored in:

```text
docs/REQUIREMENTS_CHECKLIST.md
```

## CI/CD

CI/CD is not included according to the project instructions.
