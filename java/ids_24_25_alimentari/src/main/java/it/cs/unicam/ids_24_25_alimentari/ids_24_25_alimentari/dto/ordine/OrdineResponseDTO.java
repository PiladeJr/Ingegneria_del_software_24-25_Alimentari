package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine;

public class OrdineResponseDTO {
    private Long ordineId;
    private String stato;
    private Double totale;
    private String metodoPagamento;
    private String paypalPaymentId;
    private String paypalApprovalUrl;
    private String message;
    private boolean requiresPayment;

    public OrdineResponseDTO() {
    }

    public OrdineResponseDTO(Long ordineId, String stato, Double totale, String metodoPagamento) {
        this.ordineId = ordineId;
        this.stato = stato;
        this.totale = totale;
        this.metodoPagamento = metodoPagamento;
        this.requiresPayment = false;
    }

    // Getters e Setters
    public Long getOrdineId() {
        return ordineId;
    }

    public void setOrdineId(Long ordineId) {
        this.ordineId = ordineId;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Double getTotale() {
        return totale;
    }

    public void setTotale(Double totale) {
        this.totale = totale;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRequiresPayment() {
        return requiresPayment;
    }

    public void setRequiresPayment(boolean requiresPayment) {
        this.requiresPayment = requiresPayment;
    }
}
