package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.infoAzienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class RichiestaInfoTrasformatoreDTO {
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
    private Optional<List<Azienda>> aziendeCollegate = Optional.empty();

    public List<Azienda>  getAziendeCollegate() {
        return aziendeCollegate.orElse(new ArrayList<>());
    }

    public void setAziendeCollegate(List<Azienda>  aziendeCollegate) {
        this.aziendeCollegate = Optional.ofNullable(aziendeCollegate);
    }
}
