package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineResponseDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione.TransazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.OrdineService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller per la gestione dei pagamenti PayPal.
 * Espone endpoint REST per la creazione, il completamento, l'annullamento e il
 * test dei pagamenti PayPal.
 */
@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    /**
     * Service per la logica di business PayPal.
     */
    @Autowired
    private PayPalService payPalService;

    /**
     * Service per la logica di business degli ordini.
     */
    @Autowired
    private OrdineService ordineService;

    /**
     * Inizia il processo di pagamento PayPal.
     * 
     * @param transazioneDTO dati della transazione
     * @return ResponseEntity con i dati del pagamento creato o errore
     */
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody TransazioneDTO transazioneDTO) {
        try {
            String successUrl = "http://localhost:8080/api/paypal/success";
            String cancelUrl = "http://localhost:8080/api/paypal/cancel";

            PayPalService.PaymentResult result = payPalService.processPayment(
                    transazioneDTO, successUrl, cancelUrl);

            if (result.isSuccess()) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "paymentId", result.getPaymentId(),
                        "approvalUrl", result.getApprovalUrl()));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", result.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Errore durante la creazione del pagamento: " + e.getMessage()));
        }
    }

    /**
     * Gestisce il completamento del pagamento PayPal tramite redirect da PayPal
     * (GET).
     * 
     * @param token    token PayPal
     * @param payerId  id del pagatore
     * @param ordineId id dell'ordine (opzionale)
     * @return ResponseEntity con i dati dell'ordine aggiornato o errore
     */
    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccessRedirect(@RequestParam("token") String token,
            @RequestParam("PayerID") String payerId,
            @RequestParam(value = "ordineId", required = false) String ordineId) {
        try {
            OrdineResponseDTO result;

            // Se abbiamo l'ordineId, lo usiamo per completare il pagamento
            if (ordineId != null && !ordineId.trim().isEmpty()) {
                try {
                    Long ordineIdLong = Long.parseLong(ordineId);
                    result = ordineService.completaPaymentPayPal(ordineIdLong, payerId);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("ordineId non valido: " + ordineId);
                }
            } else {
                // Fallback: se non abbiamo ordineId, proviamo con il token (probabile
                // fallimento)
                throw new RuntimeException("ordineId è richiesto per completare il pagamento PayPal");
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "ordineId", result.getOrdineId(),
                    "stato", result.getStato(),
                    "message", result.getMessage(),
                    "totale", result.getTotale(),
                    "metodoPagamento", result.getMetodoPagamento(),
                    "requiresPayment", result.isRequiresPayment()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Errore durante il completamento del pagamento: " + e.getMessage()));
        }
    }

    /**
     * Gestisce il completamento del pagamento PayPal tramite API (POST).
     * 
     * @param request mappa contenente paymentId e payerId
     * @return ResponseEntity con i dati dell'ordine aggiornato o errore
     */
    @PostMapping("/complete-payment")
    public ResponseEntity<?> completePayment(@RequestBody Map<String, String> request) {
        try {
            String paymentId = request.get("paymentId");
            String payerId = request.get("payerId");

            if (paymentId == null || payerId == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "paymentId e payerId sono richiesti"));
            }

            OrdineResponseDTO result = ordineService.completaPaymentPayPalByPaymentId(paymentId, payerId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "ordineId", result.getOrdineId(),
                    "stato", result.getStato(),
                    "message", result.getMessage(),
                    "totale", result.getTotale(),
                    "metodoPagamento", result.getMetodoPagamento(),
                    "requiresPayment", result.isRequiresPayment()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Errore durante il completamento del pagamento: " + e.getMessage()));
        }
    }

    /**
     * Gestisce l'annullamento del pagamento PayPal tramite redirect da PayPal
     * (GET).
     * 
     * @param token    token PayPal
     * @param ordineId id dell'ordine (opzionale)
     * @return ResponseEntity con i dati dell'ordine annullato o errore
     */
    @GetMapping("/cancel")
    public ResponseEntity<?> paymentCancelRedirect(@RequestParam("token") String token,
            @RequestParam(value = "ordineId", required = false) String ordineId) {
        try {
            OrdineResponseDTO result;

            // Se abbiamo l'ordineId, lo usiamo direttamente
            if (ordineId != null && !ordineId.trim().isEmpty()) {
                try {
                    Long ordineIdLong = Long.parseLong(ordineId);
                    result = ordineService.cancellaOrdineById(ordineIdLong);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("ordineId non valido: " + ordineId);
                }
            } else {
                // Fallback: prova con il token (anche se potrebbe non funzionare)
                result = ordineService.cancellaPaymentPayPalByToken(token);
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "ordineId", result.getOrdineId(),
                    "stato", result.getStato(),
                    "message", result.getMessage(),
                    "totale", result.getTotale(),
                    "metodoPagamento", result.getMetodoPagamento(),
                    "requiresPayment", result.isRequiresPayment()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Errore durante l'annullamento del pagamento: " + e.getMessage()));
        }
    }

    /**
     * Gestisce l'annullamento del pagamento PayPal tramite API (POST).
     * 
     * @param request mappa contenente paymentId
     * @return ResponseEntity con i dati dell'ordine annullato o errore
     */
    @PostMapping("/cancel-payment")
    public ResponseEntity<?> cancelPayment(@RequestBody Map<String, String> request) {
        try {
            String paymentId = request.get("paymentId");

            if (paymentId == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "paymentId è richiesto"));
            }

            OrdineResponseDTO result = ordineService.cancellaPaymentPayPalByPaymentId(paymentId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "ordineId", result.getOrdineId(),
                    "stato", result.getStato(),
                    "message", result.getMessage(),
                    "totale", result.getTotale(),
                    "metodoPagamento", result.getMetodoPagamento(),
                    "requiresPayment", result.isRequiresPayment()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Errore durante l'annullamento del pagamento: " + e.getMessage()));
        }
    }

    /**
     * Ottiene i dettagli di un pagamento PayPal.
     * 
     * @param paymentId id del pagamento PayPal
     * @return ResponseEntity con i dettagli del pagamento o errore
     */
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable String paymentId) {
        try {
            var payment = payPalService.getPaymentDetails(paymentId);
            return ResponseEntity.ok(Map.of(
                    "paymentId", payment.getId(),
                    "state", payment.getState(),
                    "intent", payment.getIntent()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Errore nel recupero dei dettagli: " + e.getMessage()));
        }
    }

    /**
     * Metodo per testare l'integrazione PayPal.
     * 
     * @param transazioneDTO dati della transazione di test
     * @return ResponseEntity con esito del test
     */
    @PostMapping("/test-payment")
    public ResponseEntity<?> testPayment(@RequestBody TransazioneDTO transazioneDTO) {
        try {
            boolean success = payPalService.processTestPayment(transazioneDTO);
            return ResponseEntity.ok(Map.of(
                    "success", success,
                    "message", success ? "Pagamento test completato" : "Pagamento test fallito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Errore nel test: " + e.getMessage()));
        }
    }
}
