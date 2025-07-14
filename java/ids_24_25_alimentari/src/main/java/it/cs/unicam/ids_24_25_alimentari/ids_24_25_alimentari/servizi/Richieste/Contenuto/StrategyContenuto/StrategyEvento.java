package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.StatusEvento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaContenutoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.EventoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrategyEvento implements RichiestaContenutoStrategy {

    private final EventoRepository eventoRepository;
    private final EventoService eventoService;
    private final UtenteService utenteService;
    private final RichiestaContenutoRepository richiestaContenutoRepository;

    public StrategyEvento(EventoRepository eventoRepository, EventoService eventoService, UtenteService utenteService, RichiestaContenutoRepository richiestaContenutoRepository) {
        this.eventoRepository = eventoRepository;
        this.eventoService = eventoService;
        this.utenteService = utenteService;
        this.richiestaContenutoRepository = richiestaContenutoRepository;
    }

    @Override
    public void processaRichiesta(RichiestaContenuto richiesta, Boolean status) {
        Evento evento = eventoRepository.findByIdNonAutorizzato(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + richiesta.getTargetId()));
        if (evento.getStatus() == Status.PENDING) {
            if (status) {
                evento.setStatus(Status.APPROVATO);
                richiesta.setStatus(Status.APPROVATO);
                evento.setStatusEvento(StatusEvento.PROGRAMMATO);
            } else {
                evento.setStatus(Status.RIFIUTATO);
                richiesta.setStatus(Status.RIFIUTATO);
            }
            eventoRepository.save(evento);
            Optional<Utente> utente = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
            if (utente.isPresent() && utente.get().getRuolo() ==  Ruolo.CURATORE) {
                richiesta.setIdCuratore(utente.get().getId());
            }
            richiestaContenutoRepository.save(richiesta);
        }
        else {
            throw new IllegalArgumentException("evento già valutato con ID: " + richiesta.getTargetId());
        }

    }

    @Override
    public ResponseEntity<?> visualizzaContenutoByRichiesta(RichiestaContenuto richiesta) {
        return ResponseEntity.ok(eventoService.getEventoById(richiesta.getTargetId()));
    }

    @Override
    public Tipologia getTipologia() {
        return Tipologia.EVENTO;
    }

}
