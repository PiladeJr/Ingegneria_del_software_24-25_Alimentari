package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.io.File;
import java.util.List;

@Entity
@Table(name = "aziende")
@EntityListeners(AuditingEntityListener.class)
public class Azienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "denominazione_sociale", nullable = false)
    private String denominazioneSociale;

    @OneToMany(cascade = CascadeType.ALL)
    private Indirizzo sedeLegale;

    @OneToMany(cascade = CascadeType.ALL)
    private Indirizzo sedeOperativa;

    @Column(name = "iva", nullable = false)
    private String iva;

    @Column(name = "iban", nullable = false)
    private String iban;

    @OneToMany(cascade = CascadeType.ALL)
    private List<File> files;

    public Azienda(String denominazioneSociale, Indirizzo sedeLegale, Indirizzo sedeOperativa, String iva, String iban,
            List<File> files) {
        this.denominazioneSociale = denominazioneSociale;
        this.sedeLegale = sedeLegale;
        this.sedeOperativa = sedeOperativa;
        this.iva = iva;
        this.iban = iban;
        this.files = files;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

}
