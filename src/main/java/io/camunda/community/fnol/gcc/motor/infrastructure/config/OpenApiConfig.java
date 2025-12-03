/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private final FnolProperties fnolProperties;

    public OpenApiConfig(FnolProperties fnolProperties) {
        this.fnolProperties = fnolProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Current Server")
                ));

        // Add API Key security scheme if enabled
        if (fnolProperties.security().apiKey().enabled()) {
            openAPI.components(new Components()
                            .addSecuritySchemes("ApiKeyAuth", new SecurityScheme()
                                    .type(SecurityScheme.Type.APIKEY)
                                    .in(SecurityScheme.In.HEADER)
                                    .name(fnolProperties.security().apiKey().headerName())
                                    .description("API Key for authentication")))
                    .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"));
        }

        return openAPI;
    }

    private Info apiInfo() {
        return new Info()
                .title("GCC Motor FNOL API")
                .description("""
                        REST API for Motor Insurance First Notice of Loss (FNOL) submission
                        in GCC countries (UAE, Saudi Arabia, Qatar, Bahrain, Kuwait, Oman).

                        ## Features
                        - Multilingual support (Arabic, English, Hindi, Urdu, Malayalam, Tagalog)
                        - GCC-specific validation (MSISDN, National ID, Plate Numbers)
                        - Arabic text normalization
                        - Camunda 8 process integration

                        ## Authentication
                        When API key authentication is enabled, include the API key in the request header.

                        ## Rate Limiting
                        API requests are rate limited. Check `X-RateLimit-Remaining` header.
                        """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("G. Ganesh Kumar")
                        .url("https://github.com/camunda-community-hub/gcc-motor-fnol-starter"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }
}
