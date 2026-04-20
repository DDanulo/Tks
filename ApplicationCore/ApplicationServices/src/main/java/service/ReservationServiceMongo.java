package service;

import control.reservation.AddReservationUseCase;
import control.reservation.GetReservationUseCase;
import control.reservation.RemoveReservationUseCase;
import control.reservation.UpdateReservationUseCase;
import domain.Reservation;
import infrastructure.reservation.AddReservationPort;
import infrastructure.reservation.FindReservationPort;
import infrastructure.reservation.RemoveReservationPort;
import infrastructure.reservation.UpdateReservationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationServiceMongo implements AddReservationUseCase, GetReservationUseCase, RemoveReservationUseCase, UpdateReservationUseCase {

    private final AddReservationPort addReservationPort;
    private final RemoveReservationPort removeReservationPort;
    private final UpdateReservationPort updateReservationPort;
    private final FindReservationPort findReservationPort;

    @Override
    public Reservation add(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        return addReservationPort.add(reservation);
    }

    @Override
    public Optional<Reservation> findOneReservationById(String Id) {
        return findReservationPort.findById(Id);
    }

    @Override
    public List<Reservation> findAllReservations() {
        return findReservationPort.findAll();
    }

    @Override
    public void removeReservation(String Id) {
        if (Id == null) {
            throw new IllegalArgumentException("Reservation Id cannot be null");
        }
        if (findOneReservationById(Id).isEmpty()) {
            throw new IllegalArgumentException();
        }

        removeReservationPort.remove(Id);
    }

    @Override
    public Reservation update(String id, Reservation obj) {
        if (findOneReservationById(id).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return updateReservationPort.update(id, obj);
    }

    @Override
    public List<Reservation> findCurrentByClientId(String clientId) {
        return findReservationPort.findByUser(clientId).stream()
                .filter(res -> res.getEndTime() == null || res.getEndTime().isAfter(LocalDateTime.now()))
                .toList();
    }

    @Override
    public List<Reservation> findPastByClientId(String clientId) {
        return findReservationPort.findByUser(clientId).stream()
                .filter(res -> res.getEndTime() != null && res.getEndTime().isBefore(LocalDateTime.now()))
                .toList();
    }

    @Override
    public List<Reservation> findCurrentByRoomId(String roomId) {
        return findReservationPort.findByRoom(roomId).stream()
                .filter(res -> res.getEndTime() == null || res.getEndTime().isAfter(LocalDateTime.now()))
                .toList();
    }

    @Override
    public List<Reservation> findPastByRoomId(String roomId) {
        return findReservationPort.findByRoom(roomId).stream()
                .filter(res -> res.getEndTime() != null && res.getEndTime().isBefore(LocalDateTime.now()))
                .toList();
    }

    @Override
    public void endReservation(String id) {
        Reservation res = findReservationPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        res.setEndTime(LocalDateTime.now());
        updateReservationPort.update(id, res);
    }
}
