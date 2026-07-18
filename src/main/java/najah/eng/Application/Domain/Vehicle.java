package najah.eng.Application.Domain;

import najah.eng.Application.strategy.RentalRuleStrategy;

public abstract class Vehicle {

    private final String id;
    private final String name;
    private final String status;
    private final RentalRuleStrategy rentalRuleStrategy;

    protected Vehicle(
            String id,
            String name,
            String status,
            RentalRuleStrategy rentalRuleStrategy) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.rentalRuleStrategy = rentalRuleStrategy;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public abstract String getType();

    public boolean isRentalAllowed(
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        RentalRequirements requirements =
                new RentalRequirements(
                        customerAge,
                        hasSpecialLicense,
                        batteryLevel
                );

        return rentalRuleStrategy.isRentalAllowed(
                requirements
        );
    }

    public String getRuleDescription() {
        return rentalRuleStrategy.getRuleDescription();
    }
}