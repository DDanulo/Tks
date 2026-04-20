package controllers;

import aggregates.UserSoapControllerAdapter;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.rentafield.tks.soap.auth.LoginRequest;
import com.rentafield.tks.soap.auth.LoginResponse;
import com.rentafield.tks.soap.auth.RefreshRequest;
import com.rentafield.tks.soap.auth.RefreshResponse;
import domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import security.JwtService;

import java.util.Map;

@Endpoint
@RequiredArgsConstructor
public class AuthSoapController {

    private static final String NAMESPACE_URI = "http://rentafield.com/tks/soap/auth";
    private final UserSoapControllerAdapter userServiceAdapter;
    private final JwtService jwtService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "loginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest credentials) {
        User user;

        try {
            user = userServiceAdapter.findUserByLoginNoConvert(credentials.getLogin());
        } catch (exception.NotFoundException e) {
            // Throwing RuntimeException generates a SOAP Fault instead of returning null
            throw new RuntimeException("Błędne hasło lub login");
        }

        if (user == null) {
            throw new RuntimeException("Błędne hasło lub login");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(
                credentials.getPassword().toCharArray(),
                user.getPassword().toCharArray()
        );

        if (!result.verified) {
            throw new RuntimeException("Błędne hasło lub login");
        }

        if (user.getIsActive() == null || !user.getIsActive()) {
            throw new RuntimeException("Konto nie jest aktywne w systemie.");
        }

        // Generate tokens
        String access = jwtService.generateToken(user.getLogin(), user.getRole().name());
        String refresh = jwtService.generateRefreshToken(user.getLogin(), user.getRole().name());

        // Create and return the SOAP Response object
        LoginResponse response = new LoginResponse();
        response.setAccessToken(access);
        response.setRefreshToken(refresh);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "refreshRequest")
    @ResponsePayload
    public RefreshResponse refresh(@RequestPayload RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken != null && jwtService.validateToken(refreshToken)) {
            String username = jwtService.extractUsername(refreshToken);
            String role = jwtService.extractRole(refreshToken);

            User user = userServiceAdapter.findUserByLoginNoConvert(username);
            if (user == null || !user.getIsActive()) {
                throw new RuntimeException("Użytkownik nieaktywny");
            }

            String newAccess = jwtService.generateToken(username, role);
            String newRefresh = jwtService.generateRefreshToken(username, role);

            RefreshResponse response = new RefreshResponse();
            response.setAccessToken(newAccess);
            response.setRefreshToken(newRefresh);

            return response;
        }

        throw new RuntimeException("Nieprawidłowy refreshToken");
    }
}
