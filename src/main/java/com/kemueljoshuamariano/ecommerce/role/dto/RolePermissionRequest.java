package com.kemueljoshuamariano.ecommerce.role.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class RolePermissionRequest {

    @NotEmpty(message = "At least one permission is required")
    private Set<String> permissions;
}
