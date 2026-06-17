package com.kemueljoshuamariano.ecommerce.auth.implement;

import com.kemueljoshuamariano.ecommerce.auth.dto.AuthenticatedUser;
import com.kemueljoshuamariano.ecommerce.auth.dto.AuthenticationPayload;
import com.kemueljoshuamariano.ecommerce.auth.model.LoginRequest;
import com.kemueljoshuamariano.ecommerce.auth.security.JwtService;
import com.kemueljoshuamariano.ecommerce.common.response.ErrorResponse;
import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.common.response.SuccessResponse;
import com.kemueljoshuamariano.ecommerce.role.model.Role;
import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationServiceImplTest {

    @Test
    void authenticateUserReturnsTokenWhenCredentialsAreValid() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        user.setRoles(new LinkedHashSet<>(Set.of(adminRole)));

        AtomicReference<Authentication> capturedAuthentication = new AtomicReference<>();
        AuthenticationManager authenticationManager = authentication -> {
            capturedAuthentication.set(authentication);
            return authentication;
        };
        JwtService jwtService = jwtServiceReturning(user, "jwt-token");

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                userRepositoryReturning(Optional.of(user)),
                jwtService,
                authenticationManager
        );

        Response response = authenticationService.authenticateUser(
                new LoginRequest("admin", "secret")
        );

        assertTrue(response instanceof SuccessResponse);
        assertEquals("success", response.getStatus());
        assertNull(response.getError());
        assertNotNull(response.getPayload());
        AuthenticationPayload payload = (AuthenticationPayload) response.getPayload();
        assertEquals("jwt-token", payload.getAccessToken());
        assertEquals("Bearer", payload.getTokenType());
        assertEquals(3600L, payload.getExpiresIn());
        AuthenticatedUser authenticatedUser = payload.getUser();
        assertNotNull(authenticatedUser);
        assertEquals(1, authenticatedUser.getId());
        assertEquals("admin", authenticatedUser.getUsername());
        assertEquals("admin@example.com", authenticatedUser.getEmail());
        assertEquals(Set.of("ADMIN"), authenticatedUser.getRoles());
        assertNotNull(capturedAuthentication.get());
        assertTrue(capturedAuthentication.get() instanceof UsernamePasswordAuthenticationToken);
        assertEquals("admin", capturedAuthentication.get().getPrincipal());
        assertEquals("secret", capturedAuthentication.get().getCredentials());
    }

    @Test
    void authenticateUserReturnsUnauthorizedErrorWhenCredentialsAreInvalid() {
        AuthenticationManager authenticationManager = authentication -> {
            throw new BadCredentialsException("bad credentials");
        };

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                userRepositoryReturning(Optional.empty()),
                jwtServiceReturning(null, "unused-token"),
                authenticationManager
        );

        Response response = authenticationService.authenticateUser(
                new LoginRequest("admin", "wrong-password")
        );

        assertTrue(response instanceof ErrorResponse);
        assertEquals("failed", response.getStatus());
        assertNull(response.getPayload());
        assertNotNull(response.getError());
        assertEquals("Incorrect username or password", ((ErrorResponse) response).getMessage());
        assertEquals("Incorrect username or password", response.getError().getDetails());
        assertEquals(401, response.getError().getCode());
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

    private JwtService jwtServiceReturning(User expectedUser, String token) {
        return new JwtService() {
            @Override
            public String generateToken(User user) {
                if (expectedUser != null) {
                    assertSame(expectedUser, user);
                }
                return token;
            }

            @Override
            public long getExpirationTimeSeconds() {
                return 3600L;
            }

            @Override
            public String extractUsername(String token) {
                throw new UnsupportedOperationException("Not needed in this test");
            }

            @Override
            public boolean isTokenValid(String token, UserDetails userDetails) {
                throw new UnsupportedOperationException("Not needed in this test");
            }
        };
    }
}
