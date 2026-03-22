package control.room;

import domain.Room;

import java.util.List;
import java.util.Optional;

public interface GetRoomUseCase {
    Optional<Room> findOneRoomById(String Id);
    List<Room> GetAllRooms();
}
