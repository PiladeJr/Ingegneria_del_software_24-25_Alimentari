package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.JwtService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * Questo metodo gestisce una richiesta HTTP GET per ottenere l'elenco di tutti
     * gli utenti.
     * Se la richiesta va a buon fine, restituisce un {@code ResponseEntity} con la
     * lista
     * degli utenti e lo status HTTP 200 (OK). In caso di errore, restituisce uno
     * stato
     * HTTP 500 (INTERNAL_SERVER_ERROR) con un messaggio di errore.
     *
     *
     * @return {@code ResponseEntity} contenente la lista di {@code UtenteDTO} se la
     *         richiesta ha successo,
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
     * <h2>Recupera un utente tramite ID</h2>
     * <br>
     * Questo metodo gestisce una richiesta HTTP GET per ottenere i dettagli di un
     * utente
     * specifico in base all'ID fornito.
     * Se l'utente esiste, restituisce un {@code ResponseEntity} con i suoi dati e
     * lo stato HTTP 200 (OK).
     * Se l'utente non viene trovato, restituisce uno stato HTTP 404 (NOT FOUND).
     * In caso di errore, restituisce uno stato HTTP 500 (INTERNAL_SERVER_ERROR) con
     * un messaggio di errore.
     *
     * @param id L'ID dell'utente da recuperare.
     * @return {@code ResponseEntity} con i dettagli dell'utente se trovato,
     *         oppure un messaggio di errore con stato HTTP 404 o 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<Utente> utente = utenteService.getUtenteById(id);
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
     * <h2>Restituisce i dettagli dell'utente autenticato</h2>.
     * <br>
     * Questo metodo recupera l'utente attualmente autenticato dal contesto di
     * sicurezza
     * e genera un token JWT per l'autenticazione.
     * Se la richiesta va a buon fine, restituisce un {@code ResponseEntity}
     * contenente
     * i dettagli dell'utente e il token. In caso di errore, restituisce uno stato
     * HTTP 500 (INTERNAL_SERVER_ERROR) con un messaggio di errore.
     *
     * @return {@code ResponseEntity} con i dettagli dell'utente autenticato e il
     *         token JWT,
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

    /**
     * <h2>Aggiunge un indirizzo di spedizione all'utente</h2>
     * <br>
     * Questo metodo gestisce una richiesta HTTP POST per aggiungere un indirizzo
     * di spedizione a un utente specifico identificato dall'ID.
     * Se la richiesta va a buon fine, restituisce uno stato HTTP 200 (OK).
     * In caso di errore, restituisce uno stato HTTP 500 (INTERNAL_SERVER_ERROR)
     * con un messaggio di errore.
     *
     * @param idUtente  l'ID dell'utente a cui aggiungere l'indirizzo
     * @param indirizzo l'indirizzo di spedizione da aggiungere
     * @return {@code ResponseEntity} con messaggio di successo o errore
     */
    @PostMapping("/{idUtente}/indirizzo-spedizione")
    public ResponseEntity<?> aggiungiIndirizzoSpedizione(@PathVariable Long idUtente,
            @RequestBody Indirizzo indirizzo) {
        try {
            utenteService.aggiungiIndirizzoSpedizione(idUtente, indirizzo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Indirizzo di spedizione aggiunto con successo.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Errore nell'aggiunta dell'indirizzo di spedizione.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * <h2>Aggiunge un indirizzo di fatturazione all'utente</h2>
     * <br>
     * Questo metodo gestisce una richiesta HTTP POST per aggiungere un indirizzo
     * di fatturazione a un utente specifico identificato dall'ID.
     * Se la richiesta va a buon fine, restituisce uno stato HTTP 200 (OK).
     * In caso di errore, restituisce uno stato HTTP 500 (INTERNAL_SERVER_ERROR)
     * con un messaggio di errore.
     *
     * @param idUtente  l'ID dell'utente a cui aggiungere l'indirizzo
     * @param indirizzo l'indirizzo di fatturazione da aggiungere
     * @return {@code ResponseEntity} con messaggio di successo o errore
     */
    @PostMapping("/{idUtente}/indirizzo-fatturazione")
    public ResponseEntity<?> aggiungiIndirizzoFatturazione(@PathVariable Long idUtente,
            @RequestBody Indirizzo indirizzo) {
        try {
            utenteService.aggiungiIndirizzoFatturazione(idUtente, indirizzo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Indirizzo di fatturazione aggiunto con successo.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Errore nell'aggiunta dell'indirizzo di fatturazione.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
