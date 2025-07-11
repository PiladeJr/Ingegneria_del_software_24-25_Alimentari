package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineResponseDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.StatoOrdine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.OrdineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller per la gestione degli ordini.
 * Espone endpoint REST per operazioni sugli ordini, inclusa la gestione dello
 * stato e dei pagamenti.
 */
@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    /**
     * Service per la logica di business degli ordini.
     */

    private OrdineService ordineService;

    /**
     * Restituisce la lista di tutti gli ordini.
     * 
     * @return lista di ordini
     */
    @GetMapping
    public List<Ordine> getAllOrdini() {
        return ordineService.getAllOrdini();
    }

    /**
     * Restituisce un ordine dato il suo id.
     * 
     * @param id identificativo dell'ordine
     * @return ResponseEntity contenente l'ordine o 404 se non trovato
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ordine> getOrdineById(@PathVariable Long id) {
        Optional<Ordine> ordine = ordineService.getOrdineById(id);
        return ordine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuovo ordine.
     * 
     * @param ordine dati dell'ordine da creare
     * @return ResponseEntity con i dati dell'ordine creato o errore
     */
    @PostMapping
    public ResponseEntity<?> creaOrdine(@RequestBody OrdineDTO ordine) {
        try {
            OrdineResponseDTO response = ordineService.salvaOrdine(ordine);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Errore nella creazione dell'ordine: " + e.getMessage()));
        }
    }

    /**
     * Aggiorna lo stato di un ordine esistente.
     * 
     * @param id         identificativo dell'ordine da aggiornare
     * @param nuovoStato nuovo stato da assegnare all'ordine
     * @return ResponseEntity con l'ordine aggiornato o 404 se non trovato
     */
    @PutMapping("/{id}/stato")
    public ResponseEntity<Ordine> aggiornaStatoOrdine(@PathVariable Long id, @RequestBody StatoOrdine nuovoStato) {
        try {
            Ordine ordineAggiornato = ordineService.aggiornaStatoOrdine(id, nuovoStato);
            return ResponseEntity.ok(ordineAggiornato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint per completare il pagamento PayPal di un ordine.
     * 
     * @param id        identificativo dell'ordine
     * @param paymentId id del pagamento PayPal
     * @param payerId   id del pagatore PayPal
     * @return ResponseEntity con lo stato del pagamento
     */
    @PostMapping("/{id}/completa-paypal")
    public ResponseEntity<?> completaPaymentPayPal(
            @PathVariable Long id,
            @RequestParam String paymentId,
            @RequestParam String payerId) {
        try {
            // Qui dovremmo implementare la logica per completare il pagamento PayPal
            // utilizzando il servizio PayPal per verificare e completare il pagamento
            return ResponseEntity.ok(Map.of(
                    "message", "Pagamento PayPal in fase di completamento",
                    "ordineId", id,
                    "paymentId", paymentId,
                    "payerId", payerId,
                    "status", "processing"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Errore nel completamento del pagamento: " + e.getMessage()));
        }
    }

    /**
     * Ottiene lo stato dettagliato di un ordine incluse le informazioni di
     * transazione.
     * 
     * @param id identificativo dell'ordine
     * @return ResponseEntity con dettagli ordine e transazione o errore
     */
    @GetMapping("/{id}/stato")
    public ResponseEntity<?> getStatoDettagliatoOrdine(@PathVariable Long id) {
        try {
            var ordine = ordineService.getOrdineById(id);
            if (ordine.isPresent()) {
                var ord = ordine.get();
                var response = Map.of(
                        "ordineId", ord.getId(),
                        "stato", ord.getStato().toString(),
                        "totale", ord.getTotale(),
                        "dataOrdine", ord.getDataOrdine().toString(),
                        "statoTransazione",
                        ord.getTransazione() != null ? ord.getTransazione().getStatoTransazione().toString() : "N/A",
                        "metodoPagamento",
                        ord.getTransazione() != null ? ord.getTransazione().getMetodoPagamento().toString() : "N/A",
                        "paypalPaymentId",
                        ord.getTransazione() != null ? ord.getTransazione().getPaypalPaymentId() : null);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Errore nel recupero dell'ordine: " + e.getMessage()));
        }
    }

    /**
     * Endpoint per testare la creazione di un ordine.
     * 
     * @return ResponseEntity con messaggio di test e stato
     */
    @PostMapping("/test")
    public ResponseEntity<?> testCreazioneOrdine() {
        try {
            return ResponseEntity.ok(Map.of(
                    "message", "Endpoint di test per la creazione ordini - PayPal integrato",
                    "timestamp", java.time.LocalDateTime.now().toString(),
                    "status", "active",
                    "paypal", "enabled"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Errore nel test: " + e.getMessage()));
        }
    }

}
