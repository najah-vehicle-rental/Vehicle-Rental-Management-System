package najah.eng.Application.Domain;

import najah.eng.Application.strategy.MotorcycleAgeStrategy;

/**
 * Represents a motorcycle available in the vehicle rental system.
 * Motorcycle rental requests are validated using the minimum-age rule.
 */
public class Motorcycle extends Vehicle {

    /**
     * Creates a new motorcycle.
     *
     * @param id the unique vehicle identifier
     * @param name the vehicle display name or model
     * @param status the current vehicle status
     */
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

    /**
     * Returns the vehicle type.
     *
     * @return the text {@code "Motorcycle"}
     */
    @Override
    public String getType() {
        return "Motorcycle";
    }
}