package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

/**
 * Validates the special-license requirement for renting a truck.
 *
 * <p>A customer must have a valid special truck license
 * before the rental can be accepted.</p>
 */
public class TruckLicenseStrategy
        implements RentalRuleStrategy {

    /**
     * Creates a truck-license validation strategy.
     */
    public TruckLicenseStrategy() {
        // Default constructor.
    }

    /**
     * Checks whether the customer has the required special license.
     *
     * @param requirements the customer and vehicle requirements
     *                     containing the license information
     * @return {@code true} if the customer has a special license;
     *         otherwise {@code false}
     */
    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        return requirements.hasSpecialLicense();
    }

    /**
     * Returns a description of the truck-license rule.
     *
     * @return the truck rental license rule description
     */
    @Override
    public String getRuleDescription() {
        return "A valid special truck license is required.";
    }
}