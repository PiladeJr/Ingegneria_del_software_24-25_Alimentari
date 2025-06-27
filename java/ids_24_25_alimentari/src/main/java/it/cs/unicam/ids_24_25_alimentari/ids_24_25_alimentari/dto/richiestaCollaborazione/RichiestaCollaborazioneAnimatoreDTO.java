package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;

import java.beans.ConstructorProperties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

@Getter
@Setter
public class RichiestaCollaborazioneAnimatoreDTO {

    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private Ruolo ruolo;
    private String aziendaReferente;
    private String iban;
    private MultipartFile cartaIdentita;

    @ConstructorProperties({ "nome", "cognome", "telefono", "email", "ruolo", "aziendaReferente", "iban",
            "cartaIdentita" })
    public RichiestaCollaborazioneAnimatoreDTO(String nome, String cognome, String telefono, String email, Ruolo ruolo,
            String aziendaReferente, String iban, MultipartFile cartaIdentita) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.email = email;
        this.ruolo = ruolo;
        this.aziendaReferente = aziendaReferente;
        this.iban = iban;
        this.cartaIdentita = cartaIdentita;
    }

}
