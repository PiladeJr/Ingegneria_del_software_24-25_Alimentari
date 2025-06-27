package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import com.paypal.api.payments.Payment;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineResponseDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione.TransazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.StatoOrdine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.StatoTransazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.OrdineRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private IndirizzoService indirizzoService;

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private TransazioneService transazioneService;

    @Autowired
    private PayPalService payPalService;

    public List<Ordine> getAllOrdini() {
        return ordineRepository.findAll();
    }

    public Optional<Ordine> getOrdineById(Long id) {
        return ordineRepository.findById(id);
    }

    public OrdineResponseDTO salvaOrdine(OrdineDTO ordine) {
        var carrello = carrelloService.getCarrelloById(ordine.getIdCarrello())
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));
        // Recupera gli indirizzi di spedizione e fatturazione
        var indirizzoSpedizione = indirizzoService.findById(carrello.getUtente().getIndirizzoSpedizione().getId());
        var indirizzoFatturazione = indirizzoService.findById(carrello.getUtente().getIndirizzoFatturazione().getId());

        // Crea l'oggetto Ordine dal DTO
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setIndirizzoConsegna(
                indirizzoSpedizione.orElseThrow(() -> new RuntimeException("Indirizzo di spedizione non trovato")));
        nuovoOrdine.setIndirizzoFatturazione(
                indirizzoFatturazione.orElseThrow(() -> new RuntimeException("Indirizzo di fatturazione non trovato")));

        nuovoOrdine.setCarrello(carrello);
        nuovoOrdine.setUtente(carrello.getUtente());
        nuovoOrdine.setDataOrdine(java.time.LocalDateTime.now());
        nuovoOrdine.setTotale(carrello.getContenutoCarrello().stream()
                .mapToDouble(prodotto -> prodotto.getQuantita() * prodotto.getProdotto().getPrezzo())
                .sum());

        // Gestione del pagamento basata sul metodo
        TransazioneDTO transazioneDTO = new TransazioneDTO();
        transazioneDTO.setImporto(nuovoOrdine.getTotale());
        transazioneDTO.setMetodoPagamento(ordine.getMetodoPagamento());

        if ("CONTANTI".equalsIgnoreCase(ordine.getMetodoPagamento().toString())) {
            // Pagamento in contanti: transazione completata e ordine in preparazione
            transazioneDTO.setStatoTransazione(StatoTransazione.COMPLETATA);
            nuovoOrdine.setStato(StatoOrdine.IN_PREPARAZIONE);
        } else if ("PAYPAL".equalsIgnoreCase(ordine.getMetodoPagamento().toString())) {
            // Pagamento PayPal: crea il link di pagamento PayPal reale
            try {
                // Impostiamo lo stato iniziale per la transazione temporanea
                transazioneDTO.setStatoTransazione(StatoTransazione.IN_ATTESA);

                // Prima salviamo l'ordine temporaneamente per ottenere l'ID
                var transazioneTemp = transazioneService.createTransazione(transazioneDTO);
                nuovoOrdine.setTransazione(transazioneTemp);
                nuovoOrdine.setStato(StatoOrdine.IN_ATTESA);
                Ordine ordineTempSalvato = ordineRepository.save(nuovoOrdine);

                // Crea il link di pagamento PayPal con l'ID dell'ordine
                String successUrl = "http://localhost:8080/api/paypal/success?ordineId=" + ordineTempSalvato.getId();
                String cancelUrl = "http://localhost:8080/api/paypal/cancel?ordineId=" + ordineTempSalvato.getId();

                PayPalService.PaymentResult paymentResult = payPalService.processPayment(
                        transazioneDTO, successUrl, cancelUrl);

                if (paymentResult.isSuccess()) {
                    // Aggiorna la transazione con l'ID PayPal
                    transazioneDTO.setPaypalPaymentId(paymentResult.getPaymentId());

                    // Aggiorna la transazione nel database
                    transazioneService.updateTransazione(transazioneTemp.getId(), transazioneDTO);

                    // Aggiorna l'ordine
                    ordineTempSalvato.setStato(StatoOrdine.IN_ATTESA);

                    System.out.println("=== PAGAMENTO PAYPAL CREATO ===");
                    System.out.println("Ordine ID: " + ordineTempSalvato.getId());
                    System.out.println("PayPal Payment ID: " + paymentResult.getPaymentId());
                    System.out.println("PayPal Approval URL: " + paymentResult.getApprovalUrl());
                    System.out.println("Completa il pagamento visitando: " + paymentResult.getApprovalUrl());
                    System.out.println("===============================");

                    Ordine ordineSalvato = ordineRepository.save(ordineTempSalvato);

                    // Crea la response DTO
                    OrdineResponseDTO response = new OrdineResponseDTO();
                    response.setOrdineId(ordineSalvato.getId());
                    response.setStato(ordineSalvato.getStato().toString());
                    response.setTotale(ordineSalvato.getTotale());
                    response.setMetodoPagamento("PAYPAL");
                    response.setPaypalPaymentId(paymentResult.getPaymentId());
                    response.setPaypalApprovalUrl(paymentResult.getApprovalUrl());
                    response.setMessage("Pagamento PayPal creato. Completa il pagamento visitando: "
                            + paymentResult.getApprovalUrl());
                    response.setRequiresPayment(true);

                    return response;
                } else {
                    // Elimina l'ordine creato temporaneamente se il pagamento fallisce
                    ordineRepository.delete(ordineTempSalvato);
                    throw new RuntimeException(
                            "Errore nella creazione del pagamento PayPal: " + paymentResult.getMessage());
                }
            } catch (Exception e) {
                transazioneDTO.setStatoTransazione(StatoTransazione.FALLITA);
                nuovoOrdine.setStato(StatoOrdine.ANNULLATO);
                throw new RuntimeException("Errore durante la creazione del pagamento PayPal: " + e.getMessage());
            }
        } else {
            // Altri metodi di pagamento: stato in attesa
            transazioneDTO.setStatoTransazione(StatoTransazione.IN_ATTESA);
            nuovoOrdine.setStato(StatoOrdine.IN_ATTESA);
        }

        var transazione = transazioneService.createTransazione(transazioneDTO);

        nuovoOrdine.setTransazione(transazioneService.getTransazioneById(
                transazione.getId())
                .orElseThrow(() -> new RuntimeException("Transazione non trovata")));

        Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);

        // Crea la response DTO per altri metodi di pagamento
        OrdineResponseDTO response = new OrdineResponseDTO();
        response.setOrdineId(ordineSalvato.getId());
        response.setStato(ordineSalvato.getStato().toString());
        response.setTotale(ordineSalvato.getTotale());
        response.setMetodoPagamento(ordine.getMetodoPagamento().toString());

        if ("CONTANTI".equalsIgnoreCase(ordine.getMetodoPagamento().toString())) {
            response.setMessage("Ordine creato con successo. Pagamento in contanti alla consegna.");
            response.setRequiresPayment(false);
        } else {
            response.setMessage("Ordine creato con successo. In attesa di pagamento.");
            response.setRequiresPayment(true);
        }

        return response;
    }

    /**
     * Completa il pagamento PayPal per un ordine esistente
     */
    public OrdineResponseDTO completaPaymentPayPal(Long ordineId, String paymentId, String payerId) {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        OrdineResponseDTO response = new OrdineResponseDTO();
        response.setOrdineId(ordineId);
        response.setTotale(ordine.getTotale());
        response.setMetodoPagamento("PAYPAL");

        try {
            // Completa il pagamento PayPal
            PayPalService.PaymentResult paymentResult = payPalService.completePayment(paymentId, payerId);

            if (paymentResult.isSuccess()) {
                // Aggiorna l'ordine e la transazione
                ordine.setStato(StatoOrdine.IN_PREPARAZIONE);
                ordine.getTransazione().setStatoTransazione(StatoTransazione.COMPLETATA);

                ordineRepository.save(ordine);

                response.setStato(StatoOrdine.IN_PREPARAZIONE.toString());
                response.setMessage("Pagamento PayPal completato con successo. Ordine in preparazione.");
                response.setRequiresPayment(false);
            } else {
                // Pagamento fallito
                ordine.setStato(StatoOrdine.ANNULLATO);
                ordine.getTransazione().setStatoTransazione(StatoTransazione.FALLITA);

                ordineRepository.save(ordine);

                response.setStato(StatoOrdine.ANNULLATO.toString());
                response.setMessage("Pagamento PayPal fallito: " + paymentResult.getMessage());
                response.setRequiresPayment(false);
            }
        } catch (Exception e) {
            // Errore durante il completamento
            ordine.setStato(StatoOrdine.ANNULLATO);
            ordine.getTransazione().setStatoTransazione(StatoTransazione.FALLITA);

            ordineRepository.save(ordine);

            response.setStato(StatoOrdine.ANNULLATO.toString());
            response.setMessage("Errore durante il completamento del pagamento: " + e.getMessage());
            response.setRequiresPayment(false);
        }

        return response;
    }

    /**
     * Completa il pagamento PayPal usando il PayPal payment ID
     */
    public OrdineResponseDTO completaPaymentPayPalByPaymentId(String paymentId, String payerId) {
        // Trova l'ordine tramite il PayPal payment ID
        Ordine ordine = ordineRepository.findByPaypalPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato per il payment ID: " + paymentId));

        OrdineResponseDTO response = new OrdineResponseDTO();
        response.setOrdineId(ordine.getId());
        response.setTotale(ordine.getTotale());
        response.setMetodoPagamento("PAYPAL");

        try {
            // Completa il pagamento PayPal
            PayPalService.PaymentResult paymentResult = payPalService.completePayment(paymentId, payerId);

            if (paymentResult.isSuccess()) {
                // Aggiorna l'ordine e la transazione
                ordine.setStato(StatoOrdine.IN_PREPARAZIONE);
                ordine.getTransazione().setStatoTransazione(StatoTransazione.COMPLETATA);

                ordineRepository.save(ordine);

                response.setStato(StatoOrdine.IN_PREPARAZIONE.toString());
                response.setMessage("Pagamento PayPal completato con successo. Ordine in preparazione.");
                response.setRequiresPayment(false);
            } else {
                // Pagamento fallito
                ordine.setStato(StatoOrdine.ANNULLATO);
                ordine.getTransazione().setStatoTransazione(StatoTransazione.FALLITA);

                ordineRepository.save(ordine);

                response.setStato(StatoOrdine.ANNULLATO.toString());
                response.setMessage("Pagamento PayPal fallito: " + paymentResult.getMessage());
                response.setRequiresPayment(false);
            }
        } catch (Exception e) {
            // Errore durante il completamento
            ordine.setStato(StatoOrdine.ANNULLATO);
            ordine.getTransazione().setStatoTransazione(StatoTransazione.FALLITA);

            ordineRepository.save(ordine);

            response.setStato(StatoOrdine.ANNULLATO.toString());
            response.setMessage("Errore durante il completamento del pagamento: " + e.getMessage());
            response.setRequiresPayment(false);
        }

        return response;
    }

    /**
     * Completa il pagamento PayPal usando il token (usato nei redirect di PayPal)
     */
    public OrdineResponseDTO completaPaymentPayPalByToken(String token, String payerId) {
        try {
            // Prima otteniamo i dettagli del pagamento dal token
            Payment payment = payPalService.getPaymentDetails(token);
            String paymentId = payment.getId();

            // Ora usiamo il metodo esistente con il Payment ID
            return completaPaymentPayPalByPaymentId(paymentId, payerId);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel completamento del pagamento tramite token: " + e.getMessage());
        }
    }

    /**
     * Annulla un ordine PayPal tramite token (usato nei redirect di PayPal)
     */
    public OrdineResponseDTO cancellaPaymentPayPalByToken(String token) {
        try {
            // Prima otteniamo i dettagli del pagamento dal token
            Payment payment = payPalService.getPaymentDetails(token);
            String paymentId = payment.getId();

            // Ora usiamo il metodo esistente con il Payment ID
            return cancellaPaymentPayPalByPaymentId(paymentId);
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'annullamento del pagamento tramite token: " + e.getMessage());
        }
    }

    /**
     * Annulla un ordine PayPal tramite payment ID
     */
    public OrdineResponseDTO cancellaPaymentPayPalByPaymentId(String paymentId) {
        // Trova l'ordine tramite il PayPal payment ID
        Ordine ordine = ordineRepository.findByPaypalPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato per il payment ID: " + paymentId));

        OrdineResponseDTO response = new OrdineResponseDTO();
        response.setOrdineId(ordine.getId());
        response.setTotale(ordine.getTotale());
        response.setMetodoPagamento("PAYPAL");

        // Aggiorna l'ordine e la transazione come annullati
        ordine.setStato(StatoOrdine.ANNULLATO);
        ordine.getTransazione().setStatoTransazione(StatoTransazione.ANNULLATA);

        ordineRepository.save(ordine);

        response.setStato(StatoOrdine.ANNULLATO.toString());
        response.setMessage("Pagamento PayPal annullato dall'utente.");
        response.setRequiresPayment(false);

        return response;
    }

    /**
     * Annulla un ordine direttamente tramite ID
     */
    public OrdineResponseDTO cancellaOrdineById(Long ordineId) {
        Optional<Ordine> ordineOpt = ordineRepository.findById(ordineId);
        if (!ordineOpt.isPresent()) {
            throw new RuntimeException("Ordine non trovato con ID: " + ordineId);
        }

        Ordine ordine = ordineOpt.get();

        // Verifica che l'ordine possa essere annullato
        if (ordine.getStato() == StatoOrdine.CONSEGNATO || ordine.getStato() == StatoOrdine.ANNULLATO) {
            throw new RuntimeException("Impossibile annullare un ordine già " + ordine.getStato().getDescrizione());
        }

        // Aggiorna lo stato a ANNULLATO
        ordine.setStato(StatoOrdine.ANNULLATO);

        // Aggiorna anche la transazione ad ANNULLATA
        if (ordine.getTransazione() != null) {
            ordine.getTransazione().setStatoTransazione(StatoTransazione.ANNULLATA);
        }

        ordineRepository.save(ordine);

        // Crea la risposta
        OrdineResponseDTO response = new OrdineResponseDTO();
        response.setOrdineId(ordine.getId());
        response.setStato(ordine.getStato().getDescrizione());
        response.setTotale(ordine.getTotale());
        // Il metodo getPagamento non esiste nella classe Ordine, usiamo la transazione
        if (ordine.getTransazione() != null) {
            response.setMetodoPagamento(ordine.getTransazione().getMetodoPagamento().toString());
        } else {
            response.setMetodoPagamento("Non specificato");
        }
        response.setMessage("Ordine annullato con successo.");
        response.setRequiresPayment(false);

        return response;
    }

    public Ordine aggiornaStatoOrdine(Long id, StatoOrdine nuovoStato) {
        Optional<Ordine> ordineOpt = ordineRepository.findById(id);
        if (ordineOpt.isPresent()) {
            Ordine ordine = ordineOpt.get();
            ordine.setStato(nuovoStato);
            return ordineRepository.save(ordine);
        }
        throw new RuntimeException("Ordine non trovato");
    }

    /**
     * Completa il pagamento PayPal usando solo ordineId e payerId (recupera
     * automaticamente il paymentId)
     */
    public OrdineResponseDTO completaPaymentPayPal(Long ordineId, String payerId) {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        // Recupera il PayPal payment ID dalla transazione dell'ordine
        String paymentId = ordine.getTransazione().getPaypalPaymentId();

        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new RuntimeException("PayPal payment ID non trovato per l'ordine: " + ordineId);
        }

        // Usa il metodo esistente che richiede il paymentId
        return completaPaymentPayPal(ordineId, paymentId, payerId);
    }
}
