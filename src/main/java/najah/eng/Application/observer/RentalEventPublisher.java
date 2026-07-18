package najah.eng.Application.observer;

import java.util.ArrayList;
import java.util.List;

public class RentalEventPublisher {

    private final List<RentalObserver> observers;

    public RentalEventPublisher() {
        observers = new ArrayList<>();
    }

    public void addObserver(RentalObserver observer) {
        if (observer == null) {
            return;
        }

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(RentalObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(RentalEvent event) {
        if (event == null) {
            return;
        }

        List<RentalObserver> currentObservers =
                new ArrayList<>(observers);

        for (RentalObserver observer : currentObservers) {
            try {
                observer.update(event);
            } catch (RuntimeException e) {
                System.out.println(
                        "Observer notification failed."
                );
            }
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}