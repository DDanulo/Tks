package domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @NotNull
    private ObjectId reservationId;

    @NotNull
    private Room room;

    @NotNull
    private Client client;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    
    private Double price;

    public Double calculateActualPrice() {
        return room.getBasePrice();
    }

    public double hoursReserved() {
        return (double) (endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC)) / 60 / 60;
    }

    public Reservation(ObjectId reservationId, Room room, Client client, LocalDateTime startTime, Double price) {
        this.reservationId = reservationId;
        this.room = room;
        this.client = client;
        this.startTime = startTime;
        this.price = price;
    }
}
