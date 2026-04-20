package mappers;

import adapterModel.ReservationEntity;
import client.data.ClientEntity;
import domain.Client;
import domain.Reservation;
import domain.Room;
import domain.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEntityMapper {
    private final RoomEntityMapper roomEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public Reservation toReservation(ReservationEntity entity){
        if (entity == null) return null;

        // Use your existing UserEntityMapper.EntityToUser
        User user = userEntityMapper.EntityToUser(entity.getClientEntity());

        return Reservation.builder()
                .reservationId(entity.getReservationId() != null ? entity.getReservationId().toString() : null)
                .room(entity.getRoomEntity() != null ? roomEntityMapper.toRoom(entity.getRoomEntity()) : null)
                .client((Client) user) // Cast to Client as per your Reservation domain
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .price(entity.getPrice())
                .build();
    }

    public ReservationEntity toEntity(Reservation reservation){
        if (reservation == null) return null;

        // Convert domain Client to ClientEntity using your UserEntityMapper
        ClientEntity clientEntity = null;
        if (reservation.getClient() != null) {
            clientEntity = userEntityMapper.toClientEntity(reservation.getClient());
            // CRITICAL: Ensure the ID is passed so Mongo knows which existing user this is!
            if (reservation.getClient().getUserId() != null) {
                clientEntity.setUserId(new ObjectId(reservation.getClient().getUserId()));
            }
        }

        return ReservationEntity.builder()
                .reservationId(reservation.getReservationId() != null ? new ObjectId(reservation.getReservationId()) : null)
                .roomEntity(reservation.getRoom() != null ? roomEntityMapper.toEntity(reservation.getRoom()) : null)
                .clientEntity(clientEntity)
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .price(reservation.getPrice())
                .build();
    }
}
