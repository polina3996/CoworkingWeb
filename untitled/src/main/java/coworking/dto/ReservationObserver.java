package coworking.dto;


public interface ReservationObserver {
    void onReservationCancelled(int reservationId);

    void onReservationChanged(int reservationId);
}
