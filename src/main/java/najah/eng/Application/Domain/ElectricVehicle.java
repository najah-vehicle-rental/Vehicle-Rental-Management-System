package najah.eng.Application.Domain;

import najah.eng.Application.strategy.ElectricBatteryStrategy;

public class ElectricVehicle extends Vehicle {

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

    @Override
    public String getType() {
        return "Electric Vehicle";
    }
}