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
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.LoginResponseDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.LoginUserDto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteRegistrazioneDTO;
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

    @PostMapping("/signup")
    public ResponseEntity<Utente> register(
            @Valid @RequestBody UtenteRegistrazioneDTO registerUserDto) {
        Utente registeredUser = authenticationService.registrazione(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    // Endpoint for user login
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
