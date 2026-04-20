package mappers;

import adapterModel.RoomEntity;
import domain.Room;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class RoomEntityMapper {
    public Room toRoom(RoomEntity roomEntity){
        return Room.builder()
                .roomType(roomEntity.getRoomType())
                .roomId(roomEntity.getRoomId() != null ? roomEntity.getRoomId().toString() : null)
                .basePrice(roomEntity.getBasePrice())
                .capacity(roomEntity.getCapacity())
                .build();
    }
    
    public RoomEntity toEntity(Room room){
        return RoomEntity.builder()
                .roomId(room.getRoomId() != null ? new ObjectId(room.getRoomId()) : null)
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .basePrice(room.getBasePrice())
                .build();
    }
}
