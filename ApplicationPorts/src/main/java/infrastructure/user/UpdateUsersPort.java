package infrastructure.user;
import domain.Client;
import domain.User;
import org.bson.types.ObjectId;

public interface UpdateUsersPort {

    public void update(String id, Client obj);

    public void activateAccount(String id);

    public void deactivateAccount(String id);
}
