package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class RentalEventPublisherTest {

    @Test
    public void registeredObserverReceivesEvent() {
        RentalObserver observer =
                mock(RentalObserver.class);

        RentalEventPublisher publisher =
                new RentalEventPublisher();

        RentalEvent event =
                createEvent();

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

        RentalEvent event =
                createEvent();

        publisher.addObserver(observer);
        publisher.removeObserver(observer);
        publisher.notifyObservers(event);

        verifyNoInteractions(observer);
    }

    @Test
    public void nullObserverIsNotAdded() {
        RentalEventPublisher publisher =
                new RentalEventPublisher();

        publisher.addObserver(null);

        assertEquals(
                0,
                publisher.getObserverCount()
        );
    }

    @Test
    public void duplicateObserverIsAddedOnce() {
        RentalObserver observer =
                mock(RentalObserver.class);

        RentalEventPublisher publisher =
                new RentalEventPublisher();

        RentalEvent event =
                createEvent();

        publisher.addObserver(observer);
        publisher.addObserver(observer);

        assertEquals(
                1,
                publisher.getObserverCount()
        );

        publisher.notifyObservers(event);

        verify(
                observer,
                times(1)
        ).update(event);
    }

    @Test
    public void nullEventDoesNotNotifyObservers() {
        RentalObserver observer =
                mock(RentalObserver.class);

        RentalEventPublisher publisher =
                new RentalEventPublisher();

        publisher.addObserver(observer);
        publisher.notifyObservers(null);

        verifyNoInteractions(observer);
    }

    @Test
    public void failingObserverDoesNotStopOtherObservers() {
        RentalObserver failingObserver =
                mock(RentalObserver.class);

        RentalObserver workingObserver =
                mock(RentalObserver.class);

        RentalEventPublisher publisher =
                new RentalEventPublisher();

        RentalEvent event =
                createEvent();

        doThrow(
                new RuntimeException()
        ).when(failingObserver).update(event);

        publisher.addObserver(
                failingObserver
        );

        publisher.addObserver(
                workingObserver
        );

        publisher.notifyObservers(event);

        verify(failingObserver)
                .update(event);

        verify(workingObserver)
                .update(event);
    }

    private RentalEvent createEvent() {
        Rental rental = new Rental(
                "1",
                "Fadi",
                "fadi@example.com",
                5,
                LocalDate.of(2026, 7, 23),
                "Active"
        );

        return new RentalEvent(
                RentalEventType.RENTED,
                rental
        );
    }
}