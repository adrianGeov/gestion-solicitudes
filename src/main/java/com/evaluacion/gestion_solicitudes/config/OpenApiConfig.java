package com.evaluacion.gestion_solicitudes.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;

@Configuration 
public class OpenApiConfig {

     private static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestión de Solicitudes y Centros de Costo")
                        .description("API REST para administrar centros de costo y sus solicitudes, "
                                + "con paginación, filtros dinámicos y consumo de servicio externo.\n\n"
                                + "**Autenticación:** obtenga un token en `POST /api/v1/auth/login` "
                                + "y péguelo en el botón **Authorize**.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Adrián Tolentino")
                                .email("adriant.masariego96@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

}
