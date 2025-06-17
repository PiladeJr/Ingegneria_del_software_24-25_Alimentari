package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesteCollaborazione;

import java.io.File;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "richiestacollaborazione")
@NoArgsConstructor
public class RichiestaCollaborazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private Ruolo ruolo;
    private String denominazioneSociale;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sede_legale_id")
    private Indirizzo sedeLegale;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sede_operativa_id")
    private Indirizzo sedeOperativa;

    @Column(name = "iva", nullable = true)
    private String iva;

    @Column(name = "iban", nullable = false)
    private String iban;

    @Column(name = "curriculum")
    private File cv;

    @Column(name = "cartaIdentita")
    private File cartaIdentita;

    @Column(name = "certificato")
    private File certificato;

    // private String aziendaReferente;
    private Boolean stato;

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
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

    public File getCurriculum() {
        return cv;
    }

    public File getCartaIdentita() {
        return cartaIdentita;
    }

    public File getCertificato() {
        return certificato;
    }

    public void setCv(File cv) {
        this.cv = cv;
    }

    public void setCartaIdentita(File cartaIdentita) {
        this.cartaIdentita = cartaIdentita;
    }

    public void setCertificato(File certificato) {
        this.certificato = certificato;
    }

    /*
     * public String getAziendaReferente() {
     * return aziendaReferente;
     * }
     * 
     * public void setAziendaReferente(String aziendaReferente) {
     * this.aziendaReferente = aziendaReferente;
     * }
     */

    public Boolean getStato() {
        return stato;
    }

    public void setStato(Boolean stato) {
        this.stato = stato;
    }

}
