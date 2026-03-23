package client.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@BsonDiscriminator(key = "clazz", value = "user")
@SuperBuilder
public abstract class UserEntity {
    @NotNull
    @BsonId
    private ObjectId userId;

    @NotBlank
    @Size(min = 5, max = 20)
    @BsonProperty("login")
    private String login;

    @NotBlank
    @Size(min = 8)
    @BsonProperty("password")
    private String password;

    @NotBlank
    @Size(min = 3, max = 30)
    @BsonProperty("first_name")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 30)
    @BsonProperty("last_name")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 50)
    @BsonProperty("email")
    private String email;

    @NotNull
    @BsonProperty("active")
    private Boolean isActive;

    @NotNull
    @BsonProperty("role")
    private domain.Role role;

}
