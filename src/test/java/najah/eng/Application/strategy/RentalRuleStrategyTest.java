package najah.eng.Application.strategy;

import najah.eng.Application.Domain.RentalRequirements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RentalRuleStrategyTest {

    @Test
    public void defaultStrategyAllowsRental() {
        RentalRuleStrategy strategy =
                new DefaultRentalRuleStrategy();

        RentalRequirements requirements =
                new RentalRequirements(
                        0,
                        false,
                        0
                );

        assertTrue(
                strategy.isRentalAllowed(
                        requirements
                )
        );

        assertFalse(
                strategy
                        .getRuleDescription()
                        .isBlank()
        );
    }

    @Test
    public void truckStrategyRequiresLicense() {
        RentalRuleStrategy strategy =
                new TruckLicenseStrategy();

        assertFalse(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                30,
                                false,
                                0
                        )
                )
        );

        assertTrue(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                30,
                                true,
                                0
                        )
                )
        );

        assertFalse(
                strategy
                        .getRuleDescription()
                        .isBlank()
        );
    }

    @Test
    public void electricStrategyChecksBattery() {
        RentalRuleStrategy strategy =
                new ElectricBatteryStrategy();

        assertFalse(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                30,
                                false,
                                29
                        )
                )
        );

        assertTrue(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                30,
                                false,
                                30
                        )
                )
        );

        assertTrue(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                30,
                                false,
                                100
                        )
                )
        );

        assertFalse(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                30,
                                false,
                                101
                        )
                )
        );

        assertFalse(
                strategy
                        .getRuleDescription()
                        .isBlank()
        );
    }

    @Test
    public void motorcycleStrategyChecksAge() {
        RentalRuleStrategy strategy =
                new MotorcycleAgeStrategy();

        assertFalse(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                17,
                                false,
                                0
                        )
                )
        );

        assertTrue(
                strategy.isRentalAllowed(
                        new RentalRequirements(
                                18,
                                false,
                                0
                        )
                )
        );

        assertFalse(
                strategy
                        .getRuleDescription()
                        .isBlank()
        );
    }
}