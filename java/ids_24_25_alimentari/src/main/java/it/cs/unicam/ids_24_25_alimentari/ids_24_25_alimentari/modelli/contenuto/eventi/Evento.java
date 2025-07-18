package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_evento", discriminatorType = DiscriminatorType.STRING)
@SequenceGenerator(name = "evento_seq", sequenceName = "evento_sequence", allocationSize = 1)
@NoArgsConstructor
public abstract class Evento extends Contenuto {

    @Column(name = "titolo", nullable = false)
    private String titolo;
    @Column(name = "descrizione", nullable = false)
    private String descrizione;
    @Column(name = "status_evento")
    @Enumerated(EnumType.STRING)
    private StatusEvento statusEvento;
    @Column(name = "inizio", nullable = false)
    private LocalDateTime inizio;
    @Column(name = "fine", nullable = false)
    private LocalDateTime fine;
    @Column(name = "locandina")
    private File locandina;
    @ManyToOne
    @JoinColumn(name = "indirizzo_id", nullable = false)
    private Indirizzo indirizzo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_creatore_id", nullable = false)
    private Utente creatore;
}
