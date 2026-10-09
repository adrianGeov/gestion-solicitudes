package com.evaluacion.gestion_solicitudes.controller;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.evaluacion.gestion_solicitudes.dto.PageResponse;
import com.evaluacion.gestion_solicitudes.dto.SolicitudFiltro;
import com.evaluacion.gestion_solicitudes.dto.SolicitudRequest;
import com.evaluacion.gestion_solicitudes.dto.SolicitudResponse;
import com.evaluacion.gestion_solicitudes.entity.EstatusSolicitud;
import com.evaluacion.gestion_solicitudes.service.SolicitudService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService service;

    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody SolicitudRequest request) {
        SolicitudResponse creada = service.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.id())
                .toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<SolicitudResponse>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) EstatusSolicitud estatus,
            @RequestParam(required = false) Long centroCostoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @PageableDefault(size = 10, sort = "fechaSolicitud", direction = Sort.Direction.DESC) Pageable pageable) {

        SolicitudFiltro filtro = new SolicitudFiltro(titulo, estatus, centroCostoId, fechaDesde, fechaHasta);
        return ResponseEntity.ok(PageResponse.from(service.listar(filtro, pageable)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody SolicitudRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
