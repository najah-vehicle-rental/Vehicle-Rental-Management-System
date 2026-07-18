package najah.eng.Application.Domain;

public class RentalRequirements {

    private final int customerAge;
    private final boolean specialLicense;
    private final int batteryLevel;

    public RentalRequirements(
            int customerAge,
            boolean specialLicense,
            int batteryLevel) {

        this.customerAge = customerAge;
        this.specialLicense = specialLicense;
        this.batteryLevel = batteryLevel;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public boolean hasSpecialLicense() {
        return specialLicense;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }
}