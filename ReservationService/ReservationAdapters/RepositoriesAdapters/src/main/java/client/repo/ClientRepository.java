package client.repo;

import client.data.ClientEntity;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import repository.AbstractMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository extends AbstractMongoRepository {

    private final MongoCollection<ClientEntity> users;

    public ClientRepository(MongoClient mongoClient, MongoDatabase rentAFieldDB) {
        super(mongoClient, rentAFieldDB);
        users = getRentAFieldDB().getCollection("users", ClientEntity.class);
    }

    //    @Override
    public ClientEntity add(ClientEntity obj) {
        ObjectId id = users.insertOne(obj).getInsertedId().asObjectId().getValue();
        return users.find(Filters.eq("_id", id)).first();
    }

    //    @Override
    public void remove(ObjectId id) {
        users.deleteOne(Filters.eq("_id", id));
    }

    //    @Override
    public Optional<ClientEntity> findById(ObjectId id) {
        return Optional.ofNullable(users.find(Filters.eq("_id", id)).first());
    }

    public Optional<ClientEntity> findByLogin(String login) {
        return Optional.ofNullable(users.find(Filters.eq("login", login)).first());
    }

    public List<ClientEntity> searchByLogin(String login) {
        Bson filter = Filters.regex("login", ".*" + login + ".*", "i");
        return users.find(filter).into(new ArrayList<>());
    }

    //    @Override
    public List<ClientEntity> findAll() {
        return users.find().into(new ArrayList<>());
    }

    //    @Override
    public ClientEntity update(ObjectId id, ClientEntity obj) {
        List<Bson> updated = new ArrayList<>();

        if (obj.getFirstName() != null) {
            updated.add(Updates.set("first_name", obj.getFirstName()));
        }
        if (obj.getLastName() != null) {
            updated.add(Updates.set("last_name", obj.getLastName()));
        }
        if (obj.getEmail() != null){
            updated.add(Updates.set("email", obj.getEmail()));
        }

        users.updateOne(Filters.eq("_id", id), Updates.combine(updated));
        return obj;
    }

    public void activateAccount(ObjectId id) {
        users.updateOne(
                Filters.eq("_id", id),
                Updates.set("active", true));
    }

    public void deactivateAccount(ObjectId id) {
        users.updateOne(
                Filters.eq("_id", id),
                Updates.set("active", false));
    }
}