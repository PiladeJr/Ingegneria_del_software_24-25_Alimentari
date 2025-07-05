package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.configurazioni.PayPalConfig;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione.TransazioneDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private PayPalConfig payPalConfig;

    /**
     * Crea un pagamento PayPal
     */
    public Payment createPayment(TransazioneDTO transazioneDTO, String successUrl, String cancelUrl)
            throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(formatPrice(transazioneDTO.getImporto()));

        Transaction transaction = new Transaction();
        transaction.setDescription("Pagamento ordine alimentari");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl != null ? cancelUrl : payPalConfig.getCancelUrl());
        redirectUrls.setReturnUrl(successUrl != null ? successUrl : payPalConfig.getReturnUrl());
        payment.setRedirectUrls(redirectUrls);

        return payment.create(getAPIContext());
    }

    /**
     * Esegue un pagamento PayPal già approvato
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        return payment.execute(getAPIContext(), paymentExecute);
    }

    /**
     * Ottiene i dettagli di un pagamento
     */
    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        return Payment.get(getAPIContext(), paymentId);
    }

    /**
     * Verifica se un pagamento è stato completato con successo
     */
    public boolean isPaymentSuccessful(Payment payment) {
        if (payment == null || payment.getTransactions() == null) {
            return false;
        }

        return "approved".equals(payment.getState()) &&
                payment.getTransactions().stream()
                        .anyMatch(t -> t.getRelatedResources() != null &&
                                t.getRelatedResources().stream()
                                        .anyMatch(r -> r.getSale() != null &&
                                                "completed".equals(r.getSale().getState())));
    }

    /**
     * Metodo semplificato per il test - simula un pagamento PayPal
     */
    public boolean processTestPayment(TransazioneDTO transazioneDTO) {
        try {
            // In un ambiente di test, possiamo simulare il processo PayPal
            if (transazioneDTO.getImporto() <= 0) {
                return false;
            }

            // Simula una chiamata PayPal che ha successo nel 90% dei casi
            // In produzione, qui ci sarebbe la logica reale di PayPal
            return Math.random() > 0.1; // 90% di successo

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Metodo per processare un pagamento PayPal reale
     */
    public PaymentResult processPayment(TransazioneDTO transazioneDTO, String successUrl, String cancelUrl) {
        try {
            // Crea il pagamento PayPal
            Payment payment = createPayment(transazioneDTO, successUrl, cancelUrl);

            // Estrae l'URL di approvazione da PayPal
            String approvalUrl = null;
            for (Links link : payment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    approvalUrl = link.getHref();
                    break;
                }
            }

            return new PaymentResult(payment.getId(), approvalUrl, true, null);

        } catch (PayPalRESTException e) {
            return new PaymentResult(null, null, false, e.getMessage());
        }
    }

    /**
     * Completa il pagamento dopo l'approvazione dell'utente
     */
    public PaymentResult completePayment(String paymentId, String payerId) {
        try {
            Payment executedPayment = executePayment(paymentId, payerId);
            boolean isSuccessful = isPaymentSuccessful(executedPayment);

            return new PaymentResult(
                    executedPayment.getId(),
                    null,
                    isSuccessful,
                    isSuccessful ? "Pagamento completato con successo" : "Pagamento fallito");

        } catch (PayPalRESTException e) {
            return new PaymentResult(paymentId, null, false, e.getMessage());
        }
    }

    /**
     * Classe per rappresentare il risultato di un'operazione PayPal
     */
    public static class PaymentResult {
        private final String paymentId;
        private final String approvalUrl;
        private final boolean success;
        private final String message;

        public PaymentResult(String paymentId, String approvalUrl, boolean success, String message) {
            this.paymentId = paymentId;
            this.approvalUrl = approvalUrl;
            this.success = success;
            this.message = message;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public String getApprovalUrl() {
            return approvalUrl;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Crea il contesto API per PayPal
     */
    private APIContext getAPIContext() {
        try {
            String clientId = payPalConfig.getClientId();
            String clientSecret = payPalConfig.getClientSecret();
            String mode = payPalConfig.getMode();

            System.out.println("=== DEBUG PAYPAL CONFIG ===");
            System.out.println("Client ID: "
                    + (clientId != null ? clientId.substring(0, Math.min(10, clientId.length())) + "..." : "NULL"));
            System.out.println("Client Secret: " + (clientSecret != null ? "***PROVIDED***" : "NULL"));
            System.out.println("Mode: " + mode);
            System.out.println("==========================");

            if (clientId == null || clientSecret == null) {
                throw new RuntimeException("PayPal credentials non configurate correttamente");
            }

            APIContext apiContext = new APIContext(clientId, clientSecret, mode);

            // Imposta configurazioni aggiuntive per il debug
            apiContext.addConfiguration("http.ConnectionTimeOut", "30000");
            apiContext.addConfiguration("http.Retry", "3");
            apiContext.addConfiguration("http.ReadTimeOut", "30000");

            return apiContext;
        } catch (Exception e) {
            System.err.println("Errore nella configurazione PayPal: " + e.getMessage());
            throw new RuntimeException("Errore nella configurazione PayPal: " + e.getMessage(), e);
        }
    }

    /**
     * Formatta il prezzo per PayPal (max 2 decimali)
     */
    private String formatPrice(double price) {
        return new BigDecimal(price)
                .setScale(2, RoundingMode.HALF_UP)
                .toString();
    }
}
