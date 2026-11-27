package com.ecotrack.report.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecoTrackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EcoTrack Report Service API")
                        .version("1.0.0")
                        .description(
                                "REST API for managing urban waste reports "
                                + "and collection points."
                        )
                );
    }
}