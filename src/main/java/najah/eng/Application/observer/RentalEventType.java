package najah.eng.Application.observer;

/**
 * Defines the supported rental-event types.
 */
public enum RentalEventType {

    /**
     * Indicates that a vehicle was rented successfully.
     */
    RENTED,

    /**
     * Indicates that a vehicle was returned on time.
     */
    RETURNED,

    /**
     * Indicates that a vehicle was returned after its expiry date.
     */
    LATE_RETURNED
}