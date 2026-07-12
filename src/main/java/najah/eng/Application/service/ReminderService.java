package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Persistence.RentalRepository;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReminderService {

    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;

    public ReminderService(
            RentalRepository rentalRepository,
            NotificationService notificationService) {

        this.rentalRepository = rentalRepository;
        this.notificationService = notificationService;
    }

    public int generateExpiryReminders() {
        return generateExpiryReminders(LocalDate.now());
    }

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