package com.evaluacion.gestion_solicitudes.service;

import java.util.List;

import com.evaluacion.gestion_solicitudes.dto.UsuarioExternoResponse;

public interface UsuarioExternoService {

     UsuarioExternoResponse obtenerPorId(Long id);

    List<UsuarioExternoResponse> listar();

}
