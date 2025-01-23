package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models;

import java.io.File;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Utente.Ruolo;
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
    private int telefono;
    private String email;
    private Ruolo ruolo;
    private String denominazioneSociale;
    private String sedeLegale;
    private String sedeOperativa;
    private String coordinate;

    @Column(name = "iva", nullable = false)
    private String iva;

    @Column(name = "iban", nullable = false)
    private String iban;

    @Column(name = "curriculum")
    private File cv;

    @Column(name = "cartaIdentita")
    private File cartaIdentita;

    @Column(name = "certificato")
    private File certificato;

    private String aziendaReferente;
    private boolean stato;

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

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
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

    public String getSedeLegale() {
        return sedeLegale;
    }

    public void setSedeLegale(String sedeLegale) {
        this.sedeLegale = sedeLegale;
    }

    public String getSedeOperativa() {
        return sedeOperativa;
    }

    public void setSedeOperativa(String sedeOperativa) {
        this.sedeOperativa = sedeOperativa;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
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

    public String getAziendaReferente() {
        return aziendaReferente;
    }

    public void setAziendaReferente(String aziendaReferente) {
        this.aziendaReferente = aziendaReferente;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

}
