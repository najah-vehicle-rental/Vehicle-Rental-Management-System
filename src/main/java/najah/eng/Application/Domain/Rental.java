package najah.eng.Application.Domain;

import java.time.LocalDate;

/**
 * Represents a vehicle rental record.
 *
 * <p>A rental stores information about the rented vehicle, customer,
 * rental period, expiry date, and current rental status.</p>
 */
public class Rental {

    /**
     * The identifier of the rented vehicle.
     */
    private final String vehicleId;

    /**
     * The name of the customer renting the vehicle.
     */
    private final String customerName;

    /**
     * The email address of the customer.
     */
    private final String customerEmail;

    /**
     * The number of days in the rental period.
     */
    private final int rentalDays;

    /**
     * The date on which the rental expires.
     */
    private final LocalDate expiryDate;

    /**
     * The current status of the rental.
     */
    private final String status;

    /**
     * Creates a new rental record.
     *
     * @param vehicleId    the identifier of the rented vehicle
     * @param customerName the name of the customer
     * @param customerEmail the email address of the customer
     * @param rentalDays   the number of rental days
     * @param expiryDate   the date on which the rental expires
     * @param status       the current rental status
     */
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

    /**
     * Returns the identifier of the rented vehicle.
     *
     * @return the vehicle identifier
     */
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * Returns the customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Returns the customer email address.
     *
     * @return the customer email address
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Returns the number of rental days.
     *
     * @return the rental duration in days
     */
    public int getRentalDays() {
        return rentalDays;
    }

    /**
     * Returns the rental expiry date.
     *
     * @return the expiry date
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Returns the current rental status.
     *
     * @return the rental status
     */
    public String getStatus() {
        return status;
    }
}