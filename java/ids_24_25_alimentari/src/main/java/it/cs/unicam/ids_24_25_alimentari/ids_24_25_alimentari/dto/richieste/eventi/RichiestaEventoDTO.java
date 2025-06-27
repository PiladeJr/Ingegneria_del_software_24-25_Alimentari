package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.StatusEvento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class RichiestaEventoDTO {
    private String titolo;
    private String descrizione;
    private StatusEvento status;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private MultipartFile locandina;
    private Indirizzo indirizzo;
    private Utente creatore;
}
