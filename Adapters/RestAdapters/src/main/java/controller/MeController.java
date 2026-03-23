package controller;

import aggregates.ReservationControllerAdapter;
import aggregates.UserControllerAdapter;
import exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.ChangePasswordDTO;
import model.ShowReservationDTO;
import model.users.ShowUserDTO;
import model.users.UpdateUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.JwtService;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MeController {

    private final UserControllerAdapter userControllerAdapter;
    private final ReservationControllerAdapter reservationControllerAdapter;
    private final JwtService jwtService;

    @GetMapping()
    public ResponseEntity<ShowUserDTO> getMe(@AuthenticationPrincipal String username) {
        ShowUserDTO userDTO = userControllerAdapter.findUserByLogin(username);
        String signature = jwtService.signData(userDTO.getLogin());
        return ResponseEntity.ok().eTag(signature).body(userDTO);
    }

    @PutMapping()
    public ResponseEntity<Void> updateClient(@AuthenticationPrincipal String username,
                                               @RequestHeader(value = "If-Match") String signature,
                                               @RequestBody @Valid UpdateUserDTO userDTO) {
        String cleanSignature = signature.replace("\"", "");
        if (!jwtService.verifySignature(userDTO.getLogin(), cleanSignature)){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        ShowUserDTO foundUserDto = userControllerAdapter.findUserByLogin(username);
        userControllerAdapter.updateClient(foundUserDto.getId(), userDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations")
    public List<ShowReservationDTO> getMyAllReservations(@AuthenticationPrincipal String username) {
        ShowUserDTO foundUserDTO = userControllerAdapter.findUserByLogin(username);
        return reservationControllerAdapter.findAllForClient(foundUserDTO.getId()).stream().map(this::addHateoasLinks).toList();
    }

    @GetMapping("/reservations/ended")
    public List<ShowReservationDTO> getMyEndedReservations(@AuthenticationPrincipal String username) {
        AtomicReference<ShowUserDTO> user = new AtomicReference<>();
        userControllerAdapter.getClientByLogin(username).ifPresentOrElse(
                user::set,
                NotFoundException::new
        );
        return reservationControllerAdapter.findPastForClient(user.get().getId()).stream().map(this::addHateoasLinks).toList();
    }

    @GetMapping("/reservations/active")
    public List<ShowReservationDTO> getMyActiveReservations(@AuthenticationPrincipal String username) {
        AtomicReference<ShowUserDTO> user = new AtomicReference<>();
        userControllerAdapter.getClientByLogin(username).ifPresentOrElse(
                user::set,
                NotFoundException::new
        );
        return reservationControllerAdapter.findCurrentForClient(user.get().getId()).stream().map(this::addHateoasLinks).toList();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changeMyPassword(@AuthenticationPrincipal String username,
                                              @RequestBody @Valid ChangePasswordDTO dto) {
        try {
            userControllerAdapter.getClientByLogin(username).ifPresentOrElse(
                    (u) -> userControllerAdapter.changePassword(u.getId(), dto),
                    NotFoundException::new
            );
            return ResponseEntity.status(200).body("Zmieniono hasło");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Wystąpił nieoczekiwany błąd");
        }
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
