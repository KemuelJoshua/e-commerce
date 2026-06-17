package com.kemueljoshuamariano.ecommerce.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationPayload {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private AuthenticatedUser user;
}
