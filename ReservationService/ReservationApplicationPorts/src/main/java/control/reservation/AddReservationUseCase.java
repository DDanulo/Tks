package control.reservation;

import domain.Reservation;

public interface AddReservationUseCase {
    Reservation add(Reservation reservation);
}