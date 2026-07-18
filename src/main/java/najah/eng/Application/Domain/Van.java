package najah.eng.Application.Domain;

import najah.eng.Application.strategy.DefaultRentalRuleStrategy;

public class Van extends Vehicle {

    public Van(String id, String name, String status) {
        super(
                id,
                name,
                status,
                new DefaultRentalRuleStrategy()
        );
    }

    @Override
    public String getType() {
        return "Van";
    }
}