package com.evaluacion.gestion_solicitudes.security;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Obtiene el usuario autenticado desde el SecurityContext,
 * que el JwtAuthenticationFilter llenó a partir del token.
 */
@Component

public class UsuarioAutenticadoProvider {

    private static final String USUARIO_SISTEMA = "SISTEMA";

    public Optional<String> obtenerUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.ofNullable(auth.getName());
    }

    public String obtenerUsuarioOSistema() {
        return obtenerUsuario().orElse(USUARIO_SISTEMA);
    }

}
