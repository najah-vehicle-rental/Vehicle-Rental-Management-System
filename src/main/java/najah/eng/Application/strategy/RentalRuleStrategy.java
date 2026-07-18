package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

public interface RentalRuleStrategy {

    boolean isRentalAllowed(
            RentalRequirements requirements
    );

    String getRuleDescription();
}