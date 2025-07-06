package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione.TransazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.Transazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.TransazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione delle transazioni.
 * Espone endpoint REST per operazioni CRUD sulle transazioni.
 */
@RestController
@RequestMapping("/api/transazioni")
public class TransazioneController {

    /**
     * Service per la logica di business delle transazioni.
     */
    @Autowired
    private TransazioneService transazioneService;

    /**
     * Restituisce la lista di tutte le transazioni.
     * 
     * @return lista di transazioni
     */
    @GetMapping
    public List<Transazione> getAllTransazioni() {
        return transazioneService.getAllTransazioni();
    }

    /**
     * Restituisce una transazione dato il suo id.
     * 
     * @param id identificativo della transazione
     * @return ResponseEntity contenente la transazione o 404 se non trovata
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transazione> getTransazioneById(@PathVariable Long id) {
        return transazioneService.getTransazioneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nuova transazione.
     * 
     * @param transazioneDTO dati della transazione da creare
     * @return la transazione creata
     */
    @PostMapping
    public Transazione createTransazione(@RequestBody TransazioneDTO transazioneDTO) {
        return transazioneService.createTransazione(transazioneDTO);
    }

    /**
     * Aggiorna una transazione esistente.
     * 
     * @param id             identificativo della transazione da aggiornare
     * @param transazioneDTO dati aggiornati della transazione
     * @return ResponseEntity contenente la transazione aggiornata o 404 se non
     *         trovata
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transazione> updateTransazione(@PathVariable Long id,
            @RequestBody TransazioneDTO transazioneDTO) {
        return transazioneService.updateTransazione(id, transazioneDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una transazione dato il suo id.
     * 
     * @param id identificativo della transazione da eliminare
     * @return ResponseEntity con stato 204 se eliminata, 404 se non trovata
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransazione(@PathVariable Long id) {
        if (transazioneService.deleteTransazione(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
