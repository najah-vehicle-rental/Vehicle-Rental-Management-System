package najah.eng.Application.factory;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.ElectricVehicle;
import najah.eng.Application.Domain.Motorcycle;
import najah.eng.Application.Domain.Truck;
import najah.eng.Application.Domain.Van;
import najah.eng.Application.Domain.Vehicle;

public class VehicleFactory {

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