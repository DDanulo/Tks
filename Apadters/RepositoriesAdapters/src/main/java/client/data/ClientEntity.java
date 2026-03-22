package client.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(key = "clazz", value = "client")
public class ClientEntity extends UserEntity {
    public ClientEntity(String login, String password, String firstName, String lastName, String email) {
        super(null, login, password, firstName, lastName, email, true, domain.Role.CLIENT);
    }

    public ClientEntity(ObjectId userId, String login, String password, String firstName, String lastName, String email, Boolean isActive) {
        super(userId, login, password, firstName, lastName, email, isActive, domain.Role.CLIENT);
    }
}