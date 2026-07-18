package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

public class DefaultRentalRuleStrategy
        implements RentalRuleStrategy {

    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        return true;
    }

    @Override
    public String getRuleDescription() {
        return "No additional rental requirements.";
    }
}