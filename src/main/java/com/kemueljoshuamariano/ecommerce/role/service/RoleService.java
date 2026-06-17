package com.kemueljoshuamariano.ecommerce.role.service;

import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.role.dto.RoleAssignmentRequest;
import com.kemueljoshuamariano.ecommerce.role.dto.RolePermissionRequest;
import com.kemueljoshuamariano.ecommerce.role.dto.RoleRequest;

public interface RoleService {

    Response getAllRoles();

    Response createRole(RoleRequest roleRequest);

    Response assignPermissionsToRole(String roleName, RolePermissionRequest rolePermissionRequest);

    Response assignRolesToUser(RoleAssignmentRequest roleAssignmentRequest);
}
