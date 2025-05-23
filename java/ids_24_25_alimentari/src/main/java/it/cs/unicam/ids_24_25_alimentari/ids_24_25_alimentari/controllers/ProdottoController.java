package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.TipoProdotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prodotto")
public class ProdottoController {

    @Autowired
    private final ProdottoService prodottoService;

    public ProdottoController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    @DeleteMapping("/{id}/{tipo}")
    public ResponseEntity<?> deleteProdotto(@PathVariable Long id, @PathVariable TipoProdotto tipo) {
        try {
            Prodotto prodotto = switch (tipo) {
                case PACCHETTO -> prodottoService.getProdottoSingoloById(id);
                case SINGOLO -> prodottoService.getPacchettoById(id);
            };
            if (prodotto == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Prodotto non trovato"));
            }else {
                this.deleteProdottoByTipo(id, tipo);
                return ResponseEntity.ok(Collections.singletonMap("message", "Prodotto eliminato con successo"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "ID non valido: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

    private void deleteProdottoByTipo(Long id, TipoProdotto tipo) {
        switch (tipo) {
            case PACCHETTO -> prodottoService.deletePacchetto(id);
            case SINGOLO -> prodottoService.deleteProdottoSingolo(id);
        }
    }

    @GetMapping("/{id}/{tipo}")
    public ResponseEntity<Prodotto> getProdottoById(@PathVariable Long id, @PathVariable TipoProdotto tipo) {
        return switch (tipo) {
            case PACCHETTO -> ResponseEntity.ok(prodottoService.getPacchettoById(id));
            case SINGOLO -> ResponseEntity.ok(prodottoService.getProdottoSingoloById(id));
        };
    }

    @GetMapping("/id-azienda/{idAzienda}")
    public List<ProdottoSingolo> getProdottiSingoliByIdAzienda(@PathVariable Long idAzienda) {
        return prodottoService.getProdottiByIdAzienda(idAzienda);
    }

    @GetMapping("/{nome}")
    public List<Optional<Prodotto>> getProdottoByNome(@PathVariable String nome) {
        return prodottoService.getProdottoByNome(nome);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Prodotto>> getAllProdotto() {
        return ResponseEntity.ok(prodottoService.getAllProdotti());
    }

    @GetMapping("/all/nome/asc")
    public ResponseEntity<List<Prodotto>> getAllProdottiByNomeAsc() {
        return ResponseEntity.ok(prodottoService.getAllProdottiOrdByNome());
    }

    @GetMapping("/all/prezzo/cre")
    public ResponseEntity<List<Prodotto>> getAllProdottiByPrezzoCre(){
        return ResponseEntity.ok(prodottoService.getAllProdottiOrdByPrezzoCre());
    }

    @GetMapping("/all/prezzo/desc")
    public ResponseEntity<List<Prodotto>> getAllProdottiByPrezzoDec(){
        return ResponseEntity.ok(prodottoService.getAllProdottiOrdByPrezzoDec());
    }
}
