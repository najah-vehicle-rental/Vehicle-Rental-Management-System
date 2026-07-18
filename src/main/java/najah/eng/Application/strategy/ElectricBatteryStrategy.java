package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;

public class ElectricBatteryStrategy
        implements RentalRuleStrategy {

    private static final int MINIMUM_BATTERY_LEVEL = 30;
    private static final int MAXIMUM_BATTERY_LEVEL = 100;

    @Override
    public boolean isRentalAllowed(
            RentalRequirements requirements) {

        int batteryLevel =
                requirements.getBatteryLevel();

        return batteryLevel >= MINIMUM_BATTERY_LEVEL
                && batteryLevel <= MAXIMUM_BATTERY_LEVEL;
    }

    @Override
    public String getRuleDescription() {
        return "Battery level must be between 30% and 100%.";
    }
}