package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(Long idAzienda) {
        this.idAzienda = idAzienda;
    }

    public MultipartFile[] getImmagini() {
        return immagini;
    }

    public void setImmagini(MultipartFile[] immagini) {
        this.immagini = immagini;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public String getAllergeni() {
        return allergeni;
    }

    public void setAllergeni(String allergeni) {
        this.allergeni = allergeni;
    }

    public String getTecniche() {
        return tecniche;
    }

    public void setTecniche(String tecniche) {
        this.tecniche = tecniche;
    }
}
