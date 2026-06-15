package com.kemueljoshuamariano.ecommerce.auth.implement;

import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Proxy;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
class CustomUserServiceImplTest {

    @Test
    void loadUserByUsernameBuildsSpringSecurityUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("encoded-password");
        user.setRole("ADMIN");
        user.setIsActive(true);

        CustomUserServiceImpl customUserService =
                new CustomUserServiceImpl(userRepositoryReturning(Optional.of(user)));

        UserDetails userDetails = customUserService.loadUserByUsername("admin");

        assertEquals("admin", userDetails.getUsername());
        assertEquals("encoded-password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority())));
    }

    @Test
    void loadUserByUsernameDisablesInactiveUsers() {
        User user = new User();
        user.setUsername("disabled");
        user.setPassword("encoded-password");
        user.setRole("USER");
        user.setIsActive(false);

        CustomUserServiceImpl customUserService =
                new CustomUserServiceImpl(userRepositoryReturning(Optional.of(user)));

        UserDetails userDetails = customUserService.loadUserByUsername("disabled");

        assertFalse(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsernameThrowsWhenMissing() {
        CustomUserServiceImpl customUserService =
                new CustomUserServiceImpl(userRepositoryReturning(Optional.empty()));

        assertThrows(UsernameNotFoundException.class,
                () -> customUserService.loadUserByUsername("missing"));
    }

    private UserRepository userRepositoryReturning(Optional<User> user) {
        return (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                (proxy, method, args) -> {
                    if ("findByUsername".equals(method.getName())) {
                        return user;
                    }

                    if ("toString".equals(method.getName())) {
                        return "UserRepositoryTestProxy";
                    }

                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }
}
