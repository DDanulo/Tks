package control.room;
import domain.Room;

public interface UpdateRoomUseCase {
    public Room update(String id, Room obj);
}
