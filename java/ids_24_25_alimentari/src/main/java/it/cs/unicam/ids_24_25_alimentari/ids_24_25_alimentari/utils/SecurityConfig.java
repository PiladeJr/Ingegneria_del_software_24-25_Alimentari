package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig{
        @Autowired
        TokenConfig token;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            http
                    .authorizeHttpRequests(request -> {
                        request.requestMatchers("/register", "/login").permitAll();
                        request.anyRequest().authenticated();
                    })
                    .csrf(AbstractHttpConfigurer::disable)
                    .addFilterAfter(token, BasicAuthenticationFilter.class);
            return http.build();
        }

    }
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


