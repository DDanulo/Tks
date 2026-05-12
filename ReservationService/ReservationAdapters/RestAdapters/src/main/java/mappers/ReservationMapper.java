package mappers;

import domain.ReservationClient;
import domain.Reservation;
import domain.Room;
import model.CreateReservationDTO;
import model.ShowReservationDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation createReservationDTOToReservation(CreateReservationDTO reservationDTO) {
        Room room = Room.builder()
                .roomId(reservationDTO.getRoomId())
                .build();

        ReservationClient client = ReservationClient.builder()
                .userId(reservationDTO.getClientId())
                .build();

        return Reservation.builder()
                .room(room)
                .client(client)
                .price(reservationDTO.getPrice())
                .startTime(reservationDTO.getStartTime())
                .build();
    }

    public ShowReservationDTO reservationToShowReservationDTO(Reservation reservation) {
        return ShowReservationDTO.builder()
                .reservationId(reservation.getReservationId().toString())
                .clientId(reservation.getClient().getUserId())
                .roomId(reservation.getRoom().getRoomId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .price(reservation.getPrice())
                .build();
    }
}
