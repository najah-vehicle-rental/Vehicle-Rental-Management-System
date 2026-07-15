package najah.eng.Application.presentation;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.service.AuthService;
import najah.eng.Application.service.BillingService;
import najah.eng.Application.service.EmailNotificationService;
import najah.eng.Application.service.NotificationService;
import najah.eng.Application.service.ReminderService;
import najah.eng.Application.service.RentalService;
import najah.eng.Application.service.ReturnService;
import najah.eng.Application.service.VehicleService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Scanner scanner = new Scanner(System.in);

        AuthService authService = new AuthService();
        VehicleService vehicleService = new VehicleService();
        RentalService rentalService = new RentalService();
        ReturnService returnService = new ReturnService();
        BillingService billingService = new BillingService();

        RentalRepository rentalRepository =
                new RentalRepository();

        NotificationService notificationService =
                new EmailNotificationService();

        ReminderService reminderService =
                new ReminderService(
                        rentalRepository,
                        notificationService
                );

        while (true) {
            if (!authService.isLoggedIn()) {
                System.out.print("Username: ");
                String username = scanner.nextLine();

                System.out.print("Password: ");
                String password = scanner.nextLine();

                if (authService.login(username, password)) {
                    System.out.println("Login successful.");
                } else {
                    System.out.println("Invalid username or password.");
                    continue;
                }
            }

            showMenu();

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                showAvailableVehicles(vehicleService);

            } else if (choice.equals("2")) {
                rentVehicle(
                        scanner,
                        rentalService,
                        vehicleService
                );

            } else if (choice.equals("3")) {
                returnVehicle(
                        scanner,
                        returnService,
                        billingService
                );

            } else if (choice.equals("4")) {
                generateReminders(reminderService);

            } else if (choice.equals("5")) {
                authService.logout();
                System.out.println("Logged out.");

            } else if (choice.equals("6")) {
                break;

            } else {
                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("1. View Available Vehicles");
        System.out.println("2. Rent a Vehicle");
        System.out.println("3. Return a Vehicle");
        System.out.println("4. Generate Expiry Reminders");
        System.out.println("5. Logout");
        System.out.println("6. Exit");
        System.out.print("Choose: ");
    }

    private static void showAvailableVehicles(
            VehicleService vehicleService) {

        ArrayList<Vehicle> vehicles =
                vehicleService.getAvailableVehicles();

        if (vehicles.isEmpty()) {
            System.out.println("No available vehicles.");
            return;
        }

        System.out.println("Available Vehicles:");

        for (Vehicle vehicle : vehicles) {
            System.out.println("ID: " + vehicle.getId());
            System.out.println("Name: " + vehicle.getName());
            System.out.println("Type: " + vehicle.getType());
            System.out.println("Status: " + vehicle.getStatus());
            System.out.println(
                    "Rental Rule: " +
                            vehicle.getRuleDescription()
            );
            System.out.println();
        }
    }

    private static void rentVehicle(
            Scanner scanner,
            RentalService rentalService,
            VehicleService vehicleService) {

        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        if (vehicle == null) {
            System.out.println("Vehicle was not found.");
            return;
        }

        if (!rentalService.isVehicleAvailable(vehicleId)) {
            System.out.println("Vehicle is not available.");
            return;
        }

        System.out.println(
                "Vehicle Type: " +
                        vehicle.getType()
        );

        System.out.println(
                "Rental Rule: " +
                        vehicle.getRuleDescription()
        );

        System.out.print("Customer Name: ");
        String customerName = scanner.nextLine();

        if (customerName.isBlank()) {
            System.out.println("Customer name is required.");
            return;
        }

        System.out.print("Customer Email: ");
        String customerEmail = scanner.nextLine();

        if (customerEmail.isBlank()) {
            System.out.println("Customer email is required.");
            return;
        }

        System.out.print("Rental Days: ");
        String daysText = scanner.nextLine();

        try {
            int rentalDays =
                    Integer.parseInt(daysText);

            if (!rentalService.isRentalDurationValid(rentalDays)) {
                System.out.println(
                        "Rental period must be between 1 and 30 days."
                );
                return;
            }

            int customerAge = 0;
            boolean hasSpecialLicense = false;
            int batteryLevel = 0;

            if (vehicle.getType().equalsIgnoreCase("Truck")) {
                System.out.print(
                        "Does the customer have a special truck license? (yes/no): "
                );

                String licenseAnswer =
                        scanner.nextLine().trim();

                hasSpecialLicense =
                        licenseAnswer.equalsIgnoreCase("yes")
                                || licenseAnswer.equalsIgnoreCase("y");
            }

            if (vehicle.getType()
                    .equalsIgnoreCase("Electric Vehicle")) {

                System.out.print(
                        "Battery Level (0-100): "
                );

                batteryLevel =
                        Integer.parseInt(
                                scanner.nextLine()
                        );
            }

            if (vehicle.getType()
                    .equalsIgnoreCase("Motorcycle")) {

                System.out.print("Customer Age: ");

                customerAge =
                        Integer.parseInt(
                                scanner.nextLine()
                        );
            }

            boolean ruleValid =
                    rentalService.isTypeSpecificRuleValid(
                            vehicleId,
                            customerAge,
                            hasSpecialLicense,
                            batteryLevel
                    );

            if (!ruleValid) {
                System.out.println(
                        "Rental rejected: " +
                                vehicle.getRuleDescription()
                );
                return;
            }

            boolean rented =
                    rentalService.rentVehicle(
                            vehicleId,
                            customerName,
                            customerEmail,
                            rentalDays,
                            customerAge,
                            hasSpecialLicense,
                            batteryLevel
                    );

            if (rented) {
                System.out.println(
                        "Vehicle rented successfully."
                );
            } else {
                System.out.println("Rental failed.");
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Rental days, customer age, and battery level must be valid numbers."
            );
        }
    }

    private static void returnVehicle(
            Scanner scanner,
            ReturnService returnService,
            BillingService billingService) {

        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Rental rental =
                returnService.getActiveRental(vehicleId);

        if (rental == null) {
            System.out.println(
                    "Vehicle return failed. No active rental found."
            );
            return;
        }

        LocalDate returnDate = LocalDate.now();

        double rentalCost =
                billingService.calculateRentalCost(
                        rental.getRentalDays()
                );

        long lateDays =
                billingService.calculateLateDays(
                        rental.getExpiryDate(),
                        returnDate
                );

        double latePenalty =
                billingService.calculateLatePenalty(
                        rental.getExpiryDate(),
                        returnDate
                );

        double totalCost =
                billingService.calculateTotalCost(
                        rental.getRentalDays(),
                        rental.getExpiryDate(),
                        returnDate
                );

        boolean returned =
                returnService.returnVehicle(vehicleId);

        if (returned) {
            System.out.println(
                    "Vehicle returned successfully."
            );

            System.out.println(
                    "Expiry Date: " +
                            rental.getExpiryDate()
            );

            System.out.println(
                    "Return Date: " +
                            returnDate
            );

            System.out.println(
                    "Rental Days: " +
                            rental.getRentalDays()
            );

            System.out.printf(
                    Locale.US,
                    "Daily Rate: %.2f ILS%n",
                    billingService.getDailyRate()
            );

            System.out.printf(
                    Locale.US,
                    "Rental Cost: %.2f ILS%n",
                    rentalCost
            );

            System.out.println(
                    "Late Days: " +
                            lateDays
            );

            System.out.printf(
                    Locale.US,
                    "Late Penalty Per Day: %.2f ILS%n",
                    billingService.getLatePenaltyPerDay()
            );

            System.out.printf(
                    Locale.US,
                    "Late Return Penalty: %.2f ILS%n",
                    latePenalty
            );

            System.out.printf(
                    Locale.US,
                    "Total Rental Cost: %.2f ILS%n",
                    totalCost
            );

        } else {
            System.out.println(
                    "Vehicle return failed. Check the vehicle and rental status."
            );
        }
    }

    private static void generateReminders(
            ReminderService reminderService) {

        int reminderCount =
                reminderService.generateExpiryReminders();

        if (reminderCount == 0) {
            System.out.println(
                    "No expiry reminders generated."
            );
        } else {
            System.out.println(
                    reminderCount +
                            " expiry reminder generated."
            );
        }
    }
}