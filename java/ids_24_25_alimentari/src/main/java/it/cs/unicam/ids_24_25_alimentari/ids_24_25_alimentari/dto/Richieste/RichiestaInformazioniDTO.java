package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste.RichiestaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class RichiestaInformazioniDTO extends RichiestaDTO {
    private String descrizione;
    private String produzione;
    private String metodologie;
    private List<MultipartFile> immagini;
    private List<MultipartFile> certificati;

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getProduzione() {
        return produzione;
    }

    public void setProduzione(String produzione) {
        this.produzione = produzione;
    }

    public String getMetodologie() {
        return metodologie;
    }

    public void setMetodologie(String metodologie) {
        this.metodologie = metodologie;
    }

    public List<MultipartFile> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<MultipartFile> immagini) {
        this.immagini = immagini;
    }

    public List<MultipartFile> getCertificati() {
        return certificati;
    }

    public void setCertificati(List<MultipartFile> certificati) {
        this.certificati = certificati;
    }
}
