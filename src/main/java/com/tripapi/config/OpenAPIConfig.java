package com.tripapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI tripApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TRIPapi - Travel Routes & Itinerary Planner")
                        .description("API for managing destinations, trips, itinerary days, budgets, and activities")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Salvatore Marchese")
                                .email("salvatore@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")));
    }

    // --- Groups for Swagger UI ---
    @Bean
    public GroupedOpenApi destinationsGroup() {
        return GroupedOpenApi.builder()
                .group("Destinations")
                .pathsToMatch("/api/destinations/**")
                .build();
    }

    @Bean
    public GroupedOpenApi tripsGroup() {
        return GroupedOpenApi.builder()
                .group("Trips")
                .pathsToMatch("/api/trips/**")
                .build();
    }

    @Bean
    public GroupedOpenApi itineraryDaysGroup() {
        return GroupedOpenApi.builder()
                .group("Itinerary Days")
                .pathsToMatch("/api/itinerary-days/**")
                .build();
    }

    @Bean
    public GroupedOpenApi budgetsGroup() {
        return GroupedOpenApi.builder()
                .group("Budgets")
                .pathsToMatch("/api/budgets/**")
                .build();
    }

    @Bean
    public GroupedOpenApi activitiesGroup() {
        return GroupedOpenApi.builder()
                .group("Activities")
                .pathsToMatch("/api/activities/**")
                .build();
    }
}

