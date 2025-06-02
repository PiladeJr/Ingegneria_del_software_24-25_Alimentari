package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.PacchettoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ProdottoSingoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyProdotto implements RichiestaStrategy {
    @Autowired
    private final ProdottoSingoloRepository prodottoSingoloRepository;
    @Autowired
    private final PacchettoRepository pacchettoRepository;

    public StrategyProdotto(ProdottoSingoloRepository prodottoSingoloRepository, PacchettoRepository pacchettoRepository) {
        this.prodottoSingoloRepository = prodottoSingoloRepository;
        this.pacchettoRepository = pacchettoRepository;
    }
    //TODO gestire le richieste per i prodotti singoli e i pacchetti
    @Override
    public void processaRichiesta(Richiesta richiesta) {
        // Implementazione della logica per la richiesta di un prodotto

        System.out.println("Elaborazione della richiesta per il prodotto: " + richiesta.getId());
    }
    @Override
    public ProdottoSingolo ottieniRichiesta(Richiesta richiesta) {
        return prodottoSingoloRepository.findById(richiesta.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + richiesta.getTargetId()));
    }


    @Override
    public Tipologia getTipologia() {
        return Tipologia.PRODOTTO;
    }
}
