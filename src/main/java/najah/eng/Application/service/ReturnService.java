package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;

public class ReturnService {

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public ReturnService() {
        vehicleRepository = new VehicleRepository();
        rentalRepository = new RentalRepository();
    }

    public Rental getActiveRental(String vehicleId) {
        return rentalRepository.findActiveRentalByVehicleId(vehicleId);
    }

    public boolean returnVehicle(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle == null) {
            return false;
        }

        if (!vehicle.getStatus().equalsIgnoreCase("Rented")) {
            return false;
        }

        Rental rental =
                rentalRepository.findActiveRentalByVehicleId(vehicleId);

        if (rental == null) {
            return false;
        }

        boolean vehicleUpdated =
                vehicleRepository.updateStatus(
                        vehicleId,
                        "Available"
                );

        if (!vehicleUpdated) {
            return false;
        }

        boolean rentalClosed =
                rentalRepository.closeActiveRental(vehicleId);

        if (!rentalClosed) {
            vehicleRepository.updateStatus(
                    vehicleId,
                    "Rented"
            );

            return false;
        }

        return true;
    }
}