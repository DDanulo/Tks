package aggregates;

import control.user.AddUserUseCase;
import control.user.GetUserUseCase;
import control.user.UpdateUserUseCase;
import domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSoapControllerAdapter {
    private final AddUserUseCase addUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
//    private final UserSoapMapper userMapper;

//
//    public List<ShowUserDTO> getAllUsers() {
//        return getUserUseCase.findAll().stream().map(userMapper::UserToDto).toList();
//    }
//
//    public ShowClientDTO registerClient(@Valid CreateClientDTO userDTO) {
//        return (ShowClientDTO) userMapper.UserToDto(addUserUseCase.registerClient(userMapper.toClient(userDTO)));
//    }
//
//    public ShowAdminDTO registerAdmin(@Valid CreateAdminDTO userDTO) {
//        return (ShowAdminDTO) userMapper.UserToDto(addUserUseCase.registerAdmin(userMapper.toAdmin(userDTO)));
//    }
//
//    public ShowModeratorDTO registerModerator(@Valid CreateModeratorDTO userDTO) {
//        return (ShowModeratorDTO) userMapper.UserToDto(addUserUseCase.registerModerator(userMapper.toModerator(userDTO)));
//    }
//
//    public ShowUserDTO findUserById(String id) {
//        return userMapper.UserToDto(getUserUseCase.findById(id));
//    }
//
//    public ShowUserDTO findUserByLogin(String login) {
//        return userMapper.UserToDto(getUserUseCase.findByLogin(login));
//    }

    public User findUserByLoginNoConvert(String login) {
        return getUserUseCase.findByLogin(login);
    }
//
//    public ShowClientDTO updateClient(String id, @Valid UpdateUserDTO userDTO) {
//        return (ShowClientDTO) userMapper.UserToDto(updateUserUseCase.updateUser(id, userMapper.updateUserFromDto(userDTO)));
//    }

    public void activateClient(String id) {
        updateUserUseCase.activateUser(id);
    }

    public void deactivateClient(String id) {
        updateUserUseCase.deactivateUser(id);
    }

//    public void changePassword(String id, @Valid ChangePasswordDTO dto) {
//        updateUserUseCase.changePassword(id, userMapper.updateUserFromChangePassword(dto));
//    }

//    public Optional<ShowUserDTO> getClientByLogin(String login) {
//        User user = getUserUseCase.findByLogin(login);
//        if (user != null) {
//            return Optional.of(userMapper.UserToDto(user));
//        }
//        return Optional.empty();
//    }
}
