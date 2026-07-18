package najah.eng.Application.Domain;

import najah.eng.Application.strategy.TruckLicenseStrategy;

public class Truck extends Vehicle {

    public Truck(String id, String name, String status) {
        super(
                id,
                name,
                status,
                new TruckLicenseStrategy()
        );
    }

    @Override
    public String getType() {
        return "Truck";
    }
}