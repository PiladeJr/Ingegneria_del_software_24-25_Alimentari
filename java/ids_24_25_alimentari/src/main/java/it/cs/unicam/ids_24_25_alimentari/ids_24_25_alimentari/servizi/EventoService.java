package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi.EventoPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoFieraRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoVisitaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EventoService {
//    private final EventoFieraRepository fieraRepository;
//    private final EventoVisitaRepository visitaRepository;
    private final EventoRepository eventoRepository;
    private final UtenteService utenteService;

    public EventoService(EventoRepository eventoRepository, UtenteService utenteService) {
        this.eventoRepository = eventoRepository;
        this.utenteService = utenteService;
    }


//    public EventoService(EventoFieraRepository fieraRepository, EventoVisitaRepository visitaRepository, UtenteService utenteService) {
//        this.fieraRepository = fieraRepository;
//        this.visitaRepository = visitaRepository;
//        this.utenteService = utenteService;
//    }
//    /**
//     * <h2>Recupera tutti gli eventi di tipo fiera</h2>
//     * <br>
//     * Questo metodo restituisce la lista di eventi che rappresentano fiere territoriali.
//     *
//     * @return {@code List<EventoFiera>} contenente tutti gli eventi di tipo fiera presenti nel database.
//     */
//    public List<EventoFiera> getEventiFiera() {
//        return fieraRepository.findAll();
//    }
//    /**
//     * <h2>Recupera tutti gli eventi di tipo visita</h2>
//     * <br>
//     * Questo metodo restituisce la lista di eventi che rappresentano visite aziendali.
//     *
//     * @return {@code List<EventoVisita>} contenente tutti gli eventi di tipo visita presenti nel database.
//     */
//    public List<EventoVisita> getEventiVisita() {
//        return visitaRepository.findAll();
//    }
//    /**
//     * <h2>Recupera tutti gli eventi ordinati alfabeticamente</h2>
//     * <br>
//     * Questo metodo restituisce una lista completa di eventi presenti sulla piattaforma,
//     * includendo sia le fiere che le visite, ordinati in ordine alfabetico in base al titolo.
//     *
//     * @return {@code List<Evento>} contenente tutti gli eventi (fiere e visite),
//     *         ordinati alfabeticamente per titolo.
//     */
//    public List<Evento> getAllEventiOrdinatiPerTitolo() {
//        List<Evento> fiere = new ArrayList<>(fieraRepository.findAll());
//        List<Evento> visite = new ArrayList<>(visitaRepository.findAll());
//
//        List<Evento> tutti = new ArrayList<>();
//        tutti.addAll(fiere);
//        tutti.addAll(visite);
//
//        return tutti.stream()
//                .sorted(Comparator.comparing(Evento::getTitolo, String.CASE_INSENSITIVE_ORDER))
//                .collect(Collectors.toList());
//    }
//    /**
//     * <h2>Recupera tutti gli eventi creati dall'utente attualmente autenticato</h2>
//     * <br>
//     * Questo metodo utilizza il contesto di sicurezza per identificare l'utente attualmente autenticato
//     * e restituisce la lista di eventi (fiere e visite) da lui creati.
//     *
//     * @return {@code List<Evento>} contenente tutti gli eventi creati dall'utente loggato.
//     * @throws RuntimeException se l'utente non è autenticato o non è presente nel database.
//     */
//    public List<Evento> getEventiCreatiDallUtenteAutenticato() {
//        Long idUtente = utenteService.getIdUtenteAutenticato(); // <-- nuova logica
//        if (idUtente == null) {
//            throw new RuntimeException("Impossibile ottenere l'ID dell'utente autenticato");
//        }
//        List<EventoFiera> fiere = fieraRepository.findByCreatoreId(idUtente);
//        List<EventoVisita> visite = visitaRepository.findByCreatoreId(idUtente);
//
//        List<Evento> tutti = new ArrayList<>();
//        tutti.addAll(fiere);
//        tutti.addAll(visite);
//        return tutti;
//    }
//    /**
//     * <h2>Recupera tutte le fiere ordinate per data di inizio decrescente</h2>
//     * <br>
//     * Questo metodo restituisce l'elenco delle fiere sulla piattaforma, ordinate dalla più recente alla meno recente.
//     *
//     * @return {@code List<EventoFiera>} contenente tutte le fiere ordinate per data di inizio decrescente.
//     */
//    public List<EventoFiera> getEventiFieraOrdinatiPerDataInizio() {
//        return fieraRepository.findAllOrderByInizioDesc();
//    }
//
//    /**
//     * <h2>Recupera tutte le visite ordinate per data di inizio decrescente</h2>
//     * <br>
//     * Questo metodo restituisce l'elenco delle visite aziendali, ordinate dalla più recente alla meno recente.
//     *
//     * @return {@code List<EventoVisita>} contenente tutte le visite ordinate per data di inizio decrescente.
//     */
//    public List<EventoVisita> getEventiVisitaOrdinatiPerDataInizio() {
//        return visitaRepository.findAllOrderByInizioDesc();
//    }
//    /**
//     * <h2>Recupera tutti gli eventi (fiere e visite) ordinati per data di inizio decrescente</h2>
//     * <br>
//     * Unisce fiere e visite in un'unica lista e le ordina per data di inizio, dalla più recente alla meno recente.
//     *
//     * @return {@code List<Evento>} contenente fiere e visite ordinate per data di inizio decrescente.
//     */
//    public List<Evento> getTuttiEventiOrdinatiPerDataInizio() {
//        List<Evento> eventi = new ArrayList<>();
//        eventi.addAll(fieraRepository.findAll());
//        eventi.addAll(visitaRepository.findAll());
//
//        return eventi.stream()
//                .sorted(Comparator.comparing(Evento::getInizio).reversed())
//                .collect(Collectors.toList());
//    }
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

}
