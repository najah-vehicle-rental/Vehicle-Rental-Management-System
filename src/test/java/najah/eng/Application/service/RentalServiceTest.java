package najah.eng.Application.service;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Truck;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;
import najah.eng.Application.observer.RentalEvent;
import najah.eng.Application.observer.RentalEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RentalServiceTest {

    private VehicleRepository vehicleRepository;
    private RentalRepository rentalRepository;
    private RentalEventPublisher eventPublisher;
    private RentalService rentalService;

    @BeforeEach
    public void setUp() {
        vehicleRepository =
                mock(VehicleRepository.class);

        rentalRepository =
                mock(RentalRepository.class);

        eventPublisher =
                mock(RentalEventPublisher.class);

        rentalService =
                new RentalService(
                        vehicleRepository,
                        rentalRepository,
                        eventPublisher
                );
    }

    @Test
    public void successfulRentalSavesRental() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                rentalRepository
                        .findActiveRentalByVehicleId("1")
        ).thenReturn(null);

        when(
                vehicleRepository.updateStatus(
                        "1",
                        "Rented"
                )
        ).thenReturn(true);

        when(
                rentalRepository.save(
                        any(Rental.class)
                )
        ).thenReturn(true);

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        5
                );

        assertTrue(result);

        verify(rentalRepository)
                .save(any(Rental.class));

        verify(eventPublisher)
                .notifyObservers(
                        any(RentalEvent.class)
                );
    }

    @Test
    public void activeRentalPreventsDoubleBooking() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        Rental activeRental = new Rental(
                "1",
                "Ali",
                "ali@example.com",
                5,
                LocalDate.now().plusDays(5),
                "Active"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                rentalRepository
                        .findActiveRentalByVehicleId("1")
        ).thenReturn(activeRental);

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        5
                );

        assertFalse(result);

        verify(
                vehicleRepository,
                never()
        ).updateStatus(
                "1",
                "Rented"
        );
    }

    @Test
    public void unavailableVehicleIsRejected() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Rented"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        5
                );

        assertFalse(result);
    }

    @Test
    public void missingVehicleIsRejected() {
        when(
                vehicleRepository.findById("99")
        ).thenReturn(null);

        boolean result =
                rentalService.rentVehicle(
                        "99",
                        "Fadi",
                        "fadi@example.com",
                        5
                );

        assertFalse(result);
    }

    @Test
    public void invalidRentalDurationIsRejected() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        assertFalse(
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        0
                )
        );

        assertFalse(
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        31
                )
        );
    }

    @Test
    public void blankCustomerNameIsRejected() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "",
                        "fadi@example.com",
                        5
                );

        assertFalse(result);
    }

    @Test
    public void blankCustomerEmailIsRejected() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "",
                        5
                );

        assertFalse(result);
    }

    @Test
    public void saveFailureRestoresVehicleStatus() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                vehicleRepository.updateStatus(
                        "1",
                        "Rented"
                )
        ).thenReturn(true);

        when(
                rentalRepository.save(
                        any(Rental.class)
                )
        ).thenReturn(false);

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        5
                );

        assertFalse(result);

        verify(vehicleRepository)
                .updateStatus(
                        "1",
                        "Available"
                );

        verify(
                eventPublisher,
                never()
        ).notifyObservers(
                any(RentalEvent.class)
        );
    }

    @Test
    public void truckWithoutLicenseIsRejected() {
        Vehicle truck = new Truck(
                "10",
                "Volvo FH16",
                "Available"
        );

        when(
                vehicleRepository.findById("10")
        ).thenReturn(truck);

        boolean result =
                rentalService.rentVehicle(
                        "10",
                        "Fadi",
                        "fadi@example.com",
                        5,
                        30,
                        false,
                        0
                );

        assertFalse(result);
    }
}