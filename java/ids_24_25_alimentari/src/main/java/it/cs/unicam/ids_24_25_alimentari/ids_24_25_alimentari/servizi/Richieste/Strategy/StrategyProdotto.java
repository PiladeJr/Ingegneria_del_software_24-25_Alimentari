package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Pacchetto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
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


    @Override
    public void processaRichiesta(Richiesta richiesta, Boolean status) {
        Prodotto prodotto = this.visualizzaContenutoByRichiesta(richiesta);

        prodotto.setApprovato(status);
        richiesta.setApprovato(status);

        switch (prodotto.getTipo()) {
            case SINGOLO -> prodottoSingoloRepository.save((ProdottoSingolo) prodotto);
            case PACCHETTO -> pacchettoRepository.save((Pacchetto) prodotto);
        }
        richiestaRepository.save(richiesta);
    }


    @Override
    public Prodotto visualizzaContenutoByRichiesta(Richiesta richiesta) {
        TipoProdotto tipo = TipoProdotto.valueOf(richiesta.getTipoContenuto().toUpperCase());

        Optional<? extends Prodotto> prodottoOp = switch (tipo) {
            case SINGOLO   -> prodottoSingoloRepository.findById(richiesta.getTargetId());
            case PACCHETTO -> pacchettoRepository.findById(richiesta.getTargetId());
        };

        return prodottoOp.orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + richiesta.getTargetId()));
    }


    @Override
    public Tipologia getTipologia() {
        return Tipologia.PRODOTTO;
    }
}
