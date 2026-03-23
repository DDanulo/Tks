package control.reservation;

import domain.Reservation;

public interface UpdateReservationUseCase {
    Reservation update(String id, Reservation obj);
    void endReservation(String id);
}
