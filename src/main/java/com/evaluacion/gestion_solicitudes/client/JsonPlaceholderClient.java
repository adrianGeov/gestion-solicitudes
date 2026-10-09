package com.evaluacion.gestion_solicitudes.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.evaluacion.gestion_solicitudes.dto.UsuarioExternoResponse;
import com.evaluacion.gestion_solicitudes.exception.ExternalServiceException;
import com.evaluacion.gestion_solicitudes.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonPlaceholderClient {

    private final RestClient jsonPlaceholderRestClient;

    public UsuarioExternoResponse obtenerUsuario(Long id) {
        log.info("Consultando usuario externo con id {}", id);
        try {
            return jsonPlaceholderRestClient.get()
                    .uri("/users/{id}", id)
                    .retrieve()
                    .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(), (req, res) -> {
                        throw new ResourceNotFoundException("Usuario externo", id);
                    })
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new ExternalServiceException(
                                "El servicio externo respondió con error HTTP " + res.getStatusCode().value());
                    })
                    .body(UsuarioExternoResponse.class);

        } catch (ResourceAccessException ex) {
            // Timeout de conexión/lectura, host caído, sin red, etc.
            log.error("No se pudo conectar con el servicio externo: {}", ex.getMessage());
            throw new ExternalServiceException("El servicio externo no está disponible o tardó demasiado en responder",
                    ex);
        } catch (RestClientException ex) {
            // Respuesta que no se pudo procesar (ej. JSON inesperado)
            log.error("Error al procesar respuesta del servicio externo: {}", ex.getMessage());
            throw new ExternalServiceException("Respuesta inválida del servicio externo", ex);
        }
    }

    public List<UsuarioExternoResponse> obtenerUsuarios() {
        log.info("Consultando listado de usuarios externos");
        try {
            List<UsuarioExternoResponse> usuarios = jsonPlaceholderRestClient.get()
                    .uri("/users")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new ExternalServiceException(
                                "El servicio externo respondió con error HTTP " + res.getStatusCode().value());
                    })
                    .body(new ParameterizedTypeReference<List<UsuarioExternoResponse>>() {
                    });
            return usuarios != null ? usuarios : List.of();

        } catch (ResourceAccessException ex) {
            log.error("No se pudo conectar con el servicio externo: {}", ex.getMessage());
            throw new ExternalServiceException("El servicio externo no está disponible o tardó demasiado en responder",
                    ex);
        } catch (RestClientException ex) {
            log.error("Error al procesar respuesta del servicio externo: {}", ex.getMessage());
            throw new ExternalServiceException("Respuesta inválida del servicio externo", ex);
        }
    }

}
