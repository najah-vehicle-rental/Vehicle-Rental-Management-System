package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.service.NotificationService;

/**
 * Observes rental events and sends the appropriate email notification
 * to the customer.
 */
public class EmailRentalObserver
        implements RentalObserver {

    /**
     * The notification service used to send customer emails.
     */
    private final NotificationService notificationService;

    /**
     * Creates an email rental observer.
     *
     * @param notificationService the service used to send notifications
     */
    public EmailRentalObserver(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    /**
     * Processes a rental event and sends a corresponding email.
     *
     * <p>No notification is sent when the event, rental, or notification
     * service is null.</p>
     *
     * @param event the rental event to process
     */
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