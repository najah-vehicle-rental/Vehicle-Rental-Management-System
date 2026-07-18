package najah.eng.Application.Domain;

import najah.eng.Application.strategy.RentalRuleStrategy;

/**
 * Represents the general structure of a vehicle in the rental system.
 *
 * <p>This abstract class stores the common information shared by all
 * vehicle types, including the vehicle ID, name, status, and rental
 * validation strategy.</p>
 *
 * <p>Concrete vehicle types such as Car, Van, Truck, Motorcycle,
 * and ElectricVehicle extend this class.</p>
 *
 * <p>The class uses the Strategy Pattern through
 * {@link RentalRuleStrategy}. Each vehicle type supplies its own
 * rental validation strategy.</p>
 */
public abstract class Vehicle {

    private final String id;
    private final String name;
    private final String status;
    private final RentalRuleStrategy rentalRuleStrategy;

    /**
     * Creates a vehicle with its basic information and rental rule strategy.
     *
     * @param id the unique identifier of the vehicle
     * @param name the vehicle model or display name
     * @param status the current vehicle status
     * @param rentalRuleStrategy the strategy used to validate rental rules
     */
    protected Vehicle(
            String id,
            String name,
            String status,
            RentalRuleStrategy rentalRuleStrategy) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.rentalRuleStrategy = rentalRuleStrategy;
    }

    /**
     * Returns the unique vehicle identifier.
     *
     * @return the vehicle ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the vehicle name or model.
     *
     * @return the vehicle name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current vehicle status.
     *
     * @return the vehicle status, such as Available, Rented, or Unavailable
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the specific type of the vehicle.
     *
     * @return the vehicle type
     */
    public abstract String getType();

    /**
     * Checks whether the vehicle can be rented according to its
     * type-specific rental strategy.
     *
     * @param customerAge the age of the customer
     * @param hasSpecialLicense whether the customer has a special license
     * @param batteryLevel the battery level used for electric vehicles
     * @return true when the rental requirements are satisfied;
     * otherwise false
     */
    public boolean isRentalAllowed(
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        RentalRequirements requirements =
                new RentalRequirements(
                        customerAge,
                        hasSpecialLicense,
                        batteryLevel
                );

        return rentalRuleStrategy
                .isRentalAllowed(requirements);
    }

    /**
     * Returns a description of the rental rule used by this vehicle.
     *
     * @return the type-specific rental rule description
     */
    public String getRuleDescription() {
        return rentalRuleStrategy
                .getRuleDescription();
    }
}