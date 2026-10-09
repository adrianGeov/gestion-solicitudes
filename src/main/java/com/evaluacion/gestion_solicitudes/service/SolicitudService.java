package com.evaluacion.gestion_solicitudes.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evaluacion.gestion_solicitudes.dto.SolicitudFiltro;
import com.evaluacion.gestion_solicitudes.dto.SolicitudRequest;
import com.evaluacion.gestion_solicitudes.dto.SolicitudResponse;

public interface SolicitudService {

     SolicitudResponse crear(SolicitudRequest request);

    SolicitudResponse obtenerPorId(Long id);

    Page<SolicitudResponse> listar(SolicitudFiltro filtro, Pageable pageable);

    SolicitudResponse actualizar(Long id, SolicitudRequest request);

    void eliminar(Long id);

}
