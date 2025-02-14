package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UtenteRepository utenteRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UtenteRepository utenteRepository, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.utenteRepository = utenteRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // .exceptionHandling(exception ->
                // exception.authenticationEntryPoint(authenticationEntryPoint()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/users/visualizzaUtenti").permitAll()
                        .requestMatchers("/api/richieste-collaborazione/azienda").permitAll()
                        .requestMatchers("/api/richieste-collaborazione/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // JWT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Disable frame options to allow H2 console to be displayed in a frame
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        return http.build();
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

    // @Bean
    // public AuthenticationEntryPoint authenticationEntryPoint() {
    // return (request, response, authException) -> {
    // authException.printStackTrace(); // Stampa l'errore vero in console
    // response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
    // authException.getMessage());
    // };
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /*
     * @Bean
     * public UserDetailsService userDetailsService() {
     * return username -> utenteRepository
     * .findByEmail(username)
     * .orElseThrow(() -> new
     * UsernameNotFoundException("User not found with email: " + username));
     * }
     */

    /*
     * @Bean
     * public DaoAuthenticationProvider authenticationProvider() {
     * DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
     * authProvider.setUserDetailsService(userDetailsService());
     * authProvider.setPasswordEncoder(passwordEncoder());
     * return authProvider;
     * }
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
