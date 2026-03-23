package aggregates;


import control.reservation.AddReservationUseCase;
import control.reservation.GetReservationUseCase;
import control.reservation.RemoveReservationUseCase;
import control.reservation.UpdateReservationUseCase;
import lombok.RequiredArgsConstructor;
import mappers.ReservationMapper;
import model.CreateReservationDTO;
import model.ShowReservationDTO;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReservationControllerAdapter {

    private final ReservationMapper reservationMapper;
    private final AddReservationUseCase addReservationUseCase;
    private final GetReservationUseCase getReservationUseCase;
    private final UpdateReservationUseCase updateReservationUseCase;
    private final RemoveReservationUseCase removeReservationUseCase;

    public ShowReservationDTO createReservation(CreateReservationDTO res) {
        return reservationMapper.reservationToShowReservationDTO(addReservationUseCase.add(reservationMapper.createReservationDTOToReservation(res)));
    }

    public Optional<ShowReservationDTO> getOneReservationById(String id) {
        return getReservationUseCase.findOneReservationById(id).map(reservationMapper::reservationToShowReservationDTO);
    }

    public List<ShowReservationDTO> getAllReservations() {
        return getReservationUseCase.findAllReservations().stream().map(reservationMapper::reservationToShowReservationDTO).toList();
    }

    public void removeReservationById(String id) {
        removeReservationUseCase.removeReservation(id);
    }

    public ShowReservationDTO updateReservation(String id, CreateReservationDTO res) {
        return reservationMapper.reservationToShowReservationDTO(updateReservationUseCase.update(id, reservationMapper.createReservationDTOToReservation(res)));
    }

    public List<ShowReservationDTO> findCurrentForClient(String clientId) {
        return getReservationUseCase.findCurrentByClientId(clientId).stream()
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    public void endReservation(String id) {
        updateReservationUseCase.endReservation(id);
    }

    public List<ShowReservationDTO> findAllForClient(String clientId) {
        return getReservationUseCase.findCurrentByClientId(clientId).stream()
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    public List<ShowReservationDTO> findCurrentForRoom(String roomId) {
        return getReservationUseCase.findCurrentByRoomId(roomId).stream()
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    public List<ShowReservationDTO> findPastForRoom(String roomId) {
        return getReservationUseCase.findPastByRoomId(roomId).stream()
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    public List<ShowReservationDTO> findPastForClient(String clientId) {
        return getReservationUseCase.findPastByClientId(clientId).stream()
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }
}
