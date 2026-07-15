package najah.eng.Application.Domain;

public class Van extends Vehicle {

    public Van(String id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public String getType() {
        return "Van";
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