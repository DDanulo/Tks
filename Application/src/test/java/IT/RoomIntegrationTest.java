package IT;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import model.CreateRoomDTO;
import model.LoginDTO;
import domain.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = app.Main.class)
public class RoomIntegrationTest {

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
        RestAssured.baseURI = "http://localhost:" + port;
    }

    private String getAdminToken() {
        LoginDTO login = new LoginDTO();
        login.setLogin("admin");
        login.setPassword("12345678");

        return given()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    private String createRoomAndGetId(String token, RoomType type, int capacity, double price) {
        CreateRoomDTO roomDTO = CreateRoomDTO.builder()
                .roomType(type)
                .capacity(capacity)
                .basePrice(price)
                .build();

        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(roomDTO)
                .when()
                .post("/api/v1/rooms")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test
    public void testCreateRoom() {
        String token = getAdminToken();

        CreateRoomDTO roomDTO = CreateRoomDTO.builder()
                .roomType(RoomType.GYM)
                .capacity(30)
                .basePrice(150.0)
                .build();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(roomDTO)
                .when()
                .post("/api/v1/rooms")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("roomType", equalTo("GYM"))
                .body("capacity", equalTo(30))
                .body("basePrice", equalTo(150.0f));
    }

    @Test
    public void testGetRoomById() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token, RoomType.HALL, 100, 500.0);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/rooms/" + roomId)
                .then()
                .statusCode(200)
                .body("id", equalTo(roomId))
                .body("roomType", equalTo("HALL"));
    }

    @Test
    public void testGetAllRooms() {
        String token = getAdminToken();

        createRoomAndGetId(token, RoomType.FIELD, 22, 200.0);
        createRoomAndGetId(token, RoomType.COURT, 10, 120.0);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/rooms")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    public void testUpdateRoom() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token, RoomType.GYM, 20, 100.0);

        CreateRoomDTO updatedRoomDTO = CreateRoomDTO.builder()
                .roomType(RoomType.GYM)
                .capacity(50)
                .basePrice(120.0)
                .build();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(updatedRoomDTO)
                .when()
                .put("/api/v1/rooms/" + roomId)
                .then()
                .statusCode(200)
                .body("capacity", equalTo(50))
                .body("basePrice", equalTo(120.0f));

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/rooms/" + roomId)
                .then()
                .statusCode(200)
                .body("capacity", equalTo(50));
    }

    @Test
    public void testDeleteRoom() {
        String token = getAdminToken();
        String roomId = createRoomAndGetId(token, RoomType.HALL, 200, 1000.0);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/v1/rooms/" + roomId)
                .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/rooms/" + roomId)
                .then()
                .statusCode(404);
    }


}
