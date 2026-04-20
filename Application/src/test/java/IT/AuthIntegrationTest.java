package IT;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import model.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = app.Main.class)
public class AuthIntegrationTest {

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

    @Test
    public void shouldLoginSuccessfullyAndReturnTokens() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("admin");
        loginDTO.setPassword("12345678");

        given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    public void shouldFailLoginWithWrongPassword() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("admin");
        loginDTO.setPassword("wrong_password");

        given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401)
                .body(equalTo("Błędne hasło lub login"));
    }

    @Test
    public void shouldFailLoginWithNonExistentUser() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("ghost_user");
        loginDTO.setPassword("password123");

        given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401)
                .body(equalTo("Błędne hasło lub login"));
    }

    @Test
    public void shouldRefreshTokensSuccessfully() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("admin");
        loginDTO.setPassword("12345678");

        String refreshToken = given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("refreshToken");

        Map<String, String> refreshPayload = new HashMap<>();
        refreshPayload.put("refreshToken", refreshToken);

        given()
                .contentType(ContentType.JSON)
                .body(refreshPayload)
                .when()
                .post("/api/v1/auth/refresh")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    public void testFailRefreshWithInvalidToken() {
        Map<String, String> refreshPayload = new HashMap<>();
        refreshPayload.put("refreshToken", "fake.token");

        given()
                .contentType(ContentType.JSON)
                .body(refreshPayload)
                .when()
                .post("/api/v1/auth/refresh")
                .then()
                .statusCode(401)
                .body(equalTo("Nieprawidłowy refreshToken"));
    }

}