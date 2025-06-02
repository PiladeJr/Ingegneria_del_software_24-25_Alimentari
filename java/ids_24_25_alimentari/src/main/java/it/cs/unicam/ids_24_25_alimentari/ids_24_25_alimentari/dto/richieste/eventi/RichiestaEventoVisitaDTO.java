package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
public class RichiestaEventoVisitaDTO extends RichiestaEventoDTO {
    private Azienda aziendaRiferimento;
    private List<Utente> iscritti;
}
