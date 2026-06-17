package com.kemueljoshuamariano.ecommerce.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthenticatedUser {

    private Integer id;
    private String username;
    private String email;
    private Set<String> roles;
}
