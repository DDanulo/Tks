package model;

import domain.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ShowRoomDTO {

    @NotNull
    private String id;

    @NotNull
    private RoomType roomType;

    @NotNull
    @Min(0)
    private Integer capacity;

    @NotNull
    @DecimalMin("0.0")
    private Double basePrice;
}
