package najah.eng.Application.service;

import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.VehicleRepository;

import java.util.ArrayList;

public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService() {
        this(new VehicleRepository());
    }

    public VehicleService(
            VehicleRepository vehicleRepository) {

        this.vehicleRepository =
                vehicleRepository;
    }

    public ArrayList<Vehicle> getAvailableVehicles() {
        return vehicleRepository
                .findAvailableVehicles();
    }

    public Vehicle getVehicleById(
            String vehicleId) {

        return vehicleRepository
                .findById(vehicleId);
    }
}