package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class InfoProduttoreDTO {
    private String descrizione;
    private String produzione;
    private String metodiProduzione;
    private List<File> immagini = new ArrayList<>();
    private List<File> certificati = new ArrayList<>();

    public InfoProduttoreDTO(InfoAzienda infoAzienda) {
        this.descrizione = infoAzienda.getDescrizioneAzienda();
        this.produzione = infoAzienda.getDescrizioneProduzione();
        this.metodiProduzione = infoAzienda.getDescrizioneMetodi();
        if (infoAzienda.getImmagini() == null){
            this.immagini = Collections.emptyList();
        }
        else {
            this.immagini.addAll(infoAzienda.getImmagini());
        }
        if (infoAzienda.getCertificati() == null){
            this.certificati = Collections.emptyList();
        }
        else {
            this.certificati.addAll(infoAzienda.getCertificati());
        }
    }
}
