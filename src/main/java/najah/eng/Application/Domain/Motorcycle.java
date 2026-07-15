package najah.eng.Application.Domain;

public class Motorcycle extends Vehicle {

    private static final int MINIMUM_AGE = 18;

    public Motorcycle(String id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public String getType() {
        return "Motorcycle";
    }

    @Override
    public boolean isRentalAllowed(
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        return customerAge >= MINIMUM_AGE;
    }

    @Override
    public String getRuleDescription() {
        return "Customer must be at least 18 years old.";
    }
}