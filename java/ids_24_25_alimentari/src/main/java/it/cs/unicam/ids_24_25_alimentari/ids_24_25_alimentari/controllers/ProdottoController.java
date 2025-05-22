package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.TipoProdotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/azienda")
public class ProdottoController {

    @Autowired
    private final ProdottoService prodottoService;

    public ProdottoController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    public ResponseEntity<List<Prodotto>> getAllProdotto() {
        return ResponseEntity.ok(prodottoService.getAllProdotti());
    }

    public ResponseEntity<Prodotto> getProdottoById(Long id, TipoProdotto tipo) {
        return switch (tipo) {
            case PACCHETTO -> ResponseEntity.ok(prodottoService.getPacchettoById(id));
            case SINGOLO -> ResponseEntity.ok(prodottoService.getProdottoSingoloById(id));
        };
    }

    public ResponseEntity<List<Prodotto>> getAllProdottiByNomeAsc() {
        return ResponseEntity.ok(prodottoService.getAllProdottiOrdByNome());
    }

    public ResponseEntity<List<Prodotto>> getAllProdottiByPrezzoDec(){
        return ResponseEntity.ok(prodottoService.getAllProdottiOrdByPrezzoDec());
    }

    public ResponseEntity<List<Prodotto>> getAllProdottiByPrezzoCre(){
        return ResponseEntity.ok(prodottoService.getAllProdottiOrdByPrezzoCre());
    }

    public List<Optional<Prodotto>> getProdottoByNome(String nome) {
        return prodottoService.getProdottoByNome(nome);
    }

    public List<ProdottoSingolo> getProdottiSingoliByIdAzienda(Long idAzienda) {
        return prodottoService.getProdottiByIdAzienda(idAzienda);
    }
}
