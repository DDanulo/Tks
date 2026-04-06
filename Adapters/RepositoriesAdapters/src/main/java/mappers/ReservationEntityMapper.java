package mappers;

import adapterModel.ReservationEntity;
import domain.Client;
import domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEntityMapper {
    private final RoomEntityMapper roomEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public Reservation toReservation(ReservationEntity entity){
        return Reservation.builder()
                .reservationId(entity.getReservationId())
                .room(roomEntityMapper.toRoom(entity.getRoomEntity()))
                .client((Client) userEntityMapper.EntityToUser(entity.getClientEntity()))
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .price(entity.getPrice())
                .build();
    }

    public ReservationEntity toEntity(Reservation reservation){
        return ReservationEntity.builder()
                .reservationId(reservation.getReservationId())
                .roomEntity(roomEntityMapper.toEntity(reservation.getRoom()))
                .clientEntity(userEntityMapper.toClientEntity(reservation.getClient()))
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .price(reservation.getPrice())
                .build();
    }
}
