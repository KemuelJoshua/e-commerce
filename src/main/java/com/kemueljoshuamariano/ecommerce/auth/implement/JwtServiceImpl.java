package com.kemueljoshuamariano.ecommerce.auth.implement;

import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.auth.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY =
            "my-super-secret-key-my-super-secret-key-123456";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .signWith(getSigningKey())
                .compact();
    }
}