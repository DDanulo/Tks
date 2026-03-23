package mappers;

import client.data.AdminEntity;
import client.data.ClientEntity;
import client.data.ModeratorEntity;
import domain.Role;
import client.data.UserEntity;
import domain.Admin;
import domain.Client;
import domain.Moderator;
import domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User EntityToUser(UserEntity user) {
        if (user == null) return null;

        return switch (user) {
            case ClientEntity c -> Client.builder()
                    .userId(c.getUserId() != null ? c.getUserId().toString() : null)
                    .login(c.getLogin())
                    .password(c.getPassword())
                    .email(c.getEmail())
                    .firstName(c.getFirstName())
                    .lastName(c.getLastName())
                    .isActive(c.getIsActive())
                    .role(c.getRole())
                    .build();

            case ModeratorEntity m -> Moderator.builder()
                    .userId(m.getUserId() != null ? m.getUserId().toString() : null)
                    .login(m.getLogin())
                    .password(m.getPassword())
                    .email(m.getEmail())
                    .firstName(m.getFirstName())
                    .lastName(m.getLastName())
                    .isActive(m.getIsActive())
                    .role(m.getRole())
                    .build();

            case AdminEntity a -> Admin.builder()
                    .userId(a.getUserId() != null ? a.getUserId().toString() : null)
                    .login(a.getLogin())
                    .password(a.getPassword())
                    .email(a.getEmail())
                    .firstName(a.getFirstName())
                    .lastName(a.getLastName())
                    .isActive(a.getIsActive())
                    .role(a.getRole())
                    .build();

            default -> throw new IllegalArgumentException();
        };
    }

    public ClientEntity toClientEntity(Client dto) {
        ClientEntity client = new ClientEntity();
        client.setLogin(dto.getLogin());
        client.setPassword(dto.getPassword());
        client.setEmail(dto.getEmail());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setRole(Role.CLIENT);
        client.setIsActive(dto.getIsActive());
        return client;
    }

    public ModeratorEntity toModeratorEntity(Moderator dto) {
        ModeratorEntity moderator = new ModeratorEntity();
        moderator.setLogin(dto.getLogin());
        moderator.setPassword(dto.getPassword());
        moderator.setEmail(dto.getEmail());
        moderator.setFirstName(dto.getFirstName());
        moderator.setLastName(dto.getLastName());
        moderator.setRole(Role.MODERATOR);
        moderator.setIsActive(dto.getIsActive());
        return moderator;
    }

    public AdminEntity toAdminEntity(Admin dto) {
        AdminEntity admin = new AdminEntity();
        admin.setLogin(dto.getLogin());
        admin.setPassword(dto.getPassword());
        admin.setEmail(dto.getEmail());
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setRole(Role.ADMIN);
        admin.setIsActive(dto.getIsActive());
        return admin;
    }
}