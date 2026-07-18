package najah.eng.Application.factory;

import najah.eng.Application.Domain.Car;
import najah.eng.Application.Domain.ElectricVehicle;
import najah.eng.Application.Domain.Motorcycle;
import najah.eng.Application.Domain.Truck;
import najah.eng.Application.Domain.Van;
import najah.eng.Application.Domain.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VehicleFactoryTest {

    private final VehicleFactory factory =
            new VehicleFactory();

    @Test
    public void createsCar() {
        Vehicle vehicle =
                factory.createVehicle(
                        "1",
                        "Toyota",
                        "Car",
                        "Available"
                );

        assertInstanceOf(
                Car.class,
                vehicle
        );
    }

    @Test
    public void createsVan() {
        Vehicle vehicle =
                factory.createVehicle(
                        "2",
                        "Ford",
                        "Van",
                        "Available"
                );

        assertInstanceOf(
                Van.class,
                vehicle
        );
    }

    @Test
    public void createsTruck() {
        Vehicle vehicle =
                factory.createVehicle(
                        "3",
                        "Volvo",
                        "Truck",
                        "Available"
                );

        assertInstanceOf(
                Truck.class,
                vehicle
        );
    }

    @Test
    public void createsMotorcycle() {
        Vehicle vehicle =
                factory.createVehicle(
                        "4",
                        "Yamaha",
                        "Motorcycle",
                        "Available"
                );

        assertInstanceOf(
                Motorcycle.class,
                vehicle
        );
    }

    @Test
    public void createsElectricVehicle() {
        Vehicle vehicle =
                factory.createVehicle(
                        "5",
                        "Tesla",
                        "Electric Vehicle",
                        "Available"
                );

        assertInstanceOf(
                ElectricVehicle.class,
                vehicle
        );
    }

    @Test
    public void unknownTypeReturnsNull() {
        assertNull(
                factory.createVehicle(
                        "6",
                        "Unknown",
                        "Boat",
                        "Available"
                )
        );
    }

    @Test
    public void nullTypeReturnsNull() {
        assertNull(
                factory.createVehicle(
                        "6",
                        "Unknown",
                        null,
                        "Available"
                )
        );
    }

    @Test
    public void nullIdReturnsNull() {
        assertNull(
                factory.createVehicle(
                        null,
                        "Toyota",
                        "Car",
                        "Available"
                )
        );
    }

    @Test
    public void nullNameReturnsNull() {
        assertNull(
                factory.createVehicle(
                        "1",
                        null,
                        "Car",
                        "Available"
                )
        );
    }

    @Test
    public void nullStatusReturnsNull() {
        assertNull(
                factory.createVehicle(
                        "1",
                        "Toyota",
                        "Car",
                        null
                )
        );
    }

    @Test
    public void typeMatchingIgnoresCase() {
        Vehicle vehicle =
                factory.createVehicle(
                        "1",
                        "Volvo",
                        "truck",
                        "Available"
                );

        assertInstanceOf(
                Truck.class,
                vehicle
        );
    }

}