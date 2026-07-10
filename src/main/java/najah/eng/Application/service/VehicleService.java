package najah.eng.Application.service;

import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.VehicleRepository;

import java.util.ArrayList;

public class VehicleService {

    private VehicleRepository vehicleRepository;

    public VehicleService() {
        vehicleRepository = new VehicleRepository();
    }

    public ArrayList<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findAvailableVehicles();
    }
}