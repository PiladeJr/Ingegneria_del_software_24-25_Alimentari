package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RichiestaProdottoDTO extends RichiestaDTO {
    @NotNull(message = "Nome prodotto obbligatorio")
    private String nome;
    private String descrizione;
    @NotNull(message = "Id azienda obbligatorio")
    private Long idAzienda;
    private MultipartFile[] immagini;
    @NotNull(message = "Prezzo prodotto obbligatorio")
    private Double prezzo;
    @NotNull(message = "Quantita prodotto obbligatoria")
    private Integer quantita;
    private String allergeni;
    private String tecniche;

}
