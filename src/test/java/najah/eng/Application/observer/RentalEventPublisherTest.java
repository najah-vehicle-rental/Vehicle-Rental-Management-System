package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class RentalEventPublisherTest {

    @Test
    public void registeredObserverReceivesEvent() {
        RentalObserver observer =
                mock(RentalObserver.class);

        RentalEventPublisher publisher =
                new RentalEventPublisher();

        Rental rental = new Rental(
                "1",
                "Fadi",
                "fadi@example.com",
                5,
                LocalDate.now().plusDays(5),
                "Active"
        );

        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RENTED,
                        rental
                );

        publisher.addObserver(observer);
        publisher.notifyObservers(event);

        verify(observer).update(event);
    }

    @Test
    public void removedObserverDoesNotReceiveEvent() {
        RentalObserver observer =
                mock(RentalObserver.class);

        RentalEventPublisher publisher =
                new RentalEventPublisher();

        Rental rental = new Rental(
                "1",
                "Fadi",
                "fadi@example.com",
                5,
                LocalDate.now().plusDays(5),
                "Active"
        );

        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RENTED,
                        rental
                );

        publisher.addObserver(observer);
        publisher.removeObserver(observer);
        publisher.notifyObservers(event);

        verifyNoInteractions(observer);
    }
}