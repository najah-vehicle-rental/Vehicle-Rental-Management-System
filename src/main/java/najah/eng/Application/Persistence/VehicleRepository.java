package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.factory.VehicleFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    private static final String VEHICLES_FILE_NOT_FOUND =
            "Vehicles file not found.";

    private final Path filePath;
    private final VehicleFactory vehicleFactory;

    public VehicleRepository() {
        this(
                Path.of(
                        System.getProperty("user.dir"),
                        "src",
                        "main",
                        "resources",
                        "vehicles.txt"
                ),
                new VehicleFactory()
        );
    }

    public VehicleRepository(Path filePath) {
        this(
                filePath,
                new VehicleFactory()
        );
    }

    public VehicleRepository(
            Path filePath,
            VehicleFactory vehicleFactory) {

        this.filePath = filePath;
        this.vehicleFactory = vehicleFactory;
    }

    public ArrayList<Vehicle> findAvailableVehicles() {
        ArrayList<Vehicle> vehicles =
                new ArrayList<>();

        if (!Files.exists(filePath)) {
            System.out.println(
                    VEHICLES_FILE_NOT_FOUND
            );
            return vehicles;
        }

        try {
            List<String> lines =
                    Files.readAllLines(filePath);

            for (String line : lines) {
                Vehicle vehicle =
                        createVehicle(line);

                if (vehicle != null &&
                        vehicle.getStatus()
                                .equalsIgnoreCase("Available")) {

                    vehicles.add(vehicle);
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error reading vehicles file."
            );
        }

        return vehicles;
    }

    public Vehicle findById(String vehicleId) {
        if (vehicleId == null ||
                vehicleId.isBlank()) {

            return null;
        }

        if (!Files.exists(filePath)) {
            System.out.println(
                    VEHICLES_FILE_NOT_FOUND
            );
            return null;
        }

        try {
            List<String> lines =
                    Files.readAllLines(filePath);

            for (String line : lines) {
                Vehicle vehicle =
                        createVehicle(line);

                if (vehicle != null &&
                        vehicle.getId().equals(
                                vehicleId.trim()
                        )) {

                    return vehicle;
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error reading vehicles file."
            );
        }

        return null;
    }

    public boolean updateStatus(
            String vehicleId,
            String newStatus) {

        if (vehicleId == null ||
                vehicleId.isBlank() ||
                newStatus == null ||
                newStatus.isBlank()) {

            return false;
        }

        if (!Files.exists(filePath)) {
            System.out.println(
                    VEHICLES_FILE_NOT_FOUND
            );
            return false;
        }

        try {
            List<String> lines =
                    Files.readAllLines(filePath);

            List<String> updatedLines =
                    new ArrayList<>();

            boolean found = false;

            for (String line : lines) {
                Vehicle vehicle =
                        createVehicle(line);

                if (vehicle != null &&
                        vehicle.getId().equals(
                                vehicleId.trim()
                        )) {

                    String updatedLine =
                            vehicle.getId() + "," +
                                    vehicle.getName() + "," +
                                    vehicle.getType() + "," +
                                    newStatus.trim();

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
            System.out.println(
                    "Error updating vehicle status."
            );
            return false;
        }
    }

    private Vehicle createVehicle(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split(",", -1);

        if (parts.length != 4) {
            return null;
        }

        return vehicleFactory.createVehicle(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim()
        );
    }
}