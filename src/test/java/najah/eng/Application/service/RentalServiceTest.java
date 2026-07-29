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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void constructorsCreateRentalService() {
        assertNotNull(
                new RentalService()
        );

        assertNotNull(
                new RentalService(
                        vehicleRepository,
                        rentalRepository
                )
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
    public void nullOrBlankVehicleIdIsRejected() {
        assertFalse(
                rentalService.rentVehicle(
                        null,
                        "Fadi",
                        "fadi@example.com",
                        5
                )
        );

        assertFalse(
                rentalService.rentVehicle(
                        "   ",
                        "Fadi",
                        "fadi@example.com",
                        5
                )
        );

        verify(
                vehicleRepository,
                never()
        ).findById(any());
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
    public void nullCustomerNameIsRejected() {
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
                        null,
                        "fadi@example.com",
                        5
                )
        );
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
    public void nullCustomerEmailIsRejected() {
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
                        null,
                        5
                )
        );
    }

    @Test
    public void vehicleStatusUpdateFailureIsRejected() {
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
        ).thenReturn(false);

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        5
                );

        assertFalse(result);

        verify(
                rentalRepository,
                never()
        ).save(any(Rental.class));

        verify(
                eventPublisher,
                never()
        ).notifyObservers(
                any(RentalEvent.class)
        );
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

    @Test
    public void availableVehicleWithoutActiveRentalIsAvailable() {
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

        assertTrue(
                rentalService.isVehicleAvailable("1")
        );
    }

    @Test
    public void nullOrBlankVehicleIdIsNotAvailable() {
        assertFalse(
                rentalService.isVehicleAvailable(null)
        );

        assertFalse(
                rentalService.isVehicleAvailable("   ")
        );
    }

    @Test
    public void missingVehicleIsNotAvailable() {
        when(
                vehicleRepository.findById("99")
        ).thenReturn(null);

        assertFalse(
                rentalService.isVehicleAvailable("99")
        );
    }

    @Test
    public void vehicleWithActiveRentalIsNotAvailable() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        Rental activeRental =
                mock(Rental.class);

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                rentalRepository
                        .findActiveRentalByVehicleId("1")
        ).thenReturn(activeRental);

        assertFalse(
                rentalService.isVehicleAvailable("1")
        );
    }

    @Test
    public void rentedVehicleIsNotAvailable() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Rented"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        assertFalse(
                rentalService.isVehicleAvailable("1")
        );
    }

    @Test
    public void rentalDurationBoundariesAreValidated() {
        assertTrue(
                rentalService.isRentalDurationValid(1)
        );

        assertTrue(
                rentalService.isRentalDurationValid(30)
        );

        assertFalse(
                rentalService.isRentalDurationValid(0)
        );

        assertFalse(
                rentalService.isRentalDurationValid(31)
        );
    }

    @Test
    public void nullOrBlankVehicleIdFailsTypeSpecificRule() {
        assertFalse(
                rentalService.isTypeSpecificRuleValid(
                        null,
                        25,
                        true,
                        80
                )
        );

        assertFalse(
                rentalService.isTypeSpecificRuleValid(
                        "   ",
                        25,
                        true,
                        80
                )
        );
    }

    @Test
    public void missingVehicleFailsTypeSpecificRule() {
        when(
                vehicleRepository.findById("99")
        ).thenReturn(null);

        assertFalse(
                rentalService.isTypeSpecificRuleValid(
                        "99",
                        25,
                        true,
                        80
                )
        );
    }

    @Test
    public void typeSpecificRuleReturnsVehicleDecision() {
        Vehicle vehicle =
                mock(Vehicle.class);

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                vehicle.isRentalAllowed(
                        25,
                        true,
                        80
                )
        ).thenReturn(true);

        when(
                vehicle.isRentalAllowed(
                        17,
                        false,
                        10
                )
        ).thenReturn(false);

        assertTrue(
                rentalService.isTypeSpecificRuleValid(
                        "1",
                        25,
                        true,
                        80
                )
        );

        assertFalse(
                rentalService.isTypeSpecificRuleValid(
                        "1",
                        17,
                        false,
                        10
                )
        );
    }
}