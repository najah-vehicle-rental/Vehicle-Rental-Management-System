package najah.eng.Application.observer;

/**
 * Defines the operation implemented by objects that observe rental events.
 *
 * <p>Classes implementing this interface receive notifications whenever
 * a rental-related event is published.</p>
 */
public interface RentalObserver {

    /**
     * Receives and processes a rental event.
     *
     * @param event the rental event to process
     */
    void update(RentalEvent event);
}