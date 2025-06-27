package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichiesteCollaborazioneOutputDTO {

    private Long id;
    private Boolean approvata;
    private String ruolo;
    private Long collaborazioneId;

    public RichiesteCollaborazioneOutputDTO(RichiestaCollaborazione collaborazione) {
        this.id = collaborazione.getId();
        this.approvata = collaborazione.getApprovato();
        this.ruolo = collaborazione.getRuolo().name();
        this.collaborazioneId = collaborazione.getCollaborazione().getId();
    }
}
