package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;

public class AuditLogObserver
        implements RentalObserver {

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