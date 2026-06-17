package com.kemueljoshuamariano.ecommerce.auth.security;

import com.kemueljoshuamariano.ecommerce.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(User user);

    long getExpirationTimeSeconds();

    String extractUsername(String token);
    
    boolean isTokenValid(String token, UserDetails userDetails);
}
