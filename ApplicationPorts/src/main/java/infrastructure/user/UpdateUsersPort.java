package infrastructure.user;
import domain.Client;
import domain.User;

public interface UpdateUsersPort {

    public User update(String id, Client obj);

    public void activateAccount(String id);

    public void deactivateAccount(String id);
}
