package najah.eng.Application.Domain;

public class Motorcycle extends Vehicle {

    public Motorcycle(String id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public String getType() {
        return "Motorcycle";
    }
}