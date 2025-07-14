package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoEstesoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.FieraEstesaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.VisitaEstesaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.IscrittoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.IndirizzoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ServizioEmail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;
    private final UtenteService utenteService;
    private final AziendaService aziendaService;
    private final ServizioEmail emailService;
    private final IndirizzoRepository indirizzoRepository;

    public EventoService(EventoRepository eventoRepository, UtenteService utenteService, AziendaService aziendaService, ImplementazioneServizioMail emailService, IndirizzoRepository indirizzoRepository) {
        this.eventoRepository = eventoRepository;
        this.utenteService = utenteService;
        this.aziendaService = aziendaService;
        this.emailService = emailService;
        this.indirizzoRepository = indirizzoRepository;
    }

    /**
     * <h2>Recupera tutti gli eventi ordinati alfabeticamente o in base a un campo
     * specifico</h2>
     * <br>
     * Questo metodo restituisce una lista completa di eventi disponibili sulla
     * piattaforma,
     * inclusi sia fiere che visite, ordinati in base al campo specificato (di
     * default è il titolo).
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo" (default), "inizio".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (default),
     *               "desc".
     * @return {@code List<Evento>} contenente tutti gli eventi (fiere e visite),
     *         ordinati in base al campo e all'ordine specificati.
     */
    public List<EventoEstesoDTO> getAllEventi(String sortBy, String order) {
        List<Evento> eventi = new ArrayList<>(eventoRepository.findAll());

        Comparator<Evento> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(Evento::getTitolo);
            case "inizio" -> Comparator.comparing(Evento::getInizio);
            default -> Comparator.comparing(Evento::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi di tipo visita</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che rappresentano visite
     * aziendali,
     * ordinati in base al campo specificato e all'ordine fornito.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoVisita>} contenente tutti gli eventi di tipo visita
     *         presenti nel database.
     */
    public List<EventoEstesoDTO> getAllVisita(String sortBy, String order) {
        List<EventoVisita> eventi = new ArrayList<>(eventoRepository.findAllVisita());
        Comparator<EventoVisita> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoVisita::getTitolo);
            case "inizio" -> Comparator.comparing(EventoVisita::getInizio);
            default -> Comparator.comparing(EventoVisita::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi di tipo fiera</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che rappresentano fiere
     * territoriali,
     * ordinati in base al campo specificato e all'ordine fornito.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoFiera>} contenente tutti gli eventi di tipo fiera
     *         presenti nel database.
     */
    public List<EventoEstesoDTO> getAllFiera(String sortBy, String order) {
        List<EventoFiera> eventi = new ArrayList<>(eventoRepository.findAllFiera());
        Comparator<EventoFiera> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoFiera::getTitolo);
            case "inizio" -> Comparator.comparing(EventoFiera::getInizio);
            default -> Comparator.comparing(EventoFiera::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera un evento specifico in base al suo ID</h2>
     * <br>
     * Questo metodo restituisce un evento specifico in base all'ID fornito
     * comprensivo di tutti i dettagli (lista iscritti, azienda referente, aziende
     * collegate).
     *
     * @param id L'ID dell'evento da recuperare.
     * @return {@code Evento} l'evento corrispondente all'ID fornito.
     * @throws NoSuchElementException se l'evento con l'ID specificato non esiste.
     */
    public EventoEstesoDTO getEventoById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + id + " non trovato"));
        return outputFormatEvento(evento);
    }

    public EventoEstesoDTO getEventoProgrammatoById(Long id) {
        Evento evento = eventoRepository.findByIdAndProgrammato(id)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + id + " non trovato"));
        if (evento.getStatusEvento() != StatusEvento.PROGRAMMATO) {
            throw new IllegalArgumentException("L'evento non è programmato");
        }
        return outputFormatEvento(evento);
    }

    /**
     * <h2>Formatta l'evento in un DTO esteso</h2>
     *
     * Questo metodo converte un evento in un DTO esteso specifico
     * verificando se si tratta di un'istanza di EventoFiera o EventoVisita
     * e restituisce il tipo appropriato.
     * 
     * @param evento L'evento da formattare in un DTO esteso.
     * @return {@code EventoEstesoDTO} il DTO esteso dell'evento in formato
     *         FieraEstesaDTO o VisitaEstesaDTO.
     */
    private EventoEstesoDTO outputFormatEvento(Evento evento) {
        if (evento instanceof EventoFiera) {
            return new FieraEstesaDTO((EventoFiera) evento);
        } else if (evento instanceof EventoVisita) {
            return new VisitaEstesaDTO((EventoVisita) evento);
        } else {
            return new EventoEstesoDTO(evento);
        }
    }

    /**
     * <h2>Recupera eventi in base al titolo</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che contengono il parametro
     * specificato
     * oppure il singolo evento con il titolo corrispondente.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<Evento>} lista di eventi che contengono il titolo
     *         specificato.
     */
    public List<EventoEstesoDTO> getEventiByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return getAllEventi("titolo", "asc");
        }
        return eventoRepository.findByTitleContainingParameter(title.toLowerCase())
                .stream()
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi creati dall'utente autenticato</h2>
     * <br>
     * Questo metodo utilizza il contesto di sicurezza per identificare l'utente
     * attualmente autenticato
     * e restituisce la lista di eventi (fiere e visite) da lui creati, ordinati in
     * base al campo e all'ordine specificati.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<Evento>} contenente tutti gli eventi creati dall'utente
     *         autenticato.
     * @throws RuntimeException se l'utente non è autenticato o non è presente nel
     *                          database.
     */
    public List<EventoEstesoDTO> getEventiByCreatore(String sortBy, String order) {
        Long idUtente = utenteService.getIdUtenteAutenticato();
        List<Evento> eventi = new ArrayList<>(eventoRepository.findByCreatoreId(idUtente));
        Comparator<Evento> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(Evento::getTitolo);
            case "inizio" -> Comparator.comparing(Evento::getInizio);
            default -> Comparator.comparing(Evento::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera eventi in base al titolo e all'ID del creatore</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che contengono il parametro
     * specificato
     * e sono stati creati da un utente specifico.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<Evento>} lista di eventi che contengono il titolo
     *         specificato
     *         e sono stati creati dall'utente con l'ID fornito.
     */
    public List<EventoEstesoDTO> getEventiByCreatoreAndTitle(String title) {
        long idCreatore = utenteService.getIdUtenteAutenticato();
        if (title == null || title.isEmpty()) {
            return getEventiByCreatore("titolo", "asc");
        }
        List<Evento> eventi = eventoRepository.findByTitleContainingParameterAndCreatoreId(title, idCreatore);
        return eventi.stream()
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi di tipo visita creati dall'utente
     * autenticato</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che rappresentano visite
     * aziendali,
     * ordinati in base al campo specificato e all'ordine fornito.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoEstesoDTO>} contenente tutti gli eventi di tipo
     *         visita creati dall'utente autenticato.
     */
    public List<EventoEstesoDTO> getVisiteByCreatore(String sortBy, String order) {
        long idCreatore = utenteService.getIdUtenteAutenticato();
        List<EventoVisita> eventi = new ArrayList<>(eventoRepository.findAllVisitaByCreatore(idCreatore));
        Comparator<EventoVisita> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoVisita::getTitolo);
            case "inizio" -> Comparator.comparing(EventoVisita::getInizio);
            default -> Comparator.comparing(EventoVisita::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    public List<EventoEstesoDTO> getFieraByCreatore(String sortBy, String order) {
        long idCreatore = utenteService.getIdUtenteAutenticato();
        List<EventoFiera> eventi = new ArrayList<>(eventoRepository.findAllFieraByCreatore(idCreatore));
        Comparator<EventoFiera> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoFiera::getTitolo);
            case "inizio" -> Comparator.comparing(EventoFiera::getInizio);
            default -> Comparator.comparing(EventoFiera::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi (fiere e visite)</h2>
     * <br>
     * Questo metodo restituisce una lista semplificata di tutti gli eventi presenti
     * sulla piattaforma,
     * inclusi sia fiere che visite, con informazioni essenziali: titolo, locandina,
     * data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo" (default), "inizio".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime di tutti gli
     *         eventi.
     */
    public List<EventoPreviewDTO> getAnteprimeEventi(String sortBy, String order) {
        return getAllEventi(sortBy, order).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutte le fiere territoriali</h2>
     * <br>
     * Questo metodo restituisce una lista semplificata di eventi di tipo fiera,
     * utile per schermate riepilogative o liste.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime degli eventi
     *         di tipo fiera.
     */
    public List<EventoPreviewDTO> getAnteprimeFiera(String sortBy, String order) {
        return getAllFiera(sortBy, order).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutte le visite aziendali</h2>
     * <br>
     * Questo metodo restituisce una lista semplificata di eventi di tipo visita
     * aziendale,
     * utile per schermate riepilogative o liste.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime degli eventi
     *         di tipo visita.
     */
    public List<EventoPreviewDTO> getAnteprimeVisite(String sortBy, String order) {
        return getAllVisita(sortBy, order).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera eventi in base al titolo</h2>
     * <br>
     * Questo metodo restituisce l'elenco semplificato di eventi che contengono il
     * parametro specificato
     * con i dati essenziali per l'interfaccia utente.
     *
     * @param titolo Il titolo da cercare negli eventi.
     * @return {@code List<EventoPreviewDTO>} anteprime di eventi programmati che
     *         contengono il titolo specificato.
     */
    public List<EventoPreviewDTO> getAnteprimaEventiPerTitolo(String titolo) {
        if (titolo == null || titolo.isEmpty()) {
            return getAnteprimeEventi("titolo", "asc");
        }
        return eventoRepository.findByTitleContainingParameter(titolo).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi programmati di tipo visita</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi di tipo visita programmati.
     *
     * @return {@code List<EventoVisita>} contenente tutti gli eventi di tipo visita
     *         programmati.
     */
    public List<EventoEstesoDTO> getAllVisitaProgrammati(String sortBy, String order) {
        List<EventoVisita> eventi = new ArrayList<>(eventoRepository.findAllVisitaProgrammati());
        Comparator<EventoVisita> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoVisita::getTitolo);
            case "inizio" -> Comparator.comparing(EventoVisita::getInizio);
            default -> Comparator.comparing(EventoVisita::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi di tipo fiera programmati</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi di tipo fiera programmati.
     *
     * @return {@code List<EventoFiera>} contenente tutti gli eventi di tipo fiera
     *         programmati.
     */
    public List<EventoEstesoDTO> getAllFieraProgrammati(String sortBy, String order) {
        List<EventoFiera> eventi = new ArrayList<>(eventoRepository.findAllFieraProgrammati());
        Comparator<EventoFiera> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoFiera::getTitolo);
            case "inizio" -> Comparator.comparing(EventoFiera::getInizio);
            default -> Comparator.comparing(EventoFiera::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera tutti gli eventi programmati</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati.
     *
     * @return {@code List<Evento>} contenente tutti gli eventi programmati.
     */
    public List<EventoEstesoDTO> getAllProgrammati(String sortBy, String order) {
        List<Evento> eventi = new ArrayList<>(eventoRepository.findAllProgrammati());

        Comparator<Evento> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(Evento::getTitolo);
            case "inizio" -> Comparator.comparing(Evento::getInizio);
            default -> Comparator.comparing(Evento::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return eventi.stream()
                .sorted(comparator)
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());

    }

    /**
     * <h2>Recupera eventi programmati in base al titolo</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati che contengono
     * il parametro specificato.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<Evento>} lista di eventi programmati che contengono il
     *         titolo specificato.
     */
    public List<EventoEstesoDTO> getEventiProgrammatiByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return getAllProgrammati("titolo", "asc");
        }
        return eventoRepository.findByTitleContainingParameterAndStatus(title)
                .stream()
                .map(EventoEstesoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutti gli eventi programmati</h2>
     * <br>
     * Questo metodo restituisce una lista semplificata di eventi programmati con
     * informazioni essenziali
     * per l'interfaccia utente, come titolo, locandina, data di inizio e fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo" (default), "inizio".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime di eventi
     *         programmati.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiProgrammati(String sortBy, String order) {
        return getAllProgrammati(sortBy, order).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutte le visite programmate</h2>
     * <br>
     * Questo metodo restituisce una lista semplificata di visite programmate con
     * informazioni essenziali,
     * come titolo, locandina, data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime di visite
     *         programmate.
     */
    public List<EventoPreviewDTO> getAnteprimeVisiteProgrammate(String sortBy, String order) {
        return getAllVisitaProgrammati(sortBy, order).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima di tutte le fiere programmate</h2>
     * <br>
     * Questo metodo restituisce una lista semplificata di fiere programmate con
     * informazioni essenziali,
     * come titolo, locandina, data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati:
     *               "titolo", "inizio", "id".
     * @param order  L'ordine di ordinamento. Valori supportati: "asc" (crescente),
     *               "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime di fiere
     *         programmate.
     */
    public List<EventoPreviewDTO> getAnteprimeFiereProgrammate(String sortBy, String order) {
        return getAllFieraProgrammati(sortBy, order).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera l'anteprima degli eventi programmati in base al titolo</h2>
     * <br>
     * Questo metodo restituisce una lista di anteprime degli eventi programmati che
     * contengono
     * il titolo specificato.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime degli eventi
     *         programmati che contengono il titolo specificato.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiProgrammatiByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return getAnteprimeEventiProgrammati("titolo", "asc");
        }
        return eventoRepository.findByTitleContainingParameterAndStatus(title).stream()
                .map(evento -> new EventoPreviewDTO(
                        evento.getId(),
                        evento.getTitolo(),
                        evento.getLocandina(),
                        evento.getInizio(),
                        evento.getFine()))
                .collect(Collectors.toList());
    }

    /**
     * <h2>Salva un evento nel database</h2>
     * <br>
     * Questo metodo consente di salvare un evento (di tipo fiera o visita) nel
     * database.
     *
     * @param evento L'evento da salvare.
     * @return {@code Evento} L'evento salvato.
     */
    public Evento salvaEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    /**
     * <h2>Cancella un evento</h2>
     * <br>
     * Questo metodo consente di eliminare un evento specifico dal database.
     * Se l'utente autenticato è il creatore dell'evento, viene eseguita
     * una cancellazione virtuale dell'evento, che aggiorna il suo stato a "ELIMINATO".
     * Se l'utente non è autorizzato, viene sollevata un'eccezione.
     *
     * @param idEvento L'ID dell'evento da eliminare.
     * @throws NoSuchElementException se l'evento o l'utente autenticato non vengono trovati.
     * @throws IllegalArgumentException se l'utente autenticato non è autorizzato a eliminare l'evento.
     */
    public void cancellaEvento(Long idEvento) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + idEvento + " non trovato"));

        Utente loggato = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato())
                .orElseThrow(() -> new NoSuchElementException("Utente non autenticato o non trovato"));
        Utente creatore = evento.getCreatore();
        if (creatore != null && creatore == loggato) {
            eliminaEventoVirtuale(idEvento);
        }
        else throw new IllegalArgumentException("Non sei autorizzato a cancellare questo evento");
    }

    /**
     * <h2>Elimina un evento virtualmente</h2>
     * <br>
     * Questo metodo consente di eliminare un evento virtualmente, aggiornando il suo stato a "ELIMINATO".
     * Se l'evento è di tipo visita e ha iscritti, rimuove l'associazione con gli utenti iscritti e invia
     * una notifica via email della cancellazione. Se l'evento è di tipo fiera, rimuove l'associazione con
     * le aziende collegate. Per altri tipi di evento, aggiorna semplicemente lo stato.
     *
     * @param idEvento L'ID dell'evento da eliminare virtualmente.
     * @throws NoSuchElementException se l'evento con l'ID specificato non viene trovato.
     */
    public void eliminaEventoVirtuale(Long idEvento) {
        Evento evento = eventoRepository.findByIdNonAutorizzato(idEvento)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + idEvento + " non trovato"));

        if (evento instanceof EventoVisita) {
            EventoVisita visita = (EventoVisita) evento;

            // Controlla se la visita ha iscritti
            if (!visita.getIscritti().isEmpty()) {
                List<Utente> utentiIscritti = new ArrayList<>(visita.getIscritti());

                // Rimuove l'associazione tra la visita e gli utenti iscritti
                visita.getIscritti().clear();
                eventoRepository.save(visita);

                // Aggiorna lo stato della visita a "ELIMINATO"
                visita.setStatus(Status.ELIMINATO);
                eventoRepository.save(visita);

                // Invia una notifica via mail agli utenti iscritti della cancellazione
                List<String> emailUtenti = utentiIscritti.stream()
                        .map(Utente::getEmail)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                inviaEmailNotificaCancellazioneEvento(emailUtenti, visita.getTitolo());
            } else {
                // Aggiorna lo stato della visita a "ELIMINATO" se non ci sono iscritti
                visita.setStatus(Status.ELIMINATO);
                eventoRepository.save(visita);
            }
        }
        else if (evento instanceof EventoFiera) {
            EventoFiera fiera = (EventoFiera) evento;

            // Rimuove l'associazione tra la fiera e le aziende collegate
            fiera.getAziendePresenti().clear();
            eventoRepository.save(fiera);

            // Aggiorna lo stato della fiera a "ELIMINATO"
            fiera.setStatus(Status.ELIMINATO);
            eventoRepository.save(fiera);

        }
        else {
            // Aggiorna lo stato a "ELIMINATO" per altri tipi di evento
            evento.setStatus(Status.ELIMINATO);
            eventoRepository.save(evento);
        }
    }

    /**
     * <h2>Elimina un evento fisicamente</h2>
     * <br>
     * Questo metodo consente di eliminare un evento dal database in modo permanente.
     * Prima di procedere con l'eliminazione, verifica che l'evento sia stato eliminato
     * virtualmente (stato impostato su "ELIMINATO").
     * Se l'evento non è stato eliminato virtualmente, viene sollevata un'eccezione.
     *
     * @param idEvento L'ID dell'evento da eliminare fisicamente.
     * @throws NoSuchElementException se l'evento con l'ID specificato non viene trovato.
     * @throws IllegalStateException se l'evento non è stato eliminato virtualmente.
     */
    public void eliminaEventoFisico(Long idEvento) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + idEvento + " non trovato"));

        // Check if the event has been virtually deleted
        if (evento.getStatus() == Status.ELIMINATO) {
            eventoRepository.deleteById(idEvento);
        } else {
            throw new IllegalStateException("L'evento non è stato eliminato virtualmente. Stato attuale: " + evento.getStatus());
        }
    }

    private void inviaEmailNotificaCancellazioneEvento(List<String> emailUtenti, String titolo) {
        if (emailUtenti == null || emailUtenti.isEmpty()) {
            return; // No emails to send
        }

        String oggetto = "Cancellazione evento: " + titolo;
        String messaggio = "Gentile utente,\n\nL'evento \"" + titolo + "\" a cui era iscritto è stato cancellato. Ci scusiamo per il disagio.\n\nCordiali saluti,\nIl team organizzativo.";

        for (String email : emailUtenti) {
            emailService.inviaMail(email, messaggio, oggetto);
        }
    }

    /**
     * <h2>Crea un nuovo evento di tipo fiera</h2>
     * <br>
     * Questo metodo permette di creare un evento di tipo fiera.
     * Utilizza la classe `EventoDirector` per costruire l'evento con i dettagli forniti.
     *
     * @param titolo      Il titolo dell'evento.
     * @param descrizione La descrizione dell'evento.
     * @param inizio      La data e ora di inizio dell'evento.
     * @param fine        La data e ora di fine dell'evento.
     * @param locandina   Il file immagine della locandina dell'evento.
     * @param indirizzo   L'indirizzo in cui si svolgerà l'evento.
     * @param aziende     La lista di aziende partecipanti alla fiera.
     * @return {@code Long} L'ID dell'evento di tipo fiera creato.
     * @throws IllegalArgumentException se l'indirizzo è nullo o se i dati delle aziende esterne non sono validi.
     */
    public Long creaFiera(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            List<Azienda> aziende) {
        if (indirizzo == null) {
            throw new IllegalArgumentException("L'indirizzo non può essere nullo.");
        }
        indirizzoRepository.save(indirizzo);

        for (Azienda azienda : aziende) {
            if (azienda.getId() < 1) { // Azienda esterna
                if (azienda.getDenominazioneSociale() == null || azienda.getDenominazioneSociale().isEmpty()) {
                    throw new IllegalArgumentException("Denominazione sociale obbligatoria per aziende esterne.");
                }
                if (azienda.getSedeLegale() == null) {
                    throw new IllegalArgumentException("Sede legale obbligatoria per aziende esterne.");
                }
                indirizzoRepository.save(azienda.getSedeLegale()); // Associa l'indirizzo salvato
                aziendaService.saveAzienda(azienda); // Salva l'azienda
            }
        }

        EventoDirector eventoDirector = new EventoDirector();
        Optional<Utente> creatore = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
        Evento evento = eventoDirector.creaFieraCompleta(
                titolo, descrizione, inizio, fine, locandina, indirizzo, creatore, aziende);
        evento.setStatus(Status.PENDING);
        return salvaEvento(evento).getId();
    }

    /**
     * <h2>Crea un nuovo evento di tipo visita </h2>
     * <br>
     * Questo metodo consente di creare un evento di tipo visita .
     * Utilizza `EventoDirector` per costruire l'evento con i dettagli forniti.
     *
     * @param titolo             Il titolo dell'evento.
     * @param descrizione        La descrizione dell'evento.
     * @param inizio             La data e l'ora di inizio dell'evento.
     * @param fine               La data e l'ora di fine dell'evento.
     * @param locandina          Il file immagine della locandina dell'evento.
     * @param indirizzo          L'indirizzo in cui si svolgerà l'evento.
     * @param aziendaRiferimento L'azienda di riferimento per la visita.
     * @return {@code Long} L'ID dell'evento di tipo visita creato.
     * @throws IllegalArgumentException se l'indirizzo è nullo o i dettagli dell'azienda di riferimento sono invalidi.
     */
    public Long creaVisita(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            Azienda aziendaRiferimento) {
        // Verifica che i campi dell'indirizzo siano stati inseriti correttamente
        if (indirizzo == null) {
            throw new IllegalArgumentException("L'indirizzo non può essere nullo.");
        }
        indirizzoRepository.save(indirizzo); // Salva l'indirizzo

        // Verifica se l'azienda di riferimento è interna o esterna
        if (aziendaRiferimento.getId() < 1) {
            if (aziendaRiferimento.getDenominazioneSociale() == null || aziendaRiferimento.getDenominazioneSociale().isEmpty()) {
                throw new IllegalArgumentException("Denominazione sociale obbligatoria per aziende esterne.");
            }
            aziendaRiferimento.setSedeLegale(indirizzo); // Associate the saved Indirizzo
            aziendaService.saveAzienda(aziendaRiferimento); // Save the Azienda entity
        } else {
            // Retrieve existing Azienda
            long idAziendaRiferimento = aziendaRiferimento.getId();
            Azienda aziendaEffettiva = aziendaService.getAziendaById(idAziendaRiferimento)
            .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata con ID: " + idAziendaRiferimento));
            if (!confrontaIndirizzi(indirizzo,aziendaEffettiva.getSedeLegale())) {
                throw new IllegalArgumentException("L'indirizzo deve corrispondere alla sede legale dell'azienda di riferimento.");
            }
            aziendaRiferimento = aziendaEffettiva;
        }

        EventoDirector eventoDirector = new EventoDirector();
        Optional<Utente> creatore = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
        Evento evento = eventoDirector.creaVisitaCompleta(
                titolo, descrizione, inizio, fine, locandina, indirizzo, creatore, aziendaRiferimento);
        evento.setStatus(Status.PENDING);
        return salvaEvento(evento).getId();
    }

    private boolean confrontaIndirizzi(Indirizzo indirizzo1, Indirizzo indirizzo2) {
        if (indirizzo1 == null || indirizzo2 == null) {
            return false;
        }
        return Objects.equals(indirizzo1.getVia().toLowerCase(), indirizzo2.getVia().toLowerCase()) &&
               Objects.equals(indirizzo1.getNumeroCivico(), indirizzo2.getNumeroCivico()) &&
               Objects.equals(indirizzo1.getCitta().toLowerCase(), indirizzo2.getCitta().toLowerCase()) &&
               Objects.equals(indirizzo1.getCap(), indirizzo2.getCap()) &&
               Objects.equals(indirizzo1.getProvincia().toLowerCase(), indirizzo2.getProvincia().toLowerCase()) &&
               Objects.equals(indirizzo1.getCoordinate(), indirizzo2.getCoordinate());
    }

    /**
     * <h2>Crea una visita aziendale</h2>
     * <br>
     * Questo metodo consente di creare un evento di tipo visita aziendale
     * utilizzando i dati dell'azienda associata all'utente autenticato.
     * Recupera l'azienda e il suo indirizzo legale, quindi delega la creazione
     * dell'evento al metodo `creaVisita`.
     *
     * @param titolo      Il titolo dell'evento.
     * @param descrizione La descrizione dell'evento.
     * @param inizio      La data e ora di inizio dell'evento.
     * @param fine        La data e ora di fine dell'evento.
     * @param locandina   Il file immagine della locandina dell'evento.
     * @return {@code Long} L'ID dell'evento di tipo visita creato.
     * @throws IllegalArgumentException se l'azienda associata all'utente autenticato non viene trovata.
     */
    public Long creaVisitaAzienda(String titolo,
                                  String descrizione,
                                  LocalDateTime inizio,
                                  LocalDateTime fine,
                                  File locandina) {
        Azienda azienda = aziendaService.getAziendaById(utenteService.getIdUtenteAutenticato())
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata per l'utente autenticato"));
        Indirizzo indirizzo = azienda.getSedeLegale();
        return creaVisita(titolo, descrizione, inizio, fine, locandina, indirizzo, azienda);
    }

    /**
     * <h2>Recupera gli iscritti a un evento di tipo visita</h2>
     * <br>
     * Questo metodo restituisce la lista degli utenti iscritti a un evento di tipo visita.
     * Se l'evento non esiste o non è di tipo visita, viene restituito un errore.
     * Inoltre, verifica che l'utente autenticato abbia i permessi necessari per visualizzare gli iscritti.
     *
     * @param idVisita L'ID dell'evento di tipo visita.
     * @return {@code ResponseEntity<List<IscrittoDTO>>} contenente la lista degli iscritti
     *         o un messaggio di errore in caso di problemi.
     */
    public ResponseEntity<List<IscrittoDTO>> getIscrittiEvento(long idVisita) {
        Evento evento = eventoRepository.findById(idVisita)
                .orElse(null);
        if (evento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        if (!(evento instanceof EventoVisita visita)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        if (!checkCreator(visita)) {
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Non hai i permessi necessari per visualizzare gli iscritti a questo evento");
        }
        List<Utente> iscritti = ((EventoVisita) evento).getIscritti();
        if (iscritti == null || iscritti.isEmpty()) {
            iscritti = new ArrayList<>();
        }
        return ResponseEntity.ok(iscritti
                .stream()
                .map(IscrittoDTO::new)
                .collect(Collectors.toList()));
    }

    /**
     * <h2>Verifica il creatore di un evento di tipo visita</h2>
     * <br>
     * Questo metodo controlla se l'utente autenticato ha i permessi per
     * visualizzare un evento di tipo visita.
     * Se l'utente è un gestore, ha sempre accesso. Altrimenti, verifica se l'utente
     * è il creatore dell'evento.
     *
     * @param evento L'evento di tipo visita da verificare.
     * @return {@code true} se l'utente autenticato ha i permessi per visualizzare
     *         l'evento, {@code false} altrimenti.
     */
    public boolean checkCreator(EventoVisita evento) {
        Optional<Utente> autenticato = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
        if (autenticato.isPresent()) {
            if (Objects.requireNonNull(autenticato.get().getRuolo()) == Ruolo.GESTORE) {
                return true; // Il gestore può sempre visualizzare gli eventi
            }
        }
        else throw new RuntimeException("Utente non autenticato o non trovato nel database");
        return evento.getCreatore().getId() == autenticato.get().getId(); // L'utente è il creatore dell'evento
    }

    /**
     * <h2>Iscrive un utente a un evento di tipo visita</h2>
     * <br>
     * Questo metodo consente a un utente di iscriversi a un evento di tipo visita.
     * Controlla se l'utente è già iscritto, se l'evento è in corso o concluso, e
     * verifica che sia di tipo visita.
     *
     * @param idEvento L'ID dell'evento a cui iscrivere l'utente.
     * @return {@code ResponseEntity<String>} Messaggio di conferma o errore.
     */
    public ResponseEntity<String> iscriviUtente(Long idEvento) {
        Long idUtente = utenteService.getIdUtenteAutenticato();
        Utente utente = utenteService.getUtenteById(idUtente)
                .orElse(null);
        Evento evento = eventoRepository.findById(idEvento)
                .orElse(null);

        if (evento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Evento con ID " + idEvento + " non trovato");
        }

        if (!(evento instanceof EventoVisita eventoVisita)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("L'evento non è di tipo visita");
        }

        if (eventoVisita.getIscritti().contains(utente)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("L'utente è già iscritto a questo evento");
        }

        switch (evento.getStatusEvento()) {
            case CONCLUSO:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("L'evento è già terminato");
            case PROPOSTO:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("L'evento non è ancora programmato");
            case IN_CORSO:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("L'evento è già in corso");
            case PROGRAMMATO:
                boolean success = aggiungiIscrizione(eventoVisita, idUtente);
                if (success) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Iscrizione avvenuta con successo");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Errore durante l'iscrizione");
                }
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Stato dell'evento non valido");
        }
    }

    /**
     * <h2>Aggiunge un utente a un evento di tipo visita</h2>
     * <br>
     * Questo metodo consente di aggiungere un utente alla lista di iscritti
     * dell'evento visita.
     * recupera le informazioni dell'utente dal DB e le aggiunge alla lista di
     * iscritti
     *
     * @param visita   l'evento di tipo visita
     * @param idUtente l'ID dell'utente da iscrivere
     * @return {@code true} se l'iscrizione è avvenuta con successo, {@code false}
     *         altrimenti
     */
    public boolean aggiungiIscrizione(EventoVisita visita, Long idUtente) {
                Optional<Utente> utente = utenteService.getUtenteById(idUtente);
                if (utente.isPresent()) {
                    visita.getIscritti().add(utente.get());
                } else {
                    return false;
                }
                eventoRepository.save(visita);
                return true;
            }

}
