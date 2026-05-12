package control.user;

import domain.User;

public interface UpdateUserUseCase {
    User updateUser(String id, User user);
    void activateUser(String id);
    void deactivateUser(String id);
    void changePassword(String id, User user);
}
