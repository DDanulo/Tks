package service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import control.user.AddUserUseCase;
import control.user.GetUserUseCase;
import control.user.UpdateUserUseCase;
import domain.*;
import exception.NotFoundException;
import exception.SameLoginException;
import infrastructure.user.AddUsersPort;
import infrastructure.user.FindUsersPort;
import infrastructure.user.RemoveUserPort;
import infrastructure.user.UpdateUsersPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceMongo implements
        AddUserUseCase,
        UpdateUserUseCase,
        GetUserUseCase {

    private final AddUsersPort addUserPort;
    private final FindUsersPort findUsersPort;
    private final UpdateUsersPort updateUsersPort;
    private final RemoveUserPort removeUserPort;


    @Override
    public User findByLogin(String login) {
        return findUsersPort.findByLogin(login).orElseThrow(NotFoundException::new);
    }

    @Override
    public User findById(String id) {
        return findUsersPort.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<User> findAll() {
        return findUsersPort.findAll();
    }

    @Override
    public Client registerClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        if (findUsersPort.findByLogin(client.getLogin()).isPresent()) {
            throw new SameLoginException("Login zajęty");
        }
        String hashedPassword = BCrypt.withDefaults().hashToString(12, client.getPassword().toCharArray());
        client.setPassword(hashedPassword);
        addUserPort.addClient(client);
        return client;
    }

    @Override
    public Admin registerAdmin(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        if (findUsersPort.findByLogin(admin.getLogin()).isPresent()) {
            throw new SameLoginException("Login zajęty");
        }
        String hashedPassword = BCrypt.withDefaults().hashToString(12, admin.getPassword().toCharArray());
        admin.setPassword(hashedPassword);
        addUserPort.addAdmin(admin);
        return admin;
    }

    @Override
    public Moderator registerModerator(Moderator moderator) {
        if (moderator == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        if (findUsersPort.findByLogin(moderator.getLogin()).isPresent()) {
            throw new SameLoginException("Login zajęty");
        }
        String hashedPassword = BCrypt.withDefaults().hashToString(12, moderator.getPassword().toCharArray());
        moderator.setPassword(hashedPassword);
        addUserPort.addModerator(moderator);
        return moderator;
    }

    @Override
    public User updateUser(String id, User user) {

        User userEdited = findUsersPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        if (user.getFirstName() != null) {
            userEdited.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userEdited.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            userEdited.setEmail(user.getEmail());
        }
        return updateUsersPort.update(id, (Client) user);
    }

    @Override
    public void activateUser(String id) {
        updateUsersPort.activateAccount(id);
    }

    @Override
    public void deactivateUser(String id) {
        updateUsersPort.deactivateAccount(id);
    }

    @Override
    public void changePassword(String id, User user) {
        updateUsersPort.update(id, (Client)user);
    }
}