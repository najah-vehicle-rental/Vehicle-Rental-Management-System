package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;
import najah.eng.Application.observer.RentalEvent;
import najah.eng.Application.observer.RentalEventPublisher;
import najah.eng.Application.observer.RentalEventType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnService {

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final RentalEventPublisher eventPublisher;

    public ReturnService() {
        this(
                new VehicleRepository(),
                new RentalRepository(),
                new RentalEventPublisher()
        );
    }

    public ReturnService(
            VehicleRepository vehicleRepository,
            RentalRepository rentalRepository) {

        this(
                vehicleRepository,
                rentalRepository,
                new RentalEventPublisher()
        );
    }

    public ReturnService(
            VehicleRepository vehicleRepository,
            RentalRepository rentalRepository,
            RentalEventPublisher eventPublisher) {

        this.vehicleRepository =
                vehicleRepository;

        this.rentalRepository =
                rentalRepository;

        this.eventPublisher =
                eventPublisher;
    }

    public Rental getActiveRental(
            String vehicleId) {

        if (vehicleId == null ||
                vehicleId.isBlank()) {

            return null;
        }

        return rentalRepository
                .findActiveRentalByVehicleId(
                        vehicleId.trim()
                );
    }

    public boolean returnVehicle(
            String vehicleId) {

        if (vehicleId == null ||
                vehicleId.isBlank()) {

            return false;
        }

        String id = vehicleId.trim();

        Vehicle vehicle =
                vehicleRepository.findById(id);

        if (vehicle == null) {
            return false;
        }

        if (!vehicle.getStatus()
                .equalsIgnoreCase("Rented")) {

            return false;
        }

        Rental rental =
                rentalRepository
                        .findActiveRentalByVehicleId(id);

        if (rental == null) {
            return false;
        }

        boolean vehicleUpdated =
                vehicleRepository.updateStatus(
                        id,
                        "Available"
                );

        if (!vehicleUpdated) {
            return false;
        }

        boolean rentalClosed =
                rentalRepository
                        .closeActiveRental(id);

        if (!rentalClosed) {
            vehicleRepository.updateStatus(
                    id,
                    "Rented"
            );

            return false;
        }

        LocalDate returnDate =
                LocalDate.now();

        long lateDays = 0;

        if (returnDate.isAfter(
                rental.getExpiryDate())) {

            lateDays =
                    ChronoUnit.DAYS.between(
                            rental.getExpiryDate(),
                            returnDate
                    );
        }

        RentalEventType eventType;

        if (lateDays > 0) {
            eventType =
                    RentalEventType.LATE_RETURNED;
        } else {
            eventType =
                    RentalEventType.RETURNED;
        }

        eventPublisher.notifyObservers(
                new RentalEvent(
                        eventType,
                        rental,
                        lateDays
                )
        );

        return true;
    }
}