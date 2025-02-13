package coworking.service;

import coworking.dto.ReservationObserver;

public class UserObserver implements ReservationObserver {
    private String name;

    public UserObserver(String name) {
        this.name = name;
    }

    @Override
    public void onReservationCancelled(int reservationId) {
        System.out.println("Notification for " + name + ": Your reservation with ID " + reservationId + " has been cancelled.");
    }

    @Override
    public void onReservationChanged(int reservationId) {
        System.out.println("Notification for " + name + ": Your reservation with ID " + reservationId + " was changed.");
    }
}

