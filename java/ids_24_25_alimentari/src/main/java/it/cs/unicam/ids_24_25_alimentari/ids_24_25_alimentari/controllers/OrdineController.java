package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineResponseDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.StatoOrdine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> creaOrdine(@RequestBody OrdineDTO ordine) {
        try {
            OrdineResponseDTO response = ordineService.salvaOrdine(ordine);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Errore nella creazione dell'ordine: " + e.getMessage()));
        }
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

    /**
     * Endpoint per completare il pagamento PayPal di un ordine
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
     * transazione
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
     * Endpoint per testare la creazione di un ordine
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
