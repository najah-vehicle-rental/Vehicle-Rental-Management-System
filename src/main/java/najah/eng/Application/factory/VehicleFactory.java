package najah.eng.Application.factory;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.ElectricVehicle;
import najah.eng.Application.Domain.Motorcycle;
import najah.eng.Application.Domain.Truck;
import najah.eng.Application.Domain.Van;
import najah.eng.Application.Domain.Vehicle;

/**
 * Creates concrete vehicle objects according to a textual vehicle type.
 */
public class VehicleFactory {

    /**
     * Creates a vehicle factory.
     */
    public VehicleFactory() {
        // Default constructor.
    }

    /**
     * Creates a concrete vehicle according to the supplied type.
     *
     * @param id the unique vehicle identifier
     * @param name the vehicle display name or model
     * @param type the vehicle type
     * @param status the current vehicle status
     * @return the created vehicle, or {@code null} when the input
     *         or vehicle type is unsupported
     */
    public Vehicle createVehicle(
            String id,
            String name,
            String type,
            String status) {

        if (id == null ||
                name == null ||
                type == null ||
                status == null) {

            return null;
        }

        if (type.equalsIgnoreCase("Car")) {
            return new Car(id, name, status);
        }

        if (type.equalsIgnoreCase("Van")) {
            return new Van(id, name, status);
        }

        if (type.equalsIgnoreCase("Truck")) {
            return new Truck(id, name, status);
        }

        if (type.equalsIgnoreCase("Motorcycle")) {
            return new Motorcycle(id, name, status);
        }

        if (type.equalsIgnoreCase("Electric Vehicle")) {
            return new ElectricVehicle(
                    id,
                    name,
                    status
            );
        }

        return null;
    }
}