package najah.eng.Application.Domain;

public class Rental {

    private String vehicleId;
    private String customerName;
    private int rentalDays;
    private String status;

    public Rental(String vehicleId, String customerName, int rentalDays, String status) {
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.rentalDays = rentalDays;
        this.status = status;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public String getStatus() {
        return status;
    }
}