package com.evaluacion.gestion_solicitudes.dto;

public record LoginResponse(
        String token,
        String tipo,
        long expiraEnSegundo) {

}
