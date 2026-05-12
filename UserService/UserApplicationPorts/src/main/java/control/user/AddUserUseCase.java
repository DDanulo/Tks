package control.user;

import domain.Admin;
import domain.Client;
import domain.Moderator;

public interface AddUserUseCase {
    Client registerClient(Client client);
    Admin registerAdmin(Admin admin);
    Moderator registerModerator(Moderator moderator);

}
