package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.factory.VehicleFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides file-based persistence operations for vehicles.
 *
 * <p>The repository reads vehicle records from a comma-separated
 * text file, creates vehicle objects through {@link VehicleFactory},
 * searches for vehicles, and updates vehicle statuses.</p>
 */
public class VehicleRepository {

    /**
     * Message displayed when the vehicles file does not exist.
     */
    private static final String VEHICLES_FILE_NOT_FOUND =
            "Vehicles file not found.";

    /**
     * The path of the text file containing vehicle records.
     */
    private final Path filePath;

    /**
     * The factory used to create vehicle objects from file records.
     */
    private final VehicleFactory vehicleFactory;

    /**
     * Creates a vehicle repository using the default vehicles file
     * and a default vehicle factory.
     *
     * <p>The default file is located at
     * {@code src/main/resources/vehicles.txt}.</p>
     */
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

    /**
     * Creates a vehicle repository using a specified file and
     * a default vehicle factory.
     *
     * @param filePath the path of the file containing vehicle records
     */
    public VehicleRepository(Path filePath) {
        this(
                filePath,
                new VehicleFactory()
        );
    }

    /**
     * Creates a vehicle repository using a specified file and factory.
     *
     * @param filePath       the path of the vehicle records file
     * @param vehicleFactory the factory used to create vehicle objects
     */
    public VehicleRepository(
            Path filePath,
            VehicleFactory vehicleFactory) {

        this.filePath = filePath;
        this.vehicleFactory = vehicleFactory;
    }

    /**
     * Reads and returns all vehicles whose status is Available.
     *
     * <p>Invalid or malformed records are ignored. When the file does
     * not exist or cannot be read, an empty list is returned.</p>
     *
     * @return a list containing the available vehicles
     */
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

    /**
     * Searches for a vehicle using its unique identifier.
     *
     * @param vehicleId the identifier of the vehicle to find
     * @return the matching vehicle, or {@code null} when no matching
     *         vehicle is found
     */
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

    /**
     * Updates the status of the vehicle with the supplied identifier.
     *
     * <p>The complete vehicles file is rewritten after the matching
     * vehicle record has been updated.</p>
     *
     * @param vehicleId the identifier of the vehicle to update
     * @param newStatus the new status to store
     * @return {@code true} when the vehicle is found and updated;
     *         otherwise {@code false}
     */
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

    /**
     * Converts one text-file record into a vehicle object.
     *
     * <p>A valid vehicle record must contain four comma-separated
     * values: ID, name, type, and status.</p>
     *
     * @param line the text-file record to convert
     * @return the created vehicle, or {@code null} when the record
     *         is empty or malformed
     */
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