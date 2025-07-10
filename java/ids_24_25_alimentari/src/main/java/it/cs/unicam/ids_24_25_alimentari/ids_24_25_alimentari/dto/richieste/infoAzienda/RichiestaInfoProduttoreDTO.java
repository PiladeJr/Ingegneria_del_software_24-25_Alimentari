package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.infoAzienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.prodotti.RichiestaDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class RichiestaInfoProduttoreDTO extends RichiestaDTO {
    @NotNull(message = "Descrizione obbligatoria")
    private String descrizione;
    @NotNull(message = "Produzione obbligatoria")
    private String produzione;
    @NotNull(message = "Metodologie obbligatorie")
    private String metodologie;
    @NotNull(message = "Immagini obbligatorie")
    private MultipartFile[] immagini;
    @NotNull(message = "Certificati obbligatori")
    private MultipartFile[] certificati;

}
