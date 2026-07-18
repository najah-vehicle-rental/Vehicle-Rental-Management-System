package najah.eng.Application.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillingServiceTest {

    private final BillingService billingService =
            new BillingService();

    @Test
    public void rentalCostUsesDailyRate() {
        assertEquals(
                250.0,
                billingService.calculateRentalCost(5)
        );
    }

    @Test
    public void zeroRentalDaysHasZeroCost() {
        assertEquals(
                0.0,
                billingService.calculateRentalCost(0)
        );
    }

    @Test
    public void earlyReturnHasNoLateDays() {
        LocalDate expiryDate =
                LocalDate.of(2026, 7, 20);

        LocalDate returnDate =
                LocalDate.of(2026, 7, 18);

        assertEquals(
                0,
                billingService.calculateLateDays(
                        expiryDate,
                        returnDate
                )
        );
    }

    @Test
    public void onTimeReturnHasNoLateDays() {
        LocalDate date =
                LocalDate.of(2026, 7, 20);

        assertEquals(
                0,
                billingService.calculateLateDays(
                        date,
                        date
                )
        );
    }

    @Test
    public void lateDaysAreCalculatedCorrectly() {
        LocalDate expiryDate =
                LocalDate.of(2026, 7, 20);

        LocalDate returnDate =
                LocalDate.of(2026, 7, 23);

        assertEquals(
                3,
                billingService.calculateLateDays(
                        expiryDate,
                        returnDate
                )
        );
    }

    @Test
    public void latePenaltyUsesPenaltyRate() {
        LocalDate expiryDate =
                LocalDate.of(2026, 7, 20);

        LocalDate returnDate =
                LocalDate.of(2026, 7, 23);

        assertEquals(
                75.0,
                billingService.calculateLatePenalty(
                        expiryDate,
                        returnDate
                )
        );
    }

    @Test
    public void totalCostIncludesLatePenalty() {
        LocalDate expiryDate =
                LocalDate.of(2026, 7, 20);

        LocalDate returnDate =
                LocalDate.of(2026, 7, 23);

        assertEquals(
                325.0,
                billingService.calculateTotalCost(
                        5,
                        expiryDate,
                        returnDate
                )
        );
    }
}