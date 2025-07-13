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
public class RichiestaInfoTrasformatoreDTO extends RichiestaInfoProduttoreDTO{

    private List<Azienda> aziendeCollegate = new ArrayList<>();
}
