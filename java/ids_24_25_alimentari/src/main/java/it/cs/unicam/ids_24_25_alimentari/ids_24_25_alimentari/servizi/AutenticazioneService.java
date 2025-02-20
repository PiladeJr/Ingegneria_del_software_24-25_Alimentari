package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.login.LoginUserDto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.UtenteRegistrazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticazioneService {
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
    /**
     * <h2>Registra un nuovo utente</h2>
     * <br>
     * Questo metodo crea e salva un nuovo utente nel database dopo aver verificato
     * che non esista già un utente con la stessa email.
     * Se un utente con la stessa email è già registrato, viene lanciata un'eccezione.
     *
     * @param input L'oggetto {@code UtenteRegistrazioneDTO} contenente i dati dell'utente da registrare.
     * @return L'oggetto {@code Utente} appena creato e salvato nel database.
     * @throws IllegalArgumentException Se esiste già un utente con la stessa email.
     */
    public Utente registrazione(@Valid UtenteRegistrazioneDTO input) {
        //controllo se l'utente esiste
        if (utenteRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "User already exists with email: " + input.getEmail());
        }
        // crea e salva un nuovo utente
        Utente utente = new Utente();
        utente.setNome(input.getNome());
        utente.setCognome(input.getCognome());
        utente.setTelefono(input.getTelefono());
        utente.setEmail(input.getEmail());
        utente.setPassword(input.getPassword());
        utente.setRuolo(Ruolo.ACQUIRENTE);
        return utenteRepository.save(utente);
    }

    /**
     * <h2>Autentica un utente</h2>
     * <br>
     * Questo metodo verifica le credenziali di un utente confrontando l'email e la password
     * con quelle salvate nel database.
     * Se l'email non è registrata o la password è errata, viene lanciata un'eccezione.
     * Se l'autenticazione ha successo, restituisce l'utente autenticato.
     *
     * @param input L'oggetto {@code LoginUserDto} contenente le credenziali dell'utente.
     * @return L'oggetto {@code Utente} autenticato.
     * @throws IllegalArgumentException Se l'email non esiste o la password è errata.
     */
    public Utente authenticate(LoginUserDto input) {
        Utente user = utenteRepository
                .findByEmail(input.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email not found: " + input.getEmail()));

        // controlla che la password sia corretta
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Incorrect password for email: " + input.getEmail());
        }

        // effettua l'autenticazione
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));

        return user;
    }
}
