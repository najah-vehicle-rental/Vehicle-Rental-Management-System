package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

/**
 * Defines the contract for validating vehicle-specific rental rules.
 *
 * <p>Each vehicle type can provide its own implementation of this
 * interface to check special rental requirements.</p>
 */
public interface RentalRuleStrategy {

    /**
     * Checks whether the supplied rental requirements satisfy
     * the vehicle-specific rental rule.
     *
     * @param requirements the customer and vehicle requirements
     *                     used during validation
     * @return {@code true} if the rental is allowed;
     *         otherwise {@code false}
     */
    boolean isRentalAllowed(
            RentalRequirements requirements
    );

    /**
     * Returns a human-readable description of the rental rule.
     *
     * @return the rental rule description
     */
    String getRuleDescription();
}