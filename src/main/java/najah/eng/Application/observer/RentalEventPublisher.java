package najah.eng.Application.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Publishes rental events to registered observers.
 *
 * <p>This class represents the publisher in the Observer design pattern.
 * Observers can be added, removed, and notified whenever a rental event
 * occurs.</p>
 */
public class RentalEventPublisher {

    /**
     * The observers currently registered to receive rental events.
     */
    private final List<RentalObserver> observers;

    /**
     * Creates an empty rental-event publisher.
     */
    public RentalEventPublisher() {
        observers = new ArrayList<>();
    }

    /**
     * Registers an observer.
     *
     * <p>A null observer is ignored. The same observer is not added
     * more than once.</p>
     *
     * @param observer the observer to register
     */
    public void addObserver(RentalObserver observer) {
        if (observer == null) {
            return;
        }

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the publisher.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(RentalObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all currently registered observers about an event.
     *
     * <p>A copy of the observer list is used so that changes to the
     * original list during notification do not interrupt iteration.</p>
     *
     * @param event the rental event to publish
     */
    public void notifyObservers(RentalEvent event) {
        if (event == null) {
            return;
        }

        List<RentalObserver> currentObservers =
                new ArrayList<>(observers);

        for (RentalObserver observer : currentObservers) {
            try {
                observer.update(event);
            } catch (RuntimeException e) {
                System.out.println(
                        "Observer notification failed."
                );
            }
        }
    }

    /**
     * Returns the number of registered observers.
     *
     * @return the number of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
}