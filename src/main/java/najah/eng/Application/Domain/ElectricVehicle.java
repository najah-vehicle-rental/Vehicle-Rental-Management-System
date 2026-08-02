package najah.eng.Application.Domain;

import najah.eng.Application.strategy.ElectricBatteryStrategy;

/**
 * Represents an electric vehicle available in the rental system.
 * Rental requests are validated using the electric-vehicle battery rule.
 */
public class ElectricVehicle extends Vehicle {

    /**
     * Creates a new electric vehicle.
     *
     * @param id the unique vehicle identifier
     * @param name the vehicle display name or model
     * @param status the current vehicle status
     */
    public ElectricVehicle(
            String id,
            String name,
            String status) {

        super(
                id,
                name,
                status,
                new ElectricBatteryStrategy()
        );
    }

    /**
     * Returns the vehicle type.
     *
     * @return the text {@code "Electric Vehicle"}
     */
    @Override
    public String getType() {
        return "Electric Vehicle";
    }
}