package najah.eng.Application.Domain;

public class Car extends Vehicle {

    public Car(String id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public String getType() {
        return "Car";
    }

    @Override
    public boolean isRentalAllowed(
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        return true;
    }

    @Override
    public String getRuleDescription() {
        return "No additional rental requirements.";
    }
}