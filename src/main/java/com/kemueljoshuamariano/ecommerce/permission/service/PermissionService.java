package com.kemueljoshuamariano.ecommerce.permission.service;

import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.permission.dto.PermissionRequest;

public interface PermissionService {

    Response getAllPermissions();

    Response createPermission(PermissionRequest permissionRequest);
}
