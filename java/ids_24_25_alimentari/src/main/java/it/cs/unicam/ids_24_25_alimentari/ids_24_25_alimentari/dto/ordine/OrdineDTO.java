package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine;

import java.util.List;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.MetodiPagamentoTransazione;

public class OrdineDTO {
    private Long idCarrello;
    private MetodiPagamentoTransazione metodoPagamento;

    public OrdineDTO() {
    }

    public OrdineDTO(Long clienteId, List<Long> prodottiId,
            Long idCarrello, MetodiPagamentoTransazione metodoPagamento) {

        this.idCarrello = idCarrello;
        this.metodoPagamento = metodoPagamento;
    }

    public Long getIdCarrello() {
        return idCarrello;
    }

    public void setIdCarrello(Long idCarrello) {
        this.idCarrello = idCarrello;
    }

    public MetodiPagamentoTransazione getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodiPagamentoTransazione metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}