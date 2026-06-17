package com.kemueljoshuamariano.ecommerce.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kemueljoshuamariano.ecommerce.common.response.ErrorResponse;
import java.io.IOException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(
                        "Authentication required.",
                        "Invalid or expired token.",
                        401
                )
        ));
    }
}
