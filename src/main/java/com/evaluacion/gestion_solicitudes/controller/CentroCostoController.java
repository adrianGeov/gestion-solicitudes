package com.evaluacion.gestion_solicitudes.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.evaluacion.gestion_solicitudes.dto.CentroCostoRequest;
import com.evaluacion.gestion_solicitudes.dto.CentroCostoResponse;
import com.evaluacion.gestion_solicitudes.dto.PageResponse;
import com.evaluacion.gestion_solicitudes.service.CentroCostoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/centros-costo")
@RequiredArgsConstructor
public class CentroCostoController {

    private final CentroCostoService service;

    @PostMapping
    public ResponseEntity<CentroCostoResponse> crear(@Valid @RequestBody CentroCostoRequest request) {
        CentroCostoResponse creado = service.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(location).body(creado); // 201 Created
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroCostoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id)); // 200 OK
    }

    @GetMapping
    public ResponseEntity<PageResponse<CentroCostoResponse>> listar(
            @RequestParam(required = false) String nombre,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(nombre, pageable)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CentroCostoResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody CentroCostoRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
