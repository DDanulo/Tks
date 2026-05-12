package domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
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

}
