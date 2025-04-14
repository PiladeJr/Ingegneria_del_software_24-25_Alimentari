package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.configurazioni;

import java.io.IOException;
import java.util.List;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.autenticazione.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/richieste-collaborazione/azienda").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/richieste-collaborazione/animatore").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/richieste-collaborazione/curatore").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/richieste-collaborazione/stato")
                        .hasAnyAuthority("ROLE_GESTORE")
                        .requestMatchers("/api/richieste-collaborazione/**").hasAnyAuthority("ROLE_GESTORE")
                        .requestMatchers(HttpMethod.GET, "/api/azienda/{id}")
                        .hasAnyAuthority("ROLE_GESTORE", "ROLE_CURATORE")
                        .requestMatchers("/api/azienda/roles/**")
                        .hasAnyAuthority("ROLE_GESTORE", "ROLE_CURATORE", "ROLE_TRASFORMATORE")
                        .requestMatchers(HttpMethod.DELETE, "/api/azienda/{id}").hasAuthority("ROLE_GESTORE")
                        .requestMatchers("/api/azienda/informazioni/new")
                        .hasAnyAuthority("ROLE_PRODUTTORE", "ROLE_TRASFORMATORE",
                                "ROLE_GESTORE", "ROLE_CURATORE")
                        .requestMatchers("/api/azienda/informazioni/new")
                        .hasAnyAuthority("ROLE_PRODUTTORE", "ROLE_TRASFORMATORE")
                        .requestMatchers("/api/azienda/**").hasAnyAuthority("ROLE_GESTORE", "ROLE_CURATORE")
                        .requestMatchers("/api/users").hasAnyAuthority("ROLE_GESTORE")
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_GESTORE")
                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                                    "Accesso negato: autenticazione richiesta");
                        }));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    // Metodo per inviare errori JSON
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080")); // Consider making this configurable
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
