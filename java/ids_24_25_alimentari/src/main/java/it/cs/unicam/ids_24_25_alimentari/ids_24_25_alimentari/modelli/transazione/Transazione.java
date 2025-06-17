package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transazione")
public class Transazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine Ordine;
    @OneToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente Utente;
    @Column(name = "importo", nullable = false)
    private double importo;
    @Column(name = "data_transazione", nullable = false)
    private String metodoPagamento;
    @Column(name = "stato_transazione", nullable = false)
    private StatoTransazione statoTransazione;

}
