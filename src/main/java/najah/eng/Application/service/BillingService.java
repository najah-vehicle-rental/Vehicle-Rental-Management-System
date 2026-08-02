package najah.eng.Application.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Provides billing calculations for vehicle rentals.
 *
 * <p>The service calculates the basic rental cost, the number of
 * late-return days, late penalties, and the final total cost.</p>
 */
public class BillingService {

    /**
     * Daily rental cost for a vehicle.
     */
    private static final double DAILY_RATE = 50.0;

    /**
     * Penalty charged for every late-return day.
     */
    private static final double LATE_PENALTY_PER_DAY = 25.0;

    /**
     * Calculates the basic rental cost.
     *
     * @param rentalDays the number of rental days
     * @return the basic rental cost, or {@code 0} when the number
     *         of rental days is zero or negative
     */
    public double calculateRentalCost(int rentalDays) {
        if (rentalDays <= 0) {
            return 0;
        }

        return rentalDays * DAILY_RATE;
    }

    /**
     * Calculates the number of days between the expiry date
     * and a late return date.
     *
     * @param expiryDate the date on which the rental expires
     * @param returnDate the date on which the vehicle is returned
     * @return the number of late days, or {@code 0} when either date
     *         is null or the return is not late
     */
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

    /**
     * Calculates the monetary penalty for a late return.
     *
     * @param expiryDate the rental expiry date
     * @param returnDate the actual vehicle return date
     * @return the calculated late-return penalty
     */
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

    /**
     * Calculates the complete rental cost, including any
     * applicable late-return penalty.
     *
     * @param rentalDays the number of rental days
     * @param expiryDate the rental expiry date
     * @param returnDate the actual vehicle return date
     * @return the total rental cost
     */
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

    /**
     * Returns the configured daily rental rate.
     *
     * @return the daily rental rate
     */
    public double getDailyRate() {
        return DAILY_RATE;
    }

    /**
     * Returns the configured penalty charged per late day.
     *
     * @return the late penalty per day
     */
    public double getLatePenaltyPerDay() {
        return LATE_PENALTY_PER_DAY;
    }
}