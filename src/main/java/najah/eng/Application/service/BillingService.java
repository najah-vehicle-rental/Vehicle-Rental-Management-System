package najah.eng.Application.service;

public class BillingService {

    private static final double DAILY_RATE = 50.0;

    public double calculateRentalCost(int rentalDays) {
        if (rentalDays <= 0) {
            return 0;
        }

        return rentalDays * DAILY_RATE;
    }

    public double getDailyRate() {
        return DAILY_RATE;
    }
}