package infrastructure.reservation;

import domain.Reservation;

public interface UpdateReservationPort {
    Reservation update(String id, Reservation obj);
}
