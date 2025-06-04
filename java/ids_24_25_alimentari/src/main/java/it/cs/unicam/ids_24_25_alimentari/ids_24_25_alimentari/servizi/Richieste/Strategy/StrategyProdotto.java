package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.TipoProdotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.PacchettoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ProdottoSingoloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrategyProdotto implements RichiestaStrategy {
    @Autowired
    private final ProdottoSingoloRepository prodottoSingoloRepository;
    @Autowired
    private final PacchettoRepository pacchettoRepository;
    @Autowired
    private final RichiestaRepository richiestaRepository;

    public StrategyProdotto(ProdottoSingoloRepository prodottoSingoloRepository, PacchettoRepository pacchettoRepository, RichiestaRepository richiestaRepository) {
        this.prodottoSingoloRepository = prodottoSingoloRepository;
        this.pacchettoRepository = pacchettoRepository;
        this.richiestaRepository = richiestaRepository;
    }


    //TODO gestire le richieste per i prodotti singoli e i pacchetti
    @Override
    public void processaRichiesta(Richiesta richiesta) {
        TipoProdotto tipo = TipoProdotto.valueOf(richiesta.getTipoContenuto().toUpperCase());

        Optional<? extends Prodotto> prodotto = switch (tipo) {
            case SINGOLO   -> prodottoSingoloRepository.findById(richiesta.getId());
            case PACCHETTO -> pacchettoRepository.findById(richiesta.getId());
        };

        richiesta.setApprovato(true);
        richiestaRepository.save(richiesta);
        System.out.println("Elaborazione della richiesta per il prodotto: " + richiesta.getId());

    }


    @Override
    public Prodotto ottieniRichiesta(Richiesta richiesta) {
        return prodottoSingoloRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + richiesta.getTargetId()));
    }


    @Override
    public Tipologia getTipologia() {
        return Tipologia.PRODOTTO;
    }
}
