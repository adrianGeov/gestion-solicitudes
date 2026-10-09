package com.evaluacion.gestion_solicitudes.dto;

import java.time.LocalDateTime;

public record CentroCostoResponse(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {
}
