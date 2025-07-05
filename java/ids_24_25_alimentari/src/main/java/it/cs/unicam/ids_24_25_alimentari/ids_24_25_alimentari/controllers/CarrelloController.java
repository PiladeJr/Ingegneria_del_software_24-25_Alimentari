package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.carrello.CreaCarrelloDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.contenutoCarrello.ContenutoCarrelloDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.Carrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller per la gestione dei carrelli.
 * Espone endpoint REST per operazioni CRUD sui carrelli e la gestione dei
 * contenuti del carrello.
 */
@RestController
@RequestMapping("/api/carrelli")
public class CarrelloController {

    /**
     * Service per la logica di business dei carrelli.
     */
    @Autowired
    private CarrelloService carrelloService;

    /**
     * Restituisce la lista di tutti i carrelli.
     * 
     * @return lista di carrelli
     */
    @GetMapping
    public List<Carrello> getAllCarrelli() {
        return carrelloService.getAllCarrelli();
    }

    /**
     * Restituisce un carrello dato il suo id.
     * 
     * @param id identificativo del carrello
     * @return ResponseEntity contenente il carrello o 404 se non trovato
     */
    @GetMapping("/{id}")
    public ResponseEntity<Carrello> getCarrelloById(@PathVariable Long id) {
        Optional<Carrello> carrello = carrelloService.getCarrelloById(id);
        return carrello.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuovo carrello.
     * 
     * @param carrello dati per la creazione del carrello
     * @return il carrello creato
     */
    @PostMapping
    public Carrello creaCarrello(@RequestBody CreaCarrelloDTO carrello) {
        return carrelloService.creaCarrello(carrello);
    }

    /**
     * Elimina un carrello dato il suo id.
     * 
     * @param id identificativo del carrello da eliminare
     * @return ResponseEntity con stato 204 se eliminato
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaCarrello(@PathVariable Long id) {
        carrelloService.eliminaCarrello(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Aggiunge un contenuto a un carrello esistente.
     * 
     * @param id                identificativo del carrello
     * @param contenutoCarrello dati del contenuto da aggiungere
     * @return ResponseEntity con il carrello aggiornato o 404 se non trovato
     */
    @PostMapping("/{id}/contenuto")
    public ResponseEntity<Carrello> aggiungiContenuto(@PathVariable Long id,
            @RequestBody ContenutoCarrelloDTO contenutoCarrello) {
        try {
            Carrello carrelloAggiornato = carrelloService.aggiungiContenutoAlCarrello(id, contenutoCarrello);
            return ResponseEntity.ok(carrelloAggiornato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Rimuove un contenuto da un carrello.
     * 
     * @param carrelloId  identificativo del carrello
     * @param contenutoId identificativo del contenuto da rimuovere
     * @return ResponseEntity con il carrello aggiornato o 404 se non trovato
     */
    @DeleteMapping("/{carrelloId}/contenuto/{contenutoId}")
    public ResponseEntity<Carrello> rimuoviContenuto(@PathVariable Long carrelloId, @PathVariable Long contenutoId) {
        try {
            Carrello carrelloAggiornato = carrelloService.rimuoviContenutoDalCarrello(carrelloId, contenutoId);
            return ResponseEntity.ok(carrelloAggiornato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
