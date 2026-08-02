package najah.eng.Application.Domain;

import najah.eng.Application.strategy.DefaultRentalRuleStrategy;

/**
 * Represents a van available in the vehicle rental system.
 * Vans use the default rental validation rules.
 */
public class Van extends Vehicle {

    /**
     * Creates a new van.
     *
     * @param id the unique vehicle identifier
     * @param name the vehicle display name or model
     * @param status the current vehicle status
     */
    public Van(String id, String name, String status) {
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
     * @return the text {@code "Van"}
     */
    @Override
    public String getType() {
        return "Van";
    }
}