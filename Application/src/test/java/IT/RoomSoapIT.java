package IT;

import com.rentafield.tks.soap.auth.LoginRequest;
import com.rentafield.tks.soap.auth.LoginResponse;
import com.rentafield.tks.soap.rooms.*;
import domain.RoomType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(classes = app.Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomSoapIT {
    @LocalServerPort
    private int port;
    private String uri;
//    private WebServiceTemplate template;
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.1");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = this.port;
        uri = "http://localhost:" + port + "/ws/";
    }

    private WebServiceTemplate getAuthenticatedTemplate(String token) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.rentafield.tks.soap.rooms");

        WebServiceTemplate template = new WebServiceTemplate(marshaller);

        template.setMessageSender(new HttpUrlConnectionMessageSender() {
            @Override
            protected void prepareConnection(HttpURLConnection connection) throws IOException {
                super.prepareConnection(connection);
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }
        });

        return template;
    }

    private String createTestRoomAndGetId(String token, RoomType type, int capacity, double price) {
        AddRoomRequest request = new AddRoomRequest();
        request.setRoomType(type.name());
        request.setCapacity(capacity);
        request.setBasePrice(price);

        String url = "http://localhost:" + port + "/ws/";

        WebServiceTemplate template = getAuthenticatedTemplate(token);
        AddRoomResponse response = (AddRoomResponse) template.marshalSendAndReceive(url, request);

        return response.getRoom().getId();
    }

    private String getAdminToken() {
        LoginRequest request = new LoginRequest();
        request.setLogin("admin");
        request.setPassword("12345678");

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.rentafield.tks.soap.auth");

        WebServiceTemplate template = new WebServiceTemplate(marshaller);
        String url = "http://localhost:" + port + "/ws/";

        LoginResponse response = (LoginResponse) template.marshalSendAndReceive(url, request);

        return response.getAccessToken();
    }

    @Test
    public void testAddRoomViaSoapClient() {
        String token = getAdminToken();
        AddRoomRequest request = new AddRoomRequest();
        request.setRoomType("GYM");
        request.setCapacity(30);
        request.setBasePrice(150.0);

        WebServiceTemplate template = getAuthenticatedTemplate(token);
        AddRoomResponse response = (AddRoomResponse) template.marshalSendAndReceive(uri, request);

        assertNotNull(response.getRoom().getId());
        assertEquals("GYM", response.getRoom().getRoomType());
        assertEquals(30, response.getRoom().getCapacity());
        assertEquals(150.0, response.getRoom().getBasePrice(), 1);
    }

    @Test
    public void testGetRoomViaSoapClient() {
        String token = getAdminToken();
        String roomId = createTestRoomAndGetId(token, RoomType.HALL, 100, 500.0);

        GetRoomRequest request = new GetRoomRequest();
        request.setId(roomId);

        String url = "http://localhost:" + port + "/ws/";
        WebServiceTemplate template = getAuthenticatedTemplate(token);
        GetRoomResponse response = (GetRoomResponse) template.marshalSendAndReceive(url, request);

        assertNotNull(response.getRoom());
        assertEquals(roomId, response.getRoom().getId());
        assertEquals("HALL", response.getRoom().getRoomType());
    }

    @Test
    public void testUpdateRoomViaSoapClient() {
        String token = getAdminToken();
        String roomId = createTestRoomAndGetId(token, RoomType.HALL, 100, 500.0);

        UpdateRoomRequest request = new UpdateRoomRequest();
        request.setId(roomId);
        request.setRoomType("COURT");
        request.setCapacity(50);
        request.setBasePrice(250.0);

        WebServiceTemplate template = getAuthenticatedTemplate(token);
        UpdateRoomResponse response = (UpdateRoomResponse) template.marshalSendAndReceive(uri, request);

        assertNotNull(response.getRoom());
        assertEquals(roomId, response.getRoom().getId());
        assertEquals(50, response.getRoom().getCapacity());
        assertEquals(250.0, response.getRoom().getBasePrice(), 1);
    }

    @Test
    public void testRemoveRoomViaSoapClient() {
        String token = getAdminToken();
        String roomId = createTestRoomAndGetId(token, RoomType.HALL, 100, 500.0);

        RemoveRoomRequest request = new RemoveRoomRequest();
        request.setId(roomId);

        WebServiceTemplate template = getAuthenticatedTemplate(token);
        RemoveRoomResponse response = (RemoveRoomResponse) template.marshalSendAndReceive(uri, request);
        assertTrue(response.isStatus());
    }
}
