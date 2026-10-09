package com.evaluacion.gestion_solicitudes.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Asigna un requestId a cada petición (MDC) y registra un log de acceso.
 * Se ejecuta antes que Spring Security para registrar también los 401/403.
 * NO registra headers ni body para no exponer tokens ni datos sensibles.
 */
@Slf4j 
@Component 
@Order (Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter  extends OncePerRequestFilter{

     private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID_MDC = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        long inicio = System.currentTimeMillis();
        MDC.put(REQUEST_ID_MDC, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        try {
            chain.doFilter(request, response);
        } finally {
            long duracion = System.currentTimeMillis() - inicio;
            int status = response.getStatus();

            if (status >= 500) {
                log.error("{} {} -> {} ({} ms)", request.getMethod(), request.getRequestURI(), status, duracion);
            } else if (status >= 400) {
                log.warn("{} {} -> {} ({} ms)", request.getMethod(), request.getRequestURI(), status, duracion);
            } else {
                log.info("{} {} -> {} ({} ms)", request.getMethod(), request.getRequestURI(), status, duracion);
            }

            MDC.clear();   // Evita que el requestId se "filtre" a otra petición del mismo hilo
        }
    }

    /** No registrar peticiones de Swagger ni Actuator para no llenar el log de ruido */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs") || uri.startsWith("/actuator");
    }

}
