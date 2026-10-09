package com.evaluacion.gestion_solicitudes.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evaluacion.gestion_solicitudes.dto.CentroCostoRequest;
import com.evaluacion.gestion_solicitudes.dto.CentroCostoResponse;

public interface CentroCostoService {

    CentroCostoResponse crear(CentroCostoRequest request);

    CentroCostoResponse obtenerPorId(Long id);

    Page<CentroCostoResponse> listar(String nombre, Pageable pageable);

    CentroCostoResponse actualizar(Long id, CentroCostoRequest request);

    void eliminar(Long id);
}
