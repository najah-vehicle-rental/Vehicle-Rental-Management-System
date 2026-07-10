package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Vehicle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    private final Path filePath = Path.of(
            "src",
            "main",
            "resources",
            "vehicles.txt"
    );

    public ArrayList<Vehicle> findAvailableVehicles() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();

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
        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                Vehicle vehicle = createVehicle(line);

                if (vehicle != null &&
                        vehicle.getId().equals(vehicleId)) {
                    return vehicle;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading vehicles file.");
        }

        return null;
    }

    public boolean updateStatus(String vehicleId, String newStatus) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            List<String> updatedLines = new ArrayList<>();
            boolean found = false;

            for (String line : lines) {
                Vehicle vehicle = createVehicle(line);

                if (vehicle != null &&
                        vehicle.getId().equals(vehicleId)) {

                    String updatedLine =
                            vehicle.getId() + "," +
                                    vehicle.getName() + "," +
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

        if (parts.length != 3) {
            return null;
        }

        return new Vehicle(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        );
    }
}