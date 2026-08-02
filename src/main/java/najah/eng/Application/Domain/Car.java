package najah.eng.Application.Domain;

import najah.eng.Application.strategy.DefaultRentalRuleStrategy;

/**
 * Represents a car available in the vehicle rental system.
 * Cars use the default rental validation rules.
 */
public class Car extends Vehicle {

    /**
     * Creates a new car.
     *
     * @param id the unique vehicle identifier
     * @param name the vehicle display name or model
     * @param status the current vehicle status
     */
    public Car(String id, String name, String status) {
        super(
                id,
                name,
                status,
                new DefaultRentalRuleStrategy()
        );
    }

    /**
     * Returns the vehicle type.
     *
     * @return the text {@code "Car"}
     */
    @Override
    public String getType() {
        return "Car";
    }
}