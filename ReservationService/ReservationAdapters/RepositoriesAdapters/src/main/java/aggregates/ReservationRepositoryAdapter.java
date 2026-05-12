package aggregates;

import domain.Reservation;
import infrastructure.reservation.AddReservationPort;
import infrastructure.reservation.FindReservationPort;
import infrastructure.reservation.RemoveReservationPort;
import infrastructure.reservation.UpdateReservationPort;
import lombok.RequiredArgsConstructor;
import mappers.ReservationEntityMapper;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import repository.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements
        AddReservationPort,
        RemoveReservationPort,
        UpdateReservationPort,
        FindReservationPort {

    private final ReservationRepository repository;
    private final ReservationEntityMapper reservationEntityMapper;

    @Override
    public Reservation add(Reservation reservation) {
        return reservationEntityMapper.toReservation(repository.add(reservationEntityMapper.toEntity(reservation)));
    }

    @Override
    public Reservation update(String id, Reservation obj) {
        return reservationEntityMapper.toReservation(repository.update(new ObjectId(id), reservationEntityMapper.toEntity(obj)));
    }

    @Override
    public Optional<Reservation> findById(String id) {
        return repository.findById(new ObjectId(id)).map(reservationEntityMapper::toReservation);
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll().stream().map(reservationEntityMapper::toReservation).toList();
    }

    @Override
    public List<Reservation> findByRoom(String roomId) {
        return repository.findByRoom(new ObjectId(roomId)).stream().map(reservationEntityMapper::toReservation).toList();
    }

    @Override
    public List<Reservation> findByUser(String userId) {
        return repository.findByClient(new ObjectId(userId)).stream().map(reservationEntityMapper::toReservation).toList();
    }

    @Override
    public void remove(String reservation) {
        repository.remove(new ObjectId(reservation));
    }
}
