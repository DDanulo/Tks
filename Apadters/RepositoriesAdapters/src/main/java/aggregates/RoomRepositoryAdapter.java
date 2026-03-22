package aggregates;

import domain.Room;
import infrastructure.room.AddRoomPort;
import infrastructure.room.FindRoomPort;
import infrastructure.room.RemoveRoomPort;
import infrastructure.room.UpdateRoomPort;
import lombok.RequiredArgsConstructor;
import mappers.RoomMapper;
import org.bson.types.ObjectId;
import repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RoomRepositoryAdapter implements AddRoomPort, UpdateRoomPort, FindRoomPort, RemoveRoomPort {
    private final RoomRepository repository;
    private final RoomMapper roomMapper;

    @Override
    public Room add(Room room) {
        return roomMapper.toRoom(repository.add(roomMapper.toEntity(room)));
    }

    @Override
    public Optional<Room> findById(String id) {
        return repository.findById(new ObjectId(id)).map(roomMapper::toRoom);
    }

    @Override
    public List<Room> findAll() {
        return repository.findAll().stream().map(roomMapper::toRoom).toList();
    }

    @Override
    public void remove(String room) {
        repository.remove(new ObjectId(room));
    }

    @Override
    public Room updateRoom(String id, Room obj) {
        return roomMapper.toRoom(repository.update(new ObjectId(id), roomMapper.toEntity(obj)));
    }
}
