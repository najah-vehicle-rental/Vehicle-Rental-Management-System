package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.ElectricVehicle;
import najah.eng.Application.Domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleRepositoryTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void findsOnlyAvailableVehicles()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "vehicles.txt"
                );

        Files.writeString(
                file,
                "1,Toyota Corolla,Car,Available\n" +
                        "2,Honda Civic,Car,Rented\n" +
                        "3,Tesla Model 3,Electric Vehicle,Available\n"
        );

        VehicleRepository repository =
                new VehicleRepository(file);

        ArrayList<Vehicle> vehicles =
                repository.findAvailableVehicles();

        assertEquals(2, vehicles.size());
    }

    @Test
    public void findsVehicleById()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "vehicles.txt"
                );

        Files.writeString(
                file,
                "12,Tesla Model 3,Electric Vehicle,Available\n"
        );

        VehicleRepository repository =
                new VehicleRepository(file);

        Vehicle vehicle =
                repository.findById("12");

        assertInstanceOf(
                ElectricVehicle.class,
                vehicle
        );

        assertEquals(
                "Tesla Model 3",
                vehicle.getName()
        );
    }

    @Test
    public void updatesVehicleStatus()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "vehicles.txt"
                );

        Files.writeString(
                file,
                "1,Toyota Corolla,Car,Available\n"
        );

        VehicleRepository repository =
                new VehicleRepository(file);

        assertTrue(
                repository.updateStatus(
                        "1",
                        "Rented"
                )
        );

        Vehicle vehicle =
                repository.findById("1");

        assertEquals(
                "Rented",
                vehicle.getStatus()
        );
    }

    @Test
    public void invalidRecordsAreIgnored()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "vehicles.txt"
                );

        Files.writeString(
                file,
                "invalid record\n" +
                        "1,Toyota Corolla,Car,Available\n"
        );

        VehicleRepository repository =
                new VehicleRepository(file);

        assertEquals(
                1,
                repository
                        .findAvailableVehicles()
                        .size()
        );
    }

    @Test
    public void missingFileReturnsSafeResults() {
        Path file =
                tempDirectory.resolve(
                        "missing.txt"
                );

        VehicleRepository repository =
                new VehicleRepository(file);

        assertTrue(
                repository
                        .findAvailableVehicles()
                        .isEmpty()
        );

        assertNull(
                repository.findById("1")
        );

        assertFalse(
                repository.updateStatus(
                        "1",
                        "Rented"
                )
        );
    }
}