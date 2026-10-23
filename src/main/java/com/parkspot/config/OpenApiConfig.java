package com.parkspot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration for interactive API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI parkSpotOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ParkSpot API")
                        .description("Society Visitor Parking Slot Tracker — "
                                + "Log visitor vehicles, manage parking slots, "
                                + "and generate daily reports.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ParkSpot Team")
                                .email("admin@parkspot.com")));
    }
}
