package najah.eng.Application.Domain;

import najah.eng.Application.strategy.TruckLicenseStrategy;

/**
 * Represents a truck available in the vehicle rental system.
 * Truck rental requests are validated using the special-license rule.
 */
public class Truck extends Vehicle {

    /**
     * Creates a new truck.
     *
     * @param id the unique vehicle identifier
     * @param name the vehicle display name or model
     * @param status the current vehicle status
     */
    public Truck(String id, String name, String status) {
        super(
                id,
                name,
                status,
                new TruckLicenseStrategy()
        );
    }

    /**
     * Returns the vehicle type.
     *
     * @return the text {@code "Truck"}
     */
    @Override
    public String getType() {
        return "Truck";
    }
}