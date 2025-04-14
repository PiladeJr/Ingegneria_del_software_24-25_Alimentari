package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RichiestaDTO {
    private Tipologia tipologia;
    public Tipologia getTipologia() {
        return tipologia;
    }
    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }

}