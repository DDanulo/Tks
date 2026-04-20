package IT;

import domain.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import model.ChangePasswordDTO;
import model.LoginDTO;
import model.users.CreateClientDTO;
import model.users.UpdateUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = app.Main.class)
public class UserIntegrationTest {

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
    }


    private String getAdminToken() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("admin");
        loginDTO.setPassword("12345678");

        return given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }


    private CreateClientDTO buildValidClientDTO(String login) {
        return CreateClientDTO.builder()
                .login(login)
                .password("Password123!")
                .firstName("John")
                .lastName("Doe")
                .email(login + "@example.com")
                .isActive(true)
                .build();
    }

    @Test
    public void shouldGetAllUsers() {
        String token = getAdminToken();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void shouldCreateClientAndGetByLogin() {
        String token = getAdminToken();
        String testLogin = "client123";

        CreateClientDTO newClient = buildValidClientDTO(testLogin);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(newClient)
                .when()
                .post("/api/v1/users/client")
                .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/users/by-login/" + testLogin)
                .then()
                .statusCode(200)
                .body("login", equalTo(testLogin))
                .body("firstName", equalTo("John"))
                .body("role", equalTo("CLIENT"))
                .body("id", notNullValue());
    }

    @Test
    public void shouldUpdateClientUsingETag() {
        String token = getAdminToken();
        String testLogin = "update_user";

        CreateClientDTO newClient = buildValidClientDTO(testLogin);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(newClient)
                .when()
                .post("/api/v1/users/client")
                .then()
                .statusCode(200);

        String userId = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/users/by-login/" + testLogin)
                .then()
                .statusCode(200)
                .extract().path("id");

        String eTag = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/users/id/" + userId)
                .then()
                .statusCode(200)
                .extract().header("ETag");

        assertNotNull(eTag, "ETag header should be present in the response");

        UpdateUserDTO updatePayload = new UpdateUserDTO();
        updatePayload.setLogin(testLogin);
        updatePayload.setFirstName("Jane");
        updatePayload.setLastName("Smith");
        updatePayload.setEmail("jane.smith@example.com");
        updatePayload.setRole(Role.CLIENT);

        given()
                .header("Authorization", "Bearer " + token)
                .header("If-Match", eTag)
                .contentType(ContentType.JSON)
                .body(updatePayload)
                .when()
                .put("/api/v1/users/" + userId)
                .then()
                .statusCode(204);
    }

    @Test
    public void shouldActivateAndDeactivateClient() {
        String token = getAdminToken();
        String testLogin = "status_user";

        CreateClientDTO newClient = buildValidClientDTO(testLogin);

        given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON).body(newClient)
                .post("/api/v1/users/client").then().statusCode(200);

        String userId = given().header("Authorization", "Bearer " + token)
                .get("/api/v1/users/by-login/" + testLogin).then().extract().path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/v1/users/" + userId + "/deactivate")
                .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/v1/users/" + userId + "/activate")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldChangePassword() {
        String token = getAdminToken();
        String testLogin = "password_user";

        CreateClientDTO newClient = buildValidClientDTO(testLogin);

        given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON).body(newClient)
                .post("/api/v1/users/client").then().statusCode(200);

        String userId = given().header("Authorization", "Bearer " + token)
                .get("/api/v1/users/by-login/" + testLogin).then().extract().path("id");

        ChangePasswordDTO passwordPayload = new ChangePasswordDTO();
        passwordPayload.setOldPassword("Password123!");
        passwordPayload.setNewPassword("NewSecurePassword1!");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(passwordPayload)
                .when()
                .patch("/api/v1/users/" + userId + "/password")
                .then()
                .statusCode(200)
                .body(equalTo("Zmieniono hasło"));
    }
}