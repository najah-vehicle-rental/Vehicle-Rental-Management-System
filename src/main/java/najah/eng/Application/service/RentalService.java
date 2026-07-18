package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;
import najah.eng.Application.observer.RentalEvent;
import najah.eng.Application.observer.RentalEventPublisher;
import najah.eng.Application.observer.RentalEventType;

import java.time.LocalDate;

/**
 * Provides the business logic required to rent vehicles.
 *
 * <p>The service validates the vehicle status, rental duration,
 * customer information, active rental records, and vehicle-specific
 * rental rules.</p>
 *
 * <p>It prevents double booking by checking both the vehicle status
 * and the active rental records.</p>
 *
 * <p>After a successful rental, the service publishes a
 * {@link RentalEventType#RENTED} event using the Observer Pattern.</p>
 */
public class RentalService {

    private static final int MIN_RENTAL_DAYS = 1;
    private static final int MAX_RENTAL_DAYS = 30;

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final RentalEventPublisher eventPublisher;

    /**
     * Creates a rental service using the default repositories
     * and a default event publisher.
     */
    public RentalService() {
        this(
                new VehicleRepository(),
                new RentalRepository(),
                new RentalEventPublisher()
        );
    }

    /**
     * Creates a rental service using the supplied repositories.
     *
     * @param vehicleRepository repository used to access vehicle data
     * @param rentalRepository repository used to access rental data
     */
    public RentalService(
            VehicleRepository vehicleRepository,
            RentalRepository rentalRepository) {

        this(
                vehicleRepository,
                rentalRepository,
                new RentalEventPublisher()
        );
    }

    /**
     * Creates a rental service using dependency injection.
     *
     * @param vehicleRepository repository used to access vehicle data
     * @param rentalRepository repository used to access rental data
     * @param eventPublisher publisher used to notify rental observers
     */
    public RentalService(
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
     * Rents a vehicle that has no additional type-specific requirements.
     *
     * <p>This method delegates to the complete rental method using
     * default values for age, special license, and battery level.</p>
     *
     * @param vehicleId the ID of the vehicle
     * @param customerName the customer name
     * @param customerEmail the customer email
     * @param rentalDays the number of rental days
     * @return true when the rental succeeds; otherwise false
     */
    public boolean rentVehicle(
            String vehicleId,
            String customerName,
            String customerEmail,
            int rentalDays) {

        return rentVehicle(
                vehicleId,
                customerName,
                customerEmail,
                rentalDays,
                0,
                false,
                0
        );
    }

    /**
     * Rents a vehicle after validating all general and type-specific rules.
     *
     * <p>The operation performs the following validations:</p>
     *
     * <ul>
     *     <li>The vehicle exists.</li>
     *     <li>The vehicle has no active rental.</li>
     *     <li>The vehicle status is Available.</li>
     *     <li>The rental duration is between 1 and 30 days.</li>
     *     <li>The customer name and email are provided.</li>
     *     <li>The vehicle-specific rental strategy accepts the request.</li>
     * </ul>
     *
     * <p>If saving the rental fails after updating the vehicle status,
     * the vehicle status is restored to Available.</p>
     *
     * @param vehicleId the ID of the vehicle
     * @param customerName the customer name
     * @param customerEmail the customer email
     * @param rentalDays the requested rental duration
     * @param customerAge the age of the customer
     * @param hasSpecialLicense whether the customer has a special license
     * @param batteryLevel the electric vehicle battery level
     * @return true when the rental is completed successfully;
     * otherwise false
     */
    public boolean rentVehicle(
            String vehicleId,
            String customerName,
            String customerEmail,
            int rentalDays,
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

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

        Rental activeRental =
                rentalRepository
                        .findActiveRentalByVehicleId(id);

        if (activeRental != null) {
            return false;
        }

        if (!vehicle.getStatus()
                .equalsIgnoreCase("Available")) {

            return false;
        }

        if (!isRentalDurationValid(rentalDays)) {
            return false;
        }

        if (customerName == null ||
                customerName.isBlank()) {

            return false;
        }

        if (customerEmail == null ||
                customerEmail.isBlank()) {

            return false;
        }

        if (!vehicle.isRentalAllowed(
                customerAge,
                hasSpecialLicense,
                batteryLevel
        )) {
            return false;
        }

        LocalDate expiryDate =
                LocalDate.now().plusDays(rentalDays);

        Rental rental = new Rental(
                id,
                customerName.trim(),
                customerEmail.trim(),
                rentalDays,
                expiryDate,
                "Active"
        );

        boolean statusUpdated =
                vehicleRepository.updateStatus(
                        id,
                        "Rented"
                );

        if (!statusUpdated) {
            return false;
        }

        boolean saved =
                rentalRepository.save(rental);

        if (!saved) {
            vehicleRepository.updateStatus(
                    id,
                    "Available"
            );

            return false;
        }

        eventPublisher.notifyObservers(
                new RentalEvent(
                        RentalEventType.RENTED,
                        rental
                )
        );

        return true;
    }

    /**
     * Checks whether a vehicle is available and has no active rental.
     *
     * @param vehicleId the ID of the vehicle
     * @return true when the vehicle is available and not double-booked;
     * otherwise false
     */
    public boolean isVehicleAvailable(
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

        Rental activeRental =
                rentalRepository
                        .findActiveRentalByVehicleId(id);

        if (activeRental != null) {
            return false;
        }

        return vehicle.getStatus()
                .equalsIgnoreCase("Available");
    }

    /**
     * Checks whether the requested rental duration is valid.
     *
     * @param rentalDays the requested number of rental days
     * @return true when the duration is between 1 and 30 days;
     * otherwise false
     */
    public boolean isRentalDurationValid(
            int rentalDays) {

        return rentalDays >= MIN_RENTAL_DAYS
                && rentalDays <= MAX_RENTAL_DAYS;
    }

    /**
     * Checks whether the customer satisfies the rental rule
     * of the selected vehicle type.
     *
     * @param vehicleId the ID of the vehicle
     * @param customerAge the age of the customer
     * @param hasSpecialLicense whether the customer has a special license
     * @param batteryLevel the electric vehicle battery level
     * @return true when the vehicle-specific rental rule is satisfied;
     * otherwise false
     */
    public boolean isTypeSpecificRuleValid(
            String vehicleId,
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        if (vehicleId == null ||
                vehicleId.isBlank()) {

            return false;
        }

        Vehicle vehicle =
                vehicleRepository.findById(
                        vehicleId.trim()
                );

        if (vehicle == null) {
            return false;
        }

        return vehicle.isRentalAllowed(
                customerAge,
                hasSpecialLicense,
                batteryLevel
        );
    }
}