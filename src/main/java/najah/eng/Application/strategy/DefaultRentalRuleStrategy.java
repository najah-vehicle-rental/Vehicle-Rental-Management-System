package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

/**
 * Implements the default rental rule.
 *
 * <p>This strategy allows a rental without requiring additional
 * age, license, or battery conditions.</p>
 */
public class DefaultRentalRuleStrategy
        implements RentalRuleStrategy {

    /**
     * Creates a default rental-rule strategy.
     */
    public DefaultRentalRuleStrategy() {
        // Default constructor.
    }

    /**
     * Accepts the rental because no additional rule is required.
     *
     * @param requirements the customer and vehicle requirements
     *                     used during validation
     * @return {@code true} because the default strategy
     *         has no extra restrictions
     */
    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        return true;
    }

    /**
     * Returns a description of the default rental rule.
     *
     * @return the default rental rule description
     */
    @Override
    public String getRuleDescription() {
        return "No additional rental requirements.";
    }
}