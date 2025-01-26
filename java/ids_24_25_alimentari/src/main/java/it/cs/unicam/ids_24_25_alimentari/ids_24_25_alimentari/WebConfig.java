package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Consente CORS per tutte le route
                        .allowedOrigins("http://localhost:3000", "https://example.com") // Origini consentite
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Metodi consentiti
                        .allowedHeaders("*") // Header consentiti
                        .allowCredentials(true); // Consente i cookie
            }
        };
    }
}
