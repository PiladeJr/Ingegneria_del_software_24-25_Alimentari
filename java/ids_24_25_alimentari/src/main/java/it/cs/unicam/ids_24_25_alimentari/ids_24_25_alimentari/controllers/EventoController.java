package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoEstesoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.EventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/api/eventi")
public class EventoController {
    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * <h2>Ottiene tutti gli eventi ordinati alfabeticamente per titolo</h2>
     * <br>
     * Restituisce una lista di eventi estesi, ordinati
     * alfabeticamente per titolo. È possibile specificare i parametri
     * di ordinamento e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi ordinati per titolo.
     */
    @GetMapping("/all")
    public ResponseEntity<List<EventoEstesoDTO>> getAllEventi(
            @RequestParam (required = false) String sortBy,
            @RequestParam(required = false) String order)
    {
        return ResponseEntity.ok(eventoService.getAllEventi(sortBy, order));
    }

    /**
     * <h2>Ottiene tutti gli eventi di tipo visita</h2>
     * <br>
     * Restituisce una lista di eventi di tipo visita.
     * È possibile specificare i parametri di ordinamento e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoVisita>>} Lista di eventi visita.
     */
    @GetMapping("/visite")
    public ResponseEntity<List<EventoEstesoDTO>> getEventiVisita(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order)
    {
        return ResponseEntity.ok(eventoService.getAllVisita(sortBy, order));
    }

    /**
     * <h2>Ottiene tutti gli eventi di tipo fiera</h2>
     * <br>
     * Restituisce una lista di eventi di tipo fiera.
     * È possibile specificare i parametri di ordinamento e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi fiera.
     */
    @GetMapping("/fiere")
    public ResponseEntity<List<EventoEstesoDTO>> getEventiFiera(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(eventoService.getAllFiera(sortBy, order));
    }

    /**
     * <h2>Ottiene un evento specifico tramite ID</h2>
     * <br>
     * Questo endpoint consente di recuperare un evento specifico utilizzando il suo ID.
     * Se l'evento non viene trovato, restituisce un errore 404.
     *
     * @param id L'ID dell'evento da recuperare.
     * @return {@code ResponseEntity<Evento>} L'evento corrispondente o un errore 404 se non trovato.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        try {
            Evento evento = eventoService.getEventoById(id);
            return ResponseEntity.ok(evento);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
//
//    /**
//     * <h2>Ottiene tutti gli eventi creati dall'utente autenticato</h2>
//     *
//     * @return Lista di eventi creati dall'utente
//     */
//    @GetMapping("/miei")
//    public ResponseEntity<List<Evento>> getEventiCreatiDallUtenteAutenticato() {
//        return ResponseEntity.ok(eventoService.getEventiCreatiDallUtenteAutenticato());
//    }
//
//    /**
//     * <h2>Ottiene un evento specifico tramite ID</h2>
//     *
//     * @param id ID dell'evento
//     * @return Evento corrispondente
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
//        return ResponseEntity.ok(eventoService.getEventoById(id));
//    }
//    /**
//     * <h2>Ottiene una lista di anteprime degli eventi (titolo, locandina, date)</h2>
//     *
//     * @return Lista semplificata degli eventi
//     */
//    @GetMapping("/anteprime")
//    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventi() {
//        return ResponseEntity.ok(eventoService.getAnteprimeEventi());
//    }
//
//    /**
//     * <h2>Restituisce le anteprime degli eventi ordinati per data di inizio</h2>
//     * <br>
//     * Elenco semplificato di tutti gli eventi, ordinati dalla data più recente alla meno recente.
//     *
//     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime ordinate per data.
//     */
//    @GetMapping("/anteprime/data-inizio")
//    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventiInizio() {
//        return ResponseEntity.ok(eventoService.getAnteprimeEventiInizio());
//    }
//
//    /**
//     * <h2>Restituisce le anteprime degli eventi di tipo fiera</h2>
//     * <br>
//     * Elenco semplificato di tutte le fiere registrate nel sistema.
//     *
//     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle fiere.
//     */
//    @GetMapping("/anteprime/fiere")
//    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeFiera() {
//        return ResponseEntity.ok(eventoService.getAnteprimeFiera());
//    }
//
//    /**
//     * <h2>Restituisce le anteprime delle visite aziendali</h2>
//     * <br>
//     * Lista degli eventi classificati come visite aziendali.
//     *
//     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle visite.
//     */
//    @GetMapping("/anteprime/visite")
//    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeVisite() {
//        return ResponseEntity.ok(eventoService.getAnteprimeVisite());
//    }
//
//    /**
//     * <h2>Restituisce le anteprime delle fiere ordinate per data di inizio</h2>
//     * <br>
//     * Le fiere vengono ordinate dalla più recente alla meno recente.
//     *
//     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle fiere ordinate per data.
//     */
//    @GetMapping("/anteprime/fiere/data-inizio")
//    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeFieraPerDataInizio() {
//        return ResponseEntity.ok(eventoService.getAnteprimeFieraPerDataInizio());
//    }
//
//    /**
//     * <h2>Restituisce le anteprime delle visite aziendali ordinate per data di inizio</h2>
//     * <br>
//     * Le visite vengono presentate in ordine decrescente a partire da quelle più recenti.
//     *
//     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} anteprime delle visite ordinate per data.
//     */
//    @GetMapping("/anteprime/visite/data-inizio")
//    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeVisitePerDataInizio() {
//        return ResponseEntity.ok(eventoService.getAnteprimeVisitePerDataInizio());
//    }


}
