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

/**
 * Provides the business logic required to return rented vehicles.
 *
 * <p>The service verifies that the vehicle is rented and that an
 * active rental exists. It then changes the vehicle status to Available
 * and closes the active rental.</p>
 *
 * <p>If closing the rental fails, the service restores the vehicle
 * status to Rented.</p>
 *
 * <p>After a successful return, the service publishes either a
 * {@link RentalEventType#RETURNED} event or a
 * {@link RentalEventType#LATE_RETURNED} event.</p>
 */
public class ReturnService {

    /**
     * Repository used to find vehicles and update their statuses.
     */
    private final VehicleRepository vehicleRepository;

    /**
     * Repository used to find and close active rental records.
     */
    private final RentalRepository rentalRepository;

    /**
     * Publisher used to notify observers after a successful return.
     */
    private final RentalEventPublisher eventPublisher;

    /**
     * Creates a return service using the default repositories
     * and a default event publisher.
     */
    public ReturnService() {
        this(
                new VehicleRepository(),
                new RentalRepository(),
                new RentalEventPublisher()
        );
    }

    /**
     * Creates a return service using the supplied repositories.
     *
     * @param vehicleRepository repository used to access vehicle data
     * @param rentalRepository repository used to access rental data
     */
    public ReturnService(
            VehicleRepository vehicleRepository,
            RentalRepository rentalRepository) {

        this(
                vehicleRepository,
                rentalRepository,
                new RentalEventPublisher()
        );
    }

    /**
     * Creates a return service using dependency injection.
     *
     * @param vehicleRepository repository used to access vehicle data
     * @param rentalRepository repository used to access rental data
     * @param eventPublisher publisher used to notify return observers
     */
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

    /**
     * Finds the active rental associated with a vehicle.
     *
     * @param vehicleId the ID of the vehicle
     * @return the active rental, or {@code null} when no active
     *         rental exists
     */
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

    /**
     * Returns a rented vehicle and closes its active rental.
     *
     * <p>The method changes the vehicle status to Available and closes
     * the active rental record. If closing the rental fails, the vehicle
     * status is restored to Rented.</p>
     *
     * <p>A normal return publishes a RETURNED event. A return after
     * the expiry date publishes a LATE_RETURNED event containing the
     * number of late days.</p>
     *
     * @param vehicleId the ID of the vehicle being returned
     * @return {@code true} when the return operation succeeds;
     *         otherwise {@code false}
     */
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