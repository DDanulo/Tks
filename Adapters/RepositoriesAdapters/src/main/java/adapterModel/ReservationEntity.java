package adapterModel;

import client.data.ClientEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationEntity {
    @NotNull
    @BsonId
    private ObjectId reservationId;

    @NotNull
    @BsonProperty("room")
    private RoomEntity roomEntity;

    @NotNull
    @BsonProperty("client")
    private ClientEntity clientEntity;

    @NotNull
    @BsonProperty("start_time")
    private LocalDateTime startTime;

    @NotNull
    @BsonProperty("end_time")
    private LocalDateTime endTime;

    @BsonProperty("price")
    private Double price;

    public Double calculateActualPrice() {
        return roomEntity.getBasePrice();
    }

    public double hoursReserved() {
        return (double) (endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC)) / 60 / 60;
    }

    public ReservationEntity(ObjectId reservationId, RoomEntity roomEntity, ClientEntity clientEntity, LocalDateTime startTime, Double price) {
        this.reservationId = reservationId;
        this.roomEntity = roomEntity;
        this.clientEntity = clientEntity;
        this.startTime = startTime;
        this.price = price;
    }
}
