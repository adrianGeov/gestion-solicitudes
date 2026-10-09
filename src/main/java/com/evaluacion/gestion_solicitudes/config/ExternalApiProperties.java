package com.evaluacion.gestion_solicitudes.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.jsonplaceholder")
public record ExternalApiProperties(
        String baseUrl,
        Duration connectTimeout,
        Duration readTimeout) {

}
