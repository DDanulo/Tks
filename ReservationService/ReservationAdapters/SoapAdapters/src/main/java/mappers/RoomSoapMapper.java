package mappers;

import com.rentafield.tks.soap.rooms.AddRoomRequest;
import com.rentafield.tks.soap.rooms.RoomSOAP;
import com.rentafield.tks.soap.rooms.UpdateRoomRequest;
import domain.Room;
import domain.RoomType;
import org.springframework.stereotype.Component;
@Component
public class RoomSoapMapper {

    public RoomSOAP domainToSoap(Room domainRoom) {
        if (domainRoom == null) {
            return null;
        }

        RoomSOAP soapRoom = new RoomSOAP();
        if (domainRoom.getRoomId() != null) {
            soapRoom.setId(domainRoom.getRoomId().toString());
        }
        if (domainRoom.getRoomType() != null) {
            soapRoom.setRoomType(domainRoom.getRoomType().name());
        }
        if (domainRoom.getCapacity() != null) {
            soapRoom.setCapacity(domainRoom.getCapacity());
        }
        if (domainRoom.getBasePrice() != null) {
            soapRoom.setBasePrice(domainRoom.getBasePrice());
        }

        return soapRoom;
    }

    public Room addRequestToDomain(AddRoomRequest request) {
        if (request == null) {
            return null;
        }

        return Room.builder()
                .roomType(RoomType.valueOf(request.getRoomType().toUpperCase()))
                .capacity(request.getCapacity())
                .basePrice(request.getBasePrice())
                .build();
    }

    public Room updateRequestToDomain(UpdateRoomRequest request) {
        if (request == null) {
            return null;
        }

        return Room.builder()
                .roomType(RoomType.valueOf(request.getRoomType().toUpperCase()))
                .capacity(request.getCapacity())
                .basePrice(request.getBasePrice())
                .build();
    }
}