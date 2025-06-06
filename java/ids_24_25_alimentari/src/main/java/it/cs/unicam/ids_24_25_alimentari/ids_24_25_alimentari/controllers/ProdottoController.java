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

    /**
     * Restituisce un prodotto specifico in base al tipo e all'ID.
     *
     * @param tipo Il tipo di prodotto.
     * @param id   L'ID del prodotto.
     * @return Il prodotto richiesto.
     * @throws IllegalArgumentException Se il tipo di prodotto non è valido.
     */
    @GetMapping()
    public ResponseEntity<?> getProdottoByID(
            @RequestParam String tipo,
            @RequestParam Long id
    ) {
        try {
            TipoProdotto tipoProdotto = TipoProdotto.valueOf(tipo.toUpperCase());
            return prodottoService.getProdottoById(id, tipoProdotto)
                    .map(ResponseEntity::<Object>ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap("error", "Prodotto non trovato")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }


    /**
     * Restituisce tutti i prodotti singoli associati ad una specifica azienda.
     *
     * @param idAzienda L'ID dell'azienda.
     * @return Lista di prodotti singoli dell'azienda.
     */
    @GetMapping("/id-azienda/{idAzienda}")
    public List<ProdottoSingolo> getProdottiSingoliByIdAzienda(@PathVariable Long idAzienda) {
        return prodottoService.getProdottiByIdAzienda(idAzienda);
    }


    /**
     * Restituisce tutti i prodotti con un nome specifico.
     *
     * @param nome Il nome del prodotto.
     * @return Lista di prodotti che corrispondono al nome.
     */
    @GetMapping("/nome/{nome}")
    public List<Optional<Prodotto>> getProdottoByNome(@PathVariable String nome) {
        return prodottoService.getProdottoByNome(nome);
    }


    /**
     * Restituisce tutti i prodotti, sia singoli che pacchetti, ordinati in base al parametro specificato.
     *
     * @param sortBy Il campo per ordinare (default: "nome").
     * @param order  L'ordine di ordinamento (default: "asc").
     * @return Lista di prodotti ordinati.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Prodotto>> getAllProdotti(
            @RequestParam(required = false, defaultValue = "nome") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(prodottoService.getAllProdotti(sortBy, order));
    }


    /**
     * Elimina un prodotto singolo dato il suo ID.
     *
     * @param id L'ID del prodotto da eliminare.
     * @return Esito dell'operazione di eliminazione.
     */
    @GetMapping("/delete/singolo/{id}")
    public ResponseEntity<?> deleteProdottoSingoloById(@PathVariable Long id) {
        return this.deleteProdottoById(id, TipoProdotto.SINGOLO);
    }


    /**
     * Elimina un pacchetto dato il suo ID.
     *
     * @param id L'ID del pacchetto da eliminare.
     * @return Esito dell'operazione di eliminazione.
     */
    @GetMapping("/delete/pacchetto/{id}")
    public ResponseEntity<?> deletePacchettoById(@PathVariable Long id) {
        return this.deleteProdottoById(id, TipoProdotto.PACCHETTO);
    }

    private ResponseEntity<?> deleteProdottoById(Long id, TipoProdotto tipo) {
        try {
            Optional<? extends Prodotto> prodotto = prodottoService.getProdottoById(id, tipo);
            if (prodotto.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Prodotto non trovato"));
            }else {
                this.prodottoService.deleteProdotto(id, tipo);
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
}