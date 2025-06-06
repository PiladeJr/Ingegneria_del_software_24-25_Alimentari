package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.indirizzo;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndirizzoDTO {
    private String via;
    private String numeroCivico;
    private String cap;
    private String citta;
    private String provincia;
    private String coordinate;

    public IndirizzoDTO(Indirizzo indirizzo) {
        this.via = indirizzo.getVia();
        this.numeroCivico = indirizzo.getNumeroCivico();
        this.cap = indirizzo.getCap();
        this.citta = indirizzo.getCitta();
        this.provincia = indirizzo.getProvincia();
        this.coordinate = indirizzo.getCoordinate();
    }

    @Override
    public String toString() {
        return via + " " + numeroCivico + ", " + cap + " " + citta + " (" + provincia + ")";
    }
}
