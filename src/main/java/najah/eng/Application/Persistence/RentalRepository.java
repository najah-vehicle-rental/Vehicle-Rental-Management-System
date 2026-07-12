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

    private final Path filePath = Path.of(
            System.getProperty("user.dir"),
            "src",
            "main",
            "resources",
            "rentals.txt"
    );

    public boolean save(Rental rental) {
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
            System.out.println("Error saving rental.");
            return false;
        }
    }

    public ArrayList<Rental> findActiveRentals() {
        ArrayList<Rental> rentals = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return rentals;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                Rental rental = createRental(line);

                if (rental != null &&
                        rental.getStatus().equalsIgnoreCase("Active")) {
                    rentals.add(rental);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading rentals file.");
        }

        return rentals;
    }

    private Rental createRental(String line) {
        String[] parts = line.split(",");

        if (parts.length != 6) {
            return null;
        }

        try {
            return new Rental(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    Integer.parseInt(parts[3].trim()),
                    LocalDate.parse(parts[4].trim()),
                    parts[5].trim()
            );

        } catch (NumberFormatException | DateTimeParseException e) {
            return null;
        }
    }
}