package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.InformazioniAggiuntiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyInformazioniAggiuntive implements RichiestaStrategy {
    @Autowired
    private final InformazioniAggiuntiveRepository informazioniRepository;

    public StrategyInformazioniAggiuntive(InformazioniAggiuntiveRepository informazioniRepository) {
        this.informazioniRepository = informazioniRepository;
    }

    @Override
    public void processaRichiesta(Richiesta richiesta) {
        var info = informazioniRepository.findById(richiesta.getTargetId());
        System.out.println("Elaborazione della richiesta per informazioni aggiuntive: " + richiesta.getId());
    }
    @Override
        public InformazioniAggiuntive ottieniRichiesta(Richiesta richiesta) {
        return informazioniRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("info non trovate con ID: " + richiesta.getTargetId()));
    }
}
