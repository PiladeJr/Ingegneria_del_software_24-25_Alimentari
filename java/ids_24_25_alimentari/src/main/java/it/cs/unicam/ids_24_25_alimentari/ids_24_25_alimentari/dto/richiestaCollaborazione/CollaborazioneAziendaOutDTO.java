package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.indirizzo.IndirizzoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class CollaborazioneAziendaOutDTO {

    private Long id;
    private String status;
    private String ruolo;
    private Long collaborazioneId;
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private String iban;
    private String denSociale;
    private IndirizzoDTO sedeLegale;
    private IndirizzoDTO sedeOperativa;
    private String iva;
    private File certificato;
    private File cartaIdentita;

    public CollaborazioneAziendaOutDTO(RichiestaCollaborazione collaborazione) {
        this.id = collaborazione.getId();
        this.status = collaborazione.getStatus().toString();
        this.ruolo = collaborazione.getRuolo().name();
        this.collaborazioneId = collaborazione.getCollaborazione().getId();
        this.nome = collaborazione.getCollaborazione().getNome();
        this.cognome = collaborazione.getCollaborazione().getCognome();
        this.telefono = collaborazione.getCollaborazione().getTelefono();
        this.email = collaborazione.getCollaborazione().getEmail();
        this.iban = collaborazione.getCollaborazione().getIban();
        this.denSociale = collaborazione.getCollaborazione().getDenominazioneSociale();
        this.sedeLegale = new IndirizzoDTO(collaborazione.getCollaborazione().getSedeLegale());
        this.sedeOperativa = new IndirizzoDTO(collaborazione.getCollaborazione().getSedeOperativa());
        this.iva = collaborazione.getCollaborazione().getIva();
        this.certificato = collaborazione.getCollaborazione().getCertificato();
        this.cartaIdentita = collaborazione.getCollaborazione().getCartaIdentita();
    }
}
