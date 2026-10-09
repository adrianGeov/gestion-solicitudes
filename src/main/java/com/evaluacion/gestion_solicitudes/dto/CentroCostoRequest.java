package com.evaluacion.gestion_solicitudes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema (description = "Datos para crear o actualizar un centro de costo")
public record CentroCostoRequest(

        @NotBlank(message = "El código es obligatorio") @Size(max = 20, message = "El código no debe exceder 20 caracteres") String codigo,

        @NotBlank(message = "El nombre es obligatorio") @Size(max = 100, message = "El nombre no debe exceder 100 caracteres") String nombre,

        @Size(max = 255, message = "La descripción no debe exceder 255 caracteres") String descripcion) {
}
