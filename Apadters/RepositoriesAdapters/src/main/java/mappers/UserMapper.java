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

        User base = User.builder()
                .userId(user.getUserId().toString())
                .login(user.getLogin())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();

        switch (user) {
            case ClientEntity client -> {
                Client entity = new Client();
                copyBase(base, entity);
                return entity;
            }
            case ModeratorEntity moderator -> {
                Moderator dto = new Moderator();
                copyBase(base, dto);
                return dto;
            }
            case AdminEntity admin -> {
                Admin dto = new Admin();
                copyBase(base, dto);
                return dto;
            }
            default -> {
                return base;
            }
        }
    }


    private void copyBase(User base, User target) {
        target.setLogin(base.getLogin());
        target.setUserId(base.getUserId());
        target.setEmail(base.getEmail());
        target.setFirstName(base.getFirstName());
        target.setLastName(base.getLastName());
        target.setIsActive(base.getIsActive());
        target.setRole(base.getRole());
    }


    public ClientEntity toClientEntity(Client dto) {
        ClientEntity client = new ClientEntity();
        client.setLogin(dto.getLogin());
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
        admin.setEmail(dto.getEmail());
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setRole(Role.ADMIN);
        admin.setIsActive(dto.getIsActive());
        return admin;
    }
}