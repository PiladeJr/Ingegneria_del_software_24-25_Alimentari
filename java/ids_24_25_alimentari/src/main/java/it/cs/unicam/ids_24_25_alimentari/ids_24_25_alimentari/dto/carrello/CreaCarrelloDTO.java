package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.carrello;

import java.util.List;

public class CreaCarrelloDTO {
    private Long idUtente;
    private List<Long> idProdotti;

    public CreaCarrelloDTO(Long idUtente, List<Long> idProdotti) {
        this.idUtente = idUtente;
        this.idProdotti = idProdotti;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public List<Long> getIdProdotti() {
        return idProdotti;
    }

    public void setIdProdotti(List<Long> idProdotti) {
        this.idProdotti = idProdotti;
    }
}
