package mappers;

import adapterModel.ReservationEntity;
import domain.Client;
import domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    public Reservation toReservation(ReservationEntity entity){
        return Reservation.builder()
                .reservationId(entity.getReservationId())
                .room(roomMapper.toRoom(entity.getRoomEntity()))
                .client((Client) userMapper.EntityToUser(entity.getClientEntity()))
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .price(entity.getPrice())
                .build();
    }

    public ReservationEntity toEntity(Reservation reservation){
        return ReservationEntity.builder()
                .reservationId(reservation.getReservationId())
                .roomEntity(roomMapper.toEntity(reservation.getRoom()))
                .clientEntity(userMapper.toClientEntity(reservation.getClient()))
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .price(reservation.getPrice())
                .build();
    }
}
