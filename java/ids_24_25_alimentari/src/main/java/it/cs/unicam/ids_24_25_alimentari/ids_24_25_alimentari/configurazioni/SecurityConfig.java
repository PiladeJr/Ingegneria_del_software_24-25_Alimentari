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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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

        public static final String ROLE_GESTORE = "ROLE_GESTORE";
        public static final String ROLE_CURATORE = "ROLE_CURATORE";
        public static final String ROLE_PRODUTTORE = "ROLE_PRODUTTORE";
        public static final String ROLE_TRASFORMATORE = "ROLE_TRASFORMATORE";
        public static final String ROLE_DISTRIBUTORE = "ROLE_DISTRIBUTORE";
        public static final String ROLE_ANIMATORE = "ROLE_ANIMATORE";
        public static final String[] ENDPOINT_AUTH = { "/api/auth/**", "/h2-console/**" };
        public static final String[] ENDPOINT_ACCESSO_LIBERO = {
                        "/api/richieste-collaborazione/azienda",
                        "/api/richieste-collaborazione/animatore",
                        "/api/richieste-collaborazione/curatore",
                        "/api/carrelli"
        };

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        /**
         * Configura l’intera catena di filtri di sicurezza per la piattaforma, basata
         * su JWT, personalizzando eccezioni e autorizzazioni.
         *
         * @param http l'oggetto HttpSecurity da configurare
         * @return il SecurityFilterChain configurato
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                                                                        "Accesso negato: autenticazione richiesta");
                                                }))
                                .authorizeHttpRequests(this::configureAuthorization)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .headers(headers -> headers
                                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

                return http.build();
        }

        private void configureAuthorization(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                // Accesso libero
                                .requestMatchers(SecurityConfig.ENDPOINT_AUTH).permitAll()
                                .requestMatchers(HttpMethod.POST, SecurityConfig.ENDPOINT_ACCESSO_LIBERO).permitAll();

                collaborazioniAuth(auth);
                aziendaAuth(auth);
                utenteAuth(auth);
                eventiAuth(auth);
                prodottoAuth(auth);
                paypal(auth);
                richiestaContenutoAuth(auth);

                // Tutto il resto: autenticazione richiesta
                auth.anyRequest().authenticated();
        }

        private void collaborazioniAuth(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                .requestMatchers(HttpMethod.PATCH, "/api/richieste-collaborazione/{id}/stato")
                                .hasAuthority(SecurityConfig.ROLE_GESTORE)
                                .requestMatchers("/api/richieste-collaborazione/**")
                                .hasAuthority(SecurityConfig.ROLE_GESTORE);
        }

        private void paypal(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                .requestMatchers(HttpMethod.GET, "/api/paypal/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/paypal/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/paypal/**").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/paypal/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/paypal/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/api/paypal/**").permitAll();
        }

        private void aziendaAuth(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                .requestMatchers(HttpMethod.GET, "/api/azienda/informazioni")
                                .hasAnyAuthority(SecurityConfig.ROLE_PRODUTTORE, SecurityConfig.ROLE_TRASFORMATORE)

                                .requestMatchers(HttpMethod.GET, "/api/azienda/roles/**")
                                .hasAnyAuthority(SecurityConfig.ROLE_GESTORE, SecurityConfig.ROLE_CURATORE, SecurityConfig.ROLE_TRASFORMATORE)

                                .requestMatchers(HttpMethod.GET, "/api/azienda/{id}")
                                .hasAnyAuthority(SecurityConfig.ROLE_GESTORE, SecurityConfig.ROLE_CURATORE)
                                .requestMatchers(HttpMethod.DELETE, "/api/azienda/{id}")
                                .hasAuthority(SecurityConfig.ROLE_GESTORE)

                                .requestMatchers("/api/azienda/**")
                                .hasAnyAuthority(SecurityConfig.ROLE_GESTORE, SecurityConfig.ROLE_CURATORE);
        }

        private void utenteAuth(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                .requestMatchers("/api/users").hasAuthority(SecurityConfig.ROLE_GESTORE)
                                .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                                .requestMatchers("/api/users/*/indirizzo-spedizione").authenticated()
                                .requestMatchers("/api/users/*/indirizzo-fatturazione").authenticated()
                                .requestMatchers("/api/users/**").hasAuthority(SecurityConfig.ROLE_GESTORE);

        }

        private void eventiAuth(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                .requestMatchers(HttpMethod.GET, "/api/eventi/gestore/**")
                                .hasAuthority(SecurityConfig.ROLE_GESTORE)
                                .requestMatchers(HttpMethod.GET, "/api/eventi/miei/**")
                                .hasAuthority(SecurityConfig.ROLE_ANIMATORE)
                                .requestMatchers(HttpMethod.GET,
                                                "/api/eventi/preview/**",
                                                "/api/eventi/programmati/**")
                                .permitAll();
        }

        private void prodottoAuth(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                .requestMatchers(HttpMethod.GET, "/api/prodotto/visualizza/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/prodotto/delete/**")
                                .hasAnyAuthority(SecurityConfig.ROLE_CURATORE);
        }

        private void richiestaContenutoAuth(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth
                                .requestMatchers(HttpMethod.GET,
                                                "/api/richieste-contenuto/visualizza/**")
                                .hasAnyAuthority(SecurityConfig.ROLE_CURATORE)
                                .requestMatchers(HttpMethod.PATCH,
                                                "/api/richieste-contenuto/valuta")
                                .hasAnyAuthority(SecurityConfig.ROLE_CURATORE)
                                .requestMatchers(HttpMethod.POST, "/api/richieste-contenuto/informazioni/produttore/new")
                                .hasAuthority(SecurityConfig.ROLE_PRODUTTORE)
                                .requestMatchers(HttpMethod.POST, "/api/richieste-contenuto/informazioni/trasformatore/new")
                                .hasAuthority(SecurityConfig.ROLE_TRASFORMATORE)
                                .requestMatchers(HttpMethod.POST,
                                                "/api/richieste-contenuto/prodotto/singolo/new")
                                .hasAnyAuthority(SecurityConfig.ROLE_PRODUTTORE, SecurityConfig.ROLE_TRASFORMATORE, SecurityConfig.ROLE_DISTRIBUTORE)
                                .requestMatchers(HttpMethod.POST,
                                                "/api/richieste-contenuto/prodotto/pacchetto/new")
                                .hasAuthority(SecurityConfig.ROLE_DISTRIBUTORE)
                                .requestMatchers(HttpMethod.POST,
                                                "/api/richieste-contenuto/eventi/new",
                                                "/api/richieste-contenuto/visita/new")
                                .hasAnyAuthority(SecurityConfig.ROLE_ANIMATORE);
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
