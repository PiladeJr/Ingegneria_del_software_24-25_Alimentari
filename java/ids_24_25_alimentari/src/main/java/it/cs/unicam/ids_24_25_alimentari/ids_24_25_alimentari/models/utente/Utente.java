package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utente")
@NoArgsConstructor
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name= "nome", nullable = false)
    @NotEmpty (message = "il nome è obbligatorio")
    private String nome;
    @Column(name= "cognome", nullable = false)
    @NotEmpty (message = "il cognome è obbligatorio")
    private String cognome;
    @Email(message = "Email non valida")
    @NotEmpty(message = "L'email è obbligatoria")
    private String email;
    @Column(name= "password", nullable = false)
    @NotEmpty(message = "La password è obbligatoria")
    private String password;
    @Column(name= "telefono")
    @Pattern(regexp = "^\\d{10}$", message = "Il numero di telefono deve contenere esattamente 10 cifre")
    private String telefono;
    @Enumerated(EnumType.STRING)
    @Column(name= "ruolo", nullable = false)
    private Ruolo ruolo;
    @Column(name = "iban")
    @Size(min = 15, max = 34, message = "L'IBAN deve avere una lunghezza compresa tra 15 e 34 caratteri")
    @NotEmpty(message = "L'IBAN è obbligatorio")
    private String iban;

    @Column(name = "curriculum")
    private File cv;

    @Column(name = "cartaIdentita")
    private File cartaIdentita;

    @Column(nullable = true)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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

   /* public List<Ruolo> getRuoliDisponibili() {
        return Arrays.asList(Ruolo.values());
    }*/
    public Utente(String nome, String cognome, String email, String password, String telefono) {

        this.nome = nome;

        this.cognome = cognome;

        this.email = email;

        this.password = password;

        this.telefono = telefono;

    }
}
