package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.File;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utente")
@NoArgsConstructor
@Getter
@Setter
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "nome", nullable = false)
    @NotEmpty(message = "il nome è obbligatorio")
    private String nome;
    @Column(name = "cognome", nullable = false)
    @NotEmpty(message = "il cognome è obbligatorio")
    private String cognome;
    @Email(message = "Email non valida")
    @NotEmpty(message = "L'email è obbligatoria")
    private String email;
    @Column(name = "password", nullable = false)
    @NotEmpty(message = "La password è obbligatoria")
    private String password;
    @Column(name = "telefono")
    @Pattern(regexp = "^\\d{10}$", message = "Il numero di telefono deve contenere esattamente 10 cifre")
    private String telefono;
    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo", nullable = false)
    private Ruolo ruolo;
    @ManyToOne()
    @JoinColumn(name = "indirizzo_fatturazione_id")
    private Indirizzo indirizzoFatturazione;
    @ManyToOne()
    @JoinColumn(name = "indirizzo_spedizione_id")
    private Indirizzo IndirizzoSpedizione;
    @Column(name = "iban")
    @Size(min = 15, max = 34, message = "L'IBAN deve avere una lunghezza compresa tra 15 e 34 caratteri")
    @NotEmpty(message = "L'IBAN è obbligatorio")
    private String iban;

    @Column(name = "curriculum")
    private File cv;

    @Column(name = "cartaIdentita")
    private File cartaIdentita;

    @OneToOne(mappedBy = "utente", cascade = CascadeType.MERGE)
    private Azienda azienda;

    @ManyToMany
    @JoinTable(
            name = "utente_aziende",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "azienda_id")
    )
    private List<Azienda> aziendeCollegate;

    /**
     * Restituisce l'elenco delle autorità (ruoli) assegnate all'utente.
     * <p>
     * Questo metodo implementa l'interfaccia {@code GrantedAuthority} di Spring
     * Security,
     * restituendo una lista contenente il ruolo dell'utente con il prefisso
     * "ROLE_".
     * </p>
     *
     * @return Una collezione di {@code GrantedAuthority} contenente il ruolo
     *         dell'utente.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name()));
    }

    /**
     * salva nell'oggetto Utente la password dopo averla crittografata
     * mediante algoritmo BCrypt
     *
     * @param hashedPassword la password da crittografare e salvare
     */

    public void setPassword(String hashedPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pass = passwordEncoder.encode(hashedPassword);
        this.password = pass;
    }

    @Override
    public String getUsername() {
        return email; // Use email as username
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *Costruttore parziale dell'oggetto Utente
     * utillizzato per inizializzazione del RepositoryConstructor
     */
    public Utente(String nome, String cognome, String email, String password, String telefono) {

        this.nome = nome;

        this.cognome = cognome;

        this.email = email;

        this.password = password;

        this.telefono = telefono;

    }
}
