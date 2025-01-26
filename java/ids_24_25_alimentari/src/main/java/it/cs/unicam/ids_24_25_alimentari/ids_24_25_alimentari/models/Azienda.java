package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.File;
import java.util.List;

@Entity
@Table(name = "aziende")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Azienda {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "denominazione_sociale", nullable = false)
    private String denominazioneSociale;

    @OneToOne(cascade = CascadeType.ALL)
    private Indirizzo sedeLegale;

    @OneToOne(cascade = CascadeType.ALL)
    private Indirizzo sedeOperativa;

    @Column(name = "iva", nullable = false)
    private String iva;

    @Column(name = "iban", nullable = false)
    private String iban;
    @ElementCollection
    @CollectionTable(name = "immagini", joinColumns = @JoinColumn(name = "richiesta_id"))
    @Column(name = "immagine")
    private List<File> immagini;

    //@ElementCollection
    //@CollectionTable(name = "certificati", joinColumns = @JoinColumn(name = "richiesta_id"))
    @Column(name = "certificato")
    private File certificato;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDenominazioneSociale() {
        return denominazioneSociale;
    }

    public void setDenominazioneSociale(String denominazioneSociale) {
        this.denominazioneSociale = denominazioneSociale;
    }

    public Indirizzo getSedeLegale() {
        return sedeLegale;
    }

    public void setSedeLegale(Indirizzo sedeLegale) {
        this.sedeLegale = sedeLegale;
    }

    public Indirizzo getSedeOperativa() {
        return sedeOperativa;
    }

    public void setSedeOperativa(Indirizzo sedeOperativa) {
        this.sedeOperativa = sedeOperativa;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public List<File> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<File> immagini) {
        this.immagini = immagini;
    }

    public File getCertificato() {
        return certificato;
    }

    public void setCertificato(File certificato) { this.certificato = certificato; }

}
