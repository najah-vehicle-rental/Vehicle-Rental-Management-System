package najah.eng.Application.Domain;

/**
 * Stores customer and vehicle information required by
 * type-specific rental validation strategies.
 */
public class RentalRequirements {

    /**
     * The age of the customer requesting the rental.
     */
    private final int customerAge;

    /**
     * Indicates whether the customer owns the required special license.
     */
    private final boolean specialLicense;

    /**
     * The reported battery level of an electric vehicle.
     */
    private final int batteryLevel;

    /**
     * Creates rental requirements for a rental request.
     *
     * @param customerAge the age of the customer
     * @param specialLicense {@code true} if the customer has a special license
     * @param batteryLevel the electric-vehicle battery level
     */
    public RentalRequirements(
            int customerAge,
            boolean specialLicense,
            int batteryLevel) {

        this.customerAge = customerAge;
        this.specialLicense = specialLicense;
        this.batteryLevel = batteryLevel;
    }

    /**
     * Returns the customer age.
     *
     * @return the customer age
     */
    public int getCustomerAge() {
        return customerAge;
    }

    /**
     * Indicates whether the customer has a special license.
     *
     * @return {@code true} if the customer has a special license;
     *         otherwise {@code false}
     */
    public boolean hasSpecialLicense() {
        return specialLicense;
    }

    /**
     * Returns the electric-vehicle battery level.
     *
     * @return the battery level
     */
    public int getBatteryLevel() {
        return batteryLevel;
    }
}