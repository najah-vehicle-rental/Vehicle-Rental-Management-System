package najah.eng.Application.Domain;

public class Truck extends Vehicle {

    public Truck(String id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public String getType() {
        return "Truck";
    }

    @Override
    public boolean isRentalAllowed(
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        return hasSpecialLicense;
    }

    @Override
    public String getRuleDescription() {
        return "A valid special truck license is required.";
    }
}