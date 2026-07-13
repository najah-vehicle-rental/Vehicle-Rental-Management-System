package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.ElectricVehicle;
import najah.eng.Application.Domain.Motorcycle;
import najah.eng.Application.Domain.Truck;
import najah.eng.Application.Domain.Van;
import najah.eng.Application.Domain.Vehicle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    private final Path filePath = Path.of(
            System.getProperty("user.dir"),
            "src",
            "main",
            "resources",
            "vehicles.txt"
    );

    public ArrayList<Vehicle> findAvailableVehicles() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        if (!Files.exists(filePath)) {
            System.out.println("Vehicles file not found.");
            System.out.println("Path: " + filePath);
            return vehicles;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                Vehicle vehicle = createVehicle(line);

                if (vehicle != null &&
                        vehicle.getStatus().equalsIgnoreCase("Available")) {
                    vehicles.add(vehicle);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading vehicles file.");
        }

        return vehicles;
    }

    public Vehicle findById(String vehicleId) {
        if (!Files.exists(filePath)) {
            System.out.println("Vehicles file not found.");
            return null;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                Vehicle vehicle = createVehicle(line);

                if (vehicle != null &&
                        vehicle.getId().equals(vehicleId.trim())) {
                    return vehicle;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading vehicles file.");
        }

        return null;
    }

    public boolean updateStatus(String vehicleId, String newStatus) {
        if (!Files.exists(filePath)) {
            System.out.println("Vehicles file not found.");
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            List<String> updatedLines = new ArrayList<>();
            boolean found = false;

            for (String line : lines) {
                Vehicle vehicle = createVehicle(line);

                if (vehicle != null &&
                        vehicle.getId().equals(vehicleId.trim())) {

                    String updatedLine =
                            vehicle.getId() + "," +
                                    vehicle.getName() + "," +
                                    vehicle.getType() + "," +
                                    newStatus;

                    updatedLines.add(updatedLine);
                    found = true;

                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                return false;
            }

            Files.write(
                    filePath,
                    updatedLines,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE
            );

            return true;

        } catch (IOException e) {
            System.out.println("Error updating vehicle status.");
            return false;
        }
    }

    private Vehicle createVehicle(String line) {
        String[] parts = line.split(",");

        if (parts.length != 4) {
            return null;
        }

        String id = parts[0].trim();
        String name = parts[1].trim();
        String type = parts[2].trim();
        String status = parts[3].trim();

        if (type.equalsIgnoreCase("Car")) {
            return new Car(id, name, status);
        }

        if (type.equalsIgnoreCase("Motorcycle")) {
            return new Motorcycle(id, name, status);
        }

        if (type.equalsIgnoreCase("Van")) {
            return new Van(id, name, status);
        }

        if (type.equalsIgnoreCase("Truck")) {
            return new Truck(id, name, status);
        }

        if (type.equalsIgnoreCase("Electric Vehicle")) {
            return new ElectricVehicle(id, name, status);
        }

        return null;
    }
}