package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;

/**
 * Wrapper per la risposta dell'ordine che include informazioni PayPal
 * quando necessario, mantenendo la compatibilità con l'API esistente
 */
public class OrdineWrapper {
    private Ordine ordine;
    private String paypalPaymentId;
    private String paypalApprovalUrl;
    private boolean requiresPayment;
    private String message;

    public OrdineWrapper(Ordine ordine) {
        this.ordine = ordine;
        this.requiresPayment = false;
    }

    public OrdineWrapper(Ordine ordine, String paypalPaymentId, String paypalApprovalUrl) {
        this.ordine = ordine;
        this.paypalPaymentId = paypalPaymentId;
        this.paypalApprovalUrl = paypalApprovalUrl;
        this.requiresPayment = true;
        this.message = "Completare il pagamento PayPal per procedere";
    }

    // Getters e Setters
    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public String getPaypalPaymentId() {
        return paypalPaymentId;
    }

    public void setPaypalPaymentId(String paypalPaymentId) {
        this.paypalPaymentId = paypalPaymentId;
    }

    public String getPaypalApprovalUrl() {
        return paypalApprovalUrl;
    }

    public void setPaypalApprovalUrl(String paypalApprovalUrl) {
        this.paypalApprovalUrl = paypalApprovalUrl;
    }

    public boolean isRequiresPayment() {
        return requiresPayment;
    }

    public void setRequiresPayment(boolean requiresPayment) {
        this.requiresPayment = requiresPayment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
