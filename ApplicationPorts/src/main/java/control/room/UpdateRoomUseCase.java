package control.room;
import domain.Room;

public interface UpdateRoomUseCase {
    Room update(String id, Room obj);
}
