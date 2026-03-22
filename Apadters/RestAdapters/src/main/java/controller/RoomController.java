package controller;

import aggregates.RoomControllerAdapter;
import control.room.AddRoomUseCase;
import exception.NotFoundException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.CreateRoomDTO;
import model.ShowRoomDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    public static final String BASE_URL = "/api/v1/rooms";
    public static final String BASE_ID_URL = "/api/v1/rooms/{id}";

//    private final RoomService roomService;

    private final RoomControllerAdapter roomControllerAdapter;

    @PostMapping(BASE_URL)
    @RolesAllowed("ADMIN")
    public ShowRoomDTO createRoom(@RequestBody @Valid CreateRoomDTO roomDTO){
        return roomControllerAdapter.createRoom(roomDTO);
    }

    @GetMapping(BASE_ID_URL)
    public ShowRoomDTO getRoomById(@PathVariable("id") String id){
        return (roomControllerAdapter.getOneRoomById(id).orElseThrow(NotFoundException::new));
    }

    @GetMapping(BASE_URL)
    public List<ShowRoomDTO> getAllRooms(){
        return roomControllerAdapter.getAllRooms();
    }

    @PutMapping(BASE_ID_URL)
    @RolesAllowed("ADMIN")
    public ShowRoomDTO updateRoom(@PathVariable String id,
                           @RequestBody @Valid CreateRoomDTO roomDTO){
        return roomControllerAdapter.updateRoom(id, roomDTO);
    }

    @DeleteMapping(BASE_ID_URL)
    @RolesAllowed("ADMIN")
    public void deleteRoom(@PathVariable String id){
        roomControllerAdapter.removeRoom(id);
    }

}
