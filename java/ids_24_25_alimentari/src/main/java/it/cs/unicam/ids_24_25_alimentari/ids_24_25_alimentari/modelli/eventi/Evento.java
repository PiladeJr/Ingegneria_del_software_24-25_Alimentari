package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(
        name = "evento_seq",
        sequenceName = "evento_sequence",
        allocationSize = 1)
@NoArgsConstructor
public abstract class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_seq")
    private long id;
    @Column(name = "titolo", nullable = false)
    private String titolo;
    @Column(name = "descrizione", nullable = false)
    private String descrizione;
    @Column(name = "tipologia", nullable = false)
    private TipologiaEvento tipologia;
    @Column(name = "status")
    private StatusEvento status;
    @Column(name = "inizio", nullable = false)
    private LocalDateTime inizio;
    @Column(name = "fine", nullable = false)
    private LocalDateTime fine;
    @Column(name = "locandina")
    private File locandina;
    @OneToOne(cascade = CascadeType.ALL)
    private Indirizzo indirizzo;
}
