package najah.eng.Application.service;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.Persistence.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private NotificationService notificationService;

    @Test
    void generatesReminderForRentalExpiringTomorrow() {
        LocalDate today = LocalDate.of(2026, 7, 12);

        Rental rental = new Rental(
                "1",
                "Ahmad",
                "ahmad@example.com",
                1,
                today.plusDays(1),
                "Active"
        );

        when(rentalRepository.findActiveRentals())
                .thenReturn(new ArrayList<>(List.of(rental)));

        ReminderService reminderService =
                new ReminderService(
                        rentalRepository,
                        notificationService
                );

        int result =
                reminderService.generateExpiryReminders(today);

        assertEquals(1, result);

        verify(notificationService).sendEmail(
                eq("ahmad@example.com"),
                eq("Rental Expiry Reminder"),
                contains("2026-07-13")
        );
    }

    @Test
    void doesNotGenerateReminderForLaterRental() {
        LocalDate today = LocalDate.of(2026, 7, 12);

        Rental rental = new Rental(
                "2",
                "Ali",
                "ali@example.com",
                5,
                today.plusDays(5),
                "Active"
        );

        when(rentalRepository.findActiveRentals())
                .thenReturn(new ArrayList<>(List.of(rental)));

        ReminderService reminderService =
                new ReminderService(
                        rentalRepository,
                        notificationService
                );

        int result =
                reminderService.generateExpiryReminders(today);

        assertEquals(0, result);

        verify(
                notificationService,
                never()
        ).sendEmail(
                eq("ali@example.com"),
                eq("Rental Expiry Reminder"),
                contains("2026-07-17")
        );
    }
}