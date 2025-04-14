package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.JwtService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
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

    /**
     * <h2>Recupera e restituisce la lista degli utenti registrati</h2>
     * <br/>
     * Questo metodo gestisce una richiesta HTTP GET per ottenere l'elenco di tutti gli utenti.
     * Se la richiesta va a buon fine, restituisce un {@code ResponseEntity} con la lista
     * degli utenti e lo status HTTP 200 (OK). In caso di errore, restituisce uno stato
     * HTTP 500 (INTERNAL_SERVER_ERROR) con un messaggio di errore.
     *
     *
     * @return {@code ResponseEntity} contenente la lista di {@code UtenteDTO} se la richiesta ha successo,
     *         altrimenti un messaggio di errore con stato HTTP 500.
     */
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
    /**
     * <h2> Recupera un utente tramite ID </h2>
     * <br>
     * Questo metodo gestisce una richiesta HTTP GET per ottenere i dettagli di un utente
     * specifico in base all'ID fornito.
     * Se l'utente esiste, restituisce un {@code ResponseEntity} con i suoi dati e lo stato HTTP 200 (OK).
     * Se l'utente non viene trovato, restituisce uno stato HTTP 404 (NOT FOUND).
     * In caso di errore, restituisce uno stato HTTP 500 (INTERNAL_SERVER_ERROR) con un messaggio di errore.
     *
     * @param id L'ID dell'utente da recuperare.
     * @return {@code ResponseEntity} con i dettagli dell'utente se trovato,
     *         oppure un messaggio di errore con stato HTTP 404 o 500.
     */
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

    /**
     * <h2> Restituisce i dettagli dell'utente autenticato </h2>.
     * <br>
     * Questo metodo recupera l'utente attualmente autenticato dal contesto di sicurezza
     * e genera un token JWT per l'autenticazione.
     * Se la richiesta va a buon fine, restituisce un {@code ResponseEntity} contenente
     * i dettagli dell'utente e il token. In caso di errore, restituisce uno stato
     * HTTP 500 (INTERNAL_SERVER_ERROR) con un messaggio di errore.
     *
     * @return {@code ResponseEntity} con i dettagli dell'utente autenticato e il token JWT,
     *         oppure un messaggio di errore con stato HTTP 500.
     */
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
