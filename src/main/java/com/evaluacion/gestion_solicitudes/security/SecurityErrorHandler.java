package com.evaluacion.gestion_solicitudes.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


/**
 * Los errores de seguridad ocurren en los filtros, ANTES de llegar al controller,
 * por eso @RestControllerAdvice no los atrapa. Aquí generamos el mismo formato JSON.
 */
@Component 
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler{

     // 401: no autenticado (sin token, token inválido o expirado)
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        escribir(response, request, HttpStatus.UNAUTHORIZED,
                "Se requiere autenticación. Envíe un token válido en el header Authorization.");
    }

    // 403: autenticado pero sin el rol necesario
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        escribir(response, request, HttpStatus.FORBIDDEN,
                "No tiene permisos para realizar esta operación.");
    }

    private void escribir(HttpServletResponse response, HttpServletRequest request,
                          HttpStatus status, String mensaje) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String json = """
                {"timestamp":"%s","status":%d,"error":"%s","message":"%s","path":"%s","details":[]}"""
                .formatted(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                        mensaje, request.getRequestURI());
        response.getWriter().write(json);
    }

}
