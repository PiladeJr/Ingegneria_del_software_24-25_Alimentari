package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import java.io.File;
import java.util.List;

@Entity
public class RichiestaInformazioniAggiuntive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descrizioneAzienda;
    private String descrizioneProduzione;
    private String descrizioneMetodi;

    @ElementCollection
    @CollectionTable(name = "immagini", joinColumns = @JoinColumn(name = "richiesta_id"))
    @Column(name = "immagine")
    private List<File> immagini;

    @ElementCollection
    @CollectionTable(name = "certificati", joinColumns = @JoinColumn(name = "richiesta_id"))
    @Column(name = "certificato")
    private List<File> certificati;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizioneAzienda() {
        return descrizioneAzienda;
    }

    public void setDescrizioneAzienda(String descrizioneAzienda) {
        this.descrizioneAzienda = descrizioneAzienda;
    }

    public String getDescrizioneProduzione() {
        return descrizioneProduzione;
    }

    public void setDescrizioneProduzione(String descrizioneProduzione) {
        this.descrizioneProduzione = descrizioneProduzione;
    }

    public String getDescrizioneMetodi() {
        return descrizioneMetodi;
    }

    public void setDescrizioneMetodi(String descrizioneMetodi) {
        this.descrizioneMetodi = descrizioneMetodi;
    }

    public List<File> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<File> immagini) {
        this.immagini = immagini;
    }

    public List<File> getCertificati() {
        return certificati;
    }

    public void setCertificati(List<File> certificati) {
        this.certificati = certificati;
    }
}