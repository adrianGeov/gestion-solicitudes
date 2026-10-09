package com.evaluacion.gestion_solicitudes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.evaluacion.gestion_solicitudes.entity.EstatusSolicitud;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema (description = "Datos para crear o actualizar una solicitud")
public record SolicitudRequest(

        @NotBlank(message = "El título es obligatorio") @Size(max = 150, message = "El título no debe exceder 150 caracteres") String titulo,

        @Size(max = 500, message = "La descripción no debe exceder 500 caracteres") String descripcion,

        @NotNull(message = "El monto es obligatorio") @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0") @Digits(integer = 10, fraction = 2, message = "El monto admite máximo 10 enteros y 2 decimales") BigDecimal monto,

        @NotNull(message = "La fecha de solicitud es obligatoria") LocalDate fechaSolicitud,

        @NotNull(message = "El centro de costo es obligatorio") @Positive(message = "El id del centro de costo debe ser positivo") Long centroCostoId,

        EstatusSolicitud estatus) {

}
