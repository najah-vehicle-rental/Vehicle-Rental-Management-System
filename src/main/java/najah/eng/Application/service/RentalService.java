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

        return rentVehicle(
                vehicleId,
                customerName,
                customerEmail,
                rentalDays,
                0,
                false,
                0
        );
    }

    public boolean rentVehicle(
            String vehicleId,
            String customerName,
            String customerEmail,
            int rentalDays,
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        if (vehicleId == null || vehicleId.isBlank()) {
            return false;
        }

        String id = vehicleId.trim();

        Vehicle vehicle =
                vehicleRepository.findById(id);

        if (vehicle == null) {
            return false;
        }

        if (!vehicle.getStatus().equalsIgnoreCase("Available")) {
            return false;
        }

        if (!isRentalDurationValid(rentalDays)) {
            return false;
        }

        if (customerName == null || customerName.isBlank()) {
            return false;
        }

        if (customerEmail == null || customerEmail.isBlank()) {
            return false;
        }

        if (!vehicle.isRentalAllowed(
                customerAge,
                hasSpecialLicense,
                batteryLevel
        )) {
            return false;
        }

        LocalDate expiryDate =
                LocalDate.now().plusDays(rentalDays);

        Rental rental = new Rental(
                id,
                customerName.trim(),
                customerEmail.trim(),
                rentalDays,
                expiryDate,
                "Active"
        );

        boolean statusUpdated =
                vehicleRepository.updateStatus(
                        id,
                        "Rented"
                );

        if (!statusUpdated) {
            return false;
        }

        boolean saved =
                rentalRepository.save(rental);

        if (!saved) {
            vehicleRepository.updateStatus(
                    id,
                    "Available"
            );

            return false;
        }

        return true;
    }

    public boolean isVehicleAvailable(String vehicleId) {
        if (vehicleId == null || vehicleId.isBlank()) {
            return false;
        }

        Vehicle vehicle =
                vehicleRepository.findById(vehicleId.trim());

        if (vehicle == null) {
            return false;
        }

        return vehicle.getStatus()
                .equalsIgnoreCase("Available");
    }

    public boolean isRentalDurationValid(int rentalDays) {
        return rentalDays >= MIN_RENTAL_DAYS
                && rentalDays <= MAX_RENTAL_DAYS;
    }

    public boolean isTypeSpecificRuleValid(
            String vehicleId,
            int customerAge,
            boolean hasSpecialLicense,
            int batteryLevel) {

        if (vehicleId == null || vehicleId.isBlank()) {
            return false;
        }

        Vehicle vehicle =
                vehicleRepository.findById(vehicleId.trim());

        if (vehicle == null) {
            return false;
        }

        return vehicle.isRentalAllowed(
                customerAge,
                hasSpecialLicense,
                batteryLevel
        );
    }
}