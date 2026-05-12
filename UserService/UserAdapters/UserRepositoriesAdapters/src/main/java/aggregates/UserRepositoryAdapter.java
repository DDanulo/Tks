package aggregates;

import domain.Admin;
import domain.Client;
import domain.Moderator;
import domain.User;
import infrastructure.user.AddUsersPort;
import infrastructure.user.FindUsersPort;
import infrastructure.user.RemoveUserPort;
import infrastructure.user.UpdateUsersPort;
import lombok.RequiredArgsConstructor;
import mappers.UserEntityMapper;
import org.bson.types.ObjectId;
import client.repo.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements AddUsersPort, FindUsersPort, RemoveUserPort, UpdateUsersPort {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Client addClient(Client client) {
        return (Client) userEntityMapper.EntityToUser(userRepository.add(userEntityMapper.toClientEntity(client)));
    }

    @Override
    public Admin addAdmin(Admin admin) {
        return (Admin) userEntityMapper.EntityToUser(userRepository.add(userEntityMapper.toAdminEntity(admin)));
    }

    @Override
    public Moderator addModerator(Moderator moderator) {
        return (Moderator) userEntityMapper.EntityToUser(userRepository.add(userEntityMapper.toModeratorEntity(moderator)));
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(new ObjectId(id)).map(userEntityMapper::EntityToUser);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login).map(userEntityMapper::EntityToUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().map(userEntityMapper::EntityToUser).toList();
    }

    @Override
    public void remove(String id) {
        userRepository.remove(new ObjectId(id));
    }

    @Override
    public User update(String id, Client obj) {
        return userEntityMapper.EntityToUser(userRepository.update(new ObjectId(id), userEntityMapper.toClientEntity(obj)));
    }

    @Override
    public void activateAccount(String id) {
        userRepository.activateAccount(new ObjectId(id));
    }

    @Override
    public void deactivateAccount(String id) {
        userRepository.deactivateAccount(new ObjectId(id));
    }


}
