package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Rental;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class RentalRepository {

    private final Path filePath = Path.of(
            "src",
            "main",
            "resources",
            "rentals.txt"
    );

    public boolean save(Rental rental) {
        String record =
                rental.getVehicleId() + "," +
                        rental.getCustomerName() + "," +
                        rental.getRentalDays() + "," +
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
}