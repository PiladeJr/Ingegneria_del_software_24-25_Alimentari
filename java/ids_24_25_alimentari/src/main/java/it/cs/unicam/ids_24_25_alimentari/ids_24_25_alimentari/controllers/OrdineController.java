package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.StatoOrdine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @GetMapping
    public List<Ordine> getAllOrdini() {
        return ordineService.getAllOrdini();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ordine> getOrdineById(@PathVariable Long id) {
        Optional<Ordine> ordine = ordineService.getOrdineById(id);
        return ordine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ordine creaOrdine(@RequestBody Ordine ordine) {
        return ordineService.salvaOrdine(ordine);
    }

    @PutMapping("/{id}/stato")
    public ResponseEntity<Ordine> aggiornaStatoOrdine(@PathVariable Long id, @RequestBody StatoOrdine nuovoStato) {
        try {
            Ordine ordineAggiornato = ordineService.aggiornaStatoOrdine(id, nuovoStato);
            return ResponseEntity.ok(ordineAggiornato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
