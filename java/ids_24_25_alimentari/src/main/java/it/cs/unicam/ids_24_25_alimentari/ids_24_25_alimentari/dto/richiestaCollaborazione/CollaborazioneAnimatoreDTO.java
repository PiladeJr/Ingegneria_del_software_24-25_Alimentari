package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class CollaborazioneAnimatoreDTO {
    private Long id;
    private Boolean approvata;
    private String ruolo;
    private Long collaborazioneId;
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private String iban;
    private File cartaIdentita;
    private File cv;

    public CollaborazioneAnimatoreDTO(RichiestaCollaborazione collaborazione) {
        this.id = collaborazione.getId();
        this.approvata = collaborazione.getApprovato();
        this.ruolo = collaborazione.getRuolo().name();
        this.collaborazioneId = collaborazione.getCollaborazione().getId();
        this.nome = collaborazione.getCollaborazione().getNome();
        this.cognome = collaborazione.getCollaborazione().getCognome();
        this.telefono = collaborazione.getCollaborazione().getTelefono();
        this.email = collaborazione.getCollaborazione().getEmail();
        this.iban = collaborazione.getCollaborazione().getIban();
        this.cartaIdentita = collaborazione.getCollaborazione().getCartaIdentita();
        this.cv = collaborazione.getCollaborazione().getCv();
    }
}
