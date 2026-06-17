package com.kemueljoshuamariano.ecommerce.config;

import com.kemueljoshuamariano.ecommerce.permission.model.Permission;
import com.kemueljoshuamariano.ecommerce.permission.repository.PermissionRepository;
import com.kemueljoshuamariano.ecommerce.role.model.Role;
import com.kemueljoshuamariano.ecommerce.role.repository.RoleRepository;
import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public DataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Permission> adminPermissions = ensurePermissions(
                "CATEGORY_READ",
                "CATEGORY_CREATE",
                "ROLE_MANAGE",
                "PERMISSION_MANAGE"
        );
        Role adminRole = ensureRole("ADMIN", adminPermissions);

        if (userRepository.findByUsernameWithRoles("admin").isEmpty()) {

            User user = new User();

            user.setProvider("LOCAL");
            user.setProviderId("admin");
            user.setUsername("admin");
            user.setPassword(this.passwordEncoder.encode("password"));
            user.setFirstname("System");
            user.setLastname("Administrator");
            user.setEmail("admin@example.com");
            user.setIsActive(true);
            user.setRoles(new HashSet<>(List.of(adminRole)));

            userRepository.save(user);

            System.out.println("Default admin account created.");
            return;
        }

        User adminUser = userRepository.findByUsernameWithRoles("admin").orElseThrow();
        adminUser.getRoles().add(adminRole);
        userRepository.save(adminUser);
    }

    private List<Permission> ensurePermissions(String... permissionNames) {
        return java.util.Arrays.stream(permissionNames)
                .map(this::ensurePermission)
                .toList();
    }

    private Permission ensurePermission(String permissionName) {
        return permissionRepository.findByName(permissionName)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName(permissionName);
                    return permissionRepository.save(permission);
                });
    }

    private Role ensureRole(String roleName, List<Permission> permissions) {
        Role role = roleRepository.findByName(roleName).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(roleName);
            return newRole;
        });

        role.setPermissions(new HashSet<>(permissions));
        return roleRepository.save(role);
    }
}
