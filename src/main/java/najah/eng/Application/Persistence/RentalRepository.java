package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Rental;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides file-based persistence operations for rental records.
 *
 * <p>The repository saves rentals, reads active rentals, searches for
 * active rentals by vehicle ID, and closes completed rental records.</p>
 */
public class RentalRepository {

    /**
     * Logger used to report file-access errors.
     */
    private static final Logger LOGGER =
            Logger.getLogger(RentalRepository.class.getName());

    /**
     * The path of the text file containing rental records.
     */
    private final Path filePath;

    /**
     * Creates a rental repository using the default rentals file.
     *
     * <p>The default file is located at
     * {@code src/main/resources/rentals.txt}.</p>
     */
    public RentalRepository() {
        this(
                Path.of(
                        System.getProperty("user.dir"),
                        "src",
                        "main",
                        "resources",
                        "rentals.txt"
                )
        );
    }

    /**
     * Creates a rental repository using a specified file.
     *
     * @param filePath the path of the file containing rental records
     */
    public RentalRepository(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Appends a rental record to the rentals file.
     *
     * @param rental the rental to save
     * @return {@code true} when the rental is saved successfully;
     *         otherwise {@code false}
     */
    public boolean save(Rental rental) {
        if (rental == null) {
            return false;
        }

        String record =
                rental.getVehicleId() + "," +
                        rental.getCustomerName() + "," +
                        rental.getCustomerEmail() + "," +
                        rental.getRentalDays() + "," +
                        rental.getExpiryDate() + "," +
                        rental.getStatus() +
                        System.lineSeparator();

        try {
            Files.writeString(
                    filePath,
                    record,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            return true;

        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Error saving rental.",
                    e
            );

            return false;
        }
    }

    /**
     * Reads and returns all rental records whose status is Active.
     *
     * <p>Empty and malformed records are ignored. When the file does
     * not exist, an empty list is returned.</p>
     *
     * @return a list containing all active rentals
     */
    public ArrayList<Rental> findActiveRentals() {
        ArrayList<Rental> rentals =
                new ArrayList<>();

        if (!Files.exists(filePath)) {
            return rentals;
        }

        try {
            List<String> lines =
                    Files.readAllLines(filePath);

            for (String line : lines) {
                Rental rental =
                        createRental(line);

                if (rental != null &&
                        rental.getStatus()
                                .equalsIgnoreCase("Active")) {

                    rentals.add(rental);
                }
            }

        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Error reading rentals file.",
                    e
            );
        }

        return rentals;
    }

    /**
     * Searches for an active rental using a vehicle identifier.
     *
     * @param vehicleId the identifier of the rented vehicle
     * @return the matching active rental, or {@code null} when no
     *         matching active rental is found
     */
    public Rental findActiveRentalByVehicleId(
            String vehicleId) {

        if (vehicleId == null ||
                vehicleId.isBlank()) {

            return null;
        }

        String id = vehicleId.trim();

        ArrayList<Rental> rentals =
                findActiveRentals();

        for (Rental rental : rentals) {
            if (rental.getVehicleId().equals(id)) {
                return rental;
            }
        }

        return null;
    }

    /**
     * Changes the first active rental for a vehicle to Closed.
     *
     * <p>The complete rentals file is rewritten after the matching
     * rental record has been updated.</p>
     *
     * @param vehicleId the identifier of the rented vehicle
     * @return {@code true} when an active rental is found and closed;
     *         otherwise {@code false}
     */
    public boolean closeActiveRental(
            String vehicleId) {

        if (vehicleId == null ||
                vehicleId.isBlank()) {

            return false;
        }

        if (!Files.exists(filePath)) {
            return false;
        }

        String id = vehicleId.trim();

        try {
            List<String> lines =
                    Files.readAllLines(filePath);

            List<String> updatedLines =
                    new ArrayList<>();

            boolean closed = false;

            for (String line : lines) {
                Rental rental =
                        createRental(line);

                if (!closed &&
                        rental != null &&
                        rental.getVehicleId().equals(id) &&
                        rental.getStatus()
                                .equalsIgnoreCase("Active")) {

                    String updatedRecord =
                            rental.getVehicleId() + "," +
                                    rental.getCustomerName() + "," +
                                    rental.getCustomerEmail() + "," +
                                    rental.getRentalDays() + "," +
                                    rental.getExpiryDate() + "," +
                                    "Closed";

                    updatedLines.add(updatedRecord);
                    closed = true;

                } else {
                    updatedLines.add(line);
                }
            }

            if (!closed) {
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
            LOGGER.log(
                    Level.SEVERE,
                    "Error closing rental.",
                    e
            );

            return false;
        }
    }

    /**
     * Converts one text-file record into a rental object.
     *
     * <p>A valid rental record must contain six comma-separated values:
     * vehicle ID, customer name, customer email, rental days,
     * expiry date, and status.</p>
     *
     * @param line the text-file record to convert
     * @return the created rental, or {@code null} when the record
     *         is empty, malformed, or contains invalid values
     */
    private Rental createRental(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split(",", -1);

        if (parts.length != 6) {
            return null;
        }

        try {
            return new Rental(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    Integer.parseInt(
                            parts[3].trim()
                    ),
                    LocalDate.parse(
                            parts[4].trim()
                    ),
                    parts[5].trim()
            );

        } catch (
                NumberFormatException |
                DateTimeParseException e) {

            return null;
        }
    }
}