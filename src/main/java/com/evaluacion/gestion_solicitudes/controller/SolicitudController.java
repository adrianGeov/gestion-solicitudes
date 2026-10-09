package com.evaluacion.gestion_solicitudes.controller;

import com.evaluacion.gestion_solicitudes.dto.ErrorResponse;
import com.evaluacion.gestion_solicitudes.dto.PageResponse;
import com.evaluacion.gestion_solicitudes.dto.SolicitudFiltro;
import com.evaluacion.gestion_solicitudes.dto.SolicitudRequest;
import com.evaluacion.gestion_solicitudes.dto.SolicitudResponse;
import com.evaluacion.gestion_solicitudes.entity.EstatusSolicitud;
import com.evaluacion.gestion_solicitudes.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Tag(name = "Solicitudes", description = "Administración de solicitudes por centro de costo")
@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService service;

    @Operation(summary = "Crear solicitud",
            description = "Registra una nueva solicitud asociada a un centro de costo activo. "
                    + "Si no se envía estatus, se crea como PENDIENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "El centro de costo no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody SolicitudRequest request) {
        SolicitudResponse creada = service.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @Operation(summary = "Consultar solicitud por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
            @ApiResponse(responseCode = "400", description = "Id con formato inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No existe o está eliminada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtenerPorId(
            @Parameter(description = "Id de la solicitud", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar solicitudes con filtros",
            description = "Listado paginado. Filtros opcionales y combinables: título (parcial), estatus, "
                    + "centro de costo y rango de fechas. Ordenamiento con sort=campo,asc|desc.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de resultados"),
            @ApiResponse(responseCode = "400", description = "Parámetro con formato inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Rango de fechas inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<SolicitudResponse>> listar(
            @Parameter(description = "Filtro parcial por título", example = "laptops")
            @RequestParam(required = false) String titulo,

            @Parameter(description = "Filtro por estatus", example = "PENDIENTE")
            @RequestParam(required = false) EstatusSolicitud estatus,

            @Parameter(description = "Filtro por id de centro de costo", example = "1")
            @RequestParam(required = false) Long centroCostoId,

            @Parameter(description = "Fecha inicial (yyyy-MM-dd)", example = "2026-01-01")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,

            @Parameter(description = "Fecha final (yyyy-MM-dd)", example = "2026-12-31")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,

            @ParameterObject
            @PageableDefault(size = 10, sort = "fechaSolicitud", direction = Sort.Direction.DESC)
            Pageable pageable) {

        SolicitudFiltro filtro = new SolicitudFiltro(titulo, estatus, centroCostoId, fechaDesde, fechaHasta);
        return ResponseEntity.ok(PageResponse.from(service.listar(filtro, pageable)));
    }

    @Operation(summary = "Actualizar solicitud",
            description = "Actualiza los datos de una solicitud. Solo se permiten cambios en solicitudes PENDIENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "La solicitud o el centro de costo no existen",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "La solicitud no está en estatus PENDIENTE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudResponse> actualizar(
            @Parameter(description = "Id de la solicitud", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody SolicitudRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar solicitud (lógico)",
            description = "Marca la solicitud como inactiva. Deja de aparecer en consultas y listados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Solicitud eliminada"),
            @ApiResponse(responseCode = "404", description = "No existe o ya fue eliminada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id de la solicitud", example = "1")
            @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}