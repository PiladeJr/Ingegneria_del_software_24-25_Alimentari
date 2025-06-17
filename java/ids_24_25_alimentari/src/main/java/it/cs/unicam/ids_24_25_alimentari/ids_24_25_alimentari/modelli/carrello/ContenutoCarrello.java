package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.*;

@Entity
@Getter
@Setter
@Table(name = "contenuto_carrello")
public class ContenutoCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prezzo", nullable = false)
    private double totale;
    @Column(name = "prezzo_scontato", nullable = true)
    private double prezzoScontato;
    @Column(name = "quantita", nullable = false)
    private int quantita;

    @OneToOne
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodotto;

    public ContenutoCarrello(Prodotto prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
        this.totale = prodotto.getPrezzo();
    }
}
