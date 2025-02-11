package coworking.dto;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationSubject {
    private List<ReservationObserver> observers = new ArrayList<>();

    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(int reservationId) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCancelled(reservationId);
        }
    }

    public void notifyObserversOnChange(int reservationId) {
        for (ReservationObserver observer : observers) {
            observer.onReservationChanged(reservationId);
        }
    }
}
