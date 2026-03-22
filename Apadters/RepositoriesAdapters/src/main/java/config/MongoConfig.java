package config;

import adapterModel.*;
import client.data.AdminEntity;
import client.data.ClientEntity;
import client.data.ModeratorEntity;
import client.data.UserEntity;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@Configuration
public class MongoConfig {

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        return fromProviders(
                PojoCodecProvider.builder()
                        .automatic(true)
                        .register(UserEntity.class, AdminEntity.class, ModeratorEntity.class, ClientEntity.class,
                                ReservationEntity.class, RoomEntity.class)
//                        .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                        .build()
        );
    }

    @Bean
    public MongoClient mongoClient(CodecRegistry pojoCodecRegistry) {
        ConnectionString connectionString = new ConnectionString(
                "mongodb://mongodb1:27017/?replicaSet=replica_set_single&authSource=admin");

        MongoCredential credential = MongoCredential.createCredential(
                "admin", "admin", "adminpassword".toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabase rentAFieldDB(MongoClient mongoClient) {
        return mongoClient.getDatabase("rentafield");
    }

    @Bean
    public CommandLineRunner ensureIndexesAndDrop() {
        return args -> {
            MongoCollection<UserEntity> users = rentAFieldDB(
                    mongoClient(pojoCodecRegistry())).getCollection("users", UserEntity.class
            );
            users.drop();
            UserEntity admin = new AdminEntity();
            admin.setLogin("admin");
            admin.setPassword("$2a$12$ZZ3Ug4gOcDYeIJN7P12EJenkl1fe30JMg3jtqK5hBoEJvEg1e9vlC");
            admin.setEmail("admin@admin.com");
            admin.setRole(domain.Role.ADMIN);
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setIsActive(true);
            users.insertOne(admin);
            MongoCollection<ReservationEntity> reservations = rentAFieldDB(
                    mongoClient(pojoCodecRegistry())).getCollection("reservations", ReservationEntity.class
            );
            reservations.drop();
            MongoCollection<RoomEntity> rooms = rentAFieldDB(
                    mongoClient(pojoCodecRegistry())).getCollection("rooms", RoomEntity.class
            );
//        rooms.drop();
            users.createIndex(
                    Indexes.ascending("login"),
                    new IndexOptions().unique(true).name("uk_users_login")
            );
        };
    }
}