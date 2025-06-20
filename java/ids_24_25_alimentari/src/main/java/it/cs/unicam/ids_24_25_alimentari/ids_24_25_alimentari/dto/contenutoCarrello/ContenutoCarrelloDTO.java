package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.contenutoCarrello;

public class ContenutoCarrelloDTO {

    private Long prodottoId;
    private int quantita;

    public ContenutoCarrelloDTO() {
    }

    public ContenutoCarrelloDTO(Long prodottoId, int quantita) {
        this.prodottoId = prodottoId;
        this.quantita = quantita;
    }

    public Long getProdottoId() {
        return prodottoId;
    }

    public void setProdottoId(Long prodottoId) {
        this.prodottoId = prodottoId;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
