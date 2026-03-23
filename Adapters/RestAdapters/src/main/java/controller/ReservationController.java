package controller;

import aggregates.ReservationControllerAdapter;
import exception.NotFoundException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.CreateReservationDTO;
import model.ShowReservationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {

    private final ReservationControllerAdapter reservationControllerAdapter;

    @PostMapping
    public void createReservation(@RequestBody @Valid CreateReservationDTO reservationDTO){
        reservationControllerAdapter.createReservation(reservationDTO);
    }

    @GetMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ShowReservationDTO getReservationById(@PathVariable String id) {
        ShowReservationDTO dto = reservationControllerAdapter.getOneReservationById(id)
                .orElseThrow(NotFoundException::new);

        return addHateoasLinks(dto);
    }

    @GetMapping
    public List<ShowReservationDTO> getAllReservations() {
        return reservationControllerAdapter.getAllReservations().stream()
                .map(this::addHateoasLinks)
                .toList();
    }

    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public void updateReservation(@PathVariable String id,
                             @RequestBody @Valid CreateReservationDTO reservationDTO){
        reservationControllerAdapter.updateReservation(id, reservationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String id){
        reservationControllerAdapter.removeReservationById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clients/{clientId}/reservations")
    @RolesAllowed("ADMIN")
    public List<ShowReservationDTO> getClientReservation(
            @PathVariable String clientId,
            @RequestParam(required = false, defaultValue = "current") String status) {

        return switch (status) {
            case "current" -> reservationControllerAdapter.findCurrentForClient(clientId);
            case "past"    -> reservationControllerAdapter.findPastForClient(clientId);
            default        -> throw new IllegalArgumentException("status must be current|past");
        };
    }

    @GetMapping("/rooms/{roomId}/reservations")
    @RolesAllowed("ADMIN")
    public List<ShowReservationDTO> getRoomReservation(
            @PathVariable String roomId,
            @RequestParam(required = false, defaultValue = "current") String status) {

        return switch (status) {
            case "current" -> reservationControllerAdapter.findCurrentForRoom(roomId);
            case "past"    -> reservationControllerAdapter.findPastForRoom(roomId);
            default        ->  reservationControllerAdapter.getAllReservations();
        };
    }

    @PostMapping("/{id}/end")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> endReservation(@PathVariable String id){
        reservationControllerAdapter.endReservation(id);
        return ResponseEntity.ok().build();
    }

    private ShowReservationDTO addHateoasLinks(ShowReservationDTO dto) {
        String id = dto.getReservationId();

        dto.removeLinks();

        dto.add(linkTo(methodOn(ReservationController.class).getReservationById(id)).withSelfRel());
        dto.add(linkTo(methodOn(ReservationController.class).endReservation(id)).withRel("cancel"));
        dto.add(linkTo(methodOn(ReservationController.class).deleteReservation(id)).withRel("delete"));

        dto.add(linkTo(methodOn(UserController.class).getClientById(dto.getClientId())).withRel("user"));
        dto.add(linkTo(methodOn(RoomController.class).getRoomById(dto.getRoomId())).withRel("resource"));

        return dto;
    }

}
