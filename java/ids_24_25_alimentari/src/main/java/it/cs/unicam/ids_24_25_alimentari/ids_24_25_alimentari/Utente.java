package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import jakarta.persistence.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "utente")

public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String telefono;
    private Ruolo ruolo;

    @Column(name = "iban", nullable = false)
    private String iban;

    @Column(name = "curriculum")
    private File cv;

    @Column(name = "cartaIdentita")
    private File cartaIdentita;

    private long idAzienda;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo idRuolo) {
        this.ruolo = idRuolo;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public File getCv() {
        return cv;
    }

    public void setCv(File cv) {
        this.cv = cv;
    }

    public File getCartaIdentita() {
        return cartaIdentita;
    }

    public void setCartaIdentita(File cartaIdentita) {
        this.cartaIdentita = cartaIdentita;
    }

    public long getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(long idAzienda) {
        this.idAzienda = idAzienda;
    }

    public List<Ruolo> getRuoliDisponibili() {
        return Arrays.asList(Ruolo.values());
    }

}
