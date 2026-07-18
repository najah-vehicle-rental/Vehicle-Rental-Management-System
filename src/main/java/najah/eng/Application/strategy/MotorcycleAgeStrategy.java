package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

public class MotorcycleAgeStrategy
        implements RentalRuleStrategy {

    private static final int MINIMUM_AGE = 18;

    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        return requirements.getCustomerAge()
                >= MINIMUM_AGE;
    }

    @Override
    public String getRuleDescription() {
        return "Customer must be at least 18 years old.";
    }
}