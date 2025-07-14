package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoEstesoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.IscrittoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.EventoService;
import org.springframework.http.HttpStatus;
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

    //------------------------------------FORMATO ESTESO--------------------------------------------------

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
    @GetMapping("/gestore/all")
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
    @GetMapping("/gestore/visite")
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
    @GetMapping("/gestore/fiere")
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
    @GetMapping("/gestore/{id}")
    public ResponseEntity<EventoEstesoDTO> getEventoById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventoService.getEventoById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * <h2>Cerca eventi per titolo</h2>
     * <br>
     * Questo endpoint consente di cercare eventi che contengono il titolo specificato.
     * Se il parametro `titolo` non viene fornito, restituisce tutti gli eventi.
     *
     * @param titolo Il titolo da cercare negli eventi (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi che corrispondono al titolo specificato.
     */
    @GetMapping("/gestore/search")
    public ResponseEntity<List<EventoEstesoDTO>> cercaEventi(
            @RequestParam(required = false) String titolo
    ) {
        return ResponseEntity.ok(eventoService.getEventiByTitle(titolo));
    }

    //------------------------------------FORMATO ANIMATORE--------------------------------------------------

    /**
     * <h2>Ottiene gli eventi creati dall'animatore autenticato</h2>
     * <br>
     * Restituisce una lista di eventi estesi creati dall'animatore autenticato.
     * È possibile specificare i parametri di ordinamento e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi creati dall'animatore.
     */
    @GetMapping("/miei")
    public ResponseEntity<List<EventoEstesoDTO>> getEventiByCreatore(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(eventoService.getEventiByCreatore(sortBy,order));
    }

    /**
     * <h2>Cerca eventi per titolo creati dall'animatore autenticato</h2>
     * <br>
     * Questo endpoint consente di cercare eventi che contengono il titolo specificato
     * e che sono stati creati dall'animatore autenticato.
     * Se il parametro `titolo` non viene fornito, restituisce tutti gli eventi creati dall'animatore.
     *
     * @param titolo Il titolo da cercare negli eventi creati dall'animatore (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi che corrispondono al titolo specificato.
     */
    @GetMapping("/miei/search")
    public ResponseEntity<List<EventoEstesoDTO>> cercaEventiByTitleAndCreatore(
            @RequestParam(required = false) String titolo
    ) {
        return ResponseEntity.ok(eventoService.getEventiByCreatoreAndTitle(titolo.toLowerCase()));
    }

    /**
     * <h2>Ottiene le visite aziendali create dall'animatore autenticato</h2>
     * <br>
     * Questo endpoint restituisce una lista di eventi di tipo visita aziendale
     * creati dall'animatore autenticato. È possibile specificare i parametri
     * di ordinamento e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di visite aziendali create dall'animatore.
     */
    @GetMapping("/miei/visite")
    public ResponseEntity<List<EventoEstesoDTO>> getVisiteByCreatore(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(eventoService.getVisiteByCreatore(sortBy, order));
    }

    /**
     * <h2>Ottiene le fiere create dall'animatore autenticato</h2>
     * <br>
     * Questo endpoint restituisce una lista di eventi di tipo fiera
     * creati dall'animatore autenticato. È possibile specificare i parametri
     * di ordinamento e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di fiere create dall'animatore.
     */
    @GetMapping("/miei/fiere")
    public ResponseEntity<List<EventoEstesoDTO>> getFieraByCreatore(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(eventoService.getFieraByCreatore(sortBy, order));
    }



    //--------------------------------FORMATO PREVIEW--------------------------------------------------------

    /**
     * <h2>Restituisce una lista di anteprime degli eventi</h2>
     * <br>
     * Questo endpoint fornisce una lista semplificata degli eventi, contenente
     * informazioni come titolo, locandina e date.
     *
     * @param sortBy Il campo su cui ordinare le anteprime (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista semplificata degli eventi.
     */
    @GetMapping("/gestore/anteprime")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventi(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimeEventi(sortBy, order));
    }

    /**
     * <h2>Restituisce le anteprime degli eventi di tipo fiera</h2>
     * <br>
     * Questo endpoint fornisce un elenco semplificato di tutte le fiere registrate nel sistema.
     *
     * @param sortBy Il campo su cui ordinare le anteprime (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista delle anteprime delle fiere.
     */
    @GetMapping("/gestore/anteprime/fiere")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeFiera(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimeFiera(sortBy, order));
    }

    /**
     * <h2>Restituisce le anteprime delle visite aziendali</h2>
     * <br>
     * Questo endpoint fornisce una lista semplificata degli eventi classificati come visite aziendali.
     *
     * @param sortBy Il campo su cui ordinare le anteprime (opzionale).
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista delle anteprime delle visite aziendali.
     */
    @GetMapping("/gestore/anteprime/visite")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeVisite(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimeVisite(sortBy, order));
    }

    /**
     * <h2>Restituisce le anteprime degli eventi filtrati per titolo</h2>
     * <br>
     * Questo endpoint consente di cercare anteprime di eventi che contengono il titolo specificato.
     * Se il parametro `titolo` non viene fornito, restituisce tutte le anteprime degli eventi.
     *
     * @param titolo Il titolo da cercare nelle anteprime degli eventi (opzionale).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista di anteprime degli eventi che corrispondono al titolo specificato.
     */
    @GetMapping("/gestore/anteprime/search")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventiPerTitolo(
            @RequestParam(required = false) String titolo
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimaEventiPerTitolo(titolo.toLowerCase()));
    }

    //-------------------------------------------FORMATO ESTESO E VISIBILE--------------------------------------------

    /**
     * <h2>Recupera tutti gli eventi programmati di tipo visita</h2>
     * <br>
     * Questo endpoint consente di ottenere una lista di eventi di tipo visita
     * che sono stati programmati. È possibile specificare i parametri di ordinamento
     * e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale, predefinito: "id").
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale, predefinito: "asc").
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi visita programmati.
     */
    @GetMapping("/programmati/visite")
    public ResponseEntity<List<EventoEstesoDTO>> getAllVisitaProgrammati(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(eventoService.getAllVisitaProgrammati(sortBy, order));
    }
    /**
     * <h2>Recupera tutti gli eventi di tipo fiera programmati</h2>
     * <br>
     * Questo endpoint consente di ottenere una lista di eventi di tipo fiera
     * che sono stati programmati. È possibile specificare i parametri di ordinamento
     * e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale, predefinito: "id").
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale, predefinito: "asc").
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi fiera programmati.
     */
    @GetMapping("/programmati/fiere")
    public ResponseEntity<List<EventoEstesoDTO>> getAllFieraProgrammati(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(eventoService.getAllFieraProgrammati(sortBy, order));
    }
    /**
     * <h2>Recupera tutti gli eventi programmati</h2>
     * <br>
     * Questo endpoint consente di ottenere una lista di tutti gli eventi
     * che sono stati programmati, indipendentemente dal tipo. È possibile
     * specificare i parametri di ordinamento e direzione.
     *
     * @param sortBy Il campo su cui ordinare gli eventi (opzionale, predefinito: "id").
     * @param order La direzione dell'ordinamento: "asc" per crescente, "desc" per decrescente (opzionale, predefinito: "asc").
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di tutti gli eventi programmati.
     */
    @GetMapping("/programmati")
    public ResponseEntity<List<EventoEstesoDTO>> getAllProgrammati(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(eventoService.getAllProgrammati(sortBy, order));
    }

    /**
     * <h2>Recupera eventi programmati in base al titolo</h2>
     * <br>
     * Questo endpoint consente di ottenere una lista di eventi programmati
     * che contengono il titolo specificato. Se il parametro `title` non viene fornito,
     * restituisce tutti gli eventi programmati ordinati per titolo in ordine crescente.
     *
     * @param title Il titolo da cercare negli eventi (opzionale).
     * @return {@code ResponseEntity<List<EventoEstesoDTO>>} Lista di eventi programmati che contengono il titolo specificato.
     */
    @GetMapping("/programmati/search")
    public ResponseEntity<List<EventoEstesoDTO>> getEventiProgrammatiByTitle(
            @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok(eventoService.getEventiProgrammatiByTitle(title));
    }

    //-------------------------------------------FORMATO ANTEPRIMA E VISIBILE--------------------------------------------

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi programmati</h2>
     * <br>
     * Questo endpoint restituisce una lista semplificata di eventi programmati con informazioni essenziali
     * per l'interfaccia utente, come titolo, locandina, data di inizio e fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo" (default), "dataInizio".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista di anteprime di eventi programmati.
     */
    @GetMapping("/preview")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventiProgrammati(
            @RequestParam(required = false, defaultValue = "titolo") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimeEventiProgrammati(sortBy, order));
    }

    /**
     * <h2>Recupera l'anteprima di tutte le visite programmate</h2>
     * <br>
     * Questo endpoint restituisce una lista semplificata di visite programmate con informazioni essenziali,
     * come titolo, locandina, data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista di anteprime di visite programmate.
     */
    @GetMapping("/preview/visite")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeVisiteProgrammate(
            @RequestParam(required = false, defaultValue = "titolo") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimeVisiteProgrammate(sortBy, order));
    }

    /**
     * <h2>Recupera l'anteprima di tutte le fiere programmate</h2>
     * <br>
     * Questo endpoint restituisce una lista semplificata di fiere programmate con informazioni essenziali,
     * come titolo, locandina, data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista di anteprime di fiere programmate.
     */
    @GetMapping("/preview/fiere")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeFiereProgrammate(
            @RequestParam(required = false, defaultValue = "titolo") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimeFiereProgrammate(sortBy, order));
    }

    /**
     * <h2>Recupera l'anteprima di eventi programmati in base al titolo</h2>
     * <br>
     * Questo endpoint restituisce una lista di anteprime di eventi programmati che contengono
     * il parametro 'titolo'.
     * Se non viene fornito, restituisce tutte le anteprime degli eventi.
     *
     * @param title Il titolo da cercare negli eventi(opzionale).
     * @return {@code ResponseEntity<List<EventoPreviewDTO>>} Lista di anteprime di eventi programmati che contengono il titolo specificato.
     */
    @GetMapping("/preview/search")
    public ResponseEntity<List<EventoPreviewDTO>> getAnteprimeEventiProgrammatiByTitle(
            @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok(eventoService.getAnteprimeEventiProgrammatiByTitle(title));
    }

    /**
     * <h2>Recupera un evento visibile tramite ID</h2>
     * <br>
     * Questo endpoint consente di ottenere un evento programmato specifico utilizzando il suo ID.
     * Se l'evento non viene trovato, restituisce un errore 404.
     *
     * @param id L'ID dell'evento da recuperare.
     * @return {@code ResponseEntity<EventoEstesoDTO>} L'evento corrispondente o un errore 404 se non trovato.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventoEstesoDTO> getEventoVisibileById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventoService.getEventoProgrammatoById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //-------------------------------------------ISCRIZIONI E VISITE------------------------------------------------

    /**
     * <h2>Recupera tutti gli iscritti a una visita</h2>
     * <br>
     * Questo endpoint permette di ottenere la lista degli iscritti a un evento di tipo visita
     * specificato tramite il suo ID, verificando prima se il creatore dell'evento corrisponde all'utente autenticato.
     *
     * @param idVisita L'ID della visita di cui recuperare gli iscritti.
     * @return {@code ResponseEntity<List<IscrittoDTO>>} Lista degli iscritti alla visita specificata.
     */
    @GetMapping("/miei/visite/{idVisita}/iscritti")
    public ResponseEntity<List<IscrittoDTO>> getIscrittiAdEventoCreato(@PathVariable Long idVisita) {
        return eventoService.getIscrittiEvento(idVisita);
    }

    /**
     * <h2>Recupera tutti gli iscritti a una visita</h2>
     * <br>
     * Questo endpoint permette di ottenere la lista degli iscritti a un evento di tipo visita
     * specificato tramite il suo ID.
     *
     * @param idVisita L'ID della visita di cui recuperare gli iscritti.
     * @return {@code ResponseEntity<List<IscrittoDTO>>} Lista degli iscritti alla visita specificata.
     */
    @GetMapping("/gestore/visite/{idVisita}/iscritti")
    public ResponseEntity<List<IscrittoDTO>> getIscrittiAdEvento(@PathVariable Long idVisita) {
        return eventoService.getIscrittiEvento(idVisita);
    }



    /**
     * <h2>Iscrive un utente a un evento di tipo visita</h2>
     * <br>
     * Questo endpoint consente a un utente autenticato di iscriversi a un evento di tipo visita.
     *
     * @param idEvento L'ID dell'evento a cui iscrivere l'utente.
     * @return {@code ResponseEntity<String>} Messaggio di conferma o errore.
     */
    @PostMapping("/visite/{idEvento}/iscrizione")
    public ResponseEntity<String> iscriviUtente(@PathVariable Long idEvento) {
        try {
            return eventoService.iscriviUtente(idEvento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //------------------------------------------------CANCELLAZIONE E ELIMINAZIONE--------------------------------------------
    /**
     * <h2>Cancella un evento creato dall'utente autenticato</h2>
     * <br>
     * Questo endpoint consente di cancellare virtualmente un evento creato dall'animatore autenticato.
     * La cancellazione virtuale non elimina fisicamente l'evento dal database, ma lo rende non visibile
     * agli utenti. Se l'evento non viene trovato o non appartiene all'animatore autenticato, restituisce
     * un errore.
     *
     * @param idEvento L'ID dell'evento da cancellare virtualmente.
     * @return {@code ResponseEntity<String>} Messaggio di conferma o errore.
     */
    @DeleteMapping("/miei/{idEvento}")
    public ResponseEntity<String> cancellaEvento(@PathVariable Long idEvento) {
        try {
            eventoService.cancellaEvento(idEvento);
            return ResponseEntity.status(HttpStatus.OK).body("Evento cancellato virtualmente con successo.");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    /**
     * <h2>Elimina virtualmente un evento</h2>
     * <br>
     * Questo endpoint consente di eliminare virtualmente un evento specifico utilizzando il suo ID.
     * Se l'evento non viene trovato, restituisce un errore 404.
     *
     * @param idEvento L'ID dell'evento da eliminare virtualmente.
     * @return {@code ResponseEntity<String>} Messaggio di conferma o errore.
     */
    @PutMapping("/{idEvento}/elimina-virtuale")
    public ResponseEntity<String> eliminaEventoVirtuale(@PathVariable Long idEvento) {
        try {
            eventoService.eliminaEventoVirtuale(idEvento);
            return ResponseEntity.status(HttpStatus.OK).body("Evento eliminato virtualmente con successo.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * <h2>Elimina fisicamente un evento</h2>
     * <br>
     * Questo endpoint consente di eliminare fisicamente un evento specifico utilizzando il suo ID.
     * Se l'evento non viene trovato o non può essere eliminato, restituisce un errore.
     *
     * @param idEvento L'ID dell'evento da eliminare fisicamente.
     * @return {@code ResponseEntity<String>} Messaggio di conferma o errore.
     */
    @DeleteMapping("/{idEvento}/elimina-fisico")
    public ResponseEntity<String> eliminaEventoFisico(@PathVariable Long idEvento) {
        try {
            eventoService.eliminaEventoFisico(idEvento);
            return ResponseEntity.status(HttpStatus.OK).body("Evento eliminato fisicamente con successo.");
        } catch (NoSuchElementException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
