package domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import domain.RoomType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    public Room(RoomType roomType, Integer capacity, Double basePrice) {
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }
    @NotNull
    private ObjectId roomId;

    @NotNull
    private RoomType roomType;

    @NotNull
    @Min(1)
    private Integer capacity;

    private Double basePrice;
}
