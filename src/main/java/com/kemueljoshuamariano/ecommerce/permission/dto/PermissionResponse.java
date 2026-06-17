package com.kemueljoshuamariano.ecommerce.permission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionResponse {
    private Integer permissionId;
    private String name;
}
