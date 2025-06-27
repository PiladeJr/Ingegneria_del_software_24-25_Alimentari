package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.InformazioniAggiuntive;
import jakarta.annotation.Nullable;
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

    @ManyToOne
    @JoinColumn(name = "indirizzoSedeLegale_id", nullable = false)
    private Indirizzo sedeLegale;

    @ManyToOne
    @JoinColumn(name = "indirizzoSedeOperativa_id", nullable = false)
    private Indirizzo sedeOperativa;

    @Column(name = "iva", nullable = false)
    private String iva;

    @Column(name = "certificato")
    private File certificato;

    @OneToOne(cascade = CascadeType.ALL)
    @Nullable
    private InformazioniAggiuntive informazioniAggiuntive;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "prodotto", nullable = true)
    private List<ProdottoSingolo> prodotti;

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


    public File getCertificato() {
        return certificato;
    }

    public void setCertificato(File certificato) {
        this.certificato = certificato;
    }

    @Nullable
    public InformazioniAggiuntive getInformazioniAggiuntive() {
        return informazioniAggiuntive;
    }

    public void setInformazioniAggiuntive(@Nullable InformazioniAggiuntive informazioniAggiuntive) {
        this.informazioniAggiuntive = informazioniAggiuntive;
    }
}
