package com.evaluacion.gestion_solicitudes.config;


import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;

@Configuration 
public class OpenApiConfig {

     @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestión de Solicitudes y Centros de Costo")
                        .description("API REST para administrar centros de costo y sus solicitudes, "
                                + "con paginación, filtros dinámicos y consumo de servicio externo.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Adrián Tolentino")
                                .email("adriant.masariego96@gmail.com")));
    }

}
