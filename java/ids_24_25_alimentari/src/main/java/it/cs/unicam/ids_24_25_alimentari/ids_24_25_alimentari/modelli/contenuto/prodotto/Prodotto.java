package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "prodotto")
@AllArgsConstructor
@NoArgsConstructor
public abstract class Prodotto extends Contenuto {

    @Column(name = "nome")
    protected String nome;

    @Column(name = "descrizione")
    protected String descrizione;

    @Column(name = "prezzo")
    protected Double prezzo;

    public abstract Double getPrezzo();

    public abstract TipoProdotto getTipo();

}
