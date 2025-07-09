package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.InformazioniAggiuntiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyInformazioniAggiuntive implements RichiestaContenutoStrategy {
    @Autowired
    private final InformazioniAggiuntiveRepository informazioniRepository;

    public StrategyInformazioniAggiuntive(InformazioniAggiuntiveRepository informazioniRepository) {
        this.informazioniRepository = informazioniRepository;
    }

    @Override
    public void processaRichiesta(RichiestaContenuto richiesta, Boolean status) {
        var info = informazioniRepository.findById(richiesta.getTargetId());
        info.get().setApprovato(status);
        richiesta.setApprovato(status);
        informazioniRepository.save(info.get());
        richiesta.setApprovato(status);
    }


    @Override
        public InfoAzienda visualizzaContenutoByRichiesta(RichiestaContenuto richiesta) {
        return informazioniRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("info non trovate con ID: " + richiesta.getTargetId()));
    }


    @Override
    public Tipologia getTipologia() {
        return Tipologia.INFO_AZIENDA;
    }
}
