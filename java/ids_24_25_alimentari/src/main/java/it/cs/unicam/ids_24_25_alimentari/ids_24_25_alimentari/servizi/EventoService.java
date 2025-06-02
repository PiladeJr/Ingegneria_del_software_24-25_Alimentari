package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;
    private final UtenteService utenteService;

    public EventoService(EventoRepository eventoRepository, UtenteService utenteService) {
        this.eventoRepository = eventoRepository;
        this.utenteService = utenteService;
    }

    /**
     * <h2>Recupera tutti gli eventi ordinati alfabeticamente</h2>
     * <br>
     * Questo metodo restituisce una lista completa di eventi presenti sulla piattaforma,
     * includendo sia le fiere che le visite, ordinati in ordine alfabetico in base al titolo.
     *
     * @return {@code List<Evento>} contenente tutti gli eventi (fiere e visite),
     *         ordinati alfabeticamente per titolo.
     */
    public List<Evento> getAllEventiOrdinatiPerTitolo() {
        return eventoRepository.findAllByOrderByTitoloAsc();
    }

    /**
     * <h2>Recupera tutti gli eventi (fiere e visite) ordinati per data di inizio decrescente</h2>
     * <br>
     * Unisce fiere e visite in un'unica lista e le ordina per data di inizio, dalla più recente alla meno recente.
     *
     * @return {@code List<Evento>} contenente fiere e visite ordinate per data di inizio decrescente.
     */
    public List<Evento> getAllEventiOrdinatiPerDataInizio() {
        return eventoRepository.findAllByOrderByInizioDesc();
    }

    /**
     * <h2>Recupera tutti gli eventi di tipo visita</h2>
     * <br>
     * Questo metodo restituisce la lista di eventi che rappresentano visite aziendali.
     *
     * @return {@code List<EventoVisita>} contenente tutti gli eventi di tipo visita presenti nel database.
     */
    public List<EventoVisita> getEventiVisita() {
        return eventoRepository.findAllVisita();
    }
    /**
     * <h2>Recupera tutti gli eventi di tipo fiera</h2>
     * <br>
     * Questo metodo restituisce la lista di eventi che rappresentano fiere territoriali.
     *
     * @return {@code List<EventoFiera>} contenente tutti gli eventi di tipo fiera presenti nel database.
     */
    public List<EventoFiera> getEventiFiera() {
        return eventoRepository.findAllFiera();
    }

    /**
     * <h2>Recupera tutte le visite ordinate per data di inizio decrescente</h2>
     * <br>
     * Questo metodo restituisce l'elenco delle visite aziendali, ordinate dalla più recente alla meno recente.
     *
     * @return {@code List<EventoVisita>} contenente tutte le visite ordinate per data di inizio decrescente.
     */
    public List<EventoVisita> getEventiVisitaOrdinatiPerDataInizio() {
        return eventoRepository.findAllVisitaOrderByInizioDesc();
    }

    /**
     * <h2>Recupera tutte le fiere ordinate per data di inizio decrescente</h2>
     * <br>
     * Questo metodo restituisce l'elenco delle fiere sulla piattaforma, ordinate dalla più recente alla meno recente.
     *
     * @return {@code List<EventoFiera>} contenente tutte le fiere ordinate per data di inizio decrescente.
     */
    public List<EventoFiera> getEventiFieraOrdinatiPerDataInizio() {
        return eventoRepository.findAllFieraOrderByInizioDesc();
    }

    /**
     * <h2>Recupera tutti gli eventi creati dall'utente attualmente autenticato</h2>
     * <br>
     * Questo metodo utilizza il contesto di sicurezza per identificare l'utente attualmente autenticato
     * e restituisce la lista di eventi (fiere e visite) da lui creati.
     *
     * @return {@code List<Evento>} contenente tutti gli eventi creati dall'utente loggato.
     * @throws RuntimeException se l'utente non è autenticato o non è presente nel database.
     */
    public List<Evento> getEventiCreatiDallUtenteAutenticato() {
        Long idUtente = utenteService.getIdUtenteAutenticato();
        return eventoRepository.findByCreatoreId(idUtente);
    }

    /**
     * <h2>Recupera un evento specifico in base al suo ID</h2>
     * <br>
     * Questo metodo restituisce un evento specifico in base all'ID fornito
     * comprensivo di tutti i dettagli (lista iscritti, azienda referente, aziende collegate).
     *
     * @param id L'ID dell'evento da recuperare.
     * @return {@code Evento} l'evento corrispondente all'ID fornito.
     * @throws NoSuchElementException se l'evento con l'ID specificato non esiste.
     */
    public Evento getEventoById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + id + " non trovato"));
        if (evento instanceof EventoFiera) {
            return (EventoFiera) evento;
        } else if (evento instanceof EventoVisita) {
            return (EventoVisita) evento;
        } else {
            return evento;
        }
    }

    /**
     * <h2>Recupera eventi in base al titolo</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che contengono il parametro specificato
     * oppure il singolo evento con il titolo corrispondente.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<Evento>} lista di eventi che contengono il titolo specificato.
     */
    public List<Evento> getEventiByTitle(String title) {
        return eventoRepository.findByTitleContainingParameter(title);
    }

    /**
     * <h2>Recupera eventi in base al titolo e all'ID del creatore</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che contengono il parametro specificato
     * e sono stati creati da un utente specifico.
     *
     * @param title Il titolo da cercare negli eventi.
     * @param idCreatore L'ID del creatore dell'evento.
     * @return {@code List<Evento>} lista di eventi che contengono il titolo specificato
     *         e sono stati creati dall'utente con l'ID fornito.
     */
    public List<Evento> getEventiByTitleAndCreatore(String title, Long idCreatore) {
        return eventoRepository.findByTitleContainingParameterAndCreatoreId(title, idCreatore);
    }

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi (fiere e visite)</h2>
     * <br>
     * Restituisce una lista semplificata di tutti gli eventi presenti sulla piattaforma,
     * con informazioni essenziali: titolo, locandina, data di inizio e data di fine.
     *
     * @return {@code List<EventoPreviewDTO>} contenente anteprime di tutti gli eventi.
     */
    public List<EventoPreviewDTO> getAnteprimeEventi() {
        return eventoRepository.findAll().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }
    /**
     * <h2>Recupera l'anteprima di tutti gli eventi ordinati per data di inizio (decrescente)</h2>
     * <br>
     * Fornisce un elenco semplificato di tutti gli eventi, ordinati dalla data più recente
     * alla meno recente, utile per visualizzare gli eventi imminenti.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di eventi ordinate per data di inizio.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiInizio() {
        return eventoRepository.findAllByOrderByInizioDesc().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }
    /**
     * <h2>Recupera l'anteprima di tutte le fiere</h2>
     * <br>
     * Fornisce l'elenco degli eventi di tipo fiera con solo le informazioni rilevanti
     * per la visualizzazione: titolo, locandina e date.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di eventi fiera.
     */
    public List<EventoPreviewDTO> getAnteprimeFiera() {
        return eventoRepository.findAllFiera().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }
    /**
     * <h2>Recupera l'anteprima di tutte le visite aziendali</h2>
     * <br>
     * Restituisce una lista semplificata di eventi di tipo visita, utile per schermate
     * riepilogative o liste.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di eventi visita.
     */
    public List<EventoPreviewDTO> getAnteprimeVisite() {
        return eventoRepository.findAllVisita().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }
    /**
     * <h2>Recupera l'anteprima delle visite aziendali ordinate per data di inizio</h2>
     * <br>
     * Fornisce le anteprime delle visite aziendali, ordinate dalla più recente alla meno recente,
     * per agevolare la consultazione degli eventi imminenti.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di visite aziendali ordinate per data.
     */
    public List<EventoPreviewDTO> getAnteprimeVisitePerDataInizio() {
        return eventoRepository.findAllVisitaOrderByInizioDesc().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }
    /**
     * <h2>Recupera l'anteprima delle fiere ordinate per data di inizio</h2>
     * <br>
     * Restituisce l'elenco semplificato delle fiere, ordinate dalla più recente
     * alla meno recente, con i dati essenziali per l'interfaccia utente.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di fiere ordinate per data di inizio.
     */
    public List<EventoPreviewDTO> getAnteprimeFieraPerDataInizio() {
        return eventoRepository.findAllFieraOrderByInizioDesc().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera eventi in base al titolo</h2>
     * <br>
     * Questo metodo restituisce l'elenco semplificato di eventi che contengono il parametro specificato
     * con i dati essenziali per l'interfaccia utente.
     *
     * @param titolo Il titolo da cercare negli eventi.
     * @return {@code List<EventoPreviewDTO>} anteprime di eventi programmati che contengono il titolo specificato.
     */
    public List<EventoPreviewDTO> getAnteprimaEventiPerTitolo(String titolo){
        return eventoRepository.findByTitleContainingParameter(titolo).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi programmati di tipo visita</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi di tipo visita programmati.
     *
     * @return {@code List<EventoVisita>} contenente tutti gli eventi di tipo visita programmati.
     */
    public List<EventoVisita> getEventiVisitaProgrammati() {
        return eventoRepository.findAllVisitaProgrammati();
    }

    /**
     * <h2>Recupera tutti gli eventi di tipo fiera programmati</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi di tipo fiera programmati.
     *
     * @return {@code List<EventoFiera>} contenente tutti gli eventi di tipo fiera programmati.
     */
    public List<EventoFiera> getEventiFieraProgrammati() {
        return eventoRepository.findAllFieraProgrammati();
    }

    /**
     * <h2>Recupera tutti gli eventi programmati</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati.
     *
     * @return {@code List<Evento>} contenente tutti gli eventi programmati.
     */
    public List<Evento> getEventiProgrammati() {
        return eventoRepository.findAllEventiProgrammati();
    }

    /**
     * <h2>Recupera tutti gli eventi programmati ordinati per data di inizio</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati, ordinati per data di inizio.
     *
     * @return {@code List<Evento>} contenente tutti gli eventi programmati ordinati per data di inizio.
     */
    public List<Evento> getEventiProgrammatiOrdinatiPerDataInizio() {
        return eventoRepository.findAllEventiProgrammatiByInizio();
    }

    /**
     * <h2>Recupera tutti gli eventi programmati di tipo visita ordinati per data di inizio</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati di tipo visita, ordinati per data di inizio.
     *
     * @return {@code List<EventoVisita>} contenente tutti gli eventi programmati di tipo visita.
     */
    public List<EventoVisita> getEventiVisitaProgrammatiOrdinatiPerDataInizio() {
        return eventoRepository.findAllVisitaProgrammatiByInizio();
    }

    /**
     * <h2>Recupera tutti gli eventi programmati di tipo fiera ordinati per data di inizio</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati di tipo fiera, ordinati per data di inizio.
     *
     * @return {@code List<EventoFiera>} contenente tutti gli eventi programmati di tipo fiera.
     */
    public List<EventoFiera> getEventiFieraProgrammatiOrdinatiPerDataInizio() {
        return eventoRepository.findAllFieraProgrammatiByInizio();
    }

    /**
     * <h2>Recupera eventi programmati in base al titolo </h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati che contengono
     * il parametro specificato.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<Evento>} lista di eventi programmati che contengono il titolo specificato.
     */
    public List<Evento> getEventiProgrammatiByTitle(String title){
        return eventoRepository.findByTitleContainingParameterAndStatus(title);
    }

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi programmati</h2>
     * <br>
     * Restituisce una lista semplificata di eventi programmati con informazioni essenziali
     * per l'interfaccia utente.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di eventi programmati.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiProgrammati() {
        return eventoRepository.findAllEventiProgrammati().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutte le visite programmate</h2>
     * <br>
     * Restituisce una lista semplificata di visite programmate con informazioni essenziali.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di visite programmate.
     */
    public List<EventoPreviewDTO> getAnteprimeVisiteProgrammate() {
        return eventoRepository.findAllVisitaProgrammati().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutte le fiere programmate</h2>
     * <br>
     * Restituisce una lista semplificata di fiere programmate con informazioni essenziali.
     *
     * @return {@code List<EventoPreviewDTO>} anteprime di fiere programmate.
     */
    public List<EventoPreviewDTO> getAnteprimeFiereProgrammate() {
        return eventoRepository.findAllFieraProgrammati().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi programmati ordinati per data di inizio</h2>
     * <br>
     * Questo metodo restituisce una lista di anteprime di eventi programmati, ordinati per data di inizio.
     *
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime di tutti gli eventi programmati ordinati per data di inizio.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiProgrammatiOrdinatiPerDataInizio() {
        return eventoRepository.findAllEventiProgrammatiByInizio().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi programmati di tipo visita ordinati per data di inizio</h2>
     * <br>
     * Questo metodo restituisce una lista di anteprime di eventi programmati di tipo visita, ordinati per data di inizio.
     *
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime di tutti gli eventi programmati di tipo visita.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiVisitaProgrammatiOrdinatiPerDataInizio() {
        return eventoRepository.findAllVisitaProgrammatiByInizio().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi programmati di tipo fiera ordinati per data di inizio</h2>
     * <br>
     * Questo metodo restituisce una lista di anteprime di eventi programmati di tipo fiera, ordinati per data di inizio.
     *
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime di tutti gli eventi programmati di tipo fiera.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiFieraProgrammatiOrdinatiPerDataInizio() {
        return eventoRepository.findAllFieraProgrammatiByInizio().stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di eventi programmati in base al titolo</h2>
     * <br>
     * Questo metodo restituisce una lista di anteprime di eventi programmati che contengono
     * il parametro specificato.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<EventoPreviewDTO>} lista di anteprime di eventi programmati che contengono il titolo specificato.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiProgrammatiByTitle(String title) {
        return eventoRepository.findByTitleContainingParameterAndStatus(title).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()
                ))
                .collect(Collectors.toList());
    }
    /**
     * <h2>Salva un evento nel database</h2>
     * <br>
     * Questo metodo consente di salvare un evento (di tipo fiera o visita) nel database.
     *
     * @param evento L'evento da salvare.
     * @return {@code Evento} L'evento salvato.
     */
    public Evento salvaEvento(Evento evento){
        return eventoRepository.save(evento);
    }

    /**
     * <h2>Crea un nuovo evento di tipo fiera</h2>
     * <br>
     * Questo metodo consente di creare un evento di tipo fiera.
     * Utilizza `EventoDirector` per costruire l'evento con i dettagli forniti.
     *
     * @param titolo Il titolo dell'evento.
     * @param descrizione La descrizione dell'evento.
     * @param inizio La data e ora di inizio dell'evento.
     * @param fine La data e ora di fine dell'evento.
     * @param locandina Il file immagine della locandina dell'evento.
     * @param indirizzo L'indirizzo in cui si svolgerà l'evento.
     * @param aziende La lista di aziende partecipanti alla fiera.
     * @return {@code Long} L'ID dell'evento di tipo fiera creato.
     */
    public Long creaFiera(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            List<Azienda> aziende
    ){
        EventoDirector eventoDirector = new EventoDirector();
        Utente creatore = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
        Evento evento = eventoDirector.creaFieraCompleta(
                titolo, descrizione, inizio, fine, locandina, indirizzo, creatore, aziende
        );
        return salvaEvento(evento).getId();

    }
/**
     * <h2>Crea un nuovo evento di tipo visita</h2>
     * <br>
     * Questo metodo consente di creare un evento di tipo visita aziendale.
     * Utilizza `EventoDirector` per costruire l'evento con i dettagli forniti.
     *
     * @param titolo Il titolo dell'evento.
     * @param descrizione La descrizione dell'evento.
     * @param inizio La data e ora di inizio dell'evento.
     * @param fine La data e ora di fine dell'evento.
     * @param locandina Il file immagine della locandina dell'evento.
     * @param indirizzo L'indirizzo in cui si svolgerà l'evento.
     * @param aziendaRiferimento L'azienda di riferimento per la visita.
     * @return {@code Long} L'ID dell'evento di tipo visita creato.
     */
    public Long creaVisita(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            Azienda aziendaRiferimento
    ){
        EventoDirector eventoDirector = new EventoDirector();
        Utente creatore = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
        Evento evento = eventoDirector.creaVisitaCompleta(
                titolo, descrizione, inizio, fine, locandina, indirizzo, creatore, aziendaRiferimento
        );
        return salvaEvento(evento).getId();
    }

    /**
     * <h2>Iscrive un utente a un evento di tipo visita</h2>
     * <br>
     * Questo metodo consente a un utente di iscriversi a un evento di tipo visita.
     * Controlla se l'utente è già iscritto,
     * se l'evento è in corso o concluso e se è di tipo visita.
     *
     * @param idEvento l'ID dell'evento a cui iscrivere l'utente
     * @return {@code true} se l'iscrizione è avvenuta con successo, {@code false} altrimenti
     */
    public boolean iscriviUtenteAEvento(Long idEvento) {
        Long idUtente = utenteService.getIdUtenteAutenticato();
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + idEvento + " non trovato"));
        if (!(evento instanceof EventoVisita))
            throw new IllegalArgumentException("L'evento non è di tipo visita");
        EventoVisita eventoVisita = (EventoVisita) evento;
        if (eventoVisita.getIscritti().contains(idUtente)) {
            throw new IllegalArgumentException("L'utente è già iscritto a questo evento");
        }
        switch (evento.getStatus()) {
            case CONCLUSO:
                throw new IllegalArgumentException("L'evento è già terminato");
            case PROPOSTO:
                throw new IllegalArgumentException("L'evento non è ancora programmato");
            case IN_CORSO:
                throw new IllegalArgumentException("L'evento è già in corso");
            default:
                break;
        }
        return aggiungiIscrizione(eventoVisita,idUtente);

    }

    /**
     * <h2>Aggiunge un utente a un evento di tipo visita</h2>
     * <br>
     * Questo metodo consente di aggiungere un utente alla lista di iscritti
     * dell'evento visita.
     * recupera le informazioni dell'utente dal DB e le aggiunge alla lista di iscritti
     *
     * @param visita l'evento di tipo visita
     * @param idUtente l'ID dell'utente da iscrivere
     * @return {@code true} se l'iscrizione è avvenuta con successo, {@code false} altrimenti
     */
    public boolean aggiungiIscrizione (EventoVisita visita, Long idUtente) {
        Utente utente = utenteService.getUtenteById(idUtente);
        visita.getIscritti().add(utente);
        eventoRepository.save(visita);
        return true;
    }


}
