package client.repo;

import client.data.UserEntity;
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
public class UserRepository extends AbstractMongoRepository {

    private final MongoCollection<UserEntity> users;

    public UserRepository(MongoClient mongoClient, MongoDatabase rentAFieldDB) {
        super(mongoClient, rentAFieldDB);
        users = getRentAFieldDB().getCollection("users", UserEntity.class);
    }

//    @Override
    public void add(UserEntity obj) {
        users.insertOne(obj);
    }

//    @Override
    public void remove(ObjectId id) {
        users.deleteOne(Filters.eq("_id", id));
    }

//    @Override
    public Optional<UserEntity> findById(ObjectId id) {
        return Optional.ofNullable(users.find(Filters.eq("_id", id)).first());
    }

    public Optional<UserEntity> findByLogin(String login) {
        return Optional.ofNullable(users.find(Filters.eq("login", login)).first());
    }

    public List<UserEntity> searchByLogin(String login) {
        Bson filter = Filters.regex("login", ".*" + login + ".*", "i");
        return users.find(filter).into(new ArrayList<>());
    }

//    @Override
    public List<UserEntity> findAll() {
        return users.find().into(new ArrayList<>());
    }

//    @Override
    public void update(ObjectId id, UserEntity obj) {
        List<Bson> updated = new ArrayList<>();

        updated.add(Updates.set("login", obj.getLogin()));
        updated.add(Updates.set("first_name", obj.getFirstName()));
        updated.add(Updates.set("last_name", obj.getLastName()));
        updated.add(Updates.set("email", obj.getEmail()));
        updated.add(Updates.set("role", obj.getRole()));
        updated.add(Updates.set("active", obj.getIsActive()));

        if (obj.getPassword() != null) {
            updated.add(Updates.set("password", obj.getPassword()));
        }

        users.updateOne(Filters.eq("_id", id), Updates.combine(updated));
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