package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

/**
 * Validates the battery-level requirement for an electric vehicle.
 *
 * <p>An electric vehicle can be rented only when its battery level
 * is between the configured minimum and maximum values.</p>
 */
public class ElectricBatteryStrategy
        implements RentalRuleStrategy {

    /**
     * Creates an electric-battery validation strategy.
     */
    public ElectricBatteryStrategy() {
        // Default constructor.
    }

    /**
     * The minimum battery level required to rent an electric vehicle.
     */
    private static final int MINIMUM_BATTERY_LEVEL = 30;

    /**
     * The maximum valid battery level of an electric vehicle.
     */
    private static final int MAXIMUM_BATTERY_LEVEL = 100;

    /**
     * Checks whether the electric vehicle battery level is valid.
     *
     * @param requirements the customer and vehicle requirements
     *                     containing the battery level
     * @return {@code true} if the battery level is between
     *         30 and 100 inclusive; otherwise {@code false}
     */
    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        int batteryLevel =
                requirements.getBatteryLevel();

        return batteryLevel >= MINIMUM_BATTERY_LEVEL
                && batteryLevel <= MAXIMUM_BATTERY_LEVEL;
    }

    /**
     * Returns a description of the electric-vehicle battery rule.
     *
     * @return the battery rule description
     */
    @Override
    public String getRuleDescription() {
        return "Battery level must be between 30% and 100%.";
    }
}