package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "carrello")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "data_creazione", nullable = false)
    private Timestamp data_creazione;

    @OneToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToMany
    @JoinTable(name = "carrelo_contenuto_carrello", joinColumns = @JoinColumn(name = "carrello_id"), inverseJoinColumns = @JoinColumn(name = "contenuto_carrello_id"))
    private Set<ContenutoCarrello> contenutoCarrello = new HashSet<>();

}
