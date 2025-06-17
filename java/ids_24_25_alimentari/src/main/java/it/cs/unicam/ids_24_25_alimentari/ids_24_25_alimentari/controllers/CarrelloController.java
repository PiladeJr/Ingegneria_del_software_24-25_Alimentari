package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.Carrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.ContenutoCarrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrelli")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @GetMapping
    public List<Carrello> getAllCarrelli() {
        return carrelloService.getAllCarrelli();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrello> getCarrelloById(@PathVariable Long id) {
        Optional<Carrello> carrello = carrelloService.getCarrelloById(id);
        return carrello.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Carrello creaCarrello(@RequestBody Carrello carrello) {
        return carrelloService.salvaCarrello(carrello);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaCarrello(@PathVariable Long id) {
        carrelloService.eliminaCarrello(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/contenuto")
    public ResponseEntity<Carrello> aggiungiContenuto(@PathVariable Long id,
            @RequestBody ContenutoCarrello contenutoCarrello) {
        try {
            Carrello carrelloAggiornato = carrelloService.aggiungiContenutoAlCarrello(id, contenutoCarrello);
            return ResponseEntity.ok(carrelloAggiornato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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
