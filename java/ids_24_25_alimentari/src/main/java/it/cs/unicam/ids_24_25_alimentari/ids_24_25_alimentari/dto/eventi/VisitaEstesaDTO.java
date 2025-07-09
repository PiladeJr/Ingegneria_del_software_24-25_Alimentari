package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.azienda.AziendaPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.EventoVisita;
import lombok.Getter;

@Getter
public class VisitaEstesaDTO extends EventoEstesoDTO{

    private AziendaPreviewDTO aziendaRiferimento;
    private int numeroIscritti;
    public VisitaEstesaDTO(EventoVisita evento) {
        super(evento);
        this.aziendaRiferimento = new AziendaPreviewDTO(evento.getAziendaRiferimento());
        this.numeroIscritti = evento.getIscritti() != null ? evento.getIscritti().size() : 0;
    }
}
