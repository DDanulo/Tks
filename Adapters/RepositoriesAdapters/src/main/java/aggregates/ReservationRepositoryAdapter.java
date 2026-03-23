package aggregates;

import domain.Reservation;
import infrastructure.reservation.AddReservationPort;
import infrastructure.reservation.FindReservationPort;
import infrastructure.reservation.RemoveReservationPort;
import infrastructure.reservation.UpdateReservationPort;
import lombok.RequiredArgsConstructor;
import mappers.ReservationMapper;
import org.bson.types.ObjectId;
import repository.ReservationRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements
        AddReservationPort,
        RemoveReservationPort,
        UpdateReservationPort,
        FindReservationPort {

    private final ReservationRepository repository;
    private final ReservationMapper reservationMapper;

    @Override
    public Reservation add(Reservation reservation) {
        return reservationMapper.toReservation(repository.add(reservationMapper.toEntity(reservation)));
    }

    @Override
    public Reservation update(String id, Reservation obj) {
        return reservationMapper.toReservation(repository.update(new ObjectId(id), reservationMapper.toEntity(obj)));
    }

    @Override
    public Optional<Reservation> findById(String id) {
        return repository.findById(new ObjectId(id)).map(reservationMapper::toReservation);
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll().stream().map(reservationMapper::toReservation).toList();
    }

    @Override
    public List<Reservation> findByRoom(String roomId) {
        return repository.findByRoom(new ObjectId(roomId)).stream().map(reservationMapper::toReservation).toList();
    }

    @Override
    public List<Reservation> findByUser(String userId) {
        return repository.findByClient(new ObjectId(userId)).stream().map(reservationMapper::toReservation).toList();
    }

    @Override
    public void remove(String reservation) {
        repository.remove(new ObjectId(reservation));
    }
}
