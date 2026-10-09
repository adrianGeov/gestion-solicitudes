package com.evaluacion.gestion_solicitudes.dto;

import java.time.LocalDate;

import com.evaluacion.gestion_solicitudes.entity.EstatusSolicitud;

public record SolicitudFiltro(
     String titulo,
        EstatusSolicitud estatus,
        Long centroCostoId,
        LocalDate fechaDesde,
        LocalDate fechaHasta
) {

}
