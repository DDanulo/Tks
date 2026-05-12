package control.user;

import domain.User;

import java.util.List;

public interface GetUserUseCase {
    User findByLogin(String login);
    User findById(String id);
    List<User> findAll();
}
