package infrastructure.user;

import domain.User;

import java.util.List;
import java.util.Optional;

public interface FindUsersPort {
    public Optional<User> findById(String id);

    public Optional<User> findByLogin(String login);

    public List<User> findAll();
}
