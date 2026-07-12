package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Domain.Vehicle;
import najah.eng.Application.Persistence.RentalRepository;
import najah.eng.Application.Persistence.VehicleRepository;

import java.time.LocalDate;

public class RentalService {

    private static final int MIN_RENTAL_DAYS = 1;
    private static final int MAX_RENTAL_DAYS = 30;

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public RentalService() {
        vehicleRepository = new VehicleRepository();
        rentalRepository = new RentalRepository();
    }

    public boolean rentVehicle(
            String vehicleId,
            String customerName,
            String customerEmail,
            int rentalDays) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle == null) {
            return false;
        }

        if (!vehicle.getStatus().equalsIgnoreCase("Available")) {
            return false;
        }

        if (!isRentalDurationValid(rentalDays)) {
            return false;
        }

        if (customerEmail == null || customerEmail.isBlank()) {
            return false;
        }

        LocalDate expiryDate =
                LocalDate.now().plusDays(rentalDays);

        Rental rental = new Rental(
                vehicleId,
                customerName,
                customerEmail,
                rentalDays,
                expiryDate,
                "Active"
        );

        boolean statusUpdated =
                vehicleRepository.updateStatus(vehicleId, "Rented");

        if (!statusUpdated) {
            return false;
        }

        boolean saved = rentalRepository.save(rental);

        if (!saved) {
            vehicleRepository.updateStatus(vehicleId, "Available");
            return false;
        }

        return true;
    }

    public boolean isVehicleAvailable(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle == null) {
            return false;
        }

        return vehicle.getStatus().equalsIgnoreCase("Available");
    }

    public boolean isRentalDurationValid(int rentalDays) {
        return rentalDays >= MIN_RENTAL_DAYS
                && rentalDays <= MAX_RENTAL_DAYS;
    }
}