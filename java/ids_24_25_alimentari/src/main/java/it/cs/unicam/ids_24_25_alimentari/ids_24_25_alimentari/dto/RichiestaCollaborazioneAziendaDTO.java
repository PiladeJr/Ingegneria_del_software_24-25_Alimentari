package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto;

import org.springframework.web.multipart.MultipartFile;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import jakarta.validation.Valid;

public class RichiestaCollaborazioneAziendaDTO {
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private Ruolo ruolo;
    private String denSociale;
    @Valid
    private Indirizzo sedeLegale;
    @Valid
    private Indirizzo sedeOperativa;
    private String iban;
    private String iva;
    private MultipartFile certificato;



    private MultipartFile cartaIdentita;

    // Getters e Setters
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

    public String getDenSociale() {
        return denSociale;
    }

    public void setDenSociale(String denSociale) {
        this.denSociale = denSociale;
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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public MultipartFile getCertificato() {
        return certificato;
    }

    public void setCertificato(MultipartFile certificato) { this.certificato = certificato; }
    
    public MultipartFile getCartaIdentita() { return cartaIdentita; }

    public void setCartaIdentita(MultipartFile cartaIdentita) { this.cartaIdentita = cartaIdentita; }
}
