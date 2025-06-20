package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione.TransazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.Transazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.TransazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transazioni")
public class TransazioneController {

    @Autowired
    private TransazioneService transazioneService;

    @GetMapping
    public List<Transazione> getAllTransazioni() {
        return transazioneService.getAllTransazioni();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transazione> getTransazioneById(@PathVariable Long id) {
        return transazioneService.getTransazioneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Transazione createTransazione(@RequestBody TransazioneDTO transazioneDTO) {
        return transazioneService.createTransazione(transazioneDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transazione> updateTransazione(@PathVariable Long id,
            @RequestBody TransazioneDTO transazioneDTO) {
        return transazioneService.updateTransazione(id, transazioneDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransazione(@PathVariable Long id) {
        if (transazioneService.deleteTransazione(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
