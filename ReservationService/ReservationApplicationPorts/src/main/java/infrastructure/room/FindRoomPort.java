package infrastructure.room;

import domain.Room;

import java.util.List;
import java.util.Optional;

public interface FindRoomPort {
    public Optional<Room> findById(String id);

    public List<Room> findAll();
}
