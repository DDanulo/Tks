package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${com.example.security.jwt.secret}")
    private String secret;

    @Value("${com.example.security.jwt.expTime}")
    private long expirationTime;

    @Value("${com.example.security.jwt.refreshExpTime}")
    private long refreshExpirationTime;

    public String generateToken(String username, String role) {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("role", role);
        return createToken(credentials, username, expirationTime);
    }

    public String generateRefreshToken(String username, String role) {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("role", role);

        return createToken(credentials, username, refreshExpirationTime);
    }

    private String createToken(Map<String, Object> claims, String subject, long ttl) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (ttl * 1000)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractCredential(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractCredential(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractCredential(String token, Function<Claims, T> credentialsResolver) {
        final Claims claims = extractAllCredentials(token);
        return credentialsResolver.apply(claims);
    }

    private Claims extractAllCredentials(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String signData(String data) {
        return Jwts.builder()
                .setSubject(data)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean verifySignature(String data, String signature) {
        try {
            String signedData = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(signature)
                    .getPayload()
                    .getSubject();
            return data.equals(signedData);
        } catch (Exception e) {
            return false;
        }
    }
}