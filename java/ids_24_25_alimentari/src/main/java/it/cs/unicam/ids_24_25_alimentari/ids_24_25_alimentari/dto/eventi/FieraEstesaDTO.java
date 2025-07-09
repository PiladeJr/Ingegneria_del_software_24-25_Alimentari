package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.azienda.AziendaPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.EventoFiera;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FieraEstesaDTO extends EventoEstesoDTO {

private List<AziendaPreviewDTO> aziendePresenti;

    public FieraEstesaDTO(EventoFiera evento) {
        super(evento);
        this.aziendePresenti = evento.getAziendePresenti().stream()
                .map(AziendaPreviewDTO::new)
                .collect(Collectors.toList());
    }
}
