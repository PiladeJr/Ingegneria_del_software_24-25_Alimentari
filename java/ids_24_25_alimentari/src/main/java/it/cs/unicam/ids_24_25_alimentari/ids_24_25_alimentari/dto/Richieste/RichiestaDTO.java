package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Tipologia;

public class RichiestaDTO {
    private Tipologia tipologia;
    private Long idMittente;
    public Tipologia getTipologia() {
        return tipologia;
    }
    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }
    public Long getIdMittente() {
        return idMittente;
    }
    public void setIdMittente(Long idMittente) {
        this.idMittente = idMittente;
    }
}