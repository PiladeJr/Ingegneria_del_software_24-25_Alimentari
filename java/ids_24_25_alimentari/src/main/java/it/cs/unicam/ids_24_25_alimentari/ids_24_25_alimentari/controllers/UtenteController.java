package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
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

    @GetMapping()
    public ResponseEntity<?> visualizzaUtenti() {
        try {
            List<UtenteDTO> utenti = utenteService.visualizzaUtenti();
            return ResponseEntity.ok(utenti);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Errore nel recuperare gli utenti.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Utente utente = utenteService.selezionaUtenteById(id).orElse(null);
            if (utente == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Utente non trovato.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(utente);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Errore nel recuperare l'utente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> authenticatedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails currentUser = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(currentUser);
            Map<String, Object> response = new HashMap<>();
            response.put("user", currentUser);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Errore durante l'autenticazione.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
