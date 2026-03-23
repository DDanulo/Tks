package infrastructure.reservation;

import domain.Reservation;

public interface AddReservationPort {
    Reservation add(Reservation reservation);
}
