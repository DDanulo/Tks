package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Client extends User {
    public Client(String login, String password, String firstName, String lastName, String email) {
        super(null, login, password, firstName, lastName, email, true, Role.CLIENT);
    }

    public Client(String userId, String login, String password, String firstName, String lastName, String email, Boolean isActive) {
        super(userId, login, password, firstName, lastName, email, isActive, Role.CLIENT);
    }
}