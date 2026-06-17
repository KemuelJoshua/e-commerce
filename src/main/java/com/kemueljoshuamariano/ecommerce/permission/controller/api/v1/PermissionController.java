package com.kemueljoshuamariano.ecommerce.permission.controller.api.v1;

import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.permission.dto.PermissionRequest;
import com.kemueljoshuamariano.ecommerce.permission.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createPermission(@Valid @RequestBody PermissionRequest permissionRequest) {
        return ResponseEntity.ok(permissionService.createPermission(permissionRequest));
    }
}
