package control.reservation;

import domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface GetReservationUseCase {
    Optional<Reservation> findOneReservationById(String id);
    List<Reservation> findAllReservations();
    List<Reservation> findCurrentByClientId(String clientId);
    List<Reservation> findPastByClientId(String clientId);
    List<Reservation> findCurrentByRoomId(String roomId);
    List<Reservation> findPastByRoomId(String roomId);
}
