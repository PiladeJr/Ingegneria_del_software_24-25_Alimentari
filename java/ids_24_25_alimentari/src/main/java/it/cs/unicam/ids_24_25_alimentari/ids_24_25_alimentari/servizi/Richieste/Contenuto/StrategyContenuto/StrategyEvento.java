package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.StatusEvento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaContenutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyEvento implements RichiestaContenutoStrategy {

    @Autowired
    private final EventoRepository eventoRepository;
    private final RichiestaContenutoRepository richiestaContenutoRepository;

    public StrategyEvento(EventoRepository eventoRepository, RichiestaContenutoRepository richiestaContenutoRepository) {
        this.eventoRepository = eventoRepository;
        this.richiestaContenutoRepository = richiestaContenutoRepository;
    }

    @Override
    public void processaRichiesta(RichiestaContenuto richiesta, Boolean status) {
        Evento evento = eventoRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + richiesta.getTargetId()));
        evento.setApprovato(status);
        richiesta.setApprovato(status);
        if(status) {
            evento.setStatus(StatusEvento.PROGRAMMATO);
        }
        eventoRepository.save(evento);
        richiestaContenutoRepository.save(richiesta);
    }

    @Override
    public Evento visualizzaContenutoByRichiesta(RichiestaContenuto richiesta) {
        return eventoRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + richiesta.getTargetId()));
    }

    @Override
    public Tipologia getTipologia() {
        return Tipologia.EVENTO;
    }

}
