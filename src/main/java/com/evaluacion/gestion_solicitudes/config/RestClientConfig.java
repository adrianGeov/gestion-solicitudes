package com.evaluacion.gestion_solicitudes.config;

import java.net.http.HttpClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration 
@EnableConfigurationProperties (ExternalApiProperties.class)
public class RestClientConfig {

     @Bean
    public RestClient jsonPlaceholderRestClient(ExternalApiProperties props) {
        // Timeout de CONEXIÓN: cuánto esperar para establecer la conexión
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(props.connectTimeout())
                .build();

        // Timeout de LECTURA
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(props.readTimeout());

        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .requestFactory(requestFactory)
                .build();
    }


}
