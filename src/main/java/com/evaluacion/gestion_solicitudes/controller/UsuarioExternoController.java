package com.evaluacion.gestion_solicitudes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evaluacion.gestion_solicitudes.dto.UsuarioExternoResponse;
import com.evaluacion.gestion_solicitudes.service.UsuarioExternoService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/v1/usuarios-externos")
@RequiredArgsConstructor 
public class UsuarioExternoController {

    private final UsuarioExternoService service;

    @GetMapping
    public ResponseEntity<List<UsuarioExternoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioExternoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

}
