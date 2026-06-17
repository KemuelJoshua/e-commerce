package com.kemueljoshuamariano.ecommerce.permission.implement;

import com.kemueljoshuamariano.ecommerce.common.exception.Error;
import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.permission.dto.PermissionRequest;
import com.kemueljoshuamariano.ecommerce.permission.dto.PermissionResponse;
import com.kemueljoshuamariano.ecommerce.permission.model.Permission;
import com.kemueljoshuamariano.ecommerce.permission.repository.PermissionRepository;
import com.kemueljoshuamariano.ecommerce.permission.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Response getAllPermissions() {
        try {
            List<PermissionResponse> permissions = permissionRepository.findAll()
                    .stream()
                    .map(this::mapPermission)
                    .toList();

            return new Response("success", permissions, null);
        } catch (Exception e) {
            return new Response("failed", new Error("Failed to fetch permissions", 500));
        }
    }

    @Override
    @Transactional
    public Response createPermission(PermissionRequest permissionRequest) {
        try {
            String permissionName = normalizeName(permissionRequest.getName());

            if (permissionRepository.findByName(permissionName).isPresent()) {
                return new Response("failed", new Error("Permission already exists", 409));
            }

            Permission permission = new Permission();
            permission.setName(permissionName);

            Permission savedPermission = permissionRepository.save(permission);

            return new Response("success", mapPermission(savedPermission), null);
        } catch (Exception e) {
            return new Response("failed", new Error("Failed to create permission", 500));
        }
    }

    private PermissionResponse mapPermission(Permission permission) {
        return new PermissionResponse(permission.getPermissionId(), permission.getName());
    }

    private String normalizeName(String name) {
        return name.trim().toUpperCase().replace(' ', '_');
    }
}
