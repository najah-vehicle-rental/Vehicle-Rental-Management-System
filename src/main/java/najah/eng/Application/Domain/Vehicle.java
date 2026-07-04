package najah.eng.Application.Domain;

public class Vehicle {

    private String id;
    private String name;
    private String status;

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
}