# Vehicle Rental Management System Requirements Checklist

## Authentication and Vehicle Catalog

- [x] Manager login
- [x] Invalid credentials rejected
- [x] Manager logout
- [x] Protected operations require login
- [x] Display available vehicles
- [x] Hide rented and unavailable vehicles

## Rental Operations

- [x] Rent an available vehicle
- [x] Create rental record
- [x] Change vehicle status to Rented
- [x] Prevent double booking using vehicle status
- [x] Prevent double booking using active rental records
- [x] Rental duration between 1 and 30 days
- [x] Validate customer name and email
- [x] Restore vehicle status when saving fails

## Notifications

- [x] Generate rental expiry reminders
- [x] NotificationService interface
- [x] EmailNotificationService implementation
- [x] Mock notification service during tests

## Returns and Billing

- [x] Return rented vehicle
- [x] Close active rental
- [x] Change vehicle status to Available
- [x] Restore status when closing fails
- [x] Calculate rental cost
- [x] Calculate late days
- [x] Apply late penalty
- [x] Calculate total cost

## Vehicle Types

- [x] Abstract Vehicle class
- [x] Car
- [x] Van
- [x] Truck
- [x] Motorcycle
- [x] ElectricVehicle
- [x] Truck special-license rule
- [x] Motorcycle minimum-age rule
- [x] Electric vehicle battery rule

## Design Patterns

- [x] Strategy Pattern
- [x] Observer Pattern
- [x] Factory Pattern
- [x] Multiple Strategy implementations
- [x] Multiple Observer implementations
- [x] Observer registration and removal
- [x] Vehicle creation through VehicleFactory

## Unit Testing and Mocking

- [x] JUnit 5
- [x] Mockito
- [x] Repository tests
- [x] Service tests
- [x] Strategy tests
- [x] Factory tests
- [x] Observer tests
- [x] Billing tests
- [x] Authentication tests
- [x] Double-booking tests
- [x] Rollback tests
- [x] Temporary file tests

## Test Result

```text
Tests run: 63
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS