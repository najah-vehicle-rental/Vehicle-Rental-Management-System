package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.service.NotificationService;

public class EmailRentalObserver
        implements RentalObserver {

    private final NotificationService notificationService;

    public EmailRentalObserver(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @Override
    public void update(RentalEvent event) {
        if (event == null ||
                event.getRental() == null ||
                notificationService == null) {

            return;
        }

        Rental rental = event.getRental();

        String subject;
        String message;

        if (event.getType() == RentalEventType.RENTED) {
            subject = "Vehicle Rental Confirmed";

            message =
                    "Vehicle " +
                            rental.getVehicleId() +
                            " was rented successfully. " +
                            "Expiry date: " +
                            rental.getExpiryDate();

        } else if (
                event.getType() ==
                        RentalEventType.LATE_RETURNED) {

            subject = "Vehicle Returned Late";

            message =
                    "Vehicle " +
                            rental.getVehicleId() +
                            " was returned " +
                            event.getLateDays() +
                            " day(s) late.";

        } else {
            subject = "Vehicle Returned";

            message =
                    "Vehicle " +
                            rental.getVehicleId() +
                            " was returned successfully.";
        }

        notificationService.sendEmail(
                rental.getCustomerEmail(),
                subject,
                message
        );
    }
}