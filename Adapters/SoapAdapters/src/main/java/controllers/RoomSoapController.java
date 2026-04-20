package controllers;

import com.rentafield.tks.soap.rooms.*;
import control.room.AddRoomUseCase;
import control.room.GetRoomUseCase;
import control.room.RemoveRoomUseCase;
import control.room.UpdateRoomUseCase;
import domain.Room;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mappers.RoomSoapMapper;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class RoomSoapController {
    private static final String NAMESPACE_URI = "http://rentafield.com/tks/soap/rooms";

    private final GetRoomUseCase getRoomUseCase;
    private final AddRoomUseCase addRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;
    private final RemoveRoomUseCase removeRoomUseCase;

    private final RoomSoapMapper roomMapper;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRoomRequest")
    @ResponsePayload
    @RolesAllowed({"ADMIN", "CLIENT", "MODERATOR"})
    public GetRoomResponse getRoom(@RequestPayload GetRoomRequest request) {
        GetRoomResponse response = new GetRoomResponse();

        Room domainRoom = getRoomUseCase.findOneRoomById(request.getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        response.setRoom(roomMapper.domainToSoap(domainRoom));
        return response;
    }

    @RolesAllowed("ADMIN")
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addRoomRequest")
    @ResponsePayload
    public AddRoomResponse addRoom(@RequestPayload AddRoomRequest request) {

        Room roomToCreate = roomMapper.addRequestToDomain(request);

        Room createdRoom = addRoomUseCase.add(roomToCreate);

        AddRoomResponse response = new AddRoomResponse();
        response.setRoom(roomMapper.domainToSoap(createdRoom));
        return response;
    }

    @RolesAllowed("ADMIN")
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateRoomRequest")
    @ResponsePayload
    public UpdateRoomResponse updateRoom(@RequestPayload UpdateRoomRequest request) {

        Room roomToUpdate = roomMapper.updateRequestToDomain(request);

        Room updatedRoom = updateRoomUseCase.update(request.getId(), roomToUpdate);
        UpdateRoomResponse response = new UpdateRoomResponse();
        response.setRoom(roomMapper.domainToSoap(updatedRoom));
        return response;
    }

    @RolesAllowed("ADMIN")
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeRoomRequest")
    @ResponsePayload
    public RemoveRoomResponse removeRoom(@RequestPayload RemoveRoomRequest request) {
        removeRoomUseCase.remove(request.getId());
        RemoveRoomResponse response = new RemoveRoomResponse();
        response.setStatus(true);
        return response;
    }

}
