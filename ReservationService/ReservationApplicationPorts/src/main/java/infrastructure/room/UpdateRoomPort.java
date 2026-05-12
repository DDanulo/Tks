package infrastructure.room;
import domain.Room;

public interface UpdateRoomPort {
    public Room updateRoom(String id, Room obj);
}
