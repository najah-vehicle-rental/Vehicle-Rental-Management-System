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

public class RentalRepository {

    private final Path filePath;

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

    public RentalRepository(Path filePath) {
        this.filePath = filePath;
    }

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
            System.out.println(
                    "Error saving rental."
            );

            return false;
        }
    }

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
            System.out.println(
                    "Error reading rentals file."
            );
        }

        return rentals;
    }

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
            System.out.println(
                    "Error closing rental."
            );

            return false;
        }
    }

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