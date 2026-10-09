package com.evaluacion.gestion_solicitudes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioExternoResponse(
        Long id,
        String name,
        String username,
        String email,
        String phone) {

}
