package com.evaluacion.gestion_solicitudes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema (description = "Credenciales de acceso")
public record LoginRequest(
    
        @Schema(example = "admin")
        @NotBlank(message = "El usuario es obligatorio")
        String username,

        @Schema(example = "Admin123*")
        @NotBlank(message = "La contraseña es obligatoria")
        String password) {

}
