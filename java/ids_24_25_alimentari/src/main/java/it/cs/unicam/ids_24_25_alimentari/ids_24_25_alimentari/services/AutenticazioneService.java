package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.LoginUserDto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteRegistrazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.UtenteBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutenticazioneService {
    private final UtenteBuilder builder = new UtenteBuilder();
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AutenticazioneService(
            UtenteRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.utenteRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    private void credenzialiBase(String nome, String cognome, String email, String password, String telefono) {
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciEmail(email);
        builder.costruisciPassword(password);
        builder.costruisciTelefono(telefono);
    }

    public Utente registrazione(@Valid UtenteRegistrazioneDTO input) {
        // Check if user already exists
        if (utenteRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "User already exists with email: " + input.getEmail());
        }
        // Create and save the new user
        String hashedPassword = passwordEncoder.encode(input.getPassword());
        Utente utente = new Utente();
        utente.setNome(input.getNome());
        utente.setCognome(input.getCognome());
        utente.setTelefono(input.getTelefono());
        utente.setEmail(input.getEmail());
        utente.setPassword(hashedPassword);
        utente.setRuolo(input.getRuolo());
        return utenteRepository.save(utente);
    }

    public Utente authenticate(LoginUserDto input) {
        Utente user = utenteRepository
                .findByEmail(input.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email not found: " + input.getEmail()));

        // Check if password is correct
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Incorrect password for email: " + input.getEmail());
        }

        // Perform authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));

        return user; // Return authenticated user
    }

    public List<Utente> allUsers() {
        return utenteRepository.findAll();
    }
}
