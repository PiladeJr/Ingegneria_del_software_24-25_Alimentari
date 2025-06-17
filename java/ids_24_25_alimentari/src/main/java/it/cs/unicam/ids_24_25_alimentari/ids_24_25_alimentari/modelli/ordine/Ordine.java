package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.Transazione;

import java.security.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ordine")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @Column(name = "totale", nullable = false)
    private double totale;

    @Column(name = "stato", nullable = false)
    private StatoOrdine stato;

    @ManyToOne
    @JoinColumn(name = "indirizzo_consegna_id", nullable = false)
    private Indirizzo indirizzoConsegna;

    @ManyToOne
    @JoinColumn(name = "indirizzo_fatturazione_id", nullable = false)
    private Indirizzo indirizzoFatturazione;

    @Column(name = "data_ordine", nullable = false)
    private Timestamp dataOrdine;

    @Column(name = "data_consegna", nullable = true)
    private LocalDateTime dataConsegna;

    @OneToOne(optional = false)
    @JoinColumn(name = "transazione_id", nullable = true)
    private Transazione transazione;

}
