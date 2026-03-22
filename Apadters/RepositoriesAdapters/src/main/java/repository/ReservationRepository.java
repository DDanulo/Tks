package repository;

import adapterModel.ReservationEntity;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository extends AbstractMongoRepository {
    private final MongoCollection<ReservationEntity> ReservationEntitys;

    public ReservationRepository(MongoClient mongoClient, MongoDatabase rentAFieldDB) {
        super(mongoClient, rentAFieldDB);
        ReservationEntitys = getRentAFieldDB().getCollection("ReservationEntitys", ReservationEntity.class);
    }

    public void add(ReservationEntity obj) {
        ReservationEntitys.insertOne(obj);
    }

    public void remove(ObjectId obj) {
        Bson filter = Filters.eq("_id", obj);
        ReservationEntitys.deleteOne(filter);
    }

    public Optional<ReservationEntity> findById(ObjectId id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(ReservationEntitys.find(filter).first());
    }

    public List<ReservationEntity> findAll() {
        return ReservationEntitys.find().into(new ArrayList<>());
    }

    public void update(ObjectId id, ReservationEntity obj) {

        Bson updateRoom = Updates.set("room", obj.getRoomEntity());
        Bson updateUser = Updates.set("client", obj.getClientEntity());
        Bson updateStartTime = Updates.set("start_time", obj.getStartTime());
        Bson updateEndTime = Updates.set("end_time", obj.getEndTime());
        Bson updatePrice = Updates.set("price", obj.getPrice());

        ReservationEntitys.updateOne(Filters.eq("_id", id), Updates.combine(updateUser, updateRoom,
                updateStartTime, updateEndTime, updatePrice));
    }

    public List<ReservationEntity> findByClient(ObjectId clientId) {
        Bson filter = Filters.eq("client._id", clientId);
        return ReservationEntitys.find(filter).into(new ArrayList<>());
    }

    public List<ReservationEntity> findByRoom(ObjectId roomId) {
        Bson filter = Filters.eq("room._id", roomId);
        return ReservationEntitys.find(filter).into(new ArrayList<>());
    }
}
