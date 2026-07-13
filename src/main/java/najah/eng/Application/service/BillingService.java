package najah.eng.Application.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillingService {

    private static final double DAILY_RATE = 50.0;
    private static final double LATE_PENALTY_PER_DAY = 25.0;

    public double calculateRentalCost(int rentalDays) {
        if (rentalDays <= 0) {
            return 0;
        }

        return rentalDays * DAILY_RATE;
    }

    public long calculateLateDays(
            LocalDate expiryDate,
            LocalDate returnDate) {

        if (expiryDate == null || returnDate == null) {
            return 0;
        }

        if (!returnDate.isAfter(expiryDate)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(
                expiryDate,
                returnDate
        );
    }

    public double calculateLatePenalty(
            LocalDate expiryDate,
            LocalDate returnDate) {

        long lateDays =
                calculateLateDays(
                        expiryDate,
                        returnDate
                );

        return lateDays * LATE_PENALTY_PER_DAY;
    }

    public double calculateTotalCost(
            int rentalDays,
            LocalDate expiryDate,
            LocalDate returnDate) {

        double rentalCost =
                calculateRentalCost(rentalDays);

        double latePenalty =
                calculateLatePenalty(
                        expiryDate,
                        returnDate
                );

        return rentalCost + latePenalty;
    }

    public double getDailyRate() {
        return DAILY_RATE;
    }

    public double getLatePenaltyPerDay() {
        return LATE_PENALTY_PER_DAY;
    }
}