package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Rental;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RentalRepositoryTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void savesRentalAndFindsIt() {
        Path file =
                tempDirectory.resolve(
                        "rentals.txt"
                );

        RentalRepository repository =
                new RentalRepository(file);

        Rental rental = createRental(
                "1",
                "Active"
        );

        assertTrue(repository.save(rental));

        assertEquals(
                1,
                repository
                        .findActiveRentals()
                        .size()
        );
    }

    @Test
    public void findsActiveRentalByVehicleId()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "rentals.txt"
                );

        Files.writeString(
                file,
                "1,Fadi,fadi@example.com,5,2026-07-23,Active\n"
        );

        RentalRepository repository =
                new RentalRepository(file);

        Rental rental =
                repository
                        .findActiveRentalByVehicleId(
                                "1"
                        );

        assertNotNull(rental);

        assertEquals(
                "Fadi",
                rental.getCustomerName()
        );
    }

    @Test
    public void closesActiveRental()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "rentals.txt"
                );

        Files.writeString(
                file,
                "1,Fadi,fadi@example.com,5,2026-07-23,Active\n"
        );

        RentalRepository repository =
                new RentalRepository(file);

        assertTrue(
                repository.closeActiveRental("1")
        );

        assertNull(
                repository
                        .findActiveRentalByVehicleId(
                                "1"
                        )
        );
    }

    @Test
    public void closedRentalsAreNotActive()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "rentals.txt"
                );

        Files.writeString(
                file,
                "1,Fadi,fadi@example.com,5,2026-07-23,Closed\n"
        );

        RentalRepository repository =
                new RentalRepository(file);

        assertTrue(
                repository
                        .findActiveRentals()
                        .isEmpty()
        );
    }

    @Test
    public void invalidRecordsAreIgnored()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "rentals.txt"
                );

        Files.writeString(
                file,
                "invalid record\n" +
                        "1,Fadi,fadi@example.com,5,2026-07-23,Active\n"
        );

        RentalRepository repository =
                new RentalRepository(file);

        assertEquals(
                1,
                repository
                        .findActiveRentals()
                        .size()
        );
    }

    @Test
    public void nullRentalCannotBeSaved() {
        Path file =
                tempDirectory.resolve(
                        "rentals.txt"
                );

        RentalRepository repository =
                new RentalRepository(file);

        assertFalse(
                repository.save(null)
        );
    }

    @Test
    public void saveReturnsFalseWhenFileCannotBeWritten() {
        RentalRepository repository =
                new RentalRepository(tempDirectory);

        Rental rental = createRental(
                "1",
                "Active"
        );

        assertFalse(
                repository.save(rental)
        );
    }

    @Test
    public void findActiveRentalsReturnsEmptyWhenFileCannotBeRead() {
        RentalRepository repository =
                new RentalRepository(tempDirectory);

        assertTrue(
                repository
                        .findActiveRentals()
                        .isEmpty()
        );
    }

    @Test
    public void closeActiveRentalReturnsFalseWhenFileCannotBeRead() {
        RentalRepository repository =
                new RentalRepository(tempDirectory);

        assertFalse(
                repository.closeActiveRental("1")
        );
    }

    private Rental createRental(
            String vehicleId,
            String status) {

        return new Rental(
                vehicleId,
                "Fadi",
                "fadi@example.com",
                5,
                LocalDate.of(2026, 7, 23),
                status
        );
    }
}