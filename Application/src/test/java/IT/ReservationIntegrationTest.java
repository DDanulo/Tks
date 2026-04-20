package IT;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import model.CreateReservationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = app.Main.class)
public class ReservationIntegrationTest {
    @LocalServerPort
    private int port;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.1");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setup() {
        RestAssured.reset();
        RestAssured.port = port;

        // Tell REST Assured how to serialize Java 8 LocalDateTime
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(
                        (cls, charset) -> {
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new JavaTimeModule());
                            // Forces dates to be formatted as ISO Strings (e.g., "2026-04-22T10:00:00")
                            // instead of weird arrays of numbers
                            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                            return mapper;
                        }
                )
        );
    }


    private String getAdminToken() {
        Map<String, String> loginDTO = new HashMap<>();
        loginDTO.put("login", "admin");
        loginDTO.put("password", "12345678");

        return given().contentType(ContentType.JSON).body(loginDTO)
                .post("/api/v1/auth/login")
                .then().statusCode(200).extract().path("accessToken");
    }

    private String createRoomAndGetId(String token) {
        Map<String, Object> roomDTO = new HashMap<>();
        roomDTO.put("roomType", "GYM");
        roomDTO.put("capacity", 20);
        roomDTO.put("basePrice", 100.0);

        return given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON).body(roomDTO)
                .post("/api/v1/rooms")
                .then().statusCode(200).extract().path("id");
    }

    private String createClientAndGetId(String token) {
        String uniqueLogin = "res_client_" + UUID.randomUUID().toString().substring(0, 5);
        Map<String, Object> newClient = new HashMap<>();
        newClient.put("login", uniqueLogin);
        newClient.put("password", "Password123!");
        newClient.put("firstName", "John");
        newClient.put("lastName", "Doe");
        newClient.put("email", uniqueLogin + "@example.com");
        newClient.put("isActive", true);

        given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON).body(newClient)
                .post("/api/v1/users/client").then().statusCode(200);

        return given().header("Authorization", "Bearer " + token)
                .get("/api/v1/users/by-login/" + uniqueLogin)
                .then().statusCode(200).extract().path("id");
    }

    private void createReservation(String token, String clientId, String roomId, int plusDays) {
        CreateReservationDTO resDTO = new CreateReservationDTO();
        resDTO.setClientId(clientId);
        resDTO.setRoomId(roomId);
        resDTO.setStartTime(LocalDateTime.now().plusDays(plusDays));
        resDTO.setPrice(150.0);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(resDTO)
                .when()
                .post("/api/v1/reservations")
                .then()
                .statusCode(200);
    }

    private String getFirstReservationIdForClient(String token, String clientId) {
        return given().header("Authorization", "Bearer " + token)
                .get("/api/v1/reservations/clients/" + clientId + "/reservations?status=current")
                .then().statusCode(200)
                .extract().path("[0].reservationId");
    }



    @Test
    public void shouldCreateAndGetReservationWithHateoasLinks() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token);
        String clientId = createClientAndGetId(token);

        createReservation(token, clientId, roomId, 2);

        String reservationId = getFirstReservationIdForClient(token, clientId);
        assertNotNull(reservationId);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/reservations/" + reservationId)
                .then()
                .statusCode(200)
                .body("reservationId", equalTo(reservationId))
                .body("clientId", equalTo(clientId))
                .body("roomId", equalTo(roomId))
                .body("_links.self.href", notNullValue())
                .body("_links.cancel.href", notNullValue())
                .body("_links.user.href", notNullValue())
                .body("_links.resource.href", notNullValue());
    }

    @Test
    public void shouldGetAllReservations() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token);
        String clientId = createClientAndGetId(token);

        createReservation(token, clientId, roomId, 5);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/reservations")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void shouldGetClientAndRoomReservationsByStatus() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token);
        String clientId = createClientAndGetId(token);

        createReservation(token, clientId, roomId, 3);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/reservations/clients/" + clientId + "/reservations?status=current")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].clientId", equalTo(clientId));

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/reservations/rooms/" + roomId + "/reservations?status=current")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].roomId", equalTo(roomId));
    }

    @Test
    public void shouldEndReservationSuccessfully() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token);
        String clientId = createClientAndGetId(token);

        createReservation(token, clientId, roomId, 1);
        String reservationId = getFirstReservationIdForClient(token, clientId);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/v1/reservations/" + reservationId + "/end")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldDeleteReservation() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token);
        String clientId = createClientAndGetId(token);

        createReservation(token, clientId, roomId, 10);
        String reservationId = getFirstReservationIdForClient(token, clientId);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/v1/reservations/" + reservationId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/reservations/" + reservationId)
                .then()
                .statusCode(404);
    }

}
