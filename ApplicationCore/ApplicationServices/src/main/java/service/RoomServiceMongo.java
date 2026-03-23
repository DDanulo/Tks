package service;


import control.room.AddRoomUseCase;
import control.room.GetRoomUseCase;
import control.room.RemoveRoomUseCase;
import control.room.UpdateRoomUseCase;
import domain.Room;
import infrastructure.room.AddRoomPort;
import infrastructure.room.FindRoomPort;
import infrastructure.room.RemoveRoomPort;
import infrastructure.room.UpdateRoomPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomServiceMongo implements AddRoomUseCase, UpdateRoomUseCase, RemoveRoomUseCase, GetRoomUseCase {

    private final FindRoomPort findRoomPort;
    private final AddRoomPort addRoomPort;
    private final UpdateRoomPort updateRoomPort;
    private final RemoveRoomPort removeRoomPort;

    @Override
    public Room add(Room room) {
        if (room == null) {
            throw new IllegalArgumentException();
        }
        return addRoomPort.add(room);

    }

    @Override
    public Optional<Room> findOneRoomById(String Id) {
        return findRoomPort.findById(Id);
    }

    @Override
    public List<Room> GetAllRooms() {
        return findRoomPort.findAll();
    }

    @Override
    public void remove(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Wrong room id");
        }

        if (findOneRoomById(id).isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }
        removeRoomPort.remove(id);
    }

    @Override
    public Room update(String id, Room obj) {
        if (findOneRoomById(id).isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }
        return updateRoomPort.updateRoom(id, obj);
    }
}
