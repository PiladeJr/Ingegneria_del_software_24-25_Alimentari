package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UtenteRegistrazioneDTO {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String nome;
    private String cognome;
    private String telefono;

    public String getEmail() {
        return email;
    }

    public UtenteRegistrazioneDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UtenteRegistrazioneDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public UtenteRegistrazioneDTO setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getCognome() {
        return cognome;
    }

    public UtenteRegistrazioneDTO setCognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public String getTelefono() {
        return telefono;
    }

    public UtenteRegistrazioneDTO setTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

}
