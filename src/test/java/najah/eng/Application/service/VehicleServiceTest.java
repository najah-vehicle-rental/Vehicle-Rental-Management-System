package najah.eng.Application.service;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VehicleServiceTest {

    private VehicleRepository vehicleRepository;
    private VehicleService vehicleService;

    @BeforeEach
    public void setUp() {
        vehicleRepository =
                mock(VehicleRepository.class);

        vehicleService =
                new VehicleService(
                        vehicleRepository
                );
    }

    @Test
    public void returnsAvailableVehicles() {
        ArrayList<Vehicle> expected =
                new ArrayList<>();

        expected.add(
                new Car(
                        "1",
                        "Toyota Corolla",
                        "Available"
                )
        );

        when(
                vehicleRepository
                        .findAvailableVehicles()
        ).thenReturn(expected);

        ArrayList<Vehicle> result =
                vehicleService
                        .getAvailableVehicles();

        assertSame(expected, result);

        verify(vehicleRepository)
                .findAvailableVehicles();
    }

    @Test
    public void returnsVehicleById() {
        Vehicle expected =
                new Car(
                        "1",
                        "Toyota Corolla",
                        "Available"
                );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(expected);

        Vehicle result =
                vehicleService
                        .getVehicleById("1");

        assertSame(expected, result);

        verify(vehicleRepository)
                .findById("1");
    }
}