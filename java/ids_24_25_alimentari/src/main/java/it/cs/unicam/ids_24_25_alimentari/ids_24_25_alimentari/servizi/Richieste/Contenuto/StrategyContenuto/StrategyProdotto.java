package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Pacchetto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.TipoProdotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.PacchettoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ProdottoSingoloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaContenutoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrategyProdotto implements RichiestaContenutoStrategy {
    private final ProdottoSingoloRepository prodottoSingoloRepository;
    private final PacchettoRepository pacchettoRepository;
    private final RichiestaContenutoRepository richiestaContenutoRepository;
    private final UtenteService utenteService;

    public StrategyProdotto(ProdottoSingoloRepository prodottoSingoloRepository, PacchettoRepository pacchettoRepository, RichiestaContenutoRepository richiestaContenutoRepository, UtenteService utenteService) {
        this.prodottoSingoloRepository = prodottoSingoloRepository;
        this.pacchettoRepository = pacchettoRepository;
        this.richiestaContenutoRepository = richiestaContenutoRepository;
        this.utenteService = utenteService;
    }


    @Override
    public void processaRichiesta(RichiestaContenuto richiesta, Boolean status) {
        Prodotto prodotto = getProdottoByRichiesta(richiesta);
        if (prodotto != null){
            if (prodotto.getStatus() != Status.PENDING){
                if (status) {
                    prodotto.setStatus(Status.APPROVATO);
                    richiesta.setStatus(Status.APPROVATO);
                } else {
                    prodotto.setStatus(Status.RIFIUTATO);
                    richiesta.setStatus(Status.RIFIUTATO);
                }
                switch (prodotto.getTipo()) {
                    case SINGOLO -> prodottoSingoloRepository.save((ProdottoSingolo) prodotto);
                    case PACCHETTO -> pacchettoRepository.save((Pacchetto) prodotto);
                }
                Optional<Utente> utente = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
                if (utente.isPresent() && utente.get().getRuolo() ==  Ruolo.CURATORE) {
                    richiesta.setIdCuratore(utente.get().getId());
                }
                richiestaContenutoRepository.save(richiesta);
            }
            else {
                throw new IllegalArgumentException("Prodotto già valutato con ID: " + richiesta.getTargetId());
            }
        }
        else {
            throw new IllegalArgumentException("Prodotto non trovato con ID: " + richiesta.getTargetId());
        }

    }


    @Override
    public ResponseEntity<?> visualizzaContenutoByRichiesta(RichiestaContenuto richiesta) {
        Prodotto prodotto = getProdottoByRichiesta(richiesta);
        if (prodotto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prodotto);
    }

    private Prodotto getProdottoByRichiesta(RichiestaContenuto richiesta) {
        TipoProdotto tipo = TipoProdotto.valueOf(richiesta.getTipoContenuto().toUpperCase());
        return switch (tipo) {
            case SINGOLO   -> prodottoSingoloRepository.findById(richiesta.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("Prodotto singolo non trovato con ID: " + richiesta.getTargetId()));
            case PACCHETTO -> pacchettoRepository.findById(richiesta.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("Pacchetto non trovato con ID: " + richiesta.getTargetId()));
        };
    }


    @Override
    public Tipologia getTipologia() {
        return Tipologia.PRODOTTO;
    }
}
