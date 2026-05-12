package controller;


import aggregates.UserControllerAdapter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.ChangePasswordDTO;
import model.users.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserControllerAdapter userControllerAdapter;
    private final JwtService jwtService;
    @RolesAllowed("ADMIN")
    @GetMapping
    public List<ShowUserDTO> getAllClients() {
        return userControllerAdapter.getAllUsers();
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/client")
    public void createClient(@RequestBody @Valid CreateClientDTO userDTO) {
        userControllerAdapter.registerClient(userDTO);
    }

    @PostMapping("/admin")
    @RolesAllowed("ADMIN")
    public void createAdmin(@RequestBody @Valid CreateAdminDTO userDTO) {
        userControllerAdapter.registerAdmin(userDTO);
    }

    @PostMapping("/moderator")
    @RolesAllowed("ADMIN")
    public void createModerator(@RequestBody @Valid CreateModeratorDTO userDTO) {
        userControllerAdapter.registerModerator(userDTO);
    }

    @GetMapping("/id/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ShowUserDTO> getClientById(@PathVariable String id) {
        ShowUserDTO userDTO = userControllerAdapter.findUserById(id);
        String signature = jwtService.signData(userDTO.getLogin());
        return ResponseEntity.ok().eTag(signature).body(userDTO);
    }

    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> updateClient(@PathVariable String id,
                             @RequestHeader(value = "If-Match") String signature,
                             @RequestBody @Valid UpdateUserDTO userDTO) {
        String cleanSignature = signature.replace("\"", "");
//        if (!jwtService.verifySignature(userDTO.getLogin(), cleanSignature)){
//            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
//        }
        userControllerAdapter.updateClient(id, userDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-login/{login}")
    @RolesAllowed("ADMIN")
    public ShowUserDTO getClientByLogin(@PathVariable String login) {
        return userControllerAdapter.findUserByLogin(login);
    }

//    @GetMapping("/search")
//    @RolesAllowed("ADMIN")
//    public List<ShowUserDTO> findClientsByLogin(@RequestParam String login) {
//        return userControllerAdapter.findUserByLogin(login);
//    }

    @PostMapping("/{id}/activate")
    @RolesAllowed("ADMIN")
    public void activateClient(@PathVariable String id) {
        userControllerAdapter.activateClient(id);
    }

    @PostMapping("/{id}/deactivate")
    @RolesAllowed("ADMIN")
    public void deactivateClient(@PathVariable String id) {
        userControllerAdapter.deactivateClient(id);
    }

    @PatchMapping("/{id}/password")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> changePassword(@PathVariable String id,
                                            @RequestBody @Valid ChangePasswordDTO dto) {
        try {
            userControllerAdapter.changePassword(id, dto);
            return ResponseEntity.status(200).body("Zmieniono hasło");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Wystąpił nieoczekiwany błąd");
        }
    }
}