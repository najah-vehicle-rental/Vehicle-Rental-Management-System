package najah.eng.Application.service;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;
import najah.eng.Application.observer.RentalEvent;
import najah.eng.Application.observer.RentalEventPublisher;
import najah.eng.Application.observer.RentalEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnServiceTest {

    private VehicleRepository vehicleRepository;
    private RentalRepository rentalRepository;
    private RentalEventPublisher eventPublisher;
    private ReturnService returnService;

    @BeforeEach
    public void setUp() {
        vehicleRepository =
                mock(VehicleRepository.class);

        rentalRepository =
                mock(RentalRepository.class);

        eventPublisher =
                mock(RentalEventPublisher.class);

        returnService =
                new ReturnService(
                        vehicleRepository,
                        rentalRepository,
                        eventPublisher
                );
    }

    @Test
    public void successfulReturnClosesRental() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Rented"
        );

        Rental rental = createRental(
                LocalDate.now().plusDays(2)
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                rentalRepository
                        .findActiveRentalByVehicleId("1")
        ).thenReturn(rental);

        when(
                vehicleRepository.updateStatus(
                        "1",
                        "Available"
                )
        ).thenReturn(true);

        when(
                rentalRepository.closeActiveRental("1")
        ).thenReturn(true);

        assertTrue(
                returnService.returnVehicle("1")
        );

        verify(rentalRepository)
                .closeActiveRental("1");

        verify(eventPublisher)
                .notifyObservers(
                        any(RentalEvent.class)
                );
    }

    @Test
    public void vehicleNotRentedIsRejected() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Available"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        assertFalse(
                returnService.returnVehicle("1")
        );
    }

    @Test
    public void noActiveRentalIsRejected() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Rented"
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                rentalRepository
                        .findActiveRentalByVehicleId("1")
        ).thenReturn(null);

        assertFalse(
                returnService.returnVehicle("1")
        );

        verify(
                vehicleRepository,
                never()
        ).updateStatus(
                "1",
                "Available"
        );
    }

    @Test
    public void closeFailureRestoresRentedStatus() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Rented"
        );

        Rental rental = createRental(
                LocalDate.now().plusDays(2)
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                rentalRepository
                        .findActiveRentalByVehicleId("1")
        ).thenReturn(rental);

        when(
                vehicleRepository.updateStatus(
                        "1",
                        "Available"
                )
        ).thenReturn(true);

        when(
                rentalRepository.closeActiveRental("1")
        ).thenReturn(false);

        assertFalse(
                returnService.returnVehicle("1")
        );

        verify(vehicleRepository)
                .updateStatus(
                        "1",
                        "Rented"
                );

        verify(
                eventPublisher,
                never()
        ).notifyObservers(
                any(RentalEvent.class)
        );
    }

    @Test
    public void lateReturnCreatesLateEvent() {
        Vehicle vehicle = new Car(
                "1",
                "Toyota Corolla",
                "Rented"
        );

        Rental rental = createRental(
                LocalDate.now().minusDays(3)
        );

        when(
                vehicleRepository.findById("1")
        ).thenReturn(vehicle);

        when(
                rentalRepository
                        .findActiveRentalByVehicleId("1")
        ).thenReturn(rental);

        when(
                vehicleRepository.updateStatus(
                        "1",
                        "Available"
                )
        ).thenReturn(true);

        when(
                rentalRepository.closeActiveRental("1")
        ).thenReturn(true);

        assertTrue(
                returnService.returnVehicle("1")
        );

        ArgumentCaptor<RentalEvent> captor =
                ArgumentCaptor.forClass(
                        RentalEvent.class
                );

        verify(eventPublisher)
                .notifyObservers(captor.capture());

        RentalEvent event =
                captor.getValue();

        assertEquals(
                RentalEventType.LATE_RETURNED,
                event.getType()
        );

        assertEquals(
                3,
                event.getLateDays()
        );
    }

    private Rental createRental(
            LocalDate expiryDate) {

        return new Rental(
                "1",
                "Fadi",
                "fadi@example.com",
                5,
                expiryDate,
                "Active"
        );
    }
}