package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

/**
 * Validates the minimum-age requirement for renting a motorcycle.
 *
 * <p>A customer must be at least 18 years old to rent
 * a motorcycle.</p>
 */
public class MotorcycleAgeStrategy
        implements RentalRuleStrategy {

    /**
     * Creates a motorcycle-age validation strategy.
     */
    public MotorcycleAgeStrategy() {
        // Default constructor.
    }

    /**
     * The minimum customer age required to rent a motorcycle.
     */
    private static final int MINIMUM_AGE = 18;

    /**
     * Checks whether the customer meets the minimum age requirement.
     *
     * @param requirements the customer and vehicle requirements
     *                     containing the customer age
     * @return {@code true} if the customer is at least 18 years old;
     *         otherwise {@code false}
     */
    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        return requirements.getCustomerAge()
                >= MINIMUM_AGE;
    }

    /**
     * Returns a description of the motorcycle age rule.
     *
     * @return the motorcycle rental age rule description
     */
    @Override
    public String getRuleDescription() {
        return "Customer must be at least 18 years old.";
    }
}