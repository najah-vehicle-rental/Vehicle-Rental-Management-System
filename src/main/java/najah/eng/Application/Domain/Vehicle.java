package najah.eng.Application.Domain;

public abstract class Vehicle {

    private final String id;
    private final String name;
    private final String status;

    public Vehicle(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

    public abstract boolean isRentalAllowed(
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel
    );

    public abstract String getRuleDescription();
}