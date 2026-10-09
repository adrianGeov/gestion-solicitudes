package com.evaluacion.gestion_solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evaluacion.gestion_solicitudes.client.JsonPlaceholderClient;
import com.evaluacion.gestion_solicitudes.dto.UsuarioExternoResponse;
import com.evaluacion.gestion_solicitudes.service.UsuarioExternoService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UsuarioExternoServiceImpl implements UsuarioExternoService{

     private final JsonPlaceholderClient client;

    @Override
    public UsuarioExternoResponse obtenerPorId(Long id) {
        return client.obtenerUsuario(id);
    }

    @Override
    public List<UsuarioExternoResponse> listar() {
        return client.obtenerUsuarios();
    }

}
