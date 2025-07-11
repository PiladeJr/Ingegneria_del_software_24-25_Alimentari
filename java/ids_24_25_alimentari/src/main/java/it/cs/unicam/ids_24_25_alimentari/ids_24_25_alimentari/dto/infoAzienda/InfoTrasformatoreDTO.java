package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.azienda.AziendaPreviewDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfoTrasformatoreDTO extends InfoProduttoreDTO{
    private List<AziendaPreviewDTO> aziendeCollaboratrici;

    public InfoTrasformatoreDTO(InfoAzienda infoTrasformatore, List<Azienda> aziendeCollaboratrici) {
        super(infoTrasformatore);
        for (Azienda azienda : aziendeCollaboratrici) {
            if (azienda == null) {
               return;
            }
            this.aziendeCollaboratrici.add(new AziendaPreviewDTO(azienda));
        }
    }
}
