package domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Moderator extends User {
    public Moderator(String userId, String login, String password, String firstName, String lastName, String email, Boolean isActive) {
        super(userId, login, password, firstName, lastName, email, isActive, Role.MODERATOR);
    }
}
