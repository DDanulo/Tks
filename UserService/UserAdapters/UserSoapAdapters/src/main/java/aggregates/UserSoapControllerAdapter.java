package aggregates;

import control.user.GetUserUseCase;
import domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSoapControllerAdapter {
    private final GetUserUseCase getUserUseCase;

    public User findUserByLoginNoConvert(String login) {
        return getUserUseCase.findByLogin(login);
    }


}
