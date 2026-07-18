package najah.eng.Application.Domain;

import najah.eng.Application.strategy.DefaultRentalRuleStrategy;

public class Car extends Vehicle {

    public Car(String id, String name, String status) {
        super(
                id,
                name,
                status,
                new DefaultRentalRuleStrategy()
        );
    }

    @Override
    public String getType() {
        return "Car";
    }
}