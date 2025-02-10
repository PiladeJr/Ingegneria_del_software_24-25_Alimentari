package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.JwtService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UtenteController {
    private final UtenteService utenteService;
    private final JwtService jwtService;

    public UtenteController(UtenteService utenteService, JwtService jwtService) {
        this.utenteService = utenteService;
        this.jwtService = jwtService;
    }

    @GetMapping("/visualizzaUtenti")
    public ResponseEntity<List<UtenteDTO>> visualizzaUtenti() {
        return ResponseEntity.ok(utenteService.visualizzaUtenti());
    }

 /*   @PostMapping("/registrazione")
    public ResponseEntity<String> registraUtente(
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String telefono) {
        try {
            UtenteDTO utente = new UtenteDTO(nome, cognome, email, password, telefono, );
            utenteService.registraUtente(utente);
            return new ResponseEntity<>("Utente registrato correttamente", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Log dettagliato per identificare il problema
            return new ResponseEntity<>("Errore interno del server: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
*/
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> authenticatedUser() {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // Retrieve the current user details (UserDetails) from the authentication
        // object
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        // Generate a JWT token for the current user
        String token = jwtService.generateToken(currentUser);

        // Prepare the response containing the user details and the token
        Map<String, Object> response = new HashMap<>();
        response.put("user", currentUser);
        response.put("token", token);

        // Return the response with HTTP 200 OK status
        return ResponseEntity.ok(response);
    }
}
