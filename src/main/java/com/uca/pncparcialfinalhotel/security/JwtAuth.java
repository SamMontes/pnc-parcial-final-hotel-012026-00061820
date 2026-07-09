package com.uca.pncparcialfinalhotel.security;

//implementar APIError
import com.uca.pncparcialfinalhotel.exception.ApiError;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuth implements AuthenticationEntryPoint {

    private final JsonMapper jsonMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiError error = ApiError.builder().status(401).error("Unauthorized")
                .message("Debes autenticarte para acceder a este recurso").timestamp(LocalDateTime.now()).build();
        jsonMapper.writeValue(response.getOutputStream(), error);
    }
}