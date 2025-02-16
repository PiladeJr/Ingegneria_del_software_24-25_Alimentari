package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste;

import org.springframework.web.multipart.MultipartFile;

public class RichiestaInformazioniDTO extends RichiestaDTO {
    private String descrizione;
    private String produzione;
    private String metodologie;
    private MultipartFile[] immagini;
    private MultipartFile[] certificati;
    private Long[] idAziendeCollegate;

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

    public MultipartFile[] getImmagini() {
        return immagini;
    }

    public void setImmagini(MultipartFile[] immagini) {
        this.immagini = immagini;
    }

    public MultipartFile[] getCertificati() {
        return certificati;
    }

    public void setCertificati(MultipartFile[] certificati) {
        this.certificati = certificati;
    }

    public Long[] getIdAziendeCollegate() {
        return idAziendeCollegate;
    }

    public void setIdAziendeCollegate(Long[] idAziendeCollegate) {
        this.idAziendeCollegate = idAziendeCollegate;
    }
}
