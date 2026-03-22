package domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public abstract class User {
    private String userId;

    @NotBlank
    @Size(min = 5, max = 20)
    private String login;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 30)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String email;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Role role;

//    public User(String login, String firstName, String lastName, String email, String password) {
//        this.login = login;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.password = password;
//    }
}
