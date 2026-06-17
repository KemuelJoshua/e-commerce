package com.kemueljoshuamariano.ecommerce.role.implement;

import com.kemueljoshuamariano.ecommerce.common.response.ErrorResponse;
import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.common.response.SuccessResponse;
import com.kemueljoshuamariano.ecommerce.permission.model.Permission;
import com.kemueljoshuamariano.ecommerce.permission.repository.PermissionRepository;
import com.kemueljoshuamariano.ecommerce.role.dto.RoleAssignmentRequest;
import com.kemueljoshuamariano.ecommerce.role.dto.RolePermissionRequest;
import com.kemueljoshuamariano.ecommerce.role.dto.RoleRequest;
import com.kemueljoshuamariano.ecommerce.role.dto.RoleResponse;
import com.kemueljoshuamariano.ecommerce.role.model.Role;
import com.kemueljoshuamariano.ecommerce.role.repository.RoleRepository;
import com.kemueljoshuamariano.ecommerce.role.service.RoleService;
import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public RoleServiceImpl(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            UserRepository userRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Response getAllRoles() {
        try {
            List<RoleResponse> roles = roleRepository.findAll()
                    .stream()
                    .map(this::mapRole)
                    .toList();

            return new SuccessResponse(roles);
        } catch (Exception e) {
            return new ErrorResponse("Failed to fetch roles", 500);
        }
    }

    @Override
    @Transactional
    public Response createRole(RoleRequest roleRequest) {
        try {
            String roleName = normalizeRoleName(roleRequest.getName());

            if (roleRepository.findByName(roleName).isPresent()) {
                return new ErrorResponse("Role already exists", 409);
            }

            Role role = new Role();
            role.setName(roleName);

            if (roleRequest.getPermissions() != null && !roleRequest.getPermissions().isEmpty()) {
                Response validationResponse = attachPermissions(role, roleRequest.getPermissions());
                if (validationResponse != null) {
                    return validationResponse;
                }
            }

            Role savedRole = roleRepository.save(role);

            return new SuccessResponse(mapRole(savedRole));
        } catch (Exception e) {
            return new ErrorResponse("Failed to create role", 500);
        }
    }

    @Override
    @Transactional
    public Response assignPermissionsToRole(String roleName, RolePermissionRequest rolePermissionRequest) {
        try {
            Role role = roleRepository.findByName(normalizeRoleName(roleName))
                    .orElse(null);

            if (role == null) {
                return new ErrorResponse("Role not found", 404);
            }

            Response validationResponse = attachPermissions(role, rolePermissionRequest.getPermissions());
            if (validationResponse != null) {
                return validationResponse;
            }

            Role updatedRole = roleRepository.save(role);

            return new SuccessResponse(mapRole(updatedRole));
        } catch (Exception e) {
            return new ErrorResponse("Failed to assign permissions to role", 500);
        }
    }

    @Override
    @Transactional
    public Response assignRolesToUser(RoleAssignmentRequest roleAssignmentRequest) {
        try {
            User user = userRepository.findByUsernameWithRoles(roleAssignmentRequest.getUsername())
                    .orElse(null);

            if (user == null) {
                return new ErrorResponse("User not found", 404);
            }

            Set<String> requestedRoleNames = roleAssignmentRequest.getRoles()
                    .stream()
                    .map(this::normalizeRoleName)
                    .collect(java.util.stream.Collectors.toSet());

            List<Role> roles = roleRepository.findByNameIn(requestedRoleNames);

            if (roles.size() != requestedRoleNames.size()) {
                return new ErrorResponse("One or more roles do not exist", 404);
            }

            user.setRoles(new HashSet<>(roles));
            User updatedUser = userRepository.save(user);

            return new SuccessResponse(mapUserRoles(updatedUser));
        } catch (Exception e) {
            return new ErrorResponse("Failed to assign roles to user", 500);
        }
    }

    private Response attachPermissions(Role role, Set<String> requestedPermissionNames) {
        Set<String> normalizedPermissionNames = requestedPermissionNames.stream()
                .map(this::normalizePermissionName)
                .collect(java.util.stream.Collectors.toSet());

        List<Permission> permissions = permissionRepository.findByNameIn(normalizedPermissionNames);

        if (permissions.size() != normalizedPermissionNames.size()) {
            return new ErrorResponse("One or more permissions do not exist", 404);
        }

        role.setPermissions(new HashSet<>(permissions));
        return null;
    }

    private RoleResponse mapRole(Role role) {
        Set<String> permissions = role.getPermissions()
                .stream()
                .map(Permission::getName)
                .collect(java.util.stream.Collectors.toCollection(java.util.TreeSet::new));

        return new RoleResponse(role.getRoleId(), role.getName(), permissions);
    }

    private java.util.Map<String, Object> mapUserRoles(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toCollection(java.util.TreeSet::new));

        java.util.Map<String, Object> payload = new java.util.LinkedHashMap<>();
        payload.put("username", user.getUsername());
        payload.put("roles", roles);
        return payload;
    }

    private String normalizeRoleName(String roleName) {
        return roleName.trim().toUpperCase().replace(' ', '_');
    }

    private String normalizePermissionName(String permissionName) {
        return permissionName.trim().toUpperCase().replace(' ', '_');
    }
}
