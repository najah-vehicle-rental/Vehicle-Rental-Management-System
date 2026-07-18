package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;

public class RentalEvent {

    private final RentalEventType type;
    private final Rental rental;
    private final long lateDays;

    public RentalEvent(
            RentalEventType type,
            Rental rental) {

        this(type, rental, 0);
    }

    public RentalEvent(
            RentalEventType type,
            Rental rental,
            long lateDays) {

        this.type = type;
        this.rental = rental;
        this.lateDays = lateDays;
    }

    public RentalEventType getType() {
        return type;
    }

    public Rental getRental() {
        return rental;
    }

    public long getLateDays() {
        return lateDays;
    }
}