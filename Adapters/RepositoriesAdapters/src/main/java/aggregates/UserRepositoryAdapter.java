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
import mappers.UserMapper;
import org.bson.types.ObjectId;
import client.repo.UserRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class UserRepositoryAdapter implements AddUsersPort, FindUsersPort, RemoveUserPort, UpdateUsersPort {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Client addClient(Client client) {
        return (Client) userMapper.EntityToUser(userRepository.add(userMapper.toClientEntity(client)));
    }

    @Override
    public Admin addAdmin(Admin admin) {
        return (Admin) userMapper.EntityToUser(userRepository.add(userMapper.toAdminEntity(admin)));
    }

    @Override
    public Moderator addModerator(Moderator moderator) {
        return (Moderator) userMapper.EntityToUser(userRepository.add(userMapper.toModeratorEntity(moderator)));
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(new ObjectId(id)).map(userMapper::EntityToUser);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::EntityToUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().map(userMapper::EntityToUser).toList();
    }

    @Override
    public void remove(String id) {
        userRepository.remove(new ObjectId(id));
    }

    @Override
    public User update(String id, Client obj) {
        return userMapper.EntityToUser(userRepository.update(new ObjectId(id), userMapper.toClientEntity(obj)));
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
