package najah.eng.Application.Domain;

import najah.eng.Application.strategy.MotorcycleAgeStrategy;

public class Motorcycle extends Vehicle {

    public Motorcycle(
            String id,
            String name,
            String status) {

        super(
                id,
                name,
                status,
                new MotorcycleAgeStrategy()
        );
    }

    @Override
    public String getType() {
        return "Motorcycle";
    }
}