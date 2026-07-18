package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;
import najah.eng.Application.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class EmailRentalObserverTest {

    private NotificationService notificationService;
    private EmailRentalObserver observer;
    private Rental rental;

    @BeforeEach
    public void setUp() {
        notificationService =
                mock(NotificationService.class);

        observer =
                new EmailRentalObserver(
                        notificationService
                );

        rental = new Rental(
                "1",
                "Fadi",
                "fadi@example.com",
                5,
                LocalDate.of(2026, 7, 23),
                "Active"
        );
    }

    @Test
    public void rentedEventSendsConfirmationEmail() {
        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RENTED,
                        rental
                );

        observer.update(event);

        verify(notificationService)
                .sendEmail(
                        eq("fadi@example.com"),
                        eq("Vehicle Rental Confirmed"),
                        contains("Expiry date")
                );
    }

    @Test
    public void returnedEventSendsReturnEmail() {
        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RETURNED,
                        rental
                );

        observer.update(event);

        verify(notificationService)
                .sendEmail(
                        eq("fadi@example.com"),
                        eq("Vehicle Returned"),
                        contains("returned successfully")
                );
    }

    @Test
    public void lateEventSendsLateEmail() {
        RentalEvent event =
                new RentalEvent(
                        RentalEventType.LATE_RETURNED,
                        rental,
                        3
                );

        observer.update(event);

        verify(notificationService)
                .sendEmail(
                        eq("fadi@example.com"),
                        eq("Vehicle Returned Late"),
                        contains("3 day")
                );
    }

    @Test
    public void nullEventDoesNotSendEmail() {
        observer.update(null);

        verifyNoInteractions(
                notificationService
        );
    }

    @Test
    public void eventWithoutRentalDoesNotSendEmail() {
        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RENTED,
                        null
                );

        observer.update(event);

        verifyNoInteractions(
                notificationService
        );
    }

    @Test
    public void nullNotificationServiceIsHandled() {
        EmailRentalObserver nullObserver =
                new EmailRentalObserver(null);

        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RENTED,
                        rental
                );

        assertDoesNotThrow(
                () -> nullObserver.update(event)
        );
    }
}