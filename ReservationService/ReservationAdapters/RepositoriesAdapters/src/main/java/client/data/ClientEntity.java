package client.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(key = "clazz", value = "client")
public class ClientEntity {
    @NotNull
    @BsonId
    private ObjectId userId;

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
}