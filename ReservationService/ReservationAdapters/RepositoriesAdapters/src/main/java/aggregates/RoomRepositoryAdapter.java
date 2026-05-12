package aggregates;

import domain.Room;
import infrastructure.room.AddRoomPort;
import infrastructure.room.FindRoomPort;
import infrastructure.room.RemoveRoomPort;
import infrastructure.room.UpdateRoomPort;
import lombok.RequiredArgsConstructor;
import mappers.RoomEntityMapper;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomRepositoryAdapter implements AddRoomPort, UpdateRoomPort, FindRoomPort, RemoveRoomPort {
    private final RoomRepository repository;
    private final RoomEntityMapper roomEntityMapper;

    @Override
    public Room add(Room room) {
        return roomEntityMapper.toRoom(repository.add(roomEntityMapper.toEntity(room)));
    }

    @Override
    public Optional<Room> findById(String id) {
        return repository.findById(new ObjectId(id)).map(roomEntityMapper::toRoom);
    }

    @Override
    public List<Room> findAll() {
        return repository.findAll().stream().map(roomEntityMapper::toRoom).toList();
    }

    @Override
    public void remove(String room) {
        repository.remove(new ObjectId(room));
    }

    @Override
    public Room updateRoom(String id, Room obj) {
        return roomEntityMapper.toRoom(repository.update(new ObjectId(id), roomEntityMapper.toEntity(obj)));
    }
}
