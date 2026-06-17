package com.kemueljoshuamariano.ecommerce.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class RoleAssignmentRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotEmpty(message = "At least one role is required")
    private Set<String> roles;
}
