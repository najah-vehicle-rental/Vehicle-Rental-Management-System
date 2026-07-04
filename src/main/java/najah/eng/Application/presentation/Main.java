package najah.eng.Application.presentation;

import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.service.AuthService;
import najah.eng.Application.service.VehicleService;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        VehicleService vehicleService = new VehicleService();

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
            System.out.println("2. Logout");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                ArrayList<Vehicle> vehicles = vehicleService.getAvailableVehicles();

                if (vehicles.isEmpty()) {
                    System.out.println("No available vehicles.");
                } else {
                    System.out.println("Available Vehicles:");

                    for (Vehicle vehicle : vehicles) {
                        System.out.println("ID: " + vehicle.getId());
                        System.out.println("Name: " + vehicle.getName());
                        System.out.println();
                    }
                }

            } else if (choice.equals("2")) {
                authService.logout();
                System.out.println("Logged out.");
            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}