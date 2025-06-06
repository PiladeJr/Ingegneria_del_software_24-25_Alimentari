package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyEvento implements RichiestaStrategy {

    @Autowired
    private final EventoRepository eventoRepository;

    public StrategyEvento(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public void processaRichiesta(Richiesta richiesta) {
        var eventi = eventoRepository.findById(richiesta.getTargetId());

    }
    @Override
    public Evento ottieniRichiesta(Richiesta richiesta) {
        return eventoRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + richiesta.getTargetId()));
    }



    @Override
    public Tipologia getTipologia() {
        return Tipologia.EVENTO;
    }

}
