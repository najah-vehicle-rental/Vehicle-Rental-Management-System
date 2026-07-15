package najah.eng.Application.Domain;

public class ElectricVehicle extends Vehicle {

    private static final int MINIMUM_BATTERY_LEVEL = 30;
    private static final int MAXIMUM_BATTERY_LEVEL = 100;

    public ElectricVehicle(
            String id,
            String name,
            String status) {

        super(id, name, status);
    }

    @Override
    public String getType() {
        return "Electric Vehicle";
    }

    @Override
    public boolean isRentalAllowed(
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        return batteryLevel >= MINIMUM_BATTERY_LEVEL
                && batteryLevel <= MAXIMUM_BATTERY_LEVEL;
    }

    @Override
    public String getRuleDescription() {
        return "Battery level must be between 30% and 100%.";
    }
}