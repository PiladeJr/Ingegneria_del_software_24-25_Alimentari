package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine;

import java.util.List;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.MetodiPagamentoTransazione;

public class OrdineDTO {
    private Long idCarrello;
    private Long indirizzo_consegna_id;
    private Long indirizzo_fatturazione_id;
    private MetodiPagamentoTransazione metodoPagamento;

    public OrdineDTO() {
    }

    public OrdineDTO(Long clienteId, List<Long> prodottiId, Long indirizzo_consegna_id,
            Long indirizzo_fatturazione_id, Long idCarrello, MetodiPagamentoTransazione metodoPagamento) {

        this.indirizzo_consegna_id = indirizzo_consegna_id;
        this.indirizzo_fatturazione_id = indirizzo_fatturazione_id;
        this.idCarrello = idCarrello;
        this.metodoPagamento = metodoPagamento; // Default value, can be changed later
    }

    public Long getIdCarrello() {
        return idCarrello;
    }

    public void setIdCarrello(Long idCarrello) {
        this.idCarrello = idCarrello;
    }

    public Long getIndirizzo_consegna_id() {
        return indirizzo_consegna_id;
    }

    public void setIndirizzo_consegna_id(Long indirizzo_consegna_id) {
        this.indirizzo_consegna_id = indirizzo_consegna_id;
    }

    public Long getIndirizzo_fatturazione_id() {
        return indirizzo_fatturazione_id;
    }

    public void setIndirizzo_fatturazione_id(Long indirizzo_fatturazione_id) {
        this.indirizzo_fatturazione_id = indirizzo_fatturazione_id;
    }

    public MetodiPagamentoTransazione getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodiPagamentoTransazione metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}