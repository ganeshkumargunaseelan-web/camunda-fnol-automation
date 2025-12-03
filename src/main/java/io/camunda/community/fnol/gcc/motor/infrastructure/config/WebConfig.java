/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FnolProperties fnolProperties;

    public WebConfig(FnolProperties fnolProperties) {
        this.fnolProperties = fnolProperties;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        String origins = fnolProperties.security().cors().allowedOrigins();
        if ("*".equals(origins)) {
            // SECURITY: Wildcard origins cannot use credentials
            config.setAllowedOriginPatterns(List.of("*"));
            config.setAllowCredentials(false);
        } else {
            // Specific origins can use credentials safely
            config.setAllowedOrigins(Arrays.asList(origins.split(",")));
            config.setAllowCredentials(true);
        }

        String methods = fnolProperties.security().cors().allowedMethods();
        config.setAllowedMethods(Arrays.asList(methods.split(",")));

        config.setAllowedHeaders(List.of(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "X-API-Key",
                "X-Idempotency-Key",
                "X-Correlation-ID"
        ));

        config.setExposedHeaders(List.of(
                "X-Correlation-ID",
                "X-RateLimit-Remaining",
                "Retry-After"
        ));

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }

    /**
     * Configure static resource handlers for the React SPA.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Static assets
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(31536000); // 1 year cache for assets

        // Favicon
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico")
                .setCachePeriod(86400); // 1 day cache

        // All other static files
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0); // No cache for HTML
    }

    /**
     * Configure view controllers for SPA routing.
     * All non-API routes should serve the React app's index.html.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward SPA routes to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/fnol").setViewName("forward:/index.html");
        registry.addViewController("/fnol/**").setViewName("forward:/index.html");
        registry.addViewController("/confirmation").setViewName("forward:/index.html");
        registry.addViewController("/confirmation/**").setViewName("forward:/index.html");
    }
}
