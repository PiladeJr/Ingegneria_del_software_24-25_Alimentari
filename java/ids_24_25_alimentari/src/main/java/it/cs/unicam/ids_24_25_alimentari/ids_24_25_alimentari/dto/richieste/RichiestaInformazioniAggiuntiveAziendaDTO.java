package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class RichiestaInformazioniAggiuntiveAziendaDTO extends RichiestaDTO {
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
    private Optional<Long[]> aziendeCollegate = Optional.empty();

    public Long[] getAziendeCollegate() {
        return aziendeCollegate.orElse(new Long[0]);
    }

    public void setAziendeCollegate(Long[] aziendeCollegate) {
        this.aziendeCollegate = Optional.ofNullable(aziendeCollegate);
    }
}
