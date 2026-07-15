package najah.eng.Application.Domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleTypeRulesTest {

    @Test
    public void truckRequiresSpecialLicense() {
        Truck truck = new Truck(
                "1",
                "Volvo Truck",
                "Available"
        );

        assertFalse(
                truck.isRentalAllowed(
                        30,
                        false,
                        0
                )
        );

        assertTrue(
                truck.isRentalAllowed(
                        30,
                        true,
                        0
                )
        );
    }

    @Test
    public void electricVehicleRequiresBatteryCheck() {
        ElectricVehicle electricVehicle =
                new ElectricVehicle(
                        "2",
                        "Tesla Model 3",
                        "Available"
                );

        assertFalse(
                electricVehicle.isRentalAllowed(
                        30,
                        false,
                        20
                )
        );

        assertTrue(
                electricVehicle.isRentalAllowed(
                        30,
                        false,
                        80
                )
        );

        assertFalse(
                electricVehicle.isRentalAllowed(
                        30,
                        false,
                        101
                )
        );
    }

    @Test
    public void motorcycleRequiresMinimumAge() {
        Motorcycle motorcycle =
                new Motorcycle(
                        "3",
                        "Yamaha MT-07",
                        "Available"
                );

        assertFalse(
                motorcycle.isRentalAllowed(
                        17,
                        false,
                        0
                )
        );

        assertTrue(
                motorcycle.isRentalAllowed(
                        18,
                        false,
                        0
                )
        );
    }

    @Test
    public void carHasNoAdditionalRules() {
        Car car = new Car(
                "4",
                "Toyota Corolla",
                "Available"
        );

        assertTrue(
                car.isRentalAllowed(
                        0,
                        false,
                        0
                )
        );
    }

    @Test
    public void vanHasNoAdditionalRules() {
        Van van = new Van(
                "5",
                "Ford Transit",
                "Available"
        );

        assertTrue(
                van.isRentalAllowed(
                        0,
                        false,
                        0
                )
        );
    }
}