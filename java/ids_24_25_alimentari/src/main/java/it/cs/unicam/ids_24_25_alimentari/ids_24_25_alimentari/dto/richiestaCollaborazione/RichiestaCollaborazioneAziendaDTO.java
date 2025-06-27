
package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import jakarta.validation.Valid;

@Getter
@Setter
public class RichiestaCollaborazioneAziendaDTO {
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private Ruolo ruolo;
    private String denSociale;
    @Valid
    private Indirizzo sedeLegale;
    @Valid
    private Indirizzo sedeOperativa;
    private String iban;
    private String iva;
    private MultipartFile certificato;
    private MultipartFile cartaIdentita;


}
