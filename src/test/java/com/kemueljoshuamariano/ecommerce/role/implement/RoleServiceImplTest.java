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
import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleServiceImplTest {

    @Test
    void createRoleReturnsConflictWhenRoleAlreadyExists() {
        Role existingRole = new Role();
        existingRole.setRoleId(1);
        existingRole.setName("ADMIN");

        RoleServiceImpl roleService = new RoleServiceImpl(
                roleRepositoryWithRoles(List.of(existingRole)),
                permissionRepositoryWithPermissions(List.of()),
                userRepositoryWithUser(Optional.empty())
        );

        RoleRequest request = new RoleRequest();
        request.setName("admin");

        Response response = roleService.createRole(request);

        assertTrue(response instanceof ErrorResponse);
        assertEquals("failed", response.getStatus());
        assertNotNull(response.getError());
        assertEquals("Role already exists", ((ErrorResponse) response).getMessage());
        assertEquals(409, response.getError().getCode());
        assertEquals("Role already exists", response.getError().getDetails());
    }

    @Test
    void assignPermissionsToRoleReturnsUpdatedRole() {
        Permission readPermission = permission("CATEGORY_READ");
        Permission createPermission = permission("CATEGORY_CREATE");

        Role adminRole = new Role();
        adminRole.setRoleId(1);
        adminRole.setName("ADMIN");

        AtomicReference<Role> savedRole = new AtomicReference<>();
        RoleRepository roleRepository = roleRepositoryWithRoles(List.of(adminRole), savedRole);
        PermissionRepository permissionRepository = permissionRepositoryWithPermissions(
                List.of(readPermission, createPermission)
        );

        RoleServiceImpl roleService = new RoleServiceImpl(
                roleRepository,
                permissionRepository,
                userRepositoryWithUser(Optional.empty())
        );

        RolePermissionRequest request = new RolePermissionRequest();
        request.setPermissions(Set.of("category read", "category_create"));

        Response response = roleService.assignPermissionsToRole("admin", request);

        assertTrue(response instanceof SuccessResponse);
        assertEquals("success", response.getStatus());
        assertNull(response.getError());
        assertNotNull(savedRole.get());
        assertEquals(Set.of("CATEGORY_READ", "CATEGORY_CREATE"),
                savedRole.get().getPermissions().stream().map(Permission::getName).collect(java.util.stream.Collectors.toSet()));

        RoleResponse payload = (RoleResponse) response.getPayload();
        assertEquals("ADMIN", payload.getName());
    }

    @Test
    void assignRolesToUserReplacesUserRoles() {
        Role adminRole = new Role();
        adminRole.setRoleId(1);
        adminRole.setName("ADMIN");

        User user = new User();
        user.setUsername("admin");

        AtomicReference<User> savedUser = new AtomicReference<>();

        RoleServiceImpl roleService = new RoleServiceImpl(
                roleRepositoryWithRoles(List.of(adminRole)),
                permissionRepositoryWithPermissions(List.of()),
                userRepositoryWithUser(Optional.of(user), savedUser)
        );

        RoleAssignmentRequest request = new RoleAssignmentRequest();
        request.setUsername("admin");
        request.setRoles(Set.of("admin"));

        Response response = roleService.assignRolesToUser(request);

        assertTrue(response instanceof SuccessResponse);
        assertEquals("success", response.getStatus());
        assertNull(response.getError());
        assertNotNull(savedUser.get());
        assertEquals(Set.of("ADMIN"),
                savedUser.get().getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()));

        LinkedHashMap<?, ?> payload = (LinkedHashMap<?, ?>) response.getPayload();
        assertEquals("admin", payload.get("username"));
        assertEquals(Set.of("ADMIN"), payload.get("roles"));
    }

    private RoleRepository roleRepositoryWithRoles(List<Role> roles) {
        return roleRepositoryWithRoles(roles, new AtomicReference<>());
    }

    private RoleRepository roleRepositoryWithRoles(List<Role> roles, AtomicReference<Role> savedRole) {
        List<Role> storedRoles = new ArrayList<>(roles);

        return (RoleRepository) Proxy.newProxyInstance(
                RoleRepository.class.getClassLoader(),
                new Class[]{RoleRepository.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findAll" -> storedRoles;
                    case "findByName" -> storedRoles.stream()
                            .filter(role -> role.getName().equals(args[0]))
                            .findFirst();
                    case "findByNameIn" -> {
                        Collection<?> names = (Collection<?>) args[0];
                        yield storedRoles.stream()
                                .filter(role -> names.contains(role.getName()))
                                .toList();
                    }
                    case "save" -> {
                        Role role = (Role) args[0];
                        savedRole.set(role);
                        if (!storedRoles.contains(role)) {
                            storedRoles.add(role);
                        }
                        yield role;
                    }
                    case "toString" -> "RoleRepositoryTestProxy";
                    default -> throw new UnsupportedOperationException(method.getName());
                }
        );
    }

    private PermissionRepository permissionRepositoryWithPermissions(List<Permission> permissions) {
        return (PermissionRepository) Proxy.newProxyInstance(
                PermissionRepository.class.getClassLoader(),
                new Class[]{PermissionRepository.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findAll" -> permissions;
                    case "findByName" -> permissions.stream()
                            .filter(permission -> permission.getName().equals(args[0]))
                            .findFirst();
                    case "findByNameIn" -> {
                        Collection<?> names = (Collection<?>) args[0];
                        yield permissions.stream()
                                .filter(permission -> names.contains(permission.getName()))
                                .toList();
                    }
                    case "save" -> args[0];
                    case "toString" -> "PermissionRepositoryTestProxy";
                    default -> throw new UnsupportedOperationException(method.getName());
                }
        );
    }

    private UserRepository userRepositoryWithUser(Optional<User> user) {
        return userRepositoryWithUser(user, new AtomicReference<>());
    }

    private UserRepository userRepositoryWithUser(Optional<User> user, AtomicReference<User> savedUser) {
        return (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findByUsername", "findByUsernameWithRoles" -> user;
                    case "save" -> {
                        User updatedUser = (User) args[0];
                        savedUser.set(updatedUser);
                        yield updatedUser;
                    }
                    case "toString" -> "UserRepositoryTestProxy";
                    default -> throw new UnsupportedOperationException(method.getName());
                }
        );
    }

    private Permission permission(String name) {
        Permission permission = new Permission();
        permission.setName(name);
        return permission;
    }
}
