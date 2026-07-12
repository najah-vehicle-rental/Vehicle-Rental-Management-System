package najah.eng.Application.presentation;

import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.service.AuthService;
import najah.eng.Application.service.EmailNotificationService;
import najah.eng.Application.service.NotificationService;
import najah.eng.Application.service.ReminderService;
import najah.eng.Application.service.RentalService;
import najah.eng.Application.service.VehicleService;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AuthService authService = new AuthService();
        VehicleService vehicleService = new VehicleService();
        RentalService rentalService = new RentalService();

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

            System.out.println("1. View Available Vehicles");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. Generate Expiry Reminders");
            System.out.println("4. Logout");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                showAvailableVehicles(vehicleService);

            } else if (choice.equals("2")) {
                rentVehicle(scanner, rentalService);

            } else if (choice.equals("3")) {
                generateReminders(reminderService);

            } else if (choice.equals("4")) {
                authService.logout();
                System.out.println("Logged out.");

            } else if (choice.equals("5")) {
                break;

            } else {
                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
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
            System.out.println();
        }
    }

    private static void rentVehicle(
            Scanner scanner,
            RentalService rentalService) {

        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        if (!rentalService.isVehicleAvailable(vehicleId)) {
            System.out.println("Vehicle is not available.");
            return;
        }

        System.out.print("Customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Customer Email: ");
        String customerEmail = scanner.nextLine();

        if (customerEmail.isBlank()) {
            System.out.println("Customer email is required.");
            return;
        }

        System.out.print("Rental Days: ");
        String daysText = scanner.nextLine();

        try {
            int rentalDays = Integer.parseInt(daysText);

            if (!rentalService.isRentalDurationValid(rentalDays)) {
                System.out.println(
                        "Rental period must be between 1 and 30 days."
                );
                return;
            }

            boolean rented = rentalService.rentVehicle(
                    vehicleId,
                    customerName,
                    customerEmail,
                    rentalDays
            );

            if (rented) {
                System.out.println("Vehicle rented successfully.");
            } else {
                System.out.println("Rental failed.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Rental days must be a number.");
        }
    }

    private static void generateReminders(
            ReminderService reminderService) {

        int reminderCount =
                reminderService.generateExpiryReminders();

        if (reminderCount == 0) {
            System.out.println("No expiry reminders generated.");
        } else {
            System.out.println(
                    reminderCount +
                            " expiry reminder generated."
            );
        }
    }
}