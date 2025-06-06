package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.azienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.indirizzo.IndirizzoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import lombok.Getter;

@Getter
public class AziendaPreviewDTO {
    private String denominazioneSociale;
    private IndirizzoDTO indirizzo;

    public AziendaPreviewDTO(Azienda azienda) {
        this.denominazioneSociale = azienda.getDenominazioneSociale();
        this.indirizzo = new IndirizzoDTO(azienda.getSedeLegale());
    }
}
