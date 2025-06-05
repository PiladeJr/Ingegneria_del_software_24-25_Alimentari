package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoEstesoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.FieraEstesaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.VisitaEstesaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.IscrittoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
     * <h2>Recupera tutti gli eventi ordinati alfabeticamente o in base a un campo specifico</h2>
     * <br>
     * Questo metodo restituisce una lista completa di eventi disponibili sulla piattaforma,
     * inclusi sia fiere che visite, ordinati in base al campo specificato (di default è il titolo).
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo" (default), "dataInizio".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (default), "desc".
     * @return {@code List<Evento>} contenente tutti gli eventi (fiere e visite),
     *         ordinati in base al campo e all'ordine specificati.
     */
    public List<EventoEstesoDTO> getAllEventi(String sortBy, String order) {
        List<Evento> eventi = new ArrayList<>(eventoRepository.findAll());

        Comparator<Evento> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(Evento::getTitolo);
            case "data inizio" -> Comparator.comparing(Evento::getInizio);
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
     * Questo metodo restituisce una lista di eventi che rappresentano visite aziendali,
     * ordinati in base al campo specificato e all'ordine fornito.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoVisita>} contenente tutti gli eventi di tipo visita presenti nel database.
     */
    public List<EventoEstesoDTO> getAllVisita(String sortBy, String order) {
        List<EventoVisita> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findAllVisita());
        Comparator<EventoVisita> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoVisita::getTitolo);
            case "dataInizio" -> Comparator.comparing(EventoVisita::getInizio);
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
     * Questo metodo restituisce una lista di eventi che rappresentano fiere territoriali,
     * ordinati in base al campo specificato e all'ordine fornito.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoFiera>} contenente tutti gli eventi di tipo fiera presenti nel database.
     */
    public List<EventoEstesoDTO> getAllFiera(String sortBy, String order) {
        List<EventoFiera> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findAllFiera());
        Comparator<EventoFiera> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoFiera::getTitolo);
            case "dataInizio" -> Comparator.comparing(EventoFiera::getInizio);
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
     * comprensivo di tutti i dettagli (lista iscritti, azienda referente, aziende collegate).
     *
     * @param id L'ID dell'evento da recuperare.
     * @return {@code Evento} l'evento corrispondente all'ID fornito.
     * @throws NoSuchElementException se l'evento con l'ID specificato non esiste.
     */
    public EventoEstesoDTO getEventoById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + id + " non trovato"));

        if (evento instanceof EventoFiera) {
            return new FieraEstesaDTO((EventoFiera) evento);
        } else if (evento instanceof EventoVisita) {
            return new VisitaEstesaDTO((EventoVisita) evento); // crea VisitaEstesaDTO se non esiste
        } else {
            return new EventoEstesoDTO(evento);
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
     * Questo metodo utilizza il contesto di sicurezza per identificare l'utente attualmente autenticato
     * e restituisce la lista di eventi (fiere e visite) da lui creati, ordinati in base al campo e all'ordine specificati.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<Evento>} contenente tutti gli eventi creati dall'utente autenticato.
     * @throws RuntimeException se l'utente non è autenticato o non è presente nel database.
     */
    public List<EventoEstesoDTO> getEventiByCreatore(String sortBy, String order) {
        Long idUtente = utenteService.getIdUtenteAutenticato();
        List<Evento> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findByCreatoreId(idUtente));
        Comparator<Evento> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(Evento::getTitolo);
            case "dataInizio" -> Comparator.comparing(Evento::getInizio);
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
     * Questo metodo restituisce una lista di eventi che contengono il parametro specificato
     * e sono stati creati da un utente specifico.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<Evento>} lista di eventi che contengono il titolo specificato
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
     * <h2>Recupera tutti gli eventi di tipo visita creati dall'utente autenticato</h2>
     * <br>
     * Questo metodo restituisce una lista di eventi che rappresentano visite aziendali,
     * ordinati in base al campo specificato e all'ordine fornito.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoEstesoDTO>} contenente tutti gli eventi di tipo visita creati dall'utente autenticato.
     */
    public List<EventoEstesoDTO> getVisiteByCreatore(String sortBy, String order) {
        long idCreatore = utenteService.getIdUtenteAutenticato();
        List<EventoVisita> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findAllVisitaByCreatore(idCreatore));
        Comparator<EventoVisita> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoVisita::getTitolo);
            case "dataInizio" -> Comparator.comparing(EventoVisita::getInizio);
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
        List<EventoFiera> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findAllFieraByCreatore(idCreatore));
        Comparator<EventoFiera> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoFiera::getTitolo);
            case "dataInizio" -> Comparator.comparing(EventoFiera::getInizio);
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
     * Questo metodo restituisce una lista semplificata di tutti gli eventi presenti sulla piattaforma,
     * inclusi sia fiere che visite, con informazioni essenziali: titolo, locandina, data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo" (default), "dataInizio".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime di tutti gli eventi.
     */
    public List<EventoPreviewDTO> getAnteprimeEventi(String sortBy, String order) {
        return getAllEventi(sortBy, order).stream()
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
     * <h2>Recupera l'anteprima di tutte le fiere territoriali</h2>
     * <br>
     * Questo metodo restituisce una lista semplificata di eventi di tipo fiera,
     * utile per schermate riepilogative o liste.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime degli eventi di tipo fiera.
     */
    public List<EventoPreviewDTO> getAnteprimeFiera(String sortBy, String order) {
        return getAllFiera(sortBy, order).stream()
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
     * Questo metodo restituisce una lista semplificata di eventi di tipo visita aziendale,
     * utile per schermate riepilogative o liste.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} contenente le anteprime degli eventi di tipo visita.
     */
    public List<EventoPreviewDTO> getAnteprimeVisite(String sortBy, String order) {
        return getAllVisita(sortBy, order).stream()
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
        if (titolo == null || titolo.isEmpty()) {
            return getAnteprimeEventi("titolo", "asc");
        }
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
    public List<EventoEstesoDTO> getAllVisitaProgrammati(String sortBy, String order) {
        List<EventoVisita> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findAllVisitaProgrammati());
        Comparator<EventoVisita> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoVisita::getTitolo);
            case "dataInizio" -> Comparator.comparing(EventoVisita::getInizio);
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
     * @return {@code List<EventoFiera>} contenente tutti gli eventi di tipo fiera programmati.
     */
    public List<EventoEstesoDTO> getAllFieraProgrammati(String sortBy, String order) {
        List<EventoFiera> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findAllFieraProgrammati());
        Comparator<EventoFiera> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(EventoFiera::getTitolo);
            case "dataInizio" -> Comparator.comparing(EventoFiera::getInizio);
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
        List<Evento> eventi = new ArrayList<>();
        eventi.addAll(eventoRepository.findAllProgrammati());

        Comparator<Evento> comparator = switch (sortBy.toLowerCase()) {
            case "titolo" -> Comparator.comparing(Evento::getTitolo);
            case "dataInizio" -> Comparator.comparing(Evento::getInizio);
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
     * <h2>Recupera eventi programmati in base al titolo </h2>
     * <br>
     * Questo metodo restituisce una lista di eventi programmati che contengono
     * il parametro specificato.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<Evento>} lista di eventi programmati che contengono il titolo specificato.
     */
    public List<EventoEstesoDTO> getEventiProgrammatiByTitle(String title){
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
     * Questo metodo restituisce una lista semplificata di eventi programmati con informazioni essenziali
     * per l'interfaccia utente, come titolo, locandina, data di inizio e fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo" (default), "dataInizio".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime di eventi programmati.
     */
    public List<EventoPreviewDTO> getAnteprimeEventiProgrammati(String sortBy, String order) {
        return getAllProgrammati(sortBy, order).stream()
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
     * Questo metodo restituisce una lista semplificata di visite programmate con informazioni essenziali,
     * come titolo, locandina, data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime di visite programmate.
     */
    public List<EventoPreviewDTO> getAnteprimeVisiteProgrammate(String sortBy, String order) {
        return getAllVisitaProgrammati(sortBy,order).stream()
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
     * Questo metodo restituisce una lista semplificata di fiere programmate con informazioni essenziali,
     * come titolo, locandina, data di inizio e data di fine.
     *
     * @param sortBy Il campo su cui effettuare l'ordinamento. Valori supportati: "titolo", "dataInizio", "id".
     * @param order L'ordine di ordinamento. Valori supportati: "asc" (crescente), "desc" (decrescente).
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime di fiere programmate.
     */
    public List<EventoPreviewDTO> getAnteprimeFiereProgrammate(String sortBy, String order) {
        return getAllFieraProgrammati(sortBy,order).stream()
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
     * <h2>Recupera l'anteprima degli eventi programmati in base al titolo</h2>
     * <br>
     * Questo metodo restituisce una lista di anteprime degli eventi programmati che contengono
     * il titolo specificato.
     *
     * @param title Il titolo da cercare negli eventi.
     * @return {@code List<EventoPreviewDTO>} Lista di anteprime degli eventi programmati che contengono il titolo specificato.
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

    public List<IscrittoDTO> getIscrittiAdEventoCreato(long idVisita){
        Evento evento =  eventoRepository.findById(idVisita)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + idVisita + " non trovato"));
        if (!(evento instanceof EventoVisita))      //non è una visita
            throw new IllegalArgumentException("L'evento non è di tipo visita");
        EventoVisita visita = (EventoVisita) evento;
        if (!checkCreator(visita)) {
            throw new IllegalArgumentException("Non hai i permessi necessari per visualizzare gli iscritti a questo evento");
        }
        List<Utente> iscritti = ((EventoVisita) evento).getIscritti();
        if (iscritti == null || iscritti.isEmpty()) {
            iscritti = new ArrayList<>();
        }
        return iscritti
                .stream()
                .map(IscrittoDTO::new)
                .collect(Collectors.toList());
    }
    public boolean checkCreator(EventoVisita evento){
        Utente autenticato = utenteService.getUtenteById( utenteService.getIdUtenteAutenticato());
        switch (autenticato.getRuolo()){
            case GESTORE -> {
                return true; // Il gestore può sempre visualizzare gli eventi
            }
            default -> {
                if (evento.getCreatore().getId() == autenticato.getId()) {
                    return true; // L'utente è il creatore dell'evento
                } else {
                    return false;
                }
            }
        }

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
    public boolean iscriviUtente(Long idEvento) {
        Long idUtente = utenteService.getIdUtenteAutenticato();
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new NoSuchElementException("Evento con ID " + idEvento + " non trovato"));
        if (!(evento instanceof EventoVisita))
            throw new IllegalArgumentException("L'evento non è di tipo visita");
        EventoVisita eventoVisita = (EventoVisita) evento;
        if (eventoVisita.getIscritti().contains(idUtente)) {
            throw new IllegalArgumentException("L'utente è già iscritto a questo evento");
        }
        return switch (evento.getStatus()) {
            case CONCLUSO -> throw new IllegalArgumentException("L'evento è già terminato");
            case PROPOSTO -> throw new IllegalArgumentException("L'evento non è ancora programmato");
            case IN_CORSO -> throw new IllegalArgumentException("L'evento è già in corso");
            case PROGRAMMATO -> aggiungiIscrizione(eventoVisita, idUtente);
            default -> throw new IllegalArgumentException("Stato dell'evento non valido");
        };

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
