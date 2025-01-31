package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;


import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/auth/**")
                                .permitAll()
                                .requestMatchers("/h2-console/**")
                                .permitAll() // Allow access to H2 console
                                .requestMatchers("/users/me")
                                .permitAll() // Allow access to /me without authentication checks
                                .requestMatchers("/users/all")
                                .permitAll() // Allow access to /all without authentication checks
                                .requestMatchers("/users/*")
                                .permitAll() // Allow access to /users/{id} without authentication checks
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider);

        // Optionally add your JWT filter here if needed
        // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Disable frame options to allow H2 console to be displayed in a frame
        //http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080")); // Consider making this configurable
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}













//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//
//@Configuration
//@EnableWebSecurity(debug = true)
//public class SecurityConfig{
//        @Autowired
//        TokenConfig token;
//
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//            http
//                    .authorizeHttpRequests(request -> {
//                        request.requestMatchers("/register", "/login").permitAll();
//                        request.anyRequest().authenticated();
//                    })
//                    .csrf(AbstractHttpConfigurer::disable)
//                    .addFilterAfter(token, BasicAuthenticationFilter.class);
//            return http.build();
//        }
//
//    }
  /*  @Lazy
    private final UtenteService userDetailsService;

    public SecurityConfig(UtenteService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users").hasAnyRole("ACQUIRENTE","PRODUTTORE","TRASFORMATORE","DISTRIBUTORE","ANIMATORE","CURATORE")
                        .requestMatchers("/api/admin").hasRole("GESTORE")
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .usernameParameter("email")
                        .defaultSuccessUrl("/users")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }*/


