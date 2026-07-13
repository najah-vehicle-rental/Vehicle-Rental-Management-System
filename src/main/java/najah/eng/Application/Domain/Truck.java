package najah.eng.Application.Domain;

public class Truck extends Vehicle {

    public Truck(String id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public String getType() {
        return "Truck";
    }
}