package najah.eng.Application.Domain;

import java.time.LocalDate;

public class Rental {

    private final String vehicleId;
    private final String customerName;
    private final String customerEmail;
    private final int rentalDays;
    private final LocalDate expiryDate;
    private final String status;

    public Rental(
            String vehicleId,
            String customerName,
            String customerEmail,
            int rentalDays,
            LocalDate expiryDate,
            String status) {

        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.rentalDays = rentalDays;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getStatus() {
        return status;
    }
}