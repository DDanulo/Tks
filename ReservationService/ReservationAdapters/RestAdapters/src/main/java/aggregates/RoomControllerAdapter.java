package aggregates;

import control.room.AddRoomUseCase;
import control.room.GetRoomUseCase;
import control.room.RemoveRoomUseCase;
import control.room.UpdateRoomUseCase;
import lombok.RequiredArgsConstructor;
import mappers.RoomMapper;
import model.CreateRoomDTO;
import model.ShowRoomDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomControllerAdapter {

    private final RoomMapper roomMapper;
    private final AddRoomUseCase addRoomUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final RemoveRoomUseCase removeRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;

    public ShowRoomDTO createRoom(CreateRoomDTO room) {
        return roomMapper.roomToShowRoomDTO(addRoomUseCase.add(roomMapper.createRoomDTOToRoom(room)));
    }

    public Optional<ShowRoomDTO> getOneRoomById(String id){
        return getRoomUseCase.findOneRoomById(id).map(roomMapper::roomToShowRoomDTO);
    }

    public List<ShowRoomDTO> getAllRooms(){
        return getRoomUseCase.GetAllRooms().stream().map(roomMapper::roomToShowRoomDTO).toList();
    }

    public void removeRoom(String id){
        removeRoomUseCase.remove(id);
    }

    public ShowRoomDTO updateRoom(String id, CreateRoomDTO update){
        return roomMapper.roomToShowRoomDTO(updateRoomUseCase.update(id, roomMapper.createRoomDTOToRoom(update)));
    }
}
