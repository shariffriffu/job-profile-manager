package com.jobmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Profile Manager (JPM) API")
                        .version("v1.0")
                        .description("Centralised Career Profile Management Platform API. " +
                                "Maintains a single source of truth for professional profiles, " +
                                "supports AI-assisted career optimisation and multi-platform synchronisation.")
                        .contact(new Contact()
                                .name("JPM Architecture Team")
                                .email("architect@jobmanager.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Local Development Server")
                ));
    }
}
