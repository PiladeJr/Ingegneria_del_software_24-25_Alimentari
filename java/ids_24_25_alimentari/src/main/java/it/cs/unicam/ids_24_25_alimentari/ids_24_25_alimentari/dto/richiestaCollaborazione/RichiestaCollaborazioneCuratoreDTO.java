package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

@Getter
@Setter
public class RichiestaCollaborazioneCuratoreDTO {
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private Ruolo ruolo;
    private String iban;
    private MultipartFile cartaIdentita;
    private MultipartFile cv;

    public RichiestaCollaborazioneCuratoreDTO() {
    }


}
