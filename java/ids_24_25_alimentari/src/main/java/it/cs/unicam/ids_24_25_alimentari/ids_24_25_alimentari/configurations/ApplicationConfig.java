// package
// it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.configurations;

// import
// it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import
// org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import
// org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// public class ApplicationConfig {
// private final UtenteRepository utenteRepository;

// public ApplicationConfig(UtenteRepository utenteRepository) {
// this.utenteRepository = utenteRepository;
// }

// @Bean
// PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }

// @Bean
// public UserDetailsService userDetailsService() {
// return username -> utenteRepository
// .findByEmail(username)
// .orElseThrow(() -> new UsernameNotFoundException(
// "User not found with email: " + username));
// }

// // @Bean
// // public AuthenticationManager authenticationManager(
// // AuthenticationConfiguration config
// // ) throws Exception {
// // return config.getAuthenticationManager();
// // }

// @Bean
// public AuthenticationProvider authenticationProvider() {
// DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
// authProvider.setUserDetailsService(userDetailsService());
// authProvider.setPasswordEncoder(passwordEncoder());
// return authProvider;
// }

// @Bean
// public AuthenticationManager
// authenticationManager(AuthenticationConfiguration config) throws Exception {
// return config.getAuthenticationManager();
// }
// }
