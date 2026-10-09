package com.evaluacion.gestion_solicitudes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.evaluacion.gestion_solicitudes.entity.EstatusSolicitud;

public record SolicitudResponse(
        Long id,
        String titulo,
        String descripcion,
        BigDecimal monto,
        EstatusSolicitud estatus,
        LocalDate fechaSolicitud,
        boolean activo,
        Long centroCostoId,
        String centroCostoCodigo,
        String centroCostoNombre,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        String creadoPor,
        String modificadoPor

) {

}
