package infrastructure.user;

import domain.Admin;
import domain.Client;
import domain.Moderator;

public interface AddUsersPort {
    void addClient(Client client);
    void addAdmin(Admin admin);
    void addModerator(Moderator moderator);
}
