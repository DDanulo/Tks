package infrastructure.reservation;
import domain.Reservation;

public interface UpdateReservationPort {
    public void update(String id, Reservation obj);
}
