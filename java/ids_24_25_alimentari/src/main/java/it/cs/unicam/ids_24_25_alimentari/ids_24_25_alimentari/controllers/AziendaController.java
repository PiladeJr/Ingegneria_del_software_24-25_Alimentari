package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.azienda.AziendaOutDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.AziendaService;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.InfoAziendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Controller per la gestione delle operazioni relative all'entità Azienda.
 * Fornisce endpoint per ottenere, salvare, eliminare e creare entità Azienda.
 */
@RestController
@RequestMapping("/api/azienda")
public class AziendaController {

    private final AziendaService aziendaService;
    private final InfoAziendaService infoAziendaService;



    public AziendaController(AziendaService aziendaService, InfoAziendaService infoAziendaService) {
        this.aziendaService = aziendaService;
        this.infoAziendaService = infoAziendaService;
    }

    /**
     * restituisce una lista di tutte le aziende salvate dalla piattaforma
     *
     * @return ResponseEntity contenente la lista di tutte le aziende.
     */
    @GetMapping
    public ResponseEntity<List<AziendaOutDTO>> getAllAziende(
            @RequestParam (required = false) String sortBy,
            @RequestParam (required = false) String order) {
        return ResponseEntity.ok(aziendaService.getAllAziende(sortBy, order));
    }

    /**
     * ottiene un'azienda dal suo id.
     *
     * @param id l'ID della Azienda.
     * @return ResponseEntity contenente l'azienda se trovata, oppure un 404 status
     *         se non trovata, oppure un errore 500 in caso di eccezione.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAziendaById(@PathVariable Long id) {
        try {
            return aziendaService.getAziendaById(id)
                    .map(ResponseEntity::<Object>ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap("error", "Azienda non trovata")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

    /**
     * ottiene la lista di aziende selezionate in base al filtro
     * ruolo dell'utente
     *
     * @param ruolo il ruolo dell'utente in base a cui filtrare le aziende
     * @return la lista di aziende appartenenti ad utenti con il ruolo specificato
     */
    @GetMapping("/roles/{ruolo}")
    public ResponseEntity<?> getAziendeByRuolo(
            @PathVariable String ruolo,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        try {
            Ruolo ruoloEnum = Ruolo.valueOf(ruolo.toUpperCase());
            List<AziendaOutDTO> aziende = aziendaService.getAziendeByRuolo(ruoloEnum, sortBy, order);

            if (aziende == null || aziende.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Nessuna azienda trovata per il ruolo specificato"));
            }

            return ResponseEntity.ok(aziende);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Ruolo non valido: " + ruolo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

    /**
     * <h2>Ottieni le informazioni aggiuntive dell'azienda</h2>
     * <br/>
     * <p>Endpoint per ottenere le informazioni aggiuntive approvate relative all'azienda dell'utente autenticato.</p>
     *<br/>
     * <p><strong>Response:</strong></p>
     * <ul>
     *   <li><code>200 OK</code>: Contiene le informazioni aggiuntive approvate.</li>
     *   <li><code>200 OK</code>: Messaggio vuoto se non ci sono informazioni disponibili.</li>
     *   <li><code>404 Not Found</code>: Se le informazioni non sono trovate.</li>
     * </ul>
     */
    @GetMapping("/me/informazioni")
    public ResponseEntity<?> getInformazioniAzienda() {
        return infoAziendaService.ottieniInformazioniAzienda();
    }

    /**
     * <h2>Cancella le informazioni aggiuntive di un'azienda</h2>
     * <br/>
     * <p>Endpoint per la cancellazione delle informazioni aggiuntive associate all'azienda dell'utente autenticato.</p>
     * <p>Le informazioni aggiuntive vengono marcate come "ELIMINATO" e dissociate dall'azienda.</p>
     *
     * @return ResponseEntity con un messaggio di successo o errore.
     */
    @DeleteMapping("/me/informazioni")
    public ResponseEntity<?> cancellaInfoAzienda() {
        try {
            infoAziendaService.cancellaInfoAzienda();
            return ResponseEntity.ok(Collections.singletonMap("message", "Informazioni aggiuntive cancellate correttamente."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

    /**
     * Elimina un'entità {@link Azienda} tramite il suo ID
     *
     * @param id dell'azienda che vogliamo eliminare.
     * @return ResponseEntity con un body JSON contenente un messaggio d'errore se l'eliminazione
     *         fallisce, altrimenti nessun contenuto.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAzienda(@PathVariable Long id) {
        try {
            Optional<Azienda> azienda = aziendaService.getAziendaById(id);
            if (azienda.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Azienda non trovata"));
            } else {
                aziendaService.deleteAzienda(id);
                return ResponseEntity.status(200)
                        .body(Collections.singletonMap("message", "Azienda eliminata correttamente"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "ID non valido: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

}