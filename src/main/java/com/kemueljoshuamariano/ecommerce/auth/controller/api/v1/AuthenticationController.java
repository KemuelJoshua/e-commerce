package com.kemueljoshuamariano.ecommerce.auth.controller.api.v1;

import com.kemueljoshuamariano.ecommerce.auth.model.LoginRequest;
import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.auth.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.authenticateUser(loginRequest));
    }

}