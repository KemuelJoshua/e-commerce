package com.kemueljoshuamariano.ecommerce.auth.implement;

import com.kemueljoshuamariano.ecommerce.common.exception.Error;
import com.kemueljoshuamariano.ecommerce.auth.model.LoginRequest;
import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.user.model.User;
import com.kemueljoshuamariano.ecommerce.user.repository.UserRepository;
import com.kemueljoshuamariano.ecommerce.auth.service.AuthenticationService;
import com.kemueljoshuamariano.ecommerce.auth.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Response authenticateUser(LoginRequest loginRequest) {

        Response response = new Response("success", null);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository
                    .findByUsername(loginRequest.getUsername())
                    .orElseThrow();

            String token = jwtService.generateToken(user);

            response.setPayload(token);

        } catch (AuthenticationException e) {

            response.setStatus("failed");
            response.setError(
                    new Error("Incorrect username or password", 401)
            );
        }


        return response;
    }
}
