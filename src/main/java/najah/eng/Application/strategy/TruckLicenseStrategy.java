package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

public class TruckLicenseStrategy
        implements RentalRuleStrategy {

    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        return requirements.hasSpecialLicense();
    }

    @Override
    public String getRuleDescription() {
        return "A valid special truck license is required.";
    }
}