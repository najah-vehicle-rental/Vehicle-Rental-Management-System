package najah.eng.Application.service;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;
import najah.eng.Application.observer.RentalEvent;
import najah.eng.Application.observer.RentalEventPublisher;
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

    @Test
    public void activeRentalPreventsDoubleBooking() {
        VehicleRepository vehicleRepository =
                mock(VehicleRepository.class);

        RentalRepository rentalRepository =
                mock(RentalRepository.class);

        RentalEventPublisher publisher =
                mock(RentalEventPublisher.class);

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

        RentalService rentalService =
                new RentalService(
                        vehicleRepository,
                        rentalRepository,
                        publisher
                );

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

        verify(
                rentalRepository,
                never()
        ).save(any(Rental.class));
    }

    @Test
    public void successfulRentalNotifiesObservers() {
        VehicleRepository vehicleRepository =
                mock(VehicleRepository.class);

        RentalRepository rentalRepository =
                mock(RentalRepository.class);

        RentalEventPublisher publisher =
                mock(RentalEventPublisher.class);

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

        RentalService rentalService =
                new RentalService(
                        vehicleRepository,
                        rentalRepository,
                        publisher
                );

        boolean result =
                rentalService.rentVehicle(
                        "1",
                        "Fadi",
                        "fadi@example.com",
                        5
                );

        assertTrue(result);

        verify(
                vehicleRepository
        ).updateStatus(
                "1",
                "Rented"
        );

        verify(
                rentalRepository
        ).save(any(Rental.class));

        verify(
                publisher
        ).notifyObservers(
                any(RentalEvent.class)
        );
    }
}