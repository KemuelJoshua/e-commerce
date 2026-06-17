package com.kemueljoshuamariano.ecommerce.role.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class RoleResponse {
    private Integer roleId;
    private String name;
    private Set<String> permissions;
}
