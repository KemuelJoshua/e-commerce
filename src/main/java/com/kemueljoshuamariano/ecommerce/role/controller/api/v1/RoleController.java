package com.kemueljoshuamariano.ecommerce.role.controller.api.v1;

import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.role.dto.RoleAssignmentRequest;
import com.kemueljoshuamariano.ecommerce.role.dto.RolePermissionRequest;
import com.kemueljoshuamariano.ecommerce.role.dto.RoleRequest;
import com.kemueljoshuamariano.ecommerce.role.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @PostMapping("/{roleName}/permissions")
    public ResponseEntity<Response> assignPermissionsToRole(
            @PathVariable String roleName,
            @Valid @RequestBody RolePermissionRequest rolePermissionRequest
    ) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(roleName, rolePermissionRequest));
    }

    @PostMapping("/assign")
    public ResponseEntity<Response> assignRolesToUser(@Valid @RequestBody RoleAssignmentRequest roleAssignmentRequest) {
        return ResponseEntity.ok(roleService.assignRolesToUser(roleAssignmentRequest));
    }
}
