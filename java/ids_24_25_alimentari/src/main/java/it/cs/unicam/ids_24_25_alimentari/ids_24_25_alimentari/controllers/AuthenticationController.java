package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.login.LoginResponseDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.login.LoginUserDto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.UtenteRegistrazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.AutenticazioneService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.JwtService;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final AutenticazioneService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(
            JwtService jwtService/* add */,
            AutenticazioneService authenticationService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;/* add */
    }
    /**
     * <h2> Registra un nuovo utente </h2>
     * <br>
     * Questo metodo gestisce una richiesta HTTP POST per registrare un nuovo utente nel sistema.
     * I dati dell'utente vengono validati e passati al servizio di autenticazione
     * per completare la registrazione.
     * Se la registrazione ha successo, restituisce lo stato HTTP 200 (OK) con i dettagli dell'utente.
     *
     * @param registerUserDto L'oggetto {@code UtenteRegistrazioneDTO} contenente i dati dell'utente da registrare.
     * @return {@code ResponseEntity} con i dettagli dell'utente registrato e stato HTTP 200.
     */
    @PostMapping("/signup")
    public ResponseEntity<Utente> register(
            @Valid @RequestBody UtenteRegistrazioneDTO registerUserDto) {
        Utente registeredUser = authenticationService.registrazione(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
    /**
     * <h2>Autentica un utente e genera un token JWT</h2>
     * <br>
     * Questo metodo gestisce una richiesta HTTP POST per autenticare un utente nel sistema.
     * Se l'autenticazione ha successo, genera un token JWT e restituisce una risposta
     * contenente il token e il tempo di scadenza.
     *
     * @param loginUserDto L'oggetto {@code LoginUserDto} contenente le credenziali dell'utente.
     * @return {@code ResponseEntity} con il token JWT e il tempo di scadenza se l'autenticazione ha successo.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(
            @RequestBody LoginUserDto loginUserDto) {
        Utente authenticatedUser = authenticationService.authenticate(loginUserDto);

        // Generate JWT token for the authenticated user
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponseDTO loginResponse = new LoginResponseDTO()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
    /**
     * <h2>Gestisce le eccezioni di validazione</h2>
     * <br>
     * Questo metodo intercetta le eccezioni {@code MethodArgumentNotValidException}
     * generate dalla validazione degli input nei controller.
     * Raccoglie tutti gli errori di validazione e li restituisce in una mappa,
     * dove la chiave è il nome del campo e il valore è il messaggio di errore associato.
     *
     * @param ex L'eccezione {@code MethodArgumentNotValidException} catturata.
     * @return {@code ResponseEntity} contenente una mappa con i campi non validi e i relativi messaggi di errore,
     *         con stato HTTP 400 (BAD REQUEST).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex
                .getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
