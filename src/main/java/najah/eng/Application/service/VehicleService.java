package najah.eng.Application.service;

import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.VehicleRepository;

import java.util.ArrayList;

/**
 * Provides application-level operations for accessing vehicle data.
 */
public class VehicleService {

    /**
     * Repository used to access vehicle records.
     */
    private final VehicleRepository vehicleRepository;

    /**
     * Creates a vehicle service using the default vehicle repository.
     */
    public VehicleService() {
        this(new VehicleRepository());
    }

    /**
     * Creates a vehicle service using a supplied repository.
     *
     * @param vehicleRepository repository used to access vehicle records
     */
    public VehicleService(
            VehicleRepository vehicleRepository) {

        this.vehicleRepository =
                vehicleRepository;
    }

    /**
     * Returns all vehicles whose status is Available.
     *
     * @return a list containing the available vehicles
     */
    public ArrayList<Vehicle> getAvailableVehicles() {
        return vehicleRepository
                .findAvailableVehicles();
    }

    /**
     * Searches for a vehicle using its unique identifier.
     *
     * @param vehicleId the identifier of the vehicle
     * @return the matching vehicle, or {@code null} when it is not found
     */
    public Vehicle getVehicleById(
            String vehicleId) {

        return vehicleRepository
                .findById(vehicleId);
    }
}