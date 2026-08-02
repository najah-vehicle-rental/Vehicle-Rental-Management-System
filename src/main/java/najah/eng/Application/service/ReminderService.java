package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Persistence.RentalRepository;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Generates email reminders for active rentals that expire
 * today or the following day.
 */
public class ReminderService {

    /**
     * Repository used to retrieve active rental records.
     */
    private final RentalRepository rentalRepository;

    /**
     * Service used to send expiry-reminder emails.
     */
    private final NotificationService notificationService;

    /**
     * Creates a reminder service using the supplied dependencies.
     *
     * @param rentalRepository    repository used to access active rentals
     * @param notificationService service used to send reminder emails
     */
    public ReminderService(
            RentalRepository rentalRepository,
            NotificationService notificationService) {

        this.rentalRepository = rentalRepository;
        this.notificationService = notificationService;
    }

    /**
     * Generates expiry reminders using the current system date.
     *
     * @return the number of reminders sent
     */
    public int generateExpiryReminders() {
        return generateExpiryReminders(LocalDate.now());
    }

    /**
     * Generates expiry reminders using a supplied reference date.
     *
     * <p>A reminder is sent when an active rental expires on the
     * supplied date or on the following day.</p>
     *
     * @param today the reference date used to check rental expiry
     * @return the number of reminders sent
     */
    public int generateExpiryReminders(LocalDate today) {
        ArrayList<Rental> rentals =
                rentalRepository.findActiveRentals();

        int reminderCount = 0;

        for (Rental rental : rentals) {
            LocalDate expiryDate = rental.getExpiryDate();

            boolean expiresToday =
                    expiryDate.equals(today);

            boolean expiresTomorrow =
                    expiryDate.equals(today.plusDays(1));

            if (expiresToday || expiresTomorrow) {
                String subject =
                        "Rental Expiry Reminder";

                String message =
                        "Hello " +
                                rental.getCustomerName() +
                                ", your rental for vehicle " +
                                rental.getVehicleId() +
                                " expires on " +
                                rental.getExpiryDate() +
                                ".";

                notificationService.sendEmail(
                        rental.getCustomerEmail(),
                        subject,
                        message
                );

                reminderCount++;
            }
        }

        return reminderCount;
    }
}