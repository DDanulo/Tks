package infrastructure.user;

import domain.Admin;
import domain.Client;
import domain.Moderator;

public interface AddUsersPort {
    Client addClient(Client client);
    Admin addAdmin(Admin admin);
    Moderator addModerator(Moderator moderator);
}
