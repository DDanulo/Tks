package domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @NotNull
    private String reservationId;

    @NotNull
    private Room room;

    @NotNull
    private ReservationClient client;

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

    public Reservation(String reservationId, Room room, ReservationClient client, LocalDateTime startTime, Double price) {
        this.reservationId = reservationId;
        this.room = room;
        this.client = client;
        this.startTime = startTime;
        this.price = price;
    }
}
