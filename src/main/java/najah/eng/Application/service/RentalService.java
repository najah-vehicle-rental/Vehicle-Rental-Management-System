package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;

public class RentalService {

    private VehicleRepository vehicleRepository;
    private RentalRepository rentalRepository;

    public RentalService() {
        vehicleRepository = new VehicleRepository();
        rentalRepository = new RentalRepository();
    }

    public boolean rentVehicle(
            String vehicleId,
            String customerName,
            int rentalDays) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle == null) {
            return false;
        }

        if (!vehicle.getStatus().equalsIgnoreCase("Available")) {
            return false;
        }

        Rental rental = new Rental(
                vehicleId,
                customerName,
                rentalDays,
                "Active"
        );

        boolean saved = rentalRepository.save(rental);

        if (!saved) {
            return false;
        }

        return vehicleRepository.updateStatus(
                vehicleId,
                "Rented"
        );
    }

    public boolean isVehicleAvailable(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle == null) {
            return false;
        }

        return vehicle.getStatus().equalsIgnoreCase("Available");
    }
}