package com.kemueljoshuamariano.ecommerce.auth.security;

import com.kemueljoshuamariano.ecommerce.user.model.User;

public interface JwtService {
    String generateToken(User user);
}