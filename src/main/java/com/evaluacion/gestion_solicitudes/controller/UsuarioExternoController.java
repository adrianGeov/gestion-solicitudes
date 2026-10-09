package com.evaluacion.gestion_solicitudes.controller;

import com.evaluacion.gestion_solicitudes.dto.ErrorResponse;
import com.evaluacion.gestion_solicitudes.dto.UsuarioExternoResponse;
import com.evaluacion.gestion_solicitudes.service.UsuarioExternoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Usuarios Externos", description = "Consulta de usuarios al servicio externo JSONPlaceholder")
@RestController
@RequestMapping("/api/v1/usuarios-externos")
@RequiredArgsConstructor
public class UsuarioExternoController {

    private final UsuarioExternoService service;

    @Operation(summary = "Listar usuarios externos",
            description = "Obtiene el listado de usuarios desde JSONPlaceholder. "
                    + "Timeout de conexión: 3s, timeout de lectura: 5s.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @ApiResponse(responseCode = "503", description = "Servicio externo no disponible o sin respuesta",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioExternoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Consultar usuario externo por id",
            description = "Obtiene un usuario desde JSONPlaceholder.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "El usuario no existe en el servicio externo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio externo no disponible o sin respuesta",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioExternoResponse> obtenerPorId(
            @Parameter(description = "Id del usuario externo", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}