package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;

/**
 * Observes rental events and prints basic event information
 * to the console for auditing purposes.
 */
public class AuditLogObserver
        implements RentalObserver {

    /**
     * Creates an audit-log observer.
     */
    public AuditLogObserver() {
        // Default constructor.
    }

    /**
     * Processes a rental event by printing its audit information.
     *
     * <p>No information is printed when the event or its rental
     * record is null.</p>
     *
     * @param event the rental event to process
     */
    @Override
    public void update(RentalEvent event) {
        if (event == null ||
                event.getRental() == null) {

            return;
        }

        Rental rental = event.getRental();

        System.out.println(
                "Audit Event: " +
                        event.getType()
        );

        System.out.println(
                "Audit Vehicle ID: " +
                        rental.getVehicleId()
        );

        System.out.println(
                "Audit Customer: " +
                        rental.getCustomerName()
        );
    }
}