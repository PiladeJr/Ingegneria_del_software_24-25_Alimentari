package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.EventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/eventi")
public class EventoController {
    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * <h2>Ottiene tutti gli eventi ordinati alfabeticamente per titolo</h2>
     *
     * @return Lista di eventi ordinati per titolo
     */
    @GetMapping("/ordinati/titolo")
    public ResponseEntity<List<Evento>> getAllEventiOrdinatiPerTitolo() {
        return ResponseEntity.ok(eventoService.getAllEventiOrdinatiPerTitolo());
    }

    /**
     * <h2>Ottiene tutti gli eventi ordinati per data di inizio (dal più recente)</h2>
     *
     * @return Lista di eventi ordinati per data inizio discendente
     */
    @GetMapping("/ordinati/data-inizio")
    public ResponseEntity<List<Evento>> getAllEventiOrdinatiPerDataInizio() {
        return ResponseEntity.ok(eventoService.getAllEventiOrdinatiPerDataInizio());
    }

    /**
     * <h2>Ottiene tutti gli eventi di tipo visita</h2>
     *
     * @return Lista di eventi visita
     */
    @GetMapping("/visite")
    public ResponseEntity<List<EventoVisita>> getEventiVisita() {
        return ResponseEntity.ok(eventoService.getEventiVisita());
    }

    /**
     * <h2>Ottiene tutti gli eventi di tipo fiera</h2>
     *
     * @return Lista di eventi fiera
     */
    @GetMapping("/fiere")
    public ResponseEntity<List<EventoFiera>> getEventiFiera() {
        return ResponseEntity.ok(eventoService.getEventiFiera());
    }

    /**
     * <h2>Ottiene tutte le visite ordinate per data di inizio</h2>
     *
     * @return Lista di eventi visita ordinati per data
     */
    @GetMapping("/visite/ordinati/data-inizio")
    public ResponseEntity<List<EventoVisita>> getEventiVisitaOrdinatiPerDataInizio() {
        return ResponseEntity.ok(eventoService.getEventiVisitaOrdinatiPerDataInizio());
    }

    /**
     * <h2>Ottiene tutte le fiere ordinate per data di inizio</h2>
     *
     * @return Lista di eventi fiera ordinati per data
     */
    @GetMapping("/fiere/ordinati/data-inizio")
    public ResponseEntity<List<EventoFiera>> getEventiFieraOrdinatiPerDataInizio() {
        return ResponseEntity.ok(eventoService.getEventiFieraOrdinatiPerDataInizio());
    }

    /**
     * <h2>Ottiene tutti gli eventi creati dall'utente autenticato</h2>
     *
     * @return Lista di eventi creati dall'utente
     */
    @GetMapping("/miei")
    public ResponseEntity<List<Evento>> getEventiCreatiDallUtenteAutenticato() {
        return ResponseEntity.ok(eventoService.getEventiCreatiDallUtenteAutenticato());
    }

    /**
     * <h2>Ottiene un evento specifico tramite ID</h2>
     *
     * @param id ID dell'evento
     * @return Evento corrispondente
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.getEventoById(id));
    }
    /**
     * <h2>Ottiene una lista di anteprime degli eventi (titolo, locandina, date)</h2>
     *
     * @return Lista semplificata degli eventi
     */
    @GetMapping("/anteprime")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventi() {
        return ResponseEntity.ok(eventoService.getAnteprimeEventi());
    }

    /**
     * <h2>Restituisce le anteprime degli eventi ordinati per data di inizio</h2>
     * <br>
     * Elenco semplificato di tutti gli eventi, ordinati dalla data più recente alla meno recente.
     *
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime ordinate per data.
     */
    @GetMapping("/anteprime/data-inizio")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventiInizio() {
        return ResponseEntity.ok(eventoService.getAnteprimeEventiInizio());
    }

    /**
     * <h2>Restituisce le anteprime degli eventi di tipo fiera</h2>
     * <br>
     * Elenco semplificato di tutte le fiere registrate nel sistema.
     *
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle fiere.
     */
    @GetMapping("/anteprime/fiere")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeFiera() {
        return ResponseEntity.ok(eventoService.getAnteprimeFiera());
    }

    /**
     * <h2>Restituisce le anteprime delle visite aziendali</h2>
     * <br>
     * Lista degli eventi classificati come visite aziendali.
     *
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle visite.
     */
    @GetMapping("/anteprime/visite")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeVisite() {
        return ResponseEntity.ok(eventoService.getAnteprimeVisite());
    }

    /**
     * <h2>Restituisce le anteprime delle fiere ordinate per data di inizio</h2>
     * <br>
     * Le fiere vengono ordinate dalla più recente alla meno recente.
     *
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle fiere ordinate per data.
     */
    @GetMapping("/anteprime/fiere/data-inizio")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeFieraPerDataInizio() {
        return ResponseEntity.ok(eventoService.getAnteprimeFieraPerDataInizio());
    }

    /**
     * <h2>Restituisce le anteprime delle visite aziendali ordinate per data di inizio</h2>
     * <br>
     * Le visite vengono presentate in ordine decrescente a partire da quelle più recenti.
     *
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle visite ordinate per data.
     */
    @GetMapping("/anteprime/visite/data-inizio")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeVisitePerDataInizio() {
        return ResponseEntity.ok(eventoService.getAnteprimeVisitePerDataInizio());
    }
    /**
     * <h2>Iscrive l'utente autenticato a un evento visita</h2>
     * <br>
     * Questo endpoint consente all'utente autenticato di iscriversi a un evento
     * di tipo visita. L'evento deve essere in stato "PROGRAMMATO" e l'utente
     * non deve essere già iscritto.
     *
     * @param idEvento L'ID dell'evento a cui iscriversi
     * @return {@code ResponseEntity<String>} esito dell'iscrizione
     */
    @PostMapping("/{idEvento}/iscriviti")
    public ResponseEntity<String> iscriviUtenteAEvento(@PathVariable Long idEvento) {
        boolean risultato = eventoService.iscriviUtenteAEvento(idEvento);
        return risultato
                ? ResponseEntity.ok("Iscrizione effettuata con successo")
                : ResponseEntity.badRequest().body("Impossibile effettuare l'iscrizione");
    }
}
