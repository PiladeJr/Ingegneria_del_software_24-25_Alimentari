package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pacchetto")
public class Pacchetto extends Prodotto{

    @ManyToMany
    @JoinTable(
            name = "pacchetto_prodotto",
            joinColumns = @JoinColumn(name = "pacchetto_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private Set<ProdottoSingolo> prodotti = new HashSet<>();

    public void aggiungiProdotto(ProdottoSingolo prodotto) {
        prodotti.add(prodotto);
    }

    public void rimuoviProdotto(ProdottoSingolo prodotto) {
        prodotti.remove(prodotto);
    }

    @Override
    public Double getPrezzo() {
        double totale = 0.0;
        for (ProdottoSingolo prodotto : prodotti) {
            totale += prodotto.getPrezzo();
        }
        return totale;
    }

    @Override
    TipoProdotto getTipo() {
        return TipoProdotto.PACCHETTO;
    }


}