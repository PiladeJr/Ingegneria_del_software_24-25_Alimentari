package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto;

import org.springframework.web.multipart.MultipartFile;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;

public class RichiestaCollaborazioneCuratoreDTO {
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private Ruolo ruolo;
    private String iban;
    private MultipartFile cartaIdentita;
    private MultipartFile cv;

    public RichiestaCollaborazioneCuratoreDTO() {
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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public MultipartFile getCartaIdentita() {
        return cartaIdentita;
    }

    public void setCartaIdentita(MultipartFile cartaIdentita) {
        this.cartaIdentita = cartaIdentita;
    }

    public MultipartFile getCv() {
        return cv;
    }

    public void setCv(MultipartFile cv) {
        this.cv = cv;
    }

}
