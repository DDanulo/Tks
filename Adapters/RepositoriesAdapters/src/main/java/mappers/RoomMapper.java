package mappers;

import adapterModel.RoomEntity;
import domain.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public Room toRoom(RoomEntity roomEntity){
        return Room.builder()
                .roomType(roomEntity.getRoomType())
                .roomId(roomEntity.getRoomId())
                .basePrice(roomEntity.getBasePrice())
                .capacity(roomEntity.getCapacity())
                .build();
    }
    
    public RoomEntity toEntity(Room room){
        return RoomEntity.builder()
                .roomId(room.getRoomId())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .basePrice(room.getBasePrice())
                .build();
    }
}
