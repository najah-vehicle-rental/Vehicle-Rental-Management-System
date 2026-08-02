package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;

/**
 * Carries the information associated with a rental or return event.
 *
 * <p>The event contains its type, the related rental record,
 * and the number of late-return days when applicable.</p>
 */
public class RentalEvent {

    /**
     * The type of the rental event.
     */
    private final RentalEventType type;

    /**
     * The rental associated with the event.
     */
    private final Rental rental;

    /**
     * The number of days by which the vehicle was returned late.
     */
    private final long lateDays;

    /**
     * Creates a rental event with no late-return days.
     *
     * @param type   the type of the rental event
     * @param rental the rental associated with the event
     */
    public RentalEvent(
            RentalEventType type,
            Rental rental) {

        this(type, rental, 0);
    }

    /**
     * Creates a rental event with the supplied late-return information.
     *
     * @param type     the type of the rental event
     * @param rental   the rental associated with the event
     * @param lateDays the number of late-return days
     */
    public RentalEvent(
            RentalEventType type,
            Rental rental,
            long lateDays) {

        this.type = type;
        this.rental = rental;
        this.lateDays = lateDays;
    }

    /**
     * Returns the rental-event type.
     *
     * @return the event type
     */
    public RentalEventType getType() {
        return type;
    }

    /**
     * Returns the rental associated with this event.
     *
     * @return the related rental
     */
    public Rental getRental() {
        return rental;
    }

    /**
     * Returns the number of late-return days.
     *
     * @return the late-return days
     */
    public long getLateDays() {
        return lateDays;
    }
}