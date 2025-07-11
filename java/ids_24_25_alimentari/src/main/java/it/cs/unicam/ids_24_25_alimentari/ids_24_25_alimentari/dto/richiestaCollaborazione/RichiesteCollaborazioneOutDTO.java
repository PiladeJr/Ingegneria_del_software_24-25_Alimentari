package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichiesteCollaborazioneOutDTO {

    private Long id;
    private String status;
    private String ruolo;
    private Long collaborazioneId;

    public RichiesteCollaborazioneOutDTO(RichiestaCollaborazione collaborazione) {
        this.id = collaborazione.getId();
        this.status = collaborazione.getStatus().toString();
        this.ruolo = collaborazione.getRuolo().name();
        this.collaborazioneId = collaborazione.getCollaborazione().getId();
    }
}
