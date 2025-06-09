package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.StatusEvento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyEvento implements RichiestaStrategy {

    @Autowired
    private final EventoRepository eventoRepository;
    private final RichiestaRepository richiestaRepository;

    public StrategyEvento(EventoRepository eventoRepository, RichiestaRepository richiestaRepository) {
        this.eventoRepository = eventoRepository;
        this.richiestaRepository = richiestaRepository;
    }

    @Override
    public void processaRichiesta(Richiesta richiesta, Boolean status) {
        Evento evento = eventoRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + richiesta.getTargetId()));
        evento.setApprovato(status);
        richiesta.setApprovato(status);
        if(status) {
            evento.setStatus(StatusEvento.PROGRAMMATO);
        }
        eventoRepository.save(evento);
        richiestaRepository.save(richiesta);
    }

    @Override
    public Evento visualizzaContenutoByRichiesta(Richiesta richiesta) {
        return eventoRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + richiesta.getTargetId()));
    }

    @Override
    public Tipologia getTipologia() {
        return Tipologia.EVENTO;
    }

}
