package infrastructure.reservation;


import domain.Reservation;

import java.util.List;
import java.util.Optional;


public interface FindReservationPort {
    public Optional<Reservation> findById(String id);

    public List<Reservation> findAll();
    public List<Reservation> findByRoom(String roomId);
    public List<Reservation> findByUser(String userId);
}
